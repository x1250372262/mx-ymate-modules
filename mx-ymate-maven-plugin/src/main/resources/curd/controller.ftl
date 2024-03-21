package ${controllerPackageName};

import com.mx.ymate.dev.support.page.PageDTO;
import ${servicePackageName}.I${modelName}Service;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.validation.validate.VRequired;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.PathVariable;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.annotation.RequestParam;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.view.IView;

/**
* @Author: mx-maven-plugin.
* @Date: ${.now?string("yyyy/MM/dd")}.
* @Time: ${.now?string("HH:mm:ss")}.
* @Description: ${modelName}Controller
*/
@Controller
@RequestMapping("/${modelName?uncap_first}")
public class ${modelName}Controller {

    @Inject
    private I${modelName}Service i${modelName}Service;

    /**
    * 添加
    <#list dtoFieldList as field>
    * @param ${field.name} ${field.comment}
    </#list>
    * @return
    * @throws Exception
    */
    @RequestMapping(value = "/create", method = Type.HttpMethod.POST)
    public IView create(<#list dtoFieldList as field>@RequestParam ${field.type} ${field.name}<#if field_has_next>,
                       </#if> </#list>) throws Exception {
        return i${modelName}Service.create(<#list dtoFieldList as field>${field.name}<#if field_has_next>, </#if></#list>).toJsonView();
    }

    /**
    * 修改
    *
    * @param id
    * @param sftId 总平台就是sft 公司就是数据的父id
    * @return
    * @throws Exception
    */
    @RequestMapping(value = "/update/{id}", method = Type.HttpMethod.POST)
    public IView update(@PathVariable String id,
                        <#list dtoFieldList as field>@RequestParam ${field.type} ${field.name}<#if field_has_next>,
                        </#if> </#list>) throws Exception {
        return i${modelName}Service.update(id,<#list dtoFieldList as field>${field.name}<#if field_has_next>, </#if></#list>).toJsonView();
    }


    /**
    * 删除
    *
    * @param ids
    * @return
    * @throws Exception
    */
    @RequestMapping(value = "/delete", method = Type.HttpMethod.POST)
    public IView delete(@VRequired(msg = "ids不能为空")
                        @RequestParam("ids[]") String[] ids) throws Exception {
        return i${modelName}Service.delete(ids).toJsonView();
    }


    /**
    * 详情
    *
    * @param id
    * @return
    * @throws Exception
    */
    @RequestMapping("/detail/{id}")
    public IView detail(@PathVariable String id) throws Exception {
        return i${modelName}Service.detail(id).toJsonView();
    }


    /**
    * 列表
    *
    * @param pageDTO
    * @return
    * @throws Exception
    */
    public IView list(PageDTO pageDTO) throws Exception {
        return i${modelName}Service.list(pageDTO.toBean()).toJsonView();
    }


}
