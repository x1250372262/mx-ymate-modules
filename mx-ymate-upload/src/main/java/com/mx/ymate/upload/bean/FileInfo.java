package com.mx.ymate.upload.bean;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:36.
 * @Description: 文件信息
 */
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件哈希值
     */
    private String hash;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 扩展名称
     */
    private String extName;


    /**
     * 文件资源类型
     */
    private String type;

    /**
     * 数据文件
     */
    private Upload upload;

    /**
     * 新文件名
     */
    private String newFileName;

    /**
     * 文件数据流
     */
    private InputStream inputStream;

    public FileInfo(String hash, long fileSize, String fileName, String extName, String type, Upload upload, String newFileName, InputStream inputStream) {
        this.hash = hash;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.extName = extName;
        this.type = type;
        this.upload = upload;
        this.newFileName = newFileName;
        this.inputStream = inputStream;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }


}


