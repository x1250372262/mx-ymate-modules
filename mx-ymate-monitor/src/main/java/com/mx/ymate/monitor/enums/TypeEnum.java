package com.mx.ymate.monitor.enums;

public enum TypeEnum {

    ALL("all"),
    SERVER("server"),
    PROJECT("project");

    private final String type;

    TypeEnum(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

    public static TypeEnum valueTo(String type) {
        switch (type) {
            case "all":
                return ALL;
            case "project":
                return PROJECT;
            case "server":
                return SERVER;
            default:
                throw new IllegalArgumentException("Unknown enum value: " + type);
        }
    }
}
