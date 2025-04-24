package com.mx.ymate.excel.controller;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.excel.analysis.IImportService;
import com.mx.ymate.excel.analysis.validate.annotation.VFile;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.webmvc.IUploadFileWrapper;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.FileUpload;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.annotation.RequestParam;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.view.IView;

import static com.mx.ymate.excel.ValidateConstant.FILE_I18N_KEY;
import static com.mx.ymate.excel.ValidateConstant.FILE_MSG;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
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
    public IView in(@VFile(msg = FILE_MSG, i18nKey = FILE_I18N_KEY)
                    @RequestParam IUploadFileWrapper file) throws Exception {
        if (iImportService == null) {
            return MxResult.fail().msg("请配置正确的导入服务").toJsonView();
        }
        return iImportService.importExcel(file).toJsonView();
    }
}
