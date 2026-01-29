package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.LibDicDetailDTO;
import org.springblade.modules.sp.entity.LibDicDetail;
import org.springblade.modules.sp.entity.LibDictionary;
import org.springblade.modules.sp.entity.Users;
import org.springblade.modules.sp.service.IUsersService;
import org.springblade.modules.sp.service.LibDicDetailService;
import org.springblade.modules.sp.service.LibDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/libDic")
@Api(value = "字段字典控制层", tags = "字段字典控制层")
public class LibDicController extends BladeController {

    @Autowired
    private LibDictionaryService libDictionaryService;

    @Autowired
    private LibDicDetailService libDicDetailService;

    @Autowired
    private IUsersService usersService;

//    -----------外层字典---------------

    @SneakyThrows
    @GetMapping("/getAllLibDictionary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllLibDictionary", notes = "getAllLibDictionary")
    public R<List<LibDictionary>> getAllLibDictionary() {
        try {
            List<LibDictionary> allLibDictionary = libDictionaryService.getLibDictionaryList();
            return R.data(allLibDictionary);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("查询失败");
        }
    }

    @SneakyThrows
    @GetMapping("/getLibDictionaryById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLibDictionaryById", notes = "getLibDictionaryById")
    public R<LibDictionary> getLibDictionaryById(@RequestParam String id) {
        try {
            LibDictionary libDictionary = libDictionaryService.getLibDictionaryById(id);
            return R.data(libDictionary);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("查询失败");
        }
    }

    @SneakyThrows
    @PostMapping("/addLibDictionary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addLibDictionary", notes = "addLibDictionary")
    public R<Boolean> addLibDictionary(@RequestHeader("Authorization") String token,@RequestBody LibDictionary libDictionary) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }
            libDictionary.setStatus(0);
            libDictionary.setCreateTime(new Date());
            libDictionary.setModifyTime(new Date());
            libDictionary.setUserName(user.getName());
            boolean b = libDictionaryService.addLibDictionary(libDictionary);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("添加失败");
        }
    }

    @SneakyThrows
    @PostMapping("/updateLibDictionary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateLibDictionary", notes = "updateLibDictionary")
    public R<Boolean> updateLibDictionary(@RequestHeader("Authorization") String token,@RequestBody LibDictionary libDictionary) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }
            libDictionary.setUserName(user.getName());
            libDictionary.setModifyTime(new Date());
            boolean b = libDictionaryService.updateLibDictionary(libDictionary);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("更新失败");
        }
    }

    @SneakyThrows
    @PostMapping("/updateLibDictionaryStatus.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateLibDictionaryStatus", notes = "updateLibDictionaryStatus")
    public R<Boolean> updateLibDictionaryStatus(@RequestHeader("Authorization") String token,@RequestParam String id, @RequestParam Integer status) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }
            boolean b = libDictionaryService.updateLibDictionaryStatus(id, status);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("更新失败");
        }
    }

    @SneakyThrows
    @PostMapping("/deleteLibDictionary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteLibDictionary", notes = "deleteLibDictionary")
    public R<Boolean> deleteLibDictionary(@RequestParam String id) {
        try {
            boolean b = libDictionaryService.deleteLibDictionary(id);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("删除失败");
        }
    }

//    ----------内层字典---------------

    @SneakyThrows
    @GetMapping("/getLibDicDetailByPid.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLibDicDetailByPid", notes = "getLibDicDetailByPid")
    public R<List<LibDicDetail>> getLibDicDetailByPid(@RequestParam String pid) {
        try {
            List<LibDicDetail> libDicDetailList = libDicDetailService.getLibDicDetailByPid(pid);
            return R.data(libDicDetailList);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("查询失败");
        }
    }

    @SneakyThrows
    @GetMapping("/getAllLibDicDetail.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllLibDicDetail", notes = "getAllLibDicDetail")
    public R<List<LibDicDetailDTO>> getAllLibDicDetail() {
        try {
            List<LibDicDetailDTO> libDicDetailList = libDicDetailService.listAllWithCode();
            return R.data(libDicDetailList);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("查询失败");
        }
    }

    @SneakyThrows
    @GetMapping("/getLibDicDetailById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLibDicDetailById", notes = "getLibDicDetailById")
    public R<LibDicDetail> getLibDicDetailById(@RequestParam String id) {
        try {
            LibDicDetail libDicDetail = libDicDetailService.getLibDicDetailById(id);
            return R.data(libDicDetail);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("查询失败");
        }
    }

    @SneakyThrows
    @PostMapping("/addLibDicDetail.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addLibDicDetail", notes = "addLibDicDetail")
    public R<Boolean> addLibDicDetail(@RequestHeader("Authorization") String token,@RequestBody LibDicDetail libDicDetail) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }
            libDicDetail.setStatus(0);
            libDicDetail.setCreateTime(new Date());
            libDicDetail.setModifyTime(new Date());
            libDicDetail.setUserName(user.getName());
            boolean b = libDicDetailService.addLibDicDetail(libDicDetail);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("添加失败");
        }
    }

    @SneakyThrows
    @PostMapping("/addLibDicDetailList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addLibDicDetailList", notes = "addLibDicDetailList")
    public R<Boolean> addLibDicDetailList(@RequestHeader("Authorization") String token,@RequestBody List<LibDicDetail> libDicDetails) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }
            for (LibDicDetail libDicDetail : libDicDetails) {
                libDicDetail.setStatus(0);
                libDicDetail.setCreateTime(new Date());
                libDicDetail.setModifyTime(new Date());
                libDicDetail.setUserName(user.getName());
            }
            boolean b = libDicDetailService.addLibDicDetailList(libDicDetails);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("添加失败");
        }
    }

    @SneakyThrows
    @PostMapping("/updateLibDicDetail.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateLibDicDetail", notes = "updateLibDicDetail")
    public R<Boolean> updateLibDicDetail(@RequestHeader("Authorization") String token,@RequestBody LibDicDetail libDicDetail) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }
            libDicDetail.setModifyTime(new Date());
            libDicDetail.setUserName(user.getName());
            boolean b = libDicDetailService.updateLibDicDetail(libDicDetail);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("更新失败");
        }
    }

    @SneakyThrows
    @PostMapping("/updateLibDicDetailStatus.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateLibDicDetailStatus", notes = "updateLibDicDetailStatus")
    public R<Boolean> updateLibDicDetailStatus(@RequestHeader("Authorization") String token,String id,Integer status) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }
            boolean b = libDicDetailService.updateLibDicDetailStatus(id, status);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("更新失败");
        }
    }

    @SneakyThrows
    @PostMapping("/deleteLibDicDetail.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteLibDicDetail", notes = "deleteLibDicDetail")
    public R<Boolean> deleteLibDicDetail(@RequestParam String id) {
        try {
            boolean b = libDicDetailService.deleteLibDicDetailById(id);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("删除失败");
        }
    }



}

