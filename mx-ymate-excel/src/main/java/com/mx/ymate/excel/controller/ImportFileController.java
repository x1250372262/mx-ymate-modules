package com.mx.ymate.excel.controller;

import com.mx.ymate.dev.result.MxResult;
import com.mx.ymate.excel.analysis.ExcelImportHelper;
import com.mx.ymate.excel.analysis.IImportService;
import com.mx.ymate.excel.analysis.validate.annotation.VFile;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.validation.validate.VRequired;
import net.ymate.platform.webmvc.IUploadFileWrapper;
import net.ymate.platform.webmvc.annotation.*;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.cors.annotation.CrossDomain;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @Author: 徐建鹏.
 * @Date: 2019/8/23.
 * @Time: 10:28 上午.
 * @Description:
 */
@Controller
@RequestMapping(value = "/mx/excel/import", method = Type.HttpMethod.OPTIONS)
@CrossDomain
public class ImportFileController {

    @Inject
    private IImportService iImportService;

    @RequestMapping(value = "/", method = Type.HttpMethod.POST)
    @FileUpload
    public IView in(@VRequired(msg = "请上传文件")
                    @VFile(msg = "请上传正确的excel文件")
                    @RequestParam IUploadFileWrapper file) throws Exception {
        if (iImportService == null) {
            return MxResult.fail().msg("请配置正确的导入服务").toMxJsonView();
        }
        return iImportService.importExcel(file).toMxJsonView();
    }
}
