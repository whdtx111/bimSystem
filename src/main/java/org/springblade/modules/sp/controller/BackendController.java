package org.springblade.modules.sp.controller;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.EbsExcelDTO;
import org.springblade.modules.sp.dto.EbsListDTO;
import org.springblade.modules.sp.dto.EbsParametesCategoryDTO;
import org.springblade.modules.sp.dto.WbsExcelDTO;
import org.springblade.modules.sp.entity.*;
import org.springblade.modules.sp.excel.EasyExcelReadUtils;
import org.springblade.modules.sp.service.*;
import org.springblade.modules.sp.vo.PageEbs;
import org.springblade.modules.sp.vo.PageWbs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 资源空间控制层
 * @author dengtx
 * @since 2024年8月15日17:05:40
 */
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/backend")
@Api(value = "资源空间控制层", tags = "资源空间控制层")
public class BackendController extends BladeController {

    @Autowired
    private EbsService ebsService;
    @Autowired
    private WbsService wbsService;
    @Autowired
    private EbsParametersService ebsParametersService;
    @Autowired
    private WbsParametersService wbsParametersService;
    @Autowired
    private WbsParametersCopyService wbsParametersCopyService;
    @Autowired
    private WbsParametersCopyColorService wbsParametersCopyColorService;
    @Autowired
    private FileService fileService;
    @Autowired
    private WbsLvService wbsLvService;
    @Autowired
    private IUsersService usersService;
    @Autowired
    private SubscriptionOrderService subscriptionOrderService;
    @Autowired
    private SubscriptionConfigService subscriptionConfigService;


////    WBS/EBS模版上传
//@SneakyThrows
//@PostMapping("/uploadFile.do")
//@ApiOperationSupport(order = 2)
//@ApiOperation(value = "uploadFile", notes = "uploadFile")
//public R uploadFile(@RequestParam MultipartFile file,@RequestHeader("Authorization") String token){
//    try{
//        // 通过token获取用户信息
//        Users user = usersService.getUserByToken(token);
//        if (user == null) {
//            return R.fail("用户未登录或token已过期");
//        }
//
//        // 获取用户订阅信息
//        SubscriptionOrder order = subscriptionOrderService.getByUserId(user.getId());
//        if (order == null) {
//            return R.fail("用户未订阅");
//        }
//
//        // 获取套餐配置
//        SubscriptionConfig config = subscriptionConfigService.getById(order.getConfigId());
//        Integer totalStorage = config.getStorage();
//        String unit = config.getStorageUnit();
//
//        // 计算总容量（转换为KB）
//        long totalStorageKB;
//        switch (unit.toUpperCase()) {
//            case "G":
//                totalStorageKB = totalStorage * 1024 * 1024L;
//                break;
//            case "M":
//                totalStorageKB = totalStorage * 1024L;
//                break;
//            case "T":
//                totalStorageKB = totalStorage *1024 * 1024 * 1024L;
//                break;
//            default:
//                return R.fail("存储单位配置错误");
//        }
//
//        // 计算可用容量（KB）
//        long usedStorageKB = order.getUsedStorage() != null ? order.getUsedStorage() : 0;
//        long availableStorageKB = totalStorageKB - usedStorageKB;
//
//        // 获取文件大小（KB）
//        long fileSize = file.getSize() / 1024; // 转换为KB
//
//        // 校验存储空间
//        if (fileSize > availableStorageKB) {
//            return R.fail("存储空间不足，当前可用：" + availableStorageKB + "KB，文件大小：" + fileSize + "KB");
//        }
////            保存文件
//        Map<String,String> map = new HashMap<>();
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        map.put("modifyUser",user.getName());
//        map.put("size",file.getSize()+"");
//        fileService.saveFile(fileName,file,map);
//    }catch (Exception e){
//
//    }
//}

    //    -------------EBS------------
    @SneakyThrows
    @GetMapping("/getAllEbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllEbs", notes = "getAllEbs")
    public R<List<Ebs>> getAllEbs() {
        try {
            List<Ebs> allEbs = ebsService.getAllEbs();
            R.success("获取EBS列表成功！");
            return R.data(allEbs);
        }catch (Exception e){
            return R.fail("获取列表失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getEbsById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getEbsById", notes = "getEbsById")
    public R<Ebs> getEbsById(@RequestParam String id) {
        try {
            Ebs ebs = ebsService.getById(id);
            R.success("获取EBS成功！");
            return R.data(ebs);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/filterEbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "filterEbs", notes = "filterEbs")
    public R<PageEbs> filterEbs(@RequestParam Optional<String> type, @RequestParam Optional<String> source, @RequestParam Optional<Integer> ebsStatus, @RequestParam Optional<String> auth, @RequestParam Optional<String> ebsName, @RequestParam Optional<String> ebsCode, @RequestParam Integer pageSize, @RequestParam Integer currentPage) {
        try {
            PageEbs pageEbs = new PageEbs();
            if (pageSize == 0 || currentPage == 0 || pageSize == null || currentPage == null){
                return R.fail("分页数据获取失败)");
            }
            Page<Ebs> ebsList = ebsService.filterEbs( ebsCode.orElse(null),
                    ebsName.orElse(null),
                    type.orElse(null),
                    source.orElse(null),
                    auth.orElse(null),
                    ebsStatus.orElse(null),
                    pageSize,
                    currentPage);
            pageEbs.setPageEbs(ebsList);
            pageEbs.setTotal(ebsList.getTotal());
            return R.data(pageEbs);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addEbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addEbs", notes = "addEbs")
    public R<String> addEbs(@RequestBody Ebs ebs) {
        try {
            ebs.setEbsStatus(0);
            //接口导入source=“user”
            ebs.setSource("user");
            boolean res = ebsService.addEbs(ebs);
            if (res){
                R.success("新增EBS成功！");
                return R.data(ebs.getId());
            }
            return R.data("新增失败");
        }catch (Exception e){
            return R.fail("新增EBS异常！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addStaticEbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addStaticEbs", notes = "addStaticEbs")
    public R<String> addStaticEbs(@RequestBody Ebs ebs) {
        try {
            ebs.setEbsStatus(0);
            ebs.setSource("SMARTLINK");
            boolean res = ebsService.addEbs(ebs);
            if (res){
                R.success("预置EBS成功！");
                return R.data(ebs.getId());
            }
            return R.data("新增失败");
        }catch (Exception e){
            return R.fail("新增EBS异常！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addEbsCopy.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addEbsCopy", notes = "addEbsCopy")
    public R<String> addEbsCopy(@RequestParam String ebsId) {
        try {
            List<EbsParameters> ebsParametersList = new ArrayList<>();
            // 存储每个层级的最新WbsParameters对象，key为层级名称
            Map<String, EbsParameters> latestNodeMap = new HashMap<>();
            Ebs ebs = ebsService.getById(ebsId);
            Ebs ebsNew = new Ebs(UUID.randomUUID().toString(),ebs.getEbsCode(),ebs.getEbsName()+"_copy",ebs.getEbsLv(),ebs.getType(),ebs.getSource(),ebs.getAuth(),ebs.getEbsStatus(),ebs.getModifyUser());
            ebsNew.setSource("user");
            ebsService.addEbs(ebsNew);
            List<EbsParameters> allEbsParametersByEbsId = ebsParametersService.getAllEbsParametersByEbsId(ebsId);
            for (EbsParameters ebsParameters : allEbsParametersByEbsId) {
                // 根据层级获取父级节点
                String pid = null;
                if (latestNodeMap.containsKey(fileService.getEbsParentLevel(ebsParameters.getEbsLv()))) {
                    pid = latestNodeMap.get(fileService.getEbsParentLevel(ebsParameters.getEbsLv())).getId();
                }
                EbsParameters ebsParametersNew = new EbsParameters(UUID.randomUUID().toString(), ebsNew.getId(),pid,ebsParameters.getEbsCode(),ebsParameters.getCategory(),ebsParameters.getEbsName(),ebsParameters.getEbsLv(),ebsParameters.getDetail(),ebsParameters.getModifyUser(),ebsParameters.getEbsStatus());
                ebsParametersList.add(ebsParametersNew);
                // 更新Map中当前层级的最新节点
                latestNodeMap.put(ebsParameters.getEbsLv(), ebsParametersNew);
            }
            // 批量插入数据
            if (!ebsParametersList.isEmpty()) {
                ebsParametersService.insertEbsParametersList(ebsParametersList); // 假设你有一个批量插入方法
            }
            return R.data(ebsNew.getId());
        }catch (Exception e){
            return R.fail("复制EBS异常！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateEbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateEbs", notes = "updateEbs")
    public R<Boolean> updateEbs(@RequestBody Ebs ebs) {
        try {
            boolean res = ebsService.updateEbs(ebs);
            if (res){
                R.success("修改EBS成功！");
            }
            return R.data(res);
        }catch (Exception e){
            return R.fail("修改EBS失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteEbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteEbs", notes = "deleteEbs")
    public R<Boolean> deleteEbs(@RequestParam String id) {
        try {
            boolean res = ebsService.deleteEbs(id);
            if (res){
                R.success("删除EBS成功！");
            }
            return R.data(res);
        }catch (Exception e){
            return R.fail("删除EBS失败！ "+e.getMessage());
        }
    }

    //    -------------EbsParameters------------

    @SneakyThrows
    @GetMapping("/getEbsParametersById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getEbsParametersById", notes = "getEbsParametersById")
    public R<EbsParameters> getEbsParametersById(@RequestParam String id) {
        try {
            EbsParameters ebsParameters = ebsParametersService.getById(id);
            R.success("获取EbsParameters成功！");
            return R.data(ebsParameters);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getAllEbsParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllEbsParameters", notes = "getAllEbsParameters")
    public R<List<EbsParameters>> getAllEbsParameters() {
        try {
            List<EbsParameters> allEbsParameters = ebsParametersService.getAllEbsParameters();
            R.success("获取EbsParameters列表成功！");
            return R.data(allEbsParameters);
        }catch (Exception e){
            return R.fail("获取列表失败！ "+e.getMessage());
        }
    }



    @SneakyThrows
    @GetMapping("/getAllEbsParametersByEbsId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllEbsParametersByEbsId", notes = "getAllEbsParametersByEbsId")
    public R<List<EbsParameters>> getAllEbsParametersByEbsId(@RequestParam String ebsId) {
        try {
            List<EbsParameters> allEbsParameters = ebsParametersService.getAllEbsParametersByEbsId(ebsId);
            R.success("获取EbsParameters列表成功！");
            return R.data(allEbsParameters);
        }catch (Exception e){
            return R.fail("获取列表失败！ "+e.getMessage());
        }
    }


    @SneakyThrows
    @PostMapping("/addEbsParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addEbsParameters", notes = "addEbsParameters")
    public R<String> addEbsParameters(@RequestBody EbsParameters ebsParameters) {
        try {
            ebsParameters.setCategory(new String[]{});
            ebsParameters.setEbsStatus(0);
            boolean res = ebsParametersService.addEbsParameters(ebsParameters);
            Ebs byId = ebsService.getById(ebsParameters.getEbsId());
            byId.setEbsStatus(1);
            ebsService.updateEbs(byId);
            if (res){
                R.success("新增EbsParameters成功！");
                return R.data(ebsParameters.getId());
            }
            return R.data("新增失败");
        }catch (Exception e){
            return R.fail("新增EbsParameters失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addEbsParametersList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addEbsParametersList", notes = "addEbsParametersList")
    public R<String> addEbsParametersList(@RequestBody List<EbsParameters> ebsParametersList) {
        try {
            boolean res = false;
            String resId = "";
            for (EbsParameters ebsParameters : ebsParametersList) {
                ebsParameters.setCategory(new String[]{});
                ebsParameters.setEbsStatus(0);
                res = ebsParametersService.addEbsParameters(ebsParameters);
                resId += ebsParameters.getId() + ",";
                Ebs byId = ebsService.getById(ebsParameters.getEbsId());
                byId.setEbsStatus(1);
                ebsService.updateEbs(byId);
            }
            if (res){
                R.success("新增EbsParameters成功！");
                return R.data(resId);
            }
            return R.data("新增失败");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("批量新增EbsParameters失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateEbsParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateEbsParameters", notes = "updateEbsParameters")
    public R<Boolean> updateEbsParameters(@RequestBody EbsParameters ebsParameters) {
        try {
            boolean res = ebsParametersService.updateEbsParameters(ebsParameters);
            Ebs byId = ebsService.getById(ebsParameters.getEbsId());
            byId.setEbsStatus(1);
            ebsService.updateEbs(byId);
            if (res){
                R.success("修改EbsParameters成功！");
            }
            return R.data(res);
        }catch (Exception e){
            return R.fail("修改EbsParameters失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteEbsParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteEbsParameters", notes = "deleteEbsParameters")
    public R<Boolean> deleteEbsParameters(@RequestBody EbsListDTO ebsListDTO) {
        try {
            String[] id = ebsListDTO.getIds();
            boolean res = false;
            for (String s : id) {
               res =  ebsParametersService.deleteEbsParameters(s);
                if (res){
                    R.success("删除EbsParameters成功！");
                }
            }
            return R.data(res);
        }catch (Exception e){
            return R.fail("删除EbsParameters失败！ "+e.getMessage());
        }
    }

//    ----------------EbsParameters绑定category-------------------
    @SneakyThrows
    @PostMapping("/linkEbsParametersCategory.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "linkEbsParametersCategory", notes = "linkEbsParametersCategory")
    public R<String> linkEbsParametersCategory(@RequestBody EbsParametesCategoryDTO ebsParametesCategoryDTO) {
        try {
            String id = ebsParametesCategoryDTO.getId();
            String[] category = ebsParametesCategoryDTO.getCategories();
            EbsParameters ep = ebsParametersService.getById(id);
            ep.setCategory(category);
            boolean res = ebsParametersService.updateEbsParameters(ep);
            if (res){
                R.success("绑定category成功！");
                return R.data("EBS:"+ep.getId()+"绑定category成功！categoryId为"+category);
            }
            return R.data("绑定失败");
        }catch (Exception e){
            return R.fail("绑定category失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/unlinkEbsParametersCategory.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "unlinkEbsParametersCategory", notes = "unlinkEbsParametersCategory")
    public R<Boolean> unlinkEbsParametersCategory(@RequestBody EbsParametesCategoryDTO ebsParametesCategoryDTO) {
        try {
            String id = ebsParametesCategoryDTO.getId();
            String[] category = ebsParametesCategoryDTO.getCategories();
            EbsParameters ep = ebsParametersService.getById(id);
            List<String> categories = new ArrayList<>(Arrays.asList(ep.getCategory()));
            categories.removeAll(Arrays.asList(category));
            ep.setCategory(categories.toArray(new String[0]));
            boolean res = ebsParametersService.updateEbsParameters(ep);
            if (res){
                return  R.success("解绑category成功！");
            }
            return R.data(res);
        }catch (Exception e){
            return R.fail("解绑category失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateEbsStatus.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateEbsStatus", notes = "updateEbsStatus")
    public R<Boolean> updateEbsStatus(@RequestParam String ebsId) {
        try {
            boolean b = ebsParametersService.updateEbsStatus(ebsId);
            boolean c = ebsService.updateEbsStatus(ebsId);
            if (b && c){
                R.success("修改EbsStatus成功！");
            }else {
                R.fail("修改EbsStatus失败！");
            }
            return R.data(b);
        }catch (Exception e){
            return R.fail("修改EbsStatus失败！ "+e.getMessage());
        }
    }

//    ----------------WBS---------------------

    @SneakyThrows
    @GetMapping("/getAllWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllWbs", notes = "getAllWbs")
    public R<List<Wbs>> getAllWbs() {
        try {
            List<Wbs> allWbs = wbsService.getAllWbs();
            R.success("获取WBS列表成功！");
            return R.data(allWbs);
        }catch (Exception e){
            return R.fail("获取列表失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getWbsById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getWbsById", notes = "getWbsById")
    public R<Wbs> getWbsById(@RequestParam String id) {
        try {
            Wbs wbs = wbsService.getById(id);
            R.success("获取WBS成功！");
            return R.data(wbs);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/filterWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "filterWbs", notes = "filterWbs")
    public R<PageWbs> filterWbs(@RequestParam Optional<String> type, @RequestParam Optional<String> source, @RequestParam Optional<Integer> wbsStatus, @RequestParam Optional<String> auth, @RequestParam Optional<String> wbsName, @RequestParam Optional<String> wbsCode, @RequestParam Integer pageSize, @RequestParam Integer currentPage) {
        try {
            PageWbs pageWbs = new PageWbs();
            if (pageSize == 0 || currentPage == 0 || pageSize == null || currentPage == null){
                return R.fail("分页数据获取失败)");
            }
            Page<Wbs> wbsList = wbsService.filterWbs( wbsCode.orElse(null),
                    wbsName.orElse(null),
                    type.orElse(null),
                    source.orElse(null),
                    auth.orElse(null),
                    wbsStatus.orElse(null),
                    pageSize,
                    currentPage);
            pageWbs.setPageWbs(wbsList);
            pageWbs.setTotal(wbsList.getTotal());
            return R.data(pageWbs);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addWbs", notes = "addWbs")
    public R<String> addWbs(@RequestBody Wbs wbs) {
        try {
            wbs.setSource("user");
            boolean res = wbsService.addWbs(wbs);
            if (res){
                return R.data(wbs.getId());
            }else{
                return R.fail("新增WBS失败！ ");
            }
        }catch (Exception e){
            return R.fail("新增WBS失败！ "+e.getMessage());
        }
    }

//    -----------预置资源-----------

    @SneakyThrows
    @PostMapping("/addStaticWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addStaticWbs", notes = "addStaticWbs")
    public R<String> addStaticWbs(@RequestBody Wbs wbs) {
        try {
            wbs.setSource("SMARTLINK");
            boolean res = wbsService.addWbs(wbs);
            if (res){
                return R.data(wbs.getId());
            }else{
                return R.fail("新增WBS失败！ ");
            }
        }catch (Exception e){
            return R.fail("新增WBS失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addWbsCopy.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addWbsCopy", notes = "addWbsCopy")
    public R<String> addWbsCopy(@RequestParam String wbsId) {
        try {
            List<WbsParameters> wbsParametersList = new ArrayList<>();
            // 存储每个层级的最新WbsParameters对象，key为层级名称
            Map<String, WbsParameters> latestNodeMap = new HashMap<>();
            Wbs wbs = wbsService.getById(wbsId);
            Wbs wbsNew = new Wbs(UUID.randomUUID().toString(), wbs.getWbsCode(),wbs.getWbsName()+"_copy",wbs.getWbsLv(),wbs.getType(),wbs.getSource(),wbs.getAuth(),wbs.getWbsStatus(),wbs.getModifyUser());
            wbsNew.setSource("user");
            wbsService.addWbs(wbsNew);
            List<WbsParameters> allWbsParametersByWbsId = wbsParametersService.getAllWbsParametersByWbsId(wbsId);
            for (WbsParameters wbsParameters : allWbsParametersByWbsId) {
                // 根据层级获取父级节点
                String pid = null;
                if (latestNodeMap.containsKey(fileService.getEbsParentLevel(wbsParameters.getWbsLv()))) {
                    pid = latestNodeMap.get(fileService.getEbsParentLevel(wbsParameters.getWbsLv())).getId();
                }
                WbsParameters wbsParametersNew = new WbsParameters(UUID.randomUUID().toString(), wbsNew.getId(),pid,wbsParameters.getWbsCode(),wbsParameters.getCategory(),wbsParameters.getWbsName(),wbsParameters.getWbsLv(),wbsParameters.getDetail(),wbsParameters.getModifyUser(),wbsParameters.getWbsStatus());
                wbsParametersList.add(wbsParametersNew);
                // 更新Map中当前层级的最新节点
                latestNodeMap.put(wbsParameters.getWbsLv(), wbsParametersNew);
            }
            // 批量插入数据
            if (!wbsParametersList.isEmpty()) {
                wbsParametersService.insertWbsParametersList(wbsParametersList); // 假设你有一个批量插入方法
            }
            return R.data(wbsNew.getId());
        }catch (Exception e){
            return R.fail("复制WBS异常！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateWbs", notes = "updateWbs")
    public R<Boolean> updateWbs(@RequestBody Wbs wbs) {
        try {
            boolean res = wbsService.updateWbs(wbs);
            if (res){
                R.success("修改WBS成功！");
            }
            return R.data(res);
        }catch (Exception e){
            return R.fail("修改WBS失败！ "+e.getMessage());
        }
    }


    @SneakyThrows
    @PostMapping("/deleteWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteWbs", notes = "deleteWbs")
    public R<Boolean> deleteWbs(@RequestParam String id) {
        try {
            boolean res = wbsService.deleteWbs(id);
            if (res){
                R.success("删除WBS成功！");
            }
            return R.data(res);
        }catch (Exception e){
            return R.fail("删除WBS失败！ "+e.getMessage());
        }
    }

//    ----------------wbsparameters----------------------

    @SneakyThrows
    @GetMapping("/getWbsByWbsCode.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getWbsByWbsCode", notes = "getWbsByWbsCode")
    public R<WbsParameters> getWbsByWbsCode(@RequestParam String wbsCode) {
        try {
            WbsParameters wbs = wbsParametersService.getWbsParametersByWbsCode(wbsCode);
            R.success("获取WBS成功！");
            return R.data(wbs);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getWbsParametersById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getWbsParametersById", notes = "getWbsParametersById")
    public R<WbsParameters> getWbsParametersById(@RequestParam String id) {
        try {
            WbsParameters wbsParameters = wbsParametersService.getById(id);
            R.success("获取WbsParameters成功！");
            return R.data(wbsParameters);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getAllWbsParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllWbsParameters", notes = "getAllWbsParameters")
    public R<List<WbsParameters>> getAllWbsParameters() {
        try {
            List<WbsParameters> allWbsParameters = wbsParametersService.getAllWbsParameters();
            R.success("获取WbsParameters列表成功！");
            return R.data(allWbsParameters);
        }catch (Exception e){
            return R.fail("获取列表失败！ "+e.getMessage());
        }
    }



    @SneakyThrows
    @GetMapping("/getAllWbsParametersByWbsId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllWbsParametersByWbsId", notes = "getAllWbsParametersByWbsId")
    public R<List<WbsParameters>> getAllWbsParametersByWbsId(@RequestParam String wbsId) {
        try {
            List<WbsParameters> allWbsParameters = wbsParametersService.getAllWbsParametersByWbsId(wbsId);
            R.success("获取WbsParameters列表成功！");
            return R.data(allWbsParameters);
        }catch (Exception e){
            return R.fail("获取列表失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getAllWbsParametersCopyByWbsId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllWbsParametersCopyByWbsId", notes = "getAllWbsParametersCopyByWbsId")
    public R<List<WbsParametersCopy>> getAllWbsParametersCopyByWbsId(@RequestParam String wbsId,@RequestParam String tempId) {
        try {
            List<WbsParametersCopy> allWbsParametersCopyByWbsId = wbsParametersCopyService.getAllWbsParametersCopyByWbsId(wbsId,tempId);
            R.success("获取WbsParameters列表成功！");
            return R.data(allWbsParametersCopyByWbsId);
        }catch (Exception e){
            return R.fail("获取列表失败！ "+e.getMessage());
        }
    }



    @SneakyThrows
    @PostMapping("/addWbsParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addWbsParameters", notes = "addWbsParameters")
    public R<String> addWbsParameters(@RequestBody WbsParameters wbsParameters) {
        try {
            wbsParameters.setCategory(new String[]{});
            wbsParameters.setWbsStatus(0);
            boolean res = wbsParametersService.addWbsParameters(wbsParameters);
            Wbs wbs = wbsService.getById(wbsParameters.getWbsId());
            wbs.setStatus(0);
            wbs.setSource("user");
            wbsService.updateWbs(wbs);
            if (res){
                R.success("新增WbsParameters成功！");
                return R.data(wbsParameters.getId());
            }
            return R.data("新增失败");
        }catch (Exception e){
            return R.fail("新增WbsParameters失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addWbsParametersList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addWbsParametersList", notes = "addWbsParametersList")
    public R<String> addWbsParametersList(@RequestBody List<WbsParameters> wbsParametersList) {
        try {
            boolean res = false;
            String resId = "";
            for (WbsParameters wbsParameters : wbsParametersList) {
                wbsParameters.setCategory(new String[]{});
                wbsParameters.setWbsStatus(0);
                res = wbsParametersService.addWbsParameters(wbsParameters);
                resId += wbsParameters.getId() + ",";
                Wbs wbs = wbsService.getById(wbsParameters.getWbsId());
                wbs.setStatus(0);
                wbs.setSource("user");
                wbsService.updateWbs(wbs);
            }
            if (res){
                R.success("新增WbsParameters成功！");
                return R.data(resId);
            }
            return R.data("新增失败");
        }catch (Exception e){
            return R.fail("新增WbsParameters失败！ "+e.getMessage());
        }
    }



    @SneakyThrows
    @PostMapping("/addWbsParametersByEbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addWbsParametersByEbs", notes = "addWbsParametersByEbs")
    public R<String> addWbsParametersByEbs(@RequestParam String ebsId) {
        try {
            List<WbsParameters> wbsParametersList = new ArrayList<>();
            // 存储每个层级的最新WbsParameters对象，key为层级名称
            Map<String, WbsParameters> latestNodeMap = new HashMap<>();
            Ebs ebs = ebsService.getById(ebsId);
            Wbs wbs = new Wbs(UUID.randomUUID().toString(),ebs.getEbsCode(),ebs.getEbsName(),ebs.getEbsLv(),ebs.getType(),ebs.getSource(),ebs.getAuth(),ebs.getEbsStatus(),ebs.getModifyUser());
            wbsService.addWbs(wbs);
            List<EbsParameters> allEbsParametersByEbsId = ebsParametersService.getAllEbsParametersByEbsId(ebsId);
            for (EbsParameters ebsParameters : allEbsParametersByEbsId) {
                // 根据层级获取父级节点
                String pid = null;
                if (latestNodeMap.containsKey(fileService.getEbsParentLevel(ebsParameters.getEbsLv()))) {
                    pid = latestNodeMap.get(fileService.getEbsParentLevel(ebsParameters.getEbsLv())).getId();
                }
                WbsParameters wbsParameters = new WbsParameters(UUID.randomUUID().toString(),wbs.getId(),pid,ebsParameters.getEbsCode(),ebsParameters.getCategory(),ebsParameters.getEbsName(),ebsParameters.getEbsLv(),ebsParameters.getDetail(),ebsParameters.getModifyUser(),ebsParameters.getEbsStatus());
                wbsParametersList.add(wbsParameters);
                // 更新Map中当前层级的最新节点
                latestNodeMap.put(ebsParameters.getEbsLv(), wbsParameters);
            }
            // 批量插入数据
            if (!wbsParametersList.isEmpty()) {
                wbsParametersService.insertWbsParametersList(wbsParametersList); // 假设你有一个批量插入方法
            }

            return R.data(wbs.getId());
        }catch (Exception e){
            return R.fail("新增WbsParameters失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/reorderWbsAsc.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "reorderWbsAsc", notes = "reorderWbsAsc")
    public R<List<WbsParameters>> reorderWbsAsc(@RequestParam String wbsId) {
        try {
            List<WbsParameters> allWbsParametersByWbsId = wbsParametersService.reorderWbsParameters(wbsId);
            return R.data(allWbsParametersByWbsId);
        }catch (Exception e){
            return R.fail("获取WbsParameters失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/reorderEbsAsc.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "reorderEbsDesc", notes = "reorderEbsDesc")
    public R<List<EbsParameters>> reorderEbsDesc(@RequestParam String ebsId) {
        try {
            List<EbsParameters> allEbsParametersByEbsId = ebsParametersService.reorderEbsParameters(ebsId);
            return R.data(allEbsParametersByEbsId);
        }catch (Exception e){
            return R.fail("获取EbsParameters失败！ "+e.getMessage());
        }
    }

//    --------------wbs上移下移------------------
//    @SneakyThrows
//    @PostMapping("/moveWbs.do")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "moveWbs", notes = "moveWbs")
//    public R<Boolean> moveWbs(@RequestParam String id) {
//        try {
//
//
//        } catch (Exception e) {
//
//        }
//    }


    @SneakyThrows
    @PostMapping("/updateWbsParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateWbsParameters", notes = "updateWbsParameters")
    public R<Boolean> updateWbsParameters(@RequestBody WbsParameters wbsParameters) {
        try {
            boolean res = wbsParametersService.updateWbsParameters(wbsParameters);
            Wbs wbs = wbsService.getById(wbsParameters.getWbsId());
            wbs.setStatus(0);
            wbsService.updateWbs(wbs);
            if (res){
                R.success("修改WbsParameters成功！");
            }
            return R.data(res);
        }catch (Exception e){
            return R.fail("修改WbsParameters失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteWbsParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteWbsParameters", notes = "deleteWbsParameters")
    public R<Boolean> deleteWbsParameters(@RequestBody EbsListDTO wbsListDTO) {
        try {
            String[] id = wbsListDTO.getIds();
            boolean res = false;
            for (String s : id) {
                res =  wbsParametersService.deleteWbsParameters(s);
                if (res){
                    R.success("删除WbsParameters成功！");
                }
            }
            return R.data(res);
        }catch (Exception e){
            return R.fail("删除WbsParameters失败！ "+e.getMessage());
        }
    }

//    --------------wbs绑定颜色-------------
    @SneakyThrows
    @GetMapping("/getByWbsCode.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getByWbsCode", notes = "getByWbsCode")
    public R<WbsParametersCopyColor> getByWbsCode(@RequestParam String wbsCode,@RequestParam String wbsId) {
        try {
            WbsParametersCopyColor wbsParametersCopyColor = wbsParametersCopyColorService.getByWbsCode(wbsCode,wbsId);
            return R.data(wbsParametersCopyColor);
    }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getAllWbsParametersCopyColorByWbsId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllWbsParametersCopyColorByWbsId", notes = "getAllWbsParametersCopyColorByWbsId")
    public R<List<WbsParametersCopyColor>> getAllWbsParametersCopyColorByWbsId(@RequestParam String wbsId){
        try {
            List<WbsParametersCopyColor> res = wbsParametersCopyColorService.getAllWbsParametersCopyColorByWbsId(wbsId);
            return R.data(res);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addWbsParametersCopyColor.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addWbsParametersCopyColor", notes = "addWbsParametersCopyColor")
    public R<String> addWbsParametersCopyColor(@RequestBody WbsParametersCopyColor wbsParametersCopyColor){
        try {
            boolean b = wbsParametersCopyColorService.addWbsParametersCopyColor(wbsParametersCopyColor);
            if (b){
                return R.data("绑定成功"+wbsParametersCopyColor.getId());
            }else {
                return R.data("绑定失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addWbsParametersCopyColorList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addWbsParametersCopyColorList", notes = "addWbsParametersCopyColorList")
    public R<String> addWbsParametersCopyColorList(@RequestBody List<WbsParametersCopyColor> wbsParametersCopyColorList){
        try {
            List<String> reslist = new ArrayList<>();
            for (WbsParametersCopyColor wbsParametersCopyColor : wbsParametersCopyColorList) {
                String wbsId = wbsParametersCopyColor.getWbsId();
                String wbsCode = wbsParametersCopyColor.getWbsCode();
                WbsParametersCopyColor byWbsCode = wbsParametersCopyColorService.getByWbsCode(wbsCode, wbsId);
                if (byWbsCode != null){
                    byWbsCode.setColor(wbsParametersCopyColor.getColor());
                    boolean b = wbsParametersCopyColorService.updateWbsParametersCopyColor(byWbsCode);
                    if (b){
                       reslist.add(wbsParametersCopyColor.getWbsCode());
                    }else {
                        return R.data("绑定失败"+wbsParametersCopyColor.getId());
                    }
                }else {
                    wbsParametersCopyColor.setId(UUID.randomUUID().toString());
                    boolean b = wbsParametersCopyColorService.addWbsParametersCopyColor(wbsParametersCopyColor);
                    if (b){
                        reslist.add(wbsParametersCopyColor.getWbsCode());
                    }else {
                        return R.data("绑定失败"+wbsParametersCopyColor.getId());
                    }
                }
            }
//            boolean b = wbsParametersCopyColorService.addWbsParametersCopyColorList(wbsParametersCopyColorList);
//            if (b){
//                return R.data("绑定成功,WBS组为:"+wbsParametersCopyColorList.get(0).getWbsId());
//            }else {
//                return R.data("绑定失败");
//            }
            if (wbsParametersCopyColorList.size() == reslist.size()){
                return R.data("绑定成功");
            }else {
                return R.data("绑定失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateWbsParametersCopyColor.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateWbsParametersCopyColor", notes = "updateWbsParametersCopyColor")
    public R<String> updateWbsParametersCopyColor(@RequestBody WbsParametersCopyColor wbsParametersCopyColor){
        try {
            boolean b = wbsParametersCopyColorService.updateWbsParametersCopyColor(wbsParametersCopyColor);
            if (b){
                return R.data("修改成功"+wbsParametersCopyColor.getId());
            }else {
                return R.data("修改失败");
            }
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteWbsParametersCopyColor.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteWbsParametersCopyColor", notes = "deleteWbsParametersCopyColor")
    public R<String> deleteWbsParametersCopyColor(@RequestParam String id){
        try {
            boolean b = wbsParametersCopyColorService.deleteWbsParametersCopyColor(id);
            if (b){
                return R.data("删除成功");
            }else {
                return R.data("删除失败");
            }
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    //    ----------------WbsParameters绑定category-------------------
    @SneakyThrows
    @PostMapping("/linkWbsParametersCategory.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "linkWbsParametersCategory", notes = "linkWbsParametersCategory")
    public R<String> linkWbsParametersCategory(@RequestBody EbsParametesCategoryDTO wbsParametesCategoryDTO) {
        try {
            String id = wbsParametesCategoryDTO.getId();
            String[] category = wbsParametesCategoryDTO.getCategories();
            WbsParameters ep = wbsParametersService.getById(id);
            ep.setCategory(category);
            boolean res = wbsParametersService.updateWbsParameters(ep);
            if (res){
                R.success("绑定category成功！");
                return R.data("WBS:"+ep.getId()+"绑定category成功！categoryId为"+category);
            }
            return R.data("绑定失败");
        }catch (Exception e){
            return R.fail("绑定category失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/unlinkWbsParametersCategory.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "unlinkWbsParametersCategory", notes = "unlinkWbsParametersCategory")
    public R<Boolean> unlinkWbsParametersCategory(@RequestBody EbsParametesCategoryDTO wbsParametesCategoryDTO) {
        try {
            String id = wbsParametesCategoryDTO.getId();
            String[] category = wbsParametesCategoryDTO.getCategories();
            WbsParameters ep = wbsParametersService.getById(id);
            List<String> categories = new ArrayList<>(Arrays.asList(ep.getCategory()));
            categories.removeAll(Arrays.asList(category));
            ep.setCategory(categories.toArray(new String[0]));
            boolean res = wbsParametersService.updateWbsParameters(ep);
            if (res){
               return R.success("解绑category成功！");
            }
            return R.data(res);
        }catch (Exception e){
            return R.fail("解绑category失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateWbsStatus.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateWbsStatus", notes = "updateWbsStatus")
    public R<Boolean> updateWbsStatus(@RequestParam String wbsId) {
        try {
            boolean b = wbsParametersService.updateWbsStatus(wbsId);
            boolean c = wbsService.updateWbsStatus(wbsId);
            if (b && c){
                R.success("修改WbsStatus成功！");
            }else {
                R.fail("修改WbsStatus失败！");
            }
            return R.data(b);
        }catch (Exception e){
            return R.fail("修改WbsStatus失败！ "+e.getMessage());
        }
    }


    //    ----------------Excel批量导入WBS-------------------------

    public String changeWbslv(String wbsLv){
        switch (wbsLv){
            case "单位工程" :
                return "0";
            case "分部工程" :
                return "1";
            case "子分部工程" :
                return "2";
            case "分项工程" :
                return "3";
            case "位置拆解" :
                return "4";
            default:
                return "4";
        }
    }

    @SneakyThrows
    @PostMapping("/uploadWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadWbs", notes = "uploadWbs")
    public  R<Boolean> uploadWbs(@RequestParam("file") MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            List<WbsExcelDTO> dataList = EasyExcelReadUtils.readWbsExcel(inputStream);

            // 分别存储 Wbs 和 WbsParameters 的列表
            // 存储每个层级的最新WbsParameters对象，key为层级名称
            Map<String, WbsParameters> latestNodeMap = new HashMap<>();
            Map<String, WbsLv> wbsLvMap = new HashMap<>();
            List<WbsParameters> wbsParametersList = new ArrayList<>();
            String wbsId = "";

            for (WbsExcelDTO dto : dataList) {
                if ("单位工程".equals(dto.getWbsLv())) {
                    Wbs wbs = new Wbs();
                    wbs.setWbsCode(dto.getWbsCode());
                    wbs.setWbsName(dto.getWbsName());
                    wbs.setAuth("all");
                    wbs.setWbsLv("0");
                    wbs.setWbsStatus(0);
                    wbs.setSource("user");
                    wbs.setModifyUser("admin"); // 这里可以根据实际情况设置修改用户
                    wbsService.addWbs(wbs);
                    wbsId = wbs.getId();
                    WbsParameters wbsParameters = new WbsParameters();
                    wbsParameters.setWbsId(wbsId);
                    wbsParameters.setPid("root");
                    wbsParameters.setWbsCode(dto.getWbsCode());
                    wbsParameters.setWbsName(dto.getWbsName());
                    wbsParameters.setWbsLv("0");
                    wbsParameters.setWbsStatus(0);
                    wbsParameters.setCategory(new String[]{});
                    wbsParameters.setDetail(dto.getDetail());
                    wbsParameters.setModifyUser("admin");
                    wbsParametersList.add(wbsParameters);
                    //保存wbsLv
                    WbsLv wbsLv = new WbsLv();
                    wbsLv.setWbsId(wbsId);
                    wbsLv.setCode(changeWbslv(dto.getWbsLv()));
                    wbsLv.setNameCn(dto.getWbsLv());
                    wbsLvMap.put(dto.getWbsLv(),wbsLv);
                    // 更新Map中当前层级的最新节点
                    latestNodeMap.put(dto.getWbsLv(), wbsParameters);
                } else {
                    // 根据层级获取父级节点
                    String pid = null;
                    if (latestNodeMap.containsKey(fileService.getWbsParentLevel(dto.getWbsLv()))) {
                        pid = latestNodeMap.get(fileService.getWbsParentLevel(dto.getWbsLv())).getId();
                    }
                    WbsParameters wbsParameters = new WbsParameters();
                    wbsParameters.setWbsId(wbsId);
                    wbsParameters.setPid(pid);
                    wbsParameters.setWbsCode(dto.getWbsCode());
                    wbsParameters.setWbsName(dto.getWbsName());
                    wbsParameters.setWbsLv(changeWbslv(dto.getWbsLv()));
                    wbsParameters.setWbsStatus(0);
                    wbsParameters.setCategory(new String[]{});
                    wbsParameters.setDetail(dto.getDetail());
                    wbsParameters.setModifyUser("admin");
                    wbsParametersList.add(wbsParameters);
                    //保存wbsLv
                    WbsLv wbsLv = new WbsLv();
                    wbsLv.setWbsId(wbsId);
                    wbsLv.setCode(changeWbslv(dto.getWbsLv()));
                    wbsLv.setNameCn(dto.getWbsLv());
                    wbsLvMap.put(dto.getWbsLv(),wbsLv);
                    // 更新Map中当前层级的最新节点
                    latestNodeMap.put(dto.getWbsLv(), wbsParameters);
                }
            }

            wbsLvMap.forEach((k,v) -> {
                wbsLvService.addWbsLv(v);
            });

            // 批量插入数据
            if (!wbsParametersList.isEmpty()) {
                wbsParametersService.insertWbsParametersList(wbsParametersList); // 假设你有一个批量插入方法
            }

            return R.success("成功导入数据");
        } catch (IOException e) {
            return R.fail("导入失败: " + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/uploadStaticWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadStaticWbs", notes = "uploadStaticWbs")
    public  R<Boolean> uploadStaticWbs(@RequestParam("file") MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            List<WbsExcelDTO> dataList = EasyExcelReadUtils.readWbsExcel(inputStream);

            // 分别存储 Wbs 和 WbsParameters 的列表
            // 存储每个层级的最新WbsParameters对象，key为层级名称
            Map<String, WbsParameters> latestNodeMap = new HashMap<>();
            Map<String, WbsLv> wbsLvMap = new HashMap<>();
            List<WbsParameters> wbsParametersList = new ArrayList<>();
            String wbsId = "";

            for (WbsExcelDTO dto : dataList) {
                if ("单位工程".equals(dto.getWbsLv())) {
                    Wbs wbs = new Wbs();
                    wbs.setWbsCode(dto.getWbsCode());
                    wbs.setWbsName(dto.getWbsName());
                    wbs.setAuth("all");
                    wbs.setWbsLv("0");
                    wbs.setWbsStatus(0);
                    wbs.setSource("SMARTLINK");
                    wbs.setModifyUser("admin"); // 这里可以根据实际情况设置修改用户
                    wbsService.addWbs(wbs);
                    wbsId = wbs.getId();
                    WbsParameters wbsParameters = new WbsParameters();
                    wbsParameters.setWbsId(wbsId);
                    wbsParameters.setPid("root");
                    wbsParameters.setWbsCode(dto.getWbsCode());
                    wbsParameters.setWbsName(dto.getWbsName());
                    wbsParameters.setWbsLv("0");
                    wbsParameters.setWbsStatus(0);
                    wbsParameters.setCategory(new String[]{});
                    wbsParameters.setDetail(dto.getDetail());
                    wbsParameters.setModifyUser("admin");
                    wbsParametersList.add(wbsParameters);
                    //保存wbsLv
                    WbsLv wbsLv = new WbsLv();
                    wbsLv.setWbsId(wbsId);
                    wbsLv.setCode(changeWbslv(dto.getWbsLv()));
                    wbsLv.setNameCn(dto.getWbsLv());
                    wbsLvMap.put(dto.getWbsLv(),wbsLv);
                    // 更新Map中当前层级的最新节点
                    latestNodeMap.put(dto.getWbsLv(), wbsParameters);
                } else {
                    // 根据层级获取父级节点
                    String pid = null;
                    if (latestNodeMap.containsKey(fileService.getWbsParentLevel(dto.getWbsLv()))) {
                        pid = latestNodeMap.get(fileService.getWbsParentLevel(dto.getWbsLv())).getId();
                    }
                    WbsParameters wbsParameters = new WbsParameters();
                    wbsParameters.setWbsId(wbsId);
                    wbsParameters.setPid(pid);
                    wbsParameters.setWbsCode(dto.getWbsCode());
                    wbsParameters.setWbsName(dto.getWbsName());
                    wbsParameters.setWbsLv(changeWbslv(dto.getWbsLv()));
                    wbsParameters.setWbsStatus(0);
                    wbsParameters.setCategory(new String[]{});
                    wbsParameters.setDetail(dto.getDetail());
                    wbsParameters.setModifyUser("admin");
                    wbsParametersList.add(wbsParameters);
                    //保存wbsLv
                    WbsLv wbsLv = new WbsLv();
                    wbsLv.setWbsId(wbsId);
                    wbsLv.setCode(changeWbslv(dto.getWbsLv()));
                    wbsLv.setNameCn(dto.getWbsLv());
                    wbsLvMap.put(dto.getWbsLv(),wbsLv);
                    // 更新Map中当前层级的最新节点
                    latestNodeMap.put(dto.getWbsLv(), wbsParameters);
                }
            }

            wbsLvMap.forEach((k,v) -> {
                wbsLvService.addWbsLv(v);
            });

            // 批量插入数据
            if (!wbsParametersList.isEmpty()) {
                wbsParametersService.insertWbsParametersList(wbsParametersList); // 假设你有一个批量插入方法
            }

            return R.success("成功导入数据");
        } catch (IOException e) {
            return R.fail("导入失败: " + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/uploadWbs2.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadWbs2", notes = "uploadWbs2")
    public  R<Boolean> uploadWbs2(@RequestParam("file") MultipartFile file,@RequestParam String wbsId) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            List<WbsExcelDTO> dataList = EasyExcelReadUtils.readWbsExcel(inputStream);

            // 分别存储 Wbs 和 WbsParameters 的列表
            // 存储每个层级的最新WbsParameters对象，key为层级名称
            Map<String, WbsParameters> latestNodeMap = new HashMap<>();
            Map<String, WbsLv> wbsLvMap = new HashMap<>();
            List<WbsParameters> wbsParametersList = new ArrayList<>();
//            String wbsId = "";

            for (WbsExcelDTO dto : dataList) {
                if ("单位工程".equals(dto.getWbsLv())) {
                    WbsParameters wbsParameters = new WbsParameters();
                    wbsParameters.setWbsId(wbsId);
                    wbsParameters.setPid("root");
                    wbsParameters.setWbsCode(dto.getWbsCode());
                    wbsParameters.setWbsName(dto.getWbsName());
                    wbsParameters.setWbsLv("0");
                    wbsParameters.setWbsStatus(0);
                    wbsParameters.setCategory(new String[]{});
                    wbsParameters.setDetail(dto.getDetail());
                    wbsParameters.setModifyUser("admin");
                    wbsParametersList.add(wbsParameters);
                    //保存wbsLv
                    WbsLv wbsLv = new WbsLv();
                    wbsLv.setWbsId(wbsId);
                    wbsLv.setCode(changeWbslv(dto.getWbsLv()));
                    wbsLv.setNameCn(dto.getWbsLv());
                    wbsLvMap.put(dto.getWbsLv(),wbsLv);
                    // 更新Map中当前层级的最新节点
                    latestNodeMap.put(dto.getWbsLv(), wbsParameters);
                } else {
                    // 根据层级获取父级节点
                    String pid = null;
                    if (latestNodeMap.containsKey(fileService.getWbsParentLevel(dto.getWbsLv()))) {
                        pid = latestNodeMap.get(fileService.getWbsParentLevel(dto.getWbsLv())).getId();
                    }
                    WbsParameters wbsParameters = new WbsParameters();
                    wbsParameters.setWbsId(wbsId);
                    wbsParameters.setPid(pid);
                    wbsParameters.setWbsCode(dto.getWbsCode());
                    wbsParameters.setWbsName(dto.getWbsName());
                    wbsParameters.setWbsLv(changeWbslv(dto.getWbsLv()));
                    wbsParameters.setWbsStatus(0);
                    wbsParameters.setCategory(new String[]{});
                    wbsParameters.setDetail(dto.getDetail());
                    wbsParameters.setModifyUser("admin");
                    wbsParametersList.add(wbsParameters);
                    //保存wbsLv
                    WbsLv wbsLv = new WbsLv();
                    wbsLv.setWbsId(wbsId);
                    wbsLv.setCode(changeWbslv(dto.getWbsLv()));
                    wbsLv.setNameCn(dto.getWbsLv());
                    wbsLvMap.put(dto.getWbsLv(),wbsLv);
                    // 更新Map中当前层级的最新节点
                    latestNodeMap.put(dto.getWbsLv(), wbsParameters);
                }
            }

            wbsLvMap.forEach((k,v) -> {
                wbsLvService.addWbsLv(v);
            });

            // 批量插入数据
            if (!wbsParametersList.isEmpty()) {
                wbsParametersService.insertWbsParametersList(wbsParametersList); // 假设你有一个批量插入方法
            }

            return R.success("成功导入数据");
        } catch (IOException e) {
            return R.fail("导入失败: " + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/uploadEbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadEbs", notes = "uploadEbs")
    public  R<Boolean> uploadEbs(@RequestParam("file") MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            List<EbsExcelDTO> dataList = EasyExcelReadUtils.readEbsExcel(inputStream);

            // 分别存储 Wbs 和 WbsParameters 的列表
            // 存储每个层级的最新WbsParameters对象，key为层级名称
            Map<String, EbsParameters> latestNodeMap = new HashMap<>();
            List<EbsParameters> ebsParametersList = new ArrayList<>();
            String ebsId = "";

            for (EbsExcelDTO dto : dataList) {
                if ("0".equals(dto.getEbsLv())) {
                   Ebs ebs = new Ebs();
                   ebs.setEbsCode(dto.getEbsCode());
                   ebs.setEbsName(dto.getEbsName());
                   ebs.setEbsLv(dto.getEbsLv());
                   ebs.setAuth("all");
                   ebs.setEbsStatus(0);
                   ebs.setSource("user");
                   ebs.setModifyUser("admin"); // 这里可以根据实际情况设置修改用户
                   ebsService.addEbs(ebs);
                   ebsId = ebs.getId();
                   EbsParameters ebsParameters = new EbsParameters();
                   ebsParameters.setEbsId(ebsId);
                   ebsParameters.setPid("root");
                   ebsParameters.setEbsCode(dto.getEbsCode());
                   ebsParameters.setEbsName(dto.getEbsName());
                   ebsParameters.setEbsLv(dto.getEbsLv());
                   ebsParameters.setEbsStatus(0);
                   ebsParameters.setCategory(new String[]{});
                   ebsParameters.setDetail(dto.getDetail());
                   ebsParameters.setModifyUser("admin");
                   ebsParametersList.add(ebsParameters);
                   // 更新Map中当前层级的最新节点
                   latestNodeMap.put(dto.getEbsLv(), ebsParameters);
                } else {
                    // 根据层级获取父级节点
                    String pid = null;
                    if (latestNodeMap.containsKey(fileService.getEbsParentLevel(dto.getEbsLv()))) {
                        pid = latestNodeMap.get(fileService.getEbsParentLevel(dto.getEbsLv())).getId();
                    }
                    EbsParameters ebsParameters = new EbsParameters();
                    ebsParameters.setEbsId(ebsId);
                    ebsParameters.setPid(pid);
                    ebsParameters.setEbsCode(dto.getEbsCode());
                    ebsParameters.setEbsName(dto.getEbsName());
                    ebsParameters.setEbsLv(dto.getEbsLv());
                    ebsParameters.setEbsStatus(0);
                    ebsParameters.setCategory(new String[]{});
                    ebsParameters.setDetail(dto.getDetail());
                    ebsParameters.setModifyUser("admin");
                    ebsParametersList.add(ebsParameters);
                    // 更新Map中当前层级的最新节点
                    latestNodeMap.put(dto.getEbsLv(), ebsParameters);
                }
            }

            // 批量插入数据
            if (!ebsParametersList.isEmpty()) {
                ebsParametersService.insertEbsParametersList(ebsParametersList); // 假设你有一个批量插入方法
            }

            return R.success("成功导入数据");
        } catch (IOException e) {
            return R.fail("导入失败: " + e.getMessage());
        }
    }



    @SneakyThrows
    @PostMapping("/uploadStaticEbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadStaticEbs", notes = "uploadStaticEbs")
    public  R<Boolean> uploadStaticEbs(@RequestParam("file") MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            List<EbsExcelDTO> dataList = EasyExcelReadUtils.readEbsExcel(inputStream);

            // 分别存储 Wbs 和 WbsParameters 的列表
            // 存储每个层级的最新WbsParameters对象，key为层级名称
            Map<String, EbsParameters> latestNodeMap = new HashMap<>();
            List<EbsParameters> ebsParametersList = new ArrayList<>();
            String ebsId = "";

            for (EbsExcelDTO dto : dataList) {
                if ("0".equals(dto.getEbsLv())) {
                    Ebs ebs = new Ebs();
                    ebs.setEbsCode(dto.getEbsCode());
                    ebs.setEbsName(dto.getEbsName());
                    ebs.setEbsLv(dto.getEbsLv());
                    ebs.setAuth("all");
                    ebs.setEbsStatus(0);
                    ebs.setSource("SMARTLINK");
                    ebs.setModifyUser("admin"); // 这里可以根据实际情况设置修改用户
                    ebsService.addEbs(ebs);
                    ebsId = ebs.getId();
                    EbsParameters ebsParameters = new EbsParameters();
                    ebsParameters.setEbsId(ebsId);
                    ebsParameters.setPid("root");
                    ebsParameters.setEbsCode(dto.getEbsCode());
                    ebsParameters.setEbsName(dto.getEbsName());
                    ebsParameters.setEbsLv(dto.getEbsLv());
                    ebsParameters.setEbsStatus(0);
                    ebsParameters.setCategory(new String[]{});
                    ebsParameters.setDetail(dto.getDetail());
                    ebsParameters.setModifyUser("admin");
                    ebsParametersList.add(ebsParameters);
                    // 更新Map中当前层级的最新节点
                    latestNodeMap.put(dto.getEbsLv(), ebsParameters);
                } else {
                    // 根据层级获取父级节点
                    String pid = null;
                    if (latestNodeMap.containsKey(fileService.getEbsParentLevel(dto.getEbsLv()))) {
                        pid = latestNodeMap.get(fileService.getEbsParentLevel(dto.getEbsLv())).getId();
                    }
                    EbsParameters ebsParameters = new EbsParameters();
                    ebsParameters.setEbsId(ebsId);
                    ebsParameters.setPid(pid);
                    ebsParameters.setEbsCode(dto.getEbsCode());
                    ebsParameters.setEbsName(dto.getEbsName());
                    ebsParameters.setEbsLv(dto.getEbsLv());
                    ebsParameters.setEbsStatus(0);
                    ebsParameters.setCategory(new String[]{});
                    ebsParameters.setDetail(dto.getDetail());
                    ebsParameters.setModifyUser("admin");
                    ebsParametersList.add(ebsParameters);
                    // 更新Map中当前层级的最新节点
                    latestNodeMap.put(dto.getEbsLv(), ebsParameters);
                }
            }

            // 批量插入数据
            if (!ebsParametersList.isEmpty()) {
                ebsParametersService.insertEbsParametersList(ebsParametersList); // 假设你有一个批量插入方法
            }

            return R.success("成功导入数据");
        } catch (IOException e) {
            return R.fail("导入失败: " + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/uploadEbs2.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadEbs2", notes = "uploadEbs2")
    public  R<Boolean> uploadEbs2(@RequestParam("file") MultipartFile file,@RequestParam String ebsId) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            List<EbsExcelDTO> dataList = EasyExcelReadUtils.readEbsExcel(inputStream);

            // 分别存储 Wbs 和 WbsParameters 的列表
            // 存储每个层级的最新WbsParameters对象，key为层级名称
            Map<String, EbsParameters> latestNodeMap = new HashMap<>();
            List<EbsParameters> ebsParametersList = new ArrayList<>();
//            String ebsId = "";

            for (EbsExcelDTO dto : dataList) {
                if ("0".equals(dto.getEbsLv())) {
                    EbsParameters ebsParameters = new EbsParameters();
                    ebsParameters.setEbsId(ebsId);
                    ebsParameters.setPid("root");
                    ebsParameters.setEbsCode(dto.getEbsCode());
                    ebsParameters.setEbsName(dto.getEbsName());
                    ebsParameters.setEbsLv(dto.getEbsLv());
                    ebsParameters.setEbsStatus(0);
                    ebsParameters.setCategory(new String[]{});
                    ebsParameters.setDetail(dto.getDetail());
                    ebsParameters.setModifyUser("admin");
                    ebsParametersList.add(ebsParameters);
                    // 更新Map中当前层级的最新节点
                    latestNodeMap.put(dto.getEbsLv(), ebsParameters);
                } else {
                    // 根据层级获取父级节点
                    String pid = null;
                    if (latestNodeMap.containsKey(fileService.getEbsParentLevel(dto.getEbsLv()))) {
                        pid = latestNodeMap.get(fileService.getEbsParentLevel(dto.getEbsLv())).getId();
                    }
                    EbsParameters ebsParameters = new EbsParameters();
                    ebsParameters.setEbsId(ebsId);
                    ebsParameters.setPid(pid);
                    ebsParameters.setEbsCode(dto.getEbsCode());
                    ebsParameters.setEbsName(dto.getEbsName());
                    ebsParameters.setEbsLv(dto.getEbsLv());
                    ebsParameters.setEbsStatus(0);
                    ebsParameters.setCategory(new String[]{});
                    ebsParameters.setDetail(dto.getDetail());
                    ebsParameters.setModifyUser("admin");
                    ebsParametersList.add(ebsParameters);
                    // 更新Map中当前层级的最新节点
                    latestNodeMap.put(dto.getEbsLv(), ebsParameters);
                }
            }

            // 批量插入数据
            if (!ebsParametersList.isEmpty()) {
                ebsParametersService.insertEbsParametersList(ebsParametersList); // 假设你有一个批量插入方法
            }

            return R.success("成功导入数据");
        } catch (IOException e) {
            return R.fail("导入失败: " + e.getMessage());
        }
    }


   // -------------WBSLV-----------------
   @SneakyThrows
   @GetMapping("/getWbsLvByWbsId.do")
   @ApiOperationSupport(order = 2)
   @ApiOperation(value = "getWbsLvByWbsId", notes = "getWbsLvByWbsId")
   public R<List<WbsLv>> getWbsLvByWbsId(@RequestParam String wbsId) throws IOException {
       try {
           List<WbsLv> wbsLvByWbsId = wbsLvService.getWbsLvByWbsId(wbsId);
           return R.data(wbsLvByWbsId);
       }catch (Exception e){
           return R.fail("获取失败！ "+e.getMessage());
       }
   }

    @SneakyThrows
    @GetMapping("/getWbsLvById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getWbsLvById", notes = "v")
    public R<WbsLv> getWbsLvById(@RequestParam String id) throws IOException {
        try {
            WbsLv wbsLvById = wbsLvService.getById(id);
            return R.data(wbsLvById);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/filterWbsLv.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "filterWbsLv", notes = "filterWbsLv")
    public R<List<WbsLv>> filterWbsLv(@RequestParam Optional<String> code, @RequestParam Optional<String> nameCn, @RequestParam Optional<String> wbsId) throws IOException {
       try {
           List<WbsLv> filterWbsLv = wbsLvService.filterWbsLv(code.orElse(""),nameCn.orElse(""),wbsId.orElse(""));
           return R.data(filterWbsLv);
       }catch (Exception e){
           return R.fail("获取失败！ "+e.getMessage());
       }
    }

    @SneakyThrows
    @PostMapping("/addWbsLv.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addWbsLv", notes = "addWbsLv")
    public R<String> addWbsLv(@RequestBody WbsLv wbsLv) throws IOException {
        try {
            boolean b = wbsLvService.addWbsLv(wbsLv);
            if (b){
                return R.data(wbsLv.getWbsId());
            }else {
                return R.fail("添加失败！ ");
            }
        }catch (Exception e){
            return R.fail("添加失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateWbsLv.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateWbsLv", notes = "updateWbsLv")
    public R<Boolean> updateWbsLv(@RequestBody WbsLv wbsLv) throws IOException {
        try {
            boolean b = wbsLvService.updateWbsLv(wbsLv);
            if (b){
                return R.data(b);
            }else {
                return R.fail("更新失败！ ");
            }
        }catch (Exception e){
            return R.fail("更新失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteWbsLv.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteWbsLv", notes = "deleteWbsLv")
    public R<Boolean> deleteWbsLv(@RequestParam String id) throws IOException {
        try {
            boolean b = wbsLvService.deleteWbsLv(id);
            if (b){
                return R.data(b);
            }else {
                return R.fail("删除失败！ ");
            }
        }catch (Exception e){
            return R.fail("删除失败！ "+e.getMessage());
        }
    }
}
