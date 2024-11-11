### 使用示例

#### 模板只适用于腾讯和阿里 网建短信通过api接口直接发送 只需要配置secret_id和secret_key即可 发送的内容在代码直接传即可
#### 模板支持key=templateId|key1=templateId1 这种方式 也支持直接放入模板id 有模板key的情况下，调用send的时候直接传模板key即可
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
        MxResult mxResult = Sms.get().sendByChannel("tx",mobile, new String[]{"123123", "15"});
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
        MxResult mxResult = Sms.get().sendByChannel("tx",mobileList, new String[]{"123123", "15"});
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
        MxResult mxResult = Sms.get().sendByChannel("ali",mobile, jsonObject.toJSONString());
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
        MxResult mxResult = Sms.get().sendByChannel("ali",mobileList, jsonObject.toJSONString());
        return mxResult.toJsonView();
    }
}
```
Sms.get().sendByChannel("ali",mobileList, jsonObject.toJSONString());

上面代码中ali对应的就是通道 可以同时支持很多通道 但是一个通道只能支持一种服务商

如果只要一种通道就直接配置default就可以  阿里腾讯配置default也支持
