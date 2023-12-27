package com.mx.ymate.security.base.enums;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: 菜单类型枚举
 */
public enum MenuType {

    /**
     * 默认
     */
    DEFAULT(0),

    /**
     * 公开
     */
    PUBLIC(1),

    /**
     * 最高权限可看
     */
    FOUNDER(2);

    private final int value;

    MenuType(int value) {
        this.value = value;
    }

    public static MenuType valueTo(int value) {
        switch (value) {
            case 0:
                return DEFAULT;
            case 1:
                return PUBLIC;
            case 2:
                return FOUNDER;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }

}
