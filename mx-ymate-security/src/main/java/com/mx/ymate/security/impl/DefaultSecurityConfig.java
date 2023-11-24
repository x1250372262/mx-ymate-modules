package com.mx.ymate.security.impl;

import cn.dev33.satoken.config.SaCookieConfig;
import cn.dev33.satoken.config.SaSignConfig;
import cn.dev33.satoken.config.SaTokenConfig;
import com.mx.ymate.security.ISecurity;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.handler.ILoginHandler;
import com.mx.ymate.security.handler.IUserHandler;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.configuration.IConfigReader;
import net.ymate.platform.core.module.IModuleConfigurer;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description:
 */
public final class DefaultSecurityConfig implements ISecurityConfig {

    private boolean enabled;

    /**
     * 客户端名称
     */
    private String client;

    /**
     * loginHandler实现类
     */
    private ILoginHandler loginHandlerClass;

    /**
     * userHandler实现类
     */
    private IUserHandler userHandlerClass;

    /**
     * 验证错误N次后锁定账户  默认不锁定  -1不锁定
     */
    private int errorCount;

    /**
     * 是否开启日志记录 默认false
     */
    private boolean openLog;

    /**
     * 登录拦截排除的路径用|分割
     */
    private String excludePathPatterns;

    /**
     * token名称 (同时也是cookie名称)
     */
    private String saTokenName;


    /**
     * token的长久有效期(单位:秒) 默认30天, -1代表永久
     */
    private long saTokenTimeout;

    /**
     * token临时有效期 [指定时间内无操作就视为token过期] (单位: 秒), 默认-1 代表不限制
     * (例如可以设置为1800代表30分钟内无操作就过期)
     */
    private long saTokenActiveTimeout;

    /**
     * 是否启用动态 activeTimeout 功能，如不需要请设置为 false，节省缓存请求次数
     */
    private Boolean saTokenDynamicActiveTimeout;

    /**
     * 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
     */
    private Boolean saTokenIsConcurrent;

    /**
     * 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
     */
    private Boolean saTokenIsShare;

    /**
     * 同一账号最大登录数量，-1代表不限 （只有在 isConcurrent=true, isShare=false 时此配置才有效）
     */
    private int saTokenMaxLoginCount;

    /**
     * 在每次创建 token 时的最高循环次数，用于保证 token 唯一性（-1=不循环尝试，直接使用
     */
    private int saTokenMaxTryTimes;

    /**
     * 是否尝试从请求体里读取token
     */
    private Boolean saTokenIsReadBody;

    /**
     * 是否尝试从header里读取token
     */
    private Boolean saTokenIsReadHeader;

    /**
     * 是否尝试从cookie里读取token
     */
    private Boolean saTokenIsReadCookie;

    /**
     * 是否在登录后将 Token 写入到响应头
     */
    private Boolean saTokenIsWriteHeader;

    /**
     * token风格(默认可取值：uuid、simple-uuid、random-32、random-64、random-128、tik)
     */
    private String saTokenStyle;

    /**
     * 默认dao层实现类中，每次清理过期数据间隔的时间 (单位: 秒) ，默认值30秒，设置为-1代表不启动定时清理
     */
    private int saTokenDataRefreshPeriod;

    /**
     * 获取[token专属session]时是否必须登录 (如果配置为true，会在每次获取[token-session]时校验是否登录)
     */
    private Boolean saTokenSessionCheckLogin;

    /**
     * 是否打开自动续签 (如果此值为true, 框架会在每次直接或间接调用getLoginId()时进行一次过期检查与续签操作)
     */
    private Boolean saTokenAutoRenew;

    /**
     * token前缀, 格式样例(satoken: Bearer xxxx-xxxx-xxxx-xxxx)
     */
    private String saTokenPrefix;

    /**
     * 是否在初始化配置时打印版本字符画
     */
    private Boolean saTokenIsPrint;

    /**
     * 是否打印操作日志
     */
    private Boolean saTokenIsLog;

    /**
     * 日志等级（trace、debug、info、warn、error、fatal）
     */
    private String saTokenLogLevel;

    /**
     * 日志等级 int 值（1=trace、2=debug、3=info、4=warn、5=error、6=fatal）
     */
    private int saTokenLogLevelInt;

    /**
     * 是否打印彩色日志 默认false
     */
    private Boolean saTokenIsColorLog;

    /**
     * jwt秘钥 (只有集成 jwt 模块时此参数才会生效)
     */
    private String saTokenJwtSecretKey;


    /**
     * Http Basic 认证的账号和密码
     */
    private String saTokenBasic;

    /**
     * 配置当前项目的网络访问地址
     */
    private String saTokenCurrDomain;

    /**
     * Same-Token 的有效期 (单位: 秒)
     */
    private long saTokenSameTokenTimeout;

    /**
     * 是否校验Same-Token（部分rpc插件有效
     */
    private Boolean saTokenCheckSameToken;

    /**
     * 域（写入Cookie时显式指定的作用域, 常用于单点登录二级域名共享Cookie的场景）
     */
    private String saTokenCookieDomain;

    /**
     * 路径
     */
    private String saTokenCookiePath;

    /**
     * 是否只在 https 协议下有效
     */
    private Boolean saTokenCookieSecure;

    /**
     * 是否禁止 js 操作 Cookie
     */
    private Boolean saTokenCookieHttpOnly;

    /**
     * 第三方限制级别（Strict=完全禁止，Lax=部分允许，None=不限制）
     */
    private String saTokenCookieSameSite;

    /**
     * API 调用签名秘钥
     */
    private String saTokenSignSecretKey;

    /**
     * #接口调用时的时间戳允许的差距（单位：ms），-1 代表不校验差距，默认15分钟
     * #比如此处你配置了60秒，当一个请求从 client 发起后，如果 server 端60秒内没有处理，60秒后再想处理就无法校验通过了。
     */
    private long saTokenSignTimestampDisparity;

    /**
     * 是否校验 nonce 随机字符串 默认true
     */
    private Boolean saTokenSignIsCheckNonce;

    private boolean initialized;

    public static DefaultSecurityConfig create(IModuleConfigurer moduleConfigurer) {
        return new DefaultSecurityConfig(moduleConfigurer);
    }

    private DefaultSecurityConfig() {
    }


    private DefaultSecurityConfig(IModuleConfigurer moduleConfigurer) {
        IConfigReader configReader = moduleConfigurer.getConfigReader();
        enabled = configReader.getBoolean(ENABLED, true);
        client = configReader.getString(CLIENT, "default");
        String loginHandlerClassName = configReader.getString(LOGIN_HANDLER_CLASS, ILoginHandler.DefaultLoginHandler.class.getName());
        loginHandlerClass = ClassUtils.impl(loginHandlerClassName, ILoginHandler.class, this.getClass());
        String userHandlerClassName = configReader.getString(USER_HANDLER_CLASS, IUserHandler.DefaultUserHandler.class.getName());
        userHandlerClass = ClassUtils.impl(userHandlerClassName, IUserHandler.class, this.getClass());
        errorCount = configReader.getInt(ERROR_COUNT, -1);
        openLog = configReader.getBoolean(OPEN_LOG, false);
        excludePathPatterns = configReader.getString(EXCLUDE_PATH_PATTERNS, excludePathPatterns);
        saTokenName = configReader.getString(SATOKEN_NAME, "saToken");
        saTokenTimeout = configReader.getLong(SATOKEN_TIMEOUT, 60 * 60 * 24 * 30);
        saTokenActiveTimeout = configReader.getLong(SATOKEN_ACTIVE_TIMEOUT, -1);
        saTokenDynamicActiveTimeout = configReader.getBoolean(SATOKEN_DYNAMIC_ACTIVE_TIMEOUT, false);
        saTokenIsConcurrent = configReader.getBoolean(SATOKEN_IS_CONCURRENT, true);
        saTokenIsShare = configReader.getBoolean(SATOKEN_IS_SHARE, true);
        saTokenMaxTryTimes = configReader.getInt(SATOKEN_MAX_TRY_TIMES, 12);
        saTokenMaxLoginCount = configReader.getInt(SATOKEN_MAX_LOGIN_COUNT, 12);
        saTokenIsReadBody = configReader.getBoolean(SATOKEN_IS_READ_BODY, true);
        saTokenIsReadHeader = configReader.getBoolean(SATOKEN_IS_READ_HEADER, true);
        saTokenIsReadCookie = configReader.getBoolean(SATOKEN_IS_READ_COOKIE, true);
        saTokenIsWriteHeader = configReader.getBoolean(SATOKEN_IS_WRITE_HEADER, true);
        saTokenStyle = configReader.getString(SATOKEN_STYLE, "uuid");
        saTokenDataRefreshPeriod = configReader.getInt(SATOKEN_DATA_REFRESH_PERIOD, 30);
        saTokenSessionCheckLogin = configReader.getBoolean(SATOKEN_SESSION_CHECK_LOGIN, true);
        saTokenAutoRenew = configReader.getBoolean(SATOKEN_AUTO_RENEW, true);
        saTokenPrefix = configReader.getString(SATOKEN_PREFIX);
        saTokenIsPrint = configReader.getBoolean(SATOKEN_IS_PRINT, true);
        saTokenIsLog = configReader.getBoolean(SATOKEN_IS_LOG, false);
        saTokenLogLevel = configReader.getString(SATOKEN_LOG_LEVEL, "trace");
        saTokenLogLevelInt = configReader.getInt(SATOKEN_LOG_LEVEL_INT, 1);
        saTokenIsColorLog = configReader.getBoolean(SATOKEN_IS_COLOR_LOG, false);
        saTokenJwtSecretKey = configReader.getString(SATOKEN_JWT_SECRET_KEY);
        saTokenBasic = configReader.getString(SATOKEN_BASIC);
        saTokenCurrDomain = configReader.getString(SATOKEN_CURR_DOMAIN);
        saTokenSameTokenTimeout = configReader.getLong(SATOKEN_SAME_TOKEN_TIMEOUT, 60 * 60 * 24);
        saTokenCookieDomain = configReader.getString(SATOKEN_COOKIE_DOMAIN);
        saTokenCookiePath = configReader.getString(SATOKEN_COOKIE_PATH);
        saTokenCookieSecure = configReader.getBoolean(SATOKEN_COOKIE_SECURE, false);
        saTokenCookieHttpOnly = configReader.getBoolean(SATOKEN_COOKIE_HTTP_ONLY, false);
        saTokenCookieSameSite = configReader.getString(SATOKEN_COOKIE_SAME_SITE);
        saTokenSignSecretKey = configReader.getString(SATOKEN_SIGN_SECRET_KEY);
        saTokenSignTimestampDisparity = configReader.getLong(SATOKEN_SIGN_TIMESTAMP_DISPARITY, 1000 * 60 * 15);
        saTokenSignIsCheckNonce = configReader.getBoolean(SATOKEN_SIGN_IS_CHECK_NONCE, true);
    }

    @Override
    public void initialize(ISecurity owner) throws Exception {
        if (!initialized) {
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
    public String client() {
        return client;
    }

    @Override
    public ILoginHandler loginHandlerClass() {
        return loginHandlerClass;
    }

    @Override
    public IUserHandler userHandlerClass() {
        return userHandlerClass;
    }

    @Override
    public int errorCount() {
        return errorCount;
    }

    @Override
    public boolean openLog() {
        return openLog;
    }

    @Override
    public String excludePathPatterns() {
        return excludePathPatterns;
    }

    @Override
    public String saTokenName() {
        return this.saTokenName;
    }

    @Override
    public long saTokenTimeout() {
        return this.saTokenTimeout;
    }

    @Override
    public long saTokenActiveTimeout() {
        return this.saTokenActiveTimeout;
    }

    @Override
    public Boolean saTokenDynamicActiveTimeout() {
        return this.saTokenDynamicActiveTimeout;
    }

    @Override
    public Boolean saTokenIsConcurrent() {
        return this.saTokenIsConcurrent;
    }

    @Override
    public Boolean saTokenIsShare() {
        return this.saTokenIsShare;
    }

    @Override
    public int saTokenMaxLoginCount() {
        return this.saTokenMaxLoginCount;
    }

    @Override
    public int saTokenMaxTryTimes() {
        return this.saTokenMaxTryTimes;
    }

    @Override
    public Boolean saTokenIsReadBody() {
        return this.saTokenIsReadBody;
    }

    @Override
    public Boolean saTokenIsReadHeader() {
        return this.saTokenIsReadHeader;
    }

    @Override
    public Boolean saTokenIsReadCookie() {
        return this.saTokenIsReadCookie;
    }

    @Override
    public Boolean saTokenIsWriteHeader() {
        return this.saTokenIsWriteHeader;
    }

    @Override
    public String saTokenStyle() {
        return this.saTokenStyle;
    }

    @Override
    public int saTokenDataRefreshPeriod() {
        return this.saTokenDataRefreshPeriod;
    }

    @Override
    public Boolean saTokenSessionCheckLogin() {
        return this.saTokenSessionCheckLogin;
    }

    @Override
    public Boolean saTokenAutoRenew() {
        return this.saTokenAutoRenew;
    }

    @Override
    public String saTokenPrefix() {
        return this.saTokenPrefix;
    }

    @Override
    public Boolean saTokenIsPrint() {
        return this.saTokenIsPrint;
    }

    @Override
    public Boolean saTokenIsLog() {
        return this.saTokenIsLog;
    }

    @Override
    public String saTokenLogLevel() {
        return this.saTokenLogLevel;
    }

    @Override
    public int saTokenLogLevelInt() {
        return this.saTokenLogLevelInt;
    }

    @Override
    public Boolean saTokenIsColorLog() {
        return this.saTokenIsColorLog;
    }

    @Override
    public String saTokenJwtSecretKey() {
        return this.saTokenJwtSecretKey;
    }

    @Override
    public String saTokenBasic() {
        return this.saTokenBasic;
    }

    @Override
    public String saTokenCurrDomain() {
        return this.saTokenCurrDomain;
    }

    @Override
    public long saTokenSameTokenTimeout() {
        return this.saTokenSameTokenTimeout;
    }

    @Override
    public Boolean saTokenCheckSameToken() {
        return this.saTokenCheckSameToken;
    }

    @Override
    public String saTokenCookieDomain() {
        return this.saTokenCookieDomain;
    }

    @Override
    public String saTokenCookiePath() {
        return this.saTokenCookiePath;
    }

    @Override
    public Boolean saTokenCookieSecure() {
        return this.saTokenCookieSecure;
    }

    @Override
    public Boolean saTokenCookieHttpOnly() {
        return this.saTokenCookieHttpOnly;
    }

    @Override
    public String saTokenCookieSameSite() {
        return this.saTokenCookieSameSite;
    }

    @Override
    public String saTokenSignSecretKey() {
        return this.saTokenSignSecretKey;
    }

    @Override
    public long saTokenSignTimestampDisparity() {
        return this.saTokenSignTimestampDisparity;
    }

    @Override
    public Boolean saTokenSignIsCheckNonce() {
        return this.saTokenSignIsCheckNonce;
    }

    @Override
    public SaTokenConfig toSaTokenConfig() {
        SaTokenConfig saTokenConfig = new SaTokenConfig();
        saTokenConfig.setTokenName(this.saTokenName);
        saTokenConfig.setTimeout(this.saTokenTimeout);
        saTokenConfig.setActiveTimeout(this.saTokenActiveTimeout);
        saTokenConfig.setDynamicActiveTimeout(this.saTokenDynamicActiveTimeout);
        saTokenConfig.setIsConcurrent(this.saTokenIsConcurrent);
        saTokenConfig.setIsShare(this.saTokenIsShare);
        saTokenConfig.setMaxLoginCount(this.saTokenMaxLoginCount);
        saTokenConfig.setMaxTryTimes(this.saTokenMaxTryTimes);
        saTokenConfig.setIsReadBody(this.saTokenIsReadBody);
        saTokenConfig.setIsReadHeader(this.saTokenIsReadHeader);
        saTokenConfig.setIsReadCookie(this.saTokenIsReadCookie);
        saTokenConfig.setIsWriteHeader(this.saTokenIsWriteHeader);
        saTokenConfig.setTokenStyle(this.saTokenStyle);
        saTokenConfig.setDataRefreshPeriod(this.saTokenDataRefreshPeriod);
        saTokenConfig.setTokenSessionCheckLogin(this.saTokenSessionCheckLogin);
        saTokenConfig.setAutoRenew(this.saTokenAutoRenew);
        saTokenConfig.setTokenPrefix(this.saTokenPrefix);
        saTokenConfig.setIsPrint(this.saTokenIsPrint);
        saTokenConfig.setIsLog(this.saTokenIsLog);
        saTokenConfig.setLogLevel(this.saTokenLogLevel);
        saTokenConfig.setLogLevelInt(this.saTokenLogLevelInt);
        saTokenConfig.setIsColorLog(this.saTokenIsColorLog);
        saTokenConfig.setJwtSecretKey(this.saTokenJwtSecretKey);
        saTokenConfig.setBasic(this.saTokenBasic);
        saTokenConfig.setCurrDomain(this.saTokenCurrDomain);
        saTokenConfig.setSameTokenTimeout(this.saTokenSameTokenTimeout);
        saTokenConfig.setCheckSameToken(this.saTokenCheckSameToken);

        SaCookieConfig saCookieConfig = new SaCookieConfig();
        saCookieConfig.setDomain(this.saTokenCookieDomain);
        saCookieConfig.setPath(this.saTokenCookiePath);
        saCookieConfig.setSecure(this.saTokenCookieSecure);
        saCookieConfig.setHttpOnly(this.saTokenCookieHttpOnly);
        saCookieConfig.setSameSite(this.saTokenCookieSameSite);
        saTokenConfig.setCookie(saCookieConfig);

        SaSignConfig saSignConfig = new SaSignConfig();
        saSignConfig.setSecretKey(this.saTokenSignSecretKey);
        saSignConfig.setTimestampDisparity(this.saTokenSignTimestampDisparity);
        saSignConfig.setIsCheckNonce(this.saTokenSignIsCheckNonce);
        saTokenConfig.setSign(saSignConfig);
        return saTokenConfig;
    }


    public void setEnabled(boolean enabled) {
        if (!initialized) {
            this.enabled = enabled;
        }
    }


    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void setClient(String client) {
        if (!initialized) {
            this.client = client;
        }
    }

    public void setLoginHandlerClass(ILoginHandler loginHandlerClass) {
        if (!initialized) {
            this.loginHandlerClass = loginHandlerClass;
        }
    }

    public void setUserHandlerClass(IUserHandler userHandlerClass) {
        if (!initialized) {
            this.userHandlerClass = userHandlerClass;
        }
    }

    public void setErrorCount(int errorCount) {
        if (!initialized) {
            this.errorCount = errorCount;
        }
    }

    public void setOpenLog(boolean openLog) {
        if (!initialized) {
            this.openLog = openLog;
        }
    }

    public void setExcludePathPatterns(String excludePathPatterns) {
        if (!initialized) {
            this.excludePathPatterns = excludePathPatterns;
        }
    }

    public void setSaTokenName(String saTokenName) {
        if (!initialized) {
            this.saTokenName = saTokenName;
        }
    }

    public void setSaTokenTimeout(long saTokenTimeout) {
        if (!initialized) {
            this.saTokenTimeout = saTokenTimeout;
        }
    }

    public void setSaTokenActiveTimeout(long saTokenActiveTimeout) {
        if (!initialized) {
            this.saTokenActiveTimeout = saTokenActiveTimeout;
        }
    }

    public void setSaTokenDynamicActiveTimeout(Boolean saTokenDynamicActiveTimeout) {
        if (!initialized) {
            this.saTokenDynamicActiveTimeout = saTokenDynamicActiveTimeout;
        }
    }

    public void setSaTokenIsConcurrent(Boolean saTokenIsConcurrent) {
        if (!initialized) {
            this.saTokenIsConcurrent = saTokenIsConcurrent;
        }
    }

    public void setSaTokenIsShare(Boolean saTokenIsShare) {
        if (!initialized) {
            this.saTokenIsShare = saTokenIsShare;
        }
    }

    public void setSaTokenMaxLoginCount(int saTokenMaxLoginCount) {
        if (!initialized) {
            this.saTokenMaxLoginCount = saTokenMaxLoginCount;
        }
    }

    public void setSaTokenMaxTryTimes(int saTokenMaxTryTimes) {
        if (!initialized) {
            this.saTokenMaxTryTimes = saTokenMaxTryTimes;
        }
    }

    public void setSaTokenIsReadBody(Boolean saTokenIsReadBody) {
        if (!initialized) {
            this.saTokenIsReadBody = saTokenIsReadBody;
        }
    }

    public void setSaTokenIsReadHeader(Boolean saTokenIsReadHeader) {
        if (!initialized) {
            this.saTokenIsReadHeader = saTokenIsReadHeader;
        }
    }

    public void setSaTokenIsReadCookie(Boolean saTokenIsReadCookie) {
        if (!initialized) {
            this.saTokenIsReadCookie = saTokenIsReadCookie;
        }
    }

    public void setSaTokenIsWriteHeader(Boolean saTokenIsWriteHeader) {
        if (!initialized) {
            this.saTokenIsWriteHeader = saTokenIsWriteHeader;
        }
    }

    public void setSaTokenStyle(String saTokenStyle) {
        if (!initialized) {
            this.saTokenStyle = saTokenStyle;
        }
    }

    public void setSaTokenDataRefreshPeriod(int saTokenDataRefreshPeriod) {
        if (!initialized) {
            this.saTokenDataRefreshPeriod = saTokenDataRefreshPeriod;
        }
    }

    public void setSaTokenSessionCheckLogin(Boolean saTokenSessionCheckLogin) {
        if (!initialized) {
            this.saTokenSessionCheckLogin = saTokenSessionCheckLogin;
        }
    }

    public void setSaTokenAutoRenew(Boolean saTokenAutoRenew) {
        if (!initialized) {
            this.saTokenAutoRenew = saTokenAutoRenew;
        }
    }

    public void setSaTokenPrefix(String saTokenPrefix) {
        if (!initialized) {
            this.saTokenPrefix = saTokenPrefix;
        }
    }

    public void setSaTokenIsPrint(Boolean saTokenIsPrint) {
        if (!initialized) {
            this.saTokenIsPrint = saTokenIsPrint;
        }
    }

    public void setSaTokenIsLog(Boolean saTokenIsLog) {
        if (!initialized) {
            this.saTokenIsLog = saTokenIsLog;
        }
    }

    public void setSaTokenLogLevel(String saTokenLogLevel) {
        if (!initialized) {
            this.saTokenLogLevel = saTokenLogLevel;
        }
    }

    public void setSaTokenLogLevelInt(int saTokenLogLevelInt) {
        if (!initialized) {
            this.saTokenLogLevelInt = saTokenLogLevelInt;
        }
    }

    public void setSaTokenIsColorLog(Boolean saTokenIsColorLog) {
        if (!initialized) {
            this.saTokenIsColorLog = saTokenIsColorLog;
        }
    }

    public void setSaTokenJwtSecretKey(String saTokenJwtSecretKey) {
        if (!initialized) {
            this.saTokenJwtSecretKey = saTokenJwtSecretKey;
        }
    }

    public void setSaTokenBasic(String saTokenBasic) {
        if (!initialized) {
            this.saTokenBasic = saTokenBasic;
        }
    }

    public void setSaTokenCurrDomain(String saTokenCurrDomain) {
        if (!initialized) {
            this.saTokenCurrDomain = saTokenCurrDomain;
        }
    }

    public void setSaTokenSameTokenTimeout(long saTokenSameTokenTimeout) {
        if (!initialized) {
            this.saTokenSameTokenTimeout = saTokenSameTokenTimeout;
        }
    }

    public void setSaTokenCheckSameToken(Boolean saTokenCheckSameToken) {
        if (!initialized) {
            this.saTokenCheckSameToken = saTokenCheckSameToken;
        }
    }

    public void setSaTokenCookieDomain(String saTokenCookieDomain) {
        if (!initialized) {
            this.saTokenCookieDomain = saTokenCookieDomain;
        }
    }

    public void setSaTokenCookiePath(String saTokenCookiePath) {
        if (!initialized) {
            this.saTokenCookiePath = saTokenCookiePath;
        }
    }

    public void setSaTokenCookieSecure(Boolean saTokenCookieSecure) {
        if (!initialized) {
            this.saTokenCookieSecure = saTokenCookieSecure;
        }
    }

    public void setSaTokenCookieHttpOnly(Boolean saTokenCookieHttpOnly) {
        if (!initialized) {
            this.saTokenCookieHttpOnly = saTokenCookieHttpOnly;
        }
    }

    public void setSaTokenCookieSameSite(String saTokenCookieSameSite) {
        if (!initialized) {
            this.saTokenCookieSameSite = saTokenCookieSameSite;
        }
    }

    public void setSaTokenSignSecretKey(String saTokenSignSecretKey) {
        if (!initialized) {
            this.saTokenSignSecretKey = saTokenSignSecretKey;
        }
    }

    public void setSaTokenSignTimestampDisparity(long saTokenSignTimestampDisparity) {
        if (!initialized) {
            this.saTokenSignTimestampDisparity = saTokenSignTimestampDisparity;
        }
    }

    public void setSaTokenSignIsCheckNonce(Boolean saTokenSignIsCheckNonce) {
        if (!initialized) {
            this.saTokenSignIsCheckNonce = saTokenSignIsCheckNonce;
        }
    }
}