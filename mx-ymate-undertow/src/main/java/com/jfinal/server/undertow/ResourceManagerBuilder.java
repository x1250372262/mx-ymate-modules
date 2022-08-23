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

import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 1：FileResourceManager 支持在普通目录下去加载 css、js 等 web 静态资源
 * 
 * 2：ClassPathResourceManager 支持在 classpath 下与 jar 包内加载 css、js
 *    等 web 静态资源
 * 
 * 3：ClassPathResourceManager 第二个参数不能省略，也不能为 ""，否则可以在浏览器地址栏直接访问
 *    class path、jar 中的所有资源，包括配置文件，下面的用法极其危险:
 *       new ClassPathResourceManager(getClassLoader()));
 * 
 * 4：在需要用到 ClassPathResourceManager 时为其给定一个参数，例如：
 *       new ClassPathResourceManager(getClassLoader(), "webapp"));
 *       
 *    上例给定 webapp 参数，undertow 可以从 classpath、jar 包内的
 *    webapp 之下去加载 web 静态资源
 * 
 */
public class ResourceManagerBuilder {
	
	public ResourceManager build(String resourcePath, ClassLoader classLoader) {
		List<String> resourcePathList = buildResourcePathList(resourcePath);
		
		CompositeResourceManager ret = new CompositeResourceManager();
		buildFileResourceManager(resourcePathList, classLoader, ret);
		buildClassPathResourceManager(resourcePathList, classLoader, ret);
		return ret;
	}
	
	private List<String> buildResourcePathList(String resourcePath) {
		List<String> ret = new ArrayList<>();
		String resourcePathArray[] = resourcePath.split(",");
		for (String path : resourcePathArray) {
			if (UndertowConfig.notBlank(path)) {
				ret.add(path.trim().replace(" ", ""));
			}
		}
		
		/**
		 * 提升配置 undertow.resourcePath 时的用户体验，可以仅配置开发时的 web 资源路径
		 * 可以免去配置生产环境约定的 webapp
		 * 
		 * 例如:
		 *     undertow.resourcePath=src/main/webapp
		 *     或者
		 *     undertow.resourcePath=classpath:static
		 */
		if ( ! ret.contains("webapp") ) {
			ret.add("webapp");
		}
		return ret;
	}
	
	private void buildFileResourceManager(List<String> resourcePathList, ClassLoader classLoader, CompositeResourceManager ret) {
		/**
		 * 开发时使用 eclipse 启动将会正确添加 FileResourceManager
		 * 执行 java -jar xxx.jar 命令时，当前目录下面如果存在 path 目录将会被添加
		 * 
		 * 经测试 eclipse 启动项目的当前目录值为 APP_BASE
		 */
		for (String path : resourcePathList) {
			if (new File(path).isDirectory()) {
		        ret.add(createFileResourceManager(path));
		    }
		}
		
		if (UndertowKit.isDeployMode()) {
			forDeployMode(classLoader, ret);
		}
	}
	
	private FileResourceManager createFileResourceManager(String path) {
		// false 参数提升资源获取性能，并解决 visual studio code 下的大小写问题
		// undertow 的 PathResourceManager 源代码表明配置为 false 可提升性能，后续升级 undertow 时需要关注源码
		return new FileResourceManager(new File(path), 1024, false);
	}
	
	/**
	 * 部署模式下需要额外搜索目录用于 FileResourceManager
	 * 额外添加 webapp、static 为前缀的 ClassPathResourceManager 到最末尾
	 */
	private void forDeployMode(ClassLoader classLoader, CompositeResourceManager ret) {
		/**
		 * 如果前方没有 FileResourceManager 被添加成功，搜索可能存在的 webapp 目录
		 */
		if (ret.isEmpty()) {
			String path = PathKitExt.getLocationPath();
			if (path.endsWith(File.separatorChar + "lib")) {
				path = path.substring(0, path.lastIndexOf(File.separatorChar));
			}
			path = new File(path + File.separator + "webapp").getAbsolutePath();
			
			if (new File(path).isDirectory()) {
				ret.add(createFileResourceManager(path));
			}
		}
	}
	
	/**
	 * undertow.resourcePath 中配置的 classpath:webapp 格式的值创建
	 * ClassPathResourceManager 对象，从 class path 与 jar 包中读取
	 * css、js 等 web 资源文件
	 * 
	 * 建议配置：classpath:webapp 或者 classpath:static
	 */
	private void buildClassPathResourceManager(List<String> resourcePathList, ClassLoader classLoader, CompositeResourceManager ret) {
		String prefix = "classpath:";
		
		for (String path : resourcePathList) {
			if (path.startsWith(prefix)) {
				path = path.substring(prefix.length());
				if (UndertowConfig.notBlank(path)) {
					ret.add(new ClassPathResourceManager(classLoader, path));
				}
			}
		}
	}
}





