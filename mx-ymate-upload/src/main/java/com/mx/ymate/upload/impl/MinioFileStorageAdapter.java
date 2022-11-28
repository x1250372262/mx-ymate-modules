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

import net.ymate.module.fileuploader.*;
import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2016/03/31 01:50
 * @since 1.0
 */
public class MinioFileStorageAdapter extends AbstractFileStorageAdapter {

    private static final Log LOG = LogFactory.getLog(MinioFileStorageAdapter.class);

    private File fileStoragePath;

    private File thumbStoragePath;

    @Override
    protected void doInitialize() throws Exception {
        this.fileStoragePath = doCheckAndFixStorageDir(IFileUploaderConfig.FILE_STORAGE_PATH, new File(RuntimeUtils.replaceEnvVariable(StringUtils.defaultIfBlank(getOwner().getConfig().getFileStoragePath(), IFileUploaderConfig.DEFAULT_STORAGE_PATH))), true);
        if (StringUtils.isBlank(getOwner().getConfig().getThumbStoragePath())) {
            this.thumbStoragePath = new File(this.fileStoragePath.getPath());
        } else {
            this.thumbStoragePath = doCheckAndFixStorageDir(IFileUploaderConfig.THUMB_STORAGE_PATH, new File(RuntimeUtils.replaceEnvVariable(getOwner().getConfig().getThumbStoragePath())), true);
        }
    }

    @Override
    public boolean isExists(UploadFileMeta fileMeta) {
        File target = new File(fileStoragePath, fileMeta.getSourcePath());
        return target.exists() && target.isFile();
    }

    @Override
    public UploadFileMeta writeFile(String hash, IFileWrapper file) throws Exception {
        ResourceType resourceType = ResourceType.valueOf(StringUtils.substringBefore(file.getContentType(), IResourcesProcessor.URL_SEPARATOR).toUpperCase());
        Map<String, Object> attributes = doBuildFileAttributes(hash, resourceType, file);
        // 转存文件，路径格式：{TYPE_NAME}/{FILE_HASH_1-2BIT}/{FILE_HASH_3-4BIT}/{FILE_HASH_32BIT}.{EXT}
        String extension = StringUtils.trimToNull(file.getSuffix());
        String filename = StringUtils.join(new Object[]{hash, extension}, FileUtils.POINT_CHAR);
        String sourcePathDir = UploadFileMeta.buildSourcePath(resourceType, hash);
        String sourcePath = String.format("%s/%s", sourcePathDir, filename);
        File targetFile = new File(fileStoragePath, sourcePath);
        if (targetFile.getParentFile().mkdirs() && LOG.isInfoEnabled()) {
            LOG.info(String.format("Successfully created directory: %s", targetFile.getParentFile().getPath()));
        }
        file.writeTo(targetFile);
        //
        doAfterWriteFile(resourceType, targetFile, sourcePathDir, thumbStoragePath.getPath(), hash);
        //
        long lastModifyTime = file.getLastModifyTime();
        return UploadFileMeta.builder()
                .hash(hash)
                .filename(filename)
                .extension(extension)
                .size(file.getContentLength())
                .mimeType(file.getContentType())
                .type(resourceType)
                .status(0)
                .sourcePath(sourcePath)
                .createTime(lastModifyTime)
                .lastModifyTime(lastModifyTime)
                .attributes(attributes)
                .build();
    }

    @Override
    public File readFile(String hash, String sourcePath) {
        return new File(fileStoragePath, sourcePath);
    }

    @Override
    public File getThumbStoragePath() {
        return thumbStoragePath;
    }
}
