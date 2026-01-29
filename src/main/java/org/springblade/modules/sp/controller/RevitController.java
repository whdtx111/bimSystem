package org.springblade.modules.sp.controller;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.FinalExcel01;
import org.springblade.modules.sp.dto.RevitAllUPDTO;
import org.springblade.modules.sp.dto.RevitFilesDTO;
import org.springblade.modules.sp.entity.*;
import org.springblade.modules.sp.service.*;
import org.springblade.modules.sp.service.impl.LibWbsTempServiceImpl;
import org.springblade.modules.sp.vo.Page03VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 *  revit控制层
 */
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/revit")
@Api(value = "Revit控制层", tags = "Revit控制层")
public class RevitController {

    private static final String REQUEST_URL2 = "http://10.5.58.138:30031/getProgress";
    private static final String UPLOAD_URL2 = "http://10.5.58.138:30031/uploadFile";

    private static final String REQUEST_URL1 = "http://10.5.146.152:30030/getProgress";
    private static final String UPLOAD_URL1 = "http://10.5.146.152:30030/uploadFile";

    @Autowired
    private HttpService httpService;
    @Autowired
    private RevitAllService revitAllService;
    @Autowired
    private StreamFilesService streamFilesService;
    @Autowired
    private FileService fileService;
    @Autowired
    private RevitFilesService revitFilesService;
    @Autowired
    private LibraryService libraryService;
    @Autowired
    private LibParametersService libParametersService;
    @Autowired
    private LibWbsTempService libWbsTempService;
    @Autowired
    private WbsService wbsService;
    @Autowired
    private WbsParametersService wbsParametersService;
    @Autowired
    private ElementParameterService elementParameterService;
    @Autowired
    private ElementWbsService elementWbsService;
    @Autowired
    private ExpInstancesService expInstancesService;
    @Autowired
    private WbsParametersCopyService wbsParametersCopyService;

    @SneakyThrows
    @PostMapping("/revitSlb.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "revitSlb", notes = "revitSlb")
    public R revitSlb(
            @RequestBody MultipartFile file,@RequestHeader String filename,@RequestHeader String userId,
            @RequestHeader String streamId,@RequestHeader String branchId,@RequestHeader String branchName) throws Exception {
        Map<String, String> res1 = httpService.isProgressComplete(REQUEST_URL1);
        if (!"200".equals(res1.get("status")) || "1".equals(res1.get("uploadState"))) {
            Map<String,String> res2 = httpService.isProgressComplete(REQUEST_URL2);
            if (!"200".equals(res2.get("status")) || "1".equals(res2.get("uploadState"))) {
                if ("1".equals(res2.get("uploadState"))){
                    res2.put("message","上传失败,revit服务器正在转换");
                }
                return R.fail(res2.get("message"));
            }
            Map<String, String> response = httpService.uploadFile(file, filename, userId, streamId, branchId, branchName, UPLOAD_URL2);
            return R.data(response);
        } else {
            Map<String, String> response = httpService.uploadFile(file, filename, userId, streamId, branchId, branchName,UPLOAD_URL1);
            return R.data(response);
        }
    }

    @SneakyThrows
    @GetMapping("/getProgress.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getProgress", notes = "getProgress")
    public R getProgress(@RequestParam String ip){
            String url = "http://"+ip+"/getProgress";
            Map<String,String> res2 = httpService.isProgressComplete(url);
                if ("2".equals(res2.get("uploadState"))){
                    res2.put("message","上传成功");
                    return R.success(res2.get("message"));
                }
                if ("0".equals(res2.get("uploadState"))){
                    res2.put("message","上传失败,revit未转换");
                }
                if ("1".equals(res2.get("uploadState"))){
                    res2.put("message","上传失败,revit服务器正在转换");
                }
                if ("-1".equals(res2.get("uploadState"))){
                    res2.put("message","上传失败,revit转换失败");
                }
//                return R.fail(res2.get("message"));
                return R.data(res2);
        }

//    --------------RevitALL属性回传-----------------

    @SneakyThrows
    @GetMapping("/searchFilter.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "searchFilter", notes = "searchFilter")
    public R<List<RevitAll>> searchFilter(@RequestParam Optional<String> elementId, @RequestParam Optional<String> streamId, @RequestParam Optional<String> branchId,@RequestParam Optional<Integer> isfinished){
        try {
            List<RevitAll> revitAll = revitAllService.searchFilter(elementId.orElse(null), streamId.orElse(null), branchId.orElse(null),isfinished.orElse(null));
            return R.data(revitAll);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("查询异常");
        }
    }

    @SneakyThrows
    @GetMapping("/getById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getById", notes = "getById")
    public R<RevitAll> getById(@RequestParam String id){
        try {
            RevitAll revitAll = revitAllService.getById(id);
            return R.data(revitAll);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("查询异常");
        }
    }


    @SneakyThrows
    @PostMapping("/uploadRevitAll.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "UploadRevitAll", notes = "UploadRevitAll")
    public R UploadRevitAll(@RequestBody RevitAll revitAll){
        try {
            List<RevitAll> ress = revitAllService.searchFilter(revitAll.getElementId(), revitAll.getStreamId(), revitAll.getBranchId(),revitAll.getIsfinished());
            if (ress == null || ress.size() == 0){
                revitAll.setModifyTime(new DateTime());
                boolean b = revitAllService.addRevitAll(revitAll);
                return R.success("新增成功！");
            }else {
                for (RevitAll re : ress) {
                    revitAll.setId(re.getId());
                    revitAll.setStatus(re.getStatus());
                    revitAll.setModifyTime(new DateTime());
                    revitAllService.updateRevitAll(revitAll);
                }
                return R.success("更新成功!");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("添加异常");
        }
    }

    @SneakyThrows
    @PostMapping("/uploadRevitAlls.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "UploadRevitAlls", notes = "UploadRevitAlls")
    public R uploadRevitAlls(@RequestBody List<RevitAll> revitAlls){
        try {
            for (RevitAll revitAll : revitAlls) {
                List<RevitAll> ress = revitAllService.searchFilter(revitAll.getElementId(), revitAll.getStreamId(), revitAll.getBranchId(),revitAll.getIsfinished());
                if (ress == null || ress.size() == 0){
                    revitAll.setModifyTime(new DateTime());
                    boolean b = revitAllService.addRevitAll(revitAll);
                }else {
                    for (RevitAll re : ress) {
                        revitAll.setId(re.getId());
                        revitAll.setStatus(re.getStatus());
                        revitAll.setModifyTime(new DateTime());
                        revitAllService.updateRevitAll(revitAll);
                    }
                }
            }
            return R.success("更新成功!");
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("添加异常");
        }
    }


    @SneakyThrows
    @PostMapping("/addRevitAll.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addRevitAll", notes = "addRevitAll")
    public R<Boolean> addRevitAll(@RequestBody RevitAll revitAll){
        try {
            revitAll.setModifyTime(new DateTime());
            boolean b = revitAllService.addRevitAll(revitAll);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("添加异常");
        }
    }

    @SneakyThrows
    @PostMapping("/updateRevitAll.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateRevitAll", notes = "updateRevitAll")
    public R<Boolean> updateRevitAll(@RequestBody RevitAll revitAll){
        try {
            revitAll.setModifyTime(new DateTime());
            boolean b = revitAllService.updateRevitAll(revitAll);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("更新异常");
        }
    }

    @SneakyThrows
    @PostMapping("/updateIsFinished.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateIsFinished", notes = "updateIsFinished")
    public R<String> updateIsFinished(@RequestBody RevitAllUPDTO revitAllUPDTO){
        try {
            revitAllService.updateIsFinished(revitAllUPDTO);
            return R.data(revitAllUPDTO.getId());
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("更新异常");
        }
    }

//    -------------Revit文件上传------------------

    @SneakyThrows
    @PostMapping("/uploadFiles.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadFiles", notes = "uploadFiles")
    public R uploadFiles(@RequestParam List<MultipartFile> files, @RequestHeader String streamId, @RequestHeader String branchId, @RequestHeader String commitId, @RequestHeader String type, @RequestHeader String modifyUser,@RequestHeader String masterFileName){
        try {
            Map<String,String> map = new HashMap<>();
            for (MultipartFile file : files) {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                map.put("streamId",streamId);
                map.put("branchId",branchId);
                map.put("commitId",commitId);
                map.put("masterFileName",masterFileName);
                map.put("type",type);
                map.put("modifyUser",modifyUser);
                map.put("size",file.getSize()+"");
                RevitFiles savedFile = fileService.saveRevitFile(fileName,file,map);
            }
            return R.success("File uploaded successfully");
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("File uploaded fail"+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getFilePath.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getFilePath", notes = "getFilePath")
    public R<List<RevitFilesDTO>> getFilePath(@RequestParam String streamId, @RequestParam String branchId, @RequestParam String commitId){
        try {
            List<RevitFilesDTO> resList = new ArrayList<>();
            List<RevitFiles> files = revitFilesService.searchFilter(streamId, branchId, commitId);
            for (RevitFiles file : files) {
                RevitFilesDTO res = new RevitFilesDTO();
                res.setFileUrl(file.getFileUrl());
                res.setName(file.getName());
                res.setMasterFileName(file.getMasterFileName());
                resList.add(res);
            }
            return R.data(resList);
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }


//    --------------LibParameters---------------

    @SneakyThrows
    @GetMapping("/getLibparametersById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLibparametersById", notes = "getLibparametersById")
    public R<LibParameters> getLibparametersById(@RequestParam String id){
        try {
            LibParameters res = libParametersService.getById(id);
            return R.data(res);
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getLibparametersList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLibparametersList", notes = "getLibparametersList")
    public R<List<LibParameters>> getLibparametersList(){
        try {
            List<LibParameters> res = libParametersService.getAllLibParameters();
            return R.data(res);
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/filterLibParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "filterLibParameters", notes = "filterLibParameters")
    public R<List<LibParameters>> filterLibParameters(@RequestParam Optional<String> wbsCode,@RequestParam Optional<String> tempId,@RequestParam Optional<String> libId){
        try {
            List<LibParameters> res = libParametersService.filterLibParameters(wbsCode.orElse(null),tempId.orElse(null),libId.orElse(null));
            return R.data(res);
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addLibparameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addLibparameters", notes = "addLibparameters")
    public R<Boolean> addLibparameters(@RequestBody LibParameters libParameters){
        try {
            boolean b = libParametersService.addLibParameters(libParameters);
            return R.data(b);
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }


    @SneakyThrows
    @PostMapping("/updateLibparameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateLibparameters", notes = "updateLibparameters")
    public R<Boolean> updateLibparameters(@RequestBody LibParameters libParameters){
        try {
            boolean b = libParametersService.updateLibParameters(libParameters);
            return R.data(b);
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteLibparameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteLibparameters", notes = "deleteLibparameters")
    public R<Boolean> deleteLibparameters(@RequestParam String id,@RequestParam String wbsCode,@RequestParam String tempId){
        try {

//            boolean res = false;
            boolean b = libParametersService.deleteLibParameters(id);
//            boolean c = libraryService.deleteLibrary(libParametersService.getById(id).getLibId());
//            LibWbsTemp libWbsTemp = libWbsTempService.getLibWbsTemp(wbsCode, tempId);
//            List<String> libIdsList = new ArrayList<>(Arrays.asList(libWbsTemp.getLibIds()));
//            libIdsList.remove(libParametersService.getById(id).getLibId());
//            String[] newLibIds = libIdsList.toArray(new String[0]);
//            libWbsTemp.setLibIds(newLibIds);
//            boolean d = libWbsTempService.updateLibWbsTemp(libWbsTemp);
//            if (b&&c&&d) res = true;
            return R.data(b);
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }

//    ---------------数据回传总表--------------
    @SneakyThrows
    @GetMapping("/getPage02.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getPage02", notes = "getPage02")
    public R<List<FinalExcel01>> getPage02(@RequestParam String tempId,@RequestParam String wbsLv) {
        try {
            List<FinalExcel01> res = new ArrayList<>();
            List<LibParameters> libParameters = libParametersService.filterLibParameters("", tempId, "");
            for (LibParameters libParameter : libParameters) {
                WbsParametersCopy wbsParameters = wbsParametersCopyService.getWbsParametersCopyByWbsCodeTempId(libParameter.getWbsCode(), tempId);
                if (wbsLv.equals(wbsParameters.getWbsLv())) {
                        FinalExcel01 finalExcel01 = new FinalExcel01();
                        Library lib = libraryService.getById(libParameter.getLibId());
                        finalExcel01.setWbsName(wbsParameters.getWbsName());
                        finalExcel01.setName(libParameter.getName());
                        finalExcel01.setValue(libParameter.getValue());
                        finalExcel01.setUnit(libParameter.getUnit());
                        finalExcel01.setDataType(lib.getDataType());
                        finalExcel01.setDetail(lib.getDetail());
                        res.add(finalExcel01);
                }
            }
            return R.data(res);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getPage03.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getPage03", notes = "getPage03")
    public R<List<Page03VO>> getPage03(@RequestParam String tempId,@RequestParam String streamId,@RequestParam String branchId,@RequestParam String commitId,@RequestParam String wbsCode) {
        try {
            List<Page03VO> resList = new ArrayList<>();
            if (StringUtils.isEmpty(wbsCode)){
                //wbsCode为空，根据模板获取全量
                List<String> wbsCodeByTempId = libWbsTempService.getWbsCodeByTempId(tempId);
                for (String code : wbsCodeByTempId) {
                    //通过wbsCode和tempId获取wbsName
                    WbsParametersCopy wbsParameters = wbsParametersCopyService.getWbsParametersCopyByWbsCodeTempId(wbsCode, tempId);
                    //获取wbs下挂的构建Id
                    ElementWbs elementWbs = elementWbsService.getElementWbs(code, branchId, tempId);
                    String[] elementId = elementWbs.getElementId();
                    List<ExpInstances> expInstancesByElements = expInstancesService.getExpInstancesByElements(streamId, elementId, commitId);
                    for (ExpInstances expInstances : expInstancesByElements) {
                        // 获取 RevitAll 数据
                        List<RevitAll> revitAlls = revitAllService.searchFilter(expInstances.getElementId(), streamId, branchId, 1);
                        Map<String, JSONObject> detailMap = new HashMap<>();
                        if (!revitAlls.isEmpty() && revitAlls.get(0).getDetail() != null) {
                            List<JSONObject> detailList = revitAlls.get(0).getDetail();
                            for (JSONObject detail : detailList) {
                                detailMap.put(detail.getString("name"), detail);
                            }
                        }

                        //获取用户属性
                        LibWbsTemp libWbsTemp = libWbsTempService.getLibWbsTemp(code, tempId);
                        if (ObjectUtils.isNotEmpty(libWbsTemp.getLibIds())){
                            for (String libId : libWbsTemp.getLibIds()) {
                                Page03VO res = new Page03VO();
                                res.setWbsCode(code);
                                res.setWbsName(wbsParameters.getWbsName());
                                res.setElementId(expInstances.getElementId());
                                res.setType(expInstances.getType());
                                Library byId = libraryService.getById(libId);
                                res.setName(byId.getName());

                                // 从 detail 中获取对应的值和单位
                                JSONObject detailInfo = detailMap.get(byId.getName());
                                if (detailInfo != null) {
                                    res.setValue(detailInfo.getString("value"));
                                    String detailUnit = detailInfo.getString("units");
                                    res.setUnit(StringUtils.hasText(detailUnit) ? detailUnit : byId.getUnits());
                                } else {
                                    res.setValue("");
                                    res.setUnit(byId.getUnits());
                                }

                                res.setCategory(byId.getCategory());
                                resList.add(res);
                            }
                        }

                        //获取构建属性
                        if (ObjectUtils.isNotEmpty(libWbsTemp.getParameterIds())){
                            for (String pa : libWbsTemp.getParameterIds()) {
                                Page03VO res = new Page03VO();
                                res.setWbsCode(code);
                                res.setWbsName(wbsParameters.getWbsName());
                                res.setElementId(expInstances.getElementId());
                                res.setType(expInstances.getType());
                                ElementParameter elementParameter = elementParameterService.getById(pa);
                                res.setName(elementParameter.getName());

                                // 从 detail 中获取对应的值和单位
                                JSONObject detailInfo = detailMap.get(elementParameter.getName());
                                if (detailInfo != null) {
                                    res.setValue(detailInfo.getString("value"));
                                    String detailUnit = detailInfo.getString("units");
                                    res.setUnit(StringUtils.hasText(detailUnit) ? detailUnit : elementParameter.getUnits());
                                } else {
                                    res.setValue("");
                                    res.setUnit(elementParameter.getUnits());
                                }

                                res.setCategory(elementParameter.getCategory());
                                resList.add(res);
                            }
                        }
                    }
                }
                return R.data(resList);
            }else {
                //通过wbsCode获取wbsName
                WbsParametersCopy wbsParameters = wbsParametersCopyService.getWbsParametersCopyByWbsCodeTempId(wbsCode, tempId);
                //获取wbs下挂的构建Id
                ElementWbs elementWbs = elementWbsService.getElementWbs(wbsCode, branchId, tempId);
                String[] elementId = elementWbs.getElementId();
                List<ExpInstances> expInstancesByElements = expInstancesService.getExpInstancesByElements(streamId, elementId, commitId);
                for (ExpInstances expInstances : expInstancesByElements) {
                    List<RevitAll> revitAlls = revitAllService.searchFilter(expInstances.getElementId(), streamId, branchId, 1);
                    Map<String, JSONObject> detailMap = new HashMap<>();
                    if (!revitAlls.isEmpty() && revitAlls.get(0).getDetail() != null) {
                        List<JSONObject> detailList = revitAlls.get(0).getDetail();
                        for (JSONObject detail : detailList) {
                            detailMap.put(detail.getString("name"), detail);
                        }
                    }

                    //获取用户属性
                    LibWbsTemp libWbsTemp = libWbsTempService.getLibWbsTemp(wbsCode, tempId);
                    if (ObjectUtils.isNotEmpty(libWbsTemp.getLibIds())) {
                        for (String libId : libWbsTemp.getLibIds()) {
                            Page03VO res = new Page03VO();
                            res.setWbsCode(wbsCode);
                            res.setWbsName(wbsParameters.getWbsName());
                            res.setElementId(expInstances.getElementId());
                            res.setType(expInstances.getType());
                            Library byId = libraryService.getById(libId);
                            res.setName(byId.getName());

                            // 从 detail 中获取对应的值和单位
                            JSONObject detailInfo = detailMap.get(byId.getName());
                            if (detailInfo != null) {
                                res.setValue(detailInfo.getString("value"));
                                String detailUnit = detailInfo.getString("units");
                                res.setUnit(StringUtils.hasText(detailUnit) ? detailUnit : byId.getUnits());
                            } else {
                                res.setValue("");
                                res.setUnit(byId.getUnits());
                            }

                            res.setCategory(byId.getCategory());
                            resList.add(res);
                        }
                    }

                    //获取构建属性
                    if (ObjectUtils.isNotEmpty(libWbsTemp.getParameterIds())) {
                        for (String pa : libWbsTemp.getParameterIds()) {
                            Page03VO res = new Page03VO();
                            res.setWbsCode(wbsCode);
                            res.setWbsName(wbsParameters.getWbsName());
                            res.setElementId(expInstances.getElementId());
                            res.setType(expInstances.getType());
                            ElementParameter elementParameter = elementParameterService.getById(pa);
                            res.setName(elementParameter.getName());

                            // 从 detail 中获取对应的值和单位
                            JSONObject detailInfo = detailMap.get(elementParameter.getName());
                            if (detailInfo != null) {
                                res.setValue(detailInfo.getString("value"));
                                String detailUnit = detailInfo.getString("units");
                                res.setUnit(StringUtils.hasText(detailUnit) ? detailUnit : elementParameter.getUnits());
                            } else {
                                res.setValue("");
                                res.setUnit(elementParameter.getUnits());
                            }

                            res.setCategory(elementParameter.getCategory());
                            resList.add(res);
                        }
                    }
                }
            return R.data(resList);
                }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }
}
