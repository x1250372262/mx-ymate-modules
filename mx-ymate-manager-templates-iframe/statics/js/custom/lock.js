const LOCK_KEY = "mxLockKey";
var lockLayer;
function checkLock(){
    const userInfo = $.trim(User.get())
    var id = "";
    if (userInfo !== undefined && userInfo !== null && userInfo.length > 0) {
        let arrData = (typeof userInfo == 'object') ? userInfo : JSON.parse(userInfo);
        id = arrData.id;
    }
    if(id){
        Request.builder()
            .url(ADMIN_CHECK_LOCK)
            .data({"id":id})
            .callback(function (e) {
                if (e.code === "00000") {
                    showLock()
                    Store.set(LOCK_KEY, id);
                }
            }).post()
    }

}
function showLock(){
    lockLayer = layer.open({
        type: 1,
        title: false, // 禁用标题栏
        closeBtn: false, // 禁用默认关闭按钮
        area: ['100%', '100%'],
        scrollbar: false, // 暂时屏蔽浏览器滚动条
        anim: -1, // 禁用弹出动画
        isOutAnim: false, // 禁用关闭动画
        resize: false,  // 禁用右下角拉伸尺寸
        id: 'ID-layer-demo-inst',
        skin: 'class-demo-layer-lockscreen', // className
        content: $(".locking-box"),
        success: function (layero, index) {
            setInterval(() => {
                var currentDate = new Date();
                // 获取当前时间
                var currentHour = currentDate.getHours();
                var currentMinute = currentDate.getMinutes();
                var currentSecond = currentDate.getSeconds();
                currentHour = currentHour < 10 ? '0' + currentHour : currentHour;
                currentMinute = currentMinute < 10 ? '0' + currentMinute : currentMinute;
                currentSecond = currentSecond < 10 ? '0' + currentSecond : currentSecond;
                $("#time").html(currentHour + ':' + currentMinute + ':' + currentSecond)
                // 获取当前日期
                var currentMonth = currentDate.getMonth() + 1; // 月份是从0开始计数的，所以要加1
                var currentDay = currentDate.getDate();
                currentMonth = currentMonth < 10 ? '0' + currentMonth : currentMonth;
                currentDay = currentDay < 10 ? '0' + currentDay : currentDay;

                $("#day").html(currentMonth + '月' + currentDay + '日')
                // 获取当前日期对应的星期
                var days = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
                $("#week").html(days[currentDate.getDay()])
            }, 0)
        }
    });
}
$(function () {

    let lock = Store.get(LOCK_KEY);
    //没有锁
    if (lock === undefined || lock === null) {
        checkLock();
    }else{
        showLock();
    }

    /**
     * 解锁
     */
    $(".but").click(function () {
        var data = Form.getValues($("#lockFom"));
        Request.builder()
            .url(ADMIN_UNLOCK)
            .data(data)
            .callback(function (e) {
                if (e.code === "00000") {
                    LayerUtil.successMsg("解锁成功")
                    Store.remove(LOCK_KEY);
                    setTimeout(function () {
                        layer.close(lockLayer)
                    }, 1000)
                } else {
                    LayerUtil.failMsg("解锁失败,密码错误")
                }
            }).post()
    })


    //锁定屏幕
    $("#lock").click(function () {
        var id = $("#id").val();
        if (!id) {
            LayerUtil.failMsg("页面错误，请重新登录");
            return false;
        }
        Request.builder()
            .url(ADMIN_LOCK)
            .data({"id": id})
            .callback(function (e) {
                if (e.code === "00000") {
                    Store.set(LOCK_KEY, id);
                    showLock();
                } else {
                    LayerUtil.failMsg("锁定失败，请重试")
                }
            }).post()

    })

});