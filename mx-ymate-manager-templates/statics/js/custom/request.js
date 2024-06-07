class Request {

    constructor() {
        //接口地址
        this._url = "";
        //请求参数
        this._data = {};
        //请求方式
        this._method = REQUEST_METHOD.GET;
        //是否需要token
        this._isToken = true;
        //token失败的回调方法
        this._tokenCallBack = null;
        //回调方法
        this._callback = null;
        //请求头
        this._headers = {};
        //同步异步
        this._async = true;
    }

    static builder() {
        return new Request();
    }

    url(url) {
        this._url = url;
        return this;
    }

    getUrl() {
        return this._url;
    }

    method(method) {
        this._method = method;
        return this;
    }

    getMethod() {
        return this._method;
    }

    data(data) {
        data.date = new Date().getTime();
        data.format = "json";
        this._data = data;
        return this;
    }

    getData() {
        return this._data;
    }

    isToken(isToken) {
        this._isToken = isToken;
        return this;
    }

    getIsToken() {
        return this._isToken;
    }

    tokenCallBack(tokenCallBack) {
        this._tokenCallBack = tokenCallBack;
        return this;
    }

    getTokenCallBack() {
        return this._tokenCallBack;
    }

    callback(callback) {
        this._callback = callback;
        return this;
    }

    getCallback() {
        return this._callback;
    }

    headers(headers) {
        this._headers = headers;
        return this;
    }

    getHeaders() {
        return this._headers;
    }

    async(async){
        this._async = async;
        return this;
    }

    getAsync(){
        return this._async;
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
            async: ths.getAsync(),
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
        this.method(REQUEST_METHOD.POST)
        this.do();
    }

    get() {
        this.method(REQUEST_METHOD.GET)
        this.do();
    }


}