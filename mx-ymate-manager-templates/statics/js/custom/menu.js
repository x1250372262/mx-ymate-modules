var MENU_KEY = "sftManagerMenu";
var PERMISSION_KEY = "sftManagerPermission";
var Menu = function () {

    return {
        expires: function () {
            return Store.get(MENU_KEY);
        },
        get: function () {
            return Store.get(MENU_KEY);
        },
        set: function (data) {
            Store.set(MENU_KEY, JSON.stringify(data));
        },
        remove: function () {
            Store.remove(MENU_KEY);
        },
        refresh: function (data) {
            Store.set(MENU_KEY, JSON.stringify(data));
        },
        getPermission: function () {
            var permissionList = [];
            var userPermission = $.trim(Store.get(PERMISSION_KEY));
            if (userPermission !== undefined && userPermission !== null && userPermission !== "") {
                permissionList = JSON.parse(userPermission);
            }
            return permissionList;
        },
        setPermission: function (data) {
            Store.set(PERMISSION_KEY, JSON.stringify(data));
        },
        removePermission: function () {
            Store.remove(PERMISSION_KEY);
        },
    };

}();


