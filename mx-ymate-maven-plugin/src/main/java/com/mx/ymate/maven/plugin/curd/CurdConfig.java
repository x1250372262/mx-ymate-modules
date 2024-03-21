package com.mx.ymate.maven.plugin.curd;

import cn.hutool.setting.dialect.Props;
import com.mx.ymate.maven.plugin.enums.CurdEnums;
import org.apache.commons.lang3.StringUtils;

import static com.mx.ymate.maven.plugin.constant.ConfigConstant.*;

/**
 * @Author: mengxiang.
 * @create: 2021-09-30 09:21
 * @Description: curd配置
 */
public class CurdConfig {

    /**
     * 文件存放位置
     */
    private String filePath;

    /**
     * 类型 分为dao,service,controller,page,all 多个用|分割  默认为all
     */
    private String type;

    /**
     * 实体类
     */
    private String modelClass;

    /**
     * dao包名
     */
    private String daoPackageName;

    /**
     * service包名
     */
    private String servicePackageName;

    /**
     * controller包名
     */
    private String controllerPackageName;


    public CurdConfig(Props props) {
        this.filePath = props.getStr(CURD_FILE_PATH);
        this.type = props.getStr(CURD_TYPE);
        if (StringUtils.isBlank(this.type)) {
            this.type = CurdEnums.TypeEnum.ALL.getValue();
        }
        this.modelClass = props.getStr(CURD_MODEL_CLASS);
        this.daoPackageName = props.getStr(CURD_DAO_PACKAGE_NAME);
        this.servicePackageName = props.getStr(CURD_SERVICE_PACKAGE_NAME);
        this.controllerPackageName = props.getStr(CURD_CONTROLLER_PACKAGE_NAME);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModelClass() {
        return modelClass;
    }

    public void setModelClass(String modelClass) {
        this.modelClass = modelClass;
    }

    public String getDaoPackageName() {
        return daoPackageName;
    }

    public void setDaoPackageName(String daoPackageName) {
        this.daoPackageName = daoPackageName;
    }

    public String getServicePackageName() {
        return servicePackageName;
    }

    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }

    public String getControllerPackageName() {
        return controllerPackageName;
    }

    public void setControllerPackageName(String controllerPackageName) {
        this.controllerPackageName = controllerPackageName;
    }
}

