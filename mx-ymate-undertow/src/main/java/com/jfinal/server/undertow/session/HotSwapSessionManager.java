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

import io.undertow.server.session.InMemorySessionManager;
import io.undertow.server.session.SessionIdGenerator;

public class HotSwapSessionManager extends InMemorySessionManager {
	
	public HotSwapSessionManager(String deploymentName, int maxSessions, boolean expireOldestUnusedSessionOnMax) {
		super(deploymentName, maxSessions, expireOldestUnusedSessionOnMax);
	}
	
	public HotSwapSessionManager(SessionIdGenerator sessionIdGenerator, String deploymentName, int maxSessions, boolean expireOldestUnusedSessionOnMax) {
		super(sessionIdGenerator, deploymentName, maxSessions, expireOldestUnusedSessionOnMax);
	}
	
	public HotSwapSessionManager(SessionIdGenerator sessionIdGenerator, String deploymentName, int maxSessions, boolean expireOldestUnusedSessionOnMax, boolean statisticsEnabled) {
		super(sessionIdGenerator, deploymentName, maxSessions, expireOldestUnusedSessionOnMax, statisticsEnabled);
	}
	
	public HotSwapSessionManager(String deploymentName, int maxSessions) {
		super(deploymentName, maxSessions);
	}
	
	public HotSwapSessionManager(String id) {
		super(id);
	}
	
	/**
	 * do nothing here
	 */
	@Override
	public void stop() {
		// for (Map.Entry<String, SessionImpl> session : sessions.entrySet()) {
			// session.getValue().destroy();
			// sessionListeners.sessionDestroyed(session.getValue(), null, SessionListener.SessionDestroyedReason.UNDEPLOY);
		// }
		// sessions.clear();
	}
}



