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
        this.showMsg(msg, "smile");
    }

    static failMsg(msg) {
        this.showMsg(msg, "cry");
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