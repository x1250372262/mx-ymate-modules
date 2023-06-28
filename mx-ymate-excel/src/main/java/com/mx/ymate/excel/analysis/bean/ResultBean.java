package com.mx.ymate.excel.analysis.bean;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date: 2019-07-18.
 * @Time: 13:39.
 * @Description: 导入结果
 */
public class ResultBean {

    /**
     * 结果
     */
    private List<IImportBean> resultData;

    /**
     * 错误信息
     */
    private List<ErrorInfo> errorInfoList;


    public List<IImportBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<IImportBean> resultData) {
        this.resultData = resultData;
    }

    public List<ErrorInfo> getErrorInfoList() {
        return errorInfoList;
    }

    public void setErrorInfoList(List<ErrorInfo> errorInfoList) {
        this.errorInfoList = errorInfoList;
    }
}
