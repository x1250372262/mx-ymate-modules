/*
 * Copyright 2022 the original author or authors.
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
package com.mx.ymate.qwen.impl;

import com.mx.ymate.dev.util.ConfigUtil;
import com.mx.ymate.qwen.IQwen;
import com.mx.ymate.qwen.IQwenConfig;
import net.ymate.platform.core.module.IModuleConfigurer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public final class DefaultQwenConfig implements IQwenConfig {

    private boolean enabled;
    private String apiKey;
    private String model;
    private List<String> packageList;
    private Float temperature;
    private Double topP;
    private Integer topK;
    private Float repetitionPenalty;
    private Integer maxTokens;
    private Integer seed;
    private String stop;

    private boolean initialized;


    public static DefaultQwenConfig create(IModuleConfigurer moduleConfigurer) {
        return new DefaultQwenConfig(moduleConfigurer);
    }


    private DefaultQwenConfig() {
    }

    private DefaultQwenConfig(IModuleConfigurer moduleConfigurer) {
        ConfigUtil configUtil = new ConfigUtil(moduleConfigurer.getConfigReader().toMap());
        enabled = configUtil.getBool(ENABLED, true);
        apiKey = configUtil.getString(API_KEY);
        model = configUtil.getString(MODEL);
        packageList = findPackageList(configUtil.getString(PACKAGES));
        temperature = configUtil.getFloat(TEMPERATURE);
        topP = configUtil.getDouble(TOP_P);
        topK = configUtil.getInteger(TOP_K);
        repetitionPenalty = configUtil.getFloat(REPETITION_PENALTY);
        maxTokens = configUtil.getInteger(MAX_TOKENS);
        seed = configUtil.getInteger(SEED);
        stop = configUtil.getString(STOP);

    }

    private List<String> findPackageList(String packages) {
        List<String> packageList = new ArrayList<>();
        if (StringUtils.isNotBlank(packages)) {
            String[] packageArr = packages.split("\\|");
            if (packageArr.length > 0) {
                packageList.addAll(Arrays.asList(packageArr));
            }
        }

        return packageList;
    }

    @Override
    public void initialize(IQwen owner) throws Exception {
        if (!initialized) {
            initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    @Override
    public String apiKey() {
        return apiKey;
    }


    @Override
    public String model() {
        return model;
    }

    @Override
    public List<String> packageList() {
        return packageList;
    }

    @Override
    public Float temperature() {
        return temperature;
    }

    @Override
    public Double topP() {
        return topP;
    }

    @Override
    public Integer topK() {
        return topK;
    }

    @Override
    public Float repetitionPenalty() {
        return repetitionPenalty;
    }

    @Override
    public Integer maxTokens() {
        return maxTokens;
    }

    @Override
    public Integer seed() {
        return seed;
    }

    @Override
    public String stop() {
        return stop;
    }


}
