package com.mx.ymate.dev.support.Ip2region;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;

public class IpRegionUtil {

    private final static Searcher SEARCHER;

    static {
        try {
            ClassPathResource resource = new ClassPathResource("ip2region.xdb");
            //获取真实文件路径
            String path = resource.getAbsolutePath();
            byte[] cBuff = Searcher.loadContentFromFile(path);
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
