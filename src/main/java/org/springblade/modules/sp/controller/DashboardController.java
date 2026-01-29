package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 看板数据控制器
 * 
 * @author system
 * @since 2024-12-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sp/dashboard")
@CrossOrigin
@Api(value = "看板数据", tags = "看板数据管理")
@Slf4j
public class DashboardController extends BladeController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取顶部统计卡片数据
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return 统计卡片数据列表
     */
    @GetMapping("/getStatistics.do")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "获取顶部统计卡片数据", notes = "根据streamId和时间类型获取待处理批注、未解决碰撞干扰、模型合规得分、近期模型迭代次数等统计数据")
    public R<List<Map<String, Object>>> getStatistics(
            @ApiParam("流ID") @RequestParam String streamId,
            @ApiParam("时间类型：month（30天）或 week（7天）") @RequestParam String type) {
        try {
            if (streamId == null || streamId.trim().isEmpty()) {
                return R.fail("streamId参数不能为空");
            }
            if (type == null || (!type.equals("month") && !type.equals("week"))) {
                return R.fail("type参数必须为month或week");
            }
            
            List<Map<String, Object>> result = dashboardService.getStatistics(streamId, type);
            return R.data(result);
        } catch (Exception e) {
            log.error("获取统计卡片数据失败: {}", e.getMessage(), e);
            return R.fail("获取统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取设计震荡指数
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return 设计震荡指数数组数据
     */
    @GetMapping("/getDesignFalloffIndex.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "获取设计震荡指数", notes = "根据streamId和时间类型获取设计震荡指数数据")
    public R<List<Map<String, Object>>> getDesignFalloffIndex(
            @ApiParam("流ID") @RequestParam String streamId,
            @ApiParam("时间类型：month（30天）或 week（7天）") @RequestParam String type) {
        try {
            if (streamId == null || streamId.trim().isEmpty()) {
                return R.fail("streamId参数不能为空");
            }
            if (type == null || (!type.equals("month") && !type.equals("week"))) {
                return R.fail("type参数必须为month或week");
            }
            
            List<Map<String, Object>> result = dashboardService.getDesignFalloffIndex(streamId, type);
            return R.data(result);
        } catch (Exception e) {
            log.error("获取设计震荡指数失败: {}", e.getMessage(), e);
            return R.fail("获取设计震荡指数失败: " + e.getMessage());
        }
    }

    /**
     * 获取碰撞检测趋势
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return 碰撞检测趋势折线图数据
     */
    @GetMapping("/getCrashDetectionTrend.do")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "获取碰撞检测趋势", notes = "根据streamId和时间类型获取碰撞检测趋势折线图数据")
    public R<List<Map<String, Object>>> getCrashDetectionTrend(
            @ApiParam("流ID") @RequestParam String streamId,
            @ApiParam("时间类型：month（30天）或 week（7天）") @RequestParam String type) {
        try {
            if (streamId == null || streamId.trim().isEmpty()) {
                return R.fail("streamId参数不能为空");
            }
            if (type == null || (!type.equals("month") && !type.equals("week"))) {
                return R.fail("type参数必须为month或week");
            }
            
            List<Map<String, Object>> result = dashboardService.getCrashDetectionTrend(streamId, type);
            return R.data(result);
        } catch (Exception e) {
            log.error("获取碰撞检测趋势失败: {}", e.getMessage(), e);
            return R.fail("获取碰撞检测趋势失败: " + e.getMessage());
        }
    }

    /**
     * 获取LOI/LOD合规得分
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return LOI/LOD合规得分折线图数据
     */
    @GetMapping("/getLODComplianceScore.do")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "获取LOI/LOD合规得分", notes = "根据streamId和时间类型获取LOI/LOD合规得分折线图数据")
    public R<List<Map<String, Object>>> getLODComplianceScore(
            @ApiParam("流ID") @RequestParam String streamId,
            @ApiParam("时间类型：month（30天）或 week（7天）") @RequestParam String type) {
        try {
            if (streamId == null || streamId.trim().isEmpty()) {
                return R.fail("streamId参数不能为空");
            }
            if (type == null || (!type.equals("month") && !type.equals("week"))) {
                return R.fail("type参数必须为month或week");
            }
            
            List<Map<String, Object>> result = dashboardService.getLODComplianceScore(streamId, type);
            return R.data(result);
        } catch (Exception e) {
            log.error("获取LOD合规得分失败: {}", e.getMessage(), e);
            return R.fail("获取LOD合规得分失败: " + e.getMessage());
        }
    }

    /**
     * 获取待处理问题分类
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return 待处理问题分类饼图数据，格式为 [{name, color, num(百分比)}, ...]
     */
    @GetMapping("/getPendingIssueCategories.do")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "获取待处理问题分类", notes = "根据streamId和时间类型获取待处理问题分类饼图数据")
    public R<List<Map<String, Object>>> getPendingIssueCategories(
            @ApiParam("流ID") @RequestParam String streamId,
            @ApiParam("时间类型：month（30天）或 week（7天）") @RequestParam String type) {
        try {
            if (streamId == null || streamId.trim().isEmpty()) {
                return R.fail("streamId参数不能为空");
            }
            if (type == null || (!type.equals("month") && !type.equals("week"))) {
                return R.fail("type参数必须为month或week");
            }
            
            List<Map<String, Object>> result = dashboardService.getPendingIssueCategories(streamId, type);
            return R.data(result);
        } catch (Exception e) {
            log.error("获取待处理问题分类失败: {}", e.getMessage(), e);
            return R.fail("获取待处理问题分类失败: " + e.getMessage());
        }
    }

    /**
     * 获取团队数据同步活跃度
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return 团队数据同步活跃度柱状图数据
     */
    @GetMapping("/getTeamSyncActivity.do")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "获取团队数据同步活跃度", notes = "根据streamId和时间类型获取团队数据同步活跃度柱状图数据")
    public R<List<Map<String, Object>>> getTeamSyncActivity(
            @ApiParam("流ID") @RequestParam String streamId,
            @ApiParam("时间类型：month（30天）或 week（7天）") @RequestParam String type) {
        try {
            if (streamId == null || streamId.trim().isEmpty()) {
                return R.fail("streamId参数不能为空");
            }
            if (type == null || (!type.equals("month") && !type.equals("week"))) {
                return R.fail("type参数必须为month或week");
            }
            
            List<Map<String, Object>> result = dashboardService.getTeamSyncActivity(streamId, type);
            return R.data(result);
        } catch (Exception e) {
            log.error("获取团队数据同步活跃度失败: {}", e.getMessage(), e);
            return R.fail("获取团队数据同步活跃度失败: " + e.getMessage());
        }
    }

    /**
     * 获取高频变更模型
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return 高频变更模型列表数据
     */
    @GetMapping("/getHighFrequencyChangeModels.do")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "获取高频变更模型", notes = "根据streamId和时间类型获取高频变更模型列表数据")
    public R<List<Map<String, Object>>> getHighFrequencyChangeModels(
            @ApiParam("流ID") @RequestParam String streamId,
            @ApiParam("时间类型：month（30天）或 week（7天）") @RequestParam String type) {
        try {
            if (streamId == null || streamId.trim().isEmpty()) {
                return R.fail("streamId参数不能为空");
            }
            if (type == null || (!type.equals("month") && !type.equals("week"))) {
                return R.fail("type参数必须为month或week");
            }
            
            List<Map<String, Object>> result = dashboardService.getHighFrequencyChangeModels(streamId, type);
            return R.data(result);
        } catch (Exception e) {
            log.error("获取高频变更模型失败: {}", e.getMessage(), e);
            return R.fail("获取高频变更模型失败: " + e.getMessage());
        }
    }
}
