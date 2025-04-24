package com.mx.ymate.upload.adapter;

import com.mx.ymate.dev.support.mvc.MxResult;

import java.io.File;

/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:36.
 * @Description: 文件存储接口
 */
public interface IUploadAdapter {

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    MxResult upload(File file) throws Exception;


}
