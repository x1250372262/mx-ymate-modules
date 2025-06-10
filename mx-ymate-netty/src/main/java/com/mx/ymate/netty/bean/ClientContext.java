package com.mx.ymate.netty.bean;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/6.
 * @Time: 09:38.
 * @Description:
 */
public class ClientContext {

    private final String clientId;
    private final Map<String, Object> extras;

    private ClientContext(Builder builder) {
        this.clientId = builder.clientId;
        this.extras = Collections.unmodifiableMap(new HashMap<>(builder.extras));
    }

    public String getClientId() {
        return clientId;
    }

    public Object getExtra(String key) {
        return extras.get(key);
    }

    public Map<String, Object> getAllExtras() {
        return extras;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder 模式
     */
    public static class Builder {
        private String clientId;
        private final Map<String, Object> extras = new HashMap<>();

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder putExtra(String key, Object value) {
            this.extras.put(key, value);
            return this;
        }

        public Builder putExtras(Map<String, Object> map) {
            if (map != null) {
                this.extras.putAll(map);
            }
            return this;
        }

        public ClientContext build() {
            if (clientId == null || clientId.isEmpty()) {
                throw new IllegalArgumentException("clientId不能为空");
            }
            return new ClientContext(this);
        }
    }
}
