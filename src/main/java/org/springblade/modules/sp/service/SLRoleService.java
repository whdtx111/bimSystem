package org.springblade.modules.sp.service;

import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.SLRole;

import java.util.List;


public interface SLRoleService extends BaseService<SLRole> {

    List<SLRole> getRoleList(String streamId);

    SLRole getRoleById(String roleId);

    SLRole getAdmin();

    SLRole getRoleByStreamIdAndRoleName(String streamId, String roleName);

    boolean addRole(SLRole role);

    boolean updateRole(SLRole role);

    boolean deleteRole(String roleId);

    boolean deleteStreamRolesAndAuth(String streamId);
}
