const { createEditor, createToolbar } = window.wangEditor
const editorConfig = {
    MENU_CONF:{}
}

editorConfig.MENU_CONF['uploadImage'] = {

    // 自定义上传
    async customUpload(file, insertFn) {
        var formFile = new FormData();
        formFile.append("file", file);
        // JS 语法
        var loading = layer.load(0, {shade: [0.5, '#000']});  // 0.1透明度的白色背景
        $.ajax({
            type: 'POST',
            url: UPLOAD_FILE_FWB,
            data: formFile,
            cache: false,
            processData: false,
            contentType: false,
            dataType: "json",
            success: function (e) {
                layer.close(loading);
                if (e.code === "00000") {
                    MX.successMsg("上传成功")
                    insertFn(e.data.url, file.name, "")
                } else {
                    MX.failMsg(e.msg?e.msg:"上传失败")
                }
            }
        });
    }
}

editorConfig.MENU_CONF['uploadVideo'] = {
    // 自定义上传
    async customUpload(file, insertFn) {
        var formFile = new FormData();
        formFile.append("file", file);
        // JS 语法
        var loading = layer.load(0, {shade: [0.5, '#000']});  // 0.1透明度的白色背景
        $.ajax({
            type: 'POST',
            url: UPLOAD_FILE_FWB,
            data: formFile,
            cache: false,
            processData: false,
            contentType: false,
            dataType: "json",
            success: function (e) {
                layer.close(loading);
                if (e.code === "00000") {
                    MX.successMsg("上传成功")
                    insertFn(e.data.url, e.data.url)
                } else {
                    MX.failMsg(e.msg?e.msg:"上传失败")
                }
            }
        });
    }
}

$(function(){
    //实例化编辑器
    $(".wangDiv").each(function () {
        init($(this));
    })
});
function init(e) {

    var contentId = $(e).find(".wangContent").attr("id");
    var barId = $(e).find(".wangBar").attr("id");
    if(!contentId || !barId){
        alert("请检查id配置");
        return false;
    }

    const editor = createEditor({
        selector: '#' + contentId,
        config: editorConfig,
        mode: 'default', // or 'simple'
    })

    const toolbarConfig = {}

    createToolbar({
        editor,
        selector: '#'+barId,
        config: toolbarConfig,
        mode: 'default', // or 'simple'
    })

    editors[contentId]=editor;
}
