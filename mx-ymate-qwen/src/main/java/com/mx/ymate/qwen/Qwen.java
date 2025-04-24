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
package com.mx.ymate.qwen;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.tools.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.JsonObject;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.qwen.annotation.QwenClass;
import com.mx.ymate.qwen.annotation.QwenFunction;
import com.mx.ymate.qwen.annotation.QwenParam;
import com.mx.ymate.qwen.bean.AiResult;
import com.mx.ymate.qwen.bean.MethodBean;
import com.mx.ymate.qwen.bean.ParameterBean;
import com.mx.ymate.qwen.impl.DefaultQwenConfig;
import io.reactivex.Flowable;
import net.ymate.platform.core.*;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.IModuleConfigurer;
import net.ymate.platform.core.module.impl.DefaultModuleConfigurer;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public final class Qwen implements IModule, IQwen {


    private static volatile IQwen instance;

    private IApplication owner;

    private IQwenConfig config;

    private GenerationParam generationParam;

    private Map<String, MethodBean> methodBeanMap = new HashMap<>();

    private final List<ToolBase> toolList = new ArrayList<>();

    private List<String> modelFilter = Arrays.asList("qwen-vl", "qwen-audio");

    private boolean initialized;

    public static IQwen get() {
        IQwen inst = instance;
        if (inst == null) {
            synchronized (Qwen.class) {
                inst = instance;
                if (inst == null) {
                    instance = inst = YMP.get().getModuleManager().getModule(Qwen.class);
                }
            }
        }
        return inst;
    }

    public Qwen() {
    }

    public Qwen(IQwenConfig config) {
        this.config = config;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public void initialize(IApplication owner) throws Exception {
        if (!initialized) {
            this.owner = owner;
            if (config == null) {
                IApplicationConfigureFactory configureFactory = owner.getConfigureFactory();
                if (configureFactory != null) {
                    IApplicationConfigurer configurer = configureFactory.getConfigurer();
                    IModuleConfigurer moduleConfigurer = configurer == null ? null : configurer.getModuleConfigurer(MODULE_NAME);
                    if (moduleConfigurer != null) {
                        config = DefaultQwenConfig.create(moduleConfigurer);
                    } else {
                        config = DefaultQwenConfig.create(DefaultModuleConfigurer.createEmpty(MODULE_NAME));
                    }
                }
            }
            if (!config.isInitialized()) {
                config.initialize(this);
            }

            if (config.isEnabled()) {
                // 可以初始化模型配置
                initParam(config);
                initToolInfo(config);
            }
            initialized = true;
            YMP.showVersion("初始化 mx-ymate-qwen-${version} 模块成功", new Version(1, 0, 0, Qwen.class, Version.VersionType.Release));
        }
    }

    private Object init(Class<?> clazz) {
        Object object;
        try {
            object = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    public void initToolInfo(IQwenConfig config) {
        List<String> packageList = config.packageList();
        if (CollUtil.isEmpty(packageList)) {
            return;
        }
        for (String packageName : packageList) {
            Set<Class<?>> classSet = ClassUtil.scanPackageByAnnotation(packageName, QwenClass.class);
            for (Class<?> clazz : classSet) {
                //获取有所的public方法  并且方法包含了LlmFunction注解的
                List<Method> methodList = ClassUtil.getPublicMethods(clazz, method -> method.getAnnotation(QwenFunction.class) != null);
                if (CollUtil.isEmpty(methodList)) {
                    continue;
                }
                Object object = init(clazz);
                for (Method method : methodList) {
                    QwenFunction qwenFunction = method.getAnnotation(QwenFunction.class);
                    String name = qwenFunction.name();
                    String description = qwenFunction.description();
                    MethodBean methodBean = new MethodBean(name, description, method, object);
                    List<ParameterBean> parameterBeanList = new ArrayList<>();
                    for (Parameter parameter : method.getParameters()) {
                        QwenParam qwenParam = parameter.getAnnotation(QwenParam.class);
                        if (qwenParam == null) {
                            throw new RuntimeException(parameter.getName() + "参数,QwenParam 注解不能为空");
                        }
                        parameterBeanList.add(new ParameterBean(qwenParam.name(), qwenParam.description(), parameter.getType().getSimpleName().toLowerCase()));
                    }
                    methodBean.setParameters(parameterBeanList);
                    methodBeanMap.put(name, methodBean);
                }
            }
        }
        methodBeanMap.forEach((key, methodBean) -> {
            FunctionDefinition functionDefinition = FunctionDefinition.builder()
                    .name(methodBean.getName())
                    .description(methodBean.getDescription())
                    .build();
            JsonObject parameterJson = new JsonObject();
            for (ParameterBean parameter : methodBean.getParameters()) {
                JsonObject typeJson = new JsonObject();
                typeJson.addProperty("type", parameter.getType());
                typeJson.addProperty("description", parameter.getDescription());
                parameterJson.add(parameter.getName(), typeJson);
            }
            functionDefinition.setParameters(parameterJson);
            toolList.add(ToolFunction.builder().function(functionDefinition).build());
        });
    }


    /**
     * 初始化千问配置
     *
     * @param config
     */
    private void initParam(IQwenConfig config) {
        generationParam = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(config.apiKey())
                // 此处以qwen-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model(config.model())
                .temperature(config.temperature())
                .topP(config.topP())
                .topK(config.topK())
                .repetitionPenalty(config.repetitionPenalty())
                .maxTokens(config.maxTokens())
                .seed(config.seed())
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        String stopStr = config.stop();
        if (StringUtils.isNotBlank(stopStr)) {
            String[] stopArr = stopStr.split("\\|");
            generationParam.setStopStrings(Arrays.asList(stopArr));
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void close() throws Exception {
        if (initialized) {
            initialized = false;
            if (config.isEnabled()) {
            }
            config = null;
            owner = null;
        }
    }

    @Override
    public IApplication getOwner() {
        return owner;
    }

    @Override
    public IQwenConfig getConfig() {
        return config;
    }

    /**
     * @param result
     * @return
     */
    private AiResult getAiResult(GenerationResult result) {
        AiResult aiResult = new AiResult();
        //获取choice 默认就取第一个 有问题之后再说
        GenerationOutput.Choice choice = result.getOutput().getChoices().get(0);
        Message message = choice.getMessage();
        if (message.getToolCalls() != null) {
            throw new RuntimeException("工具调用请使用chatTool方法");
        }
        aiResult.setFinishReason(choice.getFinishReason());
        aiResult.setRole(message.getRole());
        if (modelFilter.contains(config.model())) {
            aiResult.setContent(message.getContents());
        } else {
            aiResult.setContent(message.getContent());
        }
        return aiResult;
    }

    public interface IStreamHandler {

        /**
         * 处理流式输出
         *
         * @param aiResult
         */
        void handle(AiResult aiResult);
    }

    @Override
    public MxResult chat(String content) {
        GenerationParam param = generationParam;
        param.setMessages(Collections.singletonList(Message.builder().role(Role.USER.getValue()).content(content).build()));
        Generation gen = new Generation();
        try {
            AiResult aiResult = getAiResult(gen.call(param));
            return MxResult.okData(aiResult);
        } catch (Exception e) {
            return MxResult.fail().msg(e.getMessage());
        }
    }

    @Override
    public MxResult chatStream(String content, IStreamHandler iStreamHandler) {
        GenerationParam param = generationParam;
        param.setMessages(Collections.singletonList(Message.builder().role(Role.USER.getValue()).content(content).build()));
        param.setIncrementalOutput(true);
        Generation gen = new Generation();
        try {
            Flowable<GenerationResult> result = gen.streamCall(param);
            result.blockingForEach(generationResult -> {
                AiResult aiResult = getAiResult(generationResult);
                iStreamHandler.handle(aiResult);
            });
            return MxResult.okData(result);
        } catch (Exception e) {
            return MxResult.fail().msg(e.getMessage());
        }
    }


    @Override
    public MxResult chatTool(String content) {
        if (CollUtil.isEmpty(toolList)) {
            return MxResult.fail().msg("调用工具方法，工具链不能为空");
        }
        GenerationParam param = generationParam;
        param.setMessages(Collections.singletonList(Message.builder().role(Role.USER.getValue()).content(content).build()));
        param.setTools(toolList);
        Generation gen = new Generation();
        try {
            GenerationResult generationResult = gen.call(param);
            //获取choice 默认就取第一个 有问题之后再说
            GenerationOutput.Choice choice = generationResult.getOutput().getChoices().get(0);
            Message message = choice.getMessage();
            if (message.getToolCalls() == null) {
                AiResult aiResult = getAiResult(generationResult);
                return MxResult.okData(aiResult);
            } else {
                List<MethodBean> chooseMethodList = new ArrayList<>();
                for (ToolCallBase toolCall : message.getToolCalls()) {
                    String functionName = ((ToolCallFunction) toolCall).getFunction().getName();
                    if (StringUtils.isBlank(functionName)) {
                        continue;
                    }
                    MethodBean methodBean = methodBeanMap.get(functionName);
                    if (methodBean == null) {
                        continue;
                    }
                    String functionArgument = ((ToolCallFunction) toolCall).getFunction().getArguments();
                    if (StringUtils.isNotBlank(functionArgument)) {
                        // 先整个转为Map
                        methodBean.setParameterMap(JSON.parseObject(functionArgument, new TypeReference<Map<String, Object>>() {
                        }));
                        chooseMethodList.add(methodBean);
                    }
                }
                return MxResult.okData(chooseMethodList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
//            Logs.get().getLogger().error(e);
//            return MxResult.fail().msg(e.getMessage());
        }

    }

}
