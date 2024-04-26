baseUrl = "https://test.mapi.dlsf-tech.com";    //测试
// baseUrl = "http://localhost:8082";   //本地
// baseUrl = "http://10.10.10.109:8082";   //本地
prefix = "/sft/manager/v1";

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
