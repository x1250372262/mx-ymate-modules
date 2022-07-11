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
import net.ymate.platform.validation.validate.VDataRange;
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
import net.ymate.platform.webmvc.view.impl.BinaryView;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    @RequestMapping(value = "/chunk/push", method = {Type.HttpMethod.POST, Type.HttpMethod.OPTIONS})
    @FileUpload
    public IView doChunkUpload() {
        return HttpStatusView.BAD_REQUEST;
    }

    @RequestMapping(value = "/chunk/match", method = {Type.HttpMethod.POST, Type.HttpMethod.OPTIONS})
    public IView doChunkMatch() {
        return HttpStatusView.BAD_REQUEST;
    }

    @RequestMapping(value = "/chunk/merge", method = {Type.HttpMethod.POST, Type.HttpMethod.OPTIONS})
    public IView doChunkMerge() {
        return HttpStatusView.BAD_REQUEST;
    }

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

    /**
     * 文件哈希值检测
     *
     * @param hash 文件哈希值
     * @return 通过匹配文件哈希值判断服务端文件是否已存在
     * @throws Exception 可能产生的任何异常
     */
    @RequestMapping(value = "/match", method = {Type.HttpMethod.POST, Type.HttpMethod.OPTIONS})
    public IView doMatch(@VRequired
                         @VLength(eq = 32)
                         @RequestParam String hash) throws Exception {
        UploadFileMeta fileMeta = fileUploader.match(hash);
        if (fileMeta != null) {
            fileMeta.setUrl(doFixedResourceUrl(fileMeta.getUrl()));
            return MxResult.ok().attr("matched", true).data(fileMeta).withContentType().toMxJsonView();
        }
        return MxResult.ok().attr("matched", false).withContentType().toMxJsonView();
    }

    /**
     * 文件资源访问<br>
     * 若请求的URL中包含参数'?attach'则强制下载并支持通过?attach=FILE_NAME方式自定义文件名称<br>
     * 若请求的资源为未知或APPLICATION类型资源默认全部强制下载<br>
     * 若请求THUMB资源时将判断目标资源类型返回相应的缩略图(排除未知/TEXT/APPLICATION类型)<br>
     *
     * @param type 资源类型
     * @param hash 文件哈希值
     * @return 返回文件资料数据流
     * @throws Exception 可能产生的任何异常
     */
    @RequestMapping("/resources/{type}/{hash}")
    @SignatureValidate(disabled = true)
    public IView doResources(@PathVariable @VDataRange({"IMAGE", "VIDEO", "AUDIO", "TEXT", "APPLICATION", "THUMB"}) String type,
                             @PathVariable @VLength(max = 50) String hash) throws Exception {
        IView returnView = null;
        // 判断资源类型
        try {
            ResourceType resourceType = ResourceType.valueOf(type.toUpperCase());
            IFileWrapper fileWrapper = fileUploader.resources(resourceType, hash);
            if (fileWrapper != null) {
                if (doBrowserCacheWarp(fileWrapper.getLastModifyTime(), fileUploader.getConfig().getResourcesCacheTimeout())) {
                    returnView = View.httpStatusView(HttpServletResponse.SC_NOT_MODIFIED);
                } else {
                    BinaryView binaryView = new BinaryView(fileWrapper.getInputStream(), fileWrapper.getContentLength());
                    binaryView.setContentType(fileWrapper.getContentType());
                    // 判断是否采用强制下载
                    boolean hasAttach = WebContext.getContext().getParameters().containsKey("attach");
                    if (hasAttach) {
                        String attachName = WebContext.getRequest().getParameter("attach");
                        if (!doCheckAttachName(attachName)) {
                            attachName = StringUtils.substringAfterLast(fileWrapper.getFileName(), File.separator);
                        }
                        binaryView.useAttachment(attachName);
                    }
                    returnView = binaryView;
                }
            }
        } catch (IllegalArgumentException e) {
            returnView = View.httpStatusView(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ResourcesAccessException e) {
            returnView = View.httpStatusView(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
        return returnView == null ? HttpStatusView.NOT_FOUND : returnView;
    }

    private boolean doCheckAttachName(String attachName) {
        return attachName != null && attachName.length() <= 255 && attachName.matches("[^\\s\\\\/:*?\"<>|](\\x20|[^\\s\\\\/:*?\"<>|])*[^\\s\\\\/:*?\"<>|.]$");
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

    private boolean doBrowserCacheWarp(long lastModifyTime, int cacheTimeout) {
        //
        long lastModified = TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(lastModifyTime));
        long expiresTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(cacheTimeout);
        String eTag = String.format("\"%d\"", lastModifyTime + cacheTimeout);
        //
        HttpServletResponse httpServletResponse = WebContext.getResponse();
        httpServletResponse.setDateHeader(Type.HttpHead.LAST_MODIFIED, lastModified);
        httpServletResponse.setDateHeader(Type.HttpHead.EXPIRES, expiresTime);
        httpServletResponse.setHeader(Type.HttpHead.CACHE_CONTROL, String.format("%s=%d", Type.HttpHead.MAX_AGE, cacheTimeout));
        httpServletResponse.setHeader(Type.HttpHead.ETAG, eTag);
        //
        HttpServletRequest httpServletRequest = WebContext.getRequest();
        String ifNoneMatch = httpServletRequest.getHeader(Type.HttpHead.IF_NONE_MATCH);
        if (eTag.equals(ifNoneMatch)) {
            return true;
        } else {
            long ifModifiedSince = httpServletRequest.getDateHeader(Type.HttpHead.IF_MODIFIED_SINCE);
            if (ifModifiedSince != -1) {
                Date requestDate = new Date(ifModifiedSince);
                Date pageDate = new Date(lastModifyTime);
                if (!requestDate.before(pageDate)) {
                    httpServletResponse.setHeader(Type.HttpHead.LAST_MODIFIED, httpServletRequest.getHeader(Type.HttpHead.IF_MODIFIED_SINCE));
                    return true;
                }
            }
        }
        return false;
    }
}
