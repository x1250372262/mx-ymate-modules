var VALIDATE = function () {

    //验证非空
    var requiredCheck = function (requiredDom) {
        var result = {
            "retBool": false,
            "msg": ""
        };
        var required = false;
        var type = $.trim(requiredDom.attr("mx_required_type"));
        if (type === "radio") {
            var radioValue = requiredDom.find("input:radio:checked").val();
            if (radioValue == null) {
                required = true;
            }
        } else if (type === "checkbox") {
            var checkboxValue = requiredDom.find("input:checkbox:checked").val();
            if (checkboxValue == null) {
                required = true;
            }
        } else if (type === "select") {
            if (!$.trim(requiredDom.val())) {
                required = true;
            }
        } else if (type === "wangEditor") {
            if (!$.trim(editors[requiredDom.attr("id")].getText())) {
                required = true;
            }
        } else {
            if (!$.trim(requiredDom.val())) {
                required = true;
            }
        }
        var msg = $.trim(requiredDom.attr("mx_required_msg"));
        if (!msg) {
            msg = "此项不能为空";
        }
        result.retBool = required;
        result.msg = msg;
        return result;
    }
    //验证自定义
    var validatorCheck = function (validatorDom, customValidationFunc) {
        var result = {
            "retBool": false,
            "msg": ""
        };
        if (customValidationFunc != null && customValidationFunc(validatorDom)) {
            return customValidationFunc(validatorDom);
        }
        return result;
    }
    var validate = function (dom, url, token, customValidationFunc, successCallback, submitCallback) {

        var canExec = true;
        if (dom.find(".mx_validator").length > 0) {
            dom.find(".mx_validator_button").attr("disabled", true)
            var resultArray = [];
            var resultDom = {}
            dom.find(".mx_validator").each(function () {
                var parentThis = $(this);
                $(this).find(".mx_required").each(function () {

                    var result = {};
                    var requiredResult = requiredCheck($(this));
                    result.domKey = $(this);
                    result.parentThis = parentThis;
                    result.validatorBool = requiredResult.retBool;
                    result.validatorMsg = requiredResult.msg;
                    result.isWwangEditor = $(this).attr("mx_required_type")==="wangEditor";
                    resultArray.push(result)
                    resultDom[$(this).attr("name")] = requiredResult.retBool;

                });
                $(this).find(".mx_custom").each(function () {
                    var bool = resultDom[$(this).attr("name")];
                    // console.log(resultDom)
                    if (!bool) {
                        var result = {};
                        var validatorResult = validatorCheck($(this), customValidationFunc);
                        result.domKey = $(this);
                        result.parentThis = parentThis;
                        result.validatorBool = validatorResult.retBool;
                        result.validatorMsg = validatorResult.msg;
                        result.isWwangEditor = $(this).attr("mx_required_type")==="wangEditor";
                        resultArray.push(result)
                    }

                });
            });
            $.each(resultArray, function (index, item) {
                // console.log(item)
                var pd = item.parentThis;
                var dk = item.domKey;
                if(item.isWwangEditor){
                    pd = pd.parent();
                    dk = dk.parent();
                }
                if (item.validatorBool) {
                    if (canExec) {
                        canExec = false;
                    }
                    if (!pd.hasClass("has-error")) {
                        pd.addClass("has-error");
                    }
                    if (dk.next(".help-block").length <= 0) {
                        dk.parent().append("<small class=\"help-block\">" + item.validatorMsg + "</small>");
                    } else {
                        dk.parent().find(".help-block").html(item.validatorMsg);
                    }
                    dom.find(".mx_validator_button").attr("disabled", false)
                } else {
                    if (pd.hasClass("has-error")) {
                        pd.removeClass("has-error");
                    }
                    if (dk.parent().find(".help-block").length > 0) {
                        dk.parent().find(".help-block").remove();
                    }
                }
            });

        }
        if (canExec) {
            if (successCallback) {
                successCallback(canExec);
            } else {
                var data = FORM.getValues(dom);
                if (!url) {
                    url = dom.attr("action");
                }
                MX.axpost(url, data, token, null, function (e) {
                    submitCallback(e);
                })
            }
        } else {
            if (successCallback) {
                successCallback(canExec);
            }
        }

    }

    return {
        validate: function (dom, url, customValidationFunc, successCallback, submitCallback) {
            validate(dom, url, true, customValidationFunc, successCallback, submitCallback);
        },
        validateNoToken: function (dom, url, customValidationFunc, successCallback, submitCallback) {
            validate(dom, url, false, customValidationFunc, successCallback, submitCallback);
        }
    };

}();


