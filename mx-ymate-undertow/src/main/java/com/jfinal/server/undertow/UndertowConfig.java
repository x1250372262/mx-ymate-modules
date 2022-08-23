/**
 * Copyright (c) 2011-2021, James Zhan 詹波 (jfinal@126.com).
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

package com.jfinal.server.undertow;

import com.jfinal.server.undertow.hotswap.ClassLoaderKit;
import com.jfinal.server.undertow.hotswap.HotSwapResolver;
import com.jfinal.server.undertow.ssl.SslConfig;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.util.StatusCodes;

import java.util.zip.Deflater;

/**
 * UndertowConfig
 */
public class UndertowConfig {
	
	static final String UNDERTOW_CONFIG					= "undertow.txt";
	
	static final String DEV_MODE							= "undertow.devMode";
	static final String PORT								= "undertow.port";
	static final String HOST								= "undertow.host";
	static final String CONTEXT_PATH						= "undertow.contextPath";
	
	static final String RESOURCE_PATH					= "undertow.resourcePath";
	static final String ALLOW_RESOURCE_CHANGE_LISTENERS	= "undertow.allowResourceChangeListeners";
	
	static final String IO_THREADS						= "undertow.ioThreads";
	static final String WORKER_THREADS					= "undertow.workerThreads";
	
	static final String GZIP_ENABLE						= "undertow.gzip.enable";
	static final String GZIP_LEVEL						= "undertow.gzip.level";
	static final String GZIP_MIN_LENGTH					= "undertow.gzip.minLength";
	
	static final String HTTP2_ENABLE						= "undertow.http2.enable";
	
	static final String SESSION_TIMEOUT					= "undertow.session.timeout";
	static final String SESSION_HOT_SWAP					= "undertow.session.hotSwap";
	
	static final String HOT_SWAP_CLASS_PREFIX			= "undertow.hotSwapClassPrefix";
	
	// ssl 模式下 http 请求是否跳转到 https
	static final String HTTP_TO_HTTPS					= "undertow.http.toHttps";
	// ssl 模式下 http 请求跳转到 https 使用的状态码
	static final String HTTP_TO_HTTPS_STATUS_CODE		= "undertow.http.toHttpsStatusCode";
	// ssl 模式下是否关闭 http
	static final String HTTP_DISABLE						= "undertow.http.disable";
	
	static final String SERVER_NAME						= "undertow.serverName";
	
	// ----------------------------------------------------------------------------
	
	protected String jfinalConfig;
	
	// 开发模式才支持热加载，此配置与 jfinal 中的是不同的用途
	protected volatile static boolean devMode		= false;
	protected int port								= 80;
	protected String host							= "0.0.0.0";
	protected String contextPath						= "/";
	protected String resourcePath					= "src/main/webapp, WebRoot, WebContent";	// web 资源路径
	
	protected Integer ioThreads						= null;
	protected Integer workerThreads					= null;		// ioThreads * 16;
	
	protected boolean gzipEnable						= false;
	protected int gzipLevel							= Deflater.DEFAULT_COMPRESSION;
	protected int gzipMinLength						= 1024;
	
	protected Boolean http2Enable					= null;
	
	protected Integer sessionTimeout					= null;
	protected boolean sessionHotSwap					= true;
	
	protected String hotSwapClassPrefix				= null;
	
	protected SslConfig sslConfig					= null;
	protected boolean httpToHttps					= false;
	protected int httpToHttpsStatusCode				= StatusCodes.FOUND;
	protected boolean httpDisable					= false;
	
	protected String serverName						= null;
	
	protected String[] classPathDirs	;				// 存放 .class 文件的目录
	protected HotSwapResolver hotSwapResolver;
	protected ClassLoaderKit classLoaderKit;
	
	protected PropExt p;
	
	/**
	 * 尝试加载默认配置文件 "undertow.txt" 与 "undertow-pro.txt" 初始化
	 * UndertowConfig，这两个配置文件不存在时不抛出异常
	 */
	public UndertowConfig(Class<?> jfinalConfigClass) {
		this(jfinalConfigClass.getName());
	}
	
	public UndertowConfig(String jfinalConfigClass) {
		this.jfinalConfig = jfinalConfigClass;
		
		p = createPropExt(UNDERTOW_CONFIG);
		if (p.notEmpty()) {
			init();
		}
	}
	
	/**
	 * 使用指定的配置文件初始化 UndertowConfig，该配置文件不存在则抛出异常
	 * 配置文件存在时还会继续尝试其生产环境配置文件，规则是：
	 * 1：当配置文件名是 abc.txt
	 * 2：生产环境配置文件是 abc-pro.txt
	 * 
	 * 注意：生产环境配置文件不存在时不抛出异常，便于支持 fatjar 模式下创建
	 *      config/abc-pro.txt 文件用于配置生产环境 
	 */
	public UndertowConfig(Class<?> jfinalConfigClass, String undertowConfig) {
		this(jfinalConfigClass.getName(), undertowConfig);
	}
	
	public UndertowConfig(String jfinalConfigClass, String undertowConfig) {
		this.jfinalConfig = jfinalConfigClass;
		undertowConfig = undertowConfig.trim();
		
		p = createPropExt(undertowConfig);
		if (p.notEmpty()) {
			init();
		}
	}
	
	protected PropExt createPropExt(String undertowConfig) {
		PropExt ret = UNDERTOW_CONFIG.equals(undertowConfig)
			? new PropExt().appendIfExists(undertowConfig)	// 尝试加载默认配置文件
			: new PropExt().append(undertowConfig);			// 指定配置文件不存在时抛出异常
		return ret.appendIfExists(buildUndertowConfigPro(undertowConfig));	// 尝试加载 product 配置
	}
	
	/**
	 * 假定用户创建 UndertowServer 时指定 undertow 的配置文件为 abc.txt
	 * 或者 abc-dev.txt 或者 abc_dev.txt，例如：
	 *     UndertowServer.create(AppConfig.class, "abc.txt").start();
	 * 
	 * 尝试加载 abc-pro.txt 便于在 fatjar 模式下以 config 目录中通过创建
	 * abc-pro.txt 配置文件覆盖打包在 jar 包中的 abc-dev.txt 配置
	 */
	protected String buildUndertowConfigPro(String undertowConfig) {
		int index = undertowConfig.lastIndexOf('.');
		if (index > 0) {
			String main = undertowConfig.substring(0, index);
			main = removeDevEnds(main);
			String ext = undertowConfig.substring(index);
			return main + "-pro" + ext;
		} else {
			return removeDevEnds(undertowConfig) + "-pro";
		}
	}
	
	private String removeDevEnds(String main) {
		if (main.length() > 4) {
			if (main.endsWith("-dev")) {
				main = main.substring(0, main.lastIndexOf("-dev"));
			} else if (main.endsWith("_dev")) {
				main = main.substring(0, main.lastIndexOf("_dev"));
			}
		}
		return main;
	}
	
	protected void init() {
		initAllowResourceChangeListeners();
		
		devMode						= p.getBoolean(DEV_MODE, devMode);
		port							= p.getInt(PORT, port);
		host							= p.get(HOST, host).trim();
		contextPath					= p.get(CONTEXT_PATH, contextPath).trim();
		resourcePath					= p.get(RESOURCE_PATH, resourcePath).trim();
		
		ioThreads					= buildIoThreads();
		workerThreads				= buildWorkerThreads();	// = p.getInt(WORKER_THREADS, workerThreads);
		
		gzipEnable					= p.getBoolean(GZIP_ENABLE, gzipEnable);
		gzipLevel					= checkGzipLevel(p.getInt(GZIP_LEVEL, gzipLevel));
		gzipMinLength				= p.getInt(GZIP_MIN_LENGTH, gzipMinLength);
		
		http2Enable					= p.getBoolean(HTTP2_ENABLE, http2Enable);
		
		sessionTimeout				= p.getInt(SESSION_TIMEOUT, sessionTimeout);
		sessionHotSwap				= p.getBoolean(SESSION_HOT_SWAP, sessionHotSwap);
		
		hotSwapClassPrefix			= p.get(HOT_SWAP_CLASS_PREFIX, hotSwapClassPrefix);
		
		sslConfig					= new SslConfig(p);
		httpToHttps					= p.getBoolean(HTTP_TO_HTTPS, httpToHttps);
		httpToHttpsStatusCode		= p.getInt(HTTP_TO_HTTPS_STATUS_CODE, httpToHttpsStatusCode);
		httpDisable					= p.getBoolean(HTTP_DISABLE, httpDisable);
		
		serverName					= p.get(SERVER_NAME);
	}
	
	/**
	 * 配置是否打开资源文件变动监听器，配置为 false 可解决项目资源文件过多所导致的启动过慢问题
	 * 
	 * 当项目中的资源文件过多时（例如有几百万张图片），undertow 启动会非常慢，通过如下配置可以解决：
	 * undertow.allowResourceChangeListeners = false
	 * 
	 * 目前是针对 PathResourceManager.java 中的相关逻辑，通过设置 "io.undertow.disable-file-system-watcher"
	 * 为 "true" 来解决的，如果将来这部分源码的逻辑有变，需要随之改变解决方案
	 * 
	 * 对于绝大多数项目无需关注该配置
	 */
	private void initAllowResourceChangeListeners() {
		Boolean allowResourceChangeListeners = p.getBoolean(ALLOW_RESOURCE_CHANGE_LISTENERS);
		if (allowResourceChangeListeners != null) {
			Boolean disableFileSystemWatcher = ! allowResourceChangeListeners;
			System.setProperty("io.undertow.disable-file-system-watcher", disableFileSystemWatcher.toString());
		}
	}
	
	/**
	 * 优先使用外部配置文件中指定的值，当外部配置没有指定时，devMode 并且 notDeployMode 下使用更少的线程以节省时空
	 */
	protected Integer buildIoThreads() {
		Integer valueFromConfig = p.getInt(IO_THREADS);
		if (valueFromConfig != null) {
			return valueFromConfig;
		}
		
		int cpuNum = Runtime.getRuntime().availableProcessors();
		if (isDevMode() && UndertowKit.notDeployMode()) {
			return 2;
		} else {
			// return cpuNum * 2;
			return new Long(Math.round(cpuNum * 1.6180339)).intValue();
			// return cpuNum + 1;
		}
	}
	
	protected Integer buildWorkerThreads() {
		Integer valueFromConfig = p.getInt(WORKER_THREADS);
		if (valueFromConfig != null) {
			return valueFromConfig;
		}
		
		if (isDevMode() && UndertowKit.notDeployMode()) {
			return 4;
		} else {
			return ioThreads * 16;
			// return ioThreads * 8;
		}
	}
	
	protected int checkGzipLevel(int gzipLevel) {
		if (gzipLevel != -1 && (gzipLevel < 1 || gzipLevel > 9)) {
			throw new IllegalArgumentException(GZIP_LEVEL + " 不能配置为 " + gzipLevel + ", 可配置的值为: -1, 1, 2, 3, 4, 5, 6, 7, 8, 9");
		}
		return gzipLevel;
	}
	
	public static boolean isBlank(String str) {
		return str == null || "".equals(str.trim());
	}
	
	public static boolean notBlank(String str) {
		return ! isBlank(str);
	}
	
	/**
	 * 检查 undertow.stopKey 参数合法性，为确保安全性其长度不能少于 16 个字符
	 * 
	protected String checkStopKey(String stopKey) {
		if (stopKey != null) {
			stopKey = stopKey.trim();
			if (stopKey.length() < 16) {
				throw new IllegalArgumentException("undertow.stopKey 的长度不能小于 16 个字符");
			}
		}
		return stopKey;
	} */
	
	public String getJFinalConfig() {
		return jfinalConfig;
	}
	
	public ResourceManager getResourceManager() {
		/*
		CompositeResourceManager ret = new CompositeResourceManager();
		String resourcePathArray[] = resourcePath.split(",");
		for (String path : resourcePathArray) {
			path = path.trim();
			if (new File(path).isDirectory()) {
		        ret.add(new FileResourceManager(new File(path)));
		    }
		}
		return ret; */
		
		return new ResourceManagerBuilder().build(resourcePath, getClassLoader());
	}
	
	protected ClassLoaderKit getClassLoaderKit() {
		if (classLoaderKit == null) {
			classLoaderKit = new ClassLoaderKit(Undertow.class.getClassLoader(), getHotSwapResolver());
		}
		return classLoaderKit;
	}
	
	public ClassLoader getClassLoader() {
		// return isDevMode() ? getClassLoaderKit().getClassLoader() : Undertow.class.getClassLoader();
		
		/**
		 * 不论是否为 devMode 都使用 HotSwapClassLoader
		 * HotSwapClassLoader 添加了 isDevMode() 判断
		 * 一直使用 HotSwapClassLoader 是因为为其添加了
		 * 配置文件 config 目录到 class path，以便可以加载
		 * 外部配置文件
		 */
		return getClassLoaderKit().getClassLoader();
	}
	
	public void replaceClassLoader() {
		if (isDevMode()) {
			getClassLoaderKit().replaceClassLoader();
		}
	}
	
	public HotSwapResolver getHotSwapResolver() {
		if (hotSwapResolver == null) {
			hotSwapResolver = new HotSwapResolver(getClassPathDirs());
			// 后续将此代码转移至 HotSwapResolver 中去，保持 UndertowConfig 的简洁
			if (hotSwapClassPrefix != null) {
				for (String prefix : hotSwapClassPrefix.split(",")) {
					if (notBlank(prefix)) {
						hotSwapResolver.addHotSwapClassPrefix(prefix);
					}
				}
			}
		}
		return hotSwapResolver;
	}
	
	public void setHotSwapResolver(HotSwapResolver hotSwapResolver) {
		this.hotSwapResolver = hotSwapResolver;
	}
	
	public void addSystemClassPrefix(String prefix) {
		getHotSwapResolver().addSystemClassPrefix(prefix);
	}
	
	public void addHotSwapClassPrefix(String prefix) {
		getHotSwapResolver().addHotSwapClassPrefix(prefix);
	}
	
	/**
	 * 获取存放 .class 文件的所有 classPath 目录，绝大部分场景下只有一个目录
	 */
	public String[] getClassPathDirs() {
		if (classPathDirs == null) {
			classPathDirs = UndertowKit.getClassPathDirs();
		}
		return classPathDirs;
	}
	
	/*
	public void setClassPathDirs(String[] classPathDirs) {
		this.classPathDirs = classPathDirs;
	}*/
	
	public void setDevMode(boolean devMode) {
		if (p.getBoolean(DEV_MODE) == null) {
			UndertowConfig.devMode = devMode;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + DEV_MODE + " = " + p.getBoolean(DEV_MODE));
		}
	}
	
	public static boolean isDevMode() {
		return devMode;
	}
	
	public void setPort(int port) {
		if (p.getInt(PORT) == null) {
			this.port = port;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + PORT + " = " + p.getInt(PORT));
		}
	}
	
	public int getPort() {
		return port;
	}
	
	public void setContextPath(String contextPath) {
		if (p.get(CONTEXT_PATH) == null) {
			this.contextPath = contextPath;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + CONTEXT_PATH + " = " + p.get(CONTEXT_PATH));
		}
	}
	
	public String getContextPath() {
		if ("/".equals(contextPath)) {
			return contextPath;
		}
		if (isBlank(contextPath)) {
			contextPath = "/";
			return contextPath;
		}
		
		// 添加前缀 "/"
		if ( ! contextPath.startsWith("/") ) {
			contextPath = "/" + contextPath;
		}
		// 去除后缀 "/"
		if (contextPath.endsWith("/")) {
			contextPath = contextPath.substring(0, contextPath.length() - 1);
		}
		
		return contextPath;
	}
	
	public void setResourcePath(String resourcePath) {
		if (isBlank(resourcePath)) {
			throw new IllegalArgumentException("resourcePath can not be blank");
		}
		if (p.get(RESOURCE_PATH) == null) {
			this.resourcePath = resourcePath;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + RESOURCE_PATH + " = " + p.get(RESOURCE_PATH));
		}
	}
	
	public String getResourcePath() {
		return resourcePath;
	}
	
	public void setHost(String host) {
		if (p.get(HOST) == null) {
			this.host = host;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + HOST + " = " + p.get(HOST));
		}
	}
	
	public String getHost() {
		return host;
	}
	
	public void setIoThreads(int ioThreads) {
		if (p.getInt(IO_THREADS) == null) {
			this.ioThreads = ioThreads;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + IO_THREADS + " = " + p.getInt(IO_THREADS));
		}
	}
	
	public Integer getIoThreads() {
		return ioThreads;
	}
	
	public void setWorkerThreads(int workerThreads) {
		if (p.getInt(WORKER_THREADS) == null) {
			this.workerThreads = workerThreads;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + WORKER_THREADS + " = " + p.getInt(WORKER_THREADS));
		}
	}
	
	public Integer getWorkerThreads() {
		return workerThreads;
	}
	
	public void setGzipEnable(boolean gzipEnable) {
		if (p.getBoolean(GZIP_ENABLE) == null) {
			this.gzipEnable = gzipEnable;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + GZIP_ENABLE + " = " + p.getBoolean(GZIP_ENABLE));
		}
	}
	
	public boolean isGzipEnable() {
		return gzipEnable;
	}
	
	public void setGzipLevel(int gzipLevel) {
		if (p.getInt(GZIP_LEVEL) == null) {
			this.gzipLevel = checkGzipLevel(gzipLevel);
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + GZIP_LEVEL + " = " + p.getInt(GZIP_LEVEL));
		}
	}
	
	public int getGzipLevel() {
		return gzipLevel;
	}
	
	public void setGzipMinLength(int gzipMinLength) {
		if (p.getInt(GZIP_MIN_LENGTH) == null) {
			this.gzipMinLength = gzipMinLength;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + GZIP_MIN_LENGTH + " = " + p.getInt(GZIP_MIN_LENGTH));
		}
		
	}
	
	public int getGzipMinLength() {
		return gzipMinLength;
	}
	
	public void setHttp2Enable(boolean http2Enable) {
		if (p.getBoolean(HTTP2_ENABLE) == null) {
			this.http2Enable = http2Enable;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + HTTP2_ENABLE + " = " + p.getBoolean(HTTP2_ENABLE));
		}
	}
	
	public Boolean getHttp2Enable() {
		return http2Enable;
	}
	
	/**
	 * session 过期时间，注意单位是秒
	 */
	public void setSessionTimeout(int sessionTimeout) {
		if (p.getInt(SESSION_TIMEOUT) == null) {
			this.sessionTimeout = sessionTimeout;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + SESSION_TIMEOUT + " = " + p.getInt(SESSION_TIMEOUT));
		}
	}
	
	public Integer getSessionTimeout() {
		return sessionTimeout;
	}
	
	public void setSessionHotSwap(boolean sessionHotSwap) {
		if (p.getBoolean(SESSION_HOT_SWAP) == null) {
			this.sessionHotSwap = sessionHotSwap;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + SESSION_HOT_SWAP + " = " + p.getBoolean(SESSION_HOT_SWAP));
		}
	}
	
	public boolean getSessionHotSwap() {
		return sessionHotSwap;
	}
	
	// ---------
	
	public boolean isSslEnable() {
		return sslConfig != null && sslConfig.isEnable();
	}
	
	public SslConfig getSslConfig() {
		return sslConfig;
	}
	
	public void setSslConfig(SslConfig sslConfig) {
		this.sslConfig = sslConfig;
	}
	
	public void setHttpToHttps(boolean httpToHttps) {
		if (p.getBoolean(HTTP_TO_HTTPS) == null) {
			this.httpToHttps = httpToHttps;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + HTTP_TO_HTTPS + " = " + p.getBoolean(HTTP_TO_HTTPS));
		}
	}
	
	public boolean isHttpToHttps() {
		return httpToHttps;
	}
	
	public void setHttpToHttpsStatusCode(int httpToHttpsStatusCode) {
		if (p.getInt(HTTP_TO_HTTPS_STATUS_CODE) == null) {
			this.httpToHttpsStatusCode = httpToHttpsStatusCode;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + HTTP_TO_HTTPS_STATUS_CODE + " = " + p.getInt(HTTP_TO_HTTPS_STATUS_CODE));
		}
	}
	
	public int getHttpToHttpsStatusCode() {
		return httpToHttpsStatusCode;
	}
	
	public void setHttpDisable(boolean httpDisable) {
		if (p.getBoolean(HTTP_DISABLE) == null) {
			this.httpDisable = httpDisable;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + HTTP_DISABLE + " = " + p.getBoolean(HTTP_DISABLE));
		}
	}
	
	public boolean isHttpDisable() {
		return httpDisable;
	}
	
	public void setServerName(String serverName) {
		if (p.get(SERVER_NAME) == null) {
			this.serverName = serverName;
		} else {
			System.out.println("undertow-server: 优先使用配置文件中的 " + SERVER_NAME + " = " + p.get(SERVER_NAME));
		}
	}
	
	/**
	 * 在 HTTP response header 中显示的服务名，配置为 disable 时表示不启用
	 * 
	 * 未配置则使用默认值，例如：JFinal 4.3，否则使用配置的值
	 * 注意：disable 为特殊配置，表示不启用该功能
	 */
	public String getServerName() {
		if (isBlank(serverName)) {
			return "JFinal " + UndertowKit.getJFinalVersion();
		} else {
			return "disable".equals(serverName.trim()) ? null : serverName.trim();
		}
	}
}









