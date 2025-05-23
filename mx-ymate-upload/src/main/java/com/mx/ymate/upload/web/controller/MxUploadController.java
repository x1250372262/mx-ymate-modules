package com.mx.ymate.upload.web.controller;

import com.mx.ymate.dev.support.mvc.i18n.validate.VMxRequired;
import com.mx.ymate.upload.web.service.IMxUploadService;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.webmvc.IUploadFileWrapper;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.FileUpload;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.annotation.RequestParam;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.view.IView;

import static com.mx.ymate.upload.ValidateConstant.FILE_I18N_KEY;
import static com.mx.ymate.upload.ValidateConstant.FILE_MSG;

/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:27.
 * @Description:
 */
@Controller
@RequestMapping("/mx/upload")
public class MxUploadController {

    @Inject
    private IMxUploadService iMxUploadService;

    /**
     * 文件上传
     *
     * @param file 上传的文件
     * @return 返回文件上传处理结果
     * @throws Exception 可能产生的任何异常
     */
    @RequestMapping(value = "/push", method = {Type.HttpMethod.POST, Type.HttpMethod.OPTIONS})
    @FileUpload
    public IView push(@VMxRequired(msg = FILE_MSG, i18nKey = FILE_I18N_KEY)
                      @RequestParam IUploadFileWrapper file) throws Exception {
        return iMxUploadService.push(file).toJsonView();
    }
}
