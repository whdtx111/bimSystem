package org.springblade.modules.sp.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.ElementLibraryDTO;
import org.springblade.modules.sp.entity.*;
import org.springblade.modules.sp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/elementLibrary")
@Api(value = "构件库控制层", tags = "构件库控制层")
public class ElementLibraryController extends BladeController {

    @Autowired
    private ElementLibraryFileService elementLibraryFileService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ElementLibraryPropertyService elementLibraryPropertyService;
    @Autowired
    private ElementLibraryService elementLibraryService;
    @Autowired
    private IUsersService usersService;
    @Autowired
    private StreamAclService streamAclService;


//    ---------文件系统----------

    @SneakyThrows
    @PostMapping("/uploadFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadFile", notes = "uploadFile")
    public R<String> uploadFile(@RequestParam MultipartFile file,@RequestParam String pid){
        try {
            Map<String,String> map = new HashMap<>();
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            ElementLibraryFile savedFile = fileService.saveElementLibraryFile(fileName,file,pid);
            return R.data(savedFile.getId());
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateFile", notes = "updateFile")
    public R<String> updateFile(@RequestBody ElementLibraryFile elementLibraryFile){
        try {
            boolean b = elementLibraryFileService.updateElementLibraryFile(elementLibraryFile);
            if (b){
                return R.data("更新成功"+elementLibraryFile.getId());
            }else {
                return R.data("更新失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteFile", notes = "deleteFile")
    public R<String> deleteFile(@RequestParam String id) {
        try {
            boolean b = fileService.removeFile(elementLibraryFileService.getById(id).getFileUrl(),"element-library-files");
            if (b){
                elementLibraryFileService.deleteElementLibraryFile(id);
                return R.data("删除成功");
            }else {
                return R.data("删除失败");
            }
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getFile", notes = "getFile")
    public R<ElementLibraryFile> getFile(@RequestParam String id) {
        try {
            ElementLibraryFile elementLibraryFile = elementLibraryFileService.getById(id);
            return R.data(elementLibraryFile);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getFiles.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getFiles", notes = "getFiles")
    public R<List<ElementLibraryFile>> getFiles(@RequestParam String pid) {
        try {
            List<ElementLibraryFile> all = elementLibraryFileService.getAll(pid);
            return R.data(all);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    ---------属性系统----------

    @SneakyThrows
    @GetMapping("/getProperties.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getProperties", notes = "getProperties")
    public R<List<ElementLibraryProperty>> getProperties(@RequestParam String pid) {
        try {
            List<ElementLibraryProperty> all = elementLibraryPropertyService.getAll(pid);
            return R.data(all);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getProperty.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getProperty", notes = "getProperty")
    public R<ElementLibraryProperty> getProperty(@RequestParam String id) {
        try {
            ElementLibraryProperty elementLibraryProperty = elementLibraryPropertyService.getById(id);
            return R.data(elementLibraryProperty);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addProperty.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addProperty", notes = "addProperty")
    public R<String> addProperty(@RequestBody ElementLibraryProperty elementLibraryProperty){
        try {
            boolean b = elementLibraryPropertyService.addElementLibraryProperty(elementLibraryProperty);
            if (b){
                return R.data("添加成功"+elementLibraryProperty.getId());
            }else {
                return R.fail("添加失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addProperties.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addProperties", notes = "addProperties")
    public R<String> addProperties(@RequestBody List<ElementLibraryProperty> elementLibraryProperties){
        try {
            boolean b = elementLibraryPropertyService.insertElementLibraryPropertyList(elementLibraryProperties);
            if (b){
                return R.data("添加成功");
            }else {
                return R.fail("添加失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateProperty.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateProperty", notes = "updateProperty")
    public R<String> updateProperty(@RequestBody ElementLibraryProperty elementLibraryProperty){
        try {
            boolean b = elementLibraryPropertyService.updateElementLibraryProperty(elementLibraryProperty);
            if (b){
                return R.data("更新成功"+elementLibraryProperty.getId());
            }else {
                return R.data("更新失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteProperty.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteProperty", notes = "deleteProperty")
    public R<String> deleteProperty(@RequestParam String id) {
        try {
            boolean b = elementLibraryPropertyService.deleteElementLibraryProperty(id);
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

//    -------主流程----------

    @SneakyThrows
    @GetMapping("/getLibrarys.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLibrarys", notes = "getLibrarys")
    public R<List<ElementLibrary>> getLibrarys() {
        try {
            List<ElementLibrary> all = elementLibraryService.getAll();
            return R.data(all);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLibrary", notes = "getLibrary")
    public R<ElementLibrary> getLibrary(@RequestParam String id) {
        try {
            ElementLibrary elementLibrary = elementLibraryService.getById(id);
            return R.data(elementLibrary);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getElementLibraryWithFiles.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getElementLibraryWithFiles", notes = "getElementLibraryWithFiles")
    public R<ElementLibraryWithFiles> getElementLibraryWithFiles(@RequestParam String id) {
        try {
            ElementLibraryWithFiles elementLibrary = elementLibraryService.getElementLibraryWithFiles(id);
            return R.data(elementLibrary);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getElementLibraryWithFilesByProject.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getElementLibraryWithFilesByProject", notes = "getElementLibraryWithFilesByProject")
    public R<List<ElementLibraryWithFiles>> getElementLibraryWithFilesByProject(@RequestParam String streamId) {
        try {
            List<ElementLibraryWithFiles> elementLibrarys = elementLibraryService.getElementLibraryWithFilesByProject(streamId);
            return R.data(elementLibrarys);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getElementLibraryListByStreamId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getElementLibraryListByStreamId", notes = "getElementLibraryListByStreamId")
    public R<List<ElementLibraryMin>> getElementLibraryListByStreamId(@RequestParam String streamId) {
        try {
            List<ElementLibraryMin> all = elementLibraryService.getElementLibraryList(streamId);
            return R.data(all);
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/filterElementLibraryList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "filterElementLibraryList", notes = "filterElementLibraryList")
    public R<List<ElementLibraryWithFiles>> filterElementLibraryList(
            @RequestHeader("Authorization") String token,
            @RequestParam Optional<String> lod,
            @RequestParam Optional<String[]> library,
            @RequestParam Optional<String> name,
            @RequestParam Optional<String[]> tags,
            @RequestParam Optional<String> propertyList,
            @RequestParam Optional<String> system,
            @RequestParam Optional<Integer> status) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }

            // 获取用户关联的项目ID列表
            List<String> userProjects = streamAclService.getResourceIdsByUserId(user.getId());
            if (userProjects.isEmpty()) {
                return R.data(new ArrayList<>());
            }

            // 调用服务层方法进行过滤
            List<ElementLibraryWithFiles> all = elementLibraryService.filterElementLibraryList(
                    name.orElse(null),
                    tags.orElse(new String[0]),
                    userProjects.toArray(new String[0]),
                    propertyList.orElse(null),
                    system.orElse(null),
                    status.orElse(null),
                    library.orElse(new String[0]),
                    lod.orElse(null)
            );
            return R.data(all);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addLibrary", notes = "addLibrary")
    public R<String> addLibrary(@RequestBody ElementLibraryDTO elementLibraryDTO) {
        try {
            JSONObject infoEncoding = elementLibraryDTO.getInfoEncoding();

            JSONObject form = infoEncoding.getJSONObject("form");
            String id = form.getString("id");
            String name = form.getString("name");
            String version = form.getString("version");
            String auth = form.getString("auth");
            String description = form.getString("description");
            String modifyUser = form.getString("modifyUser");
            String lodnow = form.getString("lodnow");
            String loiNow = form.getString("loiNow");
            if (lodnow == null) {
                lodnow = "";
            }
            if (loiNow == null) {
                loiNow = "";
            }


            // 4. 解析数组字段
            String[] tags = parseJSONArrayToStringArray(form.getJSONArray("tags"));
            String[] project = parseJSONArrayToStringArray(form.getJSONArray("project"));
            List<JSONObject> codeNameParamsMapList = parseJSONArrayToJSONObjectList(infoEncoding.getJSONArray("codeNameParamsMapList"));
            ElementLibrary elementLibrary =new ElementLibrary(new String[]{}, name, version, auth,project,description,tags,codeNameParamsMapList,new ArrayList<>(),modifyUser,new Date(),new Date(),0,"","","",loiNow,lodnow,"private");
            // 6. 调用服务层新增
            boolean success = elementLibraryService.addElementLibrary(elementLibrary);
            if (success) {
                return R.data(elementLibrary.getId());
            } else {
                return R.fail("新增失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    /**
     * 处理文件列表并返回文件 ID 数组
     */
    private String[] processFileList(JSONObject modelFile) {
        JSONArray fileList = modelFile.getJSONArray("fileList");
        if (fileList == null || fileList.isEmpty()) {
            return new String[0];
        }

        List<String> fileIds = new ArrayList<>();
        for (Object obj : fileList) {
            if (obj instanceof LinkedHashMap) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, Object> fileMap = (LinkedHashMap<String, Object>) obj;
                JSONObject file = new JSONObject(fileMap);

                String fileId = file.getString("id");
                ElementLibraryFile elementLibraryFile = elementLibraryFileService.getById(fileId);
                if (elementLibraryFile == null) {
                    throw new IllegalArgumentException("文件 ID 不存在: " + fileId);
                }

                // 更新文件信息
                elementLibraryFile.setName(file.getString("name"));
                elementLibraryFile.setSystem(file.getString("system"));
                elementLibraryFile.setLod(file.getString("lod"));
                elementLibraryFileService.updateElementLibraryFile(elementLibraryFile);

                fileIds.add(fileId);
            } else {
                throw new IllegalArgumentException("文件列表格式不正确");
            }
        }

        return fileIds.toArray(new String[0]);
    }

    /**
     * 解析 JSONArray 为 String 数组
     */
    private String[] parseJSONArrayToStringArray(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) {
            return new String[0];
        }
        List<String> list = new ArrayList<>();
        for (Object obj : jsonArray) {
            list.add(obj.toString());
        }
        return list.toArray(new String[0]);
    }

    /**
     * 解析 JSONArray 为 JSONObject 列表
     */
    private List<JSONObject> parseJSONArrayToJSONObjectList(JSONArray jsonArray) {
        List<JSONObject> list = new ArrayList<>();
        if (jsonArray == null || jsonArray.isEmpty()) {
            return list;
        }
        for (Object obj : jsonArray) {
            if (obj instanceof LinkedHashMap) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) obj;
                list.add(new JSONObject(map));
            }
        }
        return list;
    }

    @SneakyThrows
    @PostMapping("/updateLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateLibrary", notes = "updateLibrary")
    public R<String> updateLibrary(@RequestBody ElementLibraryDTO elementLibraryDTO){
        try {
            // 1. 解析入参
            JSONObject modelFile = elementLibraryDTO.getModelFile();
            List<JSONObject> propertyMgt = elementLibraryDTO.getPropertyMgt();
            JSONObject infoEncoding = elementLibraryDTO.getInfoEncoding();

            // 2. 处理文件列表，支持 String[]
            String[] fileIds = processFileList(modelFile);

            // 3. 解析主体数据
            JSONObject form = infoEncoding.getJSONObject("form");
            String id = form.getString("id");
            String name = form.getString("name");
            String version = form.getString("version");
            String auth = form.getString("auth");
            String description = form.getString("description");
            String modifyUser = form.getString("modifyUser");

            // 4. 解析数组字段
            String[] tags = parseJSONArrayToStringArray(form.getJSONArray("tags"));
            String[] project = parseJSONArrayToStringArray(form.getJSONArray("project"));
            List<JSONObject> codeNameParamsMapList = parseJSONArrayToJSONObjectList(infoEncoding.getJSONArray("codeNameParamsMapList"));

            // 5. 更新ElementLibrary
            ElementLibrary elementLibrary = elementLibraryService.getById(id);
            elementLibrary.setName(name);
            elementLibrary.setVersion(version);
            elementLibrary.setAuth(auth);
            elementLibrary.setDescription(description);
            elementLibrary.setModifyUser(modifyUser);
            elementLibrary.setModifyTime(new Date());
            elementLibrary.setFileIds(fileIds);
            elementLibrary.setProject(project);
            elementLibrary.setTags(tags);
            elementLibrary.setCodeNameParamsMapList(codeNameParamsMapList);
            elementLibrary.setPropertyMgt(propertyMgt);
            elementLibrary.setStatus(0);
            boolean b = elementLibraryService.updateElementLibrary(elementLibrary);
            if (b){
                return R.data("更新成功"+elementLibrary.getId());
            }else {
                return R.data("更新失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteLibrary.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteLibrary", notes = "deleteLibrary")
    public R<String> deleteLibrary(@RequestParam String id){
        try {
            ElementLibrary element = elementLibraryService.getById(id);
            String[] fileIds = element.getFileIds();
            if (fileIds != null) {
                for (String fileId : fileIds) {
                    elementLibraryFileService.deleteElementLibraryFile(fileId);
                }
            }
            boolean b = elementLibraryService.deleteElementLibrary(id);
            if (b){
                return R.data("删除成功");
            }else {
                return R.data("删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }


//    -----------插件端相关接口------------

    @SneakyThrows
    @PostMapping("/updateRevitFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateRevitFile", notes = "updateRevitFile")
    public R<String> updateRevitFile(@RequestParam MultipartFile file,@RequestParam String fileId,@RequestParam String lod,@RequestParam String loi,@RequestParam String streamId,@RequestParam String branchId,@RequestParam String commitId){
        try {
//            获取ElementLibrary
            ElementLibrary elementLibrary = elementLibraryService.getElementLibraryByFileId(fileId);
            if (elementLibrary == null){
                return R.fail("文件关联构建不存在");
            }
//            上传文件
            boolean b = fileService.reupdateElementLibraryFile(file.getOriginalFilename(), file, elementLibrary.getId(), fileId, lod);
            if (b){
                elementLibrary.setLoiNow(loi);
                elementLibrary.setStreamId(streamId);
                elementLibrary.setBranchId(branchId);
                elementLibrary.setCommitId(commitId);
                elementLibrary.setModifyTime(new Date());
                elementLibrary.setStatus(1);
                elementLibrary.setLodNow(lod);
                elementLibraryService.updateElementLibrary(elementLibrary);
                return R.data("更新成功");
            }else {
                return R.data("更新失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateNow.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateNow", notes = "updateNow")
    public R<String> updateNow(@RequestParam String id,@RequestParam String loiNow,@RequestParam String lodNow){
        try {
            ElementLibrary elementLibrary = elementLibraryService.getById(id);
            elementLibrary.setLoiNow(loiNow);
            elementLibrary.setLodNow(lodNow);
            elementLibrary.setStatus(0);
            elementLibraryService.updateElementLibrary(elementLibrary);
            return R.data("更新成功");
        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

}
