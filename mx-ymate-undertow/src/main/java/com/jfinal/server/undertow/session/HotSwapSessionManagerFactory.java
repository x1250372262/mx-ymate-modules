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

package com.jfinal.server.undertow.session;

import io.undertow.server.session.SessionManager;
import io.undertow.servlet.api.Deployment;
import io.undertow.servlet.core.InMemorySessionManagerFactory;

public class HotSwapSessionManagerFactory extends InMemorySessionManagerFactory {
	
	public static final HotSwapSessionManagerFactory me = new HotSwapSessionManagerFactory();
	
	private volatile SessionManager sessionManager = null;
	private final int maxSessions;
	private final boolean expireOldestUnusedSessionOnMax;
	
	public HotSwapSessionManagerFactory() {
		this(-1, false);
	}
	
	public HotSwapSessionManagerFactory(int maxSessions) {
		this(maxSessions, false);
	}
	
	public HotSwapSessionManagerFactory(int maxSessions, boolean expireOldestUnusedSessionOnMax) {
		this.maxSessions = maxSessions;
		this.expireOldestUnusedSessionOnMax = expireOldestUnusedSessionOnMax;
	}
	
	@Override
	public SessionManager createSessionManager(Deployment deployment) {
		if (sessionManager == null) {
			synchronized(me) {
				if (sessionManager == null) {
					sessionManager = new HotSwapSessionManager(deployment.getDeploymentInfo().getSessionIdGenerator(), deployment.getDeploymentInfo().getDeploymentName(), maxSessions, expireOldestUnusedSessionOnMax, deployment.getDeploymentInfo().getMetricsCollector() != null);
				}
			}
		}
		
		return sessionManager;
	}
}



