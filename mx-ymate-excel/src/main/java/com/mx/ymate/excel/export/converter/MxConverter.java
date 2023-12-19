package com.mx.ymate.excel.export.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.IoUtils;
import com.mx.ymate.excel.export.annotation.MConverter;
import net.ymate.platform.commons.lang.BlurObject;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;

/**
 * @author mengxiang
 * @Date 2020/5/20.
 * @Time: 16:57.
 * @Description: excel转换类
 */
public class MxConverter implements Converter<String> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        Field field = contentProperty.getField();
        if (field == null) {
            return cellData.getStringValue();
        }
        MConverter dConverter = field.getAnnotation(MConverter.class);
        if (dConverter == null) {
            return cellData.getStringValue();
        }
        String returnValue = null;
        HandlerBean handlerBean = HandlerBean.create(dConverter);
        if (handlerBean.getDataHandle() != null) {
            returnValue = BlurObject.bind(handlerBean.getMethod().invoke(handlerBean.getDataHandle(), cellData.getStringValue())).toStringValue();
        } else {
            returnValue = cellData.getStringValue();
        }
        return returnValue;
    }

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        Field field = contentProperty.getField();
        if (field == null) {
            return new WriteCellData<>(value);
        }
        MConverter dConverter = field.getAnnotation(MConverter.class);
        if (dConverter == null) {
            return new WriteCellData<>(value);
        }
        String returnValue = null;
        HandlerBean handlerBean = HandlerBean.create(dConverter);
        if (handlerBean.getDataHandle() != null) {
            if (HandlerBean.Type.IMAGE.equals(handlerBean.getType())) {
                InputStream inputStream = null;
                try {
                    URL url = new URL(value);
                    inputStream = url.openStream();
                    byte[] bytes = IoUtils.toByteArray(inputStream);
                    return new WriteCellData<>(bytes);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
            } else {
                returnValue = BlurObject.bind(handlerBean.getMethod().invoke(handlerBean.getDataHandle(), value)).toStringValue();
            }
        } else {
            returnValue = BlurObject.bind(value).toStringValue();
        }
        return new WriteCellData<>(returnValue);
    }
}
