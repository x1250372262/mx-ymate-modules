var CASECAD_DATA = {

}
class SelectCascade {
    constructor() {
        this.domParam = $(".select-init");
        this.idParam = "id";
        this.valueParam = "name";
        this.isTokenParam = true;
        //请求方式
        this.methodParam = REQUEST_METHOD.GET;
        this.dataParam = []
        this.urlParam = "";
        this.stylesParam = "";
        this.parameterParam = "";
        this.returnKeyParam = "";
        this.handlerDataFunParam = null;
    }

    dom(dom) {
        this.domParam = dom;
        return this;
    }

    getDom() {
        return this.domParam;
    }

    id(id) {
        this.idParam = id;
        return this;
    }

    getId() {
        return this.idParam;
    }

    value(value) {
        this.valueParam = value;
        return this;
    }

    getValue() {
        return this.valueParam;
    }

    isToken(isToken) {
        this.isTokenParam = isToken;
        return this;
    }

    getIsToken() {
        return this.isTokenParam;
    }

    method(method) {
        this.methodParam = method;
        return this;
    }

    getMethod() {
        return this.methodParam;
    }


    data(data) {
        this.dataParam = data;
        return this;
    }

    getData() {
        return this.dataParam;
    }

    url(url) {
        this.urlParam = url;
        return this;
    }

    getUrl() {
        return this.urlParam;
    }

    styles(styles) {
        this.stylesParam = styles;
        return this;
    }

    getStyles() {
        return this.stylesParam;
    }

    parameter(parameter) {
        this.parameterParam = parameter;
        return this;
    }

    getParameter() {
        return this.parameterParam;
    }

    returnKey(returnKey) {
        this.returnKeyParam = returnKey;
        return this;
    }

    getReturnKey() {
        return this.returnKeyParam;
    }

    handlerDataFun(handlerDataFun) {
        this.handlerDataFunParam = handlerDataFun;
        return this;
    }

    getHandlerDataFun() {
        return this.handlerDataFunParam;
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
                    $.each(data, function (index, item) {
                        if (styles != null) {
                            html += "<option style='" + styles + "' value='" + item[id] + "'>" + item[value] + "</option>"
                        } else {
                            html += "<option value='" + item[id] + "'>" + item[value] + "</option>"
                        }

                    });
                    dom.html(html);
                    if(handlerDataFun!==undefined && handlerDataFun!==null){
                        handlerDataFun(data,CASECAD_DATA);
                    }
                    CASECAD_DATA = result.data;
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
        let html = "";
        if ($.trim(isDefault) && isDefault === "1") {
            if (styles != null) {
                html = "<option value='' style='" + styles + "'>" + defaultOptionText + "</option>"
            } else {
                html = "<option value=''>" + defaultOptionText + "</option>"
            }

        }
        $.each(data, function (index, item) {
            if (styles != null) {
                html += "<option style='" + styles + "' value='" + item[id] + "'>" + item[value] + "</option>"
            } else {
                html += "<option value='" + item[id] + "'>" + item[value] + "</option>"
            }

        });
        dom.html(html);
        if(handlerDataFun!==undefined && handlerDataFun!==null){
            handlerDataFun(data,CASECAD_DATA);
        }
        CASECAD_DATA = data;
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



