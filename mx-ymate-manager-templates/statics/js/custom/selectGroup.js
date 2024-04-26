class SelectGroup {
    constructor() {
        this._dom = $(".select-init");
        this._id = "id";
        this._value = "name";
        this._isToken = true;
        //请求方式
        this._method = REQUEST_METHOD.GET;
        this._data = []
        this._url = "";
        this._styles = "";
        this._parameter = "";
        this._returnKey = "";
        this._childList = "";
        this._groupName = "";
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

    childList(childList) {
        this._childList = childList;
        return this;
    }

    getChildList() {
        return this._childList;
    }

    groupName(groupName) {
        this._groupName = groupName;
        return this;
    }

    getGroupName() {
        return this._groupName;
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




