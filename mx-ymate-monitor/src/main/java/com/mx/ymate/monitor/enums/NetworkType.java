package com.mx.ymate.monitor.enums;

public enum NetworkType {

    /**
     * 当前网卡时间戳
     */
    TIME_STAMP(0),
    /**
     * 网卡总发送量
     */
    SEND(1),
    /**
     * 网卡总接收量
     */
    ACCEPT(2);
    private final int index;

    NetworkType(int value) {
        this.index = value;
    }

    public int getIndex() {
        return index;
    }
}
