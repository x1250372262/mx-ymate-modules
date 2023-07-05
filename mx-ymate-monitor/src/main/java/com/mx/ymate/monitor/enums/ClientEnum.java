package com.mx.ymate.monitor.enums;

public enum ClientEnum {

    ALL("all"),
    SERVER("server"),
    CLIENT("client");

    private final String type;

    ClientEnum(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

    public static ClientEnum valueTo(String type) {
        switch (type) {
            case "all":
                return ALL;
            case "client":
                return CLIENT;
            case "server":
                return SERVER;
            default:
                throw new IllegalArgumentException("Unknown enum value: " + type);
        }
    }

    public static boolean isClient(String type) {
        return CLIENT.type().equals(type) || ALL.type().equals(type);
    }
    public static boolean isServer(String type) {
        return SERVER.type().equals(type) || ALL.type().equals(type);
    }
}
