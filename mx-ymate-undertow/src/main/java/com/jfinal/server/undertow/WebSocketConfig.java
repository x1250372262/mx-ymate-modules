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

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;

import javax.websocket.server.ServerEndpoint;

/**
 * WebSocketConfig 将 WebSocket 功能所依赖的 jar 包从 UndertowServer
 * 中隔离出来，从而 "非 WebSocket" 项目不必引入 WebSocket 相关 jar 包
 * 而不会报出 ClassNotFound 异常
 * 
 * undertow websocket 官方示例：
 * https://github.com/undertow-io/undertow/tree/master/examples/src/main/java/io/undertow/examples/jsrwebsockets
 */
public class WebSocketConfig {
	
	private WebSocketDeploymentInfo webSocketDeploymentInfo;
	
	/**
	 * WebSocket 功能支持
	 * 
	 * 由于扫描添加 WebSocket 实现类存在潜在的安全风险，jfinal 官方不支持
	 * WebSocket 的扫描添加功能。有需要的同学可以稍微写点代码扫描出所有 WebSocket
	 * 实现类，然后使用 for 循环调用本方法 addWebSocketEndpoint(...) 来实现
	 * 
	 * 添加 WebSocket 的 EndPoint，被添加类必须使用 ServerEndpoint 注解
	 * 
	 * 注意：由于 JFinalFilter 会接管所有不带 "." 字符的 URL 请求所以 @ServerEndpoint
	 *       注解中的 URL 参数值建议以 ".ws" 结尾，否则请求会响应 404 找不到资源，例如：
	 *       	@ServerEndpoint("/demo.ws")
	 *       	public class ChatWebSocketEndpoint  {
	 *       		......
	 *       	}
	 *       
	 *       当然，ServerEndpoint 中的 URL 不使用 ".ws" 结尾是可以的，只需要参考 jfinal 的
	 *       com.jfinal.ext.handler.UrlSkipHandler 做一个自定义的 Handler 跳过
	 *       那些属于 WebSocket 的 URL 即可解决
	 * 
	 * <pre>
	 * 例子:
	 * 1、先定义一个被 @ServerEndpoint 注解的 ChatWebSocketEndpoint
	 * @ServerEndpoint("/demo.ws")
	 * public class ChatWebSocketEndpoint  {
	 *     @OnMessage
	 *     public void message(String message, Session session) {
	 *         for (Session s : session.getOpenSessions()) {
	 *             s.getAsyncRemote().sendText(message);
	 *         }
	 *     }
	 * }
	 * 
	 * 2、向 UndertowServer 添加上述实现类，并启动项目
	 * UndertowServer server = UndertowServer.create(AppConfig.class);
	 * server.addWebSocketEndpoint(ChatWebSocketEndpoint.class);
	 * server.start();
	 * 
	 * 3、在页面中通过 js 脚本即可访问上述 WebSocket 组件
	 * var socket = new WebSocket("ws://localhost:80/demo.ws");
	 * 
	 * 后面的 js 代码省略， js 端的 websocket 具体用法可查找相关资源，这里是一个示例：
	 * https://github.com/undertow-io/undertow/blob/master/examples/src/main/java/io/undertow/examples/jsrwebsockets/index.html
	 * 
	 * </pre>
	 * 
	 */
	public synchronized void addWebSocketEndpoint(Class<?> webSocketEndpoint) {
		if (webSocketEndpoint == null) {
			throw new IllegalArgumentException("webSocketEndpoint can not be null");
		}
		
		if (! webSocketEndpoint.isAnnotationPresent(ServerEndpoint.class)) {
			throw new IllegalArgumentException("webSocketEndpoint must annotated by @ServerEndpoint");
		}
		
		if (webSocketDeploymentInfo == null) {
			webSocketDeploymentInfo = new WebSocketDeploymentInfo();
		}
		
		webSocketDeploymentInfo.addEndpoint(webSocketEndpoint);
	}
	
	public void configWebSocket(DeploymentInfo deploymentInfo) {
		webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(true, 100));
		deploymentInfo.addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME, webSocketDeploymentInfo);
	}
}



