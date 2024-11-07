package com.mx.ymate.upload.web.service.impl;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.upload.MxUpload;
import com.mx.ymate.upload.web.service.IMxUploadService;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.webmvc.IUploadFileWrapper;

/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:30.
 * @Description:
 */
@Bean
public class MxUploadServiceImpl implements IMxUploadService {

    @Override
    public MxResult push(IUploadFileWrapper fileWrapper) throws Exception {
        return MxUpload.get().upload(fileWrapper);
    }
}
