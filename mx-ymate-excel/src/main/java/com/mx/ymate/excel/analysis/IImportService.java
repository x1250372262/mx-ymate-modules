package com.mx.ymate.excel.analysis;

import com.mx.ymate.dev.support.mvc.MxResult;
import net.ymate.platform.webmvc.IUploadFileWrapper;

/**
 * @Author: mengxiang.
 * @Date: 2024-10-11 17:00
 * @Description:
 */
public interface IImportService {

    /**
     * 导入excel
     * @param file
     * @return
     * @throws Exception
     */
    MxResult importExcel(IUploadFileWrapper file) throws Exception;
}
