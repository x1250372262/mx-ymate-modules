function setCookie() { //设置cookie
    let userName = $("#userName").val(); //获取用户名信息
    let password = $("#password").val(); //获取用户名信息
    let checked = $("#jizhu:checked")//获取“是否记住密码”复选框
    if (checked && checked.length > 0) { //判断是否选中了“记住密码”复选框
        $.cookie("is_remmber", "1");//调用jquery.cookie.js中的方法设置cookie中的记住密码
        $.cookie("userName", userName);//调用jquery.cookie.js中的方法设置cookie中的用户名
        $.cookie("password", password);//调用jquery.cookie.js中的方法设置cookie中的密码
    } else {
        $.cookie("is_remmber", "0");
        $.cookie("userName", "");
        $.cookie("password", "");
    }
}

function getCookie() { //获取cookie
    if ($.cookie("is_remmber") === "1") {
        $("#userName").val($.cookie("userName"));
        $("#password").val($.cookie("password"));
        $("#jizhu").attr("checked", true);
    }
}

$(function () {

    let logout = MX.getQueryVariable("logout");
    if (logout !== undefined && logout !== null && logout === "1") {
        Token.remove()
        Menu.remove()
        Menu.removePermission();
        User.remove()
    }
    getCookie();

    //登录
    $("#loginSubmit").click(function () {
        Validator.builder()
            .url(ADMIN_LOGIN)
            .isToken(false)
            .dom($("#loginForm"))
            .submitCallback(function (result) {
                $("#verCode").val("");
                if (result.code === "00000") {
                    setCookie();
                    Token.set(result.data.tokenValue)
                    Menu.set(result.data.navList)
                    Menu.setPermission(result.data.permissionList)
                    User.set(result.data.userInfo)
                    LayerUtil.successMsg("登录成功")
                    setTimeout(function () {
                        window.location.href = "/admin/home.html"
                    }, 1000)
                } else {
                    LayerUtil.failMsg(result.msg != null ? result.msg : "登录失败")
                    $(".mx_validator_button").attr("disabled", false)
                }

            }).do();
    });
});