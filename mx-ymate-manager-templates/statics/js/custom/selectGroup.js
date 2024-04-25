class SelectGroup {
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
        this.childListParam = "";
        this.groupNameParam = "";
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

    childList(childList) {
        this.childListParam = childList;
        return this;
    }

    getChildList() {
        return this.childListParam;
    }

    groupName(groupName) {
        this.groupNameParam = groupName;
        return this;
    }

    getGroupName() {
        return this.groupNameParam;
    }

    static builder() {
        return new SelectGroup();
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
        let childList = selectThis.getChildList();
        let groupName = selectThis.getGroupName();
        if (!$.trim(url)) {
            LayerUtil.failMsg("url不存在")
            return false;
        }
        if (parameter !== null) {
            url = url + parameter;
        }
        var isDefault = dom.attr("isDefault");
        var defaultOptionText = dom.attr("defaultOptionText");
        if (!$.trim(defaultOptionText)) {
            defaultOptionText = "请选择";
        }
        var html = "";
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
                    var data = result.data;
                    if ($.trim(returnKey)) {
                        data = result.data[returnKey];
                    }
                    $.each(data, function (index, item) {
                        html += "<optgroup label='" + item[groupName] + "'>";
                        $.each(item[childList], function (index1, item1) {
                            if (styles != null) {
                                html += "<option style='" + styles + "' value='" + item1[id] + "'>" + item1[value] + "</option>"
                            } else {
                                html += "<option value='" + item1[id] + "'>" + item1[value] + "</option>"
                            }
                        });
                        html += "</optgroup>";

                    });
                    dom.html(html);
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
        let childList = selectThis.getChildList();
        let groupName = selectThis.getGroupName();
        if (data === undefined || data === null || data.length === 0) {
            LayerUtil.failMsg("数据不能为空")
            return false;
        }
        var isDefault = dom.attr("isDefault");
        var defaultOptionText = dom.attr("defaultOptionText");
        if (!$.trim(defaultOptionText)) {
            defaultOptionText = "请选择";
        }
        var html = "";
        if ($.trim(isDefault) && isDefault === "1") {
            if (styles != null) {
                html = "<option value='' style='" + styles + "'>" + defaultOptionText + "</option>"
            } else {
                html = "<option value=''>" + defaultOptionText + "</option>"
            }

        }
        $.each(data, function (index, item) {
            html += "<optgroup label='" + item[groupName] + "'>";
            $.each(item[childList], function (index1, item1) {
                if (styles != null) {
                    html += "<option style='" + styles + "' value='" + item1[id] + "'>" + item1[value] + "</option>"
                } else {
                    html += "<option value='" + item1[id] + "'>" + item1[value] + "</option>"
                }
            });
            html += "</optgroup>";

        });
        dom.html(html);
    }


}




