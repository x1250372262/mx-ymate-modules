const baseUrl = "https://test-mapi.dlsf-tech.com";   //本地
const prefix = "/sft/manager/v1";
//管理员登录
ADMIN_LOGIN = baseUrl  + "/mx/security/login/login";
//修改管理员密码
ADMIN_UPDATE_PASSWORD = baseUrl + "/mx/security/login/password";
//修改管理员信息
ADMIN_INFO_UPDATE = baseUrl + "/mx/security/login/update";
//扫码登录获取二维码
ADMIN_SCAN_LOGIN_GENERATE_QRCODE = baseUrl  + "/mx/security/scan/login/generate/qrcode";
//轮询检查二维码
ADMIN_SCAN_LOGIN_CHECK_QRCODE = baseUrl  + "/mx/security/scan/login/check/qrcode";
//管理员退出
ADMIN_LOGOUT = baseUrl + "/mx/security/login/logout";
//检查是否加锁
ADMIN_CHECK_LOCK = baseUrl + "/mx/security/login/check/lock";
//加锁
ADMIN_LOCK = baseUrl + "/mx/security/login/lock";
//解锁
ADMIN_UNLOCK = baseUrl + "/mx/security/login/unlock";
//管理员信息
ADMIN_INFO = baseUrl + "/mx/security/login/info";
//上传文件
UPLOAD_FILE = baseUrl + "/mx/upload/push";
//富文本文件上传
UPLOAD_FILE_FWB = baseUrl + "/mx/upload/push";
//左侧菜单
MENU_NAV = baseUrl + "/mx/security/menu/nav";