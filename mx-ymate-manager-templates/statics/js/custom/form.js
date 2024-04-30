const Filter = {
    NO_CLEAR: "no_clear",
    NO_SET: "no_set",
    NO_GET: "no_get",
}

class Form {
    static setValues(dom, data, callback) {

        //设置图片数据
        dom.find(".thumbnail img").each(function () {
            $(this).attr("src", data[$(this).attr("name")] ? data[$(this).attr("name")] : "/statics/images/no_image.gif");
        });
        //设置输入框和textarea
        dom.find("input[type='text'],input[type='hidden'],input[type='number'],input[type='password'],textarea").each(function () {//普通input赋值
            if (!$(this).hasClass(Filter.NO_SET)) {
                let result;
                if ($(this).hasClass("amount") > 0) {
                    result = PriceUtil.getPrice(data[$(this).attr("name")], 100);
                } else if ($(this).hasClass("dates") || $(this).hasClass("times")) {
                    let format = $(this).attr("format");
                    if (format !== undefined && format !== null && format !== "") {
                        result = DateUtil.changeDateToString(data[$(this).attr("name")], format);
                    } else {
                        result = DateUtil.changeDateToString(data[$(this).attr("name")]);
                    }
                } else {
                    result = data[$(this).attr("name")];
                }
                $(this).val(result);
            }
        });
        //添加一行
        if (dom.find("#rowTemplate").length > 0) {
            let dataKey = $.trim($("#rowTemplate").attr("dataKey")) ? $("#rowTemplate").attr("dataKey") : "dataKey";
            let datakeyValue = data[dataKey];
            MMP.setValues(".rowValueDom", datakeyValue);
        }

        //radio 按钮
        dom.find("input[type='radio']").each(function () {
            if (data[$(this).attr("name")] === parseInt($(this).val())) {
                $(this).prop('checked', true)
            }
        });
        //文件
        dom.find(".mx_fileName").each(function () {
            if (data["mx_file_name"]) {
                $(this).html(data["mx_file_name"])
            }
        });
        //下拉选
        dom.find("select").each(function () {
            let dataValue = data[$(this).attr("name")];
            $(this).find("option[value='" + dataValue + "']").prop('selected', true)
            if ($(this).hasClass("select-cascade")) {
                let cascadeId = $.trim($(this).attr("cascadeId"));
                let cascadeKey = $.trim($(this).attr("cascadeKey"));
                let cascadePid = $.trim($(this).attr("cascadePid"));
                if (!dataValue || !cascadeKey || !cascadeId) {
                    LayerUtil.failMsg("value、cascadeKey、cascadeId都不能为空")
                    return false;
                }
                SelectCascade.selectData(cascadeKey, cascadeId, cascadePid, dataValue)
            }
        });
        //wang编辑器 重置内部html内容
        if (dom.find(".wangContent").length > 0) {
            dom.find(".wangContent").each(function () {
                let editor = editors[$(this).attr("id")]
                editor.destroy();
                editor = WangEditor.init($(this).parent())
                let content = data[$(this).attr("name")] != null && data[$(this).attr("name")] ? data[$(this).attr("name")] : "";
                editor.setHtml(content)
            });
        }
        //input标签
        $(dom).find(".js-tags-input").each(function () {
            $(this).importTags('');
            let value = data[$(this).attr("name")]
            $(this).tagsInput({
                height: '36px',
                width: '100%',
                defaultText: '',
                removeWithBackspace: true,
                delimiter: [',']
            });
            $(this).importTags(value);
        });

        if (callback !== undefined) {
            callback(dom, data);
        }
    }

    static clearValues(dom, callback) {
        $(dom).find('[name]').each(function () {
            if (!$(this).hasClass(Filter.NO_CLEAR)) {
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
                editors[$(this).attr("id")].clear();
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
            let inputChecked = $(this).children("input[type=checkbox]");
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
    }

    static getValues(dom, callback) {
        let data = {};
        dom.find("input[type='text'],input[type='hidden'],input[type='number'],input[type='password'],textarea").each(function () {
            if (!$(this).hasClass(Filter.NO_GET)) {
                if ($(this).attr("type") === "password") {
                    data[$(this).attr("name")] = hex_md5($(this).val());
                } else {
                    data[$(this).attr("name")] = $(this).val();
                }
            }
        });
        //
        if (dom.find("#rowTemplate").length > 0) {
            let dataKey = $.trim($("#rowTemplate").attr("dataKey")) ? $("#rowTemplate").attr("dataKey") : "dataKey";
            data[dataKey] = MMP.getValues(".rowValueDom");
        }

        dom.find(".dates").each(function () {//时间插件
            if ($(this).val()) {
                data[$(this).attr("name")] = new Date($(this).val().replace(/-/g, "/")).getTime();
            }
        });
        //金額插件
        if (dom.find(".amount").length > 0) {
            dom.find(".amount").each(function () {
                data[$(this).attr("name")] = PriceUtil.savePrice($(this).val(), 100);
            });
        }
        dom.find("input[type='radio']").each(function () {
            if ($(this).prop('checked')) {
                data[$(this).attr("name")] = $(this).val();
            }
        });
        dom.find("input[type='checkbox']").each(function () {
            if ($(this).prop('checked')) {
                let ddd = data[$(this).attr("name")];
                if (ddd !== undefined) {
                    ddd = ddd + "," + $(this).val();
                } else {
                    ddd = $(this).val();
                }
                data[$(this).attr("name")] = ddd;
            }
        });
        dom.find("select").each(function () {
            let dataType = $.trim($(this).attr("dataType"));
            if ("all" === dataType) {
                data[$(this).attr("name")] = $(this).val();
                data[$(this).attr("name") + "_text"] = $(this).find("option:selected").text();
            } else if ("text" === dataType) {
                data[$(this).attr("name") + "_text"] = $(this).find("option:selected").text();
            } else {
                data[$(this).attr("name")] = $(this).val();
            }

        });
        //wang编辑器 获取内部html内容
        if (dom.find(".wangContent").length > 0) {
            dom.find(".wangContent").each(function () {
                data[$(this).attr("name")] = editors[$(this).attr("id")].getHtml();
            });
        }
        return data;
    }

    static setHtml(dom, data, callback) {
        dom.find(".mx_html").each(function () {//
            if (!$(this).hasClass(Filter.NO_SET)) {
                let result;
                if ($(this).hasClass("amount")) {
                    result = PriceUtil.getPrice(data[$(this).attr("name")], 100);
                } else if ($(this).hasClass("dates") || $(this).hasClass("times")) {
                    let format = $(this).attr("format");
                    if (format !== undefined && format !== null && format !== "") {
                        result = DateUtil.changeDateToString(data[$(this).attr("name")], format);
                    } else {
                        result = DateUtil.changeDateToString(data[$(this).attr("name")]);
                    }
                } else {
                    result = data[$(this).attr("name")];
                }
                $(this).html(result);
            }
        });
        if (callback !== undefined) {
            callback(dom, data);
        }
    }
}

