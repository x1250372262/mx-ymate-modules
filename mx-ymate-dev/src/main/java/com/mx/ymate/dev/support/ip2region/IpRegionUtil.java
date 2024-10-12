package com.mx.ymate.dev.support.ip2region;

import cn.hutool.core.util.StrUtil;
import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.log.Logs;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.File;
import java.io.InputStream;

/**
 * @Author: mengxiang.
 * @create: 2023-08-03 16:58
 * @Description: 显示ip地址工具类
 */
public class IpRegionUtil {

    private final static Searcher SEARCHER;

    static {
        try {
            File file = new File(RuntimeUtils.getRootPath(), "ip2region.xdb");
            Logs.get().getLogger().info("ip数据库" + file.getAbsolutePath());
            if (!file.exists()) {
                InputStream inputStream = IpRegionUtil.class.getClassLoader().getResourceAsStream("ip2region.xdb");
                FileUtils.createFileIfNotExists(file, inputStream);
            }
            byte[] cBuff = Searcher.loadContentFromFile(file.getAbsolutePath());
            SEARCHER = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            throw new RuntimeException(StrUtil.format("初始化ip2region失败,异常原因{}", e.getMessage()));
        }
    }

    public static void main(String[] args) {
        try {
            String ipRegionBean = parseStr("175.171.53.247", "");
            System.out.println(ipRegionBean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static IpRegionBean parse(String ip) {
        String region = parseStr(ip);
        String[] split = region.split("\\|");
        return new IpRegionBean(split[0], split[2], split[3], split[4]);
    }

    public static String parseStr(String ip) {
        return parseStr(ip, null);
    }

    public static String parseStr(String ip, String replace) {
        if (StringUtils.isBlank(ip)) {
            throw new NullArgumentException("ip");
        }
        String region;
        try {
            region = SEARCHER.search(ip);
            if (replace != null) {
                region = region.replaceAll("\\|", replace);
            }
        } catch (Exception e) {
            throw new RuntimeException("地址获取失败", e);
        }
        return region;
    }
}
