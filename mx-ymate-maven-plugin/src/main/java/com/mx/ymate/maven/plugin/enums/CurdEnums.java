package com.mx.ymate.maven.plugin.enums;


/**
 * @Author: mengxiang.
 * @create: 2021-09-30 09:21
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
