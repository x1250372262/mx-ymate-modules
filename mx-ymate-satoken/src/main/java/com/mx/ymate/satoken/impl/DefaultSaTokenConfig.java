/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mx.ymate.satoken.impl;

import cn.dev33.satoken.config.SaCookieConfig;
import cn.dev33.satoken.config.SaTokenConfig;
import com.mx.ymate.satoken.ISaToken;
import com.mx.ymate.satoken.ISaTokenConfig;
import net.ymate.platform.core.configuration.IConfigReader;
import net.ymate.platform.core.module.IModuleConfigurer;

/**
 * DefaultSaTokenConfig generated By ModuleMojo on 2022/07/03 12:39
 *
 * @author YMP (https://www.ymate.net/)
 */
public final class DefaultSaTokenConfig implements ISaTokenConfig {

    private boolean enabled;

    /**
     * token名称 (同时也是cookie名称)
     */
    private String tokenName;

    /**
     * token的长久有效期(单位:秒) 默认30天, -1代表永久
     */
    private long timeout;

    /**
     * token临时有效期 [指定时间内无操作就视为token过期] (单位: 秒), 默认-1 代表不限制
     * (例如可以设置为1800代表30分钟内无操作就过期)
     */
    private long activityTimeout;

    /**
     * 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
     */
    private Boolean isConcurrent;

    /**
     * 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
     */
    private Boolean isShare;

    /**
     * 同一账号最大登录数量，-1代表不限 （只有在 isConcurrent=true, isShare=false 时此配置才有效）
     */
    private int maxLoginCount;

    /**
     * 是否尝试从请求体里读取token
     */
    private Boolean isReadBody;

    /**
     * 是否尝试从header里读取token
     */
    private Boolean isReadHeader;

    /**
     * 是否尝试从cookie里读取token
     */
    private Boolean isReadCookie;

    /**
     * 是否在登录后将 Token 写入到响应头
     */
    private Boolean isWriteHeader;

    /**
     * token风格(默认可取值：uuid、simple-uuid、random-32、random-64、random-128、tik)
     */
    private String tokenStyle;

    /**
     * 默认dao层实现类中，每次清理过期数据间隔的时间 (单位: 秒) ，默认值30秒，设置为-1代表不启动定时清理
     */
    private int dataRefreshPeriod;

    /**
     * 获取[token专属session]时是否必须登录 (如果配置为true，会在每次获取[token-session]时校验是否登录)
     */
    private Boolean tokenSessionCheckLogin;

    /**
     * 是否打开自动续签 (如果此值为true, 框架会在每次直接或间接调用getLoginId()时进行一次过期检查与续签操作)
     */
    private Boolean autoRenew;

    /**
     * token前缀, 格式样例(satoken: Bearer xxxx-xxxx-xxxx-xxxx)
     */
    private String tokenPrefix;

    /**
     * 是否在初始化配置时打印版本字符画
     */
    private Boolean isPrint;

    /**
     * 是否打印操作日志
     */
    private Boolean isLog;

    /**
     * 日志等级（trace、debug、info、warn、error、fatal）
     */
    private String logLevel;

    /**
     * 日志等级 int 值（1=trace、2=debug、3=info、4=warn、5=error、6=fatal）
     */
    private int logLevelInt;

    /**
     * jwt秘钥 (只有集成 jwt 模块时此参数才会生效)
     */
    private String jwtSecretKey;


    /**
     * Http Basic 认证的账号和密码
     */
    private String basic;

    /**
     * 配置当前项目的网络访问地址
     */
    private String currDomain;

    /**
     * Same-Token 的有效期 (单位: 秒)
     */
    private long sameTokenTimeout;

    /**
     * 是否校验Same-Token（部分rpc插件有效
     */
    private Boolean checkSameToken;

    /**
     * 域（写入Cookie时显式指定的作用域, 常用于单点登录二级域名共享Cookie的场景）
     */
    private String cookieDomain;

    /**
     * 路径
     */
    private String cookiePath;

    /**
     * 是否只在 https 协议下有效
     */
    private Boolean cookieSecure;

    /**
     * 是否禁止 js 操作 Cookie
     */
    private Boolean cookieHttpOnly;

    /**
     * 第三方限制级别（Strict=完全禁止，Lax=部分允许，None=不限制）
     */
    private String cookieSameSite;

    private boolean initialized;

    public static DefaultSaTokenConfig defaultConfig() {
        return builder().build();
    }

    public static DefaultSaTokenConfig create(IModuleConfigurer moduleConfigurer) {
        return new DefaultSaTokenConfig(moduleConfigurer);
    }


    public static Builder builder() {
        return new Builder();
    }

    private DefaultSaTokenConfig() {
    }


    private DefaultSaTokenConfig(IModuleConfigurer moduleConfigurer) {
        IConfigReader configReader = moduleConfigurer.getConfigReader();
        enabled = configReader.getBoolean(ENABLED, true);
        tokenName = configReader.getString(TOKEN_NAME, "saToken");
        timeout = configReader.getLong(TIMEOUT, 60 * 60 * 24 * 30);
        activityTimeout = configReader.getLong(ACTIVITY_TIMEOUT, -1);
        isConcurrent = configReader.getBoolean(IS_CONCURRENT, true);
        isShare = configReader.getBoolean(IS_SHARE, true);
        maxLoginCount = configReader.getInt(MAX_LOGIN_COUNT, 12);
        isReadBody = configReader.getBoolean(IS_READ_BODY, true);
        isReadHeader = configReader.getBoolean(IS_READ_HEADER, true);
        isReadCookie = configReader.getBoolean(IS_READ_COOKIE, true);
        isWriteHeader = configReader.getBoolean(IS_WRITE_HEADER, true);
        tokenStyle = configReader.getString(TOKEN_STYLE, "uuid");
        dataRefreshPeriod = configReader.getInt(DATA_REFRESH_PERIOD, 30);
        tokenSessionCheckLogin = configReader.getBoolean(TOKEN_SESSION_CHECK_LOGIN, true);
        autoRenew = configReader.getBoolean(AUTO_RENEW, true);
        tokenPrefix = configReader.getString(TOKEN_PREFIX);
        isPrint = configReader.getBoolean(IS_PRINT, true);
        isLog = configReader.getBoolean(IS_LOG, false);
        logLevel = configReader.getString(LOG_LEVEL, "trace");
        logLevelInt = configReader.getInt(LOG_LEVEL_INT, 1);
        jwtSecretKey = configReader.getString(JWT_SECRET_KEY);
        basic = configReader.getString(BASIC);
        currDomain = configReader.getString(CURR_DOMAIN);
        sameTokenTimeout = configReader.getLong(SAME_TOKEN_TIMEOUT, 60 * 60 * 24);
        cookieDomain = configReader.getString(COOKIE_DOMAIN);
        cookiePath = configReader.getString(COOKIE_PATH);
        cookieSecure = configReader.getBoolean(COOKIE_SECURE, false);
        cookieHttpOnly = configReader.getBoolean(COOKIE_HTTP_ONLY, false);
        cookieSameSite = configReader.getString(COOKIE_SAME_SITE);
        //
        // TODO What to do?
    }

    @Override
    public void initialize(ISaToken owner) throws Exception {
        if (!initialized) {
            if (enabled) {
                // TODO What to do?
            }
            initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String tokenName() {
        return this.tokenName;
    }

    @Override
    public long timeout() {
        return this.timeout;
    }

    @Override
    public long activityTimeout() {
        return this.activityTimeout;
    }

    @Override
    public Boolean isConcurrent() {
        return this.isConcurrent;
    }

    @Override
    public Boolean isShare() {
        return this.isShare;
    }

    @Override
    public int maxLoginCount() {
        return this.maxLoginCount;
    }

    @Override
    public Boolean isReadBody() {
        return this.isReadBody;
    }

    @Override
    public Boolean isReadHeader() {
        return this.isReadHeader;
    }

    @Override
    public Boolean isReadCookie() {
        return this.isReadCookie;
    }

    @Override
    public Boolean isWriteHeader() {
        return this.isWriteHeader;
    }

    @Override
    public String tokenStyle() {
        return this.tokenStyle;
    }

    @Override
    public int dataRefreshPeriod() {
        return this.dataRefreshPeriod;
    }

    @Override
    public Boolean tokenSessionCheckLogin() {
        return this.tokenSessionCheckLogin;
    }

    @Override
    public Boolean autoRenew() {
        return this.autoRenew;
    }

    @Override
    public String tokenPrefix() {
        return this.tokenPrefix;
    }

    @Override
    public Boolean isPrint() {
        return this.isPrint;
    }

    @Override
    public Boolean isLog() {
        return this.isLog;
    }

    @Override
    public String logLevel() {
        return this.logLevel;
    }

    @Override
    public int logLevelInt() {
        return this.logLevelInt;
    }

    @Override
    public String jwtSecretKey() {
        return this.jwtSecretKey;
    }

    @Override
    public String basic() {
        return this.basic;
    }

    @Override
    public String currDomain() {
        return this.currDomain;
    }

    @Override
    public long sameTokenTimeout() {
        return this.sameTokenTimeout;
    }

    @Override
    public Boolean checkSameToken() {
        return this.checkSameToken;
    }

    @Override
    public String cookieDomain() {
        return this.cookieDomain;
    }

    @Override
    public String cookiePath() {
        return this.cookiePath;
    }

    @Override
    public Boolean cookieSecure() {
        return this.cookieSecure;
    }

    @Override
    public Boolean cookieHttpOnly() {
        return this.cookieHttpOnly;
    }

    @Override
    public String cookieSameSite() {
        return this.cookieSameSite;
    }


    @Override
    public SaTokenConfig toSaTokenConfig() {
        SaTokenConfig saTokenConfig = new SaTokenConfig();
        saTokenConfig.setTokenName(this.tokenName);
        saTokenConfig.setTimeout(this.timeout);
        saTokenConfig.setActivityTimeout(this.activityTimeout);
        saTokenConfig.setIsConcurrent(this.isConcurrent);
        saTokenConfig.setIsShare(this.isShare);
        saTokenConfig.setMaxLoginCount(this.maxLoginCount);
        saTokenConfig.setIsReadBody(this.isReadBody);
        saTokenConfig.setIsReadHeader(this.isReadHeader);
        saTokenConfig.setIsReadCookie(this.isReadCookie);
        saTokenConfig.setIsWriteHeader(this.isWriteHeader);
        saTokenConfig.setTokenStyle(this.tokenStyle);
        saTokenConfig.setDataRefreshPeriod(this.dataRefreshPeriod);
        saTokenConfig.setTokenSessionCheckLogin(this.tokenSessionCheckLogin);
        saTokenConfig.setAutoRenew(this.autoRenew);
        saTokenConfig.setTokenPrefix(this.tokenPrefix);
        saTokenConfig.setIsPrint(this.isPrint);
        saTokenConfig.setIsLog(this.isLog);
        saTokenConfig.setLogLevel(this.logLevel);
        saTokenConfig.setLogLeveInt(this.logLevelInt);
        saTokenConfig.setJwtSecretKey(this.jwtSecretKey);
        saTokenConfig.setBasic(this.basic);
        saTokenConfig.setCurrDomain(this.currDomain);
        saTokenConfig.setSameTokenTimeout(this.sameTokenTimeout);
        saTokenConfig.setCheckSameToken(this.checkSameToken);

        SaCookieConfig saCookieConfig = new SaCookieConfig();
        saCookieConfig.setDomain(this.cookieDomain);
        saCookieConfig.setPath(this.cookiePath);
        saCookieConfig.setSecure(this.cookieSecure);
        saCookieConfig.setHttpOnly(this.cookieHttpOnly);
        saCookieConfig.setSameSite(this.cookieSameSite);

        saTokenConfig.setCookie(saCookieConfig);
        return saTokenConfig;
    }

    public void setEnabled(boolean enabled) {
        if (!initialized) {
            this.enabled = enabled;
        }
    }

    public void setTokenName(String tokenName) {
        if (!initialized) {
            this.tokenName = tokenName;
        }
    }

    public void setTimeout(long timeout) {
        if (!initialized) {
            this.timeout = timeout;
        }
    }

    public void setActivityTimeout(long activityTimeout) {
        if (!initialized) {
            this.activityTimeout = activityTimeout;
        }
    }

    public void setConcurrent(Boolean concurrent) {
        if (!initialized) {
            this.isConcurrent = concurrent;
        }
    }

    public void setShare(Boolean share) {
        if (!initialized) {
            this.isShare = share;
        }
    }

    public void setMaxLoginCount(int maxLoginCount) {
        if (!initialized) {
            this.maxLoginCount = maxLoginCount;
        }
    }

    public void setReadBody(Boolean readBody) {
        if (!initialized) {
            this.isReadBody = readBody;
        }
    }

    public void setReadHead(Boolean readHeader) {
        if (!initialized) {
            this.isReadHeader = readHeader;
        }
    }

    public void setReadCookie(Boolean readCookie) {
        if (!initialized) {
            this.isReadCookie = readCookie;
        }
    }

    public void setWriteHeader(Boolean writeHeader) {
        if (!initialized) {
            this.isWriteHeader = writeHeader;
        }
    }

    public void setTokenStyle(String tokenStyle) {
        if (!initialized) {
            this.tokenStyle = tokenStyle;
        }
    }

    public void setDataRefreshPeriod(int dataRefreshPeriod) {
        if (!initialized) {
            this.dataRefreshPeriod = dataRefreshPeriod;
        }
    }

    public void setTokenSessionCheckLogin(Boolean tokenSessionCheckLogin) {
        if (!initialized) {
            this.tokenSessionCheckLogin = tokenSessionCheckLogin;
        }
    }

    public void setAutoRenew(Boolean autoRenew) {
        if (!initialized) {
            this.autoRenew = autoRenew;
        }
    }

    public void setTokenPrefix(String tokenPrefix) {
        if (!initialized) {
            this.tokenPrefix = tokenPrefix;
        }
    }

    public void setPrint(Boolean print) {
        if (!initialized) {
            this.isPrint = print;
        }
    }

    public void setLog(Boolean log) {
        if (!initialized) {
            this.isLog = log;
        }
    }

    public void setLogLevel(String logLevel) {
        if (!initialized) {
            this.logLevel = logLevel;
        }
    }

    public void setLogLevelInt(int logLevelInt) {
        if (!initialized) {
            this.logLevelInt = logLevelInt;
        }
    }

    public void setJwtSecretKey(String jwtSecretKey) {
        if (!initialized) {
            this.jwtSecretKey = jwtSecretKey;
        }
    }

    public void setBasic(String basic) {
        if (!initialized) {
            this.basic = basic;
        }
    }

    public void setCurrDomain(String currDomain) {
        if (!initialized) {
            this.currDomain = currDomain;
        }
    }

    public void setSameTokenTimeout(long sameTokenTimeout) {
        if (!initialized) {
            this.sameTokenTimeout = sameTokenTimeout;
        }
    }

    public void setCheckSameToken(Boolean checkSameToken) {
        if (!initialized) {
            this.checkSameToken = checkSameToken;
        }
    }

    public void setCookieDomain(String cookieDomain) {
        if (!initialized) {
            this.cookieDomain = cookieDomain;
        }
    }

    public void setCookiePath(String cookiePath) {
        if (!initialized) {
            this.cookiePath = cookiePath;
        }
    }

    public void setCookieSecure(Boolean cookieSecure) {
        if (!initialized) {
            this.cookieSecure = cookieSecure;
        }
    }

    public void setCookieHttpOnly(Boolean cookieHttpOnly) {
        if (!initialized) {
            this.cookieHttpOnly = cookieHttpOnly;
        }
    }

    public void setCookieSameSite(String cookieSameSite) {
        if (!initialized) {
            this.cookieSameSite = cookieSameSite;
        }
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }


    public static final class Builder {

        private final DefaultSaTokenConfig config = new DefaultSaTokenConfig();

        private Builder() {
        }

        public Builder enabled(boolean enabled) {
            config.setEnabled(enabled);
            return this;
        }

        public Builder tokenName(String tokenName) {
            config.setTokenName(tokenName);
            return this;
        }

        public Builder timeout(long timeout) {
            config.setTimeout(timeout);
            return this;
        }

        public Builder activityTimeout(long activityTimeout) {
            config.setActivityTimeout(activityTimeout);
            return this;
        }

        public Builder concurrent(Boolean concurrent) {
            config.setConcurrent(concurrent);
            return this;
        }

        public Builder share(Boolean share) {
            config.setShare(share);
            return this;
        }

        public Builder maxLoginCount(int maxLoginCount) {
            config.setMaxLoginCount(maxLoginCount);
            return this;
        }

        public Builder readBody(Boolean readBody) {
            config.setReadBody(readBody);
            return this;
        }

        public Builder readHead(Boolean readHead) {
            config.setReadHead(readHead);
            return this;
        }

        public Builder readCookie(Boolean readCookie) {
            config.setReadCookie(readCookie);
            return this;
        }

        public Builder writeHeader(Boolean writeHeader) {
            config.setWriteHeader(writeHeader);
            return this;
        }

        public Builder tokenStyle(String tokenStyle) {
            config.setTokenStyle(tokenStyle);
            return this;
        }

        public Builder dataRefreshPeriod(int dataRefreshPeriod) {
            config.setDataRefreshPeriod(dataRefreshPeriod);
            return this;
        }

        public Builder tokenSessionCheckLogin(Boolean tokenSessionCheckLogin) {
            config.setTokenSessionCheckLogin(tokenSessionCheckLogin);
            return this;
        }

        public Builder autoRenew(Boolean autoRenew) {
            config.setAutoRenew(autoRenew);
            return this;
        }

        public Builder tokenPrefix(String tokenPrefix) {
            config.setTokenPrefix(tokenPrefix);
            return this;
        }

        public Builder print(Boolean print) {
            config.setPrint(print);
            return this;
        }

        public Builder log(Boolean log) {
            config.setLog(log);
            return this;
        }

        public Builder logLevel(String logLevel) {
            config.setLogLevel(logLevel);
            return this;
        }

        public Builder logLevelInt(int logLevelInt) {
            config.setLogLevelInt(logLevelInt);
            return this;
        }

        public Builder jwtSecretKey(String jwtSecretKey) {
            config.setJwtSecretKey(jwtSecretKey);
            return this;
        }


        public Builder basic(String basic) {
            config.setBasic(basic);
            return this;
        }

        public Builder currDomain(String currDomain) {
            config.setCurrDomain(currDomain);
            return this;
        }

        public Builder sameTokenTimeout(long sameTokenTimeout) {
            config.setSameTokenTimeout(sameTokenTimeout);
            return this;
        }


        public Builder checkSameToken(Boolean checkSameToken) {
            config.setCheckSameToken(checkSameToken);
            return this;
        }

        public Builder cookieDomain(String cookieDomain) {
            config.setCookieDomain(cookieDomain);
            return this;
        }

        public Builder cookiePath(String cookiePath) {
            config.setCookiePath(cookiePath);
            return this;
        }

        public Builder cookieSecure(boolean cookieSecure) {
            config.setCookieSecure(cookieSecure);
            return this;
        }

        public Builder cookieHttpOnly(boolean cookieHttpOnly) {
            config.setCookieHttpOnly(cookieHttpOnly);
            return this;
        }

        public Builder cookieSameSite(String cookieSameSite) {
            config.setCookieSameSite(cookieSameSite);
            return this;
        }


        public DefaultSaTokenConfig build() {
            return config;
        }
    }
}