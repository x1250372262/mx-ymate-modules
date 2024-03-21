package ${servicePackageName}.impl;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.page.PageBean;
import com.mx.ymate.dev.support.page.Pages;
import com.mx.ymate.security.SaUtil;
import ${daoPackageName}.I${modelName}Dao;
import ${servicePackageName}.I${modelName}Service;
import ${modelPackageName}.${modelName};
import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.core.persistence.IResultSet;

/**
* @Author: mx-maven-plugin.
* @Date: ${.now?string("yyyy/MM/dd")}.
* @Time: ${.now?string("HH:mm:ss")}.
* @Description: ${modelName}ServiceImpl
*/
@Bean
public class ${modelName}ServiceImpl implements I${modelName}Service {

    @Inject
    private I${modelName}Dao i${modelName}Dao;

    @Override
    public MxResult create(<#list dtoFieldList as field>${field.type} ${field.name}<#if field_has_next>, </#if></#list>) throws Exception {
        ${modelName} ${modelName?uncap_first} = ${modelName}.builder()
                .id(UUIDUtils.UUID())
                <#list dtoFieldList as field>
                .${field.name}(${field.name})
                </#list>
                .createTime(System.currentTimeMillis())
                .createUser(SaUtil.loginId())
                .lastModifyTime(System.currentTimeMillis())
                .lastModifyUser(SaUtil.loginId())
                .build();
        ${modelName?uncap_first} = i${modelName}Dao.create(${modelName?uncap_first});
        return MxResult.result(${modelName?uncap_first});
    }

    @Override
    public MxResult update(String id,<#list dtoFieldList as field>${field.type} ${field.name}<#if field_has_next>, </#if></#list>) throws Exception {
        ${modelName} ${modelName?uncap_first} = i${modelName}Dao.findById(id);
        if (${modelName?uncap_first} == null) {
            return MxResult.noData();
        }
        ${modelName?uncap_first} = ${modelName?uncap_first}.bind()
                <#list dtoFieldList as field>
                .${field.name}(${field.name})
                </#list>
                .lastModifyTime(System.currentTimeMillis())
                .lastModifyUser(SaUtil.loginId())
                .build();
        ${modelName?uncap_first} = i${modelName}Dao.update(${modelName?uncap_first},<#list dtoFieldList as field>${modelName}.FIELDS.${field.updateFieldName}<#if field_has_next>, </#if></#list>,${modelName}.FIELDS.LAST_MODIFY_TIME,${modelName}.FIELDS.LAST_MODIFY_USER);
        return MxResult.result(${modelName?uncap_first});
    }

    @Override
    public MxResult delete(String[] ids) throws Exception {
        int[] result = i${modelName}Dao.delete(ids);
        return MxResult.result(result);
    }

    @Override
    public MxResult detail(String id) throws Exception {
        ${modelName} ${modelName?uncap_first} = i${modelName}Dao.findById(id);
        return MxResult.okData(${modelName?uncap_first});
    }

    @Override
    public MxResult list(PageBean pageBean) throws Exception {
        IResultSet<${modelName}> resultSet = i${modelName}Dao.find(pageBean.toPage());
        return MxResult.okData(Pages.create(resultSet));
    }
}
