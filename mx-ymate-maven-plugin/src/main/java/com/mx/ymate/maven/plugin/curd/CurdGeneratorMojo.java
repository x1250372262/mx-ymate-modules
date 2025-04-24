package com.mx.ymate.maven.plugin.curd;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.maven.plugin.BaseGeneratorMojo;
import com.mx.ymate.maven.plugin.enums.CurdEnums;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import net.ymate.platform.core.persistence.annotation.Comment;
import net.ymate.platform.core.persistence.annotation.Id;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.*;

import static com.mx.ymate.maven.plugin.constant.ConfigConstant.CURD_MODEL_CLASS;
import static com.mx.ymate.maven.plugin.constant.FtlTemplatesConstant.*;
import static com.mx.ymate.maven.plugin.constant.MapConstant.*;
import static org.fusesource.jansi.Ansi.Color.GREEN;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: curd代码生成
 */
@Mojo(name = "curd")
public class CurdGeneratorMojo extends BaseGeneratorMojo {


    private final Configuration freemarkerConfig;
    private final CurdConfig curdConfig;

    /**
     * 排除字段
     */
    private final List<String> excludeFieldLists = Arrays.asList("dbOwner", "entityClass", "connectionHolder", "shardingable"
            , "dataSourceName", "TABLE_NAME", "serialVersionUID", "__entityClass", "__connectionHolder", "__shardingable", "__dsName");

    private final List<String> datetimeFields = Arrays.asList("createTime", "lastModifyTime", "modifyTime", "updateTime");
    private final List<String> usersFields = Arrays.asList("createUser", "lastModifyUser", "modifyUser", "updateUser");


    private final Log log = getLog();

    public CurdGeneratorMojo() {
        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_31);
        freemarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        freemarkerConfig.setClassForTemplateLoading(CurdGeneratorMojo.class, "/");
        freemarkerConfig.setDefaultEncoding(Constants.DEFAULT_CHARSET);
        Props props = getProps();
        curdConfig = new CurdConfig(props);
    }

    private ClassLoader getClassLoader() {
        try {
            // 所有的类路径环境，也可以直接用 compilePath
            List<String> classpathElements = new ArrayList<>();
            String projectPath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes";
            classpathElements.add(projectPath);
            // 转为 URL 数组
            URL[] urls = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File(classpathElements.get(i)).toURI().toURL();
            }
            // 自定义类加载器
            return new URLClassLoader(urls, this.getClass().getClassLoader());
        } catch (Exception e) {
            log.error("classLoader加载失败", e);
            return null;
        }
    }

    private Class<?> getCurdClass(ClassLoader classLoader, String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            log.error("实体类加载失败: " + className, e);
            throw new RuntimeException(e);
        }
    }


    private List<ModelField> getDtoFields(Class<?> modelClass) {
        List<ModelField> result = new ArrayList<>();
        Field[] fields = ClassUtil.getDeclaredFields(modelClass);
        for (Field field : fields) {
            String name = field.getName();
            if (excludeFieldLists.contains(name) || datetimeFields.contains(name) || usersFields.contains(name)) {
                continue;
            }
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                continue;
            }
            Comment comment = field.getAnnotation(Comment.class);
            String commentStr = "";
            if (comment != null) {
                commentStr = comment.value();
            }
            ModelField modelField = new ModelField(field.getType().getSimpleName(), name, commentStr, StrUtil.toUnderlineCase(name).toUpperCase());
            result.add(modelField);
        }
        return result;
    }

    private List<ModelField> getVoFields(Class<?> modelClass) {
        List<ModelField> result = new ArrayList<>();
        Field[] fields = ClassUtil.getDeclaredFields(modelClass);
        for (Field field : fields) {
            String name = field.getName();
            if (excludeFieldLists.contains(name) || datetimeFields.contains(name) || usersFields.contains(name)) {
                continue;
            }
            Comment comment = field.getAnnotation(Comment.class);
            String commentStr = "";
            if (comment != null) {
                commentStr = comment.value();
            }
            ModelField modelField = new ModelField(field.getType().getSimpleName(), name, commentStr, StrUtil.toUnderlineCase(name).toUpperCase());
            result.add(modelField);
        }
        return result;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ClassLoader classLoader = getClassLoader();
        if (classLoader == null) {
            log.error("classLoader加载失败");
            throw new RuntimeException("classLoader加载失败");
        }
        if (StringUtils.isBlank(curdConfig.getModelClass())) {
            log.error(CURD_MODEL_CLASS + "参数为空");
            throw new RuntimeException(CURD_MODEL_CLASS + "参数为空");
        }
        Class<?> modelClass = getCurdClass(classLoader, curdConfig.getModelClass());
        if (modelClass == null) {
            log.error("实体类加载失败: " + curdConfig.getModelClass());
            throw new RuntimeException("实体类加载失败");
        }
        if (StringUtils.isBlank(curdConfig.getFilePath())) {
            log.error("请指定文件输出目录");
            throw new RuntimeException("请指定文件输出目录");
        }
        Map<String, Object> propMap = buildPropMap();
        //实体类包名
        propMap.put(MODEL_PACKAGE_NAME, modelClass.getPackage().getName());
        //实体类名称
        propMap.put(MODEL_NAME, modelClass.getSimpleName());
        //dao包名
        propMap.put(DAO_PACKAGE_NAME, curdConfig.getDaoPackageName());
        //service包名
        propMap.put(SERVICE_PACKAGE_NAME, curdConfig.getServicePackageName());
        //实体类名称全大写
        propMap.put(MODEL_NAME_UP,  StrUtil.toUnderlineCase(modelClass.getSimpleName()).toUpperCase());
        //dto字段集合
        propMap.put(DTO_FIELD_LIST, getDtoFields(modelClass));
        //vo字段集合
        propMap.put(VO_FIELD_LIST, getVoFields(modelClass));
        //controller包名
        propMap.put(CONTROLLER_PACKAGE_NAME, curdConfig.getControllerPackageName());
        String type = curdConfig.getType();
        if (type.contains(CurdEnums.TypeEnum.ALL.getValue())) {
            createAll(propMap, modelClass.getSimpleName());
            return;
        }
        String[] typeArray = type.split("\\|");
        for (String item : typeArray) {
            create(CurdEnums.TypeEnum.fromValue(item), propMap, modelClass.getSimpleName());
        }
    }


    private void createAll(Map<String, Object> propMap, String modelName) {
        createDao(propMap, modelName);
        createService(propMap, modelName);
        createController(propMap, modelName);
        createPage(propMap, modelName);
    }

    private void create(CurdEnums.TypeEnum typeEnum, Map<String, Object> propMap, String modelName) {
        switch (typeEnum) {
            case ALL:
                createAll(propMap, modelName);
                break;
            case DAO:
                createDao(propMap, modelName);
                break;
            case SERVICE:
                createService(propMap, modelName);
                break;
            case CONTROLLER:
                createController(propMap, modelName);
                break;
            case PAGE:
                createPage(propMap, modelName);
                break;
            default:
                log.error("curd类型错误");
                throw new RuntimeException("curd类型错误");
        }
    }


    /**
     * 创建dao和impl
     *
     * @param propMap   参数map
     * @param modelName 实体类名称
     */
    private void createDao(Map<String, Object> propMap, String modelName) {
        String daoPackageName = curdConfig.getDaoPackageName();
        if (StringUtils.isBlank(daoPackageName)) {
            log.error("请先设置dao包名");
            throw new RuntimeException("请先设置dao包名");
        }
        outFile(modelName, createDaoFileName(modelName), DAO_JAVA, propMap);
        outFile(modelName, createDaoImplFileName(modelName), DAO_IMPL_JAVA, propMap);
    }

    /**
     * 创建service和impl
     *
     * @param propMap   参数map
     * @param modelName 实体类名称
     */
    private void createService(Map<String, Object> propMap, String modelName) {
        String servicePackageName = curdConfig.getServicePackageName();
        if (StringUtils.isBlank(servicePackageName)) {
            log.error("请先设置service包名");
            throw new RuntimeException("请先设置service包名");
        }
        outFile(modelName, createServiceFileName(modelName), SERVICE_JAVA, propMap);
        outFile(modelName, createServiceImplFileName(modelName), SERVICE_IMPL_JAVA, propMap);
    }

    /**
     * 创建controller
     *
     * @param propMap   参数map
     * @param modelName 实体类名称
     */
    private void createController(Map<String, Object> propMap, String modelName) {
        String controllerPackageName = curdConfig.getServicePackageName();
        if (StringUtils.isBlank(controllerPackageName)) {
            log.error("请先设置controller包名");
            throw new RuntimeException("请先设置controller包名");
        }
        outFile(modelName, createControllerFileName(modelName), CONTROLLER_JAVA, propMap);
    }

    /**
     * 创建页面
     *
     * @param propMap   参数map
     * @param modelName 实体类名称
     */
    private void createPage(Map<String, Object> propMap, String modelName) {
        outFile(modelName, createPageHtmlFileName(modelName), PAGE_HTML, propMap);
        outFile(modelName, createPageJsFileName(modelName), PAGE_JS, propMap);
    }


    /**
     * 输出文件
     *
     * @param entityName     实体类名称
     * @param targetFileName 文件名称
     * @param tmplFile       模板名称
     * @param propMap        参数
     */
    private void outFile(String entityName, String targetFileName, String tmplFile, Map<String, Object> propMap) {
        Writer outWriter = null;
        try {
            //文件目录/实体类名称/文件
            String outPath = curdConfig.getFilePath() + File.separator + entityName;
            File outputFile = new File(outPath, targetFileName);
            File path = outputFile.getParentFile();
            if (!path.exists()) {
                FileUtil.mkdir(path);
            }
            Template template = freemarkerConfig.getTemplate(tmplFile);
            outWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile.toPath()), StringUtils.defaultIfEmpty(freemarkerConfig.getOutputEncoding(), freemarkerConfig.getDefaultEncoding())));
            template.process(propMap, outWriter);
            out(GREEN, "输出路径" + outputFile);
        } catch (Exception e) {
            log.error("输出文件异常", e);
        } finally {
            if (outWriter != null) {
                try {
                    outWriter.flush();
                    outWriter.close();
                } catch (IOException e) {
                    log.error("关闭输出流异常", e);
                }
            }
        }
    }


    /**
     * 通用参数
     *
     * @return 通用参数map
     */
    private Map<String, Object> buildPropMap() {
        Map<String, Object> propMap = new HashMap<>();
        //dao报名
        propMap.put(DAO_PACKAGE_NAME, curdConfig.getDaoPackageName());
        //#
        propMap.put(BR, "<br/>");
        propMap.put(JING, "#");
        //<
        propMap.put(LEFT, "<");
        //>
        propMap.put(RIGHT, ">");
        return propMap;
    }
}
