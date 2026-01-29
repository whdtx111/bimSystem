package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.SLRole;

import java.util.List;
@Mapper
public interface SLRoleMapper extends BaseMapper<SLRole> {

    List<SLRole> getRoleList(String streamId);

    SLRole getRoleById(String roleId);

    SLRole getAdmin();

    SLRole getRoleByStreamIdAndRoleName(@Param("streamId") String streamId, @Param("roleName") String roleName);

    boolean addRole(SLRole role);

    boolean updateRole(SLRole role);

    boolean deleteRole(String roleId);

    boolean deleteRolesByStreamId(String streamId);

}
