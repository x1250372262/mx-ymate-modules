
var TOKEN_KEY = "sftManagerToken";
var HEADER_PARAM_NAME = "sftManagerToken";
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
        },
    };

}();


