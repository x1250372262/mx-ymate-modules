
class Request {

    constructor() {
        //接口地址
        this.urlParam = "";
        //请求参数
        this.dataParam = {};
        //请求方式
        this.methodParam = REQUEST_METHOD.GET;
        //是否需要token
        this.isTokenParam = true;
        //token失败的回调方法
        this.tokenCallBackParam = null;
        //回调方法
        this.callbackParam = null;
        //请求头
        this.headersParam = {};
    }

    static builder() {
        return new Request();
    }

    url(url) {
        this.urlParam = url;
        return this;
    }

    getUrl(){
        return this.urlParam;
    }

    method(method) {
        this.methodParam = method;
        return this;
    }

    getMethod(){
        return this.methodParam;
    }

    data(data) {
        data.date = new Date().getTime();
        data.format = "json";
        this.dataParam = data;
        return this;
    }

    getData(){
        return this.dataParam;
    }

    isToken(isToken) {
        this.isTokenParam = isToken;
        return this;
    }

    getIsToken(){
        return this.isTokenParam;
    }

    tokenCallBack(tokenCallBack) {
        this.tokenCallBackParam = tokenCallBack;
        return this;
    }

    getTokenCallBack(){
        return this.tokenCallBackParam;
    }

    callback(callback) {
        this.callbackParam = callback;
        return this;
    }

    getCallback(){
        return this.callbackParam;
    }

    headers(headers) {
        this.headersParam = headers;
        return this;
    }

    getHeaders(){
        return this.headersParam;
    }

    do() {
        let headers = this.getHeaders();
        headers["X-Requested-With"] = "XMLHttpRequest"
        if (this.getIsToken()) {
            if (!Token.expires()) {
                let tokenCallBack = this.getTokenCallBack();
                if (tokenCallBack != null) {
                    tokenCallBack();
                } else {
                    LayerUtil.failMsg(ERROR_MSG.NO_LOGIN)
                    setTimeout(function () {
                        window.location.href = LOGIN_VIEW;
                    }, 1000)
                    return false;
                }
            } else {
                headers[HEADER_PARAM_NAME] = Token.get();
            }
        }
        var ths = this;
        $.ajax({
            type: ths.getMethod(),
            data: ths.getData(),
            async: false,
            headers: headers,
            url: this.getUrl(),
            dataType: "json",
            success: function (d, status, xhr) {
                if (d.code === "M0007") {
                    $.each(d.data, function (item) {
                        LayerUtil.failMsg(d.data[item] != null ? d.data[item] : ERROR_MSG.INVALID_PARAMETERS)
                        return false;
                    });
                    $(".mx_validator_button").attr("disabled", false)
                } else if (d.code === "M0004") {
                    LayerUtil.failMsg(ERROR_MSG.NO_LOGIN)
                    setTimeout(function () {
                        window.location.href = LOGIN_VIEW;
                    }, 1000)
                } else if (d.code === "M0012") {
                    LayerUtil.failMsg(d.msg)
                } else {
                    let callBack = ths.getCallback();
                    if (callBack != null) {
                        callBack(d);
                    }
                }
            }, error: function (xhr, textStatus, errorThrown) {
                LayerUtil.failMsg(xhr.statusText + ":" + xhr.status)
                $(".mx_validator_button").attr("disabled", false)
            }
        });
    }

    post() {
        this.methodParam = REQUEST_METHOD.POST;
        this.do();
    }

    get() {
        this.methodParam = REQUEST_METHOD.GET;
        this.do();
    }


}