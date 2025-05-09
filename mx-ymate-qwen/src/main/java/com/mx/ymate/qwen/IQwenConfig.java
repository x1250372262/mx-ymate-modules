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

import net.ymate.platform.core.beans.annotation.Ignored;
import net.ymate.platform.core.support.IInitialization;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Ignored
public interface IQwenConfig extends IInitialization<IQwen> {

    String ENABLED = "enabled";
    String API_KEY = "apiKey";
    String MODEL = "model";
    String PACKAGES = "packages";
    String TEMPERATURE = "temperature";
    String TOP_P = "top_p";
    String TOP_K = "top_k";
    String REPETITION_PENALTY = "repetition_penalty";
    String MAX_TOKENS = "max_tokens";
    String SEED = "seed";
    String STOP = "stop";

    /**
     * 模块是否已启用, 默认值: true
     *
     * @return 返回false表示禁用
     */
    boolean isEnabled();

    /**
     * apiKey
     *
     * @return
     */
    String apiKey();

    /**
     * 模型
     *
     * @return
     */
    String model();

    /**
     * 方法所在包名
     *
     * @return
     */
    List<String> packageList();

    /**
     * 采样温度，控制模型生成文本的多样性。取值范围： [0, 2)
     *
     * @return
     */
    Float temperature();

    /**
     * 核采样的概率阈值，控制模型生成文本的多样性。取值范围：（0,1.0]
     *
     * @return
     */
    Double topP();

    /**
     * 生成过程中采样候选集的大小。
     *
     * @return
     */
    Integer topK();

    /**
     * 模型生成时连续序列中的重复度。提高repetition_penalty时可以降低模型生成的重复度，1.0表示不做惩罚。没有严格的取值范围，只要大于0即可。
     *
     * @return
     */
    Float repetitionPenalty();

    /**
     * 本次请求返回的最大 Token 数。
     *
     * @return
     */
    Integer maxTokens();

    /**
     * 设置seed参数会使文本生成过程更具有确定性，通常用于使模型每次运行的结果一致。
     *
     * @return
     */
    Integer seed();

    /**
     * 使用stop参数后，当模型生成的文本即将包含指定的字符串或token_id时，将自动停止生成。 用|分割
     *
     * @return
     */
    String stop();

}
