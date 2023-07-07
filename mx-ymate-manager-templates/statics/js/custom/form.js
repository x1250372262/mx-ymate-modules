var FORM = function () {
    return {
        //cond={lableTile:[{"key","value"},{"key","value"}]}
        numberTo: function (dom, data, cond) {
            dom.find(".numberTo").each(function () {
                var ths = $(this);
                var lableTitle = $.trim(ths.attr("lableTitle"));
                console.log(lableTitle);
                if (lableTitle) {
                    var dataD = data[ths.attr("name")];
                    var condArray = cond[lableTitle];
                    if (condArray != null && condArray.length > 0) {
                        var isValue = cond.isValue != null ? cond.isValue : true;
                        $.each(condArray, function (index, item) {
                            if (dataD === item.key) {
                                if (isValue) {
                                    ths.val(item.text);
                                } else {
                                    ths.html(item.text);
                                }
                            }
                        });
                    }
                }
            });
        },
        setHtml: function (dom, data, callback) {
            if (arguments.length === 1) {
                dom = $("#form_sample_1");
            }
            dom.find(".mx_html").each(function () {//
                if ($(this).attr("class") && $(this).attr("class").indexOf("amount") > 0) {
                    $(this).html(MX.getPrice(data[$(this).attr("name")], 100));
                } else if ($(this).attr("class") && $(this).attr("class").indexOf("times") > 0) {
                    $(this).html(MX.changeTimeToString(new Date(data[$(this).attr("name")])));
                } else if ($(this).attr("class") && $(this).attr("class").indexOf("dates") > 0) {
                    $(this).html(MX.changeDateToString(new Date(data[$(this).attr("name")])));
                } else if ($(this).attr("class") && $(this).attr("class").indexOf("numberCast") > 0) {
                    var ths = $(this);
                    var cond = $.trim(ths.attr("cond"));
                    var name = $.trim(ths.attr("name"));
                    if (cond) {
                        var condArray = cond.split("mx_c");
                        if (condArray != null && condArray.length > 0) {
                            $.each(condArray, function (index, item) {
                                var condItemArray = item.split(":");
                                if (condItemArray != null && condArray.length > 0) {
                                    if (condItemArray[0].toString() === data[name].toString()) {
                                        ths.html(condItemArray[1]);
                                        if (condItemArray.length > 2) {
                                            ths.addClass(condItemArray[2])
                                        }
                                    }
                                }
                            });
                        }
                    }
                } else {
                    $(this).html(data[$(this).attr("name")]);
                }
            });
            if (callback !== undefined) {
                callback(dom, data);
            }
        },
        setValues: function (dom, data, callback) {
            if (arguments.length === 1) {
                dom = $("#form_sample_1");
            }
            dom.find(".thumbnail img").each(function () {
                $(this).attr("src", data[$(this).attr("name")] ? data[$(this).attr("name")] : "/statics/images/no_image.gif");
            });
            dom.find("input[type='text'],input[type='hidden'],input[type='number'],input[type='password'],textarea").each(function () {//普通input赋值
                if (!$(this).hasClass("no_clear")) {
                    if ($(this).attr("class") && $(this).attr("class").indexOf("amount") > 0) {
                        $(this).val(MX.getPrice(data[$(this).attr("name")], 100));
                    } else if ($(this).attr("class") && $(this).attr("class").indexOf("times") > 0) {
                        $(this).val(MX.changeTimeToString(new Date(data[$(this).attr("name")])));
                    } else if ($(this).attr("class") && $(this).attr("class").indexOf("dates") > 0) {
                        $(this).val(MX.changeDateToString(new Date(data[$(this).attr("name")])));
                    } else if ($(this).attr("class") && $(this).attr("class").indexOf("numberCast") > 0) {
                        var ths = $(this);
                        var cond = $.trim(ths.attr("cond"));
                        var name = $.trim(ths.attr("name"));
                        if (cond) {
                            var condArray = cond.split("mx_c");
                            if (condArray != null && condArray.length > 0) {
                                $.each(condArray, function (index, item) {
                                    var condItemArray = item.split(":");
                                    if (condItemArray != null && condArray.length > 0) {
                                        if (condItemArray[0].toString() === data[name].toString()) {
                                            ths.val(condItemArray[1]);
                                            if (condItemArray.length > 2) {
                                                ths.addClass(condItemArray[2])
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    } else if ($(this).attr("class") && $(this).attr("class").indexOf("selectCast") > 0) {
                        var ths = $(this);
                        var dataUrl = $.trim(ths.attr("dataUrl"));
                        var selectId = $.trim(ths.attr("selectId"));
                        var selectName = $.trim(ths.attr("selectName"));
                        if (!selectId) {
                            selectId = "id";
                        }
                        if (!selectName) {
                            selectName = "name";
                        }
                        var name = $.trim(ths.attr("name"));
                        if (dataUrl) {
                            MX.axget(dataUrl, null, false, null, function (e) {
                                $.each(e.data, function (index, item) {
                                    if (item[selectId].toString() === data[name].toString()) {
                                        ths.val(item[selectName]);
                                    }
                                });
                            });
                        }
                    } else {
                        $(this).val(data[$(this).attr("name")]);
                    }
                }
            });
            //
            if(dom.find("#rowTemplate").length > 0){
                var dataKey = $.trim($("#rowTemplate").attr("dataKey"))?$("#rowTemplate").attr("dataKey"):"dataKey";
                var datakeyValue = data[dataKey];
                MMP.setValues(".rowValueDom",datakeyValue);
            }

            //radio 按钮
            dom.find("input[type='radio']").each(function () {
                if (data[$(this).attr("name")] === parseInt($(this).val())) {
                    $(this).prop('checked', true)
                }
            });
            //文件
            dom.find(".mx_fileName").each(function () {
                if ($(this).attr("class") && $(this).attr("class").indexOf("numberCast") > 0) {
                    var ths = $(this);
                    var cond = $.trim(ths.attr("cond"));
                    var name = $.trim(ths.attr("name"));
                    if (cond) {
                        var condArray = cond.split("mx_c");
                        if (condArray != null && condArray.length > 0) {
                            $.each(condArray, function (index, item) {
                                var condItemArray = item.split(":");
                                if (condItemArray != null && condArray.length > 0) {
                                    if (condItemArray[0].toString() === data[name].toString()) {
                                        ths.html(condItemArray[1]);
                                    }
                                }
                            });
                        }
                    }
                } else if ($(this).attr("class") && $(this).attr("class").indexOf("selectCast") > 0) {
                    var ths = $(this);
                    var dataUrl = $.trim(ths.attr("dataUrl"));
                    var selectId = $.trim(ths.attr("selectId"));
                    var selectName = $.trim(ths.attr("selectName"));
                    var name = $.trim(ths.attr("name"));
                    if (dataUrl) {
                        MX.axget(dataUrl, null, false, null, function (e) {
                            $.each(e.data, function (index, item) {
                                if (item[selectId].toString() === data[name].toString()) {
                                    ths.html(item[selectName]);
                                }
                            });
                        });
                    }
                } else {
                    if (data["mx_file_name"]) {
                        $(this).html(data["mx_file_name"])
                    }
                }

            });
            dom.find("select").each(function () {
                var dataValue = data[$(this).attr("name")];
                $(this).find("option[value='" + dataValue + "']").prop('selected', true)
                if($(this).hasClass("select-cascade")){
                    var cascadeId = $.trim($(this).attr("cascadeId"));
                    var cascadeKey = $.trim($(this).attr("cascadeKey"));
                    var cascadePid = $.trim($(this).attr("cascadePid"));
                    if(!dataValue || !cascadeKey || !cascadeId){
                        MX.failMsg("value、cascadeKey、cascadeId都不能为空")
                        return false;
                    }
                    SELECT.selectData(cascadeKey,cascadeId,cascadePid,dataValue)
                }

            });
            //wang编辑器 重置内部html内容
            if (dom.find(".wangContent").length > 0) {
                dom.find(".wangContent").each(function () {
                    editors[$(this).attr("id")].setHtml(data[$(this).attr("name")] != null && data[$(this).attr("name")] ? data[$(this).attr("name")] : "")
                });
            }

            $(dom).find(".js-tags-input").each(function () {
                $(this).importTags('');
                var value = data[$(this).attr("name")]
                $(this).tagsInput({
                    height: '36px',
                    width: '100%',
                    defaultText: '',
                    removeWithBackspace: true,
                    delimiter: [',']
                });
                $(this).importTags(value);
            });

            dom.find(".lyear-switch").each(function () {
                var inputChecked = $(this).children("input[type=checkbox]");
                if (data[inputChecked.attr("name")] === 1) {
                    inputChecked.prop('checked', true)
                } else {
                    inputChecked.prop('checked', false)
                }
            });

            if (callback !== undefined) {
                callback(dom, data);
            }
        },
        clearValues: function (dom, callback) {
            $(dom).find('[name]').each(function () {
                if (!$(this).hasClass("no_clear")) {
                    $(this).val('');
                }
            });
            $(dom).find("img").each(function () {
                $(this).attr("src", "/statics/images/no_image.gif");
            });
            //radio 按钮
            $(dom).find("input[type='radio']").each(function () {
                if ("0" === $(this).val()) {
                    $(this).prop('checked', true)
                }
            });

            $(dom).find(".mx_fileName").each(function () {
                $(this).html("")
            });
            $(dom).find(".fileresult").val("");

            //wang编辑器 重置内部html内容
            if ($(dom).find(".wangContent").length > 0) {
                $(dom).find(".wangContent").each(function () {
                    // editors[$(this).attr("id")].dangerouslyInsertHtml("")
                    editors[$(this).attr("id")].setHtml('<p></p>')
                });
            }

            $(dom).find(".js-tags-input").each(function () {
                $(this).importTags('');
                $(this).tagsInput({
                    height: '36px',
                    width: '100%',
                    defaultText: '添加值',
                    removeWithBackspace: true,
                    delimiter: [',']
                });
            });

            $(dom).find("input[type='file']").each(function () {
                $(this).val("")
                $(this).next().val("")
                $(this).parent().next().html("")
            });

            $(dom).find(".lyear-switch").each(function () {
                var inputChecked = $(this).children("input[type=checkbox]");
                inputChecked.prop('checked', false)
            });

            // //删除input下方的红字提醒
            if ($(dom).find(".has-error").length > 0) {
                $(dom).find(".has-error").each(function () {
                    $(this).removeClass("has-error")
                })
            }
            if ($(dom).find(".help-block").length > 0) {
                $(dom).find(".help-block").each(function () {
                    $(this).remove()
                })
            }

            if (callback !== undefined) {
                callback(dom, data);
            }

        },
        getValues: function (dom) {
            if (arguments.length === 0) {
                dom = $("#form_sample_1");
            }
            var data = {};
            dom.find("input[type='text'],input[type='hidden'],input[type='number'],input[type='password'],textarea").each(function () {
                if ($(this).attr("type") === "password") {
                    data[$(this).attr("name")] = hex_md5($(this).val());
                } else {
                    data[$(this).attr("name")] = $(this).val();
                }
            });
            //
            if(dom.find("#rowTemplate").length > 0){
                var dataKey = $.trim($("#rowTemplate").attr("dataKey"))?$("#rowTemplate").attr("dataKey"):"dataKey";
                data[dataKey] = MMP.getValues(".rowValueDom");
            }

            dom.find(".times").each(function () {//时间插件
                if ($(this).val()) {
                    data[$(this).attr("name")] = new Date($(this).val().replace(/-/g, "/")).getTime();
                } else {
                    data[$(this).attr("name")] = 0;
                }
            });
            dom.find(".dates").each(function () {//时间插件
                if ($(this).val()) {
                    data[$(this).attr("name")] = new Date($(this).val().replace(/-/g, "/")).getTime();
                } else {
                    data[$(this).attr("name")] = 0;
                }
            });
            //金額插件
            if (dom.find(".amount").length > 0) {
                dom.find(".amount").each(function () {
                    data[$(this).attr("name")] = MX.savePrice($(this).val(), 100);
                });
            }
            dom.find("input[type='radio']").each(function () {
                if ($(this).prop('checked')) {
                    data[$(this).attr("name")] = $(this).val();
                }
            });
            dom.find("input[type='checkbox']").each(function () {
                if ($(this).prop('checked')) {
                    var ddd = data[$(this).attr("name")];
                    if (ddd !== undefined) {
                        ddd = ddd + "," + $(this).val();
                    } else {
                        ddd = $(this).val();
                    }
                    data[$(this).attr("name")] = ddd;
                }
            });
            dom.find("select").each(function () {
                var dataType = $.trim($(this).attr("dataType"));
                if("all"===dataType){
                    data[$(this).attr("name")] = $(this).val();
                    data[$(this).attr("name")+"_text"] = $(this).find("option:selected").text();
                }else if("text" === dataType){
                    data[$(this).attr("name")+"_text"] = $(this).find("option:selected").text();
                }else{
                    data[$(this).attr("name")] = $(this).val();
                }

            });
            dom.find(".lyear-switch").each(function () {
                var inputChecked = $(this).children("input[type=checkbox]");
                data[inputChecked.attr("name")] = inputChecked.prop('checked') ? 1 : 0
            });
            //wang编辑器 获取内部html内容
            if (dom.find(".wangContent").length > 0) {
                dom.find(".wangContent").each(function () {
                    data[$(this).attr("name")] = editors[$(this).attr("id")].getHtml();
                });
            }
            return data;
        }

    };

}();
