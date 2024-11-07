package com.mx.ymate.upload.impl;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.upload.IMxUpload;
import com.mx.ymate.upload.IMxUploadConfig;
import com.mx.ymate.upload.adapter.IUploadAdapter;
import com.mx.ymate.upload.adapter.impl.*;
import com.mx.ymate.upload.enums.AdapterEnum;
import net.ymate.platform.core.configuration.IConfigReader;
import net.ymate.platform.core.module.IModuleConfigurer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:36.
 * @Description: 默认文件上传配置
 */
public final class DefaultMxUploadConfig implements IMxUploadConfig {

    private boolean enabled;

    private long maxSize;

    private final List<String> contentTypeList = new ArrayList<>();

    private String showUrl;

    private IUploadAdapter adapter;

    private String jsonFilePath;

    private boolean isCreateThumb;

    private int thumbWidth;
    private int thumbHeight;

    private float thumbQuality;

    private String localFileStoragePath;

    private String minioAccessKey;

    private String minioSecretKey;

    private String minioBucket;

    private String qiNiuAccessKey;

    private String qiNiuSecretKey;

    private String qiNiuBucket;

    private String qiNiuRegion;

    private String txSecretId;

    private String txSecretKey;

    private String txBucket;

    private String txRegion;

    private String aliAccessKeyId;

    private String aliAccessKeySecret;

    private String aliEndpoint;

    private String aliBucket;

    private boolean initialized;


    public static DefaultMxUploadConfig create(IModuleConfigurer moduleConfigurer) {
        return new DefaultMxUploadConfig(moduleConfigurer);
    }


    private DefaultMxUploadConfig() {
    }

    private DefaultMxUploadConfig(IModuleConfigurer moduleConfigurer) {
        IConfigReader configReader = moduleConfigurer.getConfigReader();
        enabled = configReader.getBoolean(ENABLED, true);
        maxSize = configReader.getLong(MAX_SIZE, 10 * 1024 * 1024);
        String[] contentTypeArr = configReader.getArray(CONTENT_TYPE);
        if (contentTypeArr != null && contentTypeArr.length > 0) {
            contentTypeList.addAll(Arrays.asList(contentTypeArr));
        }
        showUrl = configReader.getString(SHOW_URL);
        if (StringUtils.isNotBlank(showUrl) && !showUrl.endsWith(StrUtil.SLASH)) {
            showUrl = showUrl + StrUtil.SLASH;
        }
        adapter = getAdapter(AdapterEnum.fromValue(configReader.getString(ADAPTER,AdapterEnum.LOCAL.getValue())));
        jsonFilePath = configReader.getString(JSON_FILE_PATH);
        isCreateThumb = configReader.getBoolean(IS_CREATE_THUMB, false);
        thumbWidth = configReader.getInt(THUMB_WIDTH);
        thumbHeight = configReader.getInt(THUMB_HEIGHT);
        thumbQuality = configReader.getFloat(THUMB_QUALITY, 0F);
        localFileStoragePath = configReader.getString(LOCAL_FILE_STORAGE_PATH);
        minioAccessKey = configReader.getString(MINIO_ACCESS_KEY);
        minioSecretKey = configReader.getString(MINIO_SECRET_KEY);
        minioBucket = configReader.getString(MINIO_BUCKET);
        qiNiuAccessKey = configReader.getString(QINIU_ACCESS_KEY);
        qiNiuSecretKey = configReader.getString(QINIU_SECRET_KEY);
        qiNiuBucket = configReader.getString(QINIU_BUCKET);
        qiNiuRegion = configReader.getString(QINIU_REGION);
        txSecretId = configReader.getString(TX_SECRET_ID);
        txSecretKey = configReader.getString(TX_SECRET_KEY);
        txBucket = configReader.getString(TX_BUCKET);
        txRegion = configReader.getString(TX_REGION);
        aliAccessKeyId = configReader.getString(ALI_ACCESS_KEY_ID);
        aliAccessKeySecret = configReader.getString(ALI_ACCESS_KEY_SECRET);
        aliEndpoint = configReader.getString(ALI_ENDPOINT);
        aliBucket = configReader.getString(ALI_BUCKET);
    }

    private IUploadAdapter getAdapter(AdapterEnum adapterEnum){
        switch (adapterEnum){
            case MINIO:
                return new MinioUploadAdapter();
            case QI_NIU:
                return new QiNiuUploadAdapter();
            case TX:
                return new TxUploadAdapter();
            case ALI:
                return new AliUploadAdapter();
            default:
                return new LocalUploadAdapter();
        }
    }

    @Override
    public void initialize(IMxUpload owner) throws Exception {
        if (!initialized) {
            initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public long maxSize() {
        return maxSize;
    }

    @Override
    public List<String> contentTypeList() {
        return contentTypeList;
    }

    @Override
    public String showUrl() {
        return showUrl;
    }

    @Override
    public IUploadAdapter adapter() {
        return adapter;
    }

    @Override
    public String jsonFilePath() {
        return jsonFilePath;
    }

    @Override
    public boolean isCreateThumb() {
        return isCreateThumb;
    }

    @Override
    public int thumbWidth() {
        return thumbWidth;
    }

    @Override
    public int thumbHeight() {
        return thumbHeight;
    }

    @Override
    public float thumbQuality() {
        return thumbQuality;
    }

    @Override
    public String localFileStoragePath() {
        return localFileStoragePath;
    }

    @Override
    public String minioAccessKey() {
        return minioAccessKey;
    }

    @Override
    public String minioSecretKey() {
        return minioSecretKey;
    }

    @Override
    public String minioBucket() {
        return minioBucket;
    }

    @Override
    public String qiNiuAccessKey() {
        return qiNiuAccessKey;
    }

    @Override
    public String qiNiuSecretKey() {
        return qiNiuSecretKey;
    }

    @Override
    public String qiNiuBucket() {
        return qiNiuBucket;
    }

    @Override
    public String qiNiuRegion() {
        return qiNiuRegion;
    }

    @Override
    public String txSecretId() {
        return txSecretId;
    }

    @Override
    public String txSecretKey() {
        return txSecretKey;
    }

    @Override
    public String txBucket() {
        return txBucket;
    }

    @Override
    public String txRegion() {
        return txRegion;
    }

    @Override
    public String aliAccessKeyId() {
        return aliAccessKeyId;
    }

    @Override
    public String aliAccessKeySecret() {
        return aliAccessKeySecret;
    }

    @Override
    public String aliEndpoint() {
        return aliEndpoint;
    }

    @Override
    public String aliBucket() {
        return aliBucket;
    }

    @Override
    public boolean checkLocalConfig() {
        return StringUtils.isNotBlank(showUrl) && StringUtils.isNotBlank(localFileStoragePath);
    }

    @Override
    public boolean checkMinioConfig() {
        return StringUtils.isNotBlank(minioAccessKey) && StringUtils.isNotBlank(minioSecretKey)
                && StringUtils.isNotBlank(showUrl) && StringUtils.isNotBlank(minioBucket);
    }

    @Override
    public boolean checkQiNiuConfig() {
        return StringUtils.isNotBlank(showUrl) && StringUtils.isNotBlank(qiNiuAccessKey) && StringUtils.isNotBlank(qiNiuSecretKey)
                && StringUtils.isNotBlank(qiNiuBucket) && StringUtils.isNotBlank(qiNiuRegion);
    }

    @Override
    public boolean checkTxConfig() {
        return StringUtils.isNotBlank(showUrl) && StringUtils.isNotBlank(txSecretId) && StringUtils.isNotBlank(txSecretKey)
                && StringUtils.isNotBlank(txBucket) && StringUtils.isNotBlank(txRegion);
    }

    @Override
    public boolean checkAliConfig() {
        return StringUtils.isNotBlank(showUrl) && StringUtils.isNotBlank(aliAccessKeyId) && StringUtils.isNotBlank(aliAccessKeySecret)
                && StringUtils.isNotBlank(aliBucket) && StringUtils.isNotBlank(aliEndpoint);
    }


}
