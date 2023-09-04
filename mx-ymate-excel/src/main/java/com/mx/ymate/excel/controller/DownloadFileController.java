package com.mx.ymate.excel.controller;

import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.PathVariable;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.cors.annotation.CrossDomain;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @Author: mengxiang.
 * @Date: 2019/8/23.
 * @Time: 10:28 上午.
 * @Description:
 */
@Controller
@RequestMapping("/mx/excel/download")
@CrossDomain
public class DownloadFileController {

    /**
     * 下载zip文件
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping("/zip/{name}")
    public IView zip(@PathVariable String name) throws Exception {
        IView returnView;
        // 判断资源类型
        try {
            String filePath = RuntimeUtils.getRootPath() + File.separator + "zip" + File.separator;
            File file = new File(filePath, name+".zip");
            returnView = View.binaryView(file).useAttachment(file.getName());
        } catch (Exception e) {
            returnView = View.httpStatusView(HttpServletResponse.SC_BAD_REQUEST);
        }
        return returnView;
    }

    /**
     * 下载普通excel
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping("/excel/{name}")
    public IView excel(@PathVariable String name) throws Exception {
        IView returnView;
        // 判断资源类型
        try {
            String filePath = RuntimeUtils.getRootPath() + File.separator + "export" + File.separator;
            File file = new File(filePath, name+".xlsx");
            returnView = View.binaryView(file).useAttachment(file.getName());
        } catch (Exception e) {
            returnView = View.httpStatusView(HttpServletResponse.SC_BAD_REQUEST);
        }
        return returnView;
    }
}
