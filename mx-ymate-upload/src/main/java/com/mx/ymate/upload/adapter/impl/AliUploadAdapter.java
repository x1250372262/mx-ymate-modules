package com.mx.ymate.upload.adapter.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.upload.bean.FileInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

import static com.mx.ymate.upload.code.MxUploadCode.MX_UPLOAD_ALI_CONFIG_ERROR;


/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:36.
 * @Description: 基于阿里云文件存储
 */
public class AliUploadAdapter extends AbstractUploadAdapter {

    private OSS ossClient;

    @Override
    protected MxResult checkConfig() {
        if (!config.checkAliConfig()) {
            return MxResult.create(MX_UPLOAD_ALI_CONFIG_ERROR);
        }
        return MxResult.ok();
    }

    @Override
    protected MxResult saveFile(File srcFile, FileInfo fileInfo) {
        if (ossClient == null) {
            ossClient = new OSSClientBuilder().build(config.aliEndpoint(), config.aliAccessKeyId(), config.aliAccessKeySecret());
        }
        String fileName = fileInfo.getNewFileName();
        if (StringUtils.isNotBlank(config.prefix())) {
            fileName = config.prefix() + fileName;
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(config.aliBucket(), fileName, fileInfo.getInputStream());
        ossClient.putObject(putObjectRequest);
        return MxResult.ok();
    }

    @Override
    protected String getShowUrl() {
        return defaultShowUrl();
    }
}
