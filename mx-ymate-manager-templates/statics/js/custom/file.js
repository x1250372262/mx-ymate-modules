//检查文件是否合法
function checkFileExt(suffix, filename) {
    if (!$.trim(suffix)) {
        return 0;
    }
    var strs = suffix.split(","); //定义一数组
    //取出上传文件的扩展名
    var index = filename.lastIndexOf(".");
    var ext = filename.substr(index + 1);
    if (strs.indexOf(ext) > -1) {
        return 0;
    }
    return 1;
}

$(".fileYL").click(function(){
    var fileUrl = $.trim($(this).parent().prev().find(".fileInputShow_v2").val());
    if(!fileUrl){
        LayerUtil.failMsg("请先上传文件");
        return false;
    }
    LayerUtil.preview(fileUrl)
});

var FILE = function () {
    var init = function (dom, url, suffix) {
        dom.find(".fileButton_v2").each(function(){
            $(this).click(function(){
                $(this).next(".fileInput_v2").trigger("click");
                var inputDom = $(this).next(".fileInput_v2");
                var changeExed = true;
                inputDom.change(function () {
                    if(changeExed){
                        changeExed = false;
                        var fileName = $(this).val();
                        if (checkFileExt(suffix, fileName) === 1) {
                            LayerUtil.failMsg("文件格式错误,只允许添加" + suffix + "格式的文件")
                            return false;
                        }
                        var index = LayerUtil.loading();
                        var files = inputDom.prop('files');
                        if (!$.trim(files)) {
                            layer.close(index);
                            LayerUtil.failMsg("请选择文件")
                            return false;
                        }
                        var data = new FormData();
                        data.append('file', files[0]);
                        $.ajax({
                            type: 'POST',
                            url: url,
                            data: data,
                            headers: {"X-Requested-With":"XMLHttpRequest"},
                            cache: false,
                            processData: false,
                            contentType: false,
                            dataType: "json",
                            success: function (e) {
                                layer.close(index);
                                if (e.code === "00000") {
                                    inputDom.next("span").next(".fileInputDiv_v2").find(".fileInputShow_v2").val(e.data.url);
                                    inputDom.next("span").html(fileName);
                                    LayerUtil.successMsg(e.msg ? e.msg : '上传成功')
                                } else {
                                    LayerUtil.failMsg(e.msg ? e.msg : '上传失败')
                                }

                            }
                        });
                    }

                });
            });

        });


    }

    var initFunc = function (dom, url, suffix, success, fail) {
        dom.find(".fileButton_v2").each(function(){
            $(this).click(function(){
                $(this).next(".fileInput_v2").trigger("click");
                var inputDom = $(this).next(".fileInput_v2");
                var changeExed = true;
                inputDom.change(function () {
                    if(changeExed){
                        changeExed = false;
                        var fileName = $(this).val();
                        if (checkFileExt(suffix, fileName) === 1) {
                            LayerUtil.failMsg("文件格式错误,只允许添加" + suffix + "格式的文件")
                            return false;
                        }
                        var index = LayerUtil.loading();
                        var files = inputDom.prop('files');
                        if (!$.trim(files)) {
                            layer.close(index);
                            LayerUtil.failMsg("请选择文件")
                            return false;
                        }
                        var data = new FormData();
                        data.append('file', files[0]);
                        $.ajax({
                            type: 'POST',
                            url: url,
                            data: data,
                            headers: {"X-Requested-With":"XMLHttpRequest"},
                            cache: false,
                            processData: false,
                            contentType: false,
                            dataType: "json",
                            success: function (e) {
                                layer.close(index);

                                if (e.code === "00000") {
                                    if (success != null) {
                                        success(inputDom, e);
                                    }
                                    LayerUtil.successMsg(e.msg ? e.msg : '上传成功')
                                } else {
                                    if (fail != null) {
                                        fail(inputDom, e);
                                    }
                                    LayerUtil.failMsg(e.msg ? e.msg : '上传失败')
                                }

                            }
                        });
                    }
                });
            });

        });


    }

    return {
        init: function (dom, url, suffix) {
            if (!$.trim(suffix)) {
                suffix = "";
            }
            init(dom, url, suffix);
        },
        initFunc: function (dom, url, suffix, success, fail) {
            if (!$.trim(suffix)) {
                suffix = "";
            }
            initFunc(dom, url, suffix, success, fail);
        }

    };

}();


