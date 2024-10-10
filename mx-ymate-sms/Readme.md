### 使用示例
```java
package com.mx.play.manager;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.sms.Sms;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.annotation.RequestParam;
import net.ymate.platform.webmvc.view.IView;

import java.util.List;
@Controller
@RequestMapping("/demo")
public class DemoController {

    /**
     * 网建单独
     * @param mobile
     * @return
     * @throws Exception
     */
    @RequestMapping("/wj")
    public IView wj(@RequestParam String mobile) throws Exception {
        String content = "验证码是{}，请于15分钟内填写，如非本人操作，请忽略本短信。";
        MxResult mxResult = Sms.get().send(mobile, StrUtil.format(content, "123123"));
        return mxResult.toJsonView();
    }

    /**
     * 网建批量
     * @param mobile
     * @param mobile1
     * @return
     * @throws Exception
     */
    @RequestMapping("/wj")
    public IView wj(@RequestParam String mobile,@RequestParam String mobile1) throws Exception {
        String content = "验证码是{}，请于15分钟内填写，如非本人操作，请忽略本短信。";
        List<String> mobileList = ListUtil.toList(mobile,mobile1);
        MxResult mxResult = Sms.get().send(mobileList, StrUtil.format(content, "123123"));
        return mxResult.toJsonView();
    }

    /**
     * 腾讯单独
     * @param mobile
     * @return
     * @throws Exception
     */
    @RequestMapping("/tx")
    public IView tx(@RequestParam String mobile) throws Exception {
        MxResult mxResult = Sms.get().send("tx",mobile, new String[]{"123123", "15"});
        return mxResult.toJsonView();
    }

    /**
     * 腾讯批量
     * @param mobile
     * @param mobile1
     * @return
     * @throws Exception
     */
    @RequestMapping("/tx")
    public IView tx(@RequestParam String mobile,@RequestParam String mobile1) throws Exception {
        List<String> mobileList = ListUtil.toList(mobile,mobile1);
        MxResult mxResult = Sms.get().send("tx",mobileList, new String[]{"123123", "15"});
        return mxResult.toJsonView();
    }

    /**
     * 阿里单独
     * @param mobile
     * @return
     * @throws Exception
     */
    @RequestMapping("/ali")
    public IView ali(@RequestParam String mobile) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("workshopName", "测试车间");
        jsonObject.put("stationName", "投饵机一号");
        jsonObject.put("time", DateTimeUtils.formatTime(DateTimeUtils.systemTimeUTC(), DateTimeUtils.YYYY_MM_DD_HH_MM_SS));
        MxResult mxResult = Sms.get().send("ali",mobile, jsonObject.toJSONString());
        return mxResult.toJsonView();
    }

    /**
     * 阿里批量
     * @param mobile
     * @param mobile1
     * @return
     * @throws Exception
     */
    @RequestMapping("/ali")
    public IView ali(@RequestParam String mobile,@RequestParam String mobile1) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("workshopName", "测试车间");
        jsonObject.put("stationName", "投饵机一号");
        jsonObject.put("time", DateTimeUtils.formatTime(DateTimeUtils.systemTimeUTC(), DateTimeUtils.YYYY_MM_DD_HH_MM_SS));
        List<String> mobileList = ListUtil.toList(mobile,mobile1);
        MxResult mxResult = Sms.get().send("ali",mobileList, jsonObject.toJSONString());
        return mxResult.toJsonView();
    }
}
```
Sms.get().send("ali",mobileList, jsonObject.toJSONString());

上面代码中ali对应的就是通道 可以同时支持很多通道 但是一个通道只能支持一种服务商

配置文件
```properties
#-------------------------------------
# module.sms 模块初始化参数
#-------------------------------------

# 模块是否已启用, 默认值: true
ymp.configs.module.sms.enabled=true
#短信通道 默认default  可以配置多个 每个都可以指定短信服务商 以及 模板等等
ymp.configs.module.sms.channel=default|tx|ali
#是否开发模式 默认true  开发模式不发送验证码  采用控制台打印或者接口返回
ymp.configs.module.sms.devMode=true

#短信服务商 网建=smschinese|腾讯=tx|阿里=ali 不能为空 可以自己指定 实现com.mx.ymate.sms.adapter.ISmsAdapter 接口
ymp.configs.module.sms.default.type=smschinese
#SecretID
ymp.configs.module.sms.default.secret_id=jshopin_youban
#SecretKey
ymp.configs.module.sms.default.secret_key=320dba9689f998e6aedb

#短信服务商 网建=smschinese|腾讯=tx|阿里=ali 不能为空 可以自己指定 实现com.mx.ymate.sms.adapter.ISmsAdapter 接口
ymp.configs.module.sms.tx.type=tx
#SecretID
ymp.configs.module.sms.tx.secret_id=AKIDyysgLtKdmlbIpDSSk6TlvBRcxIull9eD
#SecretKey
ymp.configs.module.sms.tx.secret_key=mRzPpDqQRJ3e6NVVL0UenrobEoMrIevB
#-------------------------------------
# 腾讯配置
#-------------------------------------
#应用id
ymp.configs.module.sms.tx.tx_app_id=1400920069
#地域
ymp.configs.module.sms.tx.tx_region=ap-beijing
# 短信签名内容
ymp.configs.module.sms.tx.tx_sign_name=大连优伴信息
#模板id
ymp.configs.module.sms.tx.tx_template_id=2201933


#短信服务商 网建=smschinese|腾讯=tx|阿里=ali 不能为空 可以自己指定 实现com.mx.ymate.sms.adapter.ISmsAdapter 接口
ymp.configs.module.sms.ali.type=ali
#SecretID
ymp.configs.module.sms.ali.secret_id=LTAI5tLVaCiWd7JK6ZfWUfd2
#SecretKey
ymp.configs.module.sms.ali.secret_key=MYB7RIoceWyQ7yE24hXMGNpW1BYqsg
#-------------------------------------
# 阿里配置
#-------------------------------------
#短信签名内容
ymp.configs.module.sms.ali.ali_sign=智渔科技
#模板id
ymp.configs.module.sms.ali.ali_template_code=SMS_468025027
#endpoint
ymp.configs.module.sms.ali.ali_endpoint=dysmsapi.aliyuncs.com
```

如果只要一种通道就直接配置default就可以  阿里腾讯配置default也支持
