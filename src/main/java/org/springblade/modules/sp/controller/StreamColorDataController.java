package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.StreamColorDataDTO;
import org.springblade.modules.sp.entity.StreamColorData;
import org.springblade.modules.sp.service.IStreamColorDataService;
import org.springblade.modules.sp.vo.StreamColorDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stream颜色数据控制器
 *
 * @author auto-generated
 * @since 2026-01-05
 */
@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/streamColorData")
@Api(value = "Stream颜色数据控制层", tags = "Stream颜色数据控制层")
public class StreamColorDataController extends BladeController {

    @Autowired
    private IStreamColorDataService streamColorDataService;

    /**
     * 保存Stream颜色数据
     */
    @SneakyThrows
    @PostMapping("/save.do")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "保存Stream颜色数据", notes = "支持批量保存颜色数据")
    public R<Boolean> save(@Valid @RequestBody StreamColorDataDTO dto) {
        try {
            log.info("开始保存Stream颜色数据，streamId: {}, commitId: {}, 数据量: {}", 
                    dto.getStreamId(), dto.getCommitId(), 
                    dto.getData() != null ? dto.getData().size() : 0);
            
            boolean result = streamColorDataService.saveColorData(dto);
            
            if (result) {
                return R.success("保存成功");
            } else {
                return R.fail("保存失败");
            }
        } catch (Exception e) {
            log.error("保存Stream颜色数据异常", e);
            return R.fail("保存异常: " + e.getMessage());
        }
    }

    /**
     * 根据streamId和commitId查询数据
     */
    @SneakyThrows
    @GetMapping("/query.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "查询Stream颜色数据", notes = "根据streamId和commitId查询所有符合条件的数据")
    public R<StreamColorDataVO> query(
            @RequestParam String streamId,
            @RequestParam String commitId) {
        try {
            if (StringUtils.isBlank(streamId) || StringUtils.isBlank(commitId)) {
                return R.fail("streamId和commitId不能为空");
            }

            log.info("开始查询Stream颜色数据，streamId: {}, commitId: {}", streamId, commitId);
            
            List<StreamColorData> dataList = streamColorDataService.getByStreamAndCommit(streamId, commitId);
            
            StreamColorDataVO vo = new StreamColorDataVO();
            vo.setStreamId(streamId);
            vo.setCommitId(commitId);
            
            if (dataList != null && !dataList.isEmpty()) {
                List<StreamColorDataVO.ColorDataItem> items = dataList.stream()
                        .map(entity -> {
                            StreamColorDataVO.ColorDataItem item = new StreamColorDataVO.ColorDataItem();
                            item.setNodeId(entity.getNodeId());
                            item.setColor(entity.getColor());
                            return item;
                        })
                        .collect(Collectors.toList());
                vo.setData(items);
            } else {
                vo.setData(new ArrayList<>());
            }
            
            return R.data(vo);
        } catch (Exception e) {
            log.error("查询Stream颜色数据异常", e);
            return R.fail("查询异常: " + e.getMessage());
        }
    }

    /**
     * 根据streamId和commitId删除数据
     */
    @SneakyThrows
    @DeleteMapping("/delete.do")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "删除Stream颜色数据", notes = "根据streamId和commitId删除所有符合条件的数据")
    public R<Boolean> delete(
            @RequestParam String streamId,
            @RequestParam String commitId) {
        try {
            if (StringUtils.isBlank(streamId) || StringUtils.isBlank(commitId)) {
                return R.fail("streamId和commitId不能为空");
            }

            log.info("开始删除Stream颜色数据，streamId: {}, commitId: {}", streamId, commitId);
            
            boolean result = streamColorDataService.deleteByStreamAndCommit(streamId, commitId);
            
            if (result) {
                return R.success("删除成功");
            } else {
                return R.fail("删除失败，未找到数据或数据已被删除");
            }
        } catch (Exception e) {
            log.error("删除Stream颜色数据异常", e);
            return R.fail("删除异常: " + e.getMessage());
        }
    }

    /**
     * 查询所有数据
     */
    @SneakyThrows
    @GetMapping("/list.do")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "查询所有Stream颜色数据", notes = "查询所有颜色数据")
    public R<List<StreamColorData>> list() {
        try {
            log.info("开始查询所有Stream颜色数据");
            
            List<StreamColorData> list = streamColorDataService.list();
            
            return R.data(list);
        } catch (Exception e) {
            log.error("查询所有Stream颜色数据异常", e);
            return R.fail("查询异常: " + e.getMessage());
        }
    }
}
