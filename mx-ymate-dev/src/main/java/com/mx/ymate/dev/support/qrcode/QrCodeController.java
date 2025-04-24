package com.mx.ymate.dev.support.qrcode;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.mvc.i18n.validate.VMxRequired;
import com.mx.ymate.dev.support.qrcode.bean.QrCodeResult;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.PathVariable;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.annotation.RequestParam;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;
import net.ymate.platform.webmvc.view.impl.BinaryView;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

import static com.mx.ymate.dev.ValidateConstant.QR_CODE_NOT_EMPTY_I18N_KEY;
import static com.mx.ymate.dev.ValidateConstant.QR_CODE_NOT_EMPTY_MSG;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Controller
@RequestMapping("/mx/qrcode/")
public class QrCodeController {

    /**
     * 显示二维码
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    @RequestMapping("/show/{timeStr}/{fileName}/{format}")
    public IView show(@PathVariable(value = "fileName") String fileName,
                      @PathVariable(value = "timeStr") String timeStr,
                      @PathVariable(value = "format") String format,
                      @RequestParam(defaultValue = "false") boolean attach) throws Exception {
        String filePath = QrCodeFactory.FILE_PATH;
        if (StringUtils.isBlank(filePath)) {
            throw new NullArgumentException("mx.qrcode.file_path");
        }
        File qrCodeFile = new File(filePath.concat(File.separator).concat(timeStr), fileName.concat(".").concat(StringUtils.defaultIfBlank(format, "png")));
        if (!qrCodeFile.exists() || qrCodeFile.length() == 0) {
            return View.httpStatusView(404, "文件未找到");
        }
        BinaryView binaryView = BinaryView.bind(qrCodeFile);
        if (attach) {
            binaryView.useAttachment(fileName.concat(".").concat(StringUtils.defaultIfBlank(format, "png")));
        }
        return binaryView;
    }

    /**
     * 创建二维码
     *
     * @param content
     * @param characterSet
     * @param width
     * @param height
     * @param margin
     * @param level
     * @param format
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = {Type.HttpMethod.POST, Type.HttpMethod.GET})
    public IView create(@VMxRequired(msg = QR_CODE_NOT_EMPTY_MSG, i18nKey = QR_CODE_NOT_EMPTY_I18N_KEY)
                        @RequestParam String content,
                        @RequestParam(defaultValue = "UTF-8") String characterSet,
                        @RequestParam(defaultValue = "300") Integer width,
                        @RequestParam(defaultValue = "300") Integer height,
                        @RequestParam(defaultValue = "1") Integer margin,
                        @RequestParam(defaultValue = "l") String level,
                        @RequestParam(defaultValue = "png") String format,
                        @RequestParam(defaultValue = "false") Boolean createCode) throws Exception {
        ErrorCorrectionLevel errorCorrectionLevel;
        switch (level) {
            case "M":
                errorCorrectionLevel = ErrorCorrectionLevel.M;
                break;
            case "Q":
                errorCorrectionLevel = ErrorCorrectionLevel.Q;
                break;
            case "H":
                errorCorrectionLevel = ErrorCorrectionLevel.H;
                break;
            default:
                errorCorrectionLevel = ErrorCorrectionLevel.L;
                break;
        }
        QrCodeResult qrCodeResult = QrCodeFactory.init(createCode).create(content, characterSet, width, height, margin, errorCorrectionLevel, format);
        return MxResult.ok().data(qrCodeResult).toJsonView();
    }
}
