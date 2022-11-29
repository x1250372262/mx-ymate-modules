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
package com.mx.ymate.upload.controller;

import com.mx.ymate.dev.result.MxResult;
import net.ymate.module.fileuploader.*;
import net.ymate.module.fileuploader.support.FileUploadSignatureValidator;
import net.ymate.platform.commons.util.ParamUtils;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.validation.validate.VLength;
import net.ymate.platform.validation.validate.VRequired;
import net.ymate.platform.webmvc.IUploadFileWrapper;
import net.ymate.platform.webmvc.annotation.*;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.cors.annotation.CrossDomain;
import net.ymate.platform.webmvc.util.WebUtils;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 文件资源
 *
 * @author 刘镇 (suninformation@163.com) on 2016/03/27 07:17
 * @since 1.0
 */
@Controller
@RequestMapping("/mx/upload")
@SignatureValidate(nonceName = "nonce", validatorClass = FileUploadSignatureValidator.class)
@CrossDomain
public class UploadController {

    @Inject
    private FileUploader fileUploader;


    /**
     * 文件上传
     *
     * @param file 上传的文件
     * @param type 指定结果处理器名称
     * @return 返回文件上传处理结果
     * @throws Exception 可能产生的任何异常
     */
    @RequestMapping(value = "/push", method = {Type.HttpMethod.POST, Type.HttpMethod.OPTIONS})
    @FileUpload
    public IView doUpload(@VRequired @RequestParam IUploadFileWrapper file, @RequestParam @VLength(max = 32) String type) throws Exception {
        if (StringUtils.isNotBlank(WebContext.getRequest().getHeader(Type.HttpHead.CONTENT_RANGE))) {
            // 暂不支持断点续传
            return HttpStatusView.BAD_REQUEST;
        }
        return doPush(new IFileWrapper.Default(file.getName(), file.getContentType(), file.getFile()), type);
    }

    private IView doPush(IFileWrapper fileWrapper, String type) throws Exception {
        try {
            UploadFileMeta fileMeta = fileUploader.upload(fileWrapper);
            fileMeta.setUrl(doFixedResourceUrl(fileMeta.getUrl()));
            if (StringUtils.isNotBlank(type)) {
                IUploadResultProcessor resultProcessor = fileUploader.getUploadResultProcessor(type);
                if (resultProcessor != null) {
                    return View.jsonView(resultProcessor.process(fileMeta)).withContentType();
                }
            }
            if (StringUtils.isNotBlank(fileMeta.getFilename())) {
                fileMeta.setFilename(WebUtils.decodeUrl(fileMeta.getFilename()));
            }
            return MxResult.ok().data(fileMeta).withContentType().toMxJsonView();
        } catch (ContentTypeNotAllowException e) {
            throw new FileUploadBase.InvalidContentTypeException(e.getMessage());
        }
    }


    private String doFixedResourceUrl(String resourceUrl) {
        if (StringUtils.isNotBlank(resourceUrl) && !StringUtils.startsWithAny(resourceUrl, new String[]{Type.Const.HTTP_PREFIX, Type.Const.HTTPS_PREFIX})) {
            String servicePrefix = ParamUtils.fixUrl(fileUploader.getConfig().getServicePrefix(), true, false);
            // 可通过 nodeId 配合 Nginx 等进行负载均衡路由
            String nodeId = fileUploader.getConfig().getNodeId();
            if (!StringUtils.equalsIgnoreCase(Type.Const.UNKNOWN, nodeId)) {
                servicePrefix = String.format("/%s%s", nodeId, servicePrefix);
            }
            return WebUtils.buildUrl(WebContext.getRequest(), String.format("%s/uploads/resources/%s", servicePrefix, resourceUrl), true);
        }
        return resourceUrl;

    }

}
