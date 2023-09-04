package com.mx.ymate.excel.analysis;

import com.mx.ymate.dev.support.mvc.MxResult;
import net.ymate.platform.webmvc.IUploadFileWrapper;

public interface IImportService {

    MxResult importExcel(IUploadFileWrapper file) throws Exception;
}
