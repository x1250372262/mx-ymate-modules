package com.mx.ymate.upload.adapter.impl;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.upload.bean.FileInfo;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

import static com.mx.ymate.upload.code.MxUploadCode.MX_UPLOAD_ERROR;
import static com.mx.ymate.upload.code.MxUploadCode.MX_UPLOAD_MINIO_CONFIG_ERROR;


/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:36.
 * @Description: 基于minio文件存储
 */
public class MinioUploadAdapter extends AbstractUploadAdapter {

    private MinioClient minioClient;

    @Override
    protected MxResult checkConfig() {
        if (!config.checkMinioConfig()) {
            return MxResult.create(MX_UPLOAD_MINIO_CONFIG_ERROR);
        }
        return MxResult.ok();
    }

    @Override
    protected MxResult saveFile(File srcFile, FileInfo fileInfo) {
        if (minioClient == null) {
            //使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            minioClient = MinioClient.builder().endpoint(config.showUrl()).credentials(config.minioAccessKey(), config.minioSecretKey()).build();
        }
        String fileName = fileInfo.getNewFileName();
        if (StringUtils.isNotBlank(config.prefix())) {
            fileName = config.prefix() + fileName;
        }
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(config.minioBucket())
                    .object(fileName)
                    .stream(fileInfo.getInputStream(), fileInfo.getFileSize(), -1)
                    .contentType(fileInfo.getType()).build());
        } catch (Exception e) {
            return MxResult.create(MX_UPLOAD_ERROR).msg(StrUtil.format(MX_UPLOAD_ERROR.msg(), e.getMessage()));
        }
        return MxResult.ok();
    }

    @Override
    protected String getShowUrl() {
        return config.showUrl() + config.minioBucket() + "/" + config.prefix();
    }
}
