package com.sft.urchin.station.event;

import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.mqtt.Mqtt;
import com.mx.ymate.mqtt.bean.MqttContext;
import com.mx.ymate.mqtt.enums.QosEnum;
import com.mx.ymate.mqtt.event.MqttEvent;
import com.sft.urchin.station.handler.InitHandler;
import net.ymate.platform.core.event.Events;
import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.event.IEventRegister;
import net.ymate.platform.core.event.annotation.EventRegister;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.sft.urchin.station.handler.InitHandler.TOPIC_LIST;

/**
 * @author lidong
 * @version 1.0
 * @date 2024-11-06 10:33
 */
@EventRegister
public class MqttEventRegister implements IEventRegister {

    private static final Log LOG = LogFactory.getLog(MqttEventRegister.class);

    @Override
    public void register(Events events) {
        events.registerEvent(MqttEvent.class);
        // 订阅模块事件：默认同步
        events.registerListener(Events.MODE.ASYNC, MqttEvent.class, (IEventListener<MqttEvent>) context -> {
            //连接成功
            switch (context.getEventName()) {
                case MQTT_CONNECT_SUCCESS:
                    LOG.info("mqtt连接成功");
                    //正式
                    //1.获取所有的车间 去订阅主题 初始化基本的数据
//                    try {
//                        InitHandler.initStationData();
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                    Mqtt.get().subscribe(TOPIC_LIST, QosEnum.QOS_EXACTLY_ONCE);
                    MqttContext mqttContext = (MqttContext) context.getParamExtend("mqttContext");
                    LOG.info(JSONObject.toJSONString(mqttContext));
                    Mqtt.get().getManager().get(mqttContext.getClientName()).subscribe(mqttContext.getClientName() + "/in",QosEnum.QOS_EXACTLY_ONCE);
                    break;
                case MQTT_RECONNECT_SUCCESS:
                    MqttContext mqttContext1 = (MqttContext) context.getParamExtend("mqttContext");
                    Mqtt.get().getManager().get(mqttContext1.getClientName()).subscribe(mqttContext1.getClientName() + "/in",QosEnum.QOS_EXACTLY_ONCE);
                    LOG.info("mqtt重新连接成功");
                    break;
                case MQTT_CONNECT_FAIL:
                    LOG.info("mqtt连接失败");
                    break;
                case MQTT_DISCONNECT_SUCCESS:
                    LOG.info("mqtt断开成功");
                    break;
                case MQTT_DISCONNECT_FAIL:
                    LOG.info("mqtt断开失败");
                    break;
                case MQTT_PUBLISH_SUCCESS:
                    LOG.info("mqtt发布成功");
                    break;
                case MQTT_PUBLISH_FAIL:
                    LOG.info("mqtt发布失败");
                    break;
                case MQTT_SUBSCRIBE_SUCCESS:
                    LOG.info("mqtt订阅成功");
                    break;
                case MQTT_SUBSCRIBE_FAIL:
                    LOG.info("mqtt订阅失败");
                    break;
                case MQTT_UN_SUBSCRIBE_SUCCESS:
                    LOG.info("mqtt取消订阅成功");
                    break;
                case MQTT_UN_SUBSCRIBE_FAIL:
                    LOG.info("mqtt取消订阅失败");
                    break;
                default:
                    throw new RuntimeException("不支持的事件类型");
            }
            return false;
        });
    }
}
