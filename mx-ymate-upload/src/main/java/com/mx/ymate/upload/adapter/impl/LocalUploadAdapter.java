package com.mx.ymate.upload.adapter.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.upload.bean.FileInfo;
import net.ymate.platform.commons.util.FileUtils;

import java.io.File;
import java.io.IOException;

import static com.mx.ymate.upload.code.MxUploadCode.MX_UPLOAD_ERROR;
import static com.mx.ymate.upload.code.MxUploadCode.MX_UPLOAD_LOCAL_CONFIG_ERROR;


/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:36.
 * @Description: 基于本地文件存储
 */
public class LocalUploadAdapter extends AbstractUploadAdapter {

    private File fileStorageDir;

    public LocalUploadAdapter() {
    }

    @Override
    protected MxResult checkConfig() {
        if (!config.checkLocalConfig()) {
            return MxResult.create(MX_UPLOAD_LOCAL_CONFIG_ERROR);
        }
        return MxResult.ok();
    }

    @Override
    protected MxResult saveFile(File srcFile, FileInfo fileInfo) {
        if (fileStorageDir == null || !fileStorageDir.exists()) {
            fileStorageDir = FileUtil.mkdir(config.localFileStoragePath());
        }
        try {
            File targetFile = new File(fileStorageDir,  fileInfo.getNewFileName());
            if (!targetFile.getParentFile().exists()) {
                FileUtil.mkdir( targetFile.getParentFile());
            }
            FileUtils.writeTo(srcFile, new File(fileStorageDir, fileInfo.getNewFileName()));
        } catch (IOException e) {
            return MxResult.create(MX_UPLOAD_ERROR).msg(StrUtil.format(MX_UPLOAD_ERROR.msg(), e.getMessage()));
        }
        return MxResult.ok();
    }

    @Override
    protected String getShowUrl() {
        return defaultShowUrl();
    }
}
