package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.RoleAuthStreamDTO;
import org.springblade.modules.sp.entity.*;
import org.springblade.modules.sp.service.*;
import org.springblade.modules.sp.vo.RoleAuthDetailVO;
import org.springblade.modules.sp.vo.RoleAuthVO;
import org.springblade.modules.sp.vo.UserAuthVO;
import org.springblade.modules.sp.vo.UserWithRoleNameVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/role")
@Api(value = "夏三项目相关控制器", tags = "夏三项目相关控制器")
public class SmRoleController extends BladeController {

    @Autowired
    private RoleAuthService roleAuthService;
    @Autowired
    private SLRoleService slRoleService;
    @Autowired
    private StreamUsersService streamUsersService;
    @Autowired
    private IUsersService iUsersService;
    @Autowired
    private StreamAclService streamAclService;
    @Autowired
    private IUsersService usersService;
//    ----------------新增角色---------------------

    @SneakyThrows
    @PostMapping("/addSLRole.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addSLRole", notes = "addSLRole")
    public R<String> addSLRole(@RequestBody SLRole slRole) {
        try {
            slRole.setDescription("");
            slRole.setModifyTime(new Date());
            if (slRole.getStatus() == null) {
                slRole.setStatus(0);
            }
            boolean b = slRoleService.addRole(slRole);
            if (b) {
                return R.data(slRole.getRoleId());
            }else {
                return R.fail("添加角色失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateSLRole.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateSLRole", notes = "updateSLRole")
    public R<String> updateSLRole(@RequestBody SLRole slRole) {
        try {
            slRole.setModifyTime(new Date());
            boolean b = slRoleService.updateRole(slRole);
            if (!b) {
                return R.fail("更新角色失败");
            }
            return R.success("更新角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getRoleList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRoleList", notes = "getRoleList")
    public R<List<SLRole>> getRoleList(@RequestParam String streamId) {
        try {
            return R.data(slRoleService.getRoleList(streamId));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getRoleById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRoleById", notes = "getRoleById")
    public R<SLRole> getRoleById(@RequestParam String id) {
        try {
            return R.data(slRoleService.getRoleById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteRoleById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteRoleById", notes = "deleteRoleById")
    public R<String> deleteRoleById(@RequestParam String id) {
        try {
            boolean b = slRoleService.deleteRole(id);
            if (!b) {
                return R.fail("删除角色失败");
            }
            return R.success("删除角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

//    自动配置超级管理员(通过owner)

    @SneakyThrows
    @PostMapping("/checkIsOwner.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "checkIsOwner", notes = "checkIsOwner")
    public R<String> checkIsOwner(@RequestParam String userId, @RequestParam String streamId) {
        try {
            StreamAcl streamAcl = streamAclService.selectSpRole(userId, streamId);
            if (streamAcl != null && streamAcl.getRole().equals("stream:owner")) {
                // 预置角色数据
                createRoleWithAuth("超级管理员", 2, "最高权限", streamId, userId, true);
                createRoleWithAuth("管理员", 1, "管理权限", streamId, null, true);
                createRoleWithAuth("项目成员", 1, "编辑权限", streamId, null, false);
                createRoleWithAuth("外部人员", 1, "查看权限", streamId, null, false);
                return R.data("预置成功！");
            }else {
                return R.data("预置失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            R.fail("File uploaded fail" + e.getMessage());
            return R.data(e.getMessage());
        }
    }

    /**
     * 创建角色并设置权限
     */
    private void createRoleWithAuth(String roleName, Integer status, String description,
                                    String streamId, String userId, boolean isAdmin) {
        SLRole role = new SLRole();
        role.setRoleName(roleName);
        role.setStatus(status);
        role.setStreamId(streamId);
        role.setModifyUser("system");
        role.setModifyTime(new Date());
        role.setDescription(description);

        if (slRoleService.addRole(role)) {
            // 如果需要绑定用户
            if (userId != null) {
                Users byId = iUsersService.getById(userId);
                String[] roleIds = byId.getRoleIds();
                roleIds = ArrayUtils.add(roleIds, role.getRoleId());
                iUsersService.updateRole(userId, roleIds);
            }

            // 设置权限
            RoleAuthVO roleAuthVO = new RoleAuthVO();
            roleAuthVO.setRoleId(role.getRoleId());
            roleAuthVO.setModifyUser("system");

            ArrayList<RoleAuthDetailVO> authList = new ArrayList<>();
            // 根据角色名称设置权限等级
            String authLevel;
            if (isAdmin) {
                authLevel = "2";  // 超级管理员和管理员
            } else if (roleName.equals("项目成员")) {
                authLevel = "1";  // 项目成员可查看
            } else {
                authLevel = "0";  // 外部人员无权限
            }

            addAuthDetail(authList, "projectSpace", authLevel);
            addAuthDetail(authList, "dataTemplate", authLevel);
            addAuthDetail(authList, "pluginApplication", authLevel);
            addAuthDetail(authList, "permissionMgt", authLevel);

            roleAuthVO.setRoleAuthDetailVOList(authList);
            addOrUpdateRoleAuth(roleAuthVO);
        }
    }

    /**
     * 添加权限详情
     */
    private void addAuthDetail(List<RoleAuthDetailVO> authList, String authName, String authLevel) {
        RoleAuthDetailVO authDetail = new RoleAuthDetailVO();
        authDetail.setAuthName(authName);
        authDetail.setAuthLevel(authLevel);
        authList.add(authDetail);
    }

//    -------------角色权限管理-----------------

    @SneakyThrows
    @GetMapping("/getRoleAuthByAuthId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRoleAuthByAuthId", notes = "getRoleAuthByAuthId")
    public R<RoleAuth> getRoleAuthByAuthId(@RequestParam String id) {
        try {
            return R.data(roleAuthService.getRoleAuthByAuthId(id));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getRoleAuthByRoleId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRoleAuthByRoleId", notes = "getRoleAuthByRoleId")
    public R<List<RoleAuth>> getRoleAuthByRoleId(@RequestParam String roleId) {
        try {
            return R.data(roleAuthService.getRoleAuthByRoleId(roleId));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getRoleAuthByUserId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRoleAuthByUserId", notes = "getRoleAuthByUserId")
    public R<List<RoleAuth>> getRoleAuthByUserId(@RequestParam Optional<String> userId,@RequestParam Optional<String> streamId) {
        try {
            return R.data(roleAuthService.getRoleAuthByUserId(userId.orElse(null),streamId.orElse(null)));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getRoleAuthList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRoleAuthList", notes = "getRoleAuthList")
    public R<List<RoleAuth>> getRoleAuthList() {
        try {
            return R.data(roleAuthService.getRoleAuthList());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }



    @SneakyThrows
    @PostMapping("/addOrUpdateRoleAuth.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addOrUpdateRoleAuth", notes = "addOrUpdateRoleAuth")
    public R<List<String>> addOrUpdateRoleAuth(@RequestBody RoleAuthVO roleAuthVO) {
        try {
            boolean b = false;
            List<String> resId = new ArrayList<>();
            List<RoleAuth> roleAuthList = roleAuthService.getRoleAuthByRoleId(roleAuthVO.getRoleId());
            List<RoleAuthDetailVO> voList = roleAuthVO.getRoleAuthDetailVOList();
            if (roleAuthList != null && roleAuthList.size() > 0){
                for (RoleAuth roleAuth : roleAuthList) {
                    for (RoleAuthDetailVO roleAuthDetailVO : voList) {
                        if (roleAuth.getAuthName().equals(roleAuthDetailVO.getAuthName())) {
                            roleAuth.setAuthLevel(roleAuthDetailVO.getAuthLevel());
                            roleAuth.setModifyUser(roleAuthVO.getModifyUser());
                            roleAuth.setModifyTime(new Date());
                            if (roleAuth.getStatus() == null){
                                roleAuth.setStatus(0);
                            }
                            b = roleAuthService.updateRoleAuth(roleAuth);
                            resId.add(roleAuth.getAuthId());
                        }
                    }
                    }
                }else {
                for (RoleAuthDetailVO roleAuthDetailVO : voList) {
                RoleAuth roleAuth = new RoleAuth();
                roleAuth.setRoleId(roleAuthVO.getRoleId());
                roleAuth.setAuthName(roleAuthDetailVO.getAuthName());
                roleAuth.setAuthLevel(roleAuthDetailVO.getAuthLevel());
                    roleAuth.setModifyUser(roleAuthVO.getModifyUser());
                    roleAuth.setModifyTime(new Date());
                    if (roleAuth.getStatus() == null){
                        roleAuth.setStatus(0);
                    }
                b = roleAuthService.addRoleAuth(roleAuth);
                resId.add(roleAuth.getAuthId());
            }
                }
            if (b){
                return R.data(resId);
            }else {
                return R.fail("增改角色权限失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteRoleAuth.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteRoleAuth", notes = "deleteRoleAuth")
    public R<String> deleteRoleAuth(@RequestParam String id) {
        try {
            boolean b = roleAuthService.deleteRoleAuth(id);
            if (!b) {
                return R.fail("删除角色权限失败");
            }
            return R.success("删除角色权限成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }


//    ---------------用户项目管理----------------

    @SneakyThrows
    @GetMapping("/getByStreamId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getByStreamId", notes = "getByStreamId")
    public R<StreamUsers> getByStreamId(@RequestParam String streamId) {
        try {
            return R.data(streamUsersService.getByStreamId(streamId));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getByUserId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getByUserId", notes = "getByUserId")
    public R<StreamUsers> getByUserId(@RequestParam String userId) {
        try {
            return R.data(streamUsersService.getByUserId(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addStreamUsers.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addStreamUsers", notes = "addStreamUsers")
    public R<String> addStreamUsers(@RequestBody StreamUsers streamUsers) {
        try {
            boolean b = streamUsersService.addstreamUsers(streamUsers);
            if (b){
                return R.data(streamUsers.getId());
            }else {
                return R.fail("添加用户项目失败");   
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateStreamUsers.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateStreamUsers", notes = "updateStreamUsers")
    public R<String> updateStreamUsers(@RequestBody StreamUsers streamUsers) {
        try {
            boolean b = streamUsersService.updateStreamUsers(streamUsers);
            if (!b) {
                return R.fail("更新用户项目失败");
            }
            return R.success("更新用户项目成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteStreamUsers.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteStreamUsers", notes = "deleteStreamUsers")
    public R<String> deleteStreamUsers(@RequestParam String id) {
        try {
            boolean b = streamUsersService.deleteStreamUsers(id);
            if (b){
                return R.success("删除用户项目成功");
            }else {
                return R.fail("删除用户项目失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

//    ----------用户管理相关(StreamAcl/ServerAcl/Users)------------

    @SneakyThrows
    @GetMapping("/getUserByStreamId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getUserByStreamId", notes = "getUserByStreamId")
    public R<List<UserWithRoleNameVO>> getUserByStreamId(@RequestParam String streamId) {
        try {
            List<UserWithRoleNameVO> res = usersService.getUserByStreamIdWithRoleName(streamId);
            return R.data(res);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("File uploaded fail" + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateRole.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateRole", notes = "updateRole")
    public R<String> updateRole(@RequestBody UserAuthVO userAuthVO) {
        try {
            String userId = userAuthVO.getUserId();
            String newRoleId = userAuthVO.getRoleId();

            // 获取用户当前的角色列表
            Users user = usersService.getById(userId);
            String[] currentRoleIds = user.getRoleIds();
            if (currentRoleIds == null) {
                currentRoleIds = new String[0];
            }

            // 获取新角色信息
            SLRole newRole = slRoleService.getRoleById(newRoleId);
            if (newRole == null) {
                return R.fail("新角色不存在");
            }

            // 检查并更新角色数组
            boolean hasUpdated = false;
            List<String> updatedRoleIds = new ArrayList<>();

            for (String currentRoleId : currentRoleIds) {
                SLRole currentRole = slRoleService.getRoleById(currentRoleId);

                // 如果当前角色状态为2，保持不变
                if (currentRole != null && currentRole.getStatus() == 2) {
                    updatedRoleIds.add(currentRoleId);
                    continue;
                }

                // 如果streamId相同，替换为新的roleId
                if (currentRole != null &&
                        currentRole.getStreamId().equals(newRole.getStreamId())) {
                    updatedRoleIds.add(newRoleId);
                    hasUpdated = true;
                } else {
                    updatedRoleIds.add(currentRoleId);
                }
            }

            // 如果没有找到相同streamId的角色，则添加新角色
            if (!hasUpdated) {
                updatedRoleIds.add(newRoleId);
            }

            // 转换回数组并更新
            String[] finalRoleIds = updatedRoleIds.toArray(new String[0]);
            boolean success = usersService.updateRole(userId, finalRoleIds);

            if (success) {
                return R.success("更新角色成功");
            } else {
                return R.fail("更新角色失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("更新角色失败: " + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteStreamRoles.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteStreamRoles", notes = "deleteStreamRoles")
    public R<String> deleteStreamRoles(@RequestParam String streamId) {
        try {
            boolean success = slRoleService.deleteStreamRolesAndAuth(streamId);
            if (success) {
                return R.success("删除成功");
            }else {
                return R.fail("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getRoleAuthNoStream.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRoleAuthNoStream", notes = "getRoleAuthNoStream")
    public R<List<RoleAuthStreamDTO>> getRoleAuthNoStream(@RequestParam String userId) {
        try {
            List<RoleAuthStreamDTO> res = roleAuthService.getRoleAuthNoStream(userId);
            return R.data(res);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("fail" + e.getMessage());
        }
    }
//    -----------验证权限接口-----------

    


}
