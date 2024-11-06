package com.mx.ymate.upload.code;

import com.mx.ymate.dev.code.ICode;

/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:36.
 * @Description: 文件上传错误码
 */
public enum MxUploadCode implements ICode {

    /**
     * 文件上传
     */
    MX_UPLOAD_ERROR("MUP00", "文件上传失败:{}"),
    MX_UPLOAD_SIZE_ERROR("MUP01", "文件太大"),
    MX_UPLOAD_CONTENT_TYPE_ERROR("MUP03", "不允许的文件类型"),
    MX_UPLOAD_LOCAL_CONFIG_ERROR("MUP04", "请检查本地参数配置"),
    MX_UPLOAD_MINIO_CONFIG_ERROR("MUP05", "请检查minio参数配置"),
    MX_UPLOAD_QINIU_CONFIG_ERROR("MUP06", "请检查七牛参数配置"),
    MX_UPLOAD_TX_CONFIG_ERROR("MUP07", "请检查腾讯参数配置"),
    MX_UPLOAD_ALI_CONFIG_ERROR("MUP08", "请检查阿里参数配置");



    private final String code;
    private final String msg;

    MxUploadCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }
}
