var userPermissionList = Menu.getPermission();
$(function () {
    var pagePermissionStr = $.trim($("body").attr("mx_permission"));
    if (pagePermissionStr !== undefined && pagePermissionStr !== null && $.trim(pagePermissionStr) !== "") {
        var isHasPermission = checkPermission(pagePermissionStr);
        if(!isHasPermission){
            alert("当前页面没有权限访问");
            return false;
        }
    }
    $(".mx_permission").each(function () {
        var ths = $(this);
        var permissionStr = $.trim(ths.attr("mx_permission"));
        if (permissionStr !== undefined && permissionStr !== null && $.trim(permissionStr) !== "") {
           var isHasPermission = checkPermission(permissionStr);
           if(!isHasPermission){
               ths.remove();
           }
        }
    });
});

function checkPermission(permissionStr) {
    if(User.isFounder()){
        return true;
    }
    var permissionList = permissionStr.split("|");
    var flag = true;
    $.each(permissionList, function (index, item) {
        if ($.inArray(item, userPermissionList) === -1) {
            flag = false;
            return false;
        }
    })
    return flag;
}
