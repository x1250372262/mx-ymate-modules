//检查文件是否合法
function checkFileExt(suffix, filename) {
    if (!$.trim(suffix)) {
        return 0;
    }
    var strs = new Array(); //定义一数组
    strs = suffix.split(","); //字符分割
    //取出上传文件的扩展名
    var index = filename.lastIndexOf(".");
    var ext = filename.substr(index + 1);
    if (strs.indexOf(ext) > -1) {
        return 0;
    }
    return 1;
}

var EXCEL_IMPORT = function () {
    var init = function (dom, url, suffix, showErrorMsg,success, fail) {
        dom.find(".fileInputExcel").change(function () {
            var fileName = $(this).val();
            if (checkFileExt(suffix, fileName) === 1) {
                MX.failMsg("文件格式错误,只允许添加xlsx和xls格式的文件");
                $(this).val("");
                return false;
            }
        });

        dom.find(".uploadExcelButton").click(function () {
            MX.load();
            var files = dom.find(".fileInputExcel").prop('files');
            if (!$.trim(files)) {
                layer.closeAll();
                MX.failMsg("请选择文件");
                return false;
            }
            var data = new FormData();
            var otherData = FORM.getValues(dom);
            $.each(otherData, function (item) {
                data.append(item, otherData[item]);
            });
            data.append('file', files[0]);
            var headers = {};
            headers["X-Requested-With"] = "XMLHttpRequest"
            // debugger;
            if (!Token.expires()) {
                MX.failMsg("请登录后进行操作")
                setTimeout(function () {
                    window.location.href = LOGIN_VIEW;
                }, 1000)
                return false;
            } else {
                headers[HEADER_PARAM_NAME] = Token.get();
            }
            $.ajax({
                type: 'POST',
                url: url,
                data: data,
                headers:headers,
                cache: false,
                processData: false,
                contentType: false,
                dataType: "json",
                success: function (e) {
                    layer.closeAll();
                    if (e.code === "M0007") {
                        $.each(e.data, function (item) {
                            MX.failMsg(e.data[item] != null ? e.data[item] : "参数异常")
                            return false;
                        });
                    } else if (e.code === "M0004") {
                        MX.failMsg("请登录后进行操作")
                        setTimeout(function () {
                            window.location.href = LOGIN_VIEW;
                        }, 1000)
                    } else if (e.code === "00000") {
                        if(success !==undefined && success!==null){
                            success(e);
                        }else {
                            MX.successMsg("操作成功")

                            if(showErrorMsg){
                                var html = "";
                                $.each(e.data.errorInfoList, function (index,item) {
                                    html += "<p>" + item.content + "<p>";
                                });
                                if ((navigator.userAgent.match(/(iPhone|iPod|Android|ios|iOS|iPad|Backerry|WebOS|Symbian|Windows Phone|Phone)/i))) {
                                    layer.open({
                                        type: 1,
                                        skin: 'layui-layer-rim', //加上边框
                                        area: ['100%', '240px'], //宽高
                                        content: html
                                    });
                                } else {
                                    layer.open({
                                        type: 1,
                                        skin: 'layui-layer-rim', //加上边框
                                        area: ['420px', '240px'], //宽高
                                        content: html
                                    });
                                }
                            }

                            setTimeout(function () {
                                $("#tableAjaxId").bootstrapTable('refresh');
                                dom.find(".close").trigger("click");
                            }, 1000)
                        }

                    } else {
                        if(fail !==undefined && fail!==null){
                            fail(e);
                        }else{
                            MX.failMsg(e.msg ? e.msg : "操作失败")
                        }
                    }

                }
            });
        });

    }

    return {
        init: function (dom, url, suffix,showErrorMsg, success, fail) {
            if (!$.trim(suffix)) {
                suffix = "";
            }
            if(showErrorMsg===undefined || showErrorMsg===null){
                showErrorMsg = false;
            }
            init(dom, url, suffix,showErrorMsg, success, fail);
        }
    };

}();


