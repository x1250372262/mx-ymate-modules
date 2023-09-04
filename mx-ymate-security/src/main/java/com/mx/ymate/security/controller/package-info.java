@RequestMapping(value = "/mx/security",method = Type.HttpMethod.OPTIONS)
@CrossDomain
@Before(MxSaAnnotationInterceptor.class)
package com.mx.ymate.security.controller;

import com.mx.ymate.security.interceptor.MxSaAnnotationInterceptor;
import com.mx.ymate.security.interceptor.OperationLogInterceptor;
import net.ymate.platform.core.beans.annotation.After;
import net.ymate.platform.core.beans.annotation.Before;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.cors.annotation.CrossDomain;