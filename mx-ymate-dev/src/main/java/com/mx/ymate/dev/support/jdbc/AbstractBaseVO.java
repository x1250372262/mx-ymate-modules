package com.mx.ymate.dev.support.jdbc;

public abstract class AbstractBaseVO {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AbstractBaseVO{" +
                "id='" + id + '\'' +
                '}';
    }
}
