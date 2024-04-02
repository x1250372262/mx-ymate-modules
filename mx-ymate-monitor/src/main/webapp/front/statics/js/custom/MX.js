var IS_CHECK_LOGIN = false;
var MX = function () {

    var method = function (parent, dom, type, handler) {
        $(parent).on(type, dom, handler);
    }


    var ajax = function (url, data, method, isToken, tokenCallBack, callback) {
        data = (data == null || data === "" || typeof (data) == "undefined") ? {"date": new Date().getTime()} : data;
        isToken = (isToken == null) ? false : isToken;
        data.format = "json";
        var headers = {};
        headers["X-Requested-With"] = "XMLHttpRequest"
        // debugger;
        if (isToken) {
            if (!Token.expires()) {
                if(!IS_CHECK_LOGIN){
                    IS_CHECK_LOGIN = true;
                    if (tokenCallBack != null) {
                        tokenCallBack();
                    } else {
                        MX.failMsg("请登录后进行操作")
                        setTimeout(function () {
                            window.location.href = LOGIN_VIEW;
                        }, 1000)
                    }
                }
                return false;
            } else {
                headers[HEADER_PARAM_NAME] = Token.get();
            }
        }
        $.ajax({
            type: method,
            data: data,
            async: false,
            headers: headers,
            url: url,
            dataType: "json",
            success: function (d, status, xhr) {
                if (d.code === "M0007") {
                    $.each(d.data, function (item) {
                        MX.failMsg(d.data[item] != null ? d.data[item] : "参数异常")
                        return false;
                    });
                } else if (d.code === "M0004") {
                    MX.failMsg("请登录后进行操作")
                    setTimeout(function () {
                        window.location.href = LOGIN_VIEW;
                    }, 1000)
                } else {
                    callback(d)
                }
            }, error: function (xhr, textStatus, errorThrown) {
                MX.failMsg(xhr.statusText + ":" + xhr.status)
                $(".mx_validator_button").attr("disabled", false)
            }
        });
    }


    var changeTimeToString = function (dateIn, type) {
        var yearF = "-";
        var monthF = "-";
        var dayF = "";
        var hoursF = ":";
        var minutesF = "";
        if (type === 1) {
            yearF = ".";
            monthF = ".";
            dayF = "";
        } else if (type === 2) {
            yearF = "年";
            monthF = "月";
            dayF = "日";
            hoursF = "时";
            minutesF = "分";
        }
        var year = 0;
        var month = 0;
        var day = 0;
        var hours = 0;
        var minutes = 0;
        var currentDate = "";
        year = dateIn.getFullYear();
        month = dateIn.getMonth() + 1;
        day = dateIn.getDate();
        hours = dateIn.getHours();
        minutes = dateIn.getMinutes();
        currentDate = year + yearF;
        if (month >= 10) {
            currentDate = currentDate + month + monthF;
        } else {
            currentDate = currentDate + "0" + month + monthF;
        }
        if (day >= 10) {
            currentDate = currentDate + day + dayF + " ";
        } else {
            currentDate = currentDate + "0" + day + dayF + " ";
        }
        if (hours >= 10) {
            currentDate = currentDate + hours + hoursF;
        } else {
            currentDate = currentDate + "0" + hours + hoursF;
        }
        if (minutes >= 10) {
            currentDate = currentDate + minutes + minutesF;
        } else {
            currentDate = currentDate + "0" + minutes + minutesF;
        }
        return currentDate;
    }

    var changeDateToString = function (dateIn, type) {
        var yearF = "-";
        var monthF = "-";
        var dayF = "";
        if (type === 1) {
            yearF = ".";
            monthF = ".";
            dayF = "";
        } else if (type === 2) {
            yearF = "年";
            monthF = "月";
            dayF = "日";
        }
        var year = 0;
        var month = 0;
        var day = 0;
        var currentDate = "";
        year = dateIn.getFullYear();
        month = dateIn.getMonth() + 1;
        day = dateIn.getDate();
        currentDate = year + yearF;
        if (month >= 10) {
            currentDate = currentDate + month + monthF;
        } else {
            currentDate = currentDate + "0" + month + monthF;
        }
        if (day >= 10) {
            currentDate = currentDate + day + dayF;
        } else {
            currentDate = currentDate + "0" + day + dayF;
        }
        return currentDate;
    }

    var savePrice = function (arg1, arg2) {
        var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
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

    var getPrice = function (arg1, arg2) {
        var t1 = 0, t2 = 0, r1, r2;
        try {
            t1 = arg1.toString().split(".")[1].length
        } catch (e) {
        }
        try {
            t2 = arg2.toString().split(".")[1].length
        } catch (e) {
        }
        with (Math) {
            r1 = Number(arg1.toString().replace(".", ""))
            r2 = Number(arg2.toString().replace(".", ""))
            return (r1 / r2) * pow(10, t2 - t1);
        }
    }

    var addPriceZero = function (price) {
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

    var showMsg = function (msg, icon) {
        layer.msg(msg, {
            icon: icon,
            skin: $(this).data('bg'),
            time: 1000
        });
    }

    var confirm = function (msg, ok, cancel) {
        var confirm = layer.confirm(msg, {
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


    return {
        confirm: function (msg, ok, cancel) {
            return confirm(msg, ok, cancel);
        },
        method: function (parent, type, dom, handler) {
            if (!parent) {
                parent = $(document);
            }
            if (!dom || !type || !handler) {
                alert("method方法： dom  type  handler 必传");
            }
            method(parent, dom, type, handler);
        },
        load: function () {
            return layer.load(0, {shade: [0.5, '#000']});  // 0.1透明度的白色背景
        },
        successMsg: function (msg) {
            showMsg(msg, "smile");
        },
        failMsg: function (msg) {
            showMsg(msg, "cry");
        },
        axpost: function (url, data, isToken, tokenCallBack, callback) {
            ajax(url, data, "post", isToken, tokenCallBack, callback)
        },
        axget: function (url, data, isToken, tokenCallBack, callback) {
            ajax(url, data, "get", isToken, tokenCallBack, callback)
        },
        changeTimeToString: function (dateIn, type) {
            if (type === undefined || type === null || type === "") {
                type = 0;
            }
            return changeTimeToString(new Date(dateIn), type);
        },
        changeDateToString: function (dateIn, type) {
            if (type === undefined || type === null || type === "") {
                type = 0;
            }
            return changeDateToString(new Date(dateIn), type);
        },


        savePrice: function (arg1, arg2) {
            return savePrice(arg1, arg2);
        },
        getPrice: function (arg1, arg2) {
            return getPrice(arg1, arg2);
        },
        addPriceZero: function (price) {
            return addPriceZero(price);
        },
        getQueryVariable: function (variable) {
            var query = window.location.search.substring(1);
            var vars = query.split("&");
            for (var i = 0; i < vars.length; i++) {
                var pair = vars[i].split("=");
                if (pair[0] === variable) {
                    return pair[1];
                }
            }
            return null;
        },
        moreText: function (value) {
            var regex = /(<([^>]+)>)/ig;
            value = value.replace(regex, "");
            var v = value;
            if (value && value.length > 20) {
                value = value.substr(0, 18) + "...";
                return "<span style='cursor:pointer;' class='moreText' dataValue='" + v + "'>" + value + "</span>";
            }
            return "<span>" + value + "</span>";
        },
        yl: function (url) {
            layer.open({
                type: 2,
                area: ['700px', '550px'],
                fixed: false, //不固定
                maxmin: true,
                content: url
            });
        }
    };

}();


