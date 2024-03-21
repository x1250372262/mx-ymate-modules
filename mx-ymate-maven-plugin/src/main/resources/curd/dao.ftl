package ${daoPackageName};

import com.mx.ymate.dev.support.jdbc.dao.IMxDao;
import ${modelPackageName}.${modelName};

/**
* @Author: mx-maven-plugin.
* @Date: ${.now?string("yyyy/MM/dd")}.
* @Time: ${.now?string("HH:mm:ss")}.
* @Description: ${modelName}Dao
*/
public interface I${modelName}Dao extends IMxDao<${modelName}> {

}
