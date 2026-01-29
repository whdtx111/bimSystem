package org.springblade.modules.sp.service.impl;

import com.aliyun.oss.ServiceException;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.SLRole;
import org.springblade.modules.sp.mapper.RoleAuthMapper;
import org.springblade.modules.sp.mapper.SLRoleMapper;
import org.springblade.modules.sp.mapper.UsersMapper;
import org.springblade.modules.sp.service.SLRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@DS("postgresql")
@Slf4j
public class SLRoleServiceImpl extends BaseServiceImpl<SLRoleMapper, SLRole> implements SLRoleService {

    @Autowired
    private SLRoleMapper roleMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private RoleAuthMapper roleAuthMapper;

    @Override
    public List<SLRole> getRoleList(String streamId) {
        try {
            return roleMapper.getRoleList(streamId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SLRole getRoleById(String roleId) {
        try {
            return roleMapper.getRoleById(roleId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SLRole getAdmin() {
        try {
            return roleMapper.getAdmin();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SLRole getRoleByStreamIdAndRoleName(String streamId, String roleName) {
        try {
            return roleMapper.getRoleByStreamIdAndRoleName(streamId, roleName);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addRole(SLRole role) {
        try {
            return roleMapper.addRole(role);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateRole(SLRole role) {
        try {
            return roleMapper.updateRole(role);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRole(String roleId) {
        try {
            return roleMapper.deleteRole(roleId);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteStreamRolesAndAuth(String streamId) {
        try {
            // 1. 更新用户表中的roleIds
            usersMapper.removeRoleIdsByStreamId(streamId);

            // 2. 删除角色权限关联
            roleAuthMapper.deleteRoleAuthByRoleIds(streamId);

            // 3. 删除角色
            roleMapper.deleteRolesByStreamId(streamId);

            return true;
        } catch (Exception e) {
            log.error("删除失败", e);
            throw new ServiceException("删除失败");
        }
    }

}
