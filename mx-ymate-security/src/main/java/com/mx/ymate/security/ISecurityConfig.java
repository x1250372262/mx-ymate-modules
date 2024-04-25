package com.mx.ymate.security;

import cn.dev33.satoken.config.SaTokenConfig;
import com.mx.ymate.security.handler.ILoginHandler;
import com.mx.ymate.security.handler.IUserHandler;
import net.ymate.platform.core.beans.annotation.Ignored;
import net.ymate.platform.core.support.IInitialization;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description:
 */
@Ignored
public interface ISecurityConfig extends IInitialization<ISecurity> {

    String ENABLED = "enabled";

    String CLIENT = "client";
    String LOGIN_HANDLER_CLASS = "loginHandlerClass";

    String USER_HANDLER_CLASS = "userHandlerClass";

    String ERROR_COUNT = "errorCount";

    String OPEN_LOG = "openLog";

    String EXCLUDE_PATH_PATTERNS = "excludePathPatterns";

    String CHECK_LOCK = "checkLock";

    String SATOKEN_NAME = "satoken.name";

    String SATOKEN_TIMEOUT = "satoken.timeout";

    String SATOKEN_ACTIVE_TIMEOUT = "satoken.activeTimeout";

    String SATOKEN_DYNAMIC_ACTIVE_TIMEOUT = "satoken.dynamicActiveTimeout";

    String SATOKEN_IS_CONCURRENT = "satoken.isConcurrent";

    String SATOKEN_IS_SHARE = "satoken.isShare";

    String SATOKEN_MAX_LOGIN_COUNT = "satoken.maxLoginCount";

    String SATOKEN_MAX_TRY_TIMES = "satoken.maxTryTimes";

    String SATOKEN_IS_READ_BODY = "satoken.isReadBody";

    String SATOKEN_IS_READ_HEADER = "satoken.isReadHeader";

    String SATOKEN_IS_READ_COOKIE = "satoken.isReadCookie";

    String SATOKEN_IS_WRITE_HEADER = "satoken.isWriteHeader";

    String SATOKEN_STYLE = "satoken.style";

    String SATOKEN_DATA_REFRESH_PERIOD = "satoken.dataRefreshPeriod";

    String SATOKEN_SESSION_CHECK_LOGIN = "satoken.sessionCheckLogin";

    String SATOKEN_AUTO_RENEW = "satoken.autoRenew";

    String SATOKEN_PREFIX = "satoken.prefix";

    String SATOKEN_IS_PRINT = "satoken.isPrint";

    String SATOKEN_IS_LOG = "satoken.isLog";

    String SATOKEN_LOG_LEVEL = "satoken.logLevel";
    String SATOKEN_LOG_LEVEL_INT = "satoken.logLevelInt";

    String SATOKEN_IS_COLOR_LOG = "satoken.isColorLog";

    String SATOKEN_JWT_SECRET_KEY = "satoken.jwtSecretKey";

    String SATOKEN_BASIC = "satoken.basic";

    String SATOKEN_CURR_DOMAIN = "satoken.currDomain";
    String SATOKEN_SAME_TOKEN_TIMEOUT = "satoken.sameTokenTimeout";

    String SATOKEN_COOKIE_DOMAIN = "satoken.cookieDomain";

    String SATOKEN_COOKIE_PATH = "satoken.cookiePath";

    String SATOKEN_COOKIE_SECURE = "satoken.cookieSecure";

    String SATOKEN_COOKIE_HTTP_ONLY = "satoken.cookieHttpOnly";

    String SATOKEN_COOKIE_SAME_SITE = "satoken.cookieSameSite";

    String SATOKEN_SIGN_SECRET_KEY = "satoken.signSecretKey";

    String SATOKEN_SIGN_TIMESTAMP_DISPARITY = "satoken.signTimestampDisparity";

    String SATOKEN_SIGN_IS_CHECK_NONCE = "satoken.signIsCheckNonce";

    /**
     * 模块是否已启用, 默认值: true
     *
     * @return 返回false表示禁用
     */
    boolean isEnabled();

    /**
     * 客户端名称
     *
     * @return
     */
    String client();

    /**
     * loginHandler实现类
     *
     * @return
     */
    ILoginHandler loginHandlerClass();

    /**
     * userHandler实现类
     */
    IUserHandler userHandlerClass();

    /**
     * 验证错误N次后锁定账户  默认不锁定  -1不锁定
     */
    int errorCount();

    /**
     * 是否开启日志记录 默认false
     */
    boolean openLog();

    /**
     * 是否检查挂机锁定 默认false
     */
    boolean checkLock();

    /**
     * 登录拦截排除的路径用|分割
     */
    String excludePathPatterns();

    /**
     * token名称 (同时也是cookie名称)
     */
    String saTokenName();

    /**
     * token的长久有效期(单位:秒) 默认30天, -1代表永久
     */
    long saTokenTimeout();

    /**
     * token临时有效期 [指定时间内无操作就视为token过期] (单位: 秒), 默认-1 代表不限制
     * (例如可以设置为1800代表30分钟内无操作就过期)
     */
    long saTokenActiveTimeout();

    /**
     * 是否启用动态 activeTimeout 功能，如不需要请设置为 false，节省缓存请求次数
     */
    Boolean saTokenDynamicActiveTimeout();

    /**
     * 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
     */
    Boolean saTokenIsConcurrent();

    /**
     * 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
     */
    Boolean saTokenIsShare();

    /**
     * 同一账号最大登录数量，-1代表不限 （只有在 isConcurrent=true, isShare=false 时此配置才有效）
     */
    int saTokenMaxLoginCount();

    /**
     * 在每次创建 token 时的最高循环次数，用于保证 token 唯一性（-1=不循环尝试，直接使用
     *
     * @return
     */
    int saTokenMaxTryTimes();

    /**
     * 是否尝试从请求体里读取token
     */
    Boolean saTokenIsReadBody();

    /**
     * 是否尝试从header里读取token
     */
    Boolean saTokenIsReadHeader();

    /**
     * 是否尝试从cookie里读取token
     */
    Boolean saTokenIsReadCookie();

    /**
     * 是否在登录后将 Token 写入到响应头
     */
    Boolean saTokenIsWriteHeader();

    /**
     * token风格(默认可取值：uuid、simple-uuid、random-32、random-64、random-128、tik)
     */
    String saTokenStyle();

    /**
     * 默认dao层实现类中，每次清理过期数据间隔的时间 (单位: 秒) ，默认值30秒，设置为-1代表不启动定时清理
     */
    int saTokenDataRefreshPeriod();

    /**
     * 获取[token专属session]时是否必须登录 (如果配置为true，会在每次获取[token-session]时校验是否登录)
     */
    Boolean saTokenSessionCheckLogin();

    /**
     * 是否打开自动续签 (如果此值为true, 框架会在每次直接或间接调用getLoginId()时进行一次过期检查与续签操作)
     */
    Boolean saTokenAutoRenew();

    /**
     * token前缀, 格式样例(satoken: Bearer xxxx-xxxx-xxxx-xxxx)
     */
    String saTokenPrefix();

    /**
     * 是否在初始化配置时打印版本字符画
     */
    Boolean saTokenIsPrint();

    /**
     * 是否打印操作日志
     */
    Boolean saTokenIsLog();

    /**
     * 日志等级（trace、debug、info、warn、error、fatal）
     */
    String saTokenLogLevel();

    /**
     * 日志等级 int 值（1=trace、2=debug、3=info、4=warn、5=error、6=fatal）
     */
    int saTokenLogLevelInt();

    /**
     * 是否打印彩色日志 默认false
     *
     * @return
     */
    Boolean saTokenIsColorLog();

    /**
     * jwt秘钥 (只有集成 jwt 模块时此参数才会生效)
     */
    String saTokenJwtSecretKey();

    /**
     * Http Basic 认证的账号和密码
     */
    String saTokenBasic();

    /**
     * 配置当前项目的网络访问地址
     */
    String saTokenCurrDomain();

    /**
     * Same-Token 的有效期 (单位: 秒)
     */
    long saTokenSameTokenTimeout();

    /**
     * 是否校验Same-Token（部分rpc插件有效
     */
    Boolean saTokenCheckSameToken();

    /**
     * 域（写入Cookie时显式指定的作用域, 常用于单点登录二级域名共享Cookie的场景）
     */
    String saTokenCookieDomain();

    /**
     * 路径
     */
    String saTokenCookiePath();

    /**
     * 是否只在 https 协议下有效
     */
    Boolean saTokenCookieSecure();

    /**
     * 是否禁止 js 操作 Cookie
     */
    Boolean saTokenCookieHttpOnly();

    /**
     * 第三方限制级别（Strict=完全禁止，Lax=部分允许，None=不限制）
     */
    String saTokenCookieSameSite();

    /**
     * API 调用签名秘钥
     *
     * @return
     */
    String saTokenSignSecretKey();

    /**
     * #接口调用时的时间戳允许的差距（单位：ms），-1 代表不校验差距，默认15分钟
     * #比如此处你配置了60秒，当一个请求从 client 发起后，如果 server 端60秒内没有处理，60秒后再想处理就无法校验通过了。
     *
     * @return
     */
    long saTokenSignTimestampDisparity();

    /**
     * 是否校验 nonce 随机字符串 默认true
     *
     * @return
     */
    Boolean saTokenSignIsCheckNonce();

    /**
     * 转换satoken配置
     *
     * @return
     */
    SaTokenConfig toSaTokenConfig();

}