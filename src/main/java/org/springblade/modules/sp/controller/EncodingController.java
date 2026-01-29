package org.springblade.modules.sp.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.EbsParametesCategoryDTO;
import org.springblade.modules.sp.dto.EncodingExcelDTO;
import org.springblade.modules.sp.dto.EncodingLibraryExcelDTO;
import org.springblade.modules.sp.dto.ExcelHeaderAndData;
import org.springblade.modules.sp.entity.*;
import org.springblade.modules.sp.excel.EasyExcelReadUtils;
import org.springblade.modules.sp.service.*;
import org.springblade.modules.sp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.springblade.modules.sp.excel.EasyExcelReadUtils.readExcelWithMultiSheets;
import static org.springblade.modules.sp.excel.EasyExcelReadUtils.readInputStreamToByteArray;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/encoding")
@Api(value = "分类编码控制层", tags = "分类编码控制层")
public class EncodingController extends BladeController {

    private static final Logger log = LoggerFactory.getLogger(EncodingController.class);
    @Autowired
    private EncodingService encodingService;
    @Autowired
    private EncodingParametersService encodingParametersService;
    @Autowired
    private EncodingLibraryService encodingLibraryService;
    @Autowired
    private EncodingConfigService encodingConfigService;
    @Autowired
    private EncodingElementService encodingElementService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ExpInstancesService expInstancesService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private RevitAllService revitAllService;
//    ----------------分类编码(外层)-----------------

    @SneakyThrows
    @GetMapping("/getEncodingById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getEncodingById", notes = "getEncodingById")
    public R<Encoding> getEncodingById(@RequestParam String id) {
        try {
           return R.data(encodingService.getById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getAllEncoding.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllEncoding", notes = "getAllEncoding")
    public R<List<Encoding>> getAllEncoding() {
        try {
            List<Encoding> allEncoding = encodingService.getAllEncoding();
            R.success("获取编码列表成功！");
            return R.data(allEncoding);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/filterEncoding.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "filterEncoding", notes = "filterEncoding")
    public R<PageEncoding> filterEncoding(@RequestParam Optional<String> name, @RequestParam Optional<String> version, @RequestParam Optional<String> auth, @RequestParam Optional<String> user,
                                          @RequestParam Optional<String> source, @RequestParam Optional<String> isfinished, @RequestParam Integer pageSize, @RequestParam Integer currentPage) {
        try {
           PageEncoding pageEncoding = new PageEncoding();
           if (pageSize == 0 || currentPage == 0 || pageSize == null || currentPage == null){
                return R.fail("分页数据获取失败)");
            }
            Page<Encoding> page = encodingService.filterEncoding(name.orElse(null), version.orElse(null), auth.orElse(null), user.orElse(null), source.orElse(null), isfinished.orElse(null), pageSize, currentPage);
            pageEncoding.setPage(page);
            pageEncoding.setTotal(page.getTotal());
            return R.data(pageEncoding);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addEncoding.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addEncoding", notes = "addEncoding")
    public R<String> addEncoding(@RequestBody Encoding encoding) {
        try {
            encoding.setSource("user");
            boolean b = encodingService.addEncoding(encoding);
            if (b){
                return R.data(encoding.getId());
            }else {
                return R.fail("添加编码失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addStaticEncoding.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addStaticEncoding", notes = "addStaticEncoding")
    public R<String> addStaticEncoding(@RequestBody Encoding encoding) {
        try {
            encoding.setSource("SMARTLINK");
            boolean b = encodingService.addEncoding(encoding);
            if (b){
                return R.data(encoding.getId());
            }else {
                return R.fail("预置编码失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteEncodingById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteEncodingById", notes = "deleteEncodingById")
    public R<String> deleteEncodingById(@RequestParam String id) {
        try {
            boolean b = encodingService.deleteEncoding(id);
            if (b){
                boolean c = encodingParametersService.deleteEncodingParametersByEncodingId(id);
                if (c){
                    return R.data("删除编码成功！");
                }else {
                    return R.fail("删除编码失败！");
                }
            }else {
                return R.fail("删除编码失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateEncoding.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateEncoding", notes = "updateEncoding")
    public R<String> updateEncoding(@RequestBody Encoding encoding) {
        try {
            boolean b = encodingService.updateEncoding(encoding);
            if (b){
                return R.data("更新编码成功！: ID ："+encoding.getId());
            }else {
                return R.fail("更新编码失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    -------------分类编码详情（内层）----------------

    @SneakyThrows
    @GetMapping("/getEncodingParametersById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getEncodingParametersById", notes = "getEncodingParametersById")
    public R<EncodingParameters> getEncodingParametersById(@RequestParam String id) {
        try {
            return R.data(encodingParametersService.getById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }


    @SneakyThrows
    @GetMapping("/getAllEncodingParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllEncodingParameters", notes = "getAllEncodingParameters")
    public R<List<EncodingParameters>> getAllEncodingParameters() {
        try {
            List<EncodingParameters> allEncodingParameters = encodingParametersService.getAllEncodingParameters();
            R.success("获取EncodingParameters列表成功！");
            return R.data(allEncodingParameters);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getAllEncodingParametersByLv0.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllEncodingParametersByLv0", notes = "getAllEncodingParametersByLv0")
    public R<List<EncodingParameters>> getAllEncodingParametersByLv0(@RequestParam String id) {
        try {
            List<EncodingParameters> allEncodingParametersByLv0Id = encodingParametersService.getAllSubRecordsByLv0Id(id);
            return R.data(allEncodingParametersByLv0Id);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getAllEncodingParametersByEncodingId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllEncodingParametersByEncodingId", notes = "getAllEncodingParametersByEncodingId")
    public R<List<EncodingParameters>> getAllEncodingParametersByEncodingId(@RequestParam String encodingId){
        try {
            List<EncodingParameters> allEncodingParametersByEncodingId = encodingParametersService.getAllEncodingParametersByEncodingId(encodingId);
            return R.data(allEncodingParametersByEncodingId);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }




//    @SneakyThrows
//    @GetMapping("/filterEncodingParameters.do")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "filterEncodingParameters", notes = "filterEncodingParameters")
//    public R<PageEncodingParameters> filterEncodingParameters(@RequestParam Optional<String> name,@RequestParam Optional<String> version,@RequestParam Optional<String> auth,@RequestParam Optional<String> user,
//                                                              @RequestParam Optional<String> source,@RequestParam Optional<String> detail,@RequestParam Optional<String> isfinished,@RequestParam Integer pageSize, @RequestParam Integer currentPage) {
//        try {
//            PageEncodingParameters pageEncodingParameters = new PageEncodingParameters();
//            if (pageSize == 0 || currentPage == 0 || pageSize == null || currentPage == null){
//                return R.fail("分页数据获取失败)");
//            }
//            Page<EncodingParameters> page = encodingParametersService.filterEncodingParameters(name.orElse(null), version.orElse(null), auth.orElse(null),
//                    user.orElse(null), source.orElse(null),pageSize,currentPage);
//            pageEncodingParameters.setPage(page);
//            pageEncodingParameters.setTotal(page.getTotal());
//            return R.data(pageEncodingParameters);
//        }catch (Exception e){
//            e.printStackTrace();
//            return R.fail(e.getMessage());
//        }
//    }

    @SneakyThrows
    @PostMapping("/addEncodingParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addEncodingParameters", notes = "addEncodingParameters")
    public R<String> addEncodingParameters(@RequestBody EncodingParameters encodingParameters) {
        try {
            encodingParameters.setCategory(new String[]{});
            encodingParameters.setEncodingStatus("0");
            boolean b = encodingParametersService.addEncodingParameters(encodingParameters);
            Encoding byId = encodingService.getById(encodingParameters.getEncodingId());
            byId.setStatus(0);
            if (b){
                encodingService.updateEncoding(byId);
                return R.data(encodingParameters.getId());
            }else {
                return R.fail("添加EncodingParameters失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addEncodingParametersList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addEncodingParametersList", notes = "addEncodingParametersList")
    public R<String> addEncodingParametersList(@RequestBody List<EncodingParameters> encodingParametersList) {
        try {
            String resId = "";
            for (EncodingParameters encodingParameters : encodingParametersList) {
                encodingParameters.setCategory(new String[]{});
                encodingParameters.setEncodingStatus("0");
                resId += encodingParameters.getId() + ",";;
                Encoding byId = encodingService.getById(encodingParameters.getEncodingId());
                encodingService.updateEncoding(byId);
            }
            boolean b = encodingParametersService.addEncodingParametersList(encodingParametersList);
            if (b){
                return R.data(resId);
            }else {
                return R.fail("添加EncodingParameters失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteEncodingParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteEncodingParameters", notes = "deleteEncodingParameters")
    public R<String> deleteEncodingParametersById(@RequestParam String id) {
        try {
            boolean b = encodingParametersService.deleteEncodingParameters(id);
            if (b){
                return R.data("删除EncodingParameters成功！");
            }else {
                return R.fail("删除EncodingParameters失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateEncodingParameters.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateEncodingParameters", notes = "updateEncodingParameters")
    public R<String> updateEncodingParameters(@RequestBody EncodingParameters encodingParameters) {
        try {
            boolean b = encodingParametersService.updateEncodingParameters(encodingParameters);
            Encoding byId = encodingService.getById(encodingParameters.getEncodingId());
            byId.setStatus(0);
            if (b){
                encodingService.updateEncoding(byId);
                return R.data("更新EncodingParameters成功！: ID ："+encodingParameters.getId());
            }else {
                return R.fail("更新EncodingParameters失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateEncodingStatus.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateEncodingStatus", notes = "updateEncodingStatus")
    public R<String> updateEncodingStatus(@RequestParam String encodingId) {
        try {
            Encoding encoding = encodingService.getById(encodingId);
            encoding.setStatus(1);
            boolean c = encodingService.updateEncoding(encoding);
            boolean b = encodingParametersService.updateEncodingStatus(encodingId);
            if (b&&c){
                return R.data("更新EncodingParameters成功！: ID ："+encodingId);
            }else {
                return R.fail("更新EncodingParameters失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/linkEncodingParametersCategory.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "linkEncodingParametersCategory", notes = "linkEncodingParametersCategory")
    public R<String> linkEncodingParametersCategory(@RequestBody EbsParametesCategoryDTO encodingParametersDTO) {
       try {
           String id = encodingParametersDTO.getId();
           String[] categories = encodingParametersDTO.getCategories();
           EncodingParameters ep = encodingParametersService.getById(id);
           ep.setCategory(categories);
           boolean res = encodingParametersService.updateEncodingParameters(ep);
           if (res){
               R.success("绑定category成功！");
               return R.data("EncodingParameters:"+ep.getId()+"绑定category成功！categoryId为"+categories);
           }
           return R.data("绑定失败");
       }catch (Exception e){
           e.printStackTrace();
           return R.fail(e.getMessage());
       }

    }

    @SneakyThrows
    @PostMapping("/unlinkEncodingParametersCategory.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "unlinkEncodingParametersCategory", notes = "unlinkEncodingParametersCategory")
    public R<Boolean> unlinkEncodingParametersCategory(@RequestBody EbsParametesCategoryDTO encodingParametersDTO) {
        try {
            String id = encodingParametersDTO.getId();
            String[] categories = encodingParametersDTO.getCategories();
            EncodingParameters ep = encodingParametersService.getById(id);
            List<String> categoriesList = new ArrayList<>(Arrays.asList(ep.getCategory()));
            categoriesList.removeAll(Arrays.asList(categories));
            ep.setCategory(categoriesList.toArray(new String[0]));
            boolean res = encodingParametersService.updateEncodingParameters(ep);
            if (res){
                return R.success("解绑category成功！");
            }
            return R.data(res);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }
//---------------------语义字典------------------------

    @SneakyThrows
    @GetMapping("/getEncodingLibraryById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getEncodingLibraryById", notes = "getEncodingLibraryById")
    public R<EncodingLibrary> getEncodingLibraryById(@RequestParam String id) {
        try {
            return R.data(encodingLibraryService.getById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getAllEncodingLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllEncodingLibrary", notes = "getAllEncodingLibrary")
    public R<List<EncodingLibrary>> getAllEncodingLibrary(@RequestParam Optional<String> name, @RequestParam Optional<String> code, @RequestParam Optional<String> lv, @RequestParam Optional<Integer> status) {
        try {
            List<EncodingLibrary> allEncodingLibrary = encodingLibraryService.getAllEncodingLibraries(name.orElse(null), code.orElse(null), lv.orElse(null), status.orElse(null));
            R.success("获取EncodingLibrary列表成功！");
            return R.data(allEncodingLibrary);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/filterEncodingLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "filterEncodingLibrary", notes = "filterEncodingLibrary")
    public R<PageEncodingLib> filterEncodingLibrary(@RequestParam Optional<String> name, @RequestParam Optional<String> code, @RequestParam Optional<String> lv, @RequestParam Optional<Integer> status, @RequestParam Integer pageSize, @RequestParam Integer currentPage) {
        try {
            PageEncodingLib pageEncodingLib = new PageEncodingLib();
            if (pageSize == 0 || currentPage == 0 || pageSize == null || currentPage == null){
                return R.fail("分页数据获取失败)");
            }
            Page<EncodingLibrary> page = encodingLibraryService.filterEncodingLibrary(name.orElse(null), code.orElse(null), lv.orElse(null), status.orElse(null), pageSize, currentPage);
            pageEncodingLib.setPage(page);
            pageEncodingLib.setTotal(page.getTotal());
            return R.data(pageEncodingLib);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }


    @SneakyThrows
    @PostMapping("/addEncodingLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addEncodingLibrary", notes = "addEncodingLibrary")
    public R<String> addEncodingLibrary(@RequestBody EncodingLibrary encodingLibrary) {
        try {
            boolean b = encodingLibraryService.addEncodingLibrary(encodingLibrary);
            if (b){
                return R.data("添加EncodingLibrary成功！: ID ："+encodingLibrary.getId());
            }else {
                return R.fail("添加EncodingLibrary失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addEncodingLibraryList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addEncodingLibraryList", notes = "addEncodingLibraryList")
    public R<String> addEncodingLibraryList(@RequestBody List<EncodingLibrary> encodingLibraryList) {
        try {
            boolean b = encodingLibraryService.addEncodingLibraryList(encodingLibraryList);
            if (b){
                return R.data("批量添加EncodingLibrary成功！");
            }else {
                return R.fail("批量添加EncodingLibrary失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteEncodingLibraryById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteEncodingLibraryById", notes = "deleteEncodingLibraryById")
    public R<String> deleteEncodingLibraryById(@RequestParam String id) {
        try {
            boolean b = encodingLibraryService.deleteEncodingLibrary(id);
            if (b){
                return R.data("删除EncodingLibrary成功！");
            }else {
                return R.fail("删除EncodingLibrary失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateEncodingLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateEncodingLibrary", notes = "updateEncodingLibrary")
    public R<String> updateEncodingLibrary(@RequestBody EncodingLibrary encodingLibrary) {
        try {
            boolean b = encodingLibraryService.updateEncodingLibrary(encodingLibrary);
            if (b){
                return R.data("更新EncodingLibrary成功！: ID ："+encodingLibrary.getId());
            }else {
                return R.fail("更新EncodingLibrary失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/uploadLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadLibrary", notes = "uploadLibrary")
    public  R<Boolean> uploadLibrary(@RequestParam("file") MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()){
            List<EncodingLibraryExcelDTO> dataList = EasyExcelReadUtils.readEncodingLiberaryExcel(inputStream);

            // 将 DTO 对象列表转换为 Library 实体类列表
            List<EncodingLibrary> libraryList = dataList.stream()
                    .map(dto -> {
                        EncodingLibrary encodingLibrary = new EncodingLibrary(
                                dto.getName(),
                                dto.getCode(),
                                dto.getDetail(),
                                dto.getLv(),
                                "admin",
                                new Date(),
                                0
                        );
                        encodingLibrary.setId(UUID.randomUUID().toString());
                        encodingLibraryService.addEncodingLibrary(encodingLibrary);
                        return encodingLibrary;
                    })
                    .collect(Collectors.toList());

            return R.success("success");
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }


    @SneakyThrows
    @PostMapping("/uploadEncoding.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadEncoding", notes = "uploadEncoding")
    public R<Boolean> uploadEncodingExcel(@RequestParam("file") MultipartFile file, @RequestParam String encodingId) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {

            // 将 InputStream 转换为字节数组
            byte[] excelData = EasyExcelReadUtils.readInputStreamToByteArray(inputStream);

            // 判断是单表还是多表
            String fileName = file.getOriginalFilename();
            ExcelTypeEnum excelType = fileName.endsWith(".xls") ? ExcelTypeEnum.XLS : ExcelTypeEnum.XLSX;

            // 检查是否为多Sheet Excel（判断sheet的数量）
            List<ReadSheet> sheets = EasyExcel.read(new ByteArrayInputStream(excelData))
                    .excelType(excelType)
                    .build()
                    .excelExecutor()
                    .sheetList();

            if (sheets.size() == 1) {
                // 读取 Excel 文件，获取 lv=0 的数据和实际 EncodingExcelDTO 数据
                ExcelHeaderAndData excelHeaderAndData = EasyExcelReadUtils.readExcelWithHeader(new ByteArrayInputStream(excelData));

                // 存储每个层级的最新WbsParameters对象，key为层级名称
                Map<String, EncodingParameters> latestNodeMap = new HashMap<>();

                // 获取 header 和 data 部分
                Map<String, String> headerRows = excelHeaderAndData.getHeader();
                List<EncodingExcelDTO> excelDataList = excelHeaderAndData.getData();

                // 存储 EncodingParameters 列表
                List<EncodingParameters> encodingParametersList = new ArrayList<>();

                // 处理 lv=0 的数据
                EncodingParameters rootNode = new EncodingParameters();
                if (!headerRows.isEmpty()) {
                    String name = headerRows.get("标题");
                    String version = headerRows.get("版本");

                    // 创建 lv=0 的根节点 EncodingParameters 对象
                    rootNode.setPid("0");  // 根节点 pid 为 0
                    rootNode.setLv("0");  // 层级 lv 为 0
                    rootNode.setName(name);  // 设置 name
                    rootNode.setVersion(version);  // 设置 version
                    rootNode.setEncodingStatus("0");
                    rootNode.setEncodingId(encodingId);
                    rootNode.setStatus(0);

                    encodingParametersList.add(rootNode);
                }

                // 处理实际的 EncodingExcelDTO 数据
                for (EncodingExcelDTO excelDTO : excelDataList) {
                    // 创建新的 EncodingParameters 对象
                    EncodingParameters encodingParam = new EncodingParameters();
                    String pid = null;

                    if (latestNodeMap.containsKey(fileService.getEncodingParentLevel(excelDTO.getLv()))) {
                        pid = latestNodeMap.get(fileService.getEncodingParentLevel(excelDTO.getLv())).getId();
                    }
                    // 如果当前条目的层级为 1，则更新 parentId 为当前条目的 ID
                    if ("1".equals(excelDTO.getLv())) {
                        pid = rootNode.getId();  // 更新父级 ID 为0级id
                    }
                    encodingParam.setPid(pid);  // 设置父级 ID
                    encodingParam.setEncodingId(encodingId);  // 生成随机的 encodingId
                    encodingParam.setCode(excelDTO.getCode());  // 设置编码
                    encodingParam.setName(excelDTO.getName());  // 设置名称
                    encodingParam.setLv(excelDTO.getLv());  // 设置层级
                    encodingParam.setEncodingStatus("0");
                    encodingParam.setStatus(0);  // 示例状态

                    if (excelDTO.getCategory() == null) {
                        encodingParam.setCategory(new String[]{});  // 设置 category
                    } else {
                        encodingParam.setCategory(excelDTO.getCategory());  // 设置 category
                    }

                    encodingParam.setModifyUser("admin");
                    encodingParam.setModifyTime(new Date());

                    encodingParametersList.add(encodingParam);

                    // 更新Map中当前层级的最新节点
                    latestNodeMap.put(excelDTO.getLv(), encodingParam);
                }

                // 批量插入到数据库
                if (!encodingParametersList.isEmpty()) {
                    encodingParametersService.addEncodingParametersList(encodingParametersList);  // 批量保存数据到数据库
                }

                return R.success("导入成功");

            } else if (sheets.size() > 1) {
                // 多表上传：调用多表处理方法
                String fileName2 = file.getOriginalFilename();
                ExcelTypeEnum excelType2 = fileName2.endsWith(".xls") ? ExcelTypeEnum.XLS : ExcelTypeEnum.XLSX;

                // 调用多 Sheet 解析方法
                List<ExcelHeaderAndData> allSheetsData = readExcelWithMultiSheets(new ByteArrayInputStream(excelData), excelType2);

                // 遍历每个 Sheet 数据进行处理
                for (ExcelHeaderAndData sheetData : allSheetsData) {
                    processSheetData(sheetData, encodingId); // 自定义方法处理每个 Sheet 数据
                }
                return R.success("导入成功");
            } else {
                return R.fail("Excel文件中没有有效的sheet");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return R.fail("导入失败: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("处理 Excel 数据时出错: " + e.getMessage());
        }
    }

//    @SneakyThrows
//    @PostMapping("/uploadEncodingExcels.do")
//    @ApiOperationSupport(order = 3)
//    @ApiOperation(value = "uploadEncodingExcels", notes = "uploadEncodingExcels")
//    public R<Boolean> uploadEncodingExcels(@RequestParam("file") MultipartFile file, @RequestParam String encodingId) {
//        try (InputStream inputStream = file.getInputStream()) {
//            String fileName = file.getOriginalFilename();
//            ExcelTypeEnum excelType = fileName.endsWith(".xls") ? ExcelTypeEnum.XLS : ExcelTypeEnum.XLSX;
//
//            // 调用多 Sheet 解析方法
//            List<ExcelHeaderAndData> allSheetsData = readExcelWithMultiSheets(inputStream, excelType);
//
//            // 遍历每个 Sheet 数据进行处理
//            for (ExcelHeaderAndData sheetData : allSheetsData) {
//                processSheetData(sheetData, encodingId); // 自定义方法处理每个 Sheet 数据
//            }
//
//            return R.success("导入成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return R.fail("导入失败: " + e.getMessage());
//        }
//    }

    private void processSheetData(ExcelHeaderAndData sheetData, String encodingId) {
        // 获取表头和数据
        Map<String, String> headerRows = sheetData.getHeader();
        List<EncodingExcelDTO> excelDataList = sheetData.getData();

        // 存储每个层级的最新 EncodingParameters 对象
        Map<String, EncodingParameters> latestNodeMap = new HashMap<>();

        // 存储 EncodingParameters 列表
        List<EncodingParameters> encodingParametersList = new ArrayList<>();

        // 处理表头部分，生成 root 节点
        if (!headerRows.isEmpty()) {
            EncodingParameters rootNode = new EncodingParameters();
            rootNode.setPid("0"); // 顶级节点无父 ID
            rootNode.setLv("0"); // 顶级层级
            rootNode.setName(headerRows.get("标题")); // 表头中的标题
            rootNode.setVersion(headerRows.get("版本")); // 表头中的版本
            rootNode.setEncodingStatus("0"); // 初始状态
            rootNode.setEncodingId(encodingId); // 引用外部的 encodingId
            rootNode.setStatus(0); // 启用状态

            encodingParametersList.add(rootNode);


            // 处理数据行部分
            for (EncodingExcelDTO excelDTO : excelDataList) {
                EncodingParameters encodingParam = new EncodingParameters();
                String pid = null;

                // 确定父级节点的 ID
                if (latestNodeMap.containsKey(fileService.getEncodingParentLevel(excelDTO.getLv()))) {
                    pid = latestNodeMap.get(fileService.getEncodingParentLevel(excelDTO.getLv())).getId();
                }
                if ("1".equals(excelDTO.getLv())) {
                    pid = rootNode.getId();  // 更新父级 ID 为0级id
                }

                // 填充 EncodingParameters 数据
                encodingParam.setPid(pid);
                encodingParam.setEncodingId(encodingId);
                encodingParam.setCode(excelDTO.getCode());
                encodingParam.setName(excelDTO.getName());
                encodingParam.setLv(excelDTO.getLv());
                encodingParam.setEncodingStatus("0");
                encodingParam.setStatus(0);
                encodingParam.setCategory(excelDTO.getCategory() == null ? new String[]{} : excelDTO.getCategory());
                encodingParam.setModifyUser("admin");
                encodingParam.setModifyTime(new Date());

                // 将当前层级的 EncodingParameters 保存到 Map，方便后续引用
                latestNodeMap.put(excelDTO.getLv(), encodingParam);

                // 添加到批量处理列表
                encodingParametersList.add(encodingParam);
            }
            // 批量插入数据库
            if (!encodingParametersList.isEmpty()) {
                encodingParametersService.addEncodingParametersList(encodingParametersList);
            }
        }
    }

    @SneakyThrows
    @PostMapping("/validateEncodingParameters.do")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "validateEncodingParameters", notes = "validateEncodingParameters")
    public R<List<EncodingParameters>> validateEncodingParameters(@RequestParam String encodingId) {
        try {
            // 1. 获取 EncodingParameters 和 EncodingLibrary 的数据列表
            List<EncodingParameters> parametersList = encodingParametersService.getAllEncodingParametersByEncodingId(encodingId);
            List<EncodingLibrary> libraryList = encodingLibraryService.getAll();

            // 2. 将 EncodingLibrary 的 code 和 lv 组合成一个唯一标识的键值对存入 Set
            Set<String> libraryCodeLvSet = libraryList.stream()
                    .map(library -> library.getCode() + "-" + library.getLv())
                    .collect(Collectors.toSet());

            // 3. 过滤出那些 code 和 lv 在 libraryCodeLvSet 中不存在的 EncodingParameters
            List<EncodingParameters> resList = parametersList.stream()
                    .filter(param -> !libraryCodeLvSet.contains(param.getCode() + "-" + param.getLv()))
                    .collect(Collectors.toList());

            // 4. 返回结果
            return R.data(resList);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("获取失败！ " + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/validateSingleEncodingParameters.do")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "validateSingleEncodingParameters", notes = "validateSingleEncodingParameters")
    public R<Boolean> validateSingleEncodingParameters(@RequestParam String encodingParametersId) {
        try {
            EncodingParameters encodingParameters = encodingParametersService.getById(encodingParametersId);
            // 获取所有语义字典
            List<EncodingLibrary> libraryList = encodingLibraryService.getAll();

            // 将库中的 code 转换为去除空格和小写形式的 Set，确保 null 值处理正确
            Set<String> libraryCodes = libraryList.stream()
                    .map(library -> Optional.ofNullable(library.getCode()).orElse("").trim().toLowerCase())
                    .filter(code -> !code.isEmpty()) // 排除空字符串
                    .collect(Collectors.toSet());

            // 过滤不在库中的编码参数，确保 null 值处理正确
            if (encodingParameters != null) {
                String paramCode = Optional.ofNullable(encodingParameters.getCode()).orElse("").trim().toLowerCase();
                // 如果 paramCode 是空字符串或者不在 libraryCodes 中，则保留该记录
                if (!paramCode.isEmpty() && !libraryCodes.contains(paramCode)) {
                    return R.data(false);
                }else {
                    return R.data(true);
                }
            }
            return R.data(false);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("校验失败！ " + e.getMessage());
        }
    }

//    ----------------编码配置-------------

    @SneakyThrows
    @GetMapping("/getEncodingConfig.do")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "getEncodingConfig", notes = "getEncodingConfig")
    public R<EncodingConfig> getEncodingConfig(@RequestParam String id) {
        try {
            EncodingConfig encodingConfig = encodingConfigService.getById(id);
            return R.data(encodingConfig);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("获取失败！ " + e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getSheetByParametersId.do")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "getSheetByParametersId", notes = "getSheetByParametersId")
    public R<EncodingParameters> getSheetByParametersId(@RequestParam String encodingParametersId) {
        try {
            EncodingParameters sheetByParametersId = encodingParametersService.getSheetByParametersId(encodingParametersId);
            return R.data(sheetByParametersId);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("获取失败！ " + e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/searchEncodingConfig.do")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "searchEncodingConfig", notes = "searchEncodingConfig")
    public R<List<EncodingConfig>> searchEncodingConfig(@RequestParam Optional<String> encodingParametersId,@RequestParam Optional<String> lv,  @RequestParam Optional<String> lvCode) {
        try {
            List<EncodingConfig> encodingConfigs = encodingConfigService.searchFilter(encodingParametersId.orElse(null), lv.orElse(null), lvCode.orElse(null));
            return R.data(encodingConfigs);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("获取失败！ " + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateEncodingConfig.do")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "updateEncodingConfig", notes = "updateEncodingConfig")
    public R<String> updateEncodingConfig(@RequestBody EncodingConfig encodingConfig) {
        try {
            encodingConfig.setStatus(0);
            encodingConfig.setModifyTime(new Date());
            encodingConfigService.updateEncodingConfig(encodingConfig);
            return R.data("更新成功,Id:"+encodingConfig.getId());
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("更新失败！ " + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addEncodingConfig.do")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "addEncodingConfig", notes = "addEncodingConfig")
    public R<String> addEncodingConfig(@RequestBody EncodingConfig encodingConfig) {
        try {
            encodingConfig.setStatus(0);
            encodingConfig.setModifyTime(new Date());
            encodingConfigService.addEncodingConfig(encodingConfig);
            return R.data("新增成功,Id:"+encodingConfig.getId());
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("新增失败！ " + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteEncodingConfig.do")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "deleteEncodingConfig", notes = "deleteEncodingConfig")
    public R<Boolean> deleteEncodingConfig(@RequestParam String id) {
        try {
            encodingConfigService.deleteEncodingConfigById(id);
            return R.data(true);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("删除失败！ " + e.getMessage());
        }
    }


//    ---------------云端赋码-------------------

    @SneakyThrows
    @GetMapping("/getEncodingElementById.do")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "getEncodingElementById", notes = "getEncodingElementById")
    public R<EncodingElement> getEncodingElement(@RequestParam String id) {
        try {
            return R.data(encodingElementService.getById(id));
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("获取失败！ " + e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getEncodingElement.do")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "getEncodingElement", notes = "getEncodingElement")
    public R<EncodingElement> getEncodingElement(@RequestParam String streamId, @RequestParam String branchId, @RequestParam String elementId, @RequestParam String encodingId) {
        try {
            return R.data(encodingElementService.getEncodingElement(streamId, branchId, elementId, encodingId));
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("获取失败！ " + e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getEncodingElements.do")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "getEncodingElements", notes = "getEncodingElements")
    public R<List<EncodingElement>> getEncodingElements(@RequestParam String streamId, @RequestParam String branchId, @RequestParam String elementId, @RequestParam String[] encodingId) {
        try {
            return R.data(encodingElementService.getEncodingElements(streamId, branchId, elementId, encodingId));
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("获取失败！ " + e.getMessage());
        }
    }

//    @SneakyThrows
//    @PostMapping("/getEncodingElementsNoElementId.do")
//    @ApiOperationSupport(order = 9)
//    @ApiOperation(value = "getEncodingElementsNoElementId", notes = "getEncodingElementsNoElementId")
//    public R<List<EncodingElement>> getEncodingElementsNoElementId(@RequestParam String streamId, @RequestParam String branchId, @RequestParam String[] encodingId) {
//        try {
//            return R.data(encodingElementService.getEncodingElementsNoElementId(streamId, branchId, encodingId));
//        }catch (Exception e) {
//            e.printStackTrace();
//            return R.fail("获取失败！ " + e.getMessage());
//        }
//    }

    @SneakyThrows
    @GetMapping("/searchEncodingElement.do")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "searchEncodingElement", notes = "searchEncodingElement")
    public R<List<EncodingElement>> searchEncodingElement(@RequestParam Optional<String> streamId, @RequestParam Optional<String> branchId, @RequestParam Optional<String> elementId, @RequestParam Optional<String> encodingId) {
        try {
            return R.data(encodingElementService.filterEncodingElement(streamId.orElse(null), branchId.orElse(null), elementId.orElse(null), encodingId.orElse(null)));
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("获取失败！ " + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addEncodingElement.do")
    @ApiOperationSupport(order = 12)
    @ApiOperation(value = "addEncodingElement", notes = "addEncodingElement")
    public R<Boolean> addEncodingElement(@RequestBody EncodingElement encodingElement) {
        try {
            boolean b = false;
            EncodingElement encodingElement1 = encodingElementService.getEncodingElement(encodingElement.getStreamId(), encodingElement.getBranchId(), encodingElement.getElementId(), encodingElement.getEncodingId());
            if (encodingElement1 != null) {
                log.info("编码配置已存在，开始更新");
                encodingElement.setId(encodingElement1.getId());
                encodingElement.setEncodingStatus("0");
                boolean b1 = encodingElementService.updateEncodingElement(encodingElement);
                if (b1) {
                    b = true;
                }
            }else {
                log.info("编码配置未存在，开始更新");
                encodingElement.setId(UUID.randomUUID().toString());
                encodingElement.setEncodingStatus("0");
                //新增编码
                boolean b1 = encodingElementService.addEncodingElement(encodingElement);
                if (b1) {
                    b = true;
                }
            }
            //获取commitId
            String commitId = branchService.getLatestCommitsId(encodingElement.getBranchId());
            //获取存量的exp数据
            ExpInstances expInstances = expInstancesService.searchExpInstances(encodingElement.getStreamId(), encodingElement.getElementId(),commitId);
            JSONObject parameters = expInstances.getParameters();
            List<JSONObject> newJson =new ArrayList<>();
            //拼接新增的编码json数据
            // 构建 "Uniclass编码" 对象
            Encoding encoding = encodingService.getById(encodingElement.getEncodingId());
            String encodingName = encoding.getName();
//            EncodingParameters encodingParametersByCode = encodingParametersService.getEncodingParametersByCode(encodingElement.getEncodingId(), encodingElement.getCode());
            String encodingparameterName = encodingElement.getName();
            boolean isAdd = true;
            // 遍历parameters中的每一个entry
            if (parameters != null) {
                for (String key : parameters.keySet()) {
                    // 获取当前参数值，先进行非 null 和 JSONObject 类型检查
                    Object value = parameters.get(key);

                    if (value instanceof JSONObject) {
                        // 确保值是 JSONObject 类型，再进行操作
                        JSONObject parameter = (JSONObject) value;

                        // 获取当前parameter对象中的name字段
                        String currentName = parameter.getString("name");

                        // 如果找到了匹配的name，返回对应的value
                        if ((encodingName + "编码").equals(currentName)) {
//                        String pkey = key;
//                        String value = parameter.getString("value");
                            JSONObject uniclassCode = new JSONObject();
                            uniclassCode.put("id", parameter.getString("id"));
                            uniclassCode.put("name", encodingName+"编码");
                            uniclassCode.put("value",encodingElement.getCode());
                            uniclassCode.put("data_type", "文本");
                            uniclassCode.put("data_type_en", "Text");
                            uniclassCode.put("revit_parameters","常规");
                            uniclassCode.put("revit_parameters_en", "PG_GENERAL");
                            uniclassCode.put("string category", "Objects.BuiltElements.Revit.Parameter");
                            uniclassCode.put("pkey",parameter.getString("applicationInternalName"));

                            // 将 "Uniclass编码" 对象放入 newJson
                            newJson.add(uniclassCode);

                            isAdd = false;
                        }
                        if ((encodingName + "编码名称").equals(currentName)) {
                            JSONObject uniclassCodeName = new JSONObject();
                            // 构建 "Uniclass编码名称" 对象
                            uniclassCodeName.put("id", parameter.getString("id"));
                            uniclassCodeName.put("name", encodingName+"编码名称");
                            uniclassCodeName.put("value", encodingparameterName);
                            uniclassCodeName.put("data_type", "文本");
                            uniclassCodeName.put("data_type_en", "Text");
                            uniclassCodeName.put("revit_parameters","常规");
                            uniclassCodeName.put("revit_parameters_en", "PG_GENERAL");
                            uniclassCodeName.put("string category", "Objects.BuiltElements.Revit.Parameter");
                            uniclassCodeName.put("pkey",parameter.getString("applicationInternalName"));

                            newJson.add(uniclassCodeName);
                            isAdd = false;
                        }
                    }
                }
            }

            if (isAdd) {
                JSONObject uniclassCode = new JSONObject();
                JSONObject uniclassCodeName = new JSONObject();
                uniclassCode.put("id",UUID.randomUUID().toString());
                uniclassCode.put("name", encodingName+"编码");
                uniclassCode.put("value",encodingElement.getCode());
                uniclassCode.put("data_type", "文本");
                uniclassCode.put("data_type_en", "Text");
                uniclassCode.put("revit_parameters","常规");
                uniclassCode.put("revit_parameters_en", "PG_GENERAL");
                uniclassCode.put("string category", "Objects.BuiltElements.Revit.Parameter");
                uniclassCode.put("pkey","");

                // 将 "Uniclass编码" 对象放入 newJson
                newJson.add(uniclassCode);

                // 构建 "Uniclass编码名称" 对象
                uniclassCodeName.put("id", UUID.randomUUID().toString());
                uniclassCodeName.put("name", encodingName+"编码名称");
                uniclassCodeName.put("value", encodingparameterName);
                uniclassCodeName.put("data_type", "文本");
                uniclassCodeName.put("data_type_en", "Text");
                uniclassCodeName.put("revit_parameters","常规");
                uniclassCodeName.put("revit_parameters_en", "PG_GENERAL");
                uniclassCodeName.put("string category", "Objects.BuiltElements.Revit.Parameter");
                uniclassCodeName.put("pkey", "");

                // 将 "Uniclass编码名称" 对象放入 newJson
                newJson.add(uniclassCodeName);
            }

            RevitAll revitAllByCommitId = revitAllService.getRevitAllByCommitId(encodingElement.getStreamId(), encodingElement.getElementId(), commitId);

            if (revitAllByCommitId != null) {
                // 获取原有的 detail
                List<JSONObject> existingDetails = revitAllByCommitId.getDetail();
                if (existingDetails == null) {
                    existingDetails = new ArrayList<>();
                }

                // 遍历 newJson，检查是否有相同的 name
                for (JSONObject newItem : newJson) {
                    String newName = newItem.getString("name");
                    boolean isUpdated = false;

                    // 遍历 existingDetails，查找是否有相同的 name
                    for (JSONObject existingItem : existingDetails) {
                        String existingName = existingItem.getString("name");

                        if (existingName.equals(newName)) {
                            // 如果找到相同的 name，更新 value
                            existingItem.put("value", newItem.getString("value"));
                            isUpdated = true;
                            break;
                        }
                    }

                    // 如果没有找到相同的 name，则追加到 existingDetails 中
                    if (!isUpdated) {
                        existingDetails.add(newItem);
                    }
                }

                // 更新 RevitAll 对象
                revitAllByCommitId.setDetail(existingDetails);
                revitAllByCommitId.setModifyTime(new Date());
                revitAllByCommitId.setModifyUser(encodingElement.getModifyUser());

                // 更新数据库记录
                revitAllService.updateRevitAll(revitAllByCommitId);
            } else {
                // 如果 revitAllByCommitId 为空，按照原有逻辑新增
                RevitAll revitAll = new RevitAll();
                revitAll.setId(UUID.randomUUID().toString());
                revitAll.setObjId(expInstances.getObjId());
                revitAll.setElementId(encodingElement.getElementId());
                revitAll.setCommitId(commitId);
                revitAll.setDetail(newJson);
                revitAll.setStreamId(encodingElement.getStreamId());
                revitAll.setStatus(0);
                revitAll.setType("TEXT");
                revitAll.setCategory(expInstances.getCategory());
                revitAll.setBranchId(encodingElement.getBranchId());
                revitAll.setModifyTime(new Date());
                revitAll.setModifyUser(encodingElement.getModifyUser());
                revitAll.setIsfinished(0);
                revitAllService.addRevitAll(revitAll);
            }
            return R.data(b);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("新增失败！ " + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addEncodingElements.do")
    @ApiOperationSupport(order = 12)
    @ApiOperation(value = "addEncodingElements", notes = "addEncodingElements")
    public R<Boolean> addEncodingElements(@RequestBody List<EncodingElement> encodingElements) {
        try {
            boolean b = false;
            for (EncodingElement encodingElement : encodingElements) {
                EncodingElement encodingElement1 = encodingElementService.getEncodingElement(encodingElement.getStreamId(), encodingElement.getBranchId(), encodingElement.getElementId(), encodingElement.getEncodingId());
                if (encodingElement1 != null) {
                    log.info("编码配置已存在，开始更新");
                    encodingElement.setId(encodingElement1.getId());
                    encodingElement.setEncodingStatus("0");
                    boolean b1 = encodingElementService.updateEncodingElement(encodingElement);
                    if (b1) {
                        b = true;
                    }
                }else {
                    log.info("编码配置未存在，开始更新");
                    encodingElement.setId(UUID.randomUUID().toString());
                    encodingElement.setEncodingStatus("0");
                    //新增编码
                    boolean b1 = encodingElementService.addEncodingElement(encodingElement);
                    if (b1) {
                        b = true;
                    }
                }
                //获取commitId
                String commitId = branchService.getLatestCommitsId(encodingElement.getBranchId());
                //获取存量的exp数据
                ExpInstances expInstances = expInstancesService.searchExpInstances(encodingElement.getStreamId(), encodingElement.getElementId(),commitId);
                JSONObject parameters = expInstances.getParameters();
                List<JSONObject> newJson =new ArrayList<>();
                //拼接新增的编码json数据
                // 构建 "Uniclass编码" 对象
                Encoding encoding = encodingService.getById(encodingElement.getEncodingId());
                String encodingName = encoding.getName();
//                EncodingParameters encodingParametersByCode = encodingParametersService.getEncodingParametersByCode(encodingElement.getEncodingId(), encodingElement.getCode());
                String encodingparameterName = encodingElement.getName();
                boolean isAdd = true;
                // 遍历parameters中的每一个entry
                if (parameters != null) {
                    for (String key : parameters.keySet()) {
                        // 获取当前参数值，先进行非 null 和 JSONObject 类型检查
                        Object value = parameters.get(key);

                        if (value instanceof JSONObject) {
                            // 确保值是 JSONObject 类型，再进行操作
                            JSONObject parameter = (JSONObject) value;

                            // 获取当前parameter对象中的name字段
                            String currentName = parameter.getString("name");

                            // 如果找到了匹配的name，返回对应的value
                            if ((encodingName + "编码").equals(currentName)) {
//                        String pkey = key;
//                        String value = parameter.getString("value");
                                JSONObject uniclassCode = new JSONObject();
                                uniclassCode.put("id", parameter.getString("id"));
                                uniclassCode.put("name", encodingName+"编码");
                                uniclassCode.put("value",encodingElement.getCode());
                                uniclassCode.put("data_type", "文本");
                                uniclassCode.put("data_type_en", "Text");
                                uniclassCode.put("revit_parameters","常规");
                                uniclassCode.put("revit_parameters_en", "PG_GENERAL");
                                uniclassCode.put("string category", "Objects.BuiltElements.Revit.Parameter");
                                uniclassCode.put("pkey",parameter.getString("applicationInternalName"));

                                // 将 "Uniclass编码" 对象放入 newJson
                                newJson.add(uniclassCode);

                                isAdd = false;
                            }
                            if ((encodingName + "编码名称").equals(currentName)) {
                                JSONObject uniclassCodeName = new JSONObject();
                                // 构建 "Uniclass编码名称" 对象
                                uniclassCodeName.put("id", parameter.getString("id"));
                                uniclassCodeName.put("name", encodingName+"编码名称");
                                uniclassCodeName.put("value", encodingparameterName);
                                uniclassCodeName.put("data_type", "文本");
                                uniclassCodeName.put("data_type_en", "Text");
                                uniclassCodeName.put("revit_parameters","常规");
                                uniclassCodeName.put("revit_parameters_en", "PG_GENERAL");
                                uniclassCodeName.put("string category", "Objects.BuiltElements.Revit.Parameter");
                                uniclassCodeName.put("pkey",parameter.getString("applicationInternalName"));

                                newJson.add(uniclassCodeName);
                                isAdd = false;
                            }
                        }
                    }
                }

                if (isAdd) {
                    JSONObject uniclassCode = new JSONObject();
                    JSONObject uniclassCodeName = new JSONObject();
                    uniclassCode.put("id",UUID.randomUUID().toString());
                    uniclassCode.put("name", encodingName+"编码");
                    uniclassCode.put("value",encodingElement.getCode());
                    uniclassCode.put("data_type", "文本");
                    uniclassCode.put("data_type_en", "Text");
                    uniclassCode.put("revit_parameters","常规");
                    uniclassCode.put("revit_parameters_en", "PG_GENERAL");
                    uniclassCode.put("string category", "Objects.BuiltElements.Revit.Parameter");
                    uniclassCode.put("pkey","");

                    // 将 "Uniclass编码" 对象放入 newJson
                    newJson.add(uniclassCode);

                    // 构建 "Uniclass编码名称" 对象
                    uniclassCodeName.put("id", UUID.randomUUID().toString());
                    uniclassCodeName.put("name", encodingName+"编码名称");
                    uniclassCodeName.put("value", encodingparameterName);
                    uniclassCodeName.put("data_type", "文本");
                    uniclassCodeName.put("data_type_en", "Text");
                    uniclassCodeName.put("revit_parameters","常规");
                    uniclassCodeName.put("revit_parameters_en", "PG_GENERAL");
                    uniclassCodeName.put("string category", "Objects.BuiltElements.Revit.Parameter");
                    uniclassCodeName.put("pkey", "");

                    // 将 "Uniclass编码名称" 对象放入 newJson
                    newJson.add(uniclassCodeName);
                }
                RevitAll revitAllByCommitId = revitAllService.getRevitAllByCommitId(encodingElement.getStreamId(), encodingElement.getElementId(), commitId);

                if (revitAllByCommitId != null) {
                    // 获取原有的 detail
                    List<JSONObject> existingDetails = revitAllByCommitId.getDetail();
                    if (existingDetails == null) {
                        existingDetails = new ArrayList<>();
                    }

                    // 遍历 newJson，检查是否有相同的 name
                    for (JSONObject newItem : newJson) {
                        String newName = newItem.getString("name");
                        boolean isUpdated = false;

                        // 遍历 existingDetails，查找是否有相同的 name
                        for (JSONObject existingItem : existingDetails) {
                            String existingName = existingItem.getString("name");

                            if (existingName.equals(newName)) {
                                // 如果找到相同的 name，更新 value
                                existingItem.put("value", newItem.getString("value"));
                                isUpdated = true;
                                break;
                            }
                        }

                        // 如果没有找到相同的 name，则追加到 existingDetails 中
                        if (!isUpdated) {
                            existingDetails.add(newItem);
                        }
                    }

                    // 更新 RevitAll 对象
                    revitAllByCommitId.setDetail(existingDetails);
                    revitAllByCommitId.setModifyTime(new Date());
                    revitAllByCommitId.setModifyUser(encodingElement.getModifyUser());

                    // 更新数据库记录
                    revitAllService.updateRevitAll(revitAllByCommitId);
                } else {
                    // 如果 revitAllByCommitId 为空，按照原有逻辑新增
                    RevitAll revitAll = new RevitAll();
                    revitAll.setId(UUID.randomUUID().toString());
                    revitAll.setObjId(expInstances.getObjId());
                    revitAll.setElementId(encodingElement.getElementId());
                    revitAll.setCommitId(commitId);
                    revitAll.setDetail(newJson);
                    revitAll.setStreamId(encodingElement.getStreamId());
                    revitAll.setStatus(0);
                    revitAll.setType("TEXT");
                    revitAll.setCategory(expInstances.getCategory());
                    revitAll.setBranchId(encodingElement.getBranchId());
                    revitAll.setModifyTime(new Date());
                    revitAll.setModifyUser(encodingElement.getModifyUser());
                    revitAll.setIsfinished(0);
                    revitAllService.addRevitAll(revitAll);
                }
            }
            return R.data(b);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("新增失败！ " + e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getEncodingParametersByCode.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getEncodingParametersByCode", notes = "getEncodingParametersByCode")
    public R<EncodingParameters> getEncodingParametersByCode(@RequestParam String code,@RequestParam String encodingId) {
        try {
            EncodingParameters encodingParametersByCode = encodingParametersService.getEncodingParametersByCode(encodingId, code);
            return R.data(encodingParametersByCode);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("查询失败！ " + e.getMessage());
        }
    }


    @SneakyThrows
    @PostMapping("/deleteEncodingElement.do")
    @ApiOperationSupport(order = 13)
    @ApiOperation(value = "deleteEncodingElement", notes = "deleteEncodingElement")
    public R<Boolean> deleteEncodingElement(@RequestParam String id) {
        try {
            encodingElementService.deleteEncodingElement(id);
            return R.data(true);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail("删除失败！ " + e.getMessage());
        }
    }

//    --------------------获取带构建的属性----------------------

    @SneakyThrows
    @GetMapping("/getParameterByElement.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getParameterByElement", notes = "getParameterByElement")
    public R<Map<String,String>> getParameterByElement(@RequestParam String streamId, @RequestParam String elementId, @RequestParam String branchId,@RequestParam String encodingId) {
        try {
            //获取commitId
            String commitId = branchService.getLatestCommitsId(branchId);
            Map<String,String> res = new HashMap<>();
            ExpInstances expInstances = expInstancesService.searchExpInstances(streamId, elementId, commitId);
            EncodingElement encodingElement = encodingElementService.getEncodingElement(streamId, branchId, encodingId, elementId);
            res.put("code",encodingElement.getCode());
            res.put("category",expInstances.getCategory());
            res.put("type",expInstances.getType());
            return R.data(res);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/getParameterByElements.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getParameterByElements", notes = "getParameterByElements")
    public R<List<Map<String,String>>> getParameterByElements(@RequestBody EncodingElementVO encodingElementVO) {
        try {
            String streamId = encodingElementVO.getStreamId();
            String[] elementId = encodingElementVO.getElementId();
            String branchId = encodingElementVO.getBranchId();
            String encodingId = encodingElementVO.getEncodingId();
            //获取commitId
            String commitId = branchService.getLatestCommitsId(branchId);
            List< Map<String,String>> reslist = new ArrayList<>();
            List<ExpInstances> expInstancesByElements = expInstancesService.getExpInstancesByElements(streamId, elementId, commitId);
            for (ExpInstances expInstancesByElement : expInstancesByElements) {
                Map<String,String> res = new HashMap<>();
                if (StringUtils.isEmpty(encodingId)){
                    List<EncodingElement> encodingElementsNoEncodingId = encodingElementService.getEncodingElementsNoEncodingId(streamId, branchId, expInstancesByElement.getElementId());
                    for (EncodingElement encodingElement : encodingElementsNoEncodingId) {
                        if (encodingElement == null){
                            res.put("code","");
                        }else {
                            res.put("code",encodingElement.getCode());
                        }
                        res.put("elementId",expInstancesByElement.getElementId());
                        res.put("category",expInstancesByElement.getCategory());
                        res.put("type",expInstancesByElement.getType());
                        reslist.add(res);
                    }
                }else {
                    EncodingElement encodingElement = encodingElementService.getEncodingElement(streamId, branchId,expInstancesByElement.getElementId(),encodingId);
                    if (encodingElement == null){
                        res.put("code","");
                    }else {
                        res.put("code",encodingElement.getCode());
                    }
                    res.put("elementId",expInstancesByElement.getElementId());
                    res.put("category",expInstancesByElement.getCategory());
                    res.put("type",expInstancesByElement.getType());
                    reslist.add(res);
                }

            }
            return R.data(reslist);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }
}
