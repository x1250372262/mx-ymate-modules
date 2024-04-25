const LOGIN_VIEW = "/security/login_view.html";
var editors = {};
const version = 2;
const ERROR_MSG = {
    NO_LOGIN: "请登录后进行操作",
    INVALID_PARAMETERS: "参数异常",
    NO_BLANK: "此项不能为空",
    SUCCESS:"操作成功",
    FAIL:"操作失败"
}

const REQUEST_METHOD = {
    GET: "GET",
    POST: "POST"
}
document.write('<link rel="stylesheet" type="text/css" href="/statics/css/materialdesignicons.min.css?v=' + version + '"/>');
document.write('<link rel="stylesheet" type="text/css" href="/statics/css/bootstrap.min.css?v=' + version + '"/>');
document.write('<link rel="stylesheet" type="text/css" href="/statics/plugins/bootstrap-touchspin/jquery.bootstrap-touchspin.css?v=' + version + '"/>');
document.write('<link rel="stylesheet" type="text/css" href="/statics/css/animate.min.css?v=' + version + '"/>');
document.write('<link rel="stylesheet" type="text/css" href="/statics/css/style.min.css?v=' + version + '"/>');
document.write('<link rel="stylesheet" type="text/css" href="/statics/plugins/bootstrap-table/bootstrap-table.min.css?v=' + version + '"><\/script>');
document.write('<link rel="stylesheet" type="text/css" href="/statics/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css?v=' + version + '"><\/script>');

document.write('<script type="text/javascript" src="/statics/js/system/jquery.min.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/plugins/layer/layer.min.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/plugins/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/plugins/bootstrap-datetimepicker/locales/bootstrap-datetimepicker.zh-CN.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/plugins/bootstrap-table/bootstrap-table.min.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/plugins/bootstrap-touchspin/jquery.bootstrap-touchspin.min.js?v=' + version + '"><\/script>');

document.write('<script type="text/javascript" src="/statics/js/system/jquery.cookie.min.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/system/md5.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/system/perfect-scrollbar.min.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/system/main.min.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/system/angular.min.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/system/popper.min.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/system/bootstrap.min.js?v=' + version + '"><\/script>');


document.write('<script type="text/javascript" src="/statics/js/custom/store.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/custom/store/token.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/custom/store/menu.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/custom/store/user.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/custom/dateutil.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/custom/MX.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/custom/layerutil.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/custom/priceutil.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/custom/request.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/custom/validator.js?v=' + version + '"><\/script>');

document.write('<script type="text/javascript" src="/statics/js/custom/permission.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/custom/form.js?v=' + version + '"><\/script>');
document.write('<script type="text/javascript" src="/statics/js/custom/upload_pic.js?v=' + version + '"><\/script>');

document.write('<script type="text/javascript" src="/statics/js/apis/base.js?v=' + version + '"><\/script>');