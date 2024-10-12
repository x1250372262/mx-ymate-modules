package com.mx.ymate.dev.code;

/**
 * @Author: mengxiang.
 * @create: 2022-06-24 15:34
 * @Description: 错误码接口
 */
public interface ICode {

    /**
     * 错误码
     * @return
     */
    String code();

    /**
     * 错误信息
     * @return
     */
    String msg();
}
