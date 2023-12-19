package com.mx.ymate.excel.export;


import com.alibaba.excel.EasyExcel;
import com.mx.ymate.excel.export.util.FileUtil;
import net.ymate.platform.commons.util.DateTimeUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author mengxiang
 * @Date 2019.02.10.
 * @Time: 14:30.
 * @Description: Excel文件导出助手类
 */
public class ExcelExportHelper<T> extends IExportHelper implements Closeable {

    /**
     * 导出数据
     */
    private List<List<T>> resultData;


    private static Class<?> instance;


    public ExcelExportHelper() {
    }

    public static <T> ExcelExportHelper<T> init(Class<T> classes, String excelFilePath, String zipFilePath) {
        instance = classes;
        if (StringUtils.isNotBlank(excelFilePath)) {
            EXCEL_FILE_PATH = excelFilePath;
        }
        if (StringUtils.isNotBlank(zipFilePath)) {
            ZIP_FILE_PATH = zipFilePath;
        }
        return new ExcelExportHelper<>();
    }

    public static <T> ExcelExportHelper<T> init(Class<T> classes) {
        return init(classes, null, null);
    }


    public void addData(List<T> data) {
        if (resultData == null) {
            resultData = new ArrayList<>();
        }
        resultData.add(data);
    }

    /**
     * 导出一个文件  可能会占用cpu和内存  不建议使用
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public File exportOneFile(String fileName) throws Exception {
        FileUtil.fixAndMkDir(EXCEL_FILE_PATH);
        fileName = fileName + DateTimeUtils.formatTime(DateTimeUtils.currentTimeMillis(), "yyyyMMddHHmmss");
        File outFile = new File(EXCEL_FILE_PATH, fileName + ".xlsx");
        if (resultData != null && !resultData.isEmpty()) {
            List<T> data = new ArrayList<>();
            for (int idx = 0; ; idx++) {
                if (resultData.size() <= idx) {
                    break;
                }
                List<T> tempData = resultData.get(idx);
                if (tempData == null || tempData.isEmpty()) {
                    break;
                }

                data.addAll(tempData);
            }
            EasyExcel.write(outFile.getPath(), instance).sheet(fileName).doWrite(data);
        }
        return outFile;
    }

    public File exportZip(String fileName) throws Exception {
        FileUtil.fixAndMkDir(EXCEL_FILE_PATH);
        FileUtil.fixAndMkDir(ZIP_FILE_PATH);
        List<File> files = new ArrayList<>();
        if (resultData != null && !resultData.isEmpty()) {
            for (int idx = 0; ; idx++) {
                if (resultData.size() <= idx) {
                    break;
                }
                List<T> data = resultData.get(idx);
                if (data == null || data.isEmpty()) {
                    break;
                }
                File outFile = new File(EXCEL_FILE_PATH, fileName + idx + DateTimeUtils.formatTime(DateTimeUtils.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx");

                EasyExcel.write(outFile.getPath(), instance).sheet(fileName + idx).doWrite(data);
                //输出信息
                files.add(outFile);
            }
        }
        return toZip(files, fileName);
    }

    private File toZip(List<File> files, String fileName) throws Exception {
        File zipFile = new File(ZIP_FILE_PATH, fileName + DateTimeUtils.formatTime(DateTimeUtils.currentTimeMillis(), "yyyyMMdd-HHmmss") + ".zip");
        try (ZipOutputStream outputStream = new ZipOutputStream(Files.newOutputStream(zipFile.toPath()))) {
            for (File file : files) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                outputStream.putNextEntry(zipEntry);
                try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                    IOUtils.copyLarge(inputStream, outputStream);
                }
            }
        }
        return zipFile;
    }

    @Override
    public void close() throws IOException {

    }

}
