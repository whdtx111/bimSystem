package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.Label;
import org.springblade.modules.sp.service.BranchService;
import org.springblade.modules.sp.service.LabelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签控制器
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sp/label")
@Api(value = "标签管理", tags = "标签管理接口")
@Slf4j
public class LabelController {

    private final LabelService labelService;
    private final BranchService branchService;

    /**
     * 根据id查询标签
     */
    @GetMapping("/getById.do")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "根据id查询", notes = "根据id查询标签详情")
    public R<Label> getById(@ApiParam(value = "标签ID", required = true) @RequestParam String id) {
        try {
            Label label = labelService.getLabelById(id);
            if (label != null) {
                return R.data(label);
            } else {
                return R.fail("标签不存在");
            }
        } catch (Exception e) {
            log.error("查询标签失败: id={}", id, e);
            return R.fail("查询标签失败: " + e.getMessage());
        }
    }

    /**
     * 根据streamId查询标签列表
     */
    @GetMapping("/getByStreamId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "根据streamId查询", notes = "根据streamId查询标签列表")
    public R<List<Label>> getByStreamId(@ApiParam(value = "流ID", required = true) @RequestParam String streamId) {
        try {
            List<Label> labels = labelService.getLabelsByStreamId(streamId);
            return R.data(labels);
        } catch (Exception e) {
            log.error("根据streamId查询标签列表失败: streamId={}", streamId, e);
            return R.fail("查询标签列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据branchId查询标签列表
     */
    @GetMapping("/getByBranchId.do")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "根据branchId查询", notes = "根据branchId查询标签列表")
    public R<List<Label>> getByBranchId(@ApiParam(value = "分支ID", required = true) @RequestParam String branchId) {
        try {
            List<Label> labels = labelService.getLabelsByBranchId(branchId);
            return R.data(labels);
        } catch (Exception e) {
            log.error("根据branchId查询标签列表失败: branchId={}", branchId, e);
            return R.fail("查询标签列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据commitId查询标签列表
     */
    @GetMapping("/getByCommitId.do")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "根据commitId查询", notes = "根据commitId查询标签列表")
    public R<List<Label>> getByCommitId(@ApiParam(value = "提交ID", required = true) @RequestParam String commitId) {
        try {
            List<Label> labels = labelService.getLabelsByCommitId(commitId);
            return R.data(labels);
        } catch (Exception e) {
            log.error("根据commitId查询标签列表失败: commitId={}", commitId, e);
            return R.fail("查询标签列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据streamId、branchId和commitId查询标签列表
     */
    @GetMapping("/getByStreamBranchCommit.do")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "根据组合条件查询", notes = "根据streamId、branchId和commitId查询标签列表")
    public R<List<Label>> getByStreamBranchCommit(
            @ApiParam(value = "流ID", required = true) @RequestParam String streamId,
            @ApiParam(value = "分支ID", required = true) @RequestParam String branchId,
            @ApiParam(value = "提交ID") @RequestParam(required = false) String commitId) {
        try {
            List<Label> labels = labelService.getLabelsByStreamBranchCommit(streamId, branchId, commitId);
            return R.data(labels);
        } catch (Exception e) {
            log.error("根据组合条件查询标签列表失败: streamId={}, branchId={}, commitId={}", 
                      streamId, branchId, commitId, e);
            return R.fail("查询标签列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据type查询标签列表
     */
    @GetMapping("/getByType.do")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "根据类型查询", notes = "根据type查询标签列表")
    public R<List<Label>> getByType(@ApiParam(value = "标签类型", required = true) @RequestParam String type) {
        try {
            List<Label> labels = labelService.getLabelsByType(type);
            return R.data(labels);
        } catch (Exception e) {
            log.error("根据type查询标签列表失败: type={}", type, e);
            return R.fail("查询标签列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据status查询标签列表
     */
    @GetMapping("/getByStatus.do")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "根据状态查询", notes = "根据status查询标签列表")
    public R<List<Label>> getByStatus(@ApiParam(value = "状态", required = true) @RequestParam Integer status) {
        try {
            List<Label> labels = labelService.getLabelsByStatus(status);
            return R.data(labels);
        } catch (Exception e) {
            log.error("根据status查询标签列表失败: status={}", status, e);
            return R.fail("查询标签列表失败: " + e.getMessage());
        }
    }

    /**
     * 查询所有标签
     */
    @GetMapping("/getAll.do")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "查询所有标签", notes = "查询所有标签列表")
    public R<List<Label>> getAll() {
        try {
            List<Label> labels = labelService.getAllLabels();
            return R.data(labels);
        } catch (Exception e) {
            log.error("查询所有标签失败", e);
            return R.fail("查询所有标签失败: " + e.getMessage());
        }
    }

    /**
     * 新增标签
     */
    @PostMapping("/add.do")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "新增标签", notes = "新增一个标签")
    public R<String> add(@ApiParam(value = "标签对象", required = true) @RequestBody Label label) {
        try {
            if (label.getCommitId().isEmpty()){
                String commitId = branchService.getLatestCommitsId(label.getBranchId());
                label.setCommitId(commitId);
            }
            boolean result = labelService.addLabel(label);
            if (result) {
                return R.data(label.getId());
            } else {
                return R.fail("新增标签失败");
            }
        } catch (Exception e) {
            log.error("新增标签失败: label={}", label, e);
            return R.fail("新增标签失败: " + e.getMessage());
        }
    }

    /**
     * 批量新增标签
     */
    @PostMapping("/batchAdd.do")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "批量新增标签", notes = "批量新增多个标签")
    public R<String> batchAdd(@ApiParam(value = "标签列表", required = true) @RequestBody List<Label> labels) {
        try {
            if (labels == null || labels.isEmpty()) {
                return R.fail("标签列表不能为空");
            }
            String ids = "";
            boolean result = labelService.batchInsertLabels(labels);
            for (Label label : labels) {
                ids = label.getId() + ",";
            }
            if (result) {
                return R.data("批量新增标签成功,id为: "+ids);
            } else {
                return R.fail("批量新增标签失败");
            }
        } catch (Exception e) {
            log.error("批量新增标签失败: labels.size={}", labels != null ? labels.size() : 0, e);
            return R.fail("批量新增标签失败: " + e.getMessage());
        }
    }

    /**
     * 更新标签
     */
    @PostMapping("/update.do")
    @ApiOperationSupport(order = 11)
    @ApiOperation(value = "更新标签", notes = "更新标签信息")
    public R<String> update(@ApiParam(value = "标签对象", required = true) @RequestBody Label label) {
        try {
            if (label.getId() == null || label.getId().isEmpty()) {
                return R.fail("标签ID不能为空");
            }
            boolean result = labelService.updateLabel(label);
            if (result) {
                return R.data(label.getId());
            } else {
                return R.fail("更新标签失败");
            }
        } catch (Exception e) {
            log.error("更新标签失败: label={}", label, e);
            return R.fail("更新标签失败: " + e.getMessage());
        }
    }

    /**
     * 根据id删除标签
     */
    @PostMapping("/deleteById.do")
    @ApiOperationSupport(order = 12)
    @ApiOperation(value = "根据id删除", notes = "根据id删除标签")
    public R<String> deleteById(@ApiParam(value = "标签ID", required = true) @RequestParam String id) {
        try {
            boolean result = labelService.deleteLabel(id);
            if (result) {
                return R.data("删除标签成功");
            } else {
                return R.fail("删除标签失败");
            }
        } catch (Exception e) {
            log.error("删除标签失败: id={}", id, e);
            return R.fail("删除标签失败: " + e.getMessage());
        }
    }

    /**
     * 根据streamId删除标签
     */
    @PostMapping("/deleteByStreamId.do")
    @ApiOperationSupport(order = 13)
    @ApiOperation(value = "根据streamId删除", notes = "根据streamId删除所有相关标签")
    public R<String> deleteByStreamId(@ApiParam(value = "流ID", required = true) @RequestParam String streamId) {
        try {
            boolean result = labelService.deleteLabelsByStreamId(streamId);
            if (result) {
                return R.data("删除标签成功");
            } else {
                return R.fail("没有找到相关标签");
            }
        } catch (Exception e) {
            log.error("根据streamId删除标签失败: streamId={}", streamId, e);
            return R.fail("删除标签失败: " + e.getMessage());
        }
    }

    /**
     * 根据branchId删除标签
     */
    @PostMapping("/deleteByBranchId.do")
    @ApiOperationSupport(order = 14)
    @ApiOperation(value = "根据branchId删除", notes = "根据branchId删除所有相关标签")
    public R<String> deleteByBranchId(@ApiParam(value = "分支ID", required = true) @RequestParam String branchId) {
        try {
            boolean result = labelService.deleteLabelsByBranchId(branchId);
            if (result) {
                return R.success("删除标签成功");
            } else {
                return R.fail("没有找到相关标签");
            }
        } catch (Exception e) {
            log.error("根据branchId删除标签失败: branchId={}", branchId, e);
            return R.fail("删除标签失败: " + e.getMessage());
        }
    }

    /**
     * 根据commitId删除标签
     */
    @PostMapping("/deleteByCommitId.do")
    @ApiOperationSupport(order = 15)
    @ApiOperation(value = "根据commitId删除", notes = "根据commitId删除所有相关标签")
    public R<Boolean> deleteByCommitId(@ApiParam(value = "提交ID", required = true) @RequestParam String commitId) {
        try {
            boolean result = labelService.deleteLabelsByCommitId(commitId);
            if (result) {
                return R.success("删除标签成功");
            } else {
                return R.fail("没有找到相关标签");
            }
        } catch (Exception e) {
            log.error("根据commitId删除标签失败: commitId={}", commitId, e);
            return R.fail("删除标签失败: " + e.getMessage());
        }
    }

    /**
     * 统计标签数量
     */
    @GetMapping("/count.do")
    @ApiOperationSupport(order = 16)
    @ApiOperation(value = "统计标签数量", notes = "统计所有标签的数量")
    public R<Integer> count() {
        try {
            int count = labelService.countLabels();
            return R.data(count);
        } catch (Exception e) {
            log.error("统计标签数量失败", e);
            return R.fail("统计标签数量失败: " + e.getMessage());
        }
    }

    /**
     * 根据streamId统计标签数量
     */
    @GetMapping("/countByStreamId.do")
    @ApiOperationSupport(order = 17)
    @ApiOperation(value = "根据streamId统计", notes = "根据streamId统计标签数量")
    public R<Integer> countByStreamId(@ApiParam(value = "流ID", required = true) @RequestParam String streamId) {
        try {
            int count = labelService.countLabelsByStreamId(streamId);
            return R.data(count);
        } catch (Exception e) {
            log.error("根据streamId统计标签数量失败: streamId={}", streamId, e);
            return R.fail("统计标签数量失败: " + e.getMessage());
        }
    }
}
