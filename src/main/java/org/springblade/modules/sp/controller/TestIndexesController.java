package org.springblade.modules.sp.controller;

import com.github.pagehelper.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.TestIndexes;
import org.springblade.modules.sp.entity.Users;
import org.springblade.modules.sp.service.IUsersService;
import org.springblade.modules.sp.service.TestIndexesService;
import org.springblade.modules.sp.vo.PageTestIndexes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 检测指标控制层
 *
 * @author Cascade
 * @since 2025-07-21
 */
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/testIndexes")
@Api(value = "检测指标控制层", tags = "testIndexes")
public class TestIndexesController extends BladeController {
    
    @Autowired
    private TestIndexesService testIndexesService;
    
    @Autowired
    private IUsersService usersService;

    @SneakyThrows
    @GetMapping("/getTestIndexesById.do")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "根据ID获取检测指标", notes = "根据ID获取检测指标")
    public R<TestIndexes> getTestIndexesById(@RequestParam String id) {
        try {
            return R.data(testIndexesService.getTestIndexesById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getAll.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "获取所有检测指标", notes = "获取所有检测指标")
    public R<List<TestIndexes>> getAll() {
        try {
            return R.data(testIndexesService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/filterTestIndexes.do")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "筛选检测指标", notes = "筛选检测指标")
    public R<List<TestIndexes>> filterTestIndexes(
            @RequestParam Optional<String> name, 
            @RequestParam Optional<String> auth, 
            @RequestParam Optional<String> source, 
            @RequestParam Optional<Integer> status) {
        try {


            List<TestIndexes> testIndexes = testIndexesService.filterTestIndexes(
                    name.orElse(null), 
                    auth.orElse(null),
                    source.orElse(null), 
                    status.orElse(null));

            return R.data(testIndexes);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addTestIndexes.do")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "添加检测指标", notes = "添加检测指标")
    public R<String> addTestIndexes(@RequestBody TestIndexes testIndexes) {
        try {
            testIndexes.setStatus(0);
            testIndexes.setCreateTime(new Date());
            boolean b = testIndexesService.addTestIndexes(testIndexes);
            if (!b) {
                return R.fail("添加检测指标失败");
            }
            return R.data(testIndexes.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateTestIndexes.do")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "更新检测指标", notes = "更新检测指标")
    public R<Boolean> updateTestIndexes(@RequestBody TestIndexes testIndexes) {
        try {
            return R.data(testIndexesService.updateTestIndexes(testIndexes));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteTestIndexes.do")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "删除检测指标", notes = "删除检测指标")
    public R<Boolean> deleteTestIndexes(@RequestParam String id) {
        try {
            boolean b = testIndexesService.deleteTestIndexesById(id);
            if (b) {
                return R.data(true);
            } else {
                return R.fail("删除检测指标失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/batchInsertTestIndexes.do")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "批量添加检测指标", notes = "批量添加检测指标")
    public R<Boolean> batchInsertTestIndexes(@RequestBody List<TestIndexes> testIndexesList) {
        try {
            boolean b = testIndexesService.insertTestIndexesBatch(testIndexesList);
            if (b) {
                return R.data(true);
            } else {
                return R.fail("批量添加检测指标失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }
}
