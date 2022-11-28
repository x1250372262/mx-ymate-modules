/**
 * Copyright (c) 2011-2021, James Zhan 詹波 (jfinal@126.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.server.undertow;

import com.jfinal.server.undertow.handler.HttpToHttpsHandler;
import com.jfinal.server.undertow.hotswap.HotSwapWatcher;
import com.jfinal.server.undertow.session.HotSwapSessionManagerFactory;
import com.jfinal.server.undertow.session.HotSwapSessionPersistenceManager;
import com.jfinal.server.undertow.ssl.SslBuilder;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.UndertowOptions;
import io.undertow.Version;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.SetHeaderHandler;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;
import net.ymate.platform.webmvc.support.DispatchFilter;
import net.ymate.platform.webmvc.support.GeneralWebFilter;
import net.ymate.platform.webmvc.support.WebAppEventListener;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import java.text.DecimalFormat;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.jfinal.server.undertow.UndertowConfig.*;

/**
 * UndertowServer
 * <p>
 * 官方示例：https://github.com/undertow-io/undertow/tree/master/examples/src/main/java/io/undertow/examples
 * <p>
 * <p>
 * 注意：关闭服务时使用：kill pid，不要使用：kill -9 pid，
 * 否则 JFinalConfig.onStop() 不会被回调
 * <p>
 * 文档：
 * http://undertow.io/undertow-docs/undertow-docs-2.0.0/index.html
 * https://blog.csdn.net/zhaowen25/article/details/45324805
 */
public class UndertowServer {

    public String version = "1.0.0";

    protected UndertowConfig config;

    protected DeploymentInfo deploymentInfo;
    protected DeploymentManager deploymentManager;

    protected Builder builder;
    protected Undertow undertow;

    protected volatile boolean started = false;
    protected volatile HotSwapWatcher hotSwapWatcher;
    protected DecimalFormat decimalFormat = new DecimalFormat("#.#");

    protected Consumer<WebBuilder> webBuilder;
    protected Consumer<UndertowConfig> configConsumer;
    protected BiConsumer<ClassLoader, DeploymentInfo> onDeployConsumer;
    protected Consumer<Builder> onStartConsumer;

    public static void run() {
        create().start();
    }

    public static void run(int port, boolean devMode) {
        create().setPort(port).setDevMode(devMode).start();
    }

    /**
     * 创建 UndertowServer
     * <p>
     * 尝试使用 "undertow.txt" 以及 "undertow-pro.txt" 初始化 undertow
     * 当配置文件不存在时不抛出异常而是使用默认值进行初始化
     */
    public static UndertowServer create() {
        return new UndertowServer(new UndertowConfig());
    }

    /**
     * 创建 UndertowServer
     * <p>
     * 使用指定的配置文件及其生产环境配置文件初始化 undertow，假定指定的配置文件名为
     * "abc.txt"，其生产环境配置文件名约定为 "abc-pro.txt"
     * <p>
     * 注意：指定的配置文件必须要存在，而约定的那个生产环境配置文件可以不必存在
     */
    public static UndertowServer create(String undertowConfig) {
        return new UndertowServer(new UndertowConfig(undertowConfig));
    }

    /**
     * 使用手动构建的 UndertowConfig 对象创建 UndertowServer
     */
    public static UndertowServer create(UndertowConfig undertowConfig) {
        return new UndertowServer(undertowConfig);
    }

    protected UndertowServer(UndertowConfig undertowConfig) {
        this.config = undertowConfig;
    }

    public synchronized void start() {
        if (configConsumer != null) {
            configConsumer.accept(config);
            configConsumer = null;            // 配置在整个生命周期只能调用一次
        }

        loadCommandLineParameter();

        try {
            String msg = "Starting YMP " + UndertowKit.getYmpVersion() + " -> http://" + config.getHost() + ":" + config.getPort() + getContextPathInfo();
            if (config.isSslEnable()) {
                msg = msg + ", https://" + config.getHost() + ":" + config.getSslConfig().getPort() + getContextPathInfo();
            }
            long start = System.currentTimeMillis();
            doStart();
            System.out.println(msg);
            System.out.println("Info: YMP-undertow " + version + ", undertow " + Version.getVersionString() + ", jvm " + System.getProperty("java.version"));
            System.out.println("Starting Complete in " + getTimeSpent(start) + " seconds. Welcome To The YMP World (^_^)\n");

            /**
             * 使用 kill pid 命令或者 ctrl + c 关闭 JVM 时，调用 UndertowServer.stop() 方法，
             * 以便触发 JFinalConfig.onStop();
             *
             * 注意：下方代码严格测试过，只支持 kill pid 不支持 kill -9 pid
             */
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    UndertowServer.this.stop();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            stopSilently();

            // 支持在 doStart() 中抛出异常后退出 JVM，例如端口被占用，否则在 linux 控制台 JVM 并不会退出
            System.exit(1);
        }
    }

    protected String getContextPathInfo() {
        return "/".equals(config.getContextPath()) ? "" : config.getContextPath();
    }

    /**
     * 使用 System.getProperty(...) 加载命令行传入的 undertow.port 与 undertow.host 参数，
     * 因为这两个参数最有可能在运行项目时进行变动，这个功能可以免去创建 config/undertow-pro.txt
     * 来配置最需要变动的 port 与 host 参数，进一步节省时间
     * <p>
     * 使用示例：
     * java -Dundertow.port=8080 -Dundertow.host=0.0.0.0 -jar jfinal-club-release.jar
     * <p>
     * 传参注意事项：
     * 1：传参规则由 java 命令行给定，与 jfinal undertow 项目完全无关
     * 2：传参以 "-D" 为前缀，并且该前缀与后方的参数名之间不能有空格
     * 3：参数名与参数值中间用等号字符分格，且等号前后不能空格
     */
    protected void loadCommandLineParameter() {
        String port = System.getProperty(PORT);
        String host = System.getProperty(HOST);
        String resourcePath = System.getProperty(RESOURCE_PATH);
        String ioThreads = System.getProperty(IO_THREADS);
        String workerThreads = System.getProperty(WORKER_THREADS);

        if (notBlank(port)) {
            config.port = Integer.parseInt(port.trim());
        }
        if (notBlank(host)) {
            config.host = host.trim();
        }
        if (notBlank(resourcePath)) {
            config.resourcePath = resourcePath.trim();
        }
        if (notBlank(ioThreads)) {
            config.ioThreads = Integer.parseInt(ioThreads.trim());
        }
        if (notBlank(workerThreads)) {
            config.workerThreads = Integer.parseInt(workerThreads.trim());
        }
    }

    protected boolean notBlank(String str) {
        return str != null && !"".equals(str.trim());
    }

    protected void doStart() {
        if (started) {
            return;
        }

        /**
         * jfinal-undertow 中一切依赖 devMode 的动作都会切到生产模式，性能发挥到极致
         * 例如：HotSwapWatcher 不会被开启
         *
         * HotSwapClassLoader 的 return parent.loadClass(...) 依赖于 devMode 判断
         * 所以即便在部署模式下 devMode 设置为 true 时会抛出异常：Can not create instance
         * of class: jfinal.com.common.JFinalComConfig.
         * Please check the config in web.xml
         */
        if (UndertowKit.isDeployMode()) {
            UndertowConfig.devMode = false;
        }

        init();
		
		/* 挪到 init() 之中，让 config 中的值更早生效
		if (configConsumer != null) {
			configConsumer.accept(config);
			configConsumer = null;			// 配置在整个生命周期只能调用一次
		}*/

        if (onDeployConsumer != null) {
            onDeployConsumer.accept(config.getClassLoader(), deploymentInfo);
        }

        if (UndertowKit.notAvailablePort(config.getPort())) {
            throw new IllegalStateException("port: " + config.getPort() + " not available!\n");
        }


        deploymentManager = Servlets.defaultContainer().addDeployment(deploymentInfo);
        deploymentManager.deploy();

        configHttp();
        // configSsl();

        // 在 start 前进行更多配置
        if (onStartConsumer != null) {
            onStartConsumer.accept(builder);
        }

        undertow = builder.build();
        undertow.start();

        if (isDevMode() && hotSwapWatcher == null) {
            hotSwapWatcher = new HotSwapWatcher(this);
            hotSwapWatcher.start();
        }

        started = true;
    }

    protected void configHttp() {
        HttpHandler httpHandler = null;
        try {
            httpHandler = deploymentManager.start();
        } catch (ServletException e) {
            stopSilently();
            throw new RuntimeException(e);
        }
        HttpHandler pathHandler = Handlers.path().addPrefixPath(config.getContextPath(), httpHandler);
        pathHandler = configHandler(pathHandler);
        pathHandler = configGzip(pathHandler);
        pathHandler = configServerName(pathHandler);

        pathHandler = configSsl(pathHandler);

        if (config.isSslEnable() && config.isHttpDisable()) {
            // 开启 ssl 并且 undertow.http.disable = true 时不开启用于 http 的端口
        } else {
            builder.addHttpListener(config.getPort(), config.getHost());
        }

        builder
                // .setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false)
                .setHandler(pathHandler);
    }

    protected HttpHandler configSsl(HttpHandler httpHandler) {
        if (config.isSslEnable()) {
            new SslBuilder(builder, config).build();
            if (config.isHttpToHttps()) {
                httpHandler = new HttpToHttpsHandler(httpHandler, config);
            }
        } else {
            if (config.isHttpToHttps()) {
                System.err.println("http redirect to https needs ssl support");
            }
        }

        return httpHandler;
    }

    /**
     * 子类覆盖此方法可以添加 Handler 到 Handler 链条之中
     * <p>
     * 假定子类扩展出了一个 MyHandler，子类覆盖中的代码一般如下：
     * protected HttpHandler configHandler(HttpHandler next) {
     * return new MyHandler(next);
     * }
     * <p>
     * 更详细的示例参考 jfinal 社区分享： https://jfinal.com/share/2066
     */
    protected HttpHandler configHandler(HttpHandler next) {
        return next;
    }

    protected HttpHandler configGzip(HttpHandler pathHandler) {
        if (config.isGzipEnable()) {
            ContentEncodingRepository repository = new ContentEncodingRepository();
            GzipEncodingProvider provider = new GzipEncodingProvider(config.getGzipLevel());
            int minLength = config.getGzipMinLength();
            Predicate predicate = minLength > 0 ? Predicates.requestLargerThan(minLength) : Predicates.truePredicate();
            repository.addEncodingHandler("gzip", provider, 100, predicate);
            return new EncodingHandler(pathHandler, repository);
        }
        return pathHandler;
    }

    protected HttpHandler configServerName(HttpHandler pathHandler) {
        String serverName = config.getServerName();
        if (serverName != null) {
            return new SetHeaderHandler(pathHandler, "Server", serverName);
        } else {
            return pathHandler;
        }
    }

    protected void init() {
        builder = Undertow.builder();
		
		/*
		if (configConsumer != null) {
			configConsumer.accept(config);
			configConsumer = null;			// 配置在整个生命周期只能调用一次
		}*/

        configUndertow();

        configListener();
        // configWebSocket();
        // configServlet();
        // configFilter();
        configWeb();

        configYmpFilter();
    }

    public synchronized void stop() {
        if (started) {
            started = false;
        } else {
            return;
        }

        System.out.println("\nShutdown Undertow Server ......");
        long start = System.currentTimeMillis();
        try {
            if (hotSwapWatcher != null) {
                hotSwapWatcher.exit();
            }

            doStop();

        } catch (Exception e) {
            e.printStackTrace();
            stopSilently();
        } finally {
            System.out.println("Shutdown Complete in " + getTimeSpent(start) + " seconds. See you later (^_^)\n");
        }
    }

    protected void doStop() throws ServletException {
        // 保留以下三行
        // deploymentManager.undeploy();
        // Servlets.defaultContainer().removeDeployment(deploymentInfo);
        // undertow.stop();

        /**
         * 必须设置 HotSwapWatcher.setDaemon(false)，否则下面两行代码将退出 JVM，无法再次启动 undertow
         * 触发 JFinalConfig.onStop() 方法必须要调用 deploymentManager.stop()
         * 该方法不能在 deploymentManager.undeploy() 这后调用，否则有 NPE
         */
        deploymentManager.stop();
        undertow.stop();
    }

    /**
     * HotSwapWatcher 调用 restart()
     */
    public synchronized void restart() {
        if (started) {
            started = false;
        } else {
            return;
        }

        try {
            System.err.println("\nLoading changes ......");
            long start = System.currentTimeMillis();

            doStop();
            config.replaceClassLoader();
            doStart();

            System.err.println("Loading complete in " + getTimeSpent(start) + " seconds (^_^)\n");

        } catch (Exception e) {
            System.err.println("Error restarting webapp after change in watched files");
            e.printStackTrace();
        }
    }

    protected String getTimeSpent(long startTime) {
        float timeSpent = (System.currentTimeMillis() - startTime) / 1000F;
        return decimalFormat.format(timeSpent);
    }

    protected void stopSilently() {
        try {
            started = false;
            if (undertow != null) {
                undertow.stop();
            }
        } catch (Exception e) {
            UndertowKit.doNothing(e);
        }
    }

    /**
     * config 便于使用 lambda 直接拿到 UndertowConfig 对象进行更多更灵活的配置，
     *
     * <pre>
     * 例子：
     * UndertowServer
     * 		.create(AppConfig.class)
     * 		.config( config -> {
     * 			config.setHost("0.0.0.0");
     *          config.setSessionTimeout(30 * 60);
     *        })
     * 		.start();
     * </pre>
     */
    public UndertowServer config(Consumer<UndertowConfig> configConsumer) {
        this.configConsumer = configConsumer;
        return this;
    }

    /**
     * 安插在 DeploymentManager.deploy() 前的钩子方法，目前用于支持添加 shiro 的
     * EnvironmentLoaderListener，支持 shiro 还需要调用：
     * addHotSwapClassPrefix("org.apache.shiro.")
     *
     * <pre>
     * 支持 shiro 的例子代码如下：
     *
     * UndertowServer.create("com.mypackage.AppConfig")
     *     // .addHotSwapClassPrefix("org.apache.shiro.")
     *     .addHotSwapClassPrefix("org.apache.")
     *     .configWeb( builder -> {
     *         builder.addFilter("shiro", "org.apache.shiro.web.servlet.ShiroFilter");
     *         builder.addFilterUrlMapping("shiro", "/*");
     *      })
     *     .onDeploy((classLoader, deploymentInfo) -> {
     *          try {
     *              String classStr = "org.apache.shiro.web.env.EnvironmentLoaderListener";
     *              Class<?> c = classLoader.loadClass(classStr);
     *              deploymentInfo.addDeploymentCompleteListener((javax.servlet.ServletContextListener)c.newInstance());
     *          } catch (Exception e) {
     *              throw new RuntimeException(e);
     *          }
     *      })
     *     .start();
     * </pre>
     */
    public UndertowServer onDeploy(BiConsumer<ClassLoader, DeploymentInfo> onDeployConsumer) {
        this.onDeployConsumer = onDeployConsumer;
        return this;
    }

    /**
     * 安插在启动前的钩子方法，便于用户通过 lambda 表达式进行更多个性化配置
     * <p>
     * 例如以下配置：
     * // In HTTP/1.1, connections are persistent unless declared otherwise.
     * // Adding a "Connection: keep-alive" header to every response would only
     * // add useless bytes.
     * builder.setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false)
     *
     * <pre>
     * 例子：
     * UndertowServer.create(AppConfig.class)
     *     .onStart( builder -> {
     *         builder.setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false);
     *      })
     *     .start();
     * </pre>
     */
    public UndertowServer onStart(Consumer<Builder> onStartConsumer) {
        this.onStartConsumer = onStartConsumer;
        return this;
    }


    protected void configUndertow() {
        // url 支持特殊字符，例如: '{' 与 '}'
        builder.setServerOption(UndertowOptions.ALLOW_UNESCAPED_CHARACTERS_IN_URL, true);

        // ---------

        if (config.isSslEnable()) {
            if (config.getHttp2Enable() != null && config.getHttp2Enable()) {
                builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
            }
        }
        if (config.getIoThreads() != null) {
            builder.setIoThreads(config.getIoThreads());
        }
        if (config.getWorkerThreads() != null) {
            builder.setWorkerThreads(config.getWorkerThreads());
        }

        // ---------------------------------------------------------------------------------

        deploymentInfo = Servlets.deployment();
        configSessionPersistenceManager();

        DeploymentInfo di = deploymentInfo;

        // di.setResourceManager(new ClassPathResourceManager(getClassLoader()));
        di.setResourceManager(config.getResourceManager());

        di.setClassLoader(config.getClassLoader());
        di.setContextPath(config.getContextPath());
        di.setDeploymentName("ymp");
        di.setEagerFilterInit(true);        // 启动时初始化 filter

        if (config.getSessionTimeout() != null) {
            di.setDefaultSessionTimeout(config.getSessionTimeout());
        }
    }

    private void configListener() {
        deploymentInfo.addListener(new ListenerInfo(WebAppEventListener.class));
    }

    protected void configYmpFilter() {
        deploymentInfo.addFilter(Servlets.filter("DispatchFilter", DispatchFilter.class))
                .addFilterUrlMapping("DispatchFilter", "/*", DispatcherType.REQUEST);

        deploymentInfo.addFilter(Servlets.filter("GeneralWebFilter", GeneralWebFilter.class)
                        .addInitParam("responseHeaders", "X-Frame-Options=SAMEORIGIN"))
                .addFilterUrlMapping("DispatchFilter", "/*", DispatcherType.REQUEST);

    }


    protected void configWeb() {
        if (webBuilder != null) {
            WebBuilder wb = new WebBuilder(this);
            webBuilder.accept(wb);

            if (wb.webSocketConfig != null) {
                wb.webSocketConfig.configWebSocket(deploymentInfo);
            }
        }
    }

    /**
     * Filter、Servlet、Listener、WebSocket 组件统一配置入口
     *
     * <pre>
     * 例子：
     * UndertowServer.create(AppConfig.class)
     *     .configWeb( builder -> {
     *         // 配置 Filter
     *         builder.addFilter("myFilter", "com.abc.MyFilter");
     *         builder.addFilterUrlMapping("myFilter", "/*");
     *         builder.addFilterInitParam("myFilter", "key", "value");
     *
     *         // 配置 Servlet
     *         builder.addServlet("myServlet", "com.abc.MyServlet");
     *         builder.addServletMapping("myServlet", "*.do");
     *         builder.addServletInitParam("myServlet", "key", "value");
     *
     *         // 配置 Listener
     *         builder.addListener("com.abc.MyListener");
     *
     *         // 配置 WebSocket，MyWebSocket 需使用 ServerEndpoint 注解
     *         builder.addWebSocketEndpoint("com.abc.MyWebSocket");
     *      })
     *     .start();
     *
     *
     * 以上代码给出了 Filter、Servlet、Listener、WebSocket 的配置实例，其中 MyWebSocket
     * 需要使用 ServerEndpoint 注解标识其为一个 WebSocket 组件，例如：
     *
     * @ServerEndpoint("/myapp.ws")
     * public class MyWebSocket {
     *     @OnMessage
     *     public void message(String message, Session session) {
     *         for (Session s : session.getOpenSessions()) {
     *             s.getAsyncRemote().sendText(message);
     *         }
     *     }
     * }
     *
     * 与上述 MyWebSocket 配合使用的例子 html 在这里可以下载：https://github.com/undertow-io/undertow/blob/master/examples/src/main/java/io/undertow/examples/jsrwebsockets/index.html
     * 注意要修改一下该 html 中的 url 为： "ws://localhost:80/myapp.ws"，端口号适当调整
     *
     *
     * 注意：由于 JFinalFilter 会接管所有不带 "." 字符的 URL 请求所以 @ServerEndpoint
     *       注解中的 URL 参数值建议以 ".ws" 结尾，否则请求会响应 404 找不到资源，例如：
     *        @ServerEndpoint("/myapp.ws")
     *        public class MyWebSocketEndpoint  {
     *       		......
     *        }
     *
     *       当然，ServerEndpoint 中的 URL 不使用 ".ws" 结尾也是可以的，只需要参考 jfinal 的
     *       UrlSkipHandler 做一个 Handler 跳过属于属于 WebSocket 的 URL 即可
     *
     *
     * 最后：由于扫描添加 WebSocket 实现类存在潜在的安全风险，jfinal 官方不支持
     *      WebSocket 的扫描添加功能。有需要的同学可以稍微写点代码扫描出所有 WebSocket
     *      实现类，然后在 configWeb 中使用 for 循环来批量添加 WebSocket 组件
     *      只建议在 WebSocket 组件十分多的情况下使用扫描
     *
     * </pre>
     */
    public UndertowServer configWeb(Consumer<WebBuilder> webBuilder) {
        this.webBuilder = webBuilder;
        return this;
    }

    /**
     * 配置 SessionPersistenceManager，支持 session 热加载
     */
    protected void configSessionPersistenceManager() {
        if (config.getSessionHotSwap() && isDevMode() /* && UndertowKit.notDeployMode() */) {
            deploymentInfo.setSessionPersistenceManager(new HotSwapSessionPersistenceManager(config));
            deploymentInfo.setSessionManagerFactory(HotSwapSessionManagerFactory.me);
        }
    }

    public UndertowConfig getUndertowConfig() {
        return config;
    }

    public UndertowServer setHost(String host) {
        config.setHost(host);
        return this;
    }

    public UndertowServer setPort(int port) {
        config.setPort(port);
        return this;
    }

    public UndertowServer setGzipEnable(boolean gzipEnable) {
        config.setGzipEnable(gzipEnable);
        return this;
    }

    public UndertowServer setContextPath(String contextPath) {
        config.setContextPath(contextPath);
        return this;
    }

    public UndertowServer setResourcePath(String resourcePath) {
        config.setResourcePath(resourcePath);
        return this;
    }

    /**
     * 设置为 true 时支持热加载，开发环境必配置项。建议在配置文件中进行配置：
     * undertow.devMode=true
     * <p>
     * 配置文件中配置便于生产环境修改该项配置，提升性能
     * <p>
     * 注意：undertow.devMode 与 jfinal 中的 devMode 没有任何关系
     */
    public UndertowServer setDevMode(boolean devMode) {
        config.setDevMode(devMode);
        return this;
    }

    public boolean isDevMode() {
        return UndertowConfig.isDevMode();
    }

    public boolean isStarted() {
        return started;
    }

    // ------------------

    /**
     * 仅用于解决项目的 JFinalConfig 继承类打成 jar 包，并且使用 undertow.devMode=true 配置
     * 时报出的异常，以上两个条件没有同时成立时无需理会，也就是说没有报异常就无需理会
     * <p>
     * 假定项目中的 JFinalConfig 的继承类 com.abc.MyConfig 被打进了 jar 包并且
     * undertow.devMode 设置成了 true，这里在启动项目的时候由于 ClassLoader
     * 不同会报出以下异常：
     * Can not create instance of class: com.abc.MyConfig. Please check the config in web.xml
     * <p>
     * 解决办法是使用 addHotSwapClassPrefix(...) :
     * UndertowServer.create(MyConfig.class).addHotSwapClassPrefix("com.abc.").start();
     * <p>
     * 只添加 JFinalConfig 的继承类 com.abc.MyConfig 也可以：
     * UndertowServer.create(MyConfig.class).addHotSwapClassPrefix("com.abc.MyConfig").start();
     * <p>
     * 注意：该配置对生产环境无任何影响，在打包部署前无需删除该配置
     */
    public UndertowServer addHotSwapClassPrefix(String prefix) {
        config.addHotSwapClassPrefix(prefix);
        return this;
    }

    public UndertowServer addSystemClassPrefix(String prefix) {
        config.addSystemClassPrefix(prefix);
        return this;
    }

    /**
     * jfinal.sh 脚本中的 MAIN_CLASS 始终配置为 com.jfinal.server.undertow.UndertowServer
     * undertow.txt 中配置 configClass=jfinalConfigClass 指向 JFinalConfig 的继承类
     * 这样就可以避免修改 jfinal.sh，只需修改 undertow.txt
     * <p>
     * 此方法仅适用于无需对 UndertowServer 进行代码配置的场景，例如需要添加
     * servlet、websocket 的场景则不适用，因为配置文件不支持这些配置
     */
    public static void main(String[] args) {
        String undertowConfig = (args == null || args.length == 0)
                ? UndertowConfig.UNDERTOW_CONFIG
                : args[0];

        undertowConfig = undertowConfig.trim();
        PropExt p = new PropExt(undertowConfig);
        String configClass = p.get("configClass");
        if (configClass == null) {
            throw new RuntimeException("configClass must config in file : " + undertowConfig);
        }

        UndertowServer.create().start();
    }
}






