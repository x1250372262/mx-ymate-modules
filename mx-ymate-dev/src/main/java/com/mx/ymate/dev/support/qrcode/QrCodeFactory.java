package com.mx.ymate.dev.support.qrcode;

import cn.hutool.core.io.FileUtil;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mx.ymate.dev.support.qrcode.bean.QrCodeResult;
import net.ymate.platform.commons.QRCodeHelper;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.core.YMP;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.util.WebUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;

/**
 * @Author: mengxiang.
 * @Date: 2020/4/26.
 * @Time: 9:29 上午.
 * @Description:
 */
public class QrCodeFactory {

    private final String defaultCharset = "UTF-8";
    private final ErrorCorrectionLevel defaultLevel = ErrorCorrectionLevel.L;
    private final boolean createCode;
    public final static String FILE_PATH = YMP.get().getParam("mx.qrcode.file_path");
    private final String WEB_URL = YMP.get().getParam("mx.qrcode.web_url");

    private File logoFile;
    private int logoImageSize;
    private int borderWidth;
    private Color borderColor;
    private Color backgroundColor;

    private boolean isAddLogo = false;

    private QrCodeFactory(boolean createCode) {
        if (StringUtils.isBlank(FILE_PATH)) {
            throw new NullArgumentException("mx.qrcode.file_path");
        }
        this.createCode = createCode;
    }

    public QrCodeFactory setLogo(File logoFile, int logoImageSize, int borderWidth, Color borderColor, Color backgroundColor) {
        this.logoFile = logoFile;
        this.logoImageSize = logoImageSize;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
        this.isAddLogo = true;
        return this;
    }

    public static QrCodeFactory init() {
        return new QrCodeFactory(false);
    }

    public static QrCodeFactory init(boolean createCode) {
        return new QrCodeFactory(createCode);
    }


    public QrCodeResult create(String content, String characterSet, int width, int height, int margin, ErrorCorrectionLevel level, String format) throws Exception {
        if (StringUtils.isBlank(content)) {
            throw new NullArgumentException("content");
        }
        characterSet = StringUtils.defaultIfBlank(characterSet, defaultCharset);
        width = width > 0 ? width : 300;
        height = height > 0 ? height : 300;
        margin = margin > 0 ? margin : 1;
        format = StringUtils.defaultIfBlank(format, "png");
        level = level != null ? level : defaultLevel;
        String fileName = content.concat(characterSet)
                .concat(BlurObject.bind(width).toStringValue())
                .concat(BlurObject.bind(height).toStringValue())
                .concat(BlurObject.bind(margin).toStringValue())
                .concat(level.name()).concat(format);
        fileName = DigestUtils.md5Hex(fileName);
        String timeStr = DateTimeUtils.formatTime(System.currentTimeMillis(), "yyyyMMdd");
        File qrCodeDictionary = new File(FILE_PATH, timeStr);
        if (!qrCodeDictionary.exists()) {
            FileUtil.mkdir(qrCodeDictionary);
        }
        File qrCodeFile = new File(qrCodeDictionary, fileName.concat(".").concat(format));
        QRCodeHelper qrCodeHelper = null;
        if (!qrCodeFile.exists()) {
            qrCodeHelper = QRCodeHelper.create(content, characterSet, width, height, margin, level)
                    .setFormat(format);
        } else {
            if (createCode) {
                qrCodeHelper = QRCodeHelper.create(content, characterSet, width, height, margin, level).setFormat(format);
            }
        }
        if (qrCodeHelper != null) {
            if (isAddLogo) {
                qrCodeHelper.setLogo(logoFile, logoImageSize, borderWidth, borderColor, backgroundColor);
            }
            qrCodeHelper.writeToFile(qrCodeFile);
        }
        QrCodeResult qrCodeResult = new QrCodeResult();
        qrCodeResult.setQrCodeFile(qrCodeFile);
        String qrCodeUrl;
        if (StringUtils.isNotBlank(WEB_URL)) {
            qrCodeUrl = WEB_URL + "/" + timeStr + "/" + qrCodeFile.getName();
        } else {
            qrCodeUrl = WebUtils.baseUrl(WebContext.getRequest()).concat("mx/qrcode/show/" + timeStr + "/" + fileName + "/" + format);
        }
        qrCodeResult.setQrCodeUrl(qrCodeUrl);
        return qrCodeResult;
    }

    public QrCodeResult create(String content, int width, int height, int margin, ErrorCorrectionLevel level, String format) throws Exception {
        return create(content, defaultCharset, width, height, margin, level, format);
    }

    public QrCodeResult create(String content, int width, int height, int margin, String format) throws Exception {
        return create(content, width, height, margin, null, format);
    }

    public QrCodeResult create(String content, int width, int height, int margin) throws Exception {
        return create(content, width, height, margin, null);
    }

    public QrCodeResult create(String content, int width, int height) throws Exception {
        return create(content, width, height, 0);
    }

    public QrCodeResult create(String content, int width) throws Exception {
        return create(content, width, 0);
    }

    public QrCodeResult create(String content) throws Exception {
        return create(content, 0);
    }
}
