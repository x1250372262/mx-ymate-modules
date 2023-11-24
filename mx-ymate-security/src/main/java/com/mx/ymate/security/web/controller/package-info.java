@RequestMapping(value = "/mx/security",method = Type.HttpMethod.OPTIONS)
@CrossDomain
@Before(SecurityUserInterceptor.class)
package com.mx.ymate.security.web.controller;

import com.mx.ymate.security.interceptor.SecurityUserInterceptor;
import net.ymate.platform.core.beans.annotation.Before;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.cors.annotation.CrossDomain;