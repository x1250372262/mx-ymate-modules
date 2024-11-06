package com.mx.ymate.upload.adapter.impl;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.upload.bean.FileInfo;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.File;

import static com.mx.ymate.upload.code.MxUploadCode.MX_UPLOAD_ERROR;
import static com.mx.ymate.upload.code.MxUploadCode.MX_UPLOAD_QINIU_CONFIG_ERROR;


/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:36.
 * @Description: 基于七牛云文件存储
 */
public class QiNiuUploadAdapter extends AbstractUploadAdapter {

    private UploadManager uploadManager;
    private Auth auth;

    @Override
    protected MxResult checkConfig() {
        if (!config.checkQiNiuConfig()) {
            return MxResult.create(MX_UPLOAD_QINIU_CONFIG_ERROR);
        }
        return MxResult.ok();
    }

    @Override
    protected MxResult saveFile(File srcFile, FileInfo fileInfo) {
        if (uploadManager == null || auth == null) {
            auth = Auth.create(config.qiNiuAccessKey(), config.qiNiuSecretKey());
            Configuration cfg = new Configuration(Region.createWithRegionId(config.qiNiuRegion()));
            uploadManager = new UploadManager(cfg);
        }
        // 生成上传凭证，然后准备上传
        String upToken = auth.uploadToken(config.qiNiuBucket());
        try {
            uploadManager.put(fileInfo.getInputStream(), fileInfo.getNewFileName(), upToken, null, null);
        } catch (QiniuException e) {
            return MxResult.create(MX_UPLOAD_ERROR).msg(StrUtil.format(MX_UPLOAD_ERROR.msg(), e.getMessage()));
        }
        return MxResult.ok();
    }

    @Override
    protected String getShowUrl() {
        return defaultShowUrl();
    }
}
