package com.mx.ymate.excel.analysis.bean;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
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
