package com.mx.ymate.security.base.vo;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class SecurityOperationLogVO extends SecurityOperationLogListVO {

    private String requestParam;

    private String returnMessage;

    private String returnResult;

    private String className;

    private String methodName;

    private String os;

    private String browser;


    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getReturnResult() {
        return returnResult;
    }

    public void setReturnResult(String returnResult) {
        this.returnResult = returnResult;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    @Override
    public String toString() {
        return "SecurityOperationLogVO{" + "requestParam='" + requestParam + '\'' + ",  returnMessage='" + returnMessage + '\'' + ", returnResult='" + returnResult + '\'' + ", className='" + className + '\'' + ", methodName='" + methodName + '\'' + ", os='" + os + '\'' + ", browser='" + browser + '\'' + '}';
    }
}
