//存储js。暂支持localStorage  cookie 默认ocalStorage
var Store = function () {

    var TYPE = {};
    TYPE.Storage = "Storage";
    TYPE.cookie = "cookie";

    return {
        get: function (key,type) {
            if(TYPE.cookie===type){
                return $.cookie(key);
            }else{
                return localStorage.getItem(key);
            }
        },
        set: function (key, value,type) {
            if(TYPE.cookie===type){
                return $.cookie(key,value);
            }else{
                localStorage.setItem(key,value);
            }
        },
        remove: function (key,type) {
            if(TYPE.cookie===type){
                $.cookie(key, null);
            }else{
                localStorage.removeItem(key);
            }
        }
    };

}();


