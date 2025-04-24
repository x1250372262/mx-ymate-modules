package com.mx.ymate.qwen.bean;

/**
 * @Author: mengxiang.
 * @Date 2025/2/28.
 * @Time: 11:29.
 * @Description:
 */
public class AiResult {

    /**
     * 有四种情况：
     * 正在生成时为null；
     * 因模型输出自然结束，或触发输入参数中的stop条件而结束时为stop；
     * 因生成长度过长而结束为length；
     * 因发生工具调用为tool_calls。
     */
    private String finishReason;

    /**
     * 输出消息的角色，固定为assistant
     */
    private String role;

    /**
     * 输出消息的内容。当使用qwen-vl或qwen-audio系列模型时为array，其余情况为string。
     * 如果发起Function Calling，则该值为空。。
     */
    private Object content;


    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
