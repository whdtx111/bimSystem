package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.TemplateListDTO;
import org.springblade.modules.sp.entity.MetaData;
import org.springblade.modules.sp.entity.TemplateDO;
import org.springblade.modules.sp.entity.WbsParameters;
import org.springblade.modules.sp.entity.WbsParametersCopy;
import org.springblade.modules.sp.service.FileService;
import org.springblade.modules.sp.service.ITemplateService;
import org.springblade.modules.sp.service.WbsParametersCopyService;
import org.springblade.modules.sp.service.WbsParametersService;
import org.springblade.modules.sp.vo.TempWbsParametersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author huang can/dengtx
 * @since 2024/3/18 17:09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sp/template")
@CrossOrigin
@Api(value = "数据模板", tags = "数据模板")
public class TemplateController {

    @Autowired
    private ITemplateService templateService;
    @Autowired
    private WbsParametersCopyService wbsParametersCopyService;
    @Autowired
    private WbsParametersService wbsParametersService;
    @Autowired
    private FileService fileService;
    /**
     * 获取数据模板文件树
     */
    @SneakyThrows
    @GetMapping("/getTemplateList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getTemplateList", notes = "getTemplateList")
    public R<List<TemplateDO>> getTemplateList(@RequestParam String branchId,@RequestParam String streamId) {
        List<TemplateDO> templateListDto = templateService.getTemplateList(branchId,streamId);
        return R.data(templateListDto);
    }

    //根据模板id拿记录
    @SneakyThrows
    @GetMapping("/getTemplateById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getTemplateById", notes = "getTemplateById")
    public R<TemplateDO> getTemplateById(@RequestParam String id) {
        TemplateDO templateDO = templateService.getById(id);
        return R.data(templateDO);
    }

    /**
     * 新增文件树节点
     */
    @SneakyThrows
    @PostMapping("/addTemplateNode.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addTemplateNode", notes = "addTemplateNode")
    public R<Boolean> addTemplateNode(@RequestBody TemplateListDTO templateListDTO) {

        //新增模板
        TemplateDO res = new TemplateDO(templateListDTO.getPid(),templateListDTO.getName(),templateListDTO.getWbsGroup(),templateListDTO.getType(),templateListDTO.getBranchId(),templateListDTO.getStreamId(),templateListDTO.getDetail(),templateListDTO.getModifyUser(),templateListDTO.getRanknum());
        boolean status = templateService.addTemplateNode(res);

        //新增WBS副本
        String wbsId = templateListDTO.getWbsGroup();
        if (StringUtils.isNotEmpty(wbsId)) {
            List<WbsParametersCopy> wbsParametersList = new ArrayList<>();
            // 存储每个层级的最新WbsParameters对象，key为层级名称
            Map<String, WbsParametersCopy> latestNodeMap = new HashMap<>();
            List<WbsParameters> allWbsParametersByWbsId = wbsParametersService.reorderWbsParameters(wbsId);
            for (WbsParameters wbsParameters : allWbsParametersByWbsId) {
                // 根据层级获取父级节点
                String pid = null;
                if (latestNodeMap.containsKey(fileService.getEbsParentLevel(wbsParameters.getWbsLv()))) {
                    pid = latestNodeMap.get(fileService.getEbsParentLevel(wbsParameters.getWbsLv())).getId();
                }
                WbsParametersCopy wbsParametersNew = new WbsParametersCopy(UUID.randomUUID().toString(), wbsId,pid,res.getId(),wbsParameters.getWbsCode(),wbsParameters.getCategory(),wbsParameters.getWbsName(),wbsParameters.getWbsLv(),wbsParameters.getDetail(),wbsParameters.getModifyUser(),wbsParameters.getWbsStatus());
                wbsParametersList.add(wbsParametersNew);
                // 更新Map中当前层级的最新节点
                latestNodeMap.put(wbsParameters.getWbsLv(), wbsParametersNew);
            }
            // 批量插入数据
            if (!wbsParametersList.isEmpty()) {
                wbsParametersCopyService.insertWbsParametersCopyList(wbsParametersList); // 假设你有一个批量插入方法
            }
        }

        return  R.data(status,"模板ID为: "+res.getId());
    }



    /**
     * 删除文件树节点
     */
    @SneakyThrows
    @PostMapping("/deleteTemplateNode.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteTemplateNode", notes = "deleteTemplateNode")
    public R<Boolean> deleteTemplateNode(@RequestParam String id){
//        boolean b = SignAuth.preHandle();
//        if (!b){
//            return R.fail(SpConstant.STR_MSG);
//        }
        boolean status = templateService.deleteTemplateNode(id);
        return R.status(status);
    }

    /**
     * 更新文件数节点
     */
    @SneakyThrows
    @PostMapping("/updateTemplateNode.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateTemplateNode", notes = "updateTemplateNode")
    public R<Boolean> updateTemplateNode(@RequestBody TemplateDO templateDO){
//        boolean b = SignAuth.preHandle();
//        if (!b){
//            return R.fail(SpConstant.STR_MSG);
//        }
        boolean status = templateService.updateTemplateNode(templateDO);
        return R.status(status);
    }

    //    ---------------MetaData-------------------------

    @SneakyThrows
    @GetMapping("/getAllMetaData.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllMetaData", notes = "getAllMetaData")
    public R<String> getAllMetaData(@RequestParam String tempId) {
        try {
            TemplateDO byId = templateService.getById(tempId);
            return R.data(byId.getDetail());
        }catch (Exception e){
            return R.fail("获取列表失败！ "+e.getMessage());
        }
    }

//    ------------模板新增wbsparameterscopy---------------

    @SneakyThrows
    @PostMapping("/addTemplateWbsParametersCopy.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addTemplateWbsParametersCopy", notes = "addTemplateWbsParametersCopy")
    public R<String> addTemplateWbsParametersCopy(@RequestBody TempWbsParametersVO tempWbsParametersVO) {
        try {
            WbsParametersCopy wbsParametersCopy = new WbsParametersCopy();
            wbsParametersCopy.setWbsId(tempWbsParametersVO.getWbsId());
            wbsParametersCopy.setWbsCode(tempWbsParametersVO.getWbsCode());
            wbsParametersCopy.setTempId(tempWbsParametersVO.getTempId());
            wbsParametersCopy.setCategory(new String[]{});
            wbsParametersCopy.setWbsName(tempWbsParametersVO.getWbsName());
            wbsParametersCopy.setWbsLv(tempWbsParametersVO.getWbsLv());
            wbsParametersCopy.setModifyUser(tempWbsParametersVO.getModifyUser());
            wbsParametersCopy.setWbsStatus(0);
            wbsParametersCopy.setPid(tempWbsParametersVO.getPid());
            wbsParametersCopyService.addWbsParametersCopy(wbsParametersCopy);
            return R.data(wbsParametersCopy.getId());
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("新增失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteTemplateWbsParametersCopyById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteTemplateWbsParametersCopyById", notes = "deleteTemplateWbsParametersCopyById")
    public R<Boolean> deleteTemplateWbsParametersCopyById(@RequestParam String id) {
        try {
            boolean b = wbsParametersCopyService.deleteWbsParametersCopy(id);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("删除失败！ "+e.getMessage());
        }
    }
}
