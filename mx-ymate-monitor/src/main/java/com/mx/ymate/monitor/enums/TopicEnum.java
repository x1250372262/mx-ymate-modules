package com.mx.ymate.monitor.enums;

public enum TopicEnum {

    SERVER_STATUS("server_status"),
    PROJECT_STATUS("project_status");

    private final String type;

    TopicEnum(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

    public static TopicEnum valueTo(String type) {
        switch (type) {
            case "server_status":
                return SERVER_STATUS;
            case "project_status":
                return PROJECT_STATUS;
            default:
                throw new IllegalArgumentException("Unknown enum value: " + type);
        }
    }

}
