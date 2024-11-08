class Mqtt {
    constructor() {
        //连接地址
        this._url = null;
        //客户端 不填默认生成
        this._clientId = "mxMqttClientId-" + Math.random().toString(16).substring(2, 8);
        // 用户名
        this._userName = null;
        //密码
        this._password = null;
        // 是否清除会话
        this._cleanSession = true;
        // 心跳间隔，单位秒
        this._keepalive = 60;
        // 自动重连间隔
        this._reconnectPeriod = 1000;
        // 连接超时时间
        this._connectTimeout = 30 * 1000;
        // 遗嘱主题
        this._willTopic = null;
        // 遗嘱消息
        this._willMessage = null;
        // 遗嘱 QoS
        this._willQos = Mqtt.Qos.AT_MOST_ONCE;
        // 遗嘱消息是否保留
        this._willRetain = false;
        //连接成功函数
        this._onConnectSuccess = null;
        //连接失败函数
        this._onConnectFailure = null;
        //消息到达函数
        this._onMessageArrived = null;
        //连接丢失函数
        this._onConnectionLost = null;
        //客户端
        this._mqttClient = null;
    }

    url(url) {
        this._url = url;
        return this;
    }

    getUrl() {
        return this._url;
    }


    clientId(clientId) {
        this._clientId = clientId;
        return this;
    }

    getClientId() {
        return this._clientId;
    }

    userName(userName) {
        this._userName = userName;
        return this;
    }

    getUserName() {
        return this._userName;
    }

    password(password) {
        this._password = password;
        return this;
    }

    getPassword() {
        return this._password;
    }

    cleanSession(cleanSession) {
        this._cleanSession = cleanSession;
        return this;
    }

    getCleanSession() {
        return this._cleanSession;
    }

    keepalive(keepalive) {
        this._keepalive = keepalive;
        return this;
    }

    getKeepalive() {
        return this._keepalive;
    }

    reconnectPeriod(reconnectPeriod) {
        this._reconnectPeriod = reconnectPeriod;
        return this;
    }

    getReconnectPeriod() {
        return this._reconnectPeriod;
    }

    connectTimeout(connectTimeout) {
        this._connectTimeout = connectTimeout;
        return this;
    }

    getConnectTimeout() {
        return this._connectTimeout;
    }

    willTopic(willTopic) {
        this._willTopic = willTopic;
        return this;
    }

    getWillTopic() {
        return this._willTopic;
    }

    willMessage(willMessage) {
        this._willMessage = willMessage;
        return this;
    }

    getWillMessage() {
        return this._willMessage;
    }

    willQoS(willQos) {
        this._willQos = willQos;
        return this;
    }

    getWillQos() {
        return this._willQos;
    }

    willRetain(willRetain) {
        this._willRetain = willRetain;
        return this;
    }

    getWillRetain() {
        return this._willRetain;
    }

    onConnectSuccess(onConnectSuccess) {
        this._onConnectSuccess = onConnectSuccess;
        return this;
    }

    getOnConnectSuccess() {
        return this._onConnectSuccess;
    }

    onConnectFailure(onConnectFailure) {
        this._onConnectFailure = onConnectFailure;
        return this;
    }

    getOnConnectFailure() {
        return this._onConnectFailure;
    }

    onMessageArrived(onMessageArrived) {
        this._onMessageArrived = onMessageArrived;
        return this;
    }

    getOnMessageArrived() {
        return this._onMessageArrived;
    }

    onConnectionLost(onConnectionLost) {
        this._onConnectionLost = onConnectionLost;
        return this;
    }

    getOnConnectionLost() {
        return this._onConnectionLost;
    }


    static builder() {
        return new Mqtt();
    }

    static Qos = {
        AT_MOST_ONCE: 0,
        AT_LEAST_ONCE: 1,
        EXACTLY_ONCE: 2,
    };

    connect() {
        let mqttThis = this;
        let url = mqttThis.getUrl();
        let clientId = mqttThis.getClientId();
        let onConnectionLost = mqttThis.getOnConnectionLost();
        let onMessageArrived = mqttThis.getOnMessageArrived();
        let onConnectSuccess = mqttThis.getOnConnectSuccess();
        let onConnectFailure = mqttThis.getOnConnectFailure();
        if (!$.trim(url)) {
            LayerUtil.failMsg("url不存在")
            return false;
        }
        if (!$.trim(clientId)) {
            LayerUtil.failMsg("客户端id不能为空")
            return false;
        }
        const options = {
            clientId: clientId,
            username: mqttThis.getUserName() || undefined,
            password: mqttThis.getPassword() || undefined,
            clean: mqttThis.getCleanSession(),
            keepalive: mqttThis.getKeepalive(),
            reconnectPeriod: mqttThis.getReconnectPeriod(),
            connectTimeout: mqttThis.getConnectTimeout(),
            will: mqttThis.getWillTopic()
                ? {
                    topic: mqttThis.getWillTopic(),
                    payload: mqttThis.getWillMessage(),
                    qos: mqttThis.getWillQos(),
                    retain: mqttThis.getWillRetain()
                }
                : undefined
        };

        mqttThis._mqttClient = mqtt.connect(url, options)
        // 事件监听
        mqttThis._mqttClient.on('connect', () => {
            if (onConnectSuccess !==undefined && onConnectSuccess !==null && onConnectSuccess instanceof Function) {
                onConnectSuccess();
            }else{
                console.log('mqtt连接成功');
            }
        });

        mqttThis._mqttClient.on('error', (error) => {
            if (onConnectFailure !==undefined && onConnectFailure !==null && onConnectFailure instanceof Function) {
                onConnectFailure(error);
            }else{
                console.error('连接失败:', error);
            }
        });

        mqttThis._mqttClient.on('message', (topic, message) => {
            if (onMessageArrived !==undefined && onMessageArrived !==null && onMessageArrived instanceof Function) {
                onMessageArrived(message);
            }else{
                console.info('消息到达:', message.toString());
            }

        });

        mqttThis._mqttClient.on('close', () => {
            if (onConnectionLost !==undefined && onConnectionLost !==null && onConnectionLost instanceof Function) {
                onConnectionLost();
            }else{
                console.warn('连接丢失');
            }
        });
        return mqttThis;
    }

    // 断开连接
    disconnect() {
        if (this._mqttClient) {
            this._mqttClient.end();
        }
    }

    //订阅
    subscribe(topic, qos = Mqtt.Qos.EXACTLY_ONCE) {
        if (this._mqttClient && topic) {
            this._mqttClient.subscribe(topic, {qos});
        }
    }

    //取消订阅
    unsubscribe(topic) {
        if (this._mqttClient && topic) {
            this._mqttClient.unsubscribe(topic);
        }
    }

    //发布
    publish(topic, message, qos = Mqtt.Qos.EXACTLY_ONCE, retained = false) {
        if (this._mqttClient && topic && message) {
            this._mqttClient.publish(topic, message, { qos, retain: retained });
        }
    }

    //批量订阅
    batchSubscribe(topics, qos = Mqtt.Qos.EXACTLY_ONCE) {
        if (this._mqttClient && Array.isArray(topics)) {
            topics.forEach(topic => {
                this._mqttClient.subscribe(topic, qos);
            });
        }
    }

    //批量取消
    batchUnsubscribe(topics) {
        if (this._mqttClient && Array.isArray(topics)) {
            topics.forEach(topic => {
                this._mqttClient.unsubscribe(topic);
            });
        }
    }

    //批量发布
    batchPublish(topics,message, qos = Mqtt.Qos.EXACTLY_ONCE, retained = false) {
        if (this._mqttClient && Array.isArray(topics)) {
            topics.forEach(topic=> {
                this._mqttClient.publish(topic, message, { qos, retain: retained });
            });
        }
    }


}




