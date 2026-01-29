/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.modules.sp.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.RedisUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.modules.sp.constant.SpConstant;
import org.springblade.modules.sp.dto.RoleAuthStreamDTO;
import org.springblade.modules.sp.entity.*;
import org.springblade.modules.sp.mapper.A4UserMapper;
import org.springblade.modules.sp.mapper.UsersMapper;
import org.springblade.modules.sp.service.*;
import org.springblade.modules.sp.utils.RandomAvatarUtils;
import org.springblade.modules.sp.utils.RsaJwtUtil;
import org.springblade.modules.sp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 服务实现类
 *
 * @author wangc
 * @since 2023-09-13
 */
@Service
@DS("postgresql")
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private IServerAclService serverAclService;
    @Autowired
    IA4UserService ia4UserService;
    @Autowired
    A4UserMapper a4UserMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private RoleAuthService roleAuthService;

    @Value( "${smartlink.auth_local_login_url}")
    private String AUTH_LOCAL_LOGIN_URL;
    @Value( "${smartlink.token_url}")
    private String TOKEN_URL;
    @Value( "${smartlink.register_url}")
    private String REGISTER_URL;
    @Autowired
    private SubscriptionConfigService subscriptionConfigService;
    @Autowired
    private SubscriptionOrderService subscriptionOrderService;
    @Autowired
    private SubscriptionOrderStreamsService subscriptionOrderStreamsService;
    @Autowired
    private StreamAclService streamAclService;
    @Autowired
    private StreamService streamService;
    @Autowired
    private SLRoleService slRoleService;
    @Autowired
    private RsaJwtUtil rsaJwtUtil;

    @Override
    public Users getById(String id) {
        try {
            return usersMapper.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public R<String> getOwnerByTokenId(String tokenId) {
        try {
            return R.data(usersMapper.getOwnerByTokenId(tokenId));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("获取失败");
        }
    }

    @Override
    public R spAuthLogin(String account, String password ,String challenge) {
        // 根据 account 查 email phone
        Users user = this.getOne(
                new LambdaQueryWrapper<Users>().eq(Users::getEmail, account).or().eq(Users::getPhone, account)
        );
        log.info("获取当前登录对象{}",user);
        // 账号或密码不存在
        if (Func.isEmpty(user)) {
            return R.fail(SpConstant.ACCOUNT_ERROR);
        }
        // 发送请求 换code
        String url = AUTH_LOCAL_LOGIN_URL + challenge;
        Map<String, Object> params = new HashMap<>();
        params.put("email", user.getEmail());
        params.put("password", password);
        System.out.println("params" + params);
        String response = HttpUtil.post(url, params);
        log.info("换取code>>>>"+response);
        if (response.contains("access_code")) {
            Pattern pattern = Pattern.compile("access_code=([a-zA-Z0-9]+)");
            Matcher matcher = pattern.matcher(response);
            String accessCode = new String();
            if (matcher.find()) {
                accessCode = matcher.group(1);
            }
            // 发送请求 换token
            Map<String, Object> tokenparams = new HashMap<>();
            tokenparams.put("accessCode", accessCode);
            tokenparams.put("appId", SpConstant.APPID_SPKLWEBAPP);
            tokenparams.put("appSecret", SpConstant.APPSECRET_SPKLWEBAPP);
            tokenparams.put("challenge", challenge);
            String token = HttpUtil.post(TOKEN_URL, tokenparams);
            log.info("token>>>>"+token);
            if (!token.contains("token")) {
                // 无效的code凭证
                return R.fail(SpConstant.TOKEN_CODE_ERROR);
            }
            // 获取用户角色并添加到token中
            List<RoleAuthStreamDTO> roleAuthNoStream = roleAuthService.getRoleAuthNoStream(user.getId());
            JSONObject tokenJson = JSONObject.parseObject(token);
            tokenJson.put("authRole", roleAuthNoStream != null ? roleAuthNoStream : new ArrayList<>());
            tokenJson.put("user",user);
            String jwt = rsaJwtUtil.generateToken(user, roleAuthNoStream);
            tokenJson.put("jwt", jwt);
            String newToken = tokenJson.toJSONString();
            // 缓存token
            authUserRedis(newToken, user.getId());
            return R.data(newToken);
        } else {
            // 账号或密码不存在
            return R.fail(SpConstant.ACCOUNT_ERROR);
        }
    }

    @Override
    public R pluginAuthLogin(String accessCode,String challenge) {
        try {
            if (StringUtils.isEmpty(accessCode)) {
                return R.fail("accessCode为空");
            }

            // 发送请求换token
            Map<String, Object> tokenparams = new HashMap<>();
            tokenparams.put("accessCode", accessCode);
            tokenparams.put("appId", SpConstant.APPID_PLUGIN);
            tokenparams.put("appSecret", SpConstant.APPSECRET_PLUGIN);
            tokenparams.put("challenge", challenge);
            String token = HttpUtil.post(TOKEN_URL, tokenparams);
            log.info("token>>>>" + token);

            if (StringUtil.isEmpty(token) || !token.contains("token")) {
                return R.fail(SpConstant.TOKEN_CODE_ERROR);
            }

            try {
                // 解析token并获取用户信息
                JSONObject tokenObj = JSONObject.parseObject(token);
                String tokenValue = tokenObj.getString("token");
                String tokenId = tokenValue.substring(0, 10);
                String userId = usersMapper.getOwnerByTokenId(tokenId);
                Users user = this.getById(userId);
                if (user == null) {
                    return R.fail("用户不存在");
                }

                // 获取用户角色
                List<RoleAuthStreamDTO> roleAuthNoStream = roleAuthService.getRoleAuthNoStream(userId);

                // 获取用户角色并添加到token中
                JSONObject tokenJson = JSONObject.parseObject(token);
                tokenJson.put("authRole", roleAuthNoStream != null ? roleAuthNoStream : new ArrayList<>());
                tokenJson.put("user",user);
                String jwt = rsaJwtUtil.generateToken(user, roleAuthNoStream);
                tokenJson.put("jwt", jwt);
                String newToken = tokenJson.toJSONString();
                // 缓存token
                authUserRedis(newToken, user.getId());

                // 构建插件端返回结构
                JSONObject pluginToken = JSONObject.parseObject(token);

                // 添加服务器信息
                ServerInfoVO serverInfoVO = new ServerInfoVO();
                pluginToken.put("serverInfo", serverInfoVO);

                // 添加用户信息
                UserInfoVO userInfoVO = new UserInfoVO();
                userInfoVO.setId(user.getId());
                userInfoVO.setName(user.getName());
                userInfoVO.setEmail(user.getEmail());
                userInfoVO.setCompany(user.getCompany());
                userInfoVO.setAvatar(user.getAvatar());
                userInfoVO.setStreams(new JSONObject().fluentPut("totalCount", 0));
                userInfoVO.setCommits(new JSONObject().fluentPut("totalCount", 0));
                pluginToken.put("userInfo", userInfoVO);

                pluginToken.put("jwt", jwt);


                return R.data(pluginToken.toJSONString());

            } catch (Exception e) {
                log.error("Token解析失败: " + e.getMessage());
                return R.fail("Token格式错误");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("登录失败");
        }
    }

    @Override
    public R spLogOut(String token) {
        redisUtil.del(token);
        return R.status(true);
    }

    @Override
    public R<String> spRegister(String email, String name, String password, String company, String phone,String challenge) {
        long count = this.count(
                new LambdaQueryWrapper<Users>().eq(Users::getEmail, email).or().eq(Users::getPhone, phone)
        );
        if (count > 0){
            return R.fail("邮箱或者手机号重复注册!");
        }
        Map<String, Object> tokenparams = new HashMap<>();
        tokenparams.put("email", email);
        tokenparams.put("company", company);
        tokenparams.put("password", password);
        tokenparams.put("name",name);
        String token = HttpUtil.post(REGISTER_URL + "?challenge=" + challenge, tokenparams);
        // 随机头像
        Map<String, String> base64StrMap = RandomAvatarUtils.getRandomAvatarBase64Str();
        String key = new String();
        String value = new String();
        if (!base64StrMap.isEmpty()) {
                key = base64StrMap.keySet().iterator().next();
                value = base64StrMap.get(key);
            }
        if (token.contains("?access_code")) {
            LambdaUpdateWrapper<Users> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            //eq是指你查询的条件，set是指你修改的值
            lambdaUpdateWrapper
                    .eq(Users::getEmail, email)
                    .set(Users::getPhone, phone)
                    .set(Users::getAvatar,value);
            this.update(null, lambdaUpdateWrapper);
            
            // 注册成功后自动绑定默认订阅套餐
            try {
                // 通过email获取刚注册的用户ID
                Users newUser = this.getOne(new LambdaQueryWrapper<Users>().eq(Users::getEmail, email));
                if (newUser != null) {
                    // 创建订阅套餐订单
                    SubOrderVO subOrderVO = new SubOrderVO();
                    subOrderVO.setUserId(newUser.getId());
                    subOrderVO.setConfigId("01f77ada-a591-4b68-a6e1-8b6df4507e5d"); // 默认试用版configId
                    subOrderVO.setOrderTime(new Date()); // 当前时间戳
                    
                    // 调用订阅套餐添加方法
                    R<String> subscriptionResult = createSubscriptionOrder(subOrderVO);
                    if (!subscriptionResult.isSuccess()) {
                        log.warn("用户注册成功但绑定订阅套餐失败: userId={}, error={}", newUser.getId(), subscriptionResult.getMsg());
                    } else {
                        log.info("用户注册成功并自动绑定订阅套餐: userId={}, orderId={}", newUser.getId(), subscriptionResult.getData());
                    }
                }
            } catch (Exception e) {
                log.error("注册后自动绑定订阅套餐失败", e);
                // 不影响注册流程，只记录错误
            }
            
            // 注册成功后自动添加streamId为b1d4251e75的权限和角色
            try {
                // 通过email获取刚注册的用户ID
                Users newUser = this.getOne(new LambdaQueryWrapper<Users>().eq(Users::getEmail, email));
                if (newUser != null) {
                    // 1. 添加stream_acl权限记录
                    boolean aclResult = streamAclService.addStreamAcl(
                        newUser.getId(), 
                        "b1d4251e75",
                        "stream:contributor"
                    );
                    if (!aclResult) {
                        log.warn("用户注册成功但添加stream_acl权限失败: userId={}", newUser.getId());
                    } else {
                        log.info("用户注册成功并自动添加stream_acl权限: userId={}, resourceId=b1d4251e75, role=stream:contributor", newUser.getId());
                    }
                    
                    // 2. 查询sl_role表，找到streamId为39f5a0b513且roleName为管理员的记录
                    SLRole adminRole = slRoleService.getRoleByStreamIdAndRoleName("39f5a0b513", "管理员");
                    if (adminRole != null) {
                        // 3. 将该角色的roleId添加到用户的roleIds字段
                        String[] currentRoleIds = newUser.getRoleIds();
                        String[] newRoleIds;
                        
                        if (currentRoleIds == null || currentRoleIds.length == 0) {
                            // 如果当前没有角色，直接创建新数组
                            newRoleIds = new String[]{adminRole.getRoleId()};
                        } else {
                            // 如果已有角色，添加到数组中
                            newRoleIds = new String[currentRoleIds.length + 1];
                            System.arraycopy(currentRoleIds, 0, newRoleIds, 0, currentRoleIds.length);
                            newRoleIds[currentRoleIds.length] = adminRole.getRoleId();
                        }
                        
                        // 4. 更新用户的roleIds
                        boolean updateResult = this.updateRole(newUser.getId(), newRoleIds);
                        if (!updateResult) {
                            log.warn("用户注册成功但更新roleIds失败: userId={}, roleId={}", newUser.getId(), adminRole.getRoleId());
                        } else {
                            log.info("用户注册成功并自动添加管理员角色: userId={}, roleId={}, roleName={}", 
                                newUser.getId(), adminRole.getRoleId(), adminRole.getRoleName());
                        }
                    } else {
                        log.warn("未找到streamId=b1d4251e75且roleName=管理员的角色记录");
                    }
                }
            } catch (Exception e) {
                log.error("注册后自动添加stream_acl权限和角色失败", e);
                // 不影响注册流程，只记录错误
            }
        }
        // 正则返回 链接
        Pattern pattern = Pattern.compile("href=\"(.*?)\"");
        Matcher matcher = pattern.matcher(token);
        String link = new String();
        if (matcher.find()) {
            link = matcher.group(1);
        }
        return R.data(link);
    }

    @Override
    public R spRegisterAll() {
        List<A4User> list = ia4UserService.list();
        for (A4User a4User : list) {
            Map<String, Object> tokenparams = new HashMap<>();
            tokenparams.put("email", a4User.getSnebPhone() + SpConstant.SP_EMAIL_SUFFIX);
            tokenparams.put("ompany", SpConstant.COMPANY_FLAG);
            tokenparams.put("password", SpConstant.INIT_PASSWORD);
            tokenparams.put("name", a4User.getSnebName());
            String token = HttpUtil.post(REGISTER_URL + "?challenge=" + IdUtil.nanoId(10), tokenparams);
            // 随机头像
            Map<String, String> base64StrMap = RandomAvatarUtils.getRandomAvatarBase64Str();
            String key = new String();
            String value = new String();
            if (!base64StrMap.isEmpty()) {
                key = base64StrMap.keySet().iterator().next();
                value = base64StrMap.get(key);
            }
            if (token.contains("?access_code")) {
                LambdaUpdateWrapper<Users> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                //eq是指你查询的条件，set是指你修改的值
                lambdaUpdateWrapper
                        .eq(Users::getEmail, a4User.getSnebPhone() + SpConstant.SP_EMAIL_SUFFIX)
                        .set(Users::getPhone, a4User.getSnebPhone())
                        .set(Users::getCode, a4User.getSnebUserCode())
                        .set(Users::getAvatar,value);
                ;
                this.update(null, lambdaUpdateWrapper);
            }
        }
        return R.data("操作成功!");
    }

    @Override
    public R RandomAvatarAll() {
        List<Users> list = this.list();
        for (Users u : list) {
            // 随机头像
            Map<String, String> base64StrMap = RandomAvatarUtils.getRandomAvatarBase64Str();
            String key = new String();
            String value = new String();
            if (!base64StrMap.isEmpty()) {
                key = base64StrMap.keySet().iterator().next();
                value = base64StrMap.get(key);
            }
            u.setAvatar(value);
            LambdaUpdateWrapper<Users> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper
                    .eq(Users::getId, u.getId())
                    .set(Users::getAvatar,value);
            ;
            this.update(null, lambdaUpdateWrapper);
        }
        return R.success("操作成功");
    }

    @Override
    public R<List<SearchUserVO>> userByPhoneName(String str) {
//        List<SearchUserVO> searchUserVOList = new ArrayList<>();
//        List<Users> list = this.list(
//                new LambdaQueryWrapper<Users>()
//                        .like(Users::getEmail, str)
//                        .or()
//                        .like(Users::getPhone, str)
//                        .or()
//                        .like(Users::getName, str)
//        );
//        if (Func.isNotEmpty(list)){
//            for (Users users : list) {
//                SearchUserVO searchUserVO = new SearchUserVO();
//                searchUserVO.setId(users.getId());
//                searchUserVO.setName(users.getName());
//                searchUserVO.setAvatar(users.getAvatar());
//                searchUserVO.setBio(users.getBio());
//                searchUserVO.setCompany(users.getCompany());
//                searchUserVO.setVerified(users.getVerified());
//                String role = serverAclService.getOne(
//                        new LambdaQueryWrapper<ServerAcl>().eq(ServerAcl::getUserId, users.getId())
//                ).getRole();
//                searchUserVO.setRole(role);
//                searchUserVOList.add(searchUserVO);
//            }
//        }

        return R.data(baseMapper.getUserByPhonOrEmailOrName(str));
    }

    @Override
    public R importUser() {
        List<A4User> listA4User = a4UserMapper.getListA4User();
        for (A4User a4User : listA4User) {
            a4User.setId(null);
        }
        ia4UserService.saveBatch(listA4User);
        System.out.println("成功");
        return null;
    }


    // user 和auth信息缓存
    public void authUserRedis(String token, String userId) {
        JSONObject jsonObject = JSONObject.parseObject(token);
        jsonObject.put("userId", userId);
        // 统一以 JSON 字符串形式写入 Redis，避免 JDK 序列化导致读取乱码
        redisUtil.set(jsonObject.get("token").toString(), jsonObject.toJSONString());
    }

    @Override
    public Users getUserByToken(String token) {
        if (StringUtil.isEmpty(token)) {
            return null;
        }
        String authHeader = token.trim();
        try {
            if (authHeader.length() >= 7 && authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
                String jwt = authHeader.substring(7).trim();
                if (StringUtil.isEmpty(jwt)) {
                    return null;
                }
                Claims claims = rsaJwtUtil.parseToken(jwt);
                if (claims == null) {
                    return null;
                }
                String userId = claims.get("userId", String.class);
                if (StringUtil.isEmpty(userId)) {
                    userId = claims.getSubject();
                }
                if (StringUtil.isEmpty(userId)) {
                    return null;
                }
                return this.getById(userId);
            }

            Object value = redisUtil.get(authHeader);
            if (value == null) {
                return null;
            }

            // 将Redis中的对象安全地转换为JSONObject，兼容多种存储格式
            JSONObject jsonObject = convertTokenObject(value);
            if (jsonObject == null) {
                return null;
            }

            // 直接从JSONObject中获取user对象
            JSONObject userJson = jsonObject.getJSONObject("user");
            if (userJson == null) {
                // 如果没有user对象，尝试获取userId
                String userId = jsonObject.getString("userId");
                return StringUtil.isEmpty(userId) ? null : this.getById(userId);
            }

            // 将JSONObject转换为Users对象
            return userJson.toJavaObject(Users.class);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return null;
        }
    }

    /**
     * 兼容性地将 Redis 中读取到的 token 对象转换为 JSONObject。
     * 支持以下几种情况：
     * 1. 直接是 JSONObject
     * 2. 是 JSON 字符串
     * 3. 是 JDK 序列化后的 JSONObject 对象字节流
     * 4. 其他对象则退化为 toString 后再解析
     */
    private JSONObject convertTokenObject(Object value) {
        if (value == null) {
            return null;
        }
        try {
            if (value instanceof JSONObject) {
                return (JSONObject) value;
            }

            if (value instanceof String) {
                String str = (String) value;
                if (str.trim().isEmpty()) {
                    return null;
                }
                return JSONObject.parseObject(str);
            }

            if (value instanceof byte[]) {
                byte[] bytes = (byte[]) value;
                if (bytes.length == 0) {
                    return null;
                }
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                    Object obj = ois.readObject();
                    if (obj instanceof JSONObject) {
                        return (JSONObject) obj;
                    }
                    if (obj != null) {
                        return JSONObject.parseObject(obj.toString());
                    }
                } catch (Exception ignore) {
                    // 反序列化失败则继续走后面的 toString 解析逻辑
                }
            }

            return JSONObject.parseObject(value.toString());
        } catch (Exception e) {
            log.error("解析Redis中token对象失败", e);
            return null;
        }
    }

//    -------------用户权限相关-------------

    public List<Users> getUserByStreamId(String streamId) {
        try {
            List<Users> res = usersMapper.getUserByStreamId(streamId);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<UserWithRoleNameVO> getUserByStreamIdWithRoleName(String streamId) {
        try {
            List<UserWithRoleNameVO> res = usersMapper.getUserByStreamIdWithRoleName(streamId);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateRole(String id,String[] roleIds){
        try {
            return usersMapper.updateRole(id,roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
    }
    }

//    订阅用户相关

    @Override
    public IPage<SubscriptionUserVO> filterSubscriptionUsers(String filterInput, Integer isOrder, String startTime, String endTime,
                                                             Integer pageSize, Integer currentPage) {
        Page<SubscriptionUserVO> page = new Page<>(currentPage, pageSize);
        return usersMapper.filterSubscriptionUsers(page,filterInput,
                isOrder, startTime, endTime);
    }

    @Override
    public SubscriptionUserVO getSubscriptionUserById(String userId) {
        try {
            return usersMapper.getSubscriptionUserById(userId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateSubscriptionUser(Users user) {
        try {
            return usersMapper.updateSubscriptionUser(user);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateIsOrder(String id,Integer isOrder){
        try {
            return usersMapper.updateIsOrder(id,isOrder);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteSubscriptionUsers(List<String> userIds) {
        try {
            if (CollUtil.isEmpty(userIds)) {
                return false;
            }
            return usersMapper.batchDeleteSubscriptionUsers(userIds);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Users getUserByPhone(String phone){
        try {
            return usersMapper.getUserByPhone(phone);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建订阅订单
     * @param subOrderVO 订阅订单VO
     * @return 结果
     */
    private R<String> createSubscriptionOrder(SubOrderVO subOrderVO) {
        try {
            // 获取订阅配置信息
            SubscriptionConfig config = subscriptionConfigService.getById(subOrderVO.getConfigId());
            if (config == null) {
                return R.fail("订阅配置不存在");
            }

            SubscriptionOrder subscriptionOrder = new SubscriptionOrder();
            subscriptionOrder.setConfigId(subOrderVO.getConfigId());
            subscriptionOrder.setUserId(subOrderVO.getUserId());
            subscriptionOrder.setOrderTime(subOrderVO.getOrderTime());
            String unit = config.getStorageUnit();
            //设置初始套餐容量
            // 计算总容量（转换为KB）
            long totalStorageKB;
            switch (unit.toUpperCase()) {
                case "G":
                    totalStorageKB = config.getStorage() * 1024 * 1024L;
                    break;
                case "M":
                    totalStorageKB = config.getStorage() * 1024L;
                    break;
                case "T":
                    totalStorageKB = config.getStorage() *1024 * 1024 * 1024L;
                    break;
                default:
                    return R.fail("存储单位配置错误");
            }

            subscriptionOrder.setTotalStorage(totalStorageKB);

            // 计算已使用时长（当前时间 - 订阅时间）
            long currentTimeMillis = System.currentTimeMillis();
            long orderTimeMillis = subOrderVO.getOrderTime().getTime();
            long usedDuration = (currentTimeMillis - orderTimeMillis) / (1000 * 60 * 60 * 24); // 转换为天数
            subscriptionOrder.setUsedDuration((int) usedDuration);

            // 计算到期时间：订阅时间 + 使用期限(月)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(subOrderVO.getOrderTime());
            calendar.add(Calendar.MONTH, config.getDuration());
            subscriptionOrder.setDeadTime(calendar.getTime());

            subscriptionOrder.setUsedProjectNum(1);
            subscriptionOrder.setUsedUserNum(1);
            subscriptionOrder.setUsedStorage(0L);
            subscriptionOrder.setStatus(0);

            boolean b = subscriptionOrderService.insertSubscriptionOrder(subscriptionOrder);

            if (!b) {
                return R.fail("添加失败");
            }

            // 初始化项目流量表
            List<String> resourceIdsByUserId = streamAclService.getResourceIdsByUserId(subOrderVO.getUserId());
            for (String streamId : resourceIdsByUserId) {
                SubscriptionOrderStreams subscriptionOrderStreams = new SubscriptionOrderStreams();
                List<StreamAcl> streamAcls = streamAclService.selectStreamAclByStreamId(streamId);
                for (StreamAcl streamAcl : streamAcls) {
                    if (streamAcl.getRole().equals("stream:owner")){
                        Users user = this.getById(streamAcl.getUserId());
                        if (user != null) {
                            subscriptionOrderStreams.setOwner(user.getName());
                        }
                    }
                }
                subscriptionOrderStreams.setStreamId(streamId);
                subscriptionOrderStreams.setOrderId(subscriptionOrder.getId());
                subscriptionOrderStreams.setName(streamService.getStreamNameById(streamId));
                subscriptionOrderStreams.setUsedStorage(0L);
                subscriptionOrderStreams.setTotalStorage(totalStorageKB);
                subscriptionOrderStreams.setProjectNum(config.getProjectNum());
                subscriptionOrderStreams.setUserNum(resourceIdsByUserId.size());
                subscriptionOrderStreams.setDeadTime(calendar.getTime());
                subscriptionOrderStreamsService.addSubscriptionOrderStreams(subscriptionOrderStreams);
            }
            return R.data(subscriptionOrder.getId());
        } catch (Exception e) {
            log.error("创建订阅订单失败", e);
            return R.fail(e.getMessage());
        }
    }

}
