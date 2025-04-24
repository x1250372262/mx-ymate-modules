package com.mx.ymate.security.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.mvc.i18n.I18nHelper;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.annotation.OperationLog;
import com.mx.ymate.security.base.bean.SecurityMenuBean;
import com.mx.ymate.security.base.enums.MenuType;
import com.mx.ymate.security.base.enums.OperationType;
import com.mx.ymate.security.base.model.SecurityMenu;
import com.mx.ymate.security.base.vo.SecurityMenuListVO;
import com.mx.ymate.security.base.vo.SecurityMenuNavVO;
import com.mx.ymate.security.base.vo.SecurityMenuVO;
import com.mx.ymate.security.base.vo.SecurityPermissionVO;
import com.mx.ymate.security.web.dao.ISecurityMenuDao;
import com.mx.ymate.security.web.service.ISecurityMenuService;
import com.mx.ymate.security.web.service.ISecurityUserRoleService;
import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mx.ymate.security.base.code.SecurityCode.SECURITY_MENU_HAS_CHILD_NOT_DELETE;


/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Bean
public class SecurityMenuServiceImpl implements ISecurityMenuService {

    @Inject
    private ISecurityMenuDao iSecurityMenuDao;
    @Inject
    private ISecurityUserRoleService iSecurityUserRoleService;

    private final ISecurityConfig config = Security.get().getConfig();


    @Override
    public MxResult nav() throws Exception {
        List<String> permissionList = iSecurityUserRoleService.securityUserPermissionList(SaUtil.loginId());
        List<SecurityMenuNavVO> navList = navList(SaUtil.loginId(), permissionList, SaUtil.isFounder());
        return MxResult.ok().data(navList);
    }

    @Override
    public List<SecurityMenuNavVO> navList(String userId, List<String> permissionList, boolean isFounder) throws Exception {
        //取出来这个人所有的菜单
        List<SecurityMenuNavVO> menuAllList = iSecurityMenuDao.findAll(null, Constants.BOOL_FALSE, config.client()).getResultData();
        List<SecurityMenuNavVO> resultList = BeanUtil.copyList(menuAllList, SecurityMenuNavVO::new, (s, t) -> {
            t.setName(I18nHelper.getMsg(s.getI18nKey(), s.getName()));
        });
        if (isFounder) {
            return resultList;
        }
        //不是总管理的前提下
        //首先是公开的
        List<SecurityMenuNavVO> permissionMenuList = resultList.stream().filter(m -> Objects.equals(MenuType.PUBLIC.value(), m.getType())).collect(Collectors.toList());
        List<SecurityMenuNavVO> defaultPermissionsList = resultList.stream().filter(m -> Objects.equals(MenuType.DEFAULT.value(), m.getType())).collect(Collectors.toList());
        if (permissionList.isEmpty()) {
            return permissionMenuList;
        }
        for (SecurityMenuNavVO securityMenuNavVO : defaultPermissionsList) {
            //获取用户所有的权限
            String permission = securityMenuNavVO.getPermission();
            //如果菜单不需要权限  或者 用户有这个权限
            boolean flag = (StringUtils.isBlank(permission)) || (StringUtils.isNotBlank(permission) && permissionList.contains(permission));
            if (flag) {
                permissionMenuList.add(securityMenuNavVO);
            }
        }
        return permissionMenuList;
    }

    private List<SecurityMenuListVO> createTree(List<SecurityMenuNavVO> resultSet, List<SecurityMenuNavVO> allCategory) {
        List<SecurityMenuListVO> treeList = new ArrayList<>();
        if (CollUtil.isEmpty(resultSet)) {
            return treeList;
        }
        resultSet.forEach(menuNavVO -> {
            SecurityMenuListVO treeVO = new SecurityMenuListVO();
            treeVO.setText(I18nHelper.getMsg(menuNavVO.getI18nKey(),menuNavVO.getName()));
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
        List<SecurityMenuNavVO> menuNavVOList = iSecurityMenuDao.findAll(null, null, config.client()).getResultData();
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
        SecurityMenu menu = BeanUtil.copy(menuBean, SecurityMenu::new);
        long time = System.currentTimeMillis();
        String userId = SaUtil.loginId();
        menu.setId(UUIDUtils.UUID());
        menu.setClient(config.client());
        menu.setCreateTime(time);
        menu.setCreateUser(userId);
        menu.setLastModifyTime(time);
        menu.setLastModifyUser(userId);
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
        menu.setLastModifyTime(System.currentTimeMillis());
        menu.setLastModifyUser(SaUtil.loginId());
        menu = iSecurityMenuDao.update(menu, SecurityMenu.FIELDS.PARENT_ID, SecurityMenu.FIELDS.NAME, SecurityMenu.FIELDS.ICON,
                SecurityMenu.FIELDS.PATH, SecurityMenu.FIELDS.URL, SecurityMenu.FIELDS.SORT, SecurityMenu.FIELDS.TYPE,
                SecurityMenu.FIELDS.HIDE_STATUS, SecurityMenu.FIELDS.PERMISSION, SecurityMenu.FIELDS.LAST_MODIFY_TIME, SecurityMenu.FIELDS.LAST_MODIFY_USER);
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

}
