package com.mx.ymate.maven.util;

import cn.hutool.setting.dialect.Props;

import java.io.File;

/**
 * @Author: mengxiang.
 * @create: 2021-09-30 09:21
 * @Description: 资源文件工具类
 */
public class PropUtils {

    public static final String DRIVER_CLASS_NAME = "mx.driverClassName";
    public static final String URL = "mx.url";
    public static final String USER_NAME = "mx.userName";
    public static final String PASSWORD = "mx.password";
    public static final String DB_NAME = "mx.entity.dbName";
    public static final String USE_CHAIN_MODE = "mx.entity.useChainMode";
    public static final String COVER_MODEL = "mx.entity.coverModel";
    public static final String TABLE_PREFIX = "mx.entity.tablePrefix";
    public static final String REMOVE_TABLE_PREFIX = "mx.entity.removeTablePrefix";
    public static final String TABLE_LIST = "mx.entity.tableList";
    public static final String TABLE_EXCLUDE_LIST = "mx.entity.tableExcludeList";
    public static final String OUTPUT_PATH = "mx.entity.outputPath";
    public static final String PROJECT_PATH = "mx.entity.projectPath";
    public static final String MODEL_PACKAGE_NAME = "mx.entity.packageName";
    public static final String MAPPER_PACK_NAME = "mx.mapper.packageName";
    public static final String MAPPER_XML_PATH = "mx.mapper.mapperXmlPath";
    public static final String MAPPER_PROJECT_PATH = "mx.mapper.mapperProjectPath";
    public static final String MAPPER_XML_PROJECT_PATH = "mx.mapper.xmlProjectPath";
    public static final String MAPPER_CREATE = "mx.mapper.mapperCreate";
    public static final String CURD_ENTITY_CLASS = "mx.curd.entityClass";
    public static final String CURD_ENTITY_COMMENT = "mx.curd.entityComment";
    public static final String CURD_ENTITY_PATH = "mx.curd.entityPath";
    public static final String CURD_MAPPER_CLASS = "mx.curd.mapperClass";
    public static final String CURD_MAPPER_PATH = "mx.curd.mapperPath";


    public static final String CURD_DAO_CREATE = "mx.curd.dao.create";
    public static final String CURD_DAO_PACK_NAME = "mx.curd.dao.packageName";
    public static final String CURD_DAO_PROJECT_PATH = "mx.curd.dao.projectPath";
    public static final String CURD_SERVICE_CREATE = "mx.curd.service.create";
    public static final String CURD_SERVICE_PACK_NAME = "mx.curd.service.packageName";
    public static final String CURD_SERVICE_PROJECT_PATH = "mx.curd.service.projectPath";
    public static final String CURD_CONTROLLER_CREATE = "mx.curd.controller.create";
    public static final String CURD_CONTROLLER_PACK_NAME = "mx.curd.controller.packageName";
    public static final String CURD_CONTROLLER_PROJECT_PATH = "mx.curd.controller.projectPath";
    public static final String CURD_PAGE_CREATE = "mx.curd.page.create";
    public static final String CURD_PAGE_PROJECT_PATH = "mx.curd.page.projectPath";
    public static final String CURD_PAGE_JS_PATH = "mx.curd.page.jsPath";
    public static final String CURD_JS_URL = "mx.curd.page.jsUrl";
    public static final String CURD_BEAN_PACK_NAME = "mx.curd.bean.packageName";
    public static final String CURD_BEAN_PROJECT_PATH = "mx.curd.bean.projectPath";
    public static final String CURD_DTO_PACK_NAME = "mx.curd.dto.packageName";
    public static final String CURD_DTO_PROJECT_PATH = "mx.curd.dto.projectPath";
    public static final String CURD_VO_PACK_NAME = "mx.curd.vo.packageName";
    public static final String CURD_VO_PROJECT_PATH = "mx.curd.vo.projectPath";
    public static final String CURD_CHECK_VERSION = "mx.curd.checkVersion";
    public static final String CURD_CREATE_SWAGGER = "mx.curd.createSwagger";
    public static final String CURD_NOT_SAME_FIELD = "mx.curd.notSameField";
    public static final String CURD_NOT_SAME_TEXT = "mx.curd.notSameText";
    public static final String CURD_LIST_PARAMS = "mx.curd.listParams";
    public static final String CURD_LIST_RETURNS = "mx.curd.listReturns";
    public static final String CURD_DETAIL_RETURNS = "mx.curd.detailReturns";
    public static final String CURD_OPTION_PARAMS = "mx.curd.optionParams";

    public static final String MTD_DB_NAME = "mx.mtd.dbName";

    public static final String MTD_DB_CHARSET = "mx.mtd.charset";

    public static final String MTD_DB_COLLATE = "mx.mtd.collate";

    public static final String MTD_MODEL_LIST = "mx.mtd.modelList";

    public static final String MTD_MODEL_EXCLUDE_LIST = "mx.mtd.modelExcludeList";

    public static final String MTD_PACKAGE_NAME = "mx.mtd.packageName";

    public static final String MTD_OUTPUT_PATH = "mx.mtd.outputPath";


    public static Props getProps() {
        return new Props(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "mx.properties"));
    }


}
