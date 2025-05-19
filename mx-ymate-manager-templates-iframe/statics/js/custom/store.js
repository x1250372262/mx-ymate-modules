const TOKEN_KEY = "sftManagerToken";
const HEADER_PARAM_NAME = "sftManagerToken";
const MENU_KEY = "mxMenu";
const PERMISSION_KEY = "mxPermission";
const USER_KEY = "mxUser";
var Store = function () {

    var TYPE = {};
    TYPE.Storage = "Storage";
    TYPE.cookie = "cookie";

    return {
        get: function (key, type) {
            if (TYPE.cookie === type) {
                return $.cookie(key);
            } else {
                return localStorage.getItem(key);
            }
        },
        set: function (key, value, type) {
            if (TYPE.cookie === type) {
                return $.cookie(key, value);
            } else {
                localStorage.setItem(key, value);
            }
        },
        remove: function (key, type) {
            if (TYPE.cookie === type) {
                $.cookie(key, null);
            } else {
                localStorage.removeItem(key);
            }
        }
    };

}();

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
        init: function () {
            let data = this.get();
            if (data == null || data.length === 0) {
                return false;
            }
            var arrData = (typeof data == 'object') ? data : JSON.parse(data);
            var treeObj = this.getTrees(arrData, 0, 'id', 'pid', 'children');
            var html = this.createMenu(treeObj, true, 0);
            $('.sidebar-main').append(html);

            var href = window.location.pathname;
            $('.sidebar-main a').each(function () {
                var aHref = $(this).attr("path");
                var mode = 0;
                if (aHref === undefined || aHref === "") {
                    aHref = $(this).attr("href");
                    mode = 1;
                }
                if (aHref.indexOf("?") >= 0) {
                    aHref = aHref.substr(0, aHref.lastIndexOf("?"));
                }
                if (mode === 1) {
                    if (aHref === href) {
                        $(this).parents(".cc").addClass("active");
                        $(this).parents(".aa").addClass("open");
                        $(this).parents(".bb").css("display", "block");
                    }
                } else {
                    if (href.indexOf(aHref) >= 0) {
                        $(this).parents(".cc").addClass("active");
                        $(this).parents(".aa").addClass("open");
                        $(this).parents(".bb").css("display", "block");
                    }
                }

            });
        },
        createMenu: function (data, is_frist, depth) {
            var menu_body = is_frist ? '<ul class="nav-drawer">' : '<ul class="nav nav-subnav bb">';
            for (var i = 0; i < data.length; i++) {
                let iframe_class = data[i].is_out === 1 ? 'target="_blank"' : 'class="multitabs"';
                let icon_div = data[i].pid === "0" ? '<i class="' + data[i].icon + '"></i>' : '';
                let menuName = data[i].pid === "0" ? '<span>' + data[i].name + '</span>' : data[i].name;

                if (data[i].children && data[i].children.length > 0) {
                    menu_body += '<li class="aa nav-item nav-item-has-subnav"><a href="javascript:void(0)">' + icon_div + menuName + '</a>';
                    if (depth === 0) {
                        menu_body += this.createMenu(data[i].children, false, 1);
                    } else {
                        menu_body += this.createMenuChild(data[i].children, false);
                    }
                } else {
                    menu_body += '<li class="nav-item cc"><a href="' + data[i].url + '" ' + iframe_class + '>' + icon_div + menuName + '</a>';
                }
                menu_body += '</li>';
            }
            menu_body += '</ul>';
            return menu_body;
        },
        createMenuChild: function (data, is_frist) {
            var menu_body = is_frist ? '<ul class="nav nav-drawer">' : '<ul class="nav nav-subnav bb">';
            for (var i = 0; i < data.length; i++) {
                let iframe_class = data[i].is_out === 1 ? 'target="_blank"' : 'class="multitabs"';
                let icon_div = data[i].pid === "0" ? '<i class="' + data[i].icon + '"></i>' : '';
                let menuName = data[i].pid === "0" ? '<span>' + data[i].name + '</span>' : data[i].name;
                menu_body += '<li class="nav-item cc"><a href="' + data[i].url + '" ' + iframe_class + '>' + icon_div + menuName + '</a></li>';
            }

            menu_body += '</ul>';
            return menu_body;
        },
        getTrees: function (list, parentId, idName, parentIdName, childrenName) {
            let items = {};
            // 获取每个节点的直属子节点，*记住是直属，不是所有子节点
            for (let i = 0; i < list.length; i++) {
                let key = list[i][parentIdName];
                if (items[key]) {
                    items[key].push(list[i]);
                } else {
                    items[key] = [];
                    items[key].push(list[i]);
                }
            }
            return this.formatTree(items, parentId, idName, childrenName);
        },
        formatTree: function (items, parentId, idName, childrenName) {
            let result = [];
            if (!items[parentId]) {
                return result;
            }
            for (let t in items[parentId]) {
                items[parentId][t][childrenName] = this.formatTree(items, items[parentId][t][idName], 'id', 'children')
                result.push(items[parentId][t]);
            }
            return result;
        }

    };

}();


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

