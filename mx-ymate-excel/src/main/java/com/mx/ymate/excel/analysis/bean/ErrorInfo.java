package com.mx.ymate.excel.analysis.bean;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class ErrorInfo {

    private Integer row;

    private Integer cell;

    private String title;

    private String content;


    public ErrorInfo() {
    }

    public ErrorInfo(Integer row, Integer cell, String title) {
        this.row = row;
        this.cell = cell;
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getCell() {
        return cell;
    }

    public String getTitle() {
        return title;
    }
}
