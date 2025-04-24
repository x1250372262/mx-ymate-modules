package com.mx.ymate.dev.code;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 错误码接口
 */
public interface ICode {

    /**
     * 错误码
     * @return
     */
    String code();

    /**
     * i18nKey
     * @return
     */
    default String i18nKey(){
        return "";
    }

    /**
     * 错误信息
     * @return
     */
    String msg();
}
