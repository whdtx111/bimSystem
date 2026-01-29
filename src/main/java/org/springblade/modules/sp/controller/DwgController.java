package org.springblade.modules.sp.controller;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.modules.sp.constant.SpConstant;
import org.springblade.modules.sp.dto.DwgDTO;
import org.springblade.modules.sp.entity.DwgFile;
import org.springblade.modules.sp.service.DwgService;
import org.springblade.modules.sp.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DWG控制器
 * @author dengtx
 * @since 2024年5月6日16:49:00
 */
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/dwg")
@Api(value = "DWG控制层", tags = "DWG控制层")
public class DwgController extends BladeController {

    private static final Logger logger = LogManager.getLogger(DwgController.class);
    @Autowired
    private DwgService dwgService;
    @Autowired
    private FileService fileService;

    @SneakyThrows
    @GetMapping("/getAllDwg.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllDwg", notes = "getAllDwg")
    public R<List<DwgFile>> getAllDwg(@RequestParam String projectId) {
        List<DwgFile> dwgFileList = dwgService.getAllDwg(projectId);
        return R.data(dwgFileList);
    }

    @SneakyThrows
    @GetMapping("/getByMapId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getByMapId", notes = "getByMapId")
    public R<DwgFile> getByMapId(@RequestParam String mapId,@RequestParam String projectId) {
        DwgFile byMapId = dwgService.getByMapId(mapId, projectId);
        if (ObjectUtils.isNotEmpty(byMapId)){
            R.success("查询dwg成功！");
        }
        return R.data(byMapId);
    }

    @SneakyThrows
    @GetMapping("/getById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getById", notes = "getById")
    public R<DwgFile> getById(@RequestParam String id) {
        DwgFile byId = dwgService.getById(id);
        if (ObjectUtils.isNotEmpty(byId)){
            R.success("查询dwg成功！");
        }
        return R.data(byId);
    }

    @SneakyThrows
    @PostMapping("/addDwgFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addDwgFile", notes = "addDwgFile")
    public R<String> addDwgFile(@RequestParam("uploadFile") MultipartFile uploadFile, @RequestParam String projectId,@RequestParam String name,@RequestParam String mapId,@RequestParam String detail,@RequestParam String modifyUser){
//        boolean b = SignAuth.preHandle();
//        if (!b){
//            return R.fail(SpConstant.STR_MSG);
//        }
        try {
            // 反序列化 detail
            JSONObject detailJson = JSON.parseObject(detail);

            Map<String,String> map = new HashMap<>();
            map.put("projectId",projectId);
            map.put("name",name);
            map.put("mapId",mapId);
            map.put("modifyUser",modifyUser);
            DwgFile file = fileService.saveDwgFile(uploadFile, map, detailJson, new String[0]);

            if (file != null){
                return R.data(file.getUrl());
            }else {
                return R.fail("dwg文件,文件名:"+file.getName()+"新增失败!请检查是否已存在该文件!请勿重复添加!");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("添加文件异常，异常为:"+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteDwgFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteDwgFile", notes = "deleteDwgFile")
    public R<Boolean> deleteDwgFile(@RequestParam String mapId,@RequestParam String projectId){
//        boolean b = SignAuth.preHandle();
//        if (!b){
//            return R.fail(SpConstant.STR_MSG);
//        }
        boolean status = dwgService.deleteDwgFile(mapId,projectId);
        return R.status(status);
    }

    @SneakyThrows
    @PostMapping("/updateDwgFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateDwgFile", notes = "updateDwgFile")
    public R<Boolean> updateDwgFile(@Valid @RequestBody DwgDTO dto){
//        boolean b = SignAuth.preHandle();
//        if (!b){
//            return R.fail(SpConstant.STR_MSG);
//        }
        if (StringUtils.isEmpty(dto.getMapId())){
            logger.error("mapId必须！");
            return R.fail("mapId必须！");
        }
        if (StringUtils.isEmpty(dto.getProjectId())){
            logger.error("projectId必须！");
            return R.fail("projectId必须！");
        }
        DwgFile dwgFile =  dwgService.getByMapId(dto.getMapId(),dto.getProjectId());
        if (StringUtils.isNotEmpty(dto.getProjectId())){
            dwgFile.setProjectId(dto.getProjectId());
        }
        if (ObjectUtils.isNotEmpty(dto.getDetail())){
            dwgFile.setDetail(dto.getDetail());
        }
        if (StringUtils.isNotEmpty(dto.getName())){
            dwgFile.setName(dto.getName());
        }
        boolean status = dwgService.updateDwgFile(dwgFile);
        if (status){
            logger.info("update success!");
        }
        return R.status(status);
    }


    @SneakyThrows
    @PostMapping("/linkObjects.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "linkObjects", notes = "linkObjects")
    public R linkObjects(@RequestParam String id,@RequestParam String[] objectId){
        try {
            String msg = "开始DWG文件绑定模型任务！";
            DwgFile file = dwgService.getById(id);
            if (ObjectUtil.isEmpty(file)){
                return R.fail("文件不存在！");
            }
            boolean res = dwgService.linkObjects(id, objectId);
            String objectIds = "";
            for (String s : objectId) {
                objectIds += s;
            }
            if (res){
                msg = "文件："+file.getName()+"绑定模型成功！！模型ID："+objectIds;
                return R.success(msg);
            }else {
                msg = "文件："+file.getName()+"绑定模型失败！！模型ID："+objectIds;
                return R.fail(msg);
            }
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }

//    后端加盐demo
//    @SneakyThrows
//    @PostMapping("/getSalt.do")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "getSalt", notes = "getSalt")
//    public String getSalt(){
//
//        String s = BCrypt.hashpw("QSCFTqwert13524", BCrypt.gensalt());
//        return s;
//    }
}
