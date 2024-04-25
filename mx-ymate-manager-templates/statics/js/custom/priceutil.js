class PriceUtil {

    constructor() {
    }
    static savePrice(arg1, arg2){
        let m = 0, s1 = arg1.toString(), s2 = arg2.toString();
        try {
            m += s1.split(".")[1].length
        } catch (e) {
        }
        try {
            m += s2.split(".")[1].length
        } catch (e) {
        }
        return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
    }

    static getPrice(arg1, arg2){
        let t1 = 0, t2 = 0, r1, r2;
        try {
            t1 = arg1.toString().split(".")[1].length
        } catch (e) {
        }
        try {
            t2 = arg2.toString().split(".")[1].length
        } catch (e) {
        }
        r1 = Number(arg1.toString().replace(".", ""))
        r2 = Number(arg2.toString().replace(".", ""))
        return (r1 / r2) * Math.pow(10, t2 - t1);
    }

    static addPriceZero(arg1, arg2){
        if (String(price).indexOf(".") > 0) {
            //有小数
            if (String(price).indexOf(".") === String(price).length - 3) {
                //有两位小数
                return price;
            } else if (String(price).indexOf(".") === String(price).length - 2) {
                //有一位小数
                return String(price) + "0";
            }
        } else {
            //没有小数
            return String(price) + ".00";
        }
    }

}