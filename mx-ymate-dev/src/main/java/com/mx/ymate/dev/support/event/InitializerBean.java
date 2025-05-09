package com.mx.ymate.dev.support.event;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class InitializerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int order;

    private IYmpInitializer ympInitializer;

    public InitializerBean() {
    }

    public InitializerBean(int order, IYmpInitializer ympInitializer) {
        this.order = order;
        this.ympInitializer = ympInitializer;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public IYmpInitializer getYmpInitializer() {
        return ympInitializer;
    }

    public void setYmpInitializer(IYmpInitializer ympInitializer) {
        this.ympInitializer = ympInitializer;
    }

    @Override
    public String toString() {
        return "InitializerBean{" +
                "order=" + order +
                ", ympInitializer=" + ympInitializer +
                '}';
    }
}
