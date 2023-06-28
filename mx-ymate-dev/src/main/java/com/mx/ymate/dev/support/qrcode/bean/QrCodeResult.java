package com.mx.ymate.dev.support.qrcode.bean;

import java.io.File;

/**
 * @Author: mengxiang.
 * @Date: 2020/4/26.
 * @Time: 9:23 上午.
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
