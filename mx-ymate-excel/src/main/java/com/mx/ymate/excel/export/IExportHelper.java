package com.mx.ymate.excel.export;

import net.ymate.platform.commons.util.RuntimeUtils;

import java.io.File;

/**
 * @Author: mengxiang.
 * @create: 2021-03-04 16:25
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


    public static final String ZIP_URL = "/mx/excel/download/zip/";

    public static final String EXCEL_URL = "/mx/excel/download/excel/";

}
