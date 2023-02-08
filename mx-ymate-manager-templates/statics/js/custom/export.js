//导出
var EX = function () {

    var exec = function (dom, url, isToken = true) {
        if (!url) {
            url = $.trim(dom.attr("exportUrl"));
        }
        if (!url) {
            url = $.trim(dom.attr("listUrl"));
        }
        var data = FORM.getValues($('#searchForm'));
        var records = dom.bootstrapTable('getSelections');
        var ids = [];
        if (records.length > 0) {
            for (var i in records) {
                ids[i] = records[i]["id"];
            }
        }

        data.ids = ids
        data.export = 1;
        var load = MX.load();
        MX.axpost(url, data, isToken, null, function (e) {
            layer.close(load);
            if (e.code === "00000") {
                MX.successMsg("文件生成成功，开始下载...");
                window.location.href = e.data;
            } else {
                MX.failMsg(e.msg ? e.msg : "生成失败");
            }
        })
    }

    return {
        exec: function (dom, isToken = true) {
            return exec(dom, "", isToken);
        },
        execUrl: function (dom, url, isToken = true) {
            return exec(dom, $.trim(url), isToken);
        }
    };

}();


