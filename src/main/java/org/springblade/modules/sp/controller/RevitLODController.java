package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.NewTemplate;
import org.springblade.modules.sp.entity.RevitLOD;
import org.springblade.modules.sp.service.RevitLODService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RevitLOD检测数据控制器
 * @author system
 * @since 2024-09-17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sp/revitLOD")
@CrossOrigin
@Api(value = "RevitLOD检测数据", tags = "RevitLOD检测数据管理")
public class RevitLODController extends BladeController {

    @Autowired
    private RevitLODService revitLODService;

    /**
     * 批量保存RevitLOD检测数据
     */
    @PostMapping("/batchSave.do")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "批量保存检测数据", notes = "接收JSON数组格式的检测数据并保存到数据库")
    public R<List<String>> batchSave(@ApiParam("JSON数组格式的检测数据") @RequestBody List<Map<String, Object>> jsonDataList) {
        try {
            if (jsonDataList == null || jsonDataList.isEmpty()) {
                return R.fail("检测数据不能为空");
            }

            System.out.println("接收到RevitLOD批量保存请求，数据条数: " + jsonDataList.size());
            
            List<RevitLOD> savedList = revitLODService.batchSaveRevitLOD(jsonDataList);
            
            if (!savedList.isEmpty()) {
                List<String> idList = savedList.stream()
                    .map(RevitLOD::getId)
                    .collect(java.util.stream.Collectors.toList());
                
                System.out.println("RevitLOD批量保存成功，保存条数: " + savedList.size());
                return R.data(idList);
            } else {
                return R.fail("保存失败，请检查数据格式");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("RevitLOD批量保存失败: " + e.getMessage());
            return R.fail("保存失败: " + e.getMessage());
        }
    }

    /**
     * 获取格式化输出数据（根据streamId、branchId和可选的commitId获取记录）
     */
    @GetMapping("/getFormattedOutput.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "获取格式化输出数据", notes = "根据streamId、branchId和可选的commitId获取RevitLOD格式化输出数据，如果commitId有值则优先使用，否则获取最新记录")
    public R<List<Map<String, Object>>> getFormattedOutput(
            @ApiParam("流ID") @RequestParam String streamId,
            @ApiParam("分支ID") @RequestParam String branchId,
            @ApiParam("提交ID") @RequestParam String commitId,
            @ApiParam("是否历史") @RequestParam String isHistory) {
        try {
            if (streamId == null || streamId.trim().isEmpty() ||
                branchId == null || branchId.trim().isEmpty()) {
                return R.fail("streamId和branchId参数都不能为空");
            }
            
            List<Map<String, Object>> result = new ArrayList<>();

            if ("0".equals(isHistory)){
                result = revitLODService.getFormattedOutputWithCommitId(streamId, branchId, commitId);
            }else if ("1".equals(isHistory)){
                result = revitLODService.getFormattedOutput(streamId, branchId);
            }
            
            if (result != null && !result.isEmpty()) {
                System.out.println("成功获取RevitLOD格式化输出数据，条数: " + result.size());
                return R.data(result);
            } else {
                return R.fail("未找到对应的数据或数据为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("获取RevitLOD格式化输出数据失败: " + e.getMessage());
            return R.fail("获取数据失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询RevitLOD详情
     */
    @GetMapping("/getById.do")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "根据ID查询详情", notes = "根据ID查询RevitLOD详细信息")
    public R<RevitLOD> getById(@ApiParam("RevitLOD主键ID") @RequestParam("id") String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return R.fail("ID参数不能为空");
            }

            RevitLOD revitLOD = revitLODService.getById(id);

            if (revitLOD != null) {
                return R.data(revitLOD);
            } else {
                return R.fail("未找到对应的数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("查询失败: " + e.getMessage());
        }
    }


    /**
     * 根据checkId查询RevitLOD详情
     */
    @GetMapping("/getByCheckId.do")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "根据checkId查询详情", notes = "根据检测项ID查询RevitLOD详细信息")
    public R<RevitLOD> getByCheckId(@ApiParam("检测项ID") @RequestParam("checkId") String checkId) {
        try {
            if (checkId == null || checkId.trim().isEmpty()) {
                return R.fail("checkId参数不能为空");
            }

            RevitLOD revitLOD = revitLODService.getByCheckId(checkId);
            
            if (revitLOD != null) {
                return R.data(revitLOD);
            } else {
                return R.fail("未找到对应的数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("查询失败: " + e.getMessage());
        }
    }

//    /**
//     * 根据streamId、branchId、commitId查询RevitLOD列表
//     */
//    @GetMapping("/getByStreamBranchCommit.do")
//    @ApiOperationSupport(order = 5)
//    @ApiOperation(value = "多条件查询列表", notes = "根据streamId、branchId、commitId查询RevitLOD列表")
//    public R<List<RevitLOD>> getByStreamBranchCommit(
//            @ApiParam("流ID") @RequestParam("streamId") String streamId,
//            @ApiParam("分支ID") @RequestParam("branchId") String branchId,
//            @ApiParam("提交ID") @RequestParam("commitId") String commitId) {
//        try {
//            if (streamId == null || streamId.trim().isEmpty() ||
//                branchId == null || branchId.trim().isEmpty() ||
//                commitId == null || commitId.trim().isEmpty()) {
//                return R.fail("streamId、branchId、commitId参数都不能为空");
//            }
//
//            List<RevitLOD> list = revitLODService.getByStreamBranchCommit(streamId, branchId, commitId);
//
//            if (list != null) {
//                return R.data(list);
//            } else {
//                return R.fail("查询失败");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return R.fail("查询失败: " + e.getMessage());
//        }
//    }

    /**
     * 根据streamId查询RevitLOD列表
     */
    @GetMapping("/getByStreamId.do")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "根据streamId查询列表", notes = "根据streamId查询RevitLOD列表")
    public R<List<RevitLOD>> getByStreamId(@ApiParam("流ID") @RequestParam("streamId") String streamId) {
        try {
            if (streamId == null || streamId.trim().isEmpty()) {
                return R.fail("streamId参数不能为空");
            }

            List<RevitLOD> list = revitLODService.getByStreamId(streamId);
            
            if (list != null) {
                return R.data(list);
            } else {
                return R.fail("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 删除RevitLOD数据
     */
    @DeleteMapping("/deleteById.do")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "根据ID删除", notes = "根据ID删除RevitLOD数据")
    public R<Boolean> deleteById(@ApiParam("RevitLOD主键ID") @RequestParam("id") String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return R.fail("ID参数不能为空");
            }

            boolean success = revitLODService.deleteById(id);
            
            if (success) {
                return R.data(success);
            } else {
                return R.fail("删除失败，可能数据不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    /**
     * 根据checkId删除RevitLOD数据
     */
    @DeleteMapping("/deleteByCheckId.do")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "根据checkId删除", notes = "根据检测项ID删除RevitLOD数据")
    public R<Boolean> deleteByCheckId(@ApiParam("检测项ID") @RequestParam("checkId") String checkId) {
        try {
            if (checkId == null || checkId.trim().isEmpty()) {
                return R.fail("checkId参数不能为空");
            }

            boolean success = revitLODService.deleteByCheckId(checkId);
            
            if (success) {
                return R.data(success);
            } else {
                return R.fail("删除失败，可能数据不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有RevitLOD数据
     */
    @GetMapping("/getAll.do")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "获取所有数据", notes = "获取所有RevitLOD数据")
    public R<List<RevitLOD>> getAll() {
        try {
            List<RevitLOD> list = revitLODService.getAllRevitLOD();
            
            if (list != null) {
                return R.data(list);
            } else {
                return R.fail("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据elementId、streamId、branchId查找包含该elementId的templateId列表
     */
    @GetMapping("/getTemplateIdsByElementId.do")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "根据elementId查找templateId", notes = "根据elementId、streamId、branchId查找包含该elementId的templateId列表")
    public R<List<NewTemplate>> getTemplateIdsByElementId(
            @ApiParam("元素ID") @RequestParam("elementId") String elementId,
            @ApiParam("流ID") @RequestParam("streamId") String streamId,
            @ApiParam("分支ID") @RequestParam("branchId") String branchId) {
        try {
            if (elementId == null || elementId.trim().isEmpty() ||
                streamId == null || streamId.trim().isEmpty() ||
                branchId == null || branchId.trim().isEmpty()) {
                return R.fail("elementId、streamId、branchId参数都不能为空");
            }

            List<NewTemplate> templates = revitLODService.getTemplateIdsByElementId(elementId, streamId, branchId);
            
            if (templates != null) {
                System.out.println("成功查找到包含elementId " + elementId + " 的template数量: " + templates.size());
                return R.data(templates);
            } else {
                return R.fail("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("根据elementId查找templateId失败: " + e.getMessage());
            return R.fail("查询失败: " + e.getMessage());
        }
    }
}
