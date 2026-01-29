package org.springblade.modules.sp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.ObjIdsDTO;
import org.springblade.modules.sp.entity.ElementParameter;
import org.springblade.modules.sp.entity.ExpInstances;
import org.springblade.modules.sp.service.ExpInstancesService;
import org.springblade.modules.sp.vo.ExpInstancesDetailVO;
import org.springblade.modules.sp.vo.NewBimListTreeVO;
import org.springblade.modules.sp.vo.ObjParameterVO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * ExpInstances控制器
 * @author dengtx
 * @since 2024年5月15日10:11:22
 */
@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/exp/instances")
@Api(value = "ExpInstances控制层", tags = "ExpInstances控制层")
public class ExpInstancesController extends BladeController {

    private ExpInstancesService expInstancesService;

    @SneakyThrows
    @GetMapping("/getExpInstances.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getExpInstances", notes = "getExpInstances")
    public ExpInstances getExpInstances(@RequestParam String streamId,@RequestParam String id,@RequestParam String commitId){
        ExpInstances expInstances = expInstancesService.getExpInstances(streamId,id,commitId);
        return expInstances;
    }

    @SneakyThrows
    @GetMapping("/getExpInstancesFilter.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getExpInstancesFilter", notes = "getExpInstancesFilter")
    public List<ExpInstances> getExpInstancesFilter(@RequestParam String streamId,@RequestParam String commitId,@RequestParam String category,@RequestParam String family,@RequestParam String type){
        List<ExpInstances> expInstances = expInstancesService.getExpInstancesFilter(streamId, category, family, type,commitId);
        return expInstances;
    }

    @SneakyThrows
    @PostMapping("/addExpInstances.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addExpInstances", notes = "addExpInstances")
    public R<Boolean> addExpInstances(@RequestBody ExpInstances expInstances){
        System.out.println("###########CommitId#################"+expInstances.getCommitId());
        log.info(expInstances.getCommitId());
        expInstances.setId(UUID.randomUUID().toString());
        boolean status = expInstancesService.addExpInstances(expInstances);
        return R.status(status);
    }

    @SneakyThrows
    @PostMapping("/deleteExpInstances.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteExpInstances", notes = "deleteExpInstances")
    public R<Boolean> deleteExpInstances(@RequestParam String commitId){
        boolean status = expInstancesService.deleteExpInstances(commitId);
        return R.status(status);
    }

    @SneakyThrows
    @PostMapping("/deleteExpInstancesById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteExpInstancesById", notes = "deleteExpInstancesById")
    public R<Boolean> deleteExpInstancesById(@RequestParam String id){
        boolean status = expInstancesService.deleteExpInstancesById(id);
        return R.status(status);
    }

//    --------------BIM构建树---------------------
    @SneakyThrows
    @GetMapping("/BimListTreeNew.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "BimListTreeNew", notes = "BimListTreeNew")
    public R<List<NewBimListTreeVO>>  BimListTreeNew(@RequestParam String streamId,@RequestParam String commitId){
//		boolean b = SignAuth.preHandle();
//		if (!b){
//			return R.fail(SpConstant.STR_MSG);
//		}
        List<NewBimListTreeVO> newBimListTreeVOS = expInstancesService.BimListTreeNew(streamId,commitId);
        return R.data(newBimListTreeVOS);
    }

    @SneakyThrows
    @GetMapping("/getAllExpInstances.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllExpInstances", notes = "getAllExpInstances")
    public R<List<ExpInstances>>  getAllExpInstances(@RequestParam String streamId,@RequestParam String commitId){
        List<ExpInstances> allExpInstances = expInstancesService.getAllExpInstances(streamId,commitId);
        return R.data(allExpInstances);
    }

    @SneakyThrows
    @PostMapping("/getExpInstancesByElements.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getExpInstancesByElements", notes = "getExpInstancesByElements")
    public R<List<ExpInstances>>  getExpInstancesByElements(@RequestParam String[] elementIds,@RequestParam String streamId,@RequestParam String commitId){
        List<ExpInstances> allExpInstances = expInstancesService.getExpInstancesByElements(streamId,elementIds,commitId);
        return R.data(allExpInstances);
    }

//    ---------模型属性----------

    /**
     * 根据objIds数组获取实例详细数据
     */
    @SneakyThrows
    @PostMapping("/getExpInstancesDetailByObjIds.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "根据objIds数组获取实例详细数据", notes = "传入objIds数组")
    public R<List<ExpInstancesDetailVO>> getExpInstancesDetailByObjIds(@RequestBody ObjIdsDTO dto) {
        List<ExpInstancesDetailVO> detailVOList = expInstancesService.getExpInstancesDetailByObjIds(dto.getStreamId(),dto.getBranchId(),dto.getObjIds());
        return R.data(detailVOList);
    }

}
