package com.mx.ymate.excel.analysis.impl;

import com.mx.ymate.excel.analysis.annotation.ImportColumn;
import com.mx.ymate.excel.analysis.bean.CellResult;
import com.mx.ymate.excel.analysis.bean.ErrorInfo;
import com.mx.ymate.excel.analysis.bean.HandlerBean;
import com.mx.ymate.excel.analysis.bean.ValidateBean;
import net.ymate.platform.commons.lang.BlurObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Objects;

import static com.mx.ymate.excel.analysis.ExcelUtils.readNumericCell;
import static org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted;


/**
 * @Author: mengxiang.
 * @Date: 2019-07-20.
 * @Time: 15:07.
 * @Description:
 */
public class CellHandler {

    /**
     * 处理string 类型数据
     *
     * @param cell
     * @param title
     * @return
     * @throws Exception
     */
    public static CellResult handleString(HandlerBean handlerBean, ValidateBean validateBean, Cell cell, String title) throws Exception {

        CellResult cellResult = new CellResult();
        Object value;
        if (handlerBean.getDataHandle() != null) {
            value = handlerBean.getMethod().invoke(handlerBean.getDataHandle(), cell.getStringCellValue());
        } else {
            value = cell.getStringCellValue();
        }
        if (validateBean != null) {
            ErrorInfo errorInfo = new ErrorInfo(cell.getRowIndex(), cell.getColumnIndex(), title);
            //有自定义方法  先处理方法
            if (validateBean.getValidate() != null) {
                if (validateBean.getMethod() != null) {
                    if (validateBean.getParameterType() != null) {
                        errorInfo = (ErrorInfo) validateBean.getMethod().invoke(validateBean.getValidate(), value, errorInfo);
                    } else {
                        errorInfo = (ErrorInfo) validateBean.getMethod().invoke(validateBean.getValidate(), errorInfo);
                    }
                }
            } else {
                if (validateBean.getRequired() && Objects.isNull(value)) {
                    errorInfo.setContent("第" + cell.getRowIndex() + "行,第" + cell.getColumnIndex() + "列,标题为:" + title + "不能为空");
                }
            }
            if (errorInfo != null && StringUtils.isNotBlank(errorInfo.getContent())) {
                cellResult.setErrorInfo(errorInfo);
            }
        }
        if (cellResult.getErrorInfo() == null) {
            cellResult.setValue(value);
        }
        return cellResult;
    }

    /**
     * 处理Numeric型
     *
     * @param importColumn
     * @param handlerBean
     * @param validateBean
     * @param field
     * @param cell
     * @param title
     * @return
     * @throws Exception
     */
    public static CellResult handleNumeric(ImportColumn importColumn, HandlerBean handlerBean, ValidateBean validateBean, Field field, Cell cell, String title) throws Exception {

        CellResult cellResult = new CellResult();
        Object value = null;
        Object dataHandler = handlerBean.getDataHandle();
        Method dataMethod = handlerBean.getMethod();
        if (isCellDateFormatted(cell) && cell.getDateCellValue() != null) {
            value = cell.getDateCellValue().getTime();
        } else {
            if (String.class.getName().equals(field.getType().getName())) {
                if (dataHandler != null) {
                    if (importColumn.parameterType().equals(int.class)) {
                        value = dataMethod.invoke(dataHandler, BlurObject.bind(new DecimalFormat("0").format(cell.getNumericCellValue())).toIntValue());
                    } else if (importColumn.parameterType().equals(double.class)) {
                        value = dataMethod.invoke(dataHandler, BlurObject.bind(new DecimalFormat("0").format(cell.getNumericCellValue())).toDoubleValue());
                    } else if (importColumn.parameterType().equals(long.class)) {
                        value = dataMethod.invoke(dataHandler, BlurObject.bind(new DecimalFormat("0").format(cell.getNumericCellValue())).toLongValue());
                    } else {
                        value = dataMethod.invoke(dataHandler, BlurObject.bind(new DecimalFormat("0").format(cell.getNumericCellValue())).toStringValue());
                    }
                } else {
                    value = new DecimalFormat("0").format(cell.getNumericCellValue());
                }

            } else {
                if (Long.class.getName().equals(field.getType().getName())) {
                    if (dataHandler != null) {
                        value = dataMethod.invoke(dataHandler, cell.getNumericCellValue());

                    } else {
                        value = BlurObject.bind(readNumericCell(cell)).toLongValue();
                    }
                } else if (Double.class.getName().equals(field.getType().getName())) {
                    if (dataHandler != null) {
                        value = dataMethod.invoke(dataHandler, cell.getNumericCellValue());
                    } else {
                        value = BlurObject.bind(cell.getNumericCellValue()).toDoubleValue();
                    }
                } else {
                    if (dataHandler != null) {
                        value = dataMethod.invoke(dataHandler, readNumericCell(cell));
                    } else {
                        value = readNumericCell(cell);
                    }

                }
            }
        }

        if (validateBean != null) {
            ErrorInfo errorInfo = new ErrorInfo(cell.getRowIndex(), cell.getColumnIndex(), title);
            //有自定义方法  先处理方法
            if (validateBean.getValidate() != null) {
                if (validateBean.getMethod() != null) {
                    if (validateBean.getParameterType() != null) {
                        errorInfo = (ErrorInfo) validateBean.getMethod().invoke(validateBean.getValidate(), value, errorInfo);
                    } else {
                        errorInfo = (ErrorInfo) validateBean.getMethod().invoke(validateBean.getValidate(), errorInfo);
                    }
                }
            } else {
                if (validateBean.getRequired() && Objects.isNull(value)) {
                    errorInfo.setContent("第" + cell.getRowIndex() + "行,第" + cell.getColumnIndex() + "列,标题为:" + title + "不能为空");
                }
            }
            if (errorInfo != null && StringUtils.isNotBlank(errorInfo.getContent())) {
                cellResult.setErrorInfo(errorInfo);
            }
        }
        if (cellResult.getErrorInfo() == null) {
            cellResult.setValue(value);
        }
        return cellResult;
    }


    /**
     * 处理空数据
     *
     * @param validateBean
     * @param field
     * @param cell
     * @param title
     * @return
     * @throws Exception
     */
    public static CellResult handleBlank(ValidateBean validateBean, Field field, Cell cell, String title) throws Exception {
        CellResult cellResult = new CellResult();
        Object value;
        String className = field.getType().getSimpleName().toUpperCase();
        if (String.class.getSimpleName().toUpperCase().equals(className)) {
            value = "";
        } else if (Long.class.getSimpleName().toUpperCase().equals(className)) {
            value = 0L;
        } else if (Integer.class.getSimpleName().toUpperCase().equals(className)) {
            value = 0;
        } else if (Double.class.getSimpleName().toUpperCase().equals(className)) {
            value = 0d;
        } else {
            value = "";
        }
        if (validateBean != null) {
            int rowIndex = -1;
            int columnIndex = -1;
            if (cell != null) {
                rowIndex = cell.getRowIndex();
                columnIndex = cell.getColumnIndex();
            }
            ErrorInfo errorInfo = new ErrorInfo(rowIndex, columnIndex, title);
            //有自定义方法  先处理方法
            if (validateBean.getValidate() != null) {
                if (validateBean.getMethod() != null) {
                    if (validateBean.getParameterType() != null) {
                        errorInfo = (ErrorInfo) validateBean.getMethod().invoke(validateBean.getValidate(), value, errorInfo);
                    } else {
                        errorInfo = (ErrorInfo) validateBean.getMethod().invoke(validateBean.getValidate(), errorInfo);
                    }
                }
            } else {
                if (validateBean.getRequired() && Objects.isNull(value)) {
                    errorInfo.setContent("第" + rowIndex + "行,第" + columnIndex + "列,标题为:" + title + "不能为空");
                }
            }
            if (errorInfo != null && StringUtils.isNotBlank(errorInfo.getContent())) {
                cellResult.setErrorInfo(errorInfo);
            }
        }
        if (cellResult.getErrorInfo() == null) {
            cellResult.setValue(value);
        }
        return cellResult;
    }
}
