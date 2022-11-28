//package com.mx.ymate.maven.generator;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.setting.dialect.Props;
//import com.alibaba.fastjson.JSONObject;
//import com.mx.maven.bean.Attr;
//import com.mx.maven.conf.CurdConfig;
//import com.mx.maven.util.PropUtils;
//import com.mx.spring.dev.constants.Constants;
//import com.mx.spring.dev.exception.MxException;
//import com.mx.spring.dev.support.generator.annotation.FieldInfo;
//import com.mx.spring.dev.support.log.MxLog;
//import com.mx.ymate.dev.constants.Constants;
//import com.mx.ymate.maven.conf.CurdConfig;
//import com.mx.ymate.maven.util.PropUtils;
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateExceptionHandler;
//import freemarker.template.utility.NullArgumentException;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.maven.plugin.MojoExecutionException;
//import org.apache.maven.plugin.MojoFailureException;
//import org.apache.maven.plugins.annotations.Mojo;
//
//import java.io.*;
//import java.lang.reflect.Field;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.nio.file.Files;
//import java.util.*;
//
//import static com.mx.maven.constants.FileNameConstant.JAVA_FILE_SUFFIX;
//import static com.mx.maven.constants.FtlTemplatesConstant.*;
//import static com.mx.maven.constants.MapConstant.*;
//import static com.mx.maven.constants.MapConstant.Html.*;
//import static com.mx.ymate.maven.constants.MapConstant.LIST_PARAMS_LIST;
//import static org.fusesource.jansi.Ansi.Color.YELLOW;
//
///**
// * @Author: mengxiang.
// * @create: 2021-09-30 09:21
// * @Description: curd代码生成
// */
//@Mojo(name = "curd")
//public class CurdGeneratorMojo extends BaseGeneratorMojo {
//
//
//    private final Configuration freemarkerConfig;
//    private final CurdConfig curdConfig;
//
//    public CurdGeneratorMojo() {
//        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_31);
//        freemarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
//        freemarkerConfig.setClassForTemplateLoading(CurdGeneratorMojo.class, "/");
//        freemarkerConfig.setDefaultEncoding(Constants.DEFAULT_CHARSET);
//        Props props = PropUtils.getProps();
//        curdConfig = CurdConfig.me().init(props);
//    }
//
//    private ClassLoader getClassLoader() {
//        try {
//            // 所有的类路径环境，也可以直接用 compilePath
//            List<String> classpathElements = new ArrayList<>();
//            classpathElements.add(curdConfig.getEntityPath());
//            classpathElements.add(curdConfig.getMapperPath());
//            // 转为 URL 数组
//            URL[] urls = new URL[classpathElements.size()];
//            for (int i = 0; i < classpathElements.size(); ++i) {
//                urls[i] = new File(classpathElements.get(i)).toURI().toURL();
//            }
//            // 自定义类加载器
//            return new URLClassLoader(urls, this.getClass().getClassLoader());
//        } catch (Exception e) {
//            getLog().debug("Couldn't get the classloader.");
//            return this.getClass().getClassLoader();
//        }
//    }
//
//    @Override
//    public void execute() throws MojoExecutionException, MojoFailureException {
//        if (StringUtils.isBlank(curdConfig.getEntityClass())) {
//            throw new NullArgumentException(PropUtils.CURD_ENTITY_CLASS);
//        }
//        if (StringUtils.isBlank(curdConfig.getMapperClass())) {
//            throw new NullArgumentException(PropUtils.CURD_MAPPER_CLASS);
//        }
//
//        Class<?> entityClass = null;
//        Class<?> mapperClass = null;
//        try {
//            entityClass = getClassLoader().loadClass(curdConfig.getEntityClass());
//            mapperClass = getClassLoader().loadClass(curdConfig.getMapperClass());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (entityClass == null || mapperClass == null) {
//            throw new RuntimeException("实体类或mapper加载失败");
//        }
//        Map<String, Object> propMap = buildPropMap();
//        //实体类包名
//        propMap.put(MODEL_PACKAGE_NAME, entityClass.getPackage().getName());
//        //mapper包名
//        propMap.put(MAPPER_PACKAGE_NAME, mapperClass.getPackage().getName());
//        //实体类名称
//        propMap.put(MODEL_NAME, entityClass.getSimpleName());
//        //mapper名称
//        propMap.put(MAPPER_NAME, mapperClass.getSimpleName());
//        Map<String, Attr> attrMap = new HashMap<>();
//        Field[] fields = entityClass.getDeclaredFields();
//
//        for (Field field : fields) {
//            FieldInfo fieldInfo = field.getAnnotation(FieldInfo.class);
//            String comment = "";
//            boolean nullable = true;
//            if (fieldInfo != null) {
//                comment = fieldInfo.comment();
//                nullable = fieldInfo.nullable();
//            }
//            attrMap.put(field.getName(), new Attr(field.getType().getSimpleName(), field.getName(), null, null, comment, nullable));
//        }
//        //输出service
//        //输出serviceImpl
//        if (curdConfig.isCreateService()) {
//            outService(propMap, attrMap, entityClass.getSimpleName());
//        }
//        //输出controller
//        if (curdConfig.isCreateController()) {
//            outController(propMap, entityClass.getSimpleName());
//        }
//        //输出dto bean
//        outDTO(propMap, attrMap, entityClass.getSimpleName());
//        outListDTO(propMap, attrMap, entityClass.getSimpleName());
//        //输出vo
//        outVO(propMap, attrMap, entityClass.getSimpleName());
//        outListVO(propMap, attrMap, entityClass.getSimpleName());
//        //输出page
//        if (curdConfig.isCreatePage()) {
//            outPage(propMap, attrMap, entityClass.getSimpleName());
//        }
//    }
//
//    /**
//     * 输出vo
//     *
//     * @param propMap   参数map
//     * @param attrMap   属性map
//     * @param modelName 实体类名称
//     */
//    private void outVO(Map<String, Object> propMap, Map<String, Attr> attrMap, String modelName) {
//
//        String voProjectPath = curdConfig.getVoProjectPath();
//        String voName = curdConfig.getVoName();
//        String detailReturns = curdConfig.getDetailReturns();
//        if (StrUtil.hasBlank(voProjectPath, voName)) {
//            throw new MxException("请检查vo设置");
//        }
//        String[] voFields = StrUtil.splitToArray(detailReturns, "|");
//        List<Attr> fieldsList = new ArrayList<>();
//        if(voFields != null && voFields.length > 0){
//            for (String field : voFields) {
//                fieldsList.add(attrMap.get(field));
//            }
//        }else{
//            fieldsList.addAll(attrMap.values());
//        }
//
//        propMap.put(FIELDS_LIST, fieldsList);
//        propMap.put(LIST, false);
//        outFile(voProjectPath, createVoFileName(modelName), VO_JAVA, propMap, voName);
//    }
//
//    /**
//     * 输出listvo
//     *
//     * @param propMap   参数map
//     * @param attrMap   属性map
//     * @param modelName 实体类名称
//     */
//    private void outListVO(Map<String, Object> propMap, Map<String, Attr> attrMap, String modelName) {
//        String voProjectPath = curdConfig.getVoProjectPath();
//        String voName = curdConfig.getVoName();
//        String listReturns = curdConfig.getListReturns();
//        if (StrUtil.hasBlank(voProjectPath, voName)) {
//            throw new RuntimeException("请检查vo设置");
//        }
//        String[] voFields = StrUtil.splitToArray(listReturns, "|");
//        List<Attr> fieldsList = new ArrayList<>();
//        if(voFields != null && voFields.length > 0){
//            for (String field : voFields) {
//                fieldsList.add(attrMap.get(field));
//            }
//        }else{
//            fieldsList.addAll(attrMap.values());
//        }
//        propMap.put(FIELDS_LIST, fieldsList);
//        propMap.put(LIST, true);
//        outFile(curdConfig.getVoProjectPath(), createVoListFileName(modelName), VO_JAVA, propMap, curdConfig.getVoName());
//    }
//
//    /**
//     * 输出dto和bean
//     *
//     * @param propMap   参数map
//     * @param attrMap   属性map
//     * @param modelName 实体类名称
//     */
//    private void outDTO(Map<String, Object> propMap, Map<String, Attr> attrMap, String modelName) {
//        String dtoProjectPath = curdConfig.getDtoProjectPath();
//        String dtoName = curdConfig.getDtoName();
//        String beanProjectPath = curdConfig.getBeanProjectPath();
//        String beanName = curdConfig.getBeanName();
//        String optionParams = curdConfig.getOptionParams();
//
//        if (StrUtil.hasBlank(dtoProjectPath, dtoName, beanProjectPath, beanName)) {
//            throw new MxException("请检查dto和bean设置");
//        }
//        String[] dtoFields = StrUtil.splitToArray(optionParams, "|");
//        List<Attr> fieldsList = new ArrayList<>();
//        if(dtoFields != null && dtoFields.length > 0){
//            for (String field : dtoFields) {
//                fieldsList.add(attrMap.get(field));
//            }
//        }else{
//            fieldsList.addAll(attrMap.values());
//        }
//        propMap.put(FIELDS_LIST, fieldsList);
//        propMap.put(LIST, false);
//        outFile(dtoProjectPath, createDtoFileName(modelName), DTO_JAVA, propMap, dtoName);
//        outFile(beanProjectPath, createBeanFileName(modelName), BEAN_JAVA, propMap, beanName);
//    }
//
//    /**
//     * 输出listDto和bean
//     *
//     * @param propMap   参数map
//     * @param attrMap   属性map
//     * @param modelName 实体类名称
//     */
//    private void outListDTO(Map<String, Object> propMap, Map<String, Attr> attrMap, String modelName) {
//        String dtoProjectPath = curdConfig.getDtoProjectPath();
//        String dtoName = curdConfig.getDtoName();
//        String beanProjectPath = curdConfig.getBeanProjectPath();
//        String beanName = curdConfig.getBeanName();
//        String listParams = curdConfig.getListParams();
//        if (StrUtil.hasBlank(dtoProjectPath, dtoName, beanName, beanProjectPath)) {
//            throw new MxException("请检查dto和bean设置");
//        }
//        String[] dtoFields = StrUtil.splitToArray(listParams, "|");
//        List<Attr> fieldsList = new ArrayList<>();
//        if(dtoFields != null && dtoFields.length > 0){
//            for (String field : dtoFields) {
//                fieldsList.add(attrMap.get(field));
//            }
//        }else{
//            fieldsList.addAll(attrMap.values());
//        }
//        propMap.put(FIELDS_LIST, fieldsList);
//        propMap.put(LIST, true);
//        outFile(dtoProjectPath, createDtoListFileName(modelName), DTO_LIST_JAVA, propMap, dtoName);
//        outFile(beanProjectPath, createBeanListFileName(modelName), BEAN_JAVA, propMap, beanName);
//    }
//
//    /**
//     * 输出service和impl
//     *
//     * @param propMap   参数map
//     * @param attrMap   属性map
//     * @param modelName 实体类名称
//     */
//    private void outService(Map<String, Object> propMap, Map<String, Attr> attrMap, String modelName) {
//        String servicePath = curdConfig.getServiceProjectPath();
//        String serviceName = curdConfig.getServiceName();
//        if (StringUtils.isBlank(servicePath) || StringUtils.isBlank(serviceName)) {
//            throw new MxException("请检查service设置");
//        }
//        String[] listParams = StrUtil.splitToArray(curdConfig.getListParams(), "|");
//        List<Attr> listParamsList = new ArrayList<>();
//        for (String field : listParams) {
//            listParamsList.add(attrMap.get(field));
//        }
//        propMap.put(LIST_PARAMS_LIST, listParamsList);
//        outFile(servicePath, createServiceFileName(modelName), SERVICE_JAVA, propMap, serviceName);
//        outFile(servicePath, createServiceImplFileName(modelName), SERVICE_IMPL_JAVA, propMap, serviceName);
//    }
//
//    /**
//     * 输出controller
//     *
//     * @param propMap   参数map
//     * @param modelName 属性map
//     */
//    private void outController(Map<String, Object> propMap, String modelName) {
//        String controllerName = curdConfig.getControllerName();
//        String controllerProjectPath = curdConfig.getControllerProjectPath();
//        if (StringUtils.isBlank(controllerName) || StringUtils.isBlank(controllerProjectPath)) {
//            throw new MxException("请检查controller设置");
//        }
//        outFile(controllerProjectPath, modelName + "Controller.java", "/generator/controller.ftl", propMap, controllerName);
//    }
//
//    /**
//     * 输出页面
//     *
//     * @param propMap   参数map
//     * @param attrMap   属性map
//     * @param modelName 实体类名称
//     */
//    private void outPage(Map<String, Object> propMap, Map<String, Attr> attrMap, String modelName) {
//        String pageProjectPath = curdConfig.getPageProjectPath();
//        if (StringUtils.isBlank(pageProjectPath)) {
//            throw new MxException("请检查page设置");
//        }
//        String[] listReturns = StrUtil.splitToArray(curdConfig.getListReturns(), "|");
//        String[] listParams = StrUtil.splitToArray(curdConfig.getListParams(), "|");
//        String[] operationParams = StrUtil.splitToArray(curdConfig.getOptionParams(), "|");
//        List<Attr> listReturnList = new ArrayList<>();
//        List<Attr> listParamList = new ArrayList<>();
//        List<Attr> operationParamList = new ArrayList<>();
//        if(listReturns != null && listReturns.length > 0){
//            for (String field : listReturns) {
//                listReturnList.add(attrMap.get(field));
//            }
//        }else{
//            listReturnList.addAll(attrMap.values());
//        }
//        if(listParams != null && listParams.length > 0){
//            for (String field : listParams) {
//                listParamList.add(attrMap.get(field));
//            }
//        }else{
//            listParamList.addAll(attrMap.values());
//        }
//        if(operationParams != null && operationParams.length > 0){
//            for (String field : operationParams) {
//                operationParamList.add(attrMap.get(field));
//            }
//        }else{
//            operationParamList.addAll(attrMap.values());
//        }
//
//        String xg = StrUtil.SLASH;
//        String xh = StrUtil.UNDERLINE;
//        modelName = modelName.substring(0, 1).toLowerCase() + modelName.substring(1);
//        propMap.put(JS_URL, curdConfig.getJsUrl().concat(xg).concat(modelName + ".js"));
//        propMap.put(LIST_RETURN_LIST, listReturnList);
//        propMap.put(LIST_PARAM_LIST, listParamList);
//        propMap.put(OPERATION_PARAM_LIST, operationParamList);
//        propMap.put(LIST_URL, modelName.concat(xg).concat(PAGE_LIST));
//        propMap.put(DETAIL_URL, modelName.concat(xg).concat(PAGE_DETAIL));
//        propMap.put(CREATE_URL, modelName.concat(xg).concat(PAGE_CREATE));
//        propMap.put(UPDATE_URL, modelName.concat(xg).concat(PAGE_UPDATE));
//        propMap.put(DELETE_URL, modelName.concat(xg).concat(PAGE_DELETE));
//        propMap.put(LIST_URL_KEY, modelName.toUpperCase(Locale.ROOT).concat(xh).concat(PAGE_LIST_KEY));
//        propMap.put(DETAIL_URL_KEY, modelName.toUpperCase(Locale.ROOT).concat(xh).concat(PAGE_DETAIL_KEY));
//        propMap.put(CREATE_URL_KEY, modelName.toUpperCase(Locale.ROOT).concat(xh).concat(PAGE_CREATE_KEY));
//        propMap.put(UPDATE_URL_KEY, modelName.toUpperCase(Locale.ROOT).concat(xh).concat(PAGE_UPDATE_KEY));
//        propMap.put(DELETE_URL_KEY, modelName.toUpperCase(Locale.ROOT).concat(xh).concat(PAGE_DELETE_KEY));
//        outFile(pageProjectPath, createPageHtmlFileName(modelName), PAGE_HTML, propMap, null);
//        outFile(curdConfig.getPageJsPath(), createPageJsFileName(modelName), PAGE_JS, propMap, null);
//    }
//
//
//    /**
//     * 输出文件
//     *
//     * @param projectPath    项目根目录
//     * @param targetFileName 文件名称
//     * @param tmplFile       模板名称
//     * @param propMap        参数
//     * @param packageName    包名
//     */
//    private void outFile(String projectPath, String targetFileName, String tmplFile, Map<String, Object> propMap, String packageName) {
//        Writer outWriter = null;
//        try {
//            String outPath = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "java";
//            File outputFile;
//            if (targetFileName.endsWith(JAVA_FILE_SUFFIX)) {
//                outputFile = new File(outPath, new File(packageName.replace(StrUtil.DOT, StrUtil.SLASH), targetFileName).getPath());
//            } else {
//                outputFile = new File(projectPath, targetFileName);
//            }
//            File path = outputFile.getParentFile();
//            if (!path.exists()) {
//                path.mkdirs();
//            }
//            Template template = freemarkerConfig.getTemplate(tmplFile);
//            outWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile.toPath()), StringUtils.defaultIfEmpty(freemarkerConfig.getOutputEncoding(), freemarkerConfig.getDefaultEncoding())));
//            template.process(propMap, outWriter);
//            out(YELLOW, "输出路径" + outputFile);
//        } catch (Exception e) {
//            MxLog.error("输出文件异常", e);
//        } finally {
//            if (outWriter != null) {
//                try {
//                    outWriter.flush();
//                    outWriter.close();
//                } catch (IOException e) {
//                    MxLog.error("关闭输出流异常", e);
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 通用参数
//     *
//     * @return 通用参数map
//     */
//    private Map<String, Object> buildPropMap() {
//        Map<String, Object> propMap = baseProp();
//        //bean包名
//        propMap.put(BEAN_PACKAGE_NAME, curdConfig.getBeanName());
//        //dto包名
//        propMap.put(DTO_PACKAGE_NAME, curdConfig.getDtoName());
//        //vo包名
//        propMap.put(VO_PACKAGE_NAME, curdConfig.getVoName());
//        //Service包名
//        propMap.put(SERVICE_PACKAGE_NAME, curdConfig.getServiceName());
//        //控制器包名
//        propMap.put(CONTROLLER_PACKAGE_NAME, curdConfig.getControllerName());
//        //实体类注释
//        propMap.put(MODEL_COMMENT, curdConfig.getmodelComment());
//        //是否检查版本
//        propMap.put(IS_CHECK_VERSION, curdConfig.isCheckVersion());
//        //是否生成swagger
//        propMap.put(IS_CREATE_SWAGGER, curdConfig.isCreateSwagger());
//        //不能重复字段
//        propMap.put(NOT_SAME_FIELD, curdConfig.getNotSameField());
//        //不重复验证文字
//        propMap.put(NOT_SAME_TEXT, curdConfig.getNotSameText());
//        //#
//        propMap.put(JING, "#");
//        //<
//        propMap.put(LEFT, "<");
//        //>
//        propMap.put(RIGHT, ">");
//        return propMap;
//    }
//}
