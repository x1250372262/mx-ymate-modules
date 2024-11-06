package com.mx.ymate.upload.bean;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:36.
 * @Description: 上传结果
 */
public class Upload implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件哈希值
     */
    private String hash;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 扩展名称
     */
    private String extName;

    /**
     * 文件访问地址
     */
    private String url;

    /**
     * 图片文件的缩略图地址
     */
    private String thumbUrl;


    /**
     * 文件大小（字节）
     */
    private Long size;


    /**
     * 文件资源类型
     */
    private String type;

    /**
     * 是否是新传的
     */
    private boolean isNew;

    /**
     * 文件创建时间
     */
    private Long createTime;

    public Upload(String hash, String name, String extName, String url, String thumbUrl, Long size, String type, boolean isNew, Long createTime) {
        this.hash = hash;
        this.name = name;
        this.extName = extName;
        this.url = url;
        this.thumbUrl = thumbUrl;
        this.size = size;
        this.type = type;
        this.isNew = isNew;
        this.createTime = createTime;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Upload{" +
                "hash='" + hash + '\'' +
                ", name='" + name + '\'' +
                ", extName='" + extName + '\'' +
                ", url='" + url + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                ", isNew=" + isNew +
                ", createTime=" + createTime +
                '}';
    }
}


