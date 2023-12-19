package com.mx.ymate.excel.analysis.validate;

import com.mx.ymate.excel.analysis.validate.annotation.VFile;
import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.log.Logs;
import net.ymate.platform.validation.IValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import net.ymate.platform.webmvc.IUploadFileWrapper;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: mengxiang.
 * @Date: 2019/8/28.
 * @Time: 5:00 下午.
 * @Description:
 */
@Validator(VFile.class)
public class FileValidateImpl implements IValidator {

    private final List<String> ex = Arrays.asList("xls", "xlsx");

    @Override
    public ValidateResult validate(ValidateContext context) {
        if (context.getParamValue() != null) {
            IUploadFileWrapper iUploadFileWrapper = (IUploadFileWrapper) context.getParamValue();
            VFile anno = (VFile) context.getAnnotation();
            File file = null;
            try {
                file = iUploadFileWrapper.getFile();
            } catch (Exception e) {
                Logs.get().getLogger().error("文件解析异常", e);
            }
            //判断导入是否是excel
            if (file == null || !file.exists() || !file.isFile()) {
                return ValidateResult.builder(context, StringUtils.defaultIfBlank(anno.msg(), "excel文件不合法"), null, null).matched(true).build();
            } else {
                String extension = FileUtils.getExtName(file.getName());
                if (!ex.contains(extension)) {
                    return ValidateResult.builder(context, StringUtils.defaultIfBlank(anno.msg(), "excel文件不合法"), null, null).matched(true).build();
                }
            }
        }
        return null;
    }
}
