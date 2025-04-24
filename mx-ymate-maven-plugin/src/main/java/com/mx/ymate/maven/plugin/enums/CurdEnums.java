package com.mx.ymate.maven.plugin.enums;


/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class CurdEnums {

    /**
     * 类型
     */
    public enum TypeEnum {

        /**
         * 未知
         */
        ALL("all"),
        DAO("dao"),
        SERVICE("service"),
        CONTROLLER("controller"),
        PAGE("page");


        private final String value;

        TypeEnum(String value) {
            this.value = value;
        }

        public static TypeEnum fromValue(String value) {
            for (TypeEnum typeEnum : TypeEnum.values()) {
                if (typeEnum.getValue().equals(value)) {
                    return typeEnum;
                }
            }
            throw new RuntimeException("Invalid value");
        }

        public String getValue() {
            return value;
        }
    }

}
