package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.dto.RoleAuthStreamDTO;
import org.springblade.modules.sp.entity.Ebs;
import org.springblade.modules.sp.entity.RoleAuth;
import org.springblade.modules.system.entity.Role;

import java.util.List;
@Mapper
public interface RoleAuthMapper extends BaseMapper<RoleAuth> {

    List<RoleAuth> getRoleAuthList();

    RoleAuth getRoleAuthByAuthId(@Param("authId") String authId);

    List<RoleAuth> getRoleAuthByRoleId(@Param("roleId") String roleId);

    List<RoleAuth> getRoleAuthByUserId(@Param("userId") String userId,@Param("streamId") String streamId);

    List<RoleAuthStreamDTO> getRoleAuthNoStream(@Param("userId") String userId);

    boolean addRoleAuth(RoleAuth roleAuth);

    boolean updateRoleAuth(RoleAuth roleAuth);

    boolean deleteRoleAuth(String roleId);

    boolean deleteRoleAuthByRoleIds(String roleIds);

}
