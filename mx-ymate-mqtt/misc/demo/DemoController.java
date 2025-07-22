package com.sft.urchin.station.demo;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.mqtt.Mqtt;
import com.mx.ymate.mqtt.bean.MqttConfig;
import com.mx.ymate.mqtt.enums.QosEnum;
import com.sft.urchin.station.event.UrchinMqttCallback;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.annotation.RequestParam;
import net.ymate.platform.webmvc.view.IView;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: xujianpeng.
 * @Date 2025/7/21.
 * @Time: 15:03.
 * @Description:
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/send")
    public IView send(@RequestParam String name,
                      @RequestParam String msg) throws Exception {
        String topic = "ceshi".equals(name) ? "default/in":"ceshi/in";
        Mqtt.get().getManager().get(name).publish(topic, msg, QosEnum.QOS_EXACTLY_ONCE);
        return MxResult.ok().toJsonView();
    }

    @RequestMapping("/connect")
    public IView connect(@RequestParam String name) throws Exception {
        Mqtt.get().getManager().get(name).connect();
        return MxResult.ok().toJsonView();
    }

    @RequestMapping("/dsconnect")
    public IView dsconnect(@RequestParam String name) throws Exception {
        Mqtt.get().getManager().get(name).disconnect();
        return MxResult.ok().toJsonView();
    }




    @RequestMapping("/add")
    public IView add(@RequestParam String name) throws Exception {
        MqttConfig mqttConfig = new MqttConfig();
        mqttConfig.setName(name);
        mqttConfig.setUrl("tcp://127.0.0.1:1883");
        mqttConfig.setClientId("test-3");
        mqttConfig.setUserName("sft");
        mqttConfig.setPassword("aaaaaa123");
        mqttConfig.setCallback(new UrchinMqttCallback());
        mqttConfig.setCleanSession(false);
        mqttConfig.setManualAcks(false);
        mqttConfig.setConnectionTimeout(30);
        mqttConfig.setKeepAliveInterval(60);
        mqttConfig.setMaxInflight(100);
        mqttConfig.setVersion("3.1.3");
        mqttConfig.setAutomaticReconnection(true);
        mqttConfig.setReconnectDelay(0);
        mqttConfig.setStorageDir("");
        mqttConfig.setSslEnabled(false);
        mqttConfig.setSslTruststore("");
        mqttConfig.setSslTruststorePassword("");
        mqttConfig.setSslTruststoreType("");
        mqttConfig.setSslKeystore("");
        mqttConfig.setSslKeystorePassword("");
        mqttConfig.setSslKeystoreType("");
        mqttConfig.setSslProtocol("");
        mqttConfig.setWillEnabled(false);
        mqttConfig.setWillTopic("");
        mqttConfig.setWillPayload("");
        mqttConfig.setWillQos(0);
        mqttConfig.setWillRetained(false);
        MxResult result = Mqtt.get().getManager().add(mqttConfig);
        return result.toJsonView();
    }


    @RequestMapping("/remove")
    public IView remove(@RequestParam String name,
                        @RequestParam String name1) throws Exception {
        Mqtt.get().getManager().remove(name);
        if(StringUtils.isNotBlank(name1)){
            MqttConfig mqttConfig = new MqttConfig();
            mqttConfig.setName(name1);
            mqttConfig.setUrl("tcp://127.0.0.1:1883");
            mqttConfig.setClientId("test-3");
            mqttConfig.setUserName("sft");
            mqttConfig.setPassword("aaaaaa123");
            mqttConfig.setCallback(new UrchinMqttCallback());
            mqttConfig.setCleanSession(false);
            mqttConfig.setManualAcks(false);
            mqttConfig.setConnectionTimeout(30);
            mqttConfig.setKeepAliveInterval(60);
            mqttConfig.setMaxInflight(100);
            mqttConfig.setVersion("3.1.3");
            mqttConfig.setAutomaticReconnection(true);
            mqttConfig.setReconnectDelay(0);
            mqttConfig.setStorageDir("");
            mqttConfig.setSslEnabled(false);
            mqttConfig.setSslTruststore("");
            mqttConfig.setSslTruststorePassword("");
            mqttConfig.setSslTruststoreType("");
            mqttConfig.setSslKeystore("");
            mqttConfig.setSslKeystorePassword("");
            mqttConfig.setSslKeystoreType("");
            mqttConfig.setSslProtocol("");
            mqttConfig.setWillEnabled(false);
            mqttConfig.setWillTopic("");
            mqttConfig.setWillPayload("");
            mqttConfig.setWillQos(0);
            mqttConfig.setWillRetained(false);
            Mqtt.get().getManager().add(mqttConfig);
        }
        return MxResult.ok().toJsonView();
    }
}
