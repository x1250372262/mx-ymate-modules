package com.mx.ymate.excel.analysis;

import com.mx.ymate.excel.analysis.bean.ResultBean;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @Author: mengxiang.
 * @Date: 2019-07-10.
 * @Time: 16:32.
 * @Description: excel导入处理接口
 */
public interface ISheetHandler {

    /**
     * 处理导入数据
     *
     * @param sheet Sheet页接口对象
     * @return 处理Sheet页并返回数据
     * @throws Exception 可能产生的任何异常
     */
    ResultBean handle(Sheet sheet) throws Exception;


}
