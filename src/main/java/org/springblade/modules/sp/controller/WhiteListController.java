package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.WhiteList;
import org.springblade.modules.sp.service.WhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 白名单控制器
 * 提供白名单的CRUD接口
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/sp/whiteList")
@CrossOrigin
@Api(value = "白名单管理", tags = "白名单管理")
public class WhiteListController {

    @Autowired
    private WhiteListService whiteListService;

    /**
     * 根据ID查询白名单记录
     */
    @SneakyThrows
    @GetMapping("/getById.do")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "根据ID查询白名单", notes = "根据ID查询白名单记录")
    public R<WhiteList> getById(@RequestParam String id) {
        try {
            WhiteList whiteList = whiteListService.getById(id);
            if (whiteList != null) {
                return R.data(whiteList);
            } else {
                return R.fail("白名单记录不存在");
            }
        } catch (Exception e) {
            log.error("查询白名单记录失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询所有白名单记录
     */
    @SneakyThrows
    @GetMapping("/getAll.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "查询所有白名单", notes = "查询所有白名单记录")
    public R<List<WhiteList>> getAll() {
        try {
            List<WhiteList> list = whiteListService.getAll();
            return R.data(list);
        } catch (Exception e) {
            log.error("查询所有白名单记录失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据模板ID查询白名单记录列表
     */
    @SneakyThrows
    @GetMapping("/getByTemplateId.do")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "根据模板ID查询白名单", notes = "根据模板ID查询白名单记录列表，可选branchId过滤")
    public R<List<WhiteList>> getByTemplateId(
            @RequestParam String templateId,
            @RequestParam String streamId,
            @RequestParam(required = false) Optional<String> branchId) {
        try {
            List<WhiteList> list = whiteListService.getByTemplateId(templateId,streamId,branchId.orElse(null));
            return R.data(list);
        } catch (Exception e) {
            log.error("根据模板ID查询白名单记录失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据文件ID查询白名单记录列表
     */
    @SneakyThrows
    @GetMapping("/getByFileId.do")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "根据文件ID查询白名单", notes = "根据文件ID查询白名单记录列表")
    public R<List<WhiteList>> getByFileId(@RequestParam String fileId) {
        try {
            List<WhiteList> list = whiteListService.getByFileId(fileId);
            return R.data(list);
        } catch (Exception e) {
            log.error("根据文件ID查询白名单记录失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据名称查询白名单记录
     */
    @SneakyThrows
    @GetMapping("/getByName.do")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "根据名称查询白名单", notes = "根据名称查询白名单记录")
    public R<List<WhiteList>> getByName(@RequestParam String name) {
        try {
            List<WhiteList> list = whiteListService.getByName(name);
            return R.data(list);
        } catch (Exception e) {
            log.error("根据名称查询白名单记录失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增白名单记录
     */
    @SneakyThrows
    @PostMapping("/add.do")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增白名单", notes = "新增白名单记录")
    public R add(@RequestBody WhiteList whiteList) {
        try {
            boolean success = whiteListService.addWhiteList(whiteList);
            if (success) {
                return R.data(whiteList.getId(), "新增成功");
            } else {
                return R.fail("新增失败");
            }
        } catch (Exception e) {
            log.error("新增白名单记录失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    /**
     * 批量新增白名单记录
     */
    @SneakyThrows
    @PostMapping("/batchAdd.do")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "批量新增白名单", notes = "批量新增白名单记录")
    public R batchAdd(@RequestBody List<WhiteList> whiteListList) {
        try {
            boolean success = whiteListService.batchAddWhiteList(whiteListList);
            if (success) {
                return R.success("批量新增成功，共" + whiteListList.size() + "条记录");
            } else {
                return R.fail("批量新增失败");
            }
        } catch (Exception e) {
            log.error("批量新增白名单记录失败", e);
            return R.fail("批量新增失败: " + e.getMessage());
        }
    }

    /**
     * 按属性列表批量新增白名单记录
     * 入参格式：{
     *   "streamId": "a2667e2b64",
     *   "branchId": "687e4bb370",
     *   "templateId": "460f71cd-c890-49a6-935a-6a2ae5042a43",
     *   "propList": ["构件编码", "地块号", "安装时间"]
     * }
     */
    @SneakyThrows
    @PostMapping("/addByProps.do")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "按属性列表新增白名单", notes = "根据streamId、branchId、templateId和propList批量新增白名单")
    public R addByProps(@RequestBody java.util.Map<String, Object> requestMap) {
        try {
            String streamId = (String) requestMap.get("streamId");
            String branchId = (String) requestMap.get("branchId");
            String templateId = (String) requestMap.get("templateId");
            @SuppressWarnings("unchecked")
            List<String> propList = (List<String>) requestMap.get("propList");
            
            // 参数校验
            if (templateId == null || templateId.trim().isEmpty()) {
                return R.fail("templateId不能为空");
            }
            if (propList == null || propList.isEmpty()) {
                return R.fail("propList不能为空");
            }
            
            // 先删除该模板的旧白名单记录
            try {
                List<WhiteList> existingList = whiteListService.getByTemplateId(templateId,streamId,null);
                if (existingList != null && !existingList.isEmpty()) {
                    log.info("发现模板已存在{}条白名单记录，先删除旧记录", existingList.size());
                    whiteListService.deleteByTemplateId(templateId);
                }
            } catch (Exception e) {
                log.warn("删除旧白名单记录失败，继续执行: {}", e.getMessage());
            }
            
            // 构建白名单列表
            List<WhiteList> whiteListList = new java.util.ArrayList<>();
            for (String propName : propList) {
                if (propName != null && !propName.trim().isEmpty()) {
                    WhiteList whiteList = new WhiteList(
                        propName.trim(),
                        templateId,
                        null,  // fileId可选
                        streamId,
                        branchId
                    );
                    whiteListList.add(whiteList);
                }
            }
            
            if (whiteListList.isEmpty()) {
                return R.fail("有效的属性列表为空");
            }
            
            // 批量保存
            boolean success = whiteListService.batchAddWhiteList(whiteListList);
            if (success) {
                log.info("按属性列表批量新增白名单成功，templateId={}, 数量={}", templateId, whiteListList.size());
                return R.success("批量新增成功，共" + whiteListList.size() + "条记录");
            } else {
                return R.fail("批量新增失败");
            }
        } catch (Exception e) {
            log.error("按属性列表批量新增白名单记录失败", e);
            return R.fail("批量新增失败: " + e.getMessage());
        }
    }

    /**
     * 更新白名单记录
     */
    @SneakyThrows
    @PostMapping("/update.do")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "更新白名单", notes = "更新白名单记录")
    public R update(@RequestBody WhiteList whiteList) {
        try {
            boolean success = whiteListService.updateWhiteList(whiteList);
            if (success) {
                return R.data(whiteList.getId(), "更新成功");
            } else {
                return R.fail("更新失败");
            }
        } catch (Exception e) {
            log.error("更新白名单记录失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID删除白名单记录
     */
    @SneakyThrows
    @PostMapping("/deleteById.do")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "删除白名单", notes = "根据ID删除白名单记录")
    public R deleteById(@RequestParam String id) {
        try {
            boolean success = whiteListService.deleteById(id);
            if (success) {
                return R.success("删除成功");
            } else {
                return R.fail("删除失败");
            }
        } catch (Exception e) {
            log.error("删除白名单记录失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    /**
     * 根据模板ID删除白名单记录
     */
    @SneakyThrows
    @PostMapping("/deleteByTemplateId.do")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "根据模板ID删除白名单", notes = "根据模板ID删除白名单记录")
    public R deleteByTemplateId(@RequestParam String templateId) {
        try {
            boolean success = whiteListService.deleteByTemplateId(templateId);
            if (success) {
                return R.success("删除成功");
            } else {
                return R.fail("删除失败");
            }
        } catch (Exception e) {
            log.error("根据模板ID删除白名单记录失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    /**
     * 根据文件ID删除白名单记录
     */
    @SneakyThrows
    @PostMapping("/deleteByFileId.do")
    @ApiOperationSupport(order = 11)
    @ApiOperation(value = "根据文件ID删除白名单", notes = "根据文件ID删除白名单记录")
    public R deleteByFileId(@RequestParam String fileId) {
        try {
            boolean success = whiteListService.deleteByFileId(fileId);
            if (success) {
                return R.success("删除成功");
            } else {
                return R.fail("删除失败");
            }
        } catch (Exception e) {
            log.error("根据文件ID删除白名单记录失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }
}
