package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.FieldResource;
import org.springblade.modules.sp.entity.Users;
import org.springblade.modules.sp.service.FieldResourceService;
import org.springblade.modules.sp.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/fieldResource")
@Api(value = "字段资源控制层", tags = "字段资源控制层")
public class FieldResourceController extends BladeController {

    @Autowired
    private FieldResourceService fieldResourceService;
    @Autowired
    private IUsersService usersService;

    @SneakyThrows
    @GetMapping("/getAll.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAll", notes = "getAll")
    public R<List<FieldResource>> getAll() {
        try {
            return R.data(fieldResourceService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getByTag.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getByTag", notes = "getByTag")
    public R<List<FieldResource>> getByTag(@RequestParam String tag) {
        try {
            List<FieldResource> fieldResources = fieldResourceService.getByTag(tag);
            return R.data(fieldResources);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getById", notes = "getById")
    public R<FieldResource> getById(@RequestParam String id) {
        try {
            return R.data(fieldResourceService.getById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addFieldResource.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addFieldResource", notes = "addFieldResource")
    public R<Boolean> addFieldResource(@RequestHeader("Authorization") String token,@RequestBody FieldResource fieldResource) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }
            fieldResource.setModifyUser(user.getName());
            fieldResource.setModifyTime(new Date());
            return R.data(fieldResourceService.addFieldResource(fieldResource));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateFieldResource.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateFieldResource", notes = "updateFieldResource")
    public R<Boolean> updateFieldResource(@RequestHeader("Authorization") String token,@RequestBody FieldResource fieldResource) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }
            fieldResource.setModifyUser(user.getName());
            fieldResource.setModifyTime(new Date());
            return R.data(fieldResourceService.updateFieldResource(fieldResource));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteFieldResource.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteFieldResource", notes = "deleteFieldResource")
    public R<Boolean> deleteFieldResource(@RequestParam String id) {
        try {
            return R.data(fieldResourceService.deleteById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

}
