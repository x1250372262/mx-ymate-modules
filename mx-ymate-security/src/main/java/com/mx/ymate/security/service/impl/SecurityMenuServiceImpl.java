package com.mx.ymate.security.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.page.PageBean;
import com.mx.ymate.dev.support.page.Pages;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.annotation.OperationLog;
import com.mx.ymate.security.base.bean.SecurityMenuBean;
import com.mx.ymate.security.base.enums.MenuType;
import com.mx.ymate.security.base.enums.OperationType;
import com.mx.ymate.security.base.enums.ResourceType;
import com.mx.ymate.security.base.model.SecurityMenu;
import com.mx.ymate.security.base.model.SecurityMenuRole;
import com.mx.ymate.security.base.vo.SecurityMenuListVO;
import com.mx.ymate.security.base.vo.SecurityMenuNavVO;
import com.mx.ymate.security.base.vo.SecurityMenuRoleVO;
import com.mx.ymate.security.base.vo.SecurityMenuVO;
import com.mx.ymate.security.dao.ISecurityMenuDao;
import com.mx.ymate.security.dao.ISecurityMenuRoleDao;
import com.mx.ymate.security.handler.IUserHandler;
import com.mx.ymate.security.service.ISecurityMenuService;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.core.persistence.IResultSet;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.mx.ymate.security.base.code.SecurityCode.SECURITY_MENU_HAS_CHILD_NOT_DELETE;
import static com.mx.ymate.security.base.code.SecurityCode.SECURITY_MENU_ROLE_EXISTS;


/**
 * @Author: mengxiang.
 * @create: 2021-09-23 15:44
 * @Description:
 */
@Bean
public class SecurityMenuServiceImpl implements ISecurityMenuService {

    @Inject
    private ISecurityMenuDao iSecurityMenuDao;
    @Inject
    private ISecurityMenuRoleDao iSecurityMenuRoleDao;

    private final ISecurityConfig config = Security.get().getConfig();

    private final IUserHandler userHandler = config.userHandlerClass();


    @Override
    public MxResult nav() throws Exception {
        List<SecurityMenuNavVO> navList = navList(SaUtil.loginId(),SaUtil.isFounder());
        return MxResult.ok().data(navList);
    }

    @Override
    public List<SecurityMenuNavVO> navList(String userId,boolean isFounder) throws Exception {
        String resourceId = StringUtils.defaultIfBlank(userHandler.buildResourceId(ResourceType.MENU), config.client());
        if (isFounder) {
            return iSecurityMenuDao.findAllByType(null, Constants.BOOL_FALSE, config.client(), resourceId).getResultData();
        }
        //查询带权限的还有公开的 合并到一起
        List<SecurityMenuNavVO> list = iSecurityMenuDao.findAll(userId, config.client(), StringUtils.defaultIfBlank(userHandler.buildResourceId(ResourceType.ROLE), config.client()), Constants.BOOL_FALSE).getResultData();
        List<SecurityMenuNavVO> navList = new ArrayList<>(list);
        List<SecurityMenuNavVO> publicList = iSecurityMenuDao.findAllByType(MenuType.PUBLIC.value(), Constants.BOOL_FALSE, config.client(), resourceId).getResultData();
        navList.addAll(publicList);
        if (CollUtil.isNotEmpty(navList)) {
            navList = navList.stream().sorted(Comparator.comparing(SecurityMenuNavVO::getSort)).collect(Collectors.toList());
        }
        return navList;
    }

    private List<SecurityMenuListVO> createTree(List<SecurityMenuNavVO> resultSet, List<SecurityMenuNavVO> allCategory) {
        List<SecurityMenuListVO> treeList = new ArrayList<>();
        if (CollUtil.isEmpty(resultSet)) {
            return treeList;
        }
        resultSet.forEach(menuNavVO -> {
            SecurityMenuListVO treeVO = new SecurityMenuListVO();
            treeVO.setText(menuNavVO.getName());
            treeVO.setState("{ \"opened\" : false }");
            List<SecurityMenuNavVO> childrenCategory = allCategory.stream().filter(cs -> menuNavVO.getId().equals(cs.getPid())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(childrenCategory)) {
                List<SecurityMenuListVO> children = createTree(childrenCategory, allCategory);
                treeVO.setChildren(children);
                treeVO.setIcon("mdi mdi-folder-outline");
            } else {
                treeVO.setIcon("mdi mdi-file-outline");
            }

            treeVO.setAttr(MapUtil.builder().put("data_id", menuNavVO.getId()).build());
            treeVO.setSort(menuNavVO.getSort());
            treeList.add(treeVO);
        });
        return treeList;
    }


    @Override
    public MxResult list() throws Exception {
        String resourceId = StringUtils.defaultIfBlank(userHandler.buildResourceId(ResourceType.MENU), config.client());
        List<SecurityMenuNavVO> menuNavVOList = iSecurityMenuDao.findAllByType(null, null, config.client(), resourceId).getResultData();
        List<SecurityMenuListVO> menuListVOList = new ArrayList<>();
        if (CollUtil.isEmpty(menuNavVOList)) {
            return MxResult.ok().data(menuListVOList);
        }
        List<SecurityMenuNavVO> topMenuList = menuNavVOList.stream().filter(menuNavVO -> "0".equals(menuNavVO.getPid())).collect(Collectors.toList());
        menuListVOList = createTree(topMenuList, menuNavVOList);
        if (CollUtil.isNotEmpty(menuListVOList)) {
            menuListVOList = menuListVOList.stream().sorted(Comparator.comparing(SecurityMenuListVO::getSort)).collect(Collectors.toList());
        }
        return MxResult.ok().data(menuListVOList);
    }

    @Override
    @OperationLog(operationType = OperationType.CREATE, title = "添加菜单")
    public MxResult create(SecurityMenuBean menuBean) throws Exception {
        String resourceId = StringUtils.defaultIfBlank(userHandler.buildResourceId(ResourceType.MENU), config.client());
        SecurityMenu menu = BeanUtil.copy(menuBean, SecurityMenu::new);
        menu.setId(UUIDUtils.UUID());
        menu.setClient(config.client());
        menu.setResourceId(resourceId);
        menu = iSecurityMenuDao.create(menu);
        return MxResult.result(menu);
    }

    @Override
    @OperationLog(operationType = OperationType.UPDATE, title = "修改菜单")
    public MxResult update(String id, SecurityMenuBean menuBean) throws Exception {
        SecurityMenu menu = iSecurityMenuDao.findById(id);
        if (menu == null) {
            return MxResult.noData();
        }
        menu = BeanUtil.duplicate(menuBean, menu);
        menu = iSecurityMenuDao.update(menu, SecurityMenu.FIELDS.PARENT_ID, SecurityMenu.FIELDS.NAME, SecurityMenu.FIELDS.ICON,
                SecurityMenu.FIELDS.PATH, SecurityMenu.FIELDS.URL, SecurityMenu.FIELDS.SORT, SecurityMenu.FIELDS.TYPE, SecurityMenu.FIELDS.HIDE_STATUS);
        return MxResult.result(menu);
    }

    @Override
    public MxResult detail(String id) throws Exception {
        return MxResult.ok().data(BeanUtil.copy(iSecurityMenuDao.findById(id), SecurityMenuVO::new));
    }

    @Override
    @OperationLog(operationType = OperationType.DELETE, title = "删除菜单")
    public MxResult delete(String id) throws Exception {
        SecurityMenu menu = iSecurityMenuDao.findByParentId(id, SecurityMenu.FIELDS.ID);
        if (menu != null) {
            return MxResult.create(SECURITY_MENU_HAS_CHILD_NOT_DELETE);
        }
        return MxResult.result(iSecurityMenuDao.delete(id));
    }

    @Override
    public MxResult roleList(String menuId, String name, PageBean pageBean) throws Exception {
        String resourceId = StringUtils.defaultIfBlank(userHandler.buildResourceId(ResourceType.ROLE), config.client());
        IResultSet<SecurityMenuRoleVO> resultData = iSecurityMenuRoleDao.findAll(menuId, resourceId, name, pageBean.toPage());
        return MxResult.ok().data(Pages.create(resultData));
    }

    @Override
    @OperationLog(operationType = OperationType.CREATE, title = "添加菜单角色")
    public MxResult roleCreate(String menuId, String roleId) throws Exception {
        String resourceId = StringUtils.defaultIfBlank(userHandler.buildResourceId(ResourceType.ROLE), config.client());
        SecurityMenuRole menuRole = iSecurityMenuRoleDao.findByMenuIdAndRoleIdAndClient(menuId, roleId, config.client());
        if (menuRole != null) {
            return MxResult.create(SECURITY_MENU_ROLE_EXISTS);
        }
        menuRole = SecurityMenuRole.builder()
                .id(UUIDUtils.UUID())
                .resourceId(resourceId)
                .menuId(menuId)
                .roleId(roleId)
                .client(config.client())
                .createUser(SaUtil.loginId())
                .createTime(DateTimeUtils.currentTimeMillis())
                .build();
        menuRole = iSecurityMenuRoleDao.create(menuRole);
        return MxResult.result(menuRole);
    }

    @Override
    @OperationLog(operationType = OperationType.DELETE, title = "删除菜单角色")
    public MxResult roleDelete(String[] ids) throws Exception {
        return MxResult.result(iSecurityMenuRoleDao.deleteByIds(ids));
    }
}
