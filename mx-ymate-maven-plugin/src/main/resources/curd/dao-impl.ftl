package ${daoPackageName}.impl;

import com.mx.ymate.dev.support.jdbc.dao.impl.MxDaoImpl;
import ${modelPackageName}.${modelName};
import ${daoPackageName}.I${modelName}Dao;
import net.ymate.platform.core.beans.annotation.Bean;

/**
* @Author: mx-maven-plugin.
* @Date: ${.now?string("yyyy/MM/dd")}.
* @Time: ${.now?string("HH:mm:ss")}.
* @Description: ${modelName}DaoImpl
*/
@Bean
public class ${modelName}DaoImpl extends MxDaoImpl<${modelName}> implements I${modelName}Dao {
}
