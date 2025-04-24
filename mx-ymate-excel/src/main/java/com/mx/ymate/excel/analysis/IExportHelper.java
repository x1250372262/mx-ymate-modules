package com.mx.ymate.excel.analysis;

import net.ymate.platform.commons.util.RuntimeUtils;

import java.io.File;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class IExportHelper {

    /**
     * excel临时文件目录
     */
    public static String EXCEL_FILE_PATH = RuntimeUtils.getRootPath() + File.separator + "export" + File.separator;

    /**
     * zip临时文件目录
     */
    public static String ZIP_FILE_PATH = RuntimeUtils.getRootPath() + File.separator + "zip" + File.separator;


    /**
     * 模板文件文件目录
     */
    public static String TEMPLATE_FILE_PATH;


}
