package com.mx.ymate.maven.plugin;

import cn.hutool.setting.dialect.Props;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.fusesource.jansi.Ansi;

import java.io.File;

import static com.mx.ymate.maven.plugin.constant.FileNameConstant.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 代码生成抽象类
 */
public abstract class BaseGeneratorMojo extends AbstractMojo {

    public Props getProps() {
        return new Props(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "mx-maven-plugin.properties"));
    }

    public String createDaoFileName(String modelName) {
        return "I" + modelName + DAO_JAVA_FILE_SUFFIX;
    }

    public String createDaoImplFileName(String modelName) {
        return modelName + DAO_IMPL_JAVA_FILE_SUFFIX;
    }

    public String createServiceFileName(String modelName) {
        return "I" + modelName + SERVICE_JAVA_FILE_SUFFIX;
    }

    public String createServiceImplFileName(String modelName) {
        return modelName + SERVICE_IMPL_JAVA_FILE_SUFFIX;
    }

    public String createControllerFileName(String modelName) {
        return modelName + CONTROLLER_JAVA_FILE_SUFFIX;
    }

    public String createDtoFileName(String modelName) {
        return modelName + DTO_JAVA_FILE_SUFFIX;
    }

    public String createBeanFileName(String modelName) {
        return modelName + BEAN_JAVA_FILE_SUFFIX;
    }

    public String createDtoListFileName(String modelName) {
        return modelName + DTO_LIST_JAVA_FILE_SUFFIX;
    }

    public String createBeanListFileName(String modelName) {
        return modelName + BEAN_LIST_JAVA_FILE_SUFFIX;
    }

    public String createVoFileName(String modelName) {
        return modelName + VO_JAVA_FILE_SUFFIX;
    }

    public String createVoListFileName(String modelName) {
        return modelName + VO_LIST_JAVA_FILE_SUFFIX;
    }

    public String createPageHtmlFileName(String modelName) {
        return StringUtils.uncapitalize(modelName) + PAGE_HTML_FILE_SUFFIX;
    }

    public String createPageJsFileName(String modelName) {
        return StringUtils.uncapitalize(modelName) + PAGE_JS_FILE_SUFFIX;
    }


    /**
     * 生成输出信息
     *
     * @param color
     * @param msg
     */
    public void out(Ansi.Color color, String msg) {
        System.out.println(ansi().eraseScreen().fg(color).a(msg).reset().toString());
    }


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
    }


}
