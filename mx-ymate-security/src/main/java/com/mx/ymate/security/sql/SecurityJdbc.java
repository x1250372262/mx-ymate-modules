package com.mx.ymate.security.sql;

import com.mx.ymate.dev.support.jdbc.MxJdbc;
import com.mx.ymate.security.base.model.SecurityUser;
import net.ymate.platform.persistence.jdbc.query.Join;

public class SecurityJdbc {


    public static Join securityJoin(String prefix, String tableAlias, String fieldName) {
        return MxJdbc.innerJoin(prefix, SecurityUser.TABLE_NAME,"su",SecurityUser.FIELDS.ID,tableAlias,fieldName);
    }

}
