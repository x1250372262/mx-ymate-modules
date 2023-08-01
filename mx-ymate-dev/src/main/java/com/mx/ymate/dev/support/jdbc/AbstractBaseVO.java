package com.mx.ymate.dev.support.jdbc;

import java.io.Serializable;

public abstract class AbstractBaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

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
