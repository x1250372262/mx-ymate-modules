class MX {
    constructor() {
    }
    static getQueryVariable(variable){
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            if (pair[0] === variable) {
                return pair[1];
            }
        }
        return null;
    }

}

class LayerUtil {

    static confirm(msg, ok, cancel) {
        let confirm = layer.confirm(msg, {
            btn: ['确认', '关闭'] //可以无限个按钮
        }, function (index, layero) {
            ok(index, layero);
        }, function (index) {
            if (cancel !== undefined && cancel !== null) {
                cancel(index);
            } else {
                layer.close(confirm);
            }
        });
        return confirm;
    }
    static showMsg(msg, icon) {
        layer.msg(msg, {
            icon: icon,
            time: 1000
        });
    }
    static successMsg(msg) {
        this.showMsg(msg, 'smile');
    }

    static failMsg(msg) {
        this.showMsg(msg, 'cry');
    }

    static preview(url) {
        layer.open({
            type: 2,
            area: ['700px', '550px'],
            fixed: false, //不固定
            maxmin: true,
            content: url
        });
    }

    static loading() {
        return layer.load(0, {shade: [0.5, '#000']})
    }
}

const DateFormatEnum = {
    YYYY_MM_DD_HH_MM_SS_SSS: "yyyy-MM-dd hh:mm:ss.SSS",
    YYYY_MM_DD_HH_MM_SS: "yyyy-MM-dd hh:mm:ss",
    YYYY_MM_DD_HH_MM: "yyyy-MM-dd hh:mm",
    YYYY_MM_DD: "yyyy-MM-dd",
    YYYY_MM: "yyyy-MM",
}
class DateUtil {

    constructor() {
    }
    static changeDateToString(timestamp, format = DateFormatEnum.YYYY_MM_DD_HH_MM_SS) {
        var date = new Date(timestamp);
        var map = {
            "M": date.getMonth() + 1, // 月份
            "d": date.getDate(), // 日
            "h": date.getHours(), // 小时
            "m": date.getMinutes(), // 分钟
            "s": date.getSeconds(), // 秒
            "S": date.getMilliseconds() // 毫秒
        };

        format = format.replace(/([yMdhmsS])+/g, function (all, t) {
            var v = map[t];
            if (v !== undefined) {
                if (all.length > 1) {
                    v = '0' + v;
                    v = v.substr(v.length - all.length);
                }
                return v;
            } else if (t === 'y') {
                return (date.getFullYear() + '').substr(4 - all.length);
            }
            return all;
        });
        return format;
    }
}

class PriceUtil {

    constructor() {
    }
    static savePrice(arg1, arg2){
        let m = 0, s1 = arg1.toString(), s2 = arg2.toString();
        try {
            m += s1.split(".")[1].length
        } catch (e) {
        }
        try {
            m += s2.split(".")[1].length
        } catch (e) {
        }
        return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
    }

    static getPrice(arg1, arg2){
        let t1 = 0, t2 = 0, r1, r2;
        try {
            t1 = arg1.toString().split(".")[1].length
        } catch (e) {
        }
        try {
            t2 = arg2.toString().split(".")[1].length
        } catch (e) {
        }
        r1 = Number(arg1.toString().replace(".", ""))
        r2 = Number(arg2.toString().replace(".", ""))
        return (r1 / r2) * Math.pow(10, t2 - t1);
    }

    static addPriceZero(price){
        if (String(price).indexOf(".") > 0) {
            //有小数
            if (String(price).indexOf(".") === String(price).length - 3) {
                //有两位小数
                return price;
            } else if (String(price).indexOf(".") === String(price).length - 2) {
                //有一位小数
                return String(price) + "0";
            }
        } else {
            //没有小数
            return String(price) + ".00";
        }
    }
}

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
                        toLogin();
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
                        toLogin();
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


const VALIDATE_CLASS = {
    MX_VALIDATOR: ".mx_validator",
    MX_VALIDATOR_BUTTON: ".mx_validator_button",
    MX_REQUIRED_TYPE: "mx_required_type",
    MX_REQUIRED: ".mx_required",
    MX_REQUIRED_MSG: "mx_required_msg",
    MX_CUSTOM: ".mx_custom",
    HAS_ERROR: ".has-error",
    HELP_BLOCK: ".help-block"
}

const REQUIRED_TYPE = {
    DEFAULT:"",
    RADIO:"radio",
    CHECKBOX:"checkbox",
    SELECT:"select",
    WANG_EDITOR:"wangEditor",
}

const mxValidator = VALIDATE_CLASS.MX_VALIDATOR;
const mxValidatorButton = VALIDATE_CLASS.MX_VALIDATOR_BUTTON;
const mxRequiredType = VALIDATE_CLASS.MX_REQUIRED_TYPE;
const mxRequired = VALIDATE_CLASS.MX_REQUIRED;
const mxRequiredMsg = VALIDATE_CLASS.MX_REQUIRED_MSG;
const mxCustom = VALIDATE_CLASS.MX_CUSTOM;
const hasError = VALIDATE_CLASS.HAS_ERROR;
const helpBlock = VALIDATE_CLASS.HELP_BLOCK;

class Validator {

    constructor() {
        this._dom = "";
        this._url = {};
        //请求方式
        this._method = REQUEST_METHOD.POST;
        this._isToken = true;
        this._customValidate = null;
        this._customSubmit = null;
        this._submitCallback = {};
    }

    static builder() {
        return new Validator();
    }

    dom(dom) {
        this._dom = dom;
        return this;
    }

    getDom(){
        return this._dom;
    }

    url(url) {
        this._url = url;
        return this;
    }

    getUrl(){
        return this._url;
    }

    method(method) {
        this._method = method;
        return this;
    }

    getMethod(){
        return this._method;
    }


    isToken(isToken) {
        this._isToken = isToken;
        return this;
    }

    getIsToken(){
        return this._isToken;
    }

    customValidate(customValidate) {
        this._customValidate = customValidate;
        return this;
    }

    getCustomValidate(){
        return this._customValidate;
    }

    customSubmit(customSubmit) {
        this._customSubmit = customSubmit;
        return this;
    }

    getCustomSubmit(){
        return this._customSubmit;
    }

    submitCallback(submitCallback) {
        this._submitCallback = submitCallback;
        return this;
    }

    getSubmitCallback(){
        return this._submitCallback;
    }

    //验证非空
    #requiredCheck(requiredDom) {
        let result = {
            "retBool": false,
            "msg": ""
        };
        let required = false;
        let type = $.trim(requiredDom.attr(mxRequiredType));
        if (type === REQUIRED_TYPE.RADIO) {
            let radioValue = requiredDom.find("input:radio:checked").val();
            if (radioValue == null) {
                required = true;
            }
        } else if (type === REQUIRED_TYPE.CHECKBOX) {
            let checkboxValue = requiredDom.find("input:checkbox:checked").val();
            if (checkboxValue == null) {
                required = true;
            }
        } else if (type === REQUIRED_TYPE.SELECT) {
            if (!$.trim(requiredDom.val())) {
                required = true;
            }
        } else if (type === REQUIRED_TYPE.WANG_EDITOR) {
            if (!$.trim(editors[requiredDom.attr("id")].getText())) {
                required = true;
            }
        } else {
            if (!$.trim(requiredDom.val())) {
                required = true;
            }
        }
        let msg = $.trim(requiredDom.attr(mxRequiredMsg));
        if (!msg) {
            msg = ERROR_MSG.NO_BLANK;
        }
        result.retBool = required;
        result.msg = msg;
        return result;
    }
    //验证自定义
    #validatorCheck(validatorDom, customValidationFunc) {
        let result = {
            "retBool": false,
            "msg": ""
        };
        if (customValidationFunc != null && customValidationFunc(validatorDom)) {
            return customValidationFunc(validatorDom);
        }
        return result;
    }

    do() {
        let ths = this;
        let canExec = true;
        let dom = ths.getDom();
        if (dom.find(mxValidator).length > 0) {
            dom.find(mxValidatorButton).attr("disabled", true)
            let resultArray = [];
            let resultDom = {}
            dom.find(mxValidator).each(function () {
                let parentThis = $(this);
                $(this).find(mxRequired).each(function () {
                    let result = {};
                    let requiredResult = ths.#requiredCheck($(this));
                    result.domKey = $(this);
                    result.parentThis = parentThis;
                    result.validatorBool = requiredResult.retBool;
                    result.validatorMsg = requiredResult.msg;
                    result.isWwangEditor = $(this).attr(mxRequiredType)==="wangEditor";
                    result.isPic = $(this).attr(mxRequiredType)==="pic";
                    resultArray.push(result)
                    resultDom[$(this).attr("name")] = requiredResult.retBool;

                });
                $(this).find(mxCustom).each(function () {
                    let bool = resultDom[$(this).attr("name")];
                    // console.log(resultDom)
                    if (!bool) {
                        let result = {};
                        let validatorResult = ths.#validatorCheck($(this), ths.getCustomValidate());
                        result.domKey = $(this);
                        result.parentThis = parentThis;
                        result.validatorBool = validatorResult.retBool;
                        result.validatorMsg = validatorResult.msg;
                        result.isWwangEditor = $(this).attr(mxRequiredType)==="wangEditor";
                        result.isPic = $(this).attr(mxRequiredType)==="pic";
                        resultArray.push(result)
                    }

                });
            });
            $.each(resultArray, function (index, item) {
                let pd = item.parentThis;
                let dk = item.domKey;
                if(item.isWwangEditor){
                    pd = pd.parent();
                    dk = dk.parent();
                }else if(item.isPic){
                    pd = pd.parents("ul");
                    dk = dk.parents("ul");
                }
                if (item.validatorBool) {
                    if (canExec) {
                        canExec = false;
                    }
                    if (!pd.hasClass(hasError)) {
                        pd.addClass(hasError);
                    }
                    if (dk.next(helpBlock).length <= 0) {
                        dk.parent().append("<small class=\"help-block\">" + item.validatorMsg + "</small>");
                    } else {
                        dk.parent().find(helpBlock).html(item.validatorMsg);
                    }
                    dom.find(mxValidatorButton).attr("disabled", false)
                } else {
                    if (pd.hasClass(hasError)) {
                        pd.removeClass(hasError);
                    }
                    if (dk.parent().find(helpBlock).length > 0) {
                        dk.parent().find(helpBlock).remove();
                    }
                }
            });

        }
        let customSubmit = ths.getCustomSubmit();
        if (canExec) {
            if (customSubmit) {
                customSubmit(canExec);
            } else {
                let data = Form.getValues(dom);
                let url = ths.getUrl();
                if (!url) {
                    url = dom.attr("action");
                }
                let subCallBack = ths.getSubmitCallback();
                Request.builder()
                    .url(url)
                    .data(data)
                    .method(ths.getMethod())
                    .isToken(ths.getIsToken())
                    .callback(function(e){
                        subCallBack(e);
                    }).do();
            }
        } else {
            if (customSubmit) {
                customSubmit(canExec);
            }
        }
    }



}

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

function toLogin(){
    父页面存在时跳转到登录页面
    if (window.parent) {
        window.parent.location.href = LOGIN_VIEW; // 父页面跳转
    } else {
        window.location.href = LOGIN_VIEW; // 当前页面跳转
    }
}