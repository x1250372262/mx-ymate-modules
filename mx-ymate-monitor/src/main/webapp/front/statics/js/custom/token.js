
var TOKEN_KEY = "sftMonitorToken";
var HEADER_PARAM_NAME = "sftMonitorToken";
var Token = function () {

    return {
        expires: function () {
            return Store.get(TOKEN_KEY);

        },
        get: function () {
            return Store.get(TOKEN_KEY);
        },
        set: function (data) {
            Store.set(TOKEN_KEY, data);
        },
        remove: function () {
            Store.remove(TOKEN_KEY);
        },
        refresh: function (data) {
            Store.set(TOKEN_KEY, data);
        }
    };

}();

