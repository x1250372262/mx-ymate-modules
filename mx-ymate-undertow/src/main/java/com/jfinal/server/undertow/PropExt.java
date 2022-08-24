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
import com.mx.ymate.dev.constants.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Properties;

/**
 * PropExt
 * 
 * 支持 undertow 从 config 目录以及 jar 包中读取配置文件
 */
public class PropExt {
	
	protected Properties properties = null;
	static ClassLoaderKit classLoaderKit = null;
	
	/**
	 * 避免从 UndertowConfig 中获取 class loader，从而避免触发 UndertowConfig 中的 
	 * getHotSwapResolver()、getClassLoaderKit()，进而避免创建 UndertowConfig
	 * 中的 hotSwapResolver、classLoaderKit 属性对象，进而确保这些对象的创建要晚于配置
	 * 
	 * 即便从 UndertowConfig 中获取 class loader，当前的创建时机并无上述影响，在此仅为避免
	 * 随着开发的推进，在未来可能引起配置丢失问题
	 */
	private ClassLoader getClassLoader() {
		if (classLoaderKit == null) { 
			classLoaderKit = new ClassLoaderKit(PropExt.class.getClassLoader(), null);
		}
		return classLoaderKit.getClassLoader();
	}
	
	/*
	private ClassLoader getClassLoader() {
		ClassLoader ret = Thread.currentThread().getContextClassLoader();
		return ret != null ? ret : getClass().getClassLoader();
	}*/
	
	/**
	 * 支持 new PropExt().appendIfExists(...);
	 */
	public PropExt() {
		properties = new Properties();
	}
	
	public PropExt(Properties properties) {
		this.properties = properties;
	}
	
	/**
	 * PropExt constructor.
	 * @see #PropExt(String, String)
	 */
	public PropExt(String fileName) {
		this(fileName, Constants.DEFAULT_CHARSET);
	}
	
	/**
	 * PropExt constructor
	 * <p>
	 * Example:<br>
	 * PropExt prop = new PropExt("my_config.txt", "UTF-8");<br>
	 * String userName = prop.get("userName");<br><br>
	 * 
	 * prop = new PropExt("com/jfinal/file_in_sub_path_of_classpath.txt", "UTF-8");<br>
	 * String value = prop.get("key");
	 * 
	 * @param fileName the properties file's name in classpath or the sub directory of classpath
	 * @param encoding the encoding
	 */
	public PropExt(String fileName, String encoding) {
		InputStream inputStream = null;
		try {
			inputStream = getClassLoader().getResourceAsStream(fileName);		// properties.load(PropExt.class.getResourceAsStream(fileName));
			if (inputStream == null) {
				throw new FileNotFoundException("Properties file not found in classpath: " + fileName);
			}
			properties = new Properties();
			properties.load(new InputStreamReader(inputStream, encoding));
		} catch (IOException e) {
			throw new RuntimeException("Error loading properties file.", e);
		}
		finally {
			if (inputStream != null) {
				try {inputStream.close();} catch (IOException e) {UndertowKit.doNothing(e);}
			}
		}
	}
	
	/**
	 * PropExt constructor.
	 * @see #PropExt(File, String)
	 */
	public PropExt(File file) {
		this(file, Constants.DEFAULT_CHARSET);
	}
	
	/**
	 * PropExt constructor
	 * <p>
	 * Example:<br>
	 * PropExt prop = new PropExt(new File("/var/config/my_config.txt"), "UTF-8");<br>
	 * String userName = prop.get("userName");
	 * 
	 * @param file the properties File object
	 * @param encoding the encoding
	 */
	public PropExt(File file, String encoding) {
		if (file == null) {
			throw new IllegalArgumentException("File can not be null.");
		}
		if (!file.isFile()) {
			throw new IllegalArgumentException("File not found : " + file.getName());
		}
		
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			properties = new Properties();
			properties.load(new InputStreamReader(inputStream, encoding));
		} catch (IOException e) {
			throw new RuntimeException("Error loading properties file.", e);
		}
		finally {
			if (inputStream != null) {
				try {inputStream.close();} catch (IOException e) {UndertowKit.doNothing(e);}
			}
		}
	}
	
	public PropExt append(PropExt prop) {
		if (prop == null) {
			throw new IllegalArgumentException("prop can not be null");
		}
		properties.putAll(prop.getProperties());
		return this;
	}
	
	public PropExt append(String fileName, String encoding) {
		return append(new PropExt(fileName, encoding));
	}
	
	public PropExt append(String fileName) {
		return append(fileName, Constants.DEFAULT_CHARSET);
	}
	
	public PropExt appendIfExists(String fileName, String encoding) {
		try {
			return append(new PropExt(fileName, encoding));
		} catch (Exception e) {
			return this;
		}
	}
	
	public PropExt appendIfExists(String fileName) {
		return appendIfExists(fileName, Constants.DEFAULT_CHARSET);
	}
	
	public String get(String key) {
		// 下面这行代码只要 key 存在，就不会返回 null。未给定 value 或者给定一个或多个空格都将返回 ""
		String value = properties.getProperty(key);
		return value != null && value.length() != 0 ? value.trim() : null;
	}
	
	public String get(String key, String defaultValue) {
		String value = properties.getProperty(key);
		return value != null && value.length() != 0 ? value.trim() : defaultValue;
	}
	
	public Integer getInt(String key) {
		return getInt(key, null);
	}
	
	public Integer getInt(String key, Integer defaultValue) {
		String value = properties.getProperty(key);
		if (StringUtils.isNotBlank(value)) {
			return Integer.parseInt(value.trim());
		}
		return defaultValue;
	}
	
	public Long getLong(String key) {
		return getLong(key, null);
	}
	
	public Long getLong(String key, Long defaultValue) {
		String value = properties.getProperty(key);
		if (StringUtils.isNotBlank(value)) {
			return Long.parseLong(value.trim());
		}
		return defaultValue;
	}
	
	public Double getDouble(String key) {
		return getDouble(key, null);
	}
	
	public Double getDouble(String key, Double defaultValue) {
		String value = properties.getProperty(key);
		if (StringUtils.isNotBlank(value)) {
			return Double.parseDouble(value.trim());
		}
		return defaultValue;
	}
	
	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}
	
	public Boolean getBoolean(String key, Boolean defaultValue) {
		String value = properties.getProperty(key);
		if (StringUtils.isNotBlank(value)) {
			value = value.toLowerCase().trim();
			if ("true".equals(value)) {
				return true;
			} else if ("false".equals(value)) {
				return false;
			}
			throw new RuntimeException("The value can not parse to Boolean : " + value);
		}
		return defaultValue;
	}
	
	public boolean containsKey(String key) {
		return properties.containsKey(key);
	}
	
	public boolean isEmpty() {
		return properties.isEmpty();
	}
	
	public boolean notEmpty() {
		return ! properties.isEmpty();
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	// ---------
	
	@SuppressWarnings("serial")
	public static class FileNotFoundException extends RuntimeException {
		public FileNotFoundException(String msg) {
			super(msg);
		}
		
		@Override
		public Throwable fillInStackTrace() {
			return this;
		}
	}
}
