package org.springblade.modules.sp.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.TemplateData;
import org.springblade.modules.sp.service.TemplateDataService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模板数据控制器
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sp/templateData")
@Api(value = "模板数据", tags = "模板数据接口")
public class TemplateDataController {

    private final TemplateDataService templateDataService;

    /**
     * 根据id查询
     */
    @GetMapping("/getById")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "根据id查询", notes = "根据id查询模板数据详情")
    public R<TemplateData> getById(@RequestParam String id) {
        TemplateData templateData = templateDataService.getById(id);
        return R.data(templateData);
    }

    /**
     * 根据templateId和type获取data数据
     */
    @GetMapping("/getData")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "获取数据", notes = "根据templateId、type和branchId获取data的JSON数据")
    public R<JSONObject> getData(@RequestParam String templateId, @RequestParam String type, @RequestParam(required = false) String branchId) {
        JSONObject data = templateDataService.getDataByTemplateIdAndType(templateId, type, branchId);
        return R.data(data);
    }

    /**
     * 根据templateId查询列表
     */
    @GetMapping("/getByTemplateId")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "根据模板ID查询", notes = "根据templateId查询模板数据列表")
    public R<List<TemplateData>> getByTemplateId(@RequestParam String templateId) {
        List<TemplateData> list = templateDataService.getByTemplateId(templateId);
        return R.data(list);
    }

    /**
     * 根据type查询列表
     */
    @GetMapping("/getByType")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "根据类型查询", notes = "根据type查询模板数据列表")
    public R<List<TemplateData>> getByType(@RequestParam String type) {
        List<TemplateData> list = templateDataService.getByType(type);
        return R.data(list);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "新增", notes = "新增模板数据")
    public R<Boolean> add(@RequestBody TemplateData templateData) {
        boolean result = templateDataService.addTemplateData(templateData);
        if (result) {
            return R.success("新增成功");
        } else {
            return R.fail("新增失败");
        }
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/deleteById")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "根据id删除", notes = "根据id删除模板数据")
    public R<Boolean> deleteById(@RequestParam String id) {
        boolean result = templateDataService.deleteById(id);
        if (result) {
            return R.success("删除成功");
        } else {
            return R.fail("删除失败");
        }
    }

    /**
     * 根据templateId和type删除
     */
    @DeleteMapping("/deleteByTemplateIdAndType")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "根据条件删除", notes = "根据templateId、type和branchId删除模板数据")
    public R<Boolean> deleteByTemplateIdAndType(@RequestParam String templateId, @RequestParam String type, @RequestParam(required = false) String branchId) {
        boolean result = templateDataService.deleteByTemplateIdAndType(templateId, type, branchId);
        if (result) {
            return R.success("删除成功");
        } else {
            return R.fail("删除失败");
        }
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "更新", notes = "更新模板数据")
    public R<Boolean> update(@RequestBody TemplateData templateData) {
        boolean result = templateDataService.updateTemplateData(templateData);
        if (result) {
            return R.success("更新成功");
        } else {
            return R.fail("更新失败");
        }
    }

    /**
     * 获取所有数据
     */
    @GetMapping("/getAll")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "获取所有数据", notes = "获取所有模板数据列表")
    public R<List<TemplateData>> getAll() {
        List<TemplateData> list = templateDataService.getAllTemplateData();
        return R.data(list);
    }

    /**
     * 保存合并后的数据
     */
//    @PostMapping("/saveMergedData")
//    @ApiOperationSupport(order = 10)
//    @ApiOperation(value = "保存合并数据", notes = "合并getData和by-block接口数据并保存到数据库")
//    public R<String> saveMergedData(
//            @RequestParam String templateId,
//            @RequestParam String type,
//            @RequestParam(required = false) String branchId,
//            @RequestParam String blockName) {
//        try {
//            boolean result = templateDataService.saveMergedData(templateId, type, branchId, blockName);
//            if (result) {
//                return R.data(templateId);
//            } else {
//                return R.fail("保存失败");
//            }
//        } catch (Exception e) {
//            return R.fail("保存失败: " + e.getMessage());
//        }
//    }
}
