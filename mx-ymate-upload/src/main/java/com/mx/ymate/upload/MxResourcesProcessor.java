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
package com.mx.ymate.upload;

import net.ymate.module.fileuploader.AbstractResourcesProcessor;
import net.ymate.module.fileuploader.IFileWrapper;
import net.ymate.module.fileuploader.ResourceType;
import net.ymate.module.fileuploader.UploadFileMeta;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 2019-01-03 19:08
 * @since 1.0
 */
public class MxResourcesProcessor extends AbstractResourcesProcessor {

    private static final Log LOG = LogFactory.getLog(MxResourcesProcessor.class);


    @Override
    protected UploadFileMeta doMatchHash(String hash, ResourceType resourceType) throws Exception {
        return null;
    }

    @Override
    public UploadFileMeta upload(IFileWrapper fileWrapper) throws Exception {
        return super.upload(fileWrapper);
    }
}
