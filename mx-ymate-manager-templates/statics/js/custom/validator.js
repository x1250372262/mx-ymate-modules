
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
                let loading = LayerUtil.loading()
                Request.builder()
                    .url(url)
                    .data(data)
                    .method(ths.getMethod())
                    .isToken(ths.getIsToken())
                    .callback(function(e){
                        layer.close(loading);
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
