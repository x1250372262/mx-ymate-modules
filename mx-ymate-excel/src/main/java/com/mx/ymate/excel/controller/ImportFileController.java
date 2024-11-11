package com.mx.ymate.excel.controller;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.excel.analysis.IImportService;
import com.mx.ymate.excel.analysis.validate.annotation.VFile;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.validation.validate.VRequired;
import net.ymate.platform.webmvc.IUploadFileWrapper;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.FileUpload;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.annotation.RequestParam;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.view.IView;

/**
 * @Author: mengxiang.
 * @Date: 2019/8/23.
 * @Time: 10:28 上午.
 * @Description:
 */
@Controller
@RequestMapping(value = "/mx/excel/import", method = Type.HttpMethod.OPTIONS)
public class ImportFileController {

    @Inject
    private IImportService iImportService;

    /**
     * 导入文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/", method = Type.HttpMethod.POST)
    @FileUpload
    public IView in(@VRequired(msg = "请上传文件")
                    @VFile(msg = "请上传正确的excel文件")
                    @RequestParam IUploadFileWrapper file) throws Exception {
        if (iImportService == null) {
            return MxResult.fail().msg("请配置正确的导入服务").toJsonView();
        }
        return iImportService.importExcel(file).toJsonView();
    }
}
