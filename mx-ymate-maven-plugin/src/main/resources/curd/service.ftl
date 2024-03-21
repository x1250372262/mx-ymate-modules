package ${servicePackageName};

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.page.PageBean;

/**
* @Author: mx-maven-plugin.
* @Date: ${.now?string("yyyy/MM/dd")}.
* @Time: ${.now?string("HH:mm:ss")}.
* @Description: ${modelName}Service
*/
public interface I${modelName}Service {

    /**
    * 添加
    <#list dtoFieldList as field>
    * @param ${field.name} ${field.comment}
    </#list>
    * @return
    * @throws Exception
    */
    MxResult create(<#list dtoFieldList as field>${field.type} ${field.name}<#if field_has_next>, </#if></#list>) throws Exception;

    /**
    * 修改
    * @param id
    <#list dtoFieldList as field>
    * @param ${field.name} ${field.comment}
    </#list>
    * @return
    * @throws Exception
    */
    MxResult update(String id,<#list dtoFieldList as field>${field.type} ${field.name}<#if field_has_next>, </#if></#list>) throws Exception;

    /**
    * 删除
    *
    * @param ids
    * @return
    * @throws Exception
    */
    MxResult delete(String[] ids) throws Exception;

    /**
    * 详情
    *
    * @param id
    * @return
    * @throws Exception
    */
    MxResult detail(String id) throws Exception;

    /**
    * 列表
    *
    * @param pageBean
    * @return
    * @throws Exception
    */
    MxResult list(PageBean pageBean) throws Exception;

}
