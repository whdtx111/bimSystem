/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.constant.SpConstant;
import org.springblade.modules.sp.service.IUsersService;
import org.springblade.modules.sp.service.UserEdgeService;
import org.springblade.modules.sp.utils.CaptchaUtil;
import org.springblade.modules.sp.vo.SearchUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Yi
 * @since 2023-09-13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sp/user")
@CrossOrigin
@Api(value = "User任务表", tags = "User任务表接口")
class SpUserController extends BladeController {
    @Resource
    private UserEdgeService userEdgeService;
    @Autowired
    IUsersService iUsersService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    // Redis key前缀（使用固定key存储最新验证码）
    private static final String CAPTCHA_KEY = "captcha:latest";
    // 验证码过期时间（分钟）
    private static final long CAPTCHA_EXPIRE_MINUTES = 5;

    @GetMapping("/sync-4aUser")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "同步4a用户至sp用户表")
    public R<String> sync4aUser2Users() {
        boolean flag = userEdgeService.sync4aUser2Users();
        if(flag){
            return R.data(SpConstant.SYNC_SUCCESS);
        }
        return R.fail(SpConstant.SYNC_FAIL);
    }
    
    @GetMapping("/getCaptcha")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "生成验证码")
    public R<Map<String, String>> getCaptcha(
            @RequestParam(value = "timestamp", required = false) Long timestamp
    ) {
        try {
            // 生成验证码文本
            String code = CaptchaUtil.generateCode();
            
            // 存储到Redis（不区分大小写，统一转为大写存储）
            // 使用固定key，每次生成新验证码会覆盖旧的
            redisTemplate.opsForValue().set(CAPTCHA_KEY, code.toUpperCase(), CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);
            
            // 生成验证码图片（Base64）
            String imageBase64 = CaptchaUtil.generateImageBase64(code);
            
            // 返回结果（只返回图片，不返回key）
            Map<String, String> result = new HashMap<>();
            result.put("captchaImage", imageBase64);
            
            return R.data(result);
        } catch (Exception e) {
            return R.fail("生成验证码失败: " + e.getMessage());
        }
    }

    @GetMapping("/login")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "登录 手机号或者邮箱登录")
    public R<String> spLogin(
            @RequestParam (value = "account") String account ,
            @RequestParam (value = "password") String password ,
            @RequestParam (value = "challenge")String challenge,
            @RequestParam (value = "checkCode") String checkCode
            ) {
        // 验证码校验
        Object storedCode = redisTemplate.opsForValue().get(CAPTCHA_KEY);
        
        if (storedCode == null) {
            return R.fail("验证码已过期，请重新获取");
        }
        
        // 不区分大小写比对
        if (!storedCode.toString().equalsIgnoreCase(checkCode)) {
            return R.fail("验证码错误");
        }
        
        // 验证码验证成功后删除（一次性使用）
        redisTemplate.delete(CAPTCHA_KEY);
        
        // 执行登录逻辑
        return iUsersService.spAuthLogin(account, password,challenge);
    }

    @GetMapping("/plugin-login")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "插件端登录")
    public R<String> pluginLogin(
            @RequestParam (value = "accessCode") String accessCode,
            @RequestParam (value = "challenge")String challenge
            ) {
        return iUsersService.pluginAuthLogin(accessCode,challenge);
    }

    @GetMapping("/logOut")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "退出清除redis缓存user相关信息")
    public R spLogOut(String token) {
        return iUsersService.spLogOut(token);
    }

    @GetMapping("/register-all")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "注册初始化")
    public R<String> registerAll(
    ) {
        return iUsersService.spRegisterAll();
    }


    @GetMapping("/savaUser")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "savaUser")
    public R savaUser(
    ) {
        return iUsersService.importUser();
    }

    @GetMapping("/randomAvatar-all")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "初始化头像")
    public R RandomAvatarAll(
    ) {
        return iUsersService.RandomAvatarAll();
    }

    @GetMapping("/register")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "注册")
    public R<String> spRegister(
            @RequestParam (value = "email") String email,
            @RequestParam (value = "name") String name,
            @RequestParam (value = "password") String password,
            @RequestParam (value = "company") String company,
            @RequestParam (value = "phone")  String phone,
            @RequestParam (value = "challenge")  String challenge
    ) {
        return iUsersService.spRegister(email,name,password,company,phone,challenge);
    }

    @GetMapping("/userByPhoneNameEmail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "搜索用户/邮箱、姓名、电话")
    public R<List<SearchUserVO>> userByPhoneName(@RequestParam (value = "str")  String str) {
        return iUsersService.userByPhoneName(str);
    }

}


