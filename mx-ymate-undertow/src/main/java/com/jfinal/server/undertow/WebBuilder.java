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

import io.undertow.server.session.SessionListener;
import io.undertow.servlet.api.*;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import java.util.EventListener;

/**
 * WebBuilder 用于构建 WebSocket、Filter、Servlet、Listener
 * 缩减 UndertowServer、UndertowConfig 代码，简化配置
 */
public class WebBuilder {
	
	protected UndertowServer undertowServer;
	protected DeploymentInfo deploymentInfo;
	protected WebSocketConfig webSocketConfig;
	
	public WebBuilder(UndertowServer undertowServer) {
		this.undertowServer = undertowServer;
		this.deploymentInfo = undertowServer.deploymentInfo;
	}
	
	protected Class<?> loadClass(String name) {
		try {
			return undertowServer.config.getClassLoader().loadClass(name);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	// --------- Filter
	
	public WebBuilder addFilter(String filterName, String filterClass) {
		@SuppressWarnings("unchecked")
		Class<? extends Filter> filter = (Class<? extends Filter>)loadClass(filterClass);
		deploymentInfo.addFilter(new FilterInfo(filterName, filter));
		return this;
	}
	
	public WebBuilder addFilterUrlMapping(String filterName, String mapping) {
		return addFilterUrlMapping(filterName, mapping, DispatcherType.REQUEST);
	}
	
	public WebBuilder addFilterUrlMapping(String filterName, String mapping, DispatcherType dispatcherType) {
		deploymentInfo.addFilterUrlMapping(filterName, mapping, dispatcherType);
		return this;
	}
	
	public WebBuilder addFilterInitParam(String filterName, String paraName, String paraValue) {
		if (deploymentInfo.getFilters().get(filterName) == null) {
			throw new IllegalStateException("Filter not found : " + filterName + ", using addFilter(...) to add filter first.");
		}
		deploymentInfo.getFilters().get(filterName).addInitParam(paraName, paraValue);
		return this;
	}
	
	// --------- Servlet
	
	public WebBuilder addServlet(String servletName, String servletClass) {
		@SuppressWarnings("unchecked")
		Class<? extends Servlet> servlet = (Class<? extends Servlet>)loadClass(servletClass);
		deploymentInfo.addServlet(new ServletInfo(servletName, servlet));
		return this;
	}
	
	public void checkServlet(String servletName) {
		if (deploymentInfo.getServlets().get(servletName) == null) {
			throw new IllegalStateException("Servlet not found : " + servletName + ", using addServlet(...) to add servlet first.");
		}
	}
	
	public WebBuilder addServletMapping(String servletName, String mapping) {
		checkServlet(servletName);
		deploymentInfo.getServlets().get(servletName).addMapping(mapping);
		return this;
	}
	
	public WebBuilder addServletMappings(String servletName, String... mappings) {
		checkServlet(servletName);
		ServletInfo si = deploymentInfo.getServlets().get(servletName);
		for(String m : mappings) {
			si.addMapping(m);
		}
		return this;
	}
	
	public WebBuilder addServletInitParam(String servletName, String paraName, String paraValue) {
		checkServlet(servletName);
		deploymentInfo.getServlets().get(servletName).addInitParam(paraName, paraValue);
		return this;
	}
	
	public WebBuilder setServletAsyncSupported(String servletName, boolean asyncSupported) {
		checkServlet(servletName);
		deploymentInfo.getServlets().get(servletName).setAsyncSupported(asyncSupported);
		return this;
	}
	
	public WebBuilder setServletLoadOnStartup(String servletName, int loadOnStartup) {
		checkServlet(servletName);
		deploymentInfo.getServlets().get(servletName).setLoadOnStartup(loadOnStartup);
		return this;
	}
	
	// --------- Listener
	
	public WebBuilder addListener(String listenerClass) {
		@SuppressWarnings("unchecked")
		Class<? extends EventListener> listener = (Class<? extends EventListener>)loadClass(listenerClass);
		deploymentInfo.addListener(new ListenerInfo(listener));
		return this;
	}

	// --------- SessionListener
	
	public WebBuilder addSessionListener(SessionListener sessionListener) {
		deploymentInfo.addSessionListener(sessionListener);
		return this;
	}
	
	// --------- WebSocket
	
	public WebBuilder addWebSocketEndpoint(String webSocketEndpointClass) {
		Class<?> webSocketEndpoint = loadClass(webSocketEndpointClass);
		if (webSocketConfig == null) {
			webSocketConfig = new WebSocketConfig();
		}
		webSocketConfig.addWebSocketEndpoint(webSocketEndpoint);
		return this;
	}
	
	public WebBuilder addWebSocketEndpoint(Class<?> webSocketEndpointClass) {
		return addWebSocketEndpoint(webSocketEndpointClass.getName());
	}
	
	// --------- 
	
	/**
	 * 添加初始化参数，对应 web.xml 中的 context-param 例如：
	 * 
	 * <context-param>
	 * 	<param-name>shiroEnvironmentClass</param-name>  
	 * 	<param-value>org.apache.shiro.web.env.IniWebEnvironment</param-value>
	 * </context-param>  
	 */
	public WebBuilder addInitParameter(String name, String value) {
		deploymentInfo.addInitParameter(name, value);
		return this;
	}
	
	// ---------
	
	/**
	 * 配置错误页面，通过继承 ErrorPage 还可以扩展出对错误页面做日志等等功能
	 */
	public WebBuilder addErrorPage(ErrorPage errorPage) {
		deploymentInfo.addErrorPage(errorPage);
		return this;
	}
	
	/**
	 * 配置错误页面
	 */
	public WebBuilder addErrorPage(int errorCode, String errorPage) {
		return addErrorPage(new ErrorPage(errorPage, errorCode));
	}
	
	/**
	 * 配置 404 错误页面
	 */
	public WebBuilder add404ErrorPage(String _404ErrorPage) {
		return addErrorPage(404, _404ErrorPage);
	}
	
	/**
	 * 配置欢迎页面
	 */
	public WebBuilder addWelcomePage(String welcomePage) {
		deploymentInfo.addWelcomePage(welcomePage);
		return this;
	}
	
	// ---------
	
	/**
	 * 方便配置 SessionManagerFactory 用于指定 SessionManager，
	 * 从而实现分布式 session：https://jfinal.com/share/2371
	 */
	public WebBuilder setSessionManagerFactory(SessionManagerFactory sessionManagerFactory) {
		deploymentInfo.setSessionManagerFactory(sessionManagerFactory);
		return this;
	}
	
	// ---------
	
	/**
	 * 通过 getDeploymentInfo() 获取到 DeploymentInfo 对象以后，可以进行更多 web 配置
	 * 
	 * <pre>
	 *   例如：
	 *     getDeploymentInfo().addWelcomePage("index.html");
	 *     getDeploymentInfo().addErrorPage(new ErrorPage("404.html", 404));
	 * </pre>
	 * 
	 * 还可以支持 JSP 配置
	 * <pre>
	 *   例如：
	 *   UndertowServer
	 *   .create(DemoConfig.class)
	 *   .configWeb(wb -> {
	 *   	wb.getDeploymentInfo().addServlet(JspServletBuilder.createServlet("Default Jsp Servlet", "*.jsp"));
	 *   	
	 *   	HashMap<String, TagLibraryInfo> tagLibraryInfo = new HashMap<>();
	 *   	JspServletBuilder.setupDeployment(wb.getDeploymentInfo(), new HashMap<String, JspPropertyGroup>(),
	 *   									 tagLibraryInfo, new HackInstanceManager());
	 *   })
	 *   .start();
	 * </pre>
	 */
	public DeploymentInfo getDeploymentInfo() {
		return deploymentInfo;
	}
}




