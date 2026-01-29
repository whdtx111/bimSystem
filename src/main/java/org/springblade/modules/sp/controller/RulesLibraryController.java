package org.springblade.modules.sp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.*;
import org.springblade.modules.sp.excel.EasyExcelReadUtils;
import org.springblade.modules.sp.service.*;
import org.springblade.modules.sp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/rulesLibrary")
@Api(value = "规则库控制层", tags = "rulesLibrary")
public class RulesLibraryController extends BladeController {
    
    @Autowired
    private RulesLibraryService rulesLibraryService;
    @Autowired
    private RulesDetailService rulesDetailService;
    @Autowired
    private RulesParametersService rulesParametersService;
    @Autowired
    private IUsersService usersService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ExpAssemblyService expAssemblyService;
    @Autowired
    private ExpInstancesService expInstancesService;
    @Autowired
    private RulesParametersLODService rulesParametersLODService;

//    -------规则库外层-----------

    @SneakyThrows
    @GetMapping("/getRulesLibraryById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRulesLibraryById", notes = "getRulesLibraryById")
    public R<RulesLibrary> getRulesLibraryById(@RequestParam String id) {
        try {
            return R.data(rulesLibraryService.getRulesLibraryById(id));
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getAll.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAll", notes = "getAll")
    public R<List<RulesLibrary>> getAll() {
        try {
            return R.data(rulesLibraryService.getAll());
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/filterRulesLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "filterRulesLibrary", notes = "filterRulesLibrary")
    public R<PageRulesLibrary> filterRulesLibrary(@RequestParam Optional<String> name, @RequestParam Optional<String> auth, @RequestParam Optional<String> source, @RequestParam Optional<Integer> status, @RequestParam Integer pageSize, @RequestParam Integer currentPage) {
        try {
            PageRulesLibrary pageRulesLibrary = new PageRulesLibrary();
            if (pageSize == 0 || currentPage == 0 || pageSize == null || currentPage == null){
                return R.fail("分页数据获取失败)");
            }

            Page<RulesLibrary> libraries = rulesLibraryService.filterRulesLibrary(name.orElse(null), auth.orElse(null),
                    source.orElse(null), status.orElse(null), pageSize, currentPage);
            pageRulesLibrary.setPageRulesLibrary(libraries);
            pageRulesLibrary.setTotal(libraries.getTotal());
            return R.data(pageRulesLibrary);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getRulesLibraryWithDetails.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRulesLibraryWithDetails", notes = "getRulesLibraryWithDetails")
    public R<RulesLibraryDetailVO> getRulesLibraryWithDetails(@RequestParam String id) {
        try {
            return R.data(rulesLibraryService.getRulesLibraryWithDetails(id));
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getAllRulesLibraryWithDetails.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllRulesLibraryWithDetails", notes = "getAllRulesLibraryWithDetails")
    public R<List<RulesLibraryDetailVO>> getAllRulesLibraryWithDetails(@RequestParam String streamId) {
        try {
            return R.data(rulesLibraryService.getAllRulesLibraryWithDetails(streamId));
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addRulesLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addRulesLibrary", notes = "addRulesLibrary")
    public R<String> addRulesLibrary(@RequestHeader("Authorization") String token,@RequestBody RulesLibrary rulesLibrary) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }

            rulesLibrary.setCreatedUser(user.getName());
            rulesLibrary.setStatus(0);
            boolean b = rulesLibraryService.addRulesLibrary(rulesLibrary);
            if (!b){
                return R.fail("添加规则失败");
            }
            return R.data(rulesLibrary.getId());
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateRulesLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateRulesLibrary", notes = "updateRulesLibrary")
    public R<Boolean> updateRulesLibrary(@RequestBody RulesLibrary rulesLibrary) {
        try {
            return R.data(rulesLibraryService.updateRulesLibrary(rulesLibrary));
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteRulesLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteRulesLibrary", notes = "deleteRulesLibrary")
    public R<Boolean> deleteRulesLibrary(@RequestParam String id) {
        try {
            boolean c = true;
            boolean b = rulesLibraryService.deleteRulesLibraryById(id);
            List<RulesDetail> rulesDetailByPid = rulesDetailService.getRulesDetailByPid(id);
            if (rulesDetailByPid.size() > 0){
                for (RulesDetail rulesDetail : rulesDetailByPid) {
                    rulesParametersService.deleteRulesParametersByDetailId(rulesDetail.getId());
                }
               c = rulesDetailService.deleteRulesDetailByPid(id);
            }
            if (b && c){
                return R.data(true);
            }else {
                return R.fail("删除规则失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/uploadLOI.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "解析LOI数据Excel文件", notes = "解析LOI数据Excel文件")
    public R<List<Map<String, Object>>> parseLoiDataExcel(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return R.fail("文件不能为空");
            }

            // 解析Excel文件
            List<Map<String, Object>> result = EasyExcelReadUtils.readLoiDataExcel(file.getInputStream());

            return R.data(result);

        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("文件解析失败：" + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/uploadLoiTemplate.do")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "上传LOI模板文件", notes = "上传LOI模板文件到MinIO")
    public R<String> uploadLoiTemplate(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return R.fail("文件不能为空");
            }

            // 验证文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".xlsx")) {
                return R.fail("只支持上传.xlsx格式的文件");
            }

            String fileUrl = fileService.uploadLoiTemplate(file);
            return R.data(fileUrl, "LOI模板文件上传成功");

        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("LOI模板文件上传失败: " + e.getMessage());
        }
    }


    @SneakyThrows
    @GetMapping("/downloadLoiTemplate.do")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "下载LOI模板文件", notes = "下载LOI模板文件")
    public void downloadLoiTemplate(@RequestParam(value = "fileName", defaultValue = "LOI模版.xlsx") String fileName,
                                    HttpServletResponse response) {
        try {
            fileService.downloadLoiTemplate(fileName, response);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果出错，返回错误信息
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("{\"success\":false,\"message\":\"文件下载失败: " + e.getMessage() + "\"}");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @SneakyThrows
    @PostMapping("/compareBomRules.do")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "BOM表对比规则验证", notes = "根据streamId、commitId和Excel文件进行BOM表对比规则验证")
    public R<List<BomComparisonResultVO>> compareBomRules(
            @RequestParam String streamId,
            @RequestParam String commitId,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return R.fail("文件不能为空");
            }

            // 1. 查询assembly数据
            JSONArray assembly = expAssemblyService.getAssemblyByStreamAndCommit(streamId, commitId);
            if (assembly == null || assembly.isEmpty()) {
                return R.fail("未找到对应的assembly数据");
            }

            // 2. 解析Excel文件，获取elementId和规则
            List<Map<String, Object>> excelData = EasyExcelReadUtils.readBomComparisonRules(file.getInputStream());
            if (excelData.isEmpty()) {
                return R.fail("Excel文件中未找到有效的elementId数据");
            }

            // 3. 对每个elementId进行验证
            List<BomComparisonResultVO> results = new ArrayList<>();

            for (Map<String, Object> data : excelData) {
                String elementId = (String) data.get("elementId");
                String rule = (String) data.get("rule");
                Integer columnIndex = (Integer) data.get("columnIndex");
                Integer rowIndex = (Integer) data.get("rowIndex");

                if (elementId == null || rule == null) {
                    continue;
                }

                BomComparisonResultVO result = new BomComparisonResultVO();
                result.setElementId(elementId);
                result.setRule(rule);
                result.setColumnIndex(columnIndex);
                result.setRowIndex(rowIndex);

                // 4. 查询对应的exp_instances数据
                ExpInstances instance = expInstancesService.getInstanceByElementIdAndStreamAndCommit(elementId, streamId, commitId);

                if (instance == null) {
                    result.setMatched(false);
                    result.setMessage("未找到对应的实例数据");
                    results.add(result);
                    continue;
                }

                result.setCategory(instance.getCategory());
                result.setType(instance.getType());

                // 5. 解析规则并进行匹配
                boolean matched = matchRule(instance.getCategory(), instance.getType(), rule, result);
                result.setMatched(matched);

                if (matched) {
                    result.setMessage("匹配成功");
                } else {
                    result.setMessage("匹配失败：category或type不符合规则");
                }

                results.add(result);
            }

            return R.data(results);

        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("BOM表对比规则验证失败：" + e.getMessage());
        }
    }

    /**
     * 匹配规则
     * @param category 实际category
     * @param type 实际type
     * @param rule 规则字符串
     * @param result 结果对象
     * @return 是否匹配
     */
    private boolean matchRule(String category, String type, String rule, BomComparisonResultVO result) {
        if (rule == null || rule.trim().isEmpty()) {
            return false;
        }

        // 解析规则：第一个/前的部分为category，最后一个/后的部分为type
        String[] parts = rule.split("/");
        if (parts.length < 2) {
            return false;
        }

        String expectedCategory = parts[0].trim();
        String expectedType = parts[parts.length - 1].trim();

        result.setExpectedCategory(expectedCategory);
        result.setExpectedType(expectedType);

        // 进行匹配（忽略大小写）
        boolean categoryMatch = expectedCategory.equalsIgnoreCase(category);
        boolean typeMatch = expectedType.equalsIgnoreCase(type);

        return categoryMatch && typeMatch;
    }

    // 在比较逻辑中查找elementId
    private boolean findElementInAssembly(JSONArray assembly, String targetElementId) {
        for (int i = 0; i < assembly.size(); i++) {
            JSONObject element = assembly.getJSONObject(i);
            if (targetElementId.equals(element.getString("elementId"))) {
                return true;
            }
            // 如果有children，递归查找
            JSONArray children = element.getJSONArray("children");
            if (children != null && !children.isEmpty()) {
                if (findElementInAssembly(children, targetElementId)) {
                    return true;
                }
            }
        }
        return false;
    }

//    --------------规则库详情------------

    @SneakyThrows
    @GetMapping("/getRulesDetailById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRulesDetailById", notes = "getRulesDetailById")
    public R<RulesDetail> getRulesDetailById(@RequestParam String id) {
        try {
            return R.data(rulesDetailService.getRulesDetailById(id));
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/saveRulesDetails.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "saveRulesDetails", notes = "saveRulesDetails")
    public R<String> saveRulesDetails(@RequestBody RulesLibraryVO vo) {
        try {
            // 先删除原有数据
            List<RulesDetail> existingDetails = rulesDetailService.getRulesDetailByPid(vo.getId());
            if (existingDetails != null && !existingDetails.isEmpty()) {
                for (RulesDetail detail : existingDetails) {
                    rulesParametersService.deleteRulesParametersByDetailId(detail.getId());
                }
                rulesDetailService.deleteRulesDetailByPid(vo.getId());
            }

            List<RulesParametersLOD> rulesParametersLODByPid = rulesParametersLODService.getRulesParametersLODByPid(vo.getId());
            if (rulesParametersLODByPid != null && !rulesParametersLODByPid.isEmpty()) {
                rulesParametersLODService.deleteRulesParametersByPid(vo.getId());
            }

            // 保存新数据
            boolean b = rulesLibraryService.saveRulesDetails(vo);
            if (!b) {
                return R.fail("保存规则失败");
            }
            boolean b1 = true;

            if (vo.getRulesParametersLOD() != null && vo.getRulesParametersLOD().size() > 0){
                for (RulesParametersLOD rulesParametersLOD : vo.getRulesParametersLOD()) {
                    rulesParametersLOD.setPid(vo.getId());
                    rulesParametersLOD.setStatus(0);
                    b1 = rulesParametersLODService.insertRulesParametersLOD(rulesParametersLOD);
                }
                if (!b1){
                    return R.fail("保存规则失败");
                }
            }

            // 更新规则库状态
            RulesLibrary rulesLibraryById = rulesLibraryService.getRulesLibraryById(vo.getId());
            rulesLibraryById.setStatus(1);
            rulesLibraryService.updateRulesLibrary(rulesLibraryById);

            return R.data("保存规则成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteRulesDetail.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteRulesDetail", notes = "deleteRulesDetail")
    public R<Boolean> deleteRulesDetail(@RequestParam String id) {
        try {
            boolean b = rulesDetailService.deleteRulesDetailById(id);
            boolean c = rulesParametersService.deleteRulesParametersByDetailId(id);
            if (b && c){
                return R.data(true);
            }else {
                return R.fail("删除规则失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    ---------LOD属性------------

    @SneakyThrows
    @GetMapping("/getRulesParametersLODById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRulesParametersLODById", notes = "getRulesParametersLODById")
    public R<RulesParametersLOD> getRulesParametersLODById(@RequestParam String id) {
        try {
            return R.data(rulesParametersLODService.getRulesParametersLODById(id));
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteRulesParametersLOD.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteRulesParametersLOD", notes = "deleteRulesParametersLOD")
    public R<Boolean> deleteRulesParametersLOD(@RequestParam String id) {
        try {
            return R.data(rulesParametersLODService.deleteRulesParametersById(id));
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }



//    ----------规则最小子集-------------

    @SneakyThrows
    @GetMapping("/getRulesParametersById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRulesParametersById", notes = "getRulesParametersById")
    public R<RulesParameters> getRulesParametersById( @RequestParam String id) {
        try {
            return R.data(rulesParametersService.getRulesParametersById(id));
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addRulesParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addRulesParameters", notes = "addRulesParameters")
    public R<Boolean> addRulesParameters(@RequestBody RulesParameters rulesParameters) {
        try {
            return R.data(rulesParametersService.addRulesParameters(rulesParameters));
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateRulesParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateRulesParameters", notes = "updateRulesParameters")
    public R<Boolean> updateRulesParameters(@RequestBody RulesParameters rulesParameters) {
        try {
            return R.data(rulesParametersService.updateRulesParameters(rulesParameters));
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteRulesParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteRulesParameters", notes = "deleteRulesParameters")
    public R<Boolean> deleteRulesParameters(@RequestParam String id) {
        try {
            return R.data(rulesParametersService.deleteRulesParametersById(id));
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

}