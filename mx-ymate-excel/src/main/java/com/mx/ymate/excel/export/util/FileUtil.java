package com.mx.ymate.excel.export.util;

import java.io.File;

/**
 * @Author: mengxiang.
 * @create: 2022-10-27 15:49
 * @Description:
 */
public class FileUtil {

    /**
     * 修复并创建目标文件目录
     *
     * @param dir 待处理的文件夹路径
     * @return 修改并创建的目标文件夹路径
     */
    public static void fixAndMkDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
