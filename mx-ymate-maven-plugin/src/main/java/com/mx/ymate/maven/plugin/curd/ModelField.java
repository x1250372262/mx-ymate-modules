package com.mx.ymate.maven.plugin.curd;

/**
 * @Author: mengxiang.
 * @Create: 2024/3/21 10:26
 * @Description:
 */
public class ModelField {

    private String type;

    private String name;

    private String comment;

    private String updateFieldName;

    public ModelField(String type, String name, String comment, String updateFieldName) {
        this.type = type;
        this.name = name;
        this.comment = comment;
        this.updateFieldName = updateFieldName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUpdateFieldName() {
        return updateFieldName;
    }

    public void setUpdateFieldName(String updateFieldName) {
        this.updateFieldName = updateFieldName;
    }
}
