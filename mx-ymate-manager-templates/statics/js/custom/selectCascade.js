var CASECAD_DATA = {

}
class SelectCascade {
    constructor() {
        //第一个节点dom
        this._dom = $(".select-init");
        //option id的字段
        this._id = "id";
        //option value的字段
        this._value = "name";
        //有url的时候是否需要token
        this._isToken = true;
        //有url的时候请求方式
        this._method = REQUEST_METHOD.GET;
        //数据 和url并存 通过方法来具体调用哪个
        this._data = []
        //数据 和url并存 通过方法来具体调用哪个
        this._url = "";
        //自定义样式
        this._styles = "";
        //有url的时候请求参数
        this._parameter = "";
        //有url的时候 返回数据的变化  正常res.data  有可能 res.data.returnKey
        this._returnKey = "";
        //自定义函数
        this._handlerDataFun = null;
    }

    dom(dom) {
        this._dom = dom;
        return this;
    }

    getDom() {
        return this._dom;
    }

    id(id) {
        this._id = id;
        return this;
    }

    getId() {
        return this._id;
    }

    value(value) {
        this._value = value;
        return this;
    }

    getValue() {
        return this._value;
    }

    isToken(isToken) {
        this._isToken = isToken;
        return this;
    }

    getIsToken() {
        return this._isToken;
    }

    method(method) {
        this._method = method;
        return this;
    }

    getMethod() {
        return this._method;
    }


    data(data) {
        this._data = data;
        return this;
    }

    getData() {
        return this._data;
    }

    url(url) {
        this._url = url;
        return this;
    }

    getUrl() {
        return this._url;
    }

    styles(styles) {
        this._styles = styles;
        return this;
    }

    getStyles() {
        return this._styles;
    }

    parameter(parameter) {
        this._parameter = parameter;
        return this;
    }

    getParameter() {
        return this._parameter;
    }

    returnKey(returnKey) {
        this._returnKey = returnKey;
        return this;
    }

    getReturnKey() {
        return this._returnKey;
    }

    handlerDataFun(handlerDataFun) {
        this._handlerDataFun = handlerDataFun;
        return this;
    }

    getHandlerDataFun() {
        return this._handlerDataFun;
    }

    static builder() {
        return new SelectCascade();
    }


    initByUrl() {
        let selectThis = this;
        let dom = selectThis.getDom();
        let url = selectThis.getUrl();
        let parameter = selectThis.getParameter();
        let styles = selectThis.getStyles();
        let returnKey = selectThis.getReturnKey();
        let id = selectThis.getId();
        let value = selectThis.getValue();
        let handlerDataFun = selectThis.getHandlerDataFun();
        if (!$.trim(url)) {
            LayerUtil.failMsg("url不存在")
            return false;
        }
        if (parameter !== null) {
            url = url + parameter;
        }
        let isDefault = dom.attr("isDefault");
        let defaultOptionText = dom.attr("defaultOptionText");
        if (!$.trim(defaultOptionText)) {
            defaultOptionText = "请选择";
        }
        let dataKey = dom.attr("dataKey");
        if (!$.trim(dataKey)) {
            LayerUtil.failMsg("dataKey不存在")
            return false;
        }
        let html = "";
        if ($.trim(isDefault) && isDefault === "1") {
            if (styles != null) {
                html = "<option value='' style='" + styles + "'>" + defaultOptionText + "</option>"
            } else {
                html = "<option value=''>" + defaultOptionText + "</option>"
            }

        }
        Request.builder()
            .url(url)
            .isToken(selectThis.getIsToken())
            .method(selectThis.getMethod())
            .callback(function (result) {
                if (result.code === "00000") {
                    let data = result.data;
                    if ($.trim(returnKey)) {
                        data = result.data[returnKey];
                    }
                    let resultData = data[dataKey];
                    $.each(resultData, function (index, item) {
                        if (styles != null) {
                            html += "<option style='" + styles + "' value='" + item[id] + "'>" + item[value] + "</option>"
                        } else {
                            html += "<option value='" + item[id] + "'>" + item[value] + "</option>"
                        }

                    });
                    dom.html(html);
                    CASECAD_DATA = data;
                    if(handlerDataFun!==undefined && handlerDataFun!==null){
                        handlerDataFun(resultData,CASECAD_DATA);
                    }
                }
            }).do();
    }

    initByData() {
        let selectThis = this;
        let dom = selectThis.getDom();
        let data = selectThis.getData();
        let styles = selectThis.getStyles();
        let id = selectThis.getId();
        let value = selectThis.getValue();
        let handlerDataFun = selectThis.getHandlerDataFun();
        if (data === undefined || data === null || data.length === 0) {
            LayerUtil.failMsg("数据不能为空")
            return false;
        }
        let isDefault = dom.attr("isDefault");
        let defaultOptionText = dom.attr("defaultOptionText");
        if (!$.trim(defaultOptionText)) {
            defaultOptionText = "请选择";
        }
        let dataKey = dom.attr("dataKey");
        if (!$.trim(dataKey)) {
            LayerUtil.failMsg("dataKey不存在")
            return false;
        }
        let html = "";
        if ($.trim(isDefault) && isDefault === "1") {
            if (styles != null) {
                html = "<option value='' style='" + styles + "'>" + defaultOptionText + "</option>"
            } else {
                html = "<option value=''>" + defaultOptionText + "</option>"
            }

        }
        let resultData = data[dataKey];
        $.each(resultData, function (index, item) {
            if (styles != null) {
                html += "<option style='" + styles + "' value='" + item[id] + "'>" + item[value] + "</option>"
            } else {
                html += "<option value='" + item[id] + "'>" + item[value] + "</option>"
            }

        });
        dom.html(html);
        CASECAD_DATA = data;
        if(handlerDataFun!==undefined && handlerDataFun!==null){
            handlerDataFun(resultData,CASECAD_DATA);
        }
    }


}

$(function(){
    $(document).on("change",".select-cascade",function(){
        let cascadeId = $.trim($(this).attr("cascadeId"));
        let cascadeKey = $.trim($(this).attr("cascadeKey"));
        let cascadePid = $.trim($(this).attr("cascadePid"));
        if(!cascadePid){
            cascadePid = "pid";
        }

        let value =  $.trim($(this).val());
        if(!value || !cascadeKey || !cascadeId){
            LayerUtil.failMsg("value、cascadeKey、cascadeId都不能为空")
            return false;
        }
        let data = CASECAD_DATA[cascadeKey];
        let result = [];
        $.each(data,function(index,item){
            if(value===item[cascadePid]){
                result.push(item);
            }
        })
        Select.builder()
            .dom($("#"+cascadeId))
            .data(result).initByData();
    })
});



