package com.mx.ymate.upload.adapter.impl;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.upload.bean.FileInfo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

import static com.mx.ymate.upload.code.MxUploadCode.MX_UPLOAD_TX_CONFIG_ERROR;


/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:36.
 * @Description: 基于腾讯云文件存储
 */
public class TxUploadAdapter extends AbstractUploadAdapter {

    private COSClient cosClient;

    @Override
    protected MxResult checkConfig() {
        if (!config.checkTxConfig()) {
            return MxResult.create(MX_UPLOAD_TX_CONFIG_ERROR);
        }
        return MxResult.ok();
    }

    @Override
    protected MxResult saveFile(File srcFile, FileInfo fileInfo) {
        if (cosClient == null) {
            COSCredentials cred = new BasicCOSCredentials(config.txSecretId(), config.txSecretKey());
            ClientConfig clientConfig = new ClientConfig(new Region(config.txRegion()));
            cosClient = new COSClient(cred, clientConfig);
        }
        String fileName = fileInfo.getNewFileName();
        if (StringUtils.isNotBlank(config.prefix())) {
            fileName = config.prefix() + fileName;
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(config.txBucket(), fileName, srcFile);
        cosClient.putObject(putObjectRequest);
        return MxResult.ok();
    }

    @Override
    protected String getShowUrl() {
        return defaultShowUrl();
    }
}
