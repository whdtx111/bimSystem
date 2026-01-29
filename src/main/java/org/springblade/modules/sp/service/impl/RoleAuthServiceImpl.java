package org.springblade.modules.sp.service.impl;



import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.RedisUtil;
import org.springblade.modules.sp.dto.RoleAuthStreamDTO;
import org.springblade.modules.sp.entity.RoleAuth;
import org.springblade.modules.sp.mapper.RoleAuthMapper;
import org.springblade.modules.sp.service.RoleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@DS("postgresql")
@Slf4j
public class RoleAuthServiceImpl extends BaseServiceImpl<RoleAuthMapper, RoleAuth> implements RoleAuthService {

   @Autowired
   private RoleAuthMapper roleAuthMapper;
    @Autowired
    RedisUtil redisUtil;

   @Override
    public List<RoleAuth> getRoleAuthList() {
        try {
            return roleAuthMapper.getRoleAuthList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RoleAuth getRoleAuthByAuthId(String authId) {
        try {
            return roleAuthMapper.getRoleAuthByAuthId(authId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<RoleAuth> getRoleAuthByRoleId(String roleId) {
        try {
            return roleAuthMapper.getRoleAuthByRoleId(roleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<RoleAuth> getRoleAuthByUserId(String userId,String streamId) {
        try {
            return roleAuthMapper.getRoleAuthByUserId(userId,streamId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<RoleAuthStreamDTO> getRoleAuthNoStream(String userId) {
        try {
            return roleAuthMapper.getRoleAuthNoStream(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addRoleAuth(RoleAuth roleAuth) {
        try {
            return roleAuthMapper.addRoleAuth(roleAuth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateRoleAuth(RoleAuth roleAuth) {
        try {
            return roleAuthMapper.updateRoleAuth(roleAuth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteRoleAuth(String roleId) {
        try {
            return roleAuthMapper.deleteRoleAuth(roleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }




}
