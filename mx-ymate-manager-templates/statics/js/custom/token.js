
var TOKEN_KEY = "mxToken";
var HEADER_PARAM_NAME = "mxToken";
var Token = function () {

    return {
        expires: function () {
            var token = Store.get(TOKEN_KEY);
            if (!token) {
                return false;
            }
            return true;
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
        },
    };

}();


