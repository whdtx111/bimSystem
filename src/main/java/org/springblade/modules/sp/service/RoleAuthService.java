package org.springblade.modules.sp.service;

import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.dto.RoleAuthStreamDTO;
import org.springblade.modules.sp.entity.RoleAuth;
import org.springblade.modules.system.entity.Role;

import java.util.List;


public interface RoleAuthService extends BaseService<RoleAuth> {


    List<RoleAuth> getRoleAuthList();

    RoleAuth getRoleAuthByAuthId(@Param("authId") String authId);

    List<RoleAuth> getRoleAuthByRoleId(@Param("roleId") String roleId);

    List<RoleAuth> getRoleAuthByUserId(@Param("userId") String userId,@Param("streamId") String streamId);

    List<RoleAuthStreamDTO> getRoleAuthNoStream(@Param("userId") String userId);

    boolean addRoleAuth(RoleAuth roleAuth);

    boolean updateRoleAuth(RoleAuth roleAuth);

    boolean deleteRoleAuth(String roleId);

}
