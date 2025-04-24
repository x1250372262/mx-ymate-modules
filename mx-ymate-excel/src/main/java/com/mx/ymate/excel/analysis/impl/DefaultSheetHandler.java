package com.mx.ymate.excel.analysis.impl;

import com.mx.ymate.excel.analysis.ExcelUtils;
import com.mx.ymate.excel.analysis.ISheetHandler;
import com.mx.ymate.excel.analysis.annotation.Excel;
import com.mx.ymate.excel.analysis.annotation.ImportColumn;
import com.mx.ymate.excel.analysis.annotation.Validate;
import com.mx.ymate.excel.analysis.bean.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 默认excel导入处理实现
 */
public class DefaultSheetHandler implements ISheetHandler {

    /**
     * excel取值方式
     */
    private final Excel.TYPE type;

    /**
     * vo对象class
     */
    private final Class<? extends IImportBean> cls;


    public DefaultSheetHandler(Class<? extends IImportBean> cls) throws Exception {
        this.cls = cls;
        Excel excel = cls.getAnnotation(Excel.class);
        if (excel == null) {
            throw new Exception("vo对象未包含Excle注解");
        }
        this.type = excel.type();
    }

    /**
     * 获取字段信息
     *
     * @return
     * @throws Exception
     */
    private Map<Object, Field> getFilesMap() {
        Field[] fields = cls.getDeclaredFields();
        Map<Object, Field> fieldMap = new HashMap<>();
        for (Field field : fields) {
            ImportColumn importColumn = field.getAnnotation(ImportColumn.class);
            if (importColumn != null) {
                switch (type) {
                    case IDX:
                        fieldMap.put(importColumn.idx(), field);
                        break;
                    case TITLE:
                        fieldMap.put(importColumn.title(), field);
                        break;
                    default:
                        break;
                }
            }
        }
        return fieldMap;
    }


    /**
     * 读取单元格内容
     *
     * @param field
     * @param cell
     * @return
     * @throws Exception
     */
    private CellResult parseCell(Field field, Cell cell, String title) throws Exception {
        ImportColumn importColumn = field.getAnnotation(ImportColumn.class);
        if (importColumn == null) {
            throw new Exception("vo类注解错误");
        }
        HandlerBean importHandlerBean = HandlerBean.create(importColumn);
        Validate validate = field.getAnnotation(Validate.class);
        ValidateBean validateBean = null;
        if (validate != null) {
            validateBean = ValidateBean.create(validate);
        }
        if (cell == null) {
            return CellHandler.handleBlank(validateBean, field, cell, title);
        }
        CellResult cellResult = new CellResult();
        switch (cell.getCellType()) {
            case STRING:
                cellResult = CellHandler.handleString(importHandlerBean, validateBean, cell, title);
                break;
            case NUMERIC:
                cellResult = CellHandler.handleNumeric(importColumn, importHandlerBean, validateBean, field, cell, title);
                break;
            case BLANK:
                cellResult = CellHandler.handleBlank(validateBean, field, cell, title);
                break;
            default:
                break;
        }
        return cellResult;
    }


    @Override
    public ResultBean handle(Sheet sheet) throws Exception {

        ResultBean resultBean = new ResultBean();

        //Vo数据
        List<IImportBean> result = new ArrayList<>();
        //错误信息
        List<ErrorInfo> errorInfoList = new ArrayList<>();

        //字段信息
        Map<Object, Field> fieldMap = getFilesMap();

        for (int rowId = sheet.getFirstRowNum(); rowId <= sheet.getLastRowNum(); rowId++) {
            if (rowId != sheet.getFirstRowNum()) {
                Row row = sheet.getRow(rowId);
                boolean isError = false;
                if (ExcelUtils.isRowNotEmpty(row)) {
                    IImportBean objectVo = cls.newInstance();
                    for (int cellIdx = row.getFirstCellNum(); cellIdx <= row.getLastCellNum(); cellIdx++) {
                        Field field = fieldMap.get(cellIdx);
                        Object title = ExcelUtils.getTitle(sheet.getRow(sheet.getFirstRowNum()).getCell(cellIdx));
                        if (type.equals(Excel.TYPE.TITLE)) {
                            field = fieldMap.get(title);
                        }
                        if (field != null) {
                            CellResult cellResult = parseCell(field, row.getCell(cellIdx), String.valueOf(title));
                            if (cellResult.getErrorInfo() != null) {
                                errorInfoList.add(cellResult.getErrorInfo());
                                isError = true;
                            } else {
                                //对私有字段的访问取消权限检查。暴力访问。
                                field.setAccessible(true);
                                field.set(objectVo, cellResult.getValue());
                            }
                        }
                    }
                    if (!isError) {
                        result.add(objectVo);
                    }
                }
            }
        }
        resultBean.setResultData(result);
        resultBean.setErrorInfoList(errorInfoList);
        return resultBean;
    }
}
