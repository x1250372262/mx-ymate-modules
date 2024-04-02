//连接 发送  接收数据  关闭方法  设置url

//websocket web = new webco


var connection;

var WS = function () {
    return {
        connect: function (url,successFunction) {
            if (window.WebSocket) {
                //如果支持websocket
                connection = new WebSocket(url);
                connection.onopen = function(event) {
                    console.log(connection.readyState)
                    if (connection.readyState === WebSocket.OPEN) {
                        console.log('WebSocket连接成功');
                        if (typeof successFunction === 'function') {
                            successFunction(event);
                        }
                    }
                };
            }else {
                //否则报错
                alert("浏览器不支持websocket");
            }
        },
        send:function(message){
            connection.send(message);
        },
        onData:function(onDataFunction){
            connection.onmessage = function (evt) {
                onDataFunction(evt)
            };
        },
        onClose:function () {
            connection.close();
            console.log("websocket已断开");
        }
    };

}();

