/*
 * Copyright 2007-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mx.ymate.upload.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.upload.config.MinioConfig;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.credentials.AssumeRoleBaseProvider;
import net.ymate.module.fileuploader.*;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2016/03/31 01:50
 * @since 1.0
 */
public class MinioFileStorageAdapter extends AbstractFileStorageAdapter {

    private static final Log LOG = LogFactory.getLog(MinioFileStorageAdapter.class);

    private MinioConfig minioConfig;

    private MinioClient minioClient;

    @Override
    protected void doInitialize() throws Exception {
        if(minioConfig == null){
            minioConfig = new MinioConfig();
        }
        if(minioClient == null){
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            minioClient = MinioClient.builder().endpoint(minioConfig.getUrl()).credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();
        }
    }

    @Override
    public boolean isExists(UploadFileMeta fileMeta) {
       return false;
    }

    private String getFilePath(String type, String filename) {
        //路径格式 例如 image/20210/01/01/hash.png
        long time = System.currentTimeMillis();
        String year = DateTimeUtils.formatTime(time, "yyyy");
        String month = DateTimeUtils.formatTime(time, "MM");
        String day = DateTimeUtils.formatTime(time, "dd");
        return StrUtil.format("{}/{}/{}/{}/{}", type, year, month, day, filename);
    }

    private String getUrl(String url) {
        if (!url.endsWith(StrUtil.SLASH)) {
            url = url + StrUtil.SLASH;
        }
        return url;
    }

    @Override
    public UploadFileMeta writeFile(String hash, IFileWrapper file) throws Exception {
        ResourceType resourceType = ResourceType.valueOf(StringUtils.substringBefore(file.getContentType(), IResourcesProcessor.URL_SEPARATOR).toUpperCase());
        Map<String, Object> attributes = doBuildFileAttributes(hash, resourceType, file);
        // 路径格式 例如 image/20210/01/01/hash.png
        String extension = StringUtils.trimToNull(file.getSuffix());
        String filename = StringUtils.join(new Object[]{hash, extension}, FileUtils.POINT_CHAR);
        String newFileName = getFilePath(resourceType.name(),filename);
        // 重新生成一个文件名
        InputStream inputStram = file.getInputStream();
        minioClient.putObject(PutObjectArgs.builder().bucket(minioConfig.getBucket())
                .object(newFileName)
                .stream(inputStram, file.getContentLength(), -1)
                .contentType(file.getContentType())
                .build());
        String url = getUrl(minioConfig.getUrl()) + minioConfig.getBucket() + "/" + newFileName;
        long lastModifyTime = file.getLastModifyTime();
        return UploadFileMeta.builder()
                .hash(hash)
                .filename(filename)
                .extension(extension)
                .size(file.getContentLength())
                .mimeType(file.getContentType())
                .type(resourceType)
                .status(0)
                .url(url)
                .createTime(lastModifyTime)
                .lastModifyTime(lastModifyTime)
                .attributes(attributes)
                .build();
    }

    @Override
    public File readFile(String hash, String sourcePath) {
        return null;
    }

    @Override
    public File getThumbStoragePath() {
        return null;
    }
}
