$(function () {
    $(document).on("click", ".picUpload", function () {
        $(this).siblings("input[type=file]").trigger("click");
    })

    $(document).on("click", ".picDelete", function () {
        $(this).siblings(".fileresult").val("");
        $(this).parent().siblings("img").attr("src","/statics/images/no_image.gif");
    })

    $(document).on("change", ".fileInput", function () {
        var $this = $(this);
        var $input = $(this)[0];
        var $len = $input.files.length;
        var formFile = new FormData();
        if ($len === 0) {
            return false;
        } else {
            var fileAccaccept = $this.attr('filter');
            var fileType = $input.files[0].type;
            var type = (fileType.substr(fileType.lastIndexOf("/") + 1)).toLowerCase();

            if (!type || fileAccaccept.indexOf(type) === -1) {
                layer.msg("您上传图片的类型不符合:"+fileAccaccept, {
                    icon: 'cry',
                    skin: $(this).data('bg')
                });
                return false;
            }
            formFile.append("file", $input.files[0]);
        }

        var data = formFile;
        var loading = layer.load(0, {shade: [0.5, '#000']});  // 0.1透明度的白色背景
        $.ajax({
            type: 'POST',
            url: UPLOAD_FILE,
            data: data,
            headers: {"X-Requested-With":"XMLHttpRequest"},
            cache: false,
            processData: false,
            contentType: false,
            dataType: "json",
            success: function (e) {
               layer.close(loading);
                if (e.code === "00000") {
                    layer.msg("上传成功", {
                        icon: 'smile',
                        skin: $(this).data('bg')
                    });
                    $this.siblings(".fileresult").val(e.data.url);
                    $this.parent().siblings("img").attr("src",e.data.url);
                } else {
                    layer.msg(e.msg?e.msg:"上传失败", {
                        icon: 'cry',
                        skin: $(this).data('bg')
                    });
                }

            }
        });
    })
});
