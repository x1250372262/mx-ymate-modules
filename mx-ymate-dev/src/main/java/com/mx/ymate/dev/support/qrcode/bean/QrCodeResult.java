package com.mx.ymate.dev.support.qrcode.bean;

import java.io.File;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class QrCodeResult {

    private File qrCodeFile;

    private String qrCodeUrl;

    public File getQrCodeFile() {
        return qrCodeFile;
    }

    public void setQrCodeFile(File qrCodeFile) {
        this.qrCodeFile = qrCodeFile;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
}
