var USER_KEY = "sftManagerUser";
var User = function () {

    return {
        expires: function () {
            return Store.get(USER_KEY);

        },
        get: function () {
            return Store.get(USER_KEY);
        },
        set: function (data) {
            Store.set(USER_KEY, JSON.stringify(data));
        },
        remove: function () {
            Store.remove(USER_KEY);
        },
        refresh: function (data) {
            Store.set(USER_KEY, JSON.stringify(data));
        },
        isFounder: function () {
            var userInfo = $.trim(User.get())
            if (userInfo !== undefined && userInfo !== null && userInfo.length > 0) {
                var arrData = (typeof userInfo == 'object') ? userInfo : JSON.parse(userInfo);
                return arrData.founder === 1;
            }
            return false;
        }
    };

}();


