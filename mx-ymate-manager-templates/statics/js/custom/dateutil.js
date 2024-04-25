const DateFormatEnum = {
    YYYY_MM_DD_HH_MM_SS_SSS: "yyyy-MM-dd hh:mm:ss.SSS",
    YYYY_MM_DD_HH_MM_SS: "yyyy-MM-dd hh:mm:ss",
    YYYY_MM_DD_HH_MM: "yyyy-MM-dd hh:mm",
    YYYY_MM_DD: "yyyy-MM-dd",
    YYYY_MM: "yyyy-MM",
}


class DateUtil {

    constructor() {
    }
    static changeDateToString(timestamp, format = DateFormatEnum.YYYY_MM_DD_HH_MM_SS) {
        var date = new Date(timestamp);
        var map = {
            "M": date.getMonth() + 1, // 月份
            "d": date.getDate(), // 日
            "h": date.getHours(), // 小时
            "m": date.getMinutes(), // 分钟
            "s": date.getSeconds(), // 秒
            "S": date.getMilliseconds() // 毫秒
        };

        format = format.replace(/([yMdhmsS])+/g, function (all, t) {
            var v = map[t];
            if (v !== undefined) {
                if (all.length > 1) {
                    v = '0' + v;
                    v = v.substr(v.length - all.length);
                }
                return v;
            } else if (t === 'y') {
                return (date.getFullYear() + '').substr(4 - all.length);
            }
            return all;
        });
        return format;
    }


}