package com.mx.ymate.security.web.service;


import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.page.PageBean;
import com.mx.ymate.security.base.bean.SecurityUserBean;
import com.mx.ymate.security.base.vo.SecurityUserVO;

/**
 * @Author: mengxiang.
 * @create: 2021-09-23 15:41
 * @Description:
 */
public interface ISecurityUserService {


    /**
     * 人员列表
     *
     * @param userName
     * @param realName
     * @param disableStatus
     * @param pageBean
     * @return
     * @throws Exception
     */
    MxResult list(String userName, String realName, Integer disableStatus, PageBean pageBean) throws Exception;

    /**
     * 下拉选
     * @param disableStatus
     * @return
     * @throws Exception
     */
    MxResult select(Integer disableStatus) throws Exception;

    /**
     * 添加角色
     *
     * @param password
     * @param securityUserBean
     * @return
     * @throws Exception
     */
    MxResult create(String password, SecurityUserBean securityUserBean) throws Exception;

    /**
     * 详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    MxResult detail(String id) throws Exception;

    /**
     * 详情
     * @param id
     * @return
     * @throws Exception
     */
    SecurityUserVO detailInfo(String id) throws Exception;


    /**
     * 修改状态
     *
     * @param id
     * @param lastModifyTime
     * @param status
     * @return
     * @throws Exception
     */
    MxResult status(String id, Long lastModifyTime, Integer status) throws Exception;

    /**
     * 解锁
     *
     * @param id
     * @param lastModifyTime
     * @return
     * @throws Exception
     */
    MxResult unlock(String id, Long lastModifyTime) throws Exception;

    /**
     * 重置密码
     *
     * @param id
     * @param lastModifyTime
     * @return
     * @throws Exception
     */
    MxResult resetPassword(String id, Long lastModifyTime) throws Exception;

    /**
     * 人员权限列表
     *
     * @param userId
     * @param pageBean
     * @return
     * @throws Exception
     */
    MxResult roleList(String userId, PageBean pageBean) throws Exception;

    /**
     * 添加人员角色
     *
     * @param userId
     * @param roleId
     * @return
     * @throws Exception
     */
    MxResult roleCreate(String userId, String roleId) throws Exception;

    /**
     * 删除人员角色
     *
     * @param ids
     * @return
     * @throws Exception
     */
    MxResult roleDelete(String[] ids) throws Exception;
}
