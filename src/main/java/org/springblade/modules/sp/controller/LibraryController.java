package org.springblade.modules.sp.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.*;
import org.springblade.modules.sp.entity.*;
import org.springblade.modules.sp.excel.EasyExcelReadUtils;
import org.springblade.modules.sp.service.*;
import org.springblade.modules.sp.service.impl.LibWbsTempServiceImpl;
import org.springblade.modules.sp.vo.LibTagVO;
import org.springblade.modules.sp.vo.LibraryVO;
import org.springblade.modules.sp.vo.LinkVO;
import org.springblade.modules.sp.vo.PageLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 属性字典控制层
 * @author dengtx
 * @since 2024年6月7日11:27:13
 */
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/library")
@Api(value = "属性字典控制层", tags = "属性字典控制层")
public class LibraryController extends BladeController {

    @Autowired
    private EbsService ebsService;
    @Autowired
    private WbsService wbsService;
    @Autowired
    private WbsObjService wbsObjService;
    @Autowired
    private MetaService metaService;
    @Autowired
    private LibraryService libraryService;
    @Autowired
    private ElementWbsService elementWbsService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private RevitParameterService revitParameterService;
    @Autowired
    private LibWbsTempServiceImpl libWbsTempService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ExpInstancesService expInstancesService;
    @Autowired
    private ElementParameterService elementParameterService;
    @Autowired
    private RevitRefService revitRefService;
    @Autowired
    private LibraryTagService libraryTagService;
    @Autowired
    private IUsersService usersService;


//    @SneakyThrows
//    @PostMapping("/linkWbsObj.do")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "linkWbsObj", notes = "linkWbsObj")
//    public R<Boolean> linkWbsObj(@RequestParam String wbsId,@RequestParam String objectId,@RequestParam String modifyUser) {
//        try {
//            WbsObj wbsObj = new WbsObj();
//            wbsObj.setWbsId(wbsId);
//            wbsObj.setObjectId(objectId);
//            wbsObj.setModifyUser(modifyUser);
//            wbsObj.setStatus(0);
//            boolean b = wbsObjService.addWbsObj(wbsObj);
//            R.success("WBS绑定模型成功!");
//            return R.status(b);
//        } catch (Exception e) {
//            return R.fail("WBS绑定模型失败！ " + e.getMessage());
//        }
//    }
//
//    @SneakyThrows
//    @PostMapping("/unlinkWbsObj.do")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "unlinkWbsObj", notes = "unlinkWbsObj")
//    public R<Boolean> unlinkWbsObj(@RequestParam String wbsId,@RequestParam String objectId,@RequestParam String modifyUser) {
//        try {
//            WbsObj link = wbsObjService.getLink(wbsId, objectId);
//            if (link == null){
//                R.fail("该绑定关系不存在,请检查后重试！！ WBS："+wbsId+" ,object: "+objectId);
//            }
//            boolean b = wbsObjService.deleteWbsObj(link.getId(),modifyUser);
//            R.success("WBS解绑模型成功!");
//            return R.status(b);
//        } catch (Exception e) {
//            return R.fail("WBS解绑模型失败！ " + e.getMessage());
//        }
//    }


//    --------------------Library-------------------

    @SneakyThrows
    @GetMapping("/getAllLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllLibrary", notes = "getAllLibrary")
    public R<List<Library>> getAllLibrary() {
        try {
            List<Library> allLibrary = libraryService.getAllLibrary();
            R.success("获取Library列表成功！");
            return R.data(allLibrary);
        }catch (Exception e){
            return R.fail("获取列表失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getAllLibraryFilter.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllLibraryFilter", notes = "getAllLibraryFilter")
    public R<List<Library>> getAllLibraryFilter(@RequestParam Optional<String> name,@RequestParam Optional<String> parameters,@RequestParam Optional<String> tag){
        try {
            List<Library> allLibrary = libraryService.getAllLibraryFilter(name.orElse(null), parameters.orElse(null), tag.orElse(null));
            R.success("获取Library列表成功！");
            return R.data(allLibrary);
        }catch (Exception e){
            return R.fail("获取列表失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getLibraryById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLibraryById", notes = "getLibraryById")
    public R<Library> getLibraryById(@RequestParam String id) {
        try {
            Library library = libraryService.getById(id);
            R.success("获取library成功！");
            return R.data(library);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getRevitByName.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRevitByName", notes = "getRevitByName")
    public R<LibraryRevitDTO> getRevitByName(@RequestParam String name) {
        try {
            LibraryRevitDTO library = libraryService.getRevitByName(name);
            R.success("获取library成功！");
            return R.data(library);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/filterLib.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "filterLib", notes = "filterLib")
    public R<PageLib> filterLib(@RequestBody Map<String, Object> params){
        try {
            PageLib pageLib = new PageLib();
            Integer pageSize = (Integer) params.get("pageSize");
            Integer currentPage = (Integer) params.get("currentPage");

            if (pageSize == null || pageSize == 0 || currentPage == null || currentPage == 0) {
                return R.fail("分页数据获取失败");
            }
            Page<Library> libraries = libraryService.filterLibrary(params, pageSize, currentPage);
            pageLib.setPageLib(libraries);
            pageLib.setTotal(libraries.getTotal());
            return R.data(pageLib);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }
    //    --------模板下载----------
    @SneakyThrows
    @GetMapping("/exportLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "exportLibrary", notes = "导出属性字典Excel")
    public void exportLibrary(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "dataType", required = false) String dataType,
            @RequestParam(value = "group", required = false) String group,
            HttpServletResponse response) throws IOException {
        try {
            // 根据参数筛选Library列表
            List<Library> libraryList = libraryService.getLibraryForExport(name, category, dataType, group);
            if (libraryList == null) {
                libraryList = new ArrayList<>();
            }
            
            // 将Library实体转换为LiberaryExcelDTO对象，保持与uploadLibrary一致的字段映射
            List<LiberaryExcelDTO> excelData = libraryList.stream()
                    .map(library -> {
                        LiberaryExcelDTO dto = new LiberaryExcelDTO();
                        dto.setName(library.getName());
                        dto.setUnits(library.getUnits());
                        dto.setCategory(library.getCategory());
                        dto.setParameterType(library.getParameterType());
                        dto.setDataType(library.getDataType());
                        dto.setParameters(library.getParameters());
                        dto.setDetail(library.getDetail());
                        dto.setType(library.getType());
                        dto.setLod(library.getLod());
                        dto.setRevitParameters(library.getRevitParameters());
                        return dto;
                    })
                    .collect(Collectors.toList());
            
            // 设置响应头，支持文件流下载
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            
            // 生成文件名，与导入模板格式一致
            String fileName = "用户属性_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName);
            
            // 使用EasyExcel写入数据，与uploadLibrary使用的DTO保持一致
            EasyExcel.write(response.getOutputStream(), LiberaryExcelDTO.class)
                    .sheet("用户属性")
                    .doWrite(excelData);
                    
        } catch (Exception e) {
            // 如果出现异常，清空response并返回错误信息
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("{\"success\":false,\"message\":\"导出失败：" + e.getMessage() + "\"}");
        }
    }

    @SneakyThrows
    @PostMapping("/addLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addLibrary", notes = "addLibrary")
    public R<String> addLibrary(@RequestHeader("Authorization") String token,@RequestBody Library library) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }
            // 校验属性名是否已存在
            if (libraryService.checkLibraryNameExists(library.getName())) {
                return R.fail("属性名已存在");
            }
            library.setModifyUser(user.getName());
            library.setSource("user");
            boolean res = libraryService.addLibrary(library);
            if (res){
                R.success("新增Library成功！");
            }
            return R.data(library.getId());
        }catch (Exception e){
            return R.fail("新增Library失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addLibrarys.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addLibrarys", notes = "addLibrarys")
    public R<String> addLibrarys(@RequestHeader("Authorization") String token,@RequestBody List<Library> librarys) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }
            boolean res = false;
            List<String> ids = new ArrayList<>();
            for (Library library : librarys) {
                // 校验属性名是否已存在
                if (libraryService.checkLibraryNameExists(library.getName())) {
                    return R.fail("属性名已存在: " + library.getName());
                }
                 library.setModifyUser(user.getName());
                 library.setSource("user");
                 res = libraryService.addLibrary(library);
                 ids.add(library.getId());
            }
            if (res){
                R.success("新增Library成功！");
            }
            return R.data(ids.toString());
        }catch (Exception e){
            return R.fail("新增Library失败！ "+e.getMessage());
        }
    }

//    预置属性资源接口
    @SneakyThrows
    @PostMapping("/addStaticLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addStaticLibrary", notes = "addStaticLibrary")
    public R<String> addStaticLibrary(@RequestHeader("Authorization") String token,@RequestBody Library library) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }
            library.setModifyUser(user.getName());
            library.setSource("SMARTLINK");
            boolean res = libraryService.addLibrary(library);
            if (res){
                R.success("新增预置Library成功");
            }
            return R.data(library.getId());
        }catch (Exception e){
            return R.fail("新增预置Library失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateLibrary", notes = "updateLibrary")
    public R<Boolean> updateLibrary(@RequestBody Library library) {
        try {
            boolean res = libraryService.updateLibrary(library);
            if (res){
                R.success("修改Library成功！");
            }
            return R.data(res);
        }catch (Exception e){
            return R.fail("修改Library失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateLibrarys.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateLibrarys", notes = "updateLibrarys")
    public R<String> updateLibrarys(@RequestBody LibraryVO libraryVO) {
        try {
            boolean b = false;
            String res = "";
            String[] ids = libraryVO.getIds();
            String key = libraryVO.getKey();
            String value = libraryVO.getValue();
            for (String id :  ids) {
                Library library = new Library();
                if (key.equals("name")){
                   library.setName(value);
                }
                if (key.equals("units")){
                    library.setUnits(value);
                }
                if (key.equals("category")){
                    library.setCategory(value);
                }
                if (key.equals("parameterType")){
                    library.setParameterType(value);
                }
                if (key.equals("dataType")){
                    library.setDataType(value);
                }
                if (key.equals("dataTypeEn")){
                    library.setDataTypeEn(value);
                }
                if (key.equals("parameters")){
                    library.setParameters(value);
                }
                if (key.equals("detail")){
                    library.setDetail(value);
                }
                if (key.equals("type")){
                    library.setType(value);
                }
                if (key.equals("lod")){
                    library.setLod(value);
                }
                if (key.equals("revitParameters")){
                    library.setRevitParameters(value);
                }
                if (key.equals("revitParametersEn")){
                    library.setRevitParametersEn(value);
                }
                library.setId(id);
                b = libraryService.updateLibrary(library);
                res += id + " ";
            }
            if (b){
                return R.data("批量修改Library成功！ "+res);
            }else {
                return R.fail("批量修改Library失败！"+res);
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("修改Library失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteLibrary", notes = "deleteLibrary")
    public R<Boolean> deleteLibrary(@RequestParam String id) {
        try {
            boolean res = libraryService.deleteLibrary(id);
            if (res){
                R.success("删除Library成功！");
            }
            return R.data(res);
        }catch (Exception e){
            return R.fail("删除Library失败！ "+e.getMessage());
        }
    }

//    -----------------ElementWbs-----------------

    @SneakyThrows
    @GetMapping("/getAllElementWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllElementWbs", notes = "getAllElementWbs")
    public R<List<ElementWbs>> getAllElementWbs(@RequestParam String branchId,@RequestParam String tempId){
        try {
            List<ElementWbs> allElementWbs = elementWbsService.getAllElementWbs(branchId, tempId);
            R.success("获取ElementWbs成功！");
            return R.data(allElementWbs);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getElementWbsById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getElementWbsById", notes = "getElementWbsById")
    public R<ElementWbs> getElementWbsById(@RequestParam String id) {
        try {
            ElementWbs elementWbs = elementWbsService.getById(id);
            R.success("获取ElementWbs成功！");
            return R.data(elementWbs);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getElementWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getElementWbs", notes = "getElementWbs")
    public R<ElementWbs> getElementWbs(@RequestParam String wbsCode,@RequestParam String branchId,@RequestParam String tempId) {
        try {
            ElementWbs elementWbs = elementWbsService.getElementWbs(wbsCode,branchId,tempId);
            R.success("获取ElementWbs成功！");
            return R.data(elementWbs);
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/linkElementWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "linkElementWbs", notes = "linkElementWbs")
    public R<Boolean> linkElementWbs(@RequestParam String wbsCode,@RequestParam String[] elementId,@RequestParam String branchId,@RequestParam String tempId) {
        try {
            ElementWbs elementWbs = elementWbsService.getElementWbs(wbsCode, branchId, tempId);
            if (elementWbs == null){
                ElementWbs wbs = new ElementWbs();
                wbs.setStatus(0);
                wbs.setWbsCode(wbsCode);
                wbs.setElementId(elementId);
                wbs.setBranchId(branchId);
                wbs.setTempId(tempId);
                boolean res = elementWbsService.linkElementWbs(wbs);
                if (res){
                    R.success("新建绑定关系成功！");
                }
                return R.data(res);
            }else {
                String[] oldelementId = elementWbs.getElementId();
                // 转换为Set以去重，并避免重复元素
                Set<String> uniqueSet = new HashSet<>();

                // 将oldelementId的元素加入Set中
                for (String id : oldelementId) {
                    uniqueSet.add(id);
                }

                // 只将elementId中不在Set中的元素加入Set
                for (String id : elementId) {
                    uniqueSet.add(id);
                }

                // 转换回数组
                String[] newelementId = uniqueSet.toArray(new String[0]);
                boolean b = elementWbsService.updateElementWbs(elementWbs.getId(), newelementId);
                if (b){
                    R.success("更新绑定关系成功！");
                }
                return R.data(b);
            }
        }catch (Exception e){
            return R.fail("绑定失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/unlinkElementWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "unlinkElementWbs", notes = "unlinkElementWbs")
    public R<Boolean> unlinkElementWbs(@RequestParam String wbsCode,@RequestParam String[] elementId,@RequestParam String branchId,@RequestParam String tempId) {
        try {
            ElementWbs elementWbs = elementWbsService.getElementWbs(wbsCode, branchId, tempId);
            String[] elementIds = elementWbs.getElementId();
            if ( Arrays.toString((elementIds)).equals("[[]]")){
                elementId = new String[0];
            }
            String[] newelementId = ArrayUtils.removeElements(elementIds, elementId);
            boolean res = elementWbsService.updateElementWbs(elementWbs.getId(), newelementId);
            if (res){
                R.success("解绑成功！");
            }
            return R.data(res);
        }catch (Exception e){
            return R.fail("解绑失败！ "+e.getMessage());
        }
    }

//    @SneakyThrows
//    @PostMapping("/updateElementWbs.do")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "updateElementWbs", notes = "updateElementWbs")
//    public R<Boolean> updateElementWbs(@RequestParam String id,@RequestParam String[] elementId) {
//        try {
//            boolean res = elementWbsService.updateElementWbs(id,elementId);
//            if (res){
//                R.success("绑定成功！");
//            }
//            return R.data(res);
//        }catch (Exception e){
//            return R.fail("绑定失败！ "+e.getMessage());
//        }
//    }

//    -------------------Excel批量导入属性字典-------------------

    @SneakyThrows
    @PostMapping("/uploadLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadLibrary", notes = "uploadLibrary")
    public  R<String> uploadLibrary(@RequestParam("file") MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            List<LiberaryExcelDTO> dataList = EasyExcelReadUtils.readLiberaryExcel(inputStream);

            // 将 DTO 对象列表转换为 Library 实体类列表
            List<Library> libraryList = dataList.stream()
                    .map(dto -> {
                        Library library = new Library(
                                dto.getName(),
                                dto.getUnits(),
                                "1.0",
                                dto.getCategory(),
                                dto.getParameterType(),
                                dto.getDataType(),
                                "",
                                dto.getParameters(),
                                dto.getDetail(),
                                dto.getType(),
                                dto.getLod(),
                                dto.getRevitParameters(),
                                "",
                                new String[]{},
                                "admin",
                                new Date(),
                                0,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "user"
                        );
                        library.setId(UUID.randomUUID().toString());
                        libraryService.addLibrary(library);
                        return library;
                    })
                    .collect(Collectors.toList());
            // 批量插入数据
//            libraryService.insertBatchSomeColumn(libraryList);

            return R.data("导入属性成功，一共: "+libraryList.size()+" 条!");
        } catch (IOException e) {
            // 处理异常，例如记录日志或返回错误信息
            // log.error("Failed to upload file", e);
            return R.fail("导入属性失败！ "+e.getMessage());
        }
    }

//    -----------excel批量预置属性---------------

    @SneakyThrows
    @PostMapping("/uploadStaticLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadStaticLibrary", notes = "uploadStaticLibrary")
    public  R<Boolean> uploadStaticLibrary(@RequestParam("file") MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            List<LiberaryExcelDTO> dataList = EasyExcelReadUtils.readLiberaryExcel(inputStream);

            // 将 DTO 对象列表转换为 Library 实体类列表
            List<Library> libraryList = dataList.stream()
                    .map(dto -> {
                        Library library = new Library(
                                dto.getName(),
                                dto.getUnits(),
                                "1.0",
                                dto.getCategory(),
                                dto.getParameterType(),
                                dto.getDataType(),
                                "",
                                dto.getParameters(),
                                dto.getDetail(),
                                dto.getType(),
                                dto.getLod(),
                                dto.getRevitParameters(),
                                "",
                                new String[]{},
                                "admin",
                                new Date(),
                                0,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "SMARTLINK"
                        );
                        library.setId(UUID.randomUUID().toString());
                        libraryService.addLibrary(library);
                        return library;
                    })
                    .collect(Collectors.toList());
            // 批量插入数据
//            libraryService.insertBatchSomeColumn(libraryList);

            return R.success("success");
        } catch (IOException e) {
            // 处理异常，例如记录日志或返回错误信息
            // log.error("Failed to upload file", e);
            return R.fail("failed"+e.getMessage());
        }
    }

    /**
     * 搜索resourceID
     * @param branchId
     * @return
     */
    @SneakyThrows
    @GetMapping("/searchResouceId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "searchResouceId", notes = "searchResouceId")
    public R<String> searchResouceId(@RequestParam String branchId){
        try {
            String commitsId = branchService.getLatestCommitsId(branchId);
            return R.data(commitsId);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//---------------属性字典/WBS/模板绑定--------------------

    /**
     * 获取关联的属性字典列表
     * @param wbsCode
     * @param tempId
     * @return
     */
    @SneakyThrows
    @GetMapping("/getLibrarysByWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLibrarysByWbs", notes = "getLibrarysByWbs")
    public R<List<Library>> getLibrarysByWbs(@RequestParam String wbsCode,@RequestParam String tempId){
        try {
            List<Library> libraries = new ArrayList<>();
            LibWbsTemp libWbsTemp = libWbsTempService.getLibWbsTemp(wbsCode, tempId);
            if (libWbsTemp == null){
                return R.data(libraries);
            }else {
                String[] libIds = libWbsTemp.getLibIds();
                for (String libId : libIds) {
                    Library library = libraryService.getById(libId);
                    libraries.add(library);
                }
                return R.data(libraries);
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addLibrary2.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addLibrary2", notes = "addLibrary2")
    public R<String> addLibrary2(@RequestBody LibWbsTempDTO libWbsTempDTO){
        try {
            Library library = libWbsTempDTO.getLibrarys().get(0);
            // 校验属性名是否已存在
            if (libraryService.checkLibraryNameExists(library.getName())) {
                return R.fail("属性名已存在");
            }
            boolean b = libraryService.addLibrary(library);
            if (b){
                String[] libIds = library.getId().split(",");
                boolean res = false;
                LibWbsTemp libWbsTemp = libWbsTempService.getLibWbsTemp(libWbsTempDTO.getWbsCode(), libWbsTempDTO.getTempId());
                if (libWbsTemp == null){
                    res = libWbsTempService.addLibWbsTemp(new LibWbsTemp(libWbsTempDTO.getWbsCode(), libWbsTempDTO.getTempId(), libIds, new String[]{}, 0));
                }else {
                    List<String> libIdsList = new ArrayList<>(Arrays.asList(libWbsTemp.getLibIds()));
                    libIdsList.add(library.getId());
                    String[] newLibIds = libIdsList.toArray(new String[0]);
                    libWbsTemp.setLibIds(newLibIds);
                    res = libWbsTempService.updateLibWbsTemp(libWbsTemp);
                }
                if (res){
                    return R.data(library.getId());
                }else {
                    R.fail("绑定wbs失败");
                }
            }
            return R.data("用户字典添加失败");
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    /**
     * 获取系统专业参数的字典
     * @return
     */
    @SneakyThrows
    @GetMapping("/getLibrarysParams.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLibrarysParams", notes = "getLibrarysParams")
    public R<List<Library>> getLibrarysParams(){
        try {
            List<Library> libraries = libraryService.getLibrarysParams();
            return R.data(libraries);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }



//    LibWbsTemp libWbsTemp = libWbsTempService.getLibWbsTemp(wbsCode, tempId, library.getId());
//                if (libWbsTemp != null){
//        libWbsTempService.updateLibWbsTemp(libWbsTemp);
//        libIds.add(library.getId());
//    }else {
//        boolean b = libWbsTempService.addLibWbsTemp(new LibWbsTemp(UUID.randomUUID().toString(), wbsCode, tempId, library.getId(), 0));
//        if (b){
//            libIds.add(library.getId());
//        }else {
//            return R.fail("通过wbs添加属性出错！");
//        }
//    }

    @SneakyThrows
    @PostMapping("/deleteLibrarysByWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteLibrarysByWbs", notes = "deleteLibrarysByWbs")
    public R<Boolean> deleteLibrarysByWbs(@RequestParam String libId){
        try {
            boolean res = false;
            boolean b = libraryService.deleteLibrary(libId);
            boolean b1 = libWbsTempService.deleteLibWbsTemp(libId);
            if (b && b1){
                res = true;
                return R.data(res);
            }
            return R.fail("删除失败");
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    ------------批量操作属性字典---------------

    @SneakyThrows
    @PostMapping("/updateLibActivity.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateLibActivity", notes = "updateLibActivity")
    public R<String> updateLibActivity(@RequestBody Librarys librarys){
        try {
            boolean b = libraryService.updateLibraryByKey(librarys);
            String res = "";
            for (String libId : librarys.getLibIds()) {
                res += libId + ",";
            }
            if (b){
                return R.data("以下属性字典:"+res+"更新成功！更新内容为:"+librarys.getKey()+"-"+librarys.getValue());
            }else {
                return R.fail("以下属性字典:"+res+"更新失败！更新内容为:"+librarys.getKey()+"-"+librarys.getValue());
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteLibrariesByIds.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteLibrariesByIds", notes = "deleteLibrariesByIds")
    public R<String> deleteLibrariesByIds(@RequestParam String[] ids){
        try {
            List<String> libIds = Arrays.asList(ids);
            boolean b = libraryService.deleteLibrariesByIds(libIds);
            if (b){
                return R.data("以下属性字典:"+libIds+"删除成功！");
            }else {
                return R.fail("以下属性字典:"+libIds+"删除失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    --------------属性集Tag-------------------

    @SneakyThrows
    @GetMapping("/getLibraryTag.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLibraryTag", notes = "getLibraryTag")
    public R<LibraryTag> getLibraryTag(@RequestParam String id){
        try {
            LibraryTag libraryTag = libraryTagService.getById(id);
            return R.data(libraryTag);
        }catch (Exception e){
            return null;
        }
    }

    @SneakyThrows
    @GetMapping("/getLibraryTagByName.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLibraryTagByName", notes = "getLibraryTagByName")
    public R<LibraryTag> getLibraryTagByName(@RequestParam String tag){
        try {
            LibraryTag libraryTag = libraryTagService.getByName(tag);
            return R.data(libraryTag);
        }catch (Exception e){
            return null;
        }
    }

    @SneakyThrows
    @GetMapping("/getAllLibraryTag.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllLibraryTag", notes = "getAllLibraryTag")
    public R<List<LibraryTag>> getAllLibraryTag(){
        try {
            List<LibraryTag> libraryTags = libraryTagService.getAllLibraryTag();
            return R.data(libraryTags);
        }catch (Exception e){
            return null;
        }
    }


    @SneakyThrows
    @PostMapping("/addLibraryTag.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addLibraryTag", notes = "addLibraryTag")
    public R<Boolean> addLibraryTag(@RequestBody LibraryTag libraryTag){
        try {
            boolean b = libraryTagService.addLibraryTag(libraryTag);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }


    @SneakyThrows
    @PostMapping("/updateLibraryTag.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateLibraryTag", notes = "updateLibraryTag")
    public R<Boolean> updateLibraryTag(@RequestBody LibraryTag libraryTag){
        try {
            boolean b = libraryTagService.updateLibraryTag(libraryTag);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteLibraryTag.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteLibraryTag", notes = "deleteLibraryTag")
    public R<Boolean> deleteLibraryTag(@RequestParam String id){
        try {
            boolean b = libraryTagService.deleteLibraryTag(id);
            return R.data(b);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    ------------------绑定属性集------------------

    @SneakyThrows
    @GetMapping("/getLibrarysByTag.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLibrarysByTag", notes = "getLibrarysByTag")
    public R<List<Library>> getLibrarysByTag(@RequestParam String tag){
        try {
            List<Library> librarys = libraryService.getLibrarysByTag(tag);
            return R.data(librarys);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/linkTag1V1.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "linkTag1V1", notes = "linkTag1V1")
    public R<String> linkTag1V1(@RequestParam String libId, @RequestParam String tag){
        try {
            List<String> tags = new ArrayList<>();
            Library lib = libraryService.getById(libId);
            String[] libtag = lib.getTag();
            if (libtag == null){
                tags.add(tag);
                lib.setTag(tags.toArray(new String[0]));
            }else {
                tags = new ArrayList<>(Arrays.asList(libtag));
                tags.add(tag);
                lib.setTag(tags.toArray(new String[0]));
            }
            boolean b = libraryService.updateLibrary(lib);
            if (b){
                return R.data("属性集"+tag+"绑定"+lib.getName()+"成功");
            }else {
                return R.data("属性集"+tag+"绑定"+lib.getName()+"失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/unlinkTag1V1.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "unlinkTag1V1", notes = "unlinkTag1V1")
    public R<String> unlinkTag1V1(@RequestParam String libId, @RequestParam String tag){
        try {
            List<String> tags = new ArrayList<>();
            Library lib = libraryService.getById(libId);
            String[] libtag = lib.getTag();
            if (libtag == null){
                lib.setTag(new String[0]);
            }else {
                tags = new ArrayList<>(Arrays.asList(libtag));
                tags.remove(tag);
                lib.setTag(tags.toArray(new String[0]));
            }
                boolean b = libraryService.updateLibrary(lib);
                if (b){
                    return R.data("属性集"+tag+"解绑"+lib.getName()+"成功");
                }else {
                    return R.data("属性集"+tag+"解绑"+lib.getName()+"失败");
                }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }



    @SneakyThrows
    @PostMapping("/linkTag.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "linkTag", notes = "linkTag")
    public R<String> linkTag(@RequestBody LibTagVO libTagVO){
        try {
            String[] ids = libTagVO.getIds();
            LibraryTag libraryTag = new LibraryTag();
            libraryTag.setId(UUID.randomUUID().toString());
            libraryTag.setTag(libTagVO.getTag());
            libraryTag.setTagColor(libTagVO.getTagColor());
            libraryTagService.addLibraryTag(libraryTag);
            for (String id : ids) {
                List<String> tags = new ArrayList<>();
                Library lib = libraryService.getById(id);
                String[] tag = lib.getTag();
                if (tag == null){
                    tags.add(libTagVO.getTag());
                    lib.setTag(tags.toArray(new String[0]));
                }else {
                    tags = new ArrayList<>(Arrays.asList(tag));
                    tags.add(libTagVO.getTag());
                    lib.setTag(tags.toArray(new String[0]));
                }
                libraryService.updateLibrary(lib);
            }
            return R.data("添加属性集成功！+新增属性集:"+libTagVO.getTag());
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/unlinkTag.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "unlinkTag", notes = "unlinkTag")
    public R<String> unlinkTag(@RequestBody LibTagVO libTagVO){
        try {
            String[] ids = libTagVO.getIds();
            for (String id : ids) {
                List<String> tags = new ArrayList<>();
                Library lib = libraryService.getById(id);
                String[] tag = lib.getTag();
                if (tag == null){
                    lib.setTag(new String[0]);
                }else {
                    tags = new ArrayList<>(Arrays.asList(tag));
                    tags.remove(libTagVO.getTag());
                    lib.setTag(tags.toArray(new String[0]));
                }
                libraryService.updateLibrary(lib);
            }
            if (getLibrarysByTag(libTagVO.getTag()).getData().size() == 0){
                LibraryTag byName = libraryTagService.getByName(libTagVO.getTag());
                libraryTagService.deleteLibraryTag(byName.getId());
                return R.data("当前属性集"+libTagVO.getTag()+"下未有属性绑定，已删除!");
            }
            return R.data("解绑属性集成功！+属性集:"+libTagVO.getTag());
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }



//    --------------------------通过.txt文件导入revit参数----------------------------

    @SneakyThrows
    @PostMapping("/uploadrevitParametersByTxt.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadrevitParametersByTxt", notes = "uploadrevitParametersByTxt")
    public R<List<RevitParameter>> uploadrevitParametersByTxt(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            List<RevitParameter> revitParameters = fileService.parseFile1(file);
            //遍历sharedParameters，创建新Library并将遍历sharedParameters中数据写入Library
            return R.data(revitParameters);
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }


//    ----------------预置属性字典--------------------

    @SneakyThrows
    @PostMapping("/addRevitRefs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addRevitRefs", notes = "addRevitRefs")
    public R<Boolean> addRevitRefs(@RequestBody List<RevitRef> revitRefs){
        try {
            boolean b = false;
            if (revitRefs == null || revitRefs.size() == 0){
                return R.data(false);
            }
            for (RevitRef revitRef : revitRefs) {
                b = revitRefService.addRevitRef(revitRef);
            }
            return R.data(b);
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getRevitRefsByGroup.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getRevitRefsByGroup", notes = "getRevitRefsByGroup")
    public R<List<RevitRef>> getRevitRefsByGroup(@RequestParam String group){
        try {
            List<RevitRef> revitRefs = revitRefService.getAllByGroup(group);
            return R.data(revitRefs);
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }

//    ---------------------wbs/element/parameter联合接口----------------------

    /**
     * 通过WBS和模版页面新增属性(用户属性librarys和模型属性parameters)
     * @param
     * @return
     */
    @SneakyThrows
    @PostMapping("/link.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "link", notes = "link")
    public R<Boolean> link(@RequestBody AllLibDTO allLibDTO) {
        try {
            String wbsCode = allLibDTO.getWbsCode();
            String tempId = allLibDTO.getTempId();
            List<Library> librarys = allLibDTO.getLibrarys();
            String[] libIds = new String[]{};
            for (Library library : librarys) {
                Library byId = libraryService.getById(library.getId());
                if (byId == null) {
                    libraryService.addLibrary(library);
                }else {
                    libraryService.updateLibrary(library);
                }
                libIds = ArrayUtils.add(libIds, library.getId());
            }
            List<ElementParameter> parameters = allLibDTO.getParameters();
            String[] elementParameterIds = new String[]{};
            for (ElementParameter elementParameter : parameters) {
                ElementParameter byId = elementParameterService.getById(elementParameter.getId());
                if (byId == null) {
                    elementParameterService.insert(elementParameter);
                }else {
                    elementParameterService.updateById(elementParameter);
                }
                elementParameterIds = ArrayUtils.add(elementParameterIds, elementParameter.getId());
            }
            LibWbsTemp libWbsTemp = libWbsTempService.getLibWbsTemp(wbsCode, tempId);
            if (libWbsTemp != null) {
                libWbsTemp.setLibIds(libIds);
                libWbsTemp.setParameterIds(elementParameterIds);
                libWbsTempService.updateLibWbsTemp(libWbsTemp);
            } else {
                boolean b = libWbsTempService.addLibWbsTemp(new LibWbsTemp(wbsCode, tempId, libIds,elementParameterIds, 0));
            }
            return R.data(true, "关联成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    /**
     * 通过WBS和模版页面新增属性(用户属性librarys和模型属性parameters)
     * @param
     * @return
     */
    @SneakyThrows
    @PostMapping("/links.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "links", notes = "links")
    public R<Boolean> links(@RequestBody AllLibListDTO allLibDTO) {
        try {
            String[] wbsCodes = allLibDTO.getWbsCodes();
            String tempId = allLibDTO.getTempId();
            List<Library> librarys = allLibDTO.getLibrarys();
            String[] libIds = new String[]{};
            for (Library library : librarys) {
                Library byId = libraryService.getById(library.getId());
                if (byId == null) {
                    libraryService.addLibrary(library);
                }else {
                    libraryService.updateLibrary(library);
                }
                libIds = ArrayUtils.add(libIds, library.getId());
            }
            List<ElementParameter> parameters = allLibDTO.getParameters();
            String[] elementParameterIds = new String[]{};
            for (ElementParameter elementParameter : parameters) {
                ElementParameter byId = elementParameterService.getById(elementParameter.getId());
                if (byId == null) {
                    elementParameterService.insert(elementParameter);
                }else {
                    elementParameterService.updateById(elementParameter);
                }
                elementParameterIds = ArrayUtils.add(elementParameterIds, elementParameter.getId());
            }
            for (String wbsCode : wbsCodes) {
                LibWbsTemp libWbsTemp = libWbsTempService.getLibWbsTemp(wbsCode, tempId);
                if (libWbsTemp != null) {
                    libWbsTemp.setLibIds(libIds);
                    libWbsTemp.setParameterIds(elementParameterIds);
                    libWbsTempService.updateLibWbsTemp(libWbsTemp);
                } else {
                    boolean b = libWbsTempService.addLibWbsTemp(new LibWbsTemp(wbsCode, tempId, libIds,elementParameterIds, 0));
                }
            }
            return R.data(true, "关联成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }


    @SneakyThrows
    @GetMapping("/getlink.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getlink", notes = "getlink")
    public R<AllLibDTO> getlink(@RequestParam String wbsCode, @RequestParam String tempId){
        try {
            AllLibDTO allLibDTO = new AllLibDTO();
            List<Library> librarys = new ArrayList<>();
            List<ElementParameter> parameters = new ArrayList<>();
            LibWbsTemp libWbsTemp = libWbsTempService.getLibWbsTemp(wbsCode, tempId);
            if (ObjectUtils.isEmpty(libWbsTemp) || libWbsTemp == null){
                return R.fail("无绑定关系!");
            }
            if (ObjectUtils.isNotEmpty(libWbsTemp.getLibIds())){
                for (String libId : libWbsTemp.getLibIds()) {
                    Library byId = libraryService.getById(libId);
                    librarys.add(byId);
                }
            }
            if (ObjectUtils.isNotEmpty(libWbsTemp.getParameterIds())){
                for (String pa : libWbsTemp.getParameterIds()) {
                    ElementParameter elementParameter = elementParameterService.getById(pa);
                    parameters.add(elementParameter);
                }
            }
            allLibDTO.setLibrarys(librarys);
            allLibDTO.setParameters(parameters);
            return R.data(allLibDTO,"该模板wbs下已绑定数据");
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/getlinks.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getlinks", notes = "getlinks")
    public R<List<AllLibDTO>> getlinks(@RequestBody LinkVO linkVO) {
        try {
            List<AllLibDTO> allLibDTOS = new ArrayList<>();
            String[] wbsCodes = linkVO.getWbsCodes();
            String tempId = linkVO.getTempId();
            for (String wbsCode : wbsCodes) {
                AllLibDTO allLibDTO = new AllLibDTO();
                List<Library> librarys = new ArrayList<>();
                List<ElementParameter> parameters = new ArrayList<>();
                LibWbsTemp libWbsTemp = libWbsTempService.getLibWbsTemp(wbsCode, tempId);
                if (ObjectUtils.isEmpty(libWbsTemp) || libWbsTemp == null) {
                    continue;
                }
                if (ObjectUtils.isNotEmpty(libWbsTemp.getLibIds())) {
                    for (String libId : libWbsTemp.getLibIds()) {
                        Library byId = libraryService.getById(libId);
                        librarys.add(byId);
                    }
                }
                if (ObjectUtils.isNotEmpty(libWbsTemp.getParameterIds())) {
                    for (String pa : libWbsTemp.getParameterIds()) {
                        ElementParameter elementParameter = elementParameterService.getById(pa);
                        parameters.add(elementParameter);
                    }
                }
                allLibDTO.setWbsCode(wbsCode);
                allLibDTO.setTempId(tempId);
                allLibDTO.setLibrarys(librarys);
                allLibDTO.setParameters(parameters);
                allLibDTOS.add(allLibDTO);
            }
            return R.data(allLibDTOS,"该模板wbs下已绑定数据");
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/getLinkList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLinkList", notes = "getLinkList")
    public R<List<LIbAndParametersDTO>> getLinkList(@RequestBody LinkVO linkVO) {
        try {
            List<LIbAndParametersDTO> allLibDTOS = new ArrayList<>();
            String[] wbsCodes = linkVO.getWbsCodes();
            String tempId = linkVO.getTempId();

            // 初始化交集集合
            Set<String> commonLibIds = null;
            Map<String, ElementParameter> commonParameters = null;

            for (String wbsCode : wbsCodes) {
                LibWbsTemp libWbsTemp = libWbsTempService.getLibWbsTemp(wbsCode, tempId);
                if (ObjectUtils.isEmpty(libWbsTemp)) {
                    return R.data(new ArrayList<>(), "无绑定关系!");
                }

                // 处理 libIds 交集
                if (ObjectUtils.isNotEmpty(libWbsTemp.getLibIds())) {
                    Set<String> currentLibIds = new HashSet<>(Arrays.asList(libWbsTemp.getLibIds()));
                    if (commonLibIds == null) {
                        commonLibIds = currentLibIds;
                    } else {
                        commonLibIds.retainAll(currentLibIds);
                    }
                } else {
                    commonLibIds = null;
                }

                // 获取当前 WBS 的 parameters
                if (ObjectUtils.isNotEmpty(libWbsTemp.getParameterIds())) {
                    List<ElementParameter> currentParameters = elementParameterService.getByIds(Arrays.asList(libWbsTemp.getParameterIds()));

                    // 构造当前 WBS 的 name+category 映射
                    Map<String, ElementParameter> currentParameterMap = currentParameters.stream()
                            .collect(Collectors.toMap(
                                    p -> p.getName() + "|" + p.getCategory(), // 联合键
                                    p -> p,                                  // 值为当前对象
                                    (existing, replacement) -> existing      // 如果有重复，保留已有的
                            ));

                    // 处理 parameters 的交集
                    if (commonParameters == null) {
                        commonParameters = new HashMap<>(currentParameterMap);
                    } else {
                        commonParameters.keySet().retainAll(currentParameterMap.keySet());
                    }
                } else {
                  commonParameters = null;
                }
            }

            // 构造结果 DTO
            LIbAndParametersDTO allLibDTO = new LIbAndParametersDTO();

            // 处理 Librarys 结果
            if (commonLibIds != null && !commonLibIds.isEmpty()) {
                List<Library> librarys = libraryService.getByIds(new ArrayList<>(commonLibIds));
                allLibDTO.setLibrarys(librarys);
            } else {
                allLibDTO.setLibrarys(Collections.emptyList());
            }

            // 处理 Parameters 结果
            if (commonParameters != null && !commonParameters.isEmpty()) {
                List<ElementParameter> parameters = new ArrayList<>(commonParameters.values());
                allLibDTO.setParameters(parameters);
            } else {
                allLibDTO.setParameters(Collections.emptyList());
            }

            allLibDTOS.add(allLibDTO);

            return R.data(allLibDTOS, "处理完成");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getParameterByWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getParameterByWbs", notes = "getParameterByWbs")
    public R<List<ElementParameter>> getParameterByWbs(@RequestParam String wbsCode, @RequestParam String branchId, @RequestParam String tempId,@RequestParam String name, @RequestParam String category) {
        try {
            List<ElementParameter> res = new ArrayList<>();
            ElementWbs elementWbs = elementWbsService.getElementWbs(wbsCode, branchId, tempId);
            if (ObjectUtils.isEmpty(elementWbs) || elementWbs == null){
                return R.fail("未找到该模板下wbs和属性的关联关系！");
            }
            String[] elementIds = elementWbs.getElementId();
            for (String elementId : elementIds) {
                List<ElementParameter> elementParameters = expInstancesService.getParameterByElementId(elementId, branchId, name, category).getData();
                for (ElementParameter elementParameter : elementParameters) {
                    res.add(elementParameter);
                }
            }
            return R.data(res,"获取成功");
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/getParameterListByWbs.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getParameterListByWbs", notes = "getParameterListByWbs")
    public R<List<ElementParameter>> getParameterListByWbs(@RequestParam String[] wbsCodes, @RequestParam String branchId, @RequestParam String tempId, @RequestParam String name, @RequestParam String category) {
        try {
            // 用于存储所有 elementParameters 的交集，使用 LinkedHashMap 保证顺序
            Map<String, ElementParameter> intersection = null;

            // 遍历 wbsCodes，获取每个 WBS 的 elementParameters
            for (String wbsCode : wbsCodes) {
                ElementWbs elementWbs = elementWbsService.getElementWbs(wbsCode, branchId, tempId);
                if (ObjectUtils.isEmpty(elementWbs)) {
                    return R.fail("未找到该模板下wbs和属性的关联关系！");
                }

                String[] elementIds = elementWbs.getElementId();
                Map<String, ElementParameter> currentResult = new LinkedHashMap<>();

                // 查询当前 WBS 对应的 elementParameters
                for (String elementId : elementIds) {
                    List<ElementParameter> elementParameters = expInstancesService.getParameterByElementId(elementId, branchId, name, category).getData();
                    if (!ObjectUtils.isEmpty(elementParameters)) {
                        // 将当前查询结果以 name+category 为键存入 LinkedHashMap，保留查询顺序
                        for (ElementParameter param : elementParameters) {
                            String key = param.getName() + "|" + param.getCategory();
                            currentResult.putIfAbsent(key, param); // 如果已存在，则保留第一个出现的顺序
                        }
                    }
                }

                // 求交集
                if (intersection == null) {
                    // 第一次初始化交集集合
                    intersection = new LinkedHashMap<>(currentResult);
                } else {
                    // 保留交集
                    intersection.keySet().retainAll(currentResult.keySet());
                }

                // 如果交集为空，提前结束循环
                if (intersection.isEmpty()) {
                    break;
                }
            }

            // 将交集结果转为 List（顺序由 LinkedHashMap 保证）
            List<ElementParameter> uniqueParameters = new ArrayList<>(intersection.values());

            // 返回结果
            return R.data(uniqueParameters, "获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }


//
//    @SneakyThrows
//    @PostMapping("/linkWbsParameter.do")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "linkWbsParameter", notes = "linkWbsParameter")
//    public R<Boolean> linkWbsParameter(@RequestBody ElementParameterDTO elementParameterDTO){
//        try {
//            String wbsCode = elementParameterDTO.getWbsCode();
//            String[] elementParameterIds = new String[]{};
//            for (ElementParameter elementParameter : elementParameterDTO.getElementParameters()) {
//                elementParameterService.insert(elementParameter);
//                elementParameterIds = ArrayUtils.add(elementParameterIds, elementParameter.getId());
//            }
//            LibWbsTemp libWbsTemp = libWbsTempService.getLibWbsTemp(wbsCode, elementParameterDTO.getTempId());
//            if (ObjectUtils.isEmpty(libWbsTemp) || libWbsTemp == null){
//             libWbsTemp.setParameterIds(elementParameterIds);
//            }
//            return R.data(true,"关联成功!");
//        }catch (Exception e){
//            e.printStackTrace();
//            return R.fail(e.getMessage());
//        }
//    }

}
