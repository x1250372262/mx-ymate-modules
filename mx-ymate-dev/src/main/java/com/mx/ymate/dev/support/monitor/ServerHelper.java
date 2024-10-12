package com.mx.ymate.dev.support.monitor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.mx.ymate.dev.support.monitor.bean.DiskBean;
import com.mx.ymate.dev.support.monitor.bean.NetworkBean;
import com.mx.ymate.dev.support.monitor.bean.ServerBean;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.log.Logs;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mengxiang.
 * @Date: 2024-10-11 17:00
 * @Description:
 */
public class ServerHelper {

    private static final HardwareAbstractionLayer ABSTRACTION_LAYER;

    private static final OperatingSystem OPERATING_SYSTEM;

    private static final GlobalMemory GLOBAL_MEMORY;

    private static final int GB = 1024 * 1024 * 1024;


    private ServerHelper() {
    }


    static {
        SystemInfo systemInfo = new SystemInfo();
        ABSTRACTION_LAYER = systemInfo.getHardware();
        OPERATING_SYSTEM = systemInfo.getOperatingSystem();
        GLOBAL_MEMORY = ABSTRACTION_LAYER.getMemory();
    }


    /**
     * 获取磁盘列表信息
     *
     * @return
     */
    public static List<DiskBean> getDisksList() {
        FileSystem fileSystem = OPERATING_SYSTEM.getFileSystem();
        List<OSFileStore> fileStores = fileSystem.getFileStores();
        List<DiskBean> list = new ArrayList<>();
        for (OSFileStore osFileStore : fileStores) {
            double total = NumberUtil.div(osFileStore.getTotalSpace(), GB, 2);
            double free = NumberUtil.div(osFileStore.getUsableSpace(), GB, 2);
            double used = NumberUtil.sub(total, free, 2).doubleValue();
            if (used < 0) {
                continue;
            }
            DiskBean diskBean = new DiskBean();
            diskBean.setDirName(osFileStore.getMount());
            String name = osFileStore.getName();
            diskBean.setSysTypeName(name);
            diskBean.setTypeName(osFileStore.getType());
            diskBean.setTotal(total);
            diskBean.setFree(free);
            diskBean.setUsed(used);
            if (total != 0) {
                diskBean.setUsage(NumberUtil.div(used, total, 2));
            }
            list.add(diskBean);
        }
        return list;
    }

    /**
     * 网络信息
     *
     * @return
     */
    public static List<NetworkBean> getNetworkInfo() {
        List<NetworkBean> list = new ArrayList<>();
        for (NetworkIF networkIf : ABSTRACTION_LAYER.getNetworkIFs()) {
            if (!networkIf.isKnownVmMacAddr() && networkIf.getMacaddr() != null && networkIf.getIPv4addr().length > 0
                    && networkIf.getIPv6addr().length > 0) {
                NetworkBean networkBean = new NetworkBean();
                networkBean.setIpv4Address(networkIf.getIPv4addr()[0]);
                networkBean.setIpv6Address(networkIf.getIPv6addr()[0]);
                networkBean.setMacAddress(networkIf.getMacaddr());
                networkBean.setNetworkName(networkIf.getName());
                list.add(networkBean);
            }

        }
        return list;
    }


    public static ServerBean getServerInfo() {
        ServerBean osBean = new ServerBean();
        OsInfo osInfo = SystemUtil.getOsInfo();
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        //操作系统
        osBean.setOs(osInfo.getName());
        //系统架构
        osBean.setOsArch(osInfo.getArch());
        //java版本
        osBean.setJavaVersion(SystemUtil.getJavaInfo().getVersion());
        //工作目录
        osBean.setUserDir(SystemUtil.getUserInfo().getHomeDir());
        //CPU核数
        osBean.setCpuCount(cpuInfo.getCpuNum());
        //cpu型号
        osBean.setCpuModel(cpuInfo.getCpuModel());
        //cpu使用率
        osBean.setCpuUsage(cpuInfo.getUsed());
        //主机信息
        try {
            InetAddress address = InetAddress.getLocalHost();
            osBean.setHost(address.getHostAddress());
            osBean.setHostName(address.getHostName());
        } catch (UnknownHostException e) {
            Logs.get().getLogger().error("主机信息获取失败");
        }
        //系统启动时间
        long now = DateTimeUtils.systemTimeUTC();
        long startTime = OshiUtil.getOs().getSystemUptime();
        osBean.setBootTime(now - startTime);
        osBean.setRunTime(DateUtil.formatBetween(startTime * 1000));
        double memoryTotal = NumberUtil.div(GLOBAL_MEMORY.getTotal(), GB, 2);
        double available = NumberUtil.div(GLOBAL_MEMORY.getAvailable(), GB, 2);
        double used = memoryTotal - available;
        //系统内存总量
        osBean.setTotalMemory(memoryTotal);
        //系统使用量
        osBean.setUsedMemory(used);

        osBean.setMemoryUsage(NumberUtil.div(used, memoryTotal, 2));
        //可用虚拟总内存
        osBean.setSwapTotalMemory(NumberUtil.div(GLOBAL_MEMORY.getVirtualMemory().getSwapTotal(), GB, 2));
        //已用虚拟内存
        osBean.setSwapUsedMemory(NumberUtil.div(GLOBAL_MEMORY.getVirtualMemory().getSwapUsed(), GB, 2));
        //磁盘信息
        osBean.setDisksList(getDisksList());
        //网卡信息
        osBean.setNetworkList(getNetworkInfo());

        return osBean;
    }

    public static void main(String[] args) throws Exception {

        //系统信息
        System.out.println("-----------系统信息-----------");
        ServerBean osInfo = getServerInfo();
        System.out.println("操作系统：" + osInfo.getOs());
        System.out.println("系统架构：" + osInfo.getOsArch());
        System.out.println("Java版本：" + osInfo.getJavaVersion());
        System.out.println("工作目录：" + osInfo.getUserDir());
        System.out.println("cpu核心数：" + osInfo.getCpuCount());
        System.out.println("主机host：" + osInfo.getHost());
        System.out.println("主机名称：" + osInfo.getHostName());
        Long bootTime = osInfo.getBootTime();
        String runTime = osInfo.getRunTime();
        System.out.println("系统正常启动时间：" + DateTimeUtils.formatTime(bootTime, DateTimeUtils.YYYY_MM_DD_HH_MM_SS));
        System.out.println("系统正常运行时长：" + runTime);
        //运行时信息
        System.out.println("-----------运行时信息-----------");
        //1.CPU信息
        System.out.println("------cpu信息------");
        System.out.println("cpu型号：" + osInfo.getCpuModel());
        System.out.println("cpu使用率：" + osInfo.getCpuUsage());
        //2.内存信息
        System.out.println("------内存信息------");
        //系统内存总量
        Double total = osInfo.getTotalMemory();
        Double used = osInfo.getUsedMemory();
        System.out.println("系统内存总量：" + total + " -> " + total);
        System.out.println("系统内存使用量：" + used + " -> " + used);
        System.out.println("系统内存使用率：" + osInfo.getMemoryUsage());
        //可用虚拟总内存
        Double swapTotal = osInfo.getSwapTotalMemory();
        //已用虚拟内存
        Double swapUsed = osInfo.getSwapUsedMemory();
        System.out.println("可用虚拟总内存(swap)：" + swapTotal + " -> " + swapTotal);
        System.out.println("虚拟内存使用量(swap)：" + swapUsed + " -> " + swapUsed);
        //3.磁盘信息
        System.out.println("------磁盘信息------");
        List<DiskBean> disksList = osInfo.getDisksList();
        for (DiskBean disksInfo : disksList) {
            System.out.println("挂载点：" + disksInfo.getDirName());
            System.out.println("文件系统名称：" + disksInfo.getSysTypeName());
            System.out.println("文件系统类型：" + disksInfo.getTypeName());
            System.out.println("磁盘总量：" + disksInfo.getTotal() + " -> " + disksInfo.getTotal());
            System.out.println("磁盘使用量：" + disksInfo.getUsed() + " -> " + disksInfo.getUsed());
            System.out.println("磁盘剩余量：" + disksInfo.getFree() + " -> " + disksInfo.getFree());
            System.out.println("磁盘使用率：" + disksInfo.getUsage());
        }
        //4.网卡网络信息
        List<NetworkBean> netList = getNetworkInfo();
        System.out.println("------网卡网络信息------");
        for (NetworkBean networkInfo : netList) {
            System.out.println("ipv4地址：" + networkInfo.getIpv4Address());
            System.out.println("ipv6地址：" + networkInfo.getIpv6Address());
            System.out.println("mac地址：" + networkInfo.getMacAddress());
            System.out.println("网卡名称：" + networkInfo.getNetworkName());
        }
    }
}
