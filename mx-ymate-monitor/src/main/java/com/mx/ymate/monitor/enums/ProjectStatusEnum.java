package com.mx.ymate.monitor.enums;

public enum ProjectStatusEnum {

    ONLINE(0),
    OFFLINE(1);

    private final int type;

    ProjectStatusEnum(int type) {
        this.type = type;
    }

    public int type() {
        return type;
    }

    public static ProjectStatusEnum valueTo(int type) {
        switch (type) {
            case 0:
                return ONLINE;
            case 1:
                return OFFLINE;
            default:
                throw new IllegalArgumentException("Unknown enum value: " + type);
        }
    }
}
