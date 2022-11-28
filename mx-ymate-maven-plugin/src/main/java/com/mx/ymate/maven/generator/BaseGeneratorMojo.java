//package com.mx.ymate.maven.generator;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.maven.plugin.AbstractMojo;
//import org.apache.maven.plugin.MojoExecutionException;
//import org.apache.maven.plugin.MojoFailureException;
//import org.fusesource.jansi.Ansi;
//
//import java.io.File;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import static com.mx.ymate.maven.constants.FileNameConstant.*;
//import static com.mx.ymate.maven.constants.MapConstant.CREATE_TIME;
//import static org.fusesource.jansi.Ansi.ansi;
//
///**
// * @Author: mengxiang.
// * @create: 2021-09-30 09:21
// * @Description: 代码生成抽象类
// */
//public abstract class BaseGeneratorMojo extends AbstractMojo {
//
//    public String createDaoJavaFileName(String modelName) {
//        return "I" + StringUtils.capitalize(modelName) + DAO_JAVA_FILE_SUFFIX;
//    }
//
//    public String createDaoImplJavaFileName(String modelName) {
//        return modelName + DAO_IMPL_JAVA_FILE_SUFFIX;
//    }
//
//    public String createServiceFileName(String modelName) {
//        return "I" + modelName + SERVICE_JAVA_FILE_SUFFIX;
//    }
//
//    public String createServiceImplFileName(String modelName) {
//        return "impl" + File.separator + modelName + SERVICE_IMPL_JAVA_FILE_SUFFIX;
//    }
//
//    public String createDtoFileName(String modelName) {
//        return modelName + DTO_JAVA_FILE_SUFFIX;
//    }
//
//    public String createBeanFileName(String modelName) {
//        return modelName + BEAN_JAVA_FILE_SUFFIX;
//    }
//
//    public String createDtoListFileName(String modelName) {
//        return modelName + DTO_LIST_JAVA_FILE_SUFFIX;
//    }
//
//    public String createBeanListFileName(String modelName) {
//        return modelName + BEAN_LIST_JAVA_FILE_SUFFIX;
//    }
//
//    public String createVoFileName(String modelName) {
//        return modelName + VO_JAVA_FILE_SUFFIX;
//    }
//
//    public String createVoListFileName(String modelName) {
//        return modelName + VO_LIST_JAVA_FILE_SUFFIX;
//    }
//
//    public String createPageHtmlFileName(String modelName) {
//        return modelName + PAGE_HTML_FILE_SUFFIX;
//    }
//
//    public String createPageJsFileName(String modelName) {
//        return modelName + PAGE_JS_FILE_SUFFIX;
//    }
//
//
//    /**
//     * 生成输出信息
//     *
//     * @param color
//     * @param msg
//     */
//    public void out(Ansi.Color color, String msg) {
//        System.out.println(ansi().eraseScreen().fg(color).a(msg).reset());
//    }
//
//
//    /**
//     * 创建ftl参数集合
//     *
//     * @return
//     */
//    public Map<String, Object> baseProp() {
//        Map<String, Object> propMap = new HashMap<>();
//        //创建时间
//        propMap.put(CREATE_TIME, new Date());
//        return propMap;
//    }
//
//    @Override
//    public void execute() throws MojoExecutionException, MojoFailureException {
//    }
//
//
//}
