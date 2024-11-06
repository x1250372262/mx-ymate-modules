package com.mx.ymate.upload;

import com.mx.ymate.upload.adapter.IUploadAdapter;
import net.ymate.platform.core.beans.annotation.Ignored;
import net.ymate.platform.core.support.IInitialization;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:36.
 * @Description:
 */
@Ignored
public interface IMxUploadConfig extends IInitialization<IMxUpload> {

    String ENABLED = "enabled";
    String MAX_SIZE = "maxSize";
    String CONTENT_TYPE = "contentType";
    String SHOW_URL = "showUrl";
    String ADAPTER = "adapter";
    String JSON_FILE_PATH = "jsonFilePath";
    String IS_CREATE_THUMB = "isCreateThumb";
    String THUMB_WIDTH = "thumbWidth";
    String THUMB_HEIGHT = "thumbHeight";
    String THUMB_QUALITY = "thumbQuality";
    String LOCAL_FILE_STORAGE_PATH = "local.fileStoragePath";
    String MINIO_ACCESS_KEY = "minio.accessKey";
    String MINIO_SECRET_KEY = "minio.secretKey";
    String MINIO_BUCKET = "minio.bucket";
    String QINIU_ACCESS_KEY = "qiniu.accessKey";
    String QINIU_SECRET_KEY = "qiniu.secretKey";
    String QINIU_BUCKET = "qiniu.bucket";
    String QINIU_REGION = "qiniu.region";
    String TX_SECRET_ID = "tx.secretId";
    String TX_SECRET_KEY = "tx.secretKey";
    String TX_BUCKET = "tx.bucket";
    String TX_REGION = "tx.region";
    String ALI_ACCESS_KEY_ID = "ali.accessKeyId";
    String ALI_ACCESS_KEY_SECRET = "ali.accessKeySecret";
    String ALI_ENDPOINT = "ali.endpoint";
    String ALI_BUCKET = "ali.bucket";


    /**
     * 模块是否已启用, 默认值: true
     *
     * @return 返回false表示禁用
     */
    boolean isEnabled();

    /**
     * 上传文件大小最大值(M) 默认10M
     * @return
     */
    long maxSize();

    /**
     * 允许上传的contentType列表  用|分割 默认不限制 image|video
     * @return
     */
    List<String> contentTypeList();

    /**
     * 文件访问的展示url地址 阿里示例:https://bucket.endpoint
     * @return
     */
    String showUrl();

    /**
     * 文件上传适配器类型
     * 默认local基于本地 可选值:qiniu|minio|tx|ali 支持自定义只需要实现com.mx.ymate.upload.adapter.IUploadAdapter即可
     * @return
     */
    IUploadAdapter adapter();

    /**
     * 文件上传之后信息存放位置 如果不设置 则不保留   主要为了实现重复文件不多次上传 而是直接就可以返回结果  用adapter+文件hash判断
     * @return
     */
    String jsonFilePath();

    /**
     * 文件上传成功后图片是否生成缩略图 默认false
     * @return
     */
    boolean isCreateThumb();

    /**
     * 缩略图宽
     * @return
     */
    int thumbWidth();

    /**
     * 缩略图高
     * @return
     */
    int thumbHeight();

    /**
     * 缩略图清晰度, 如: 0.70f, 默认值: 0f
     * @return
     */
    float thumbQuality();

    /**
     * 文件存储位置
     * @return
     */
    String localFileStoragePath();

    /**
     * 公钥
     * @return
     */
    String minioAccessKey();

    /**
     * 私钥
     * @return
     */
    String minioSecretKey();

    /**
     * 桶
     * @return
     */
    String minioBucket();

    /**
     * 公钥ak
     * @return
     */
    String qiNiuAccessKey();

    /**
     * 私钥sk
     * @return
     */
    String qiNiuSecretKey();

    /**
     * 空间名称
     * @return
     */
    String qiNiuBucket();

    /**
     * 地域 不配置会自动判断区域，使用相应域名处理。
     * 如果可以明确 区域 的话，最好指定固定区域，这样可以少一步网络请求，少一步出错的可能。
     * https://developer.qiniu.com/kodo/1671/region-endpoint-fq  具体配置参考这个网址
     * @return
     */
    String qiNiuRegion();

    /**
     * secretId
     * @return
     */
    String txSecretId();

    /**
     * secretKey
     * @return
     */
    String txSecretKey();

    /**
     * 存储桶
     * @return
     */
    String txBucket();

    /**
     * 地域
     * @return
     */
    String txRegion();

    /**
     * accessKeyId
     * @return
     */
    String aliAccessKeyId();

    /**
     * accessKeySecret
     * @return
     */
    String aliAccessKeySecret();

    /**
     * 地址
     * @return
     */
    String aliEndpoint();

    /**
     * 存储桶
     * @return
     */
    String aliBucket();

    /**
     * 检查本地配置
     * @return
     */
    boolean checkLocalConfig();

    /**
     * 检查Minio配置
     * @return
     */
    boolean checkMinioConfig();

    /**
     * 检查七牛配置
     * @return
     */
    boolean checkQiNiuConfig();

    /**
     * 检查腾讯配置
     * @return
     */
    boolean checkTxConfig();

    /**
     * 检查阿里配置
     * @return
     */
    boolean checkAliConfig();
}
