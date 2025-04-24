package com.mx.ymate.upload.web.service;

import com.mx.ymate.dev.support.mvc.MxResult;
import net.ymate.platform.webmvc.IUploadFileWrapper;

/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:29.
 * @Description:
 */
public interface IMxUploadService {

    /**
     * 文件上传
     *
     * @param fileWrapper
     * @return
     * @throws Exception
     */
    MxResult push(IUploadFileWrapper fileWrapper) throws Exception;
}
