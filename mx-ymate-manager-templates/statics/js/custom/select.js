var SELECT = function () {
    var init = function (dom, id, value, url, styles, params, returnKey) {
        dom.find(".select-init").each(function () {
            var that = $(this);
            if (!url) {
                url = that.attr("listurl");
            }
            if (params !== null) {
                url = url + params;
            }
            var isDefault = that.attr("isDefault");
            var defaultOptionText = that.attr("defaultOptionText");
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
            if ($.trim(url)) {
                var token = Token.get();
                var tokenFlag = false;
                if (token !== undefined && token !== null && token !== "") {
                    tokenFlag = true;
                }
                MX.axget(url, null, tokenFlag, null, function (result) {
                    if (result.code === "00000") {
                        var data = result.data;
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
                        that.html(html);
                    }
                })
            }
        });
    }

    var initById = function (domId, id, value, url, styles, params, returnKey) {
        var that = $("#"+domId);
        if (!url) {
            url = that.attr("listurl");
        }
        if (params !== null) {
            url = url + params;
        }
        var isDefault = that.attr("isDefault");
        var defaultOptionText = that.attr("defaultOptionText");
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
        if ($.trim(url)) {
            var token = Token.get();
            var tokenFlag = false;
            if (token !== undefined && token !== null && token !== "") {
                tokenFlag = true;
            }
            MX.axget(url, null, tokenFlag, null, function (result) {
                if (result.code === "00000") {
                    var data = result.data;
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
                    that.html(html);
                }
            })
        }
    }

    var initByDataAndId = function (domId, id, value, data, styles) {
        var that = $("#"+domId);
        if (!data) {
            MX.failMsg("data不能为空");
            return false;
        }
        var isDefault = that.attr("isDefault");
        var defaultOptionText = that.attr("defaultOptionText");
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
            if (styles != null) {
                html += "<option style='" + styles + "' value='" + item[id] + "'>" + item[value] + "</option>"
            } else {
                html += "<option value='" + item[id] + "'>" + item[value] + "</option>"
            }

        });
        that.html(html);
    }

    var initCascade = function (domId, id, value, url, styles, params, returnKey,handlerDataFun) {
        var that = $("#"+domId);
        if (!url) {
            url = that.attr("listurl");
        }
        if (params !== null) {
            url = url + params;
        }
        var isDefault = that.attr("isDefault");
        var defaultOptionText = that.attr("defaultOptionText");
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
        if ($.trim(url)) {
            var token = Token.get();
            var tokenFlag = false;
            if (token !== undefined && token !== null && token !== "") {
                tokenFlag = true;
            }
            MX.axget(url, null, tokenFlag, null, function (result) {
                if (result.code === "00000") {
                    var data = result.data;
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
                    that.html(html);
                    if(handlerDataFun!==undefined && handlerDataFun!==null){
                        handlerDataFun(data,CASECAD_DATA);
                    }
                    CASECAD_DATA = result.data;

                }
            })
        }
    }


    var initGroup = function (dom, id, value, url, styles, params, returnKey, childList, groupName) {
        dom.find(".select-init-group").each(function () {
            var that = $(this);
            if (!url) {
                url = that.attr("listurl");
            }
            if (params !== null) {
                url = url + params;
            }
            var isDefault = that.attr("isDefault");
            var defaultOptionText = that.attr("defaultOptionText");
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

            if ($.trim(url)) {
                var token = Token.get();
                var tokenFlag = false;
                if (token !== undefined && token !== null && token !== "") {
                    tokenFlag = true;
                }
                MX.axget(url, null, tokenFlag, null, function (result) {
                    if (result.code === "00000") {
                        var data = result.data;
                        if ($.trim(returnKey)) {
                            data = result.data[returnKey];
                        }
                        $.each(data, function (index, item) {
                            $.each(result.data, function (index, item) {
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

                        });
                        that.html(html);
                    }
                })

            }
        });
    }


    return {
        init: function (dom, id, value, url, styles, params, returnKey) {
            id = $.trim(id) ? id : "id";
            value = $.trim(value) ? value : "name";
            styles = $.trim(styles) ? styles : null;
            params = $.trim(params) ? params : null;
            init(dom, id, value, $.trim(url), styles, params, returnKey);
        },
        initById: function (domId, id, value, url, styles, params, returnKey) {
            id = $.trim(id) ? id : "id";
            value = $.trim(value) ? value : "name";
            styles = $.trim(styles) ? styles : null;
            params = $.trim(params) ? params : null;
            initById(domId, id, value, $.trim(url), styles, params, returnKey);
        },
        initByDataAndId:function(domId, id, value, data, styles){
            id = $.trim(id) ? id : "id";
            value = $.trim(value) ? value : "name";
            styles = $.trim(styles) ? styles : null;
            initByDataAndId(domId, id, value, data, styles);
        },
        initGroup: function (dom, id, value, styles, url, params, returnKey, childList, groupName) {
            id = $.trim(id) ? id : "id";
            value = $.trim(value) ? value : "name";
            styles = $.trim(styles) ? styles : null;
            params = $.trim(params) ? params : null;
            childList = (childList != null !== undefined && childList != null) ? childList : "childList";
            groupName = $.trim(groupName) ? value : "name";
            initGroup(dom, id, value, $.trim(url), styles, params, returnKey, childList, groupName);
        },
        initCascade:function(domId, id, value, url, styles, params, returnKey,handlerDataFun){
            id = $.trim(id) ? id : "id";
            value = $.trim(value) ? value : "name";
            styles = $.trim(styles) ? styles : null;
            params = $.trim(params) ? params : null;
            initCascade(domId, id, value, $.trim(url), styles, params, returnKey,handlerDataFun);
        },
        selectData:function(cascadeKey,cascadeId,cascadePid,value){
            var data = CASECAD_DATA[cascadeKey];
            var result = [];
            if(!cascadePid){
                cascadePid = "pid";
            }
            $.each(data,function(index,item){
                if(value===item[cascadePid]){
                    result.push(item);
                }
            })
            SELECT.initByDataAndId(cascadeId,"id","name",result,null);
        }
    };

}();

//key value
var CASECAD_DATA = {

}

$(function(){
    $(document).on("change",".select-cascade",function(){
        var cascadeId = $.trim($(this).attr("cascadeId"));
        var cascadeKey = $.trim($(this).attr("cascadeKey"));
        var cascadePid = $.trim($(this).attr("cascadePid"));
        if(!cascadePid){
            cascadePid = "pid";
        }

        var value =  $.trim($(this).val());
        if(!value || !cascadeKey || !cascadeId){
            MX.failMsg("value、cascadeKey、cascadeId都不能为空")
            return false;
        }
        var data = CASECAD_DATA[cascadeKey];
        var result = [];
        $.each(data,function(index,item){
            if(value===item[cascadePid]){
                result.push(item);
            }
        })
        SELECT.initByDataAndId(cascadeId,"id","name",result,null);

    })
});




