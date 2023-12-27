package com.mx.ymate.upload.config;

import net.ymate.platform.core.YMP;

/**
 * @Author: mengxiang.
 * @create: 2022-11-28 17:12
 * @Description:
 */
public class MinioConfig {

    private String url;

    private String accessKey;

    private String secretKey;

    private String bucket;

    public MinioConfig() {
        this.url = YMP.get().getParam("module.fileuploader.minio.url");
        this.accessKey = YMP.get().getParam("module.fileuploader.minio.access_key");
        this.secretKey = YMP.get().getParam("module.fileuploader.minio.secret_key");
        this.bucket = YMP.get().getParam("module.fileuploader.minio.bucket");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
