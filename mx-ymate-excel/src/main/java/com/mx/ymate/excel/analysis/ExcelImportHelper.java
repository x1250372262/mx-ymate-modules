package com.mx.ymate.excel.analysis;

import com.mx.ymate.excel.analysis.bean.ResultBean;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author mengxiang
 * @Date 2019.02.10.
 * @Time: 14:30.
 * @Description: Excel文件数据导入助手类
 */
public class ExcelImportHelper implements Closeable {

    private final Workbook workbook;

    private final String[] sheetNames;

    public static ExcelImportHelper bind(File file) throws IOException {
        return new ExcelImportHelper(Files.newInputStream(file.toPath()));
    }

    public static ExcelImportHelper bind(InputStream inputStream) throws IOException {
        return new ExcelImportHelper(inputStream);
    }

    private ExcelImportHelper(InputStream inputStream) throws IOException {
        workbook = WorkbookFactory.create(inputStream);
        sheetNames = new String[workbook.getNumberOfSheets()];
        for (int idx = 0; idx < sheetNames.length; idx++) {
            sheetNames[idx] = workbook.getSheetName(idx);
        }
    }

    /**
     * @return 返回SHEET名称集合
     */
    public String[] getSheetNames() {
        return sheetNames;
    }

    public ResultBean openSheet(int sheetIdx, ISheetHandler handler) throws Exception {
        Sheet sheet = workbook.getSheetAt(sheetIdx);
        return handler.handle(sheet);
    }

    public ResultBean openSheet(String sheetName, ISheetHandler handler) throws Exception {
        Sheet sheet = workbook.getSheet(sheetName);
        return handler.handle(sheet);
    }

    @Override
    public void close() throws IOException {
        if (workbook != null) {
            workbook.close();
        }
    }


}
