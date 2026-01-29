package org.springblade.modules.sp.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.RevitAllUPDTO;
import org.springblade.modules.sp.entity.*;
import org.springframework.web.bind.annotation.*;
import org.springblade.modules.sp.service.ITemplateUpdateService;
import org.springblade.modules.sp.service.TemplatePartElementService;
import org.springblade.modules.sp.service.NewTemplateService;
import org.springblade.modules.sp.service.TemplateVersionService;
import org.springblade.modules.sp.service.TemplateDataService;
import org.springblade.modules.sp.service.ExpInstancesService;
import org.springblade.modules.sp.service.LibraryService;
import org.springblade.modules.sp.service.ITemplateCustomDataService;
import org.springblade.modules.sp.service.BranchService;
import org.springblade.modules.sp.service.WhiteListService;
import org.springblade.modules.sp.entity.WhiteList;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSON;

import javax.validation.Valid;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 模板修改表 控制器
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sp/templateUpdate")
@Api(value = "模板修改", tags = "模板修改接口")
public class TemplateUpdateController {

    private final ITemplateUpdateService templateUpdateService;
    private final TemplatePartElementService templatePartElementService;
    private final NewTemplateService newTemplateService;
    private final TemplateVersionService templateVersionService;
    private final TemplateDataService templateDataService;
    private final ExpInstancesService expInstancesService;
    private final LibraryService libraryService;
    private final ITemplateCustomDataService templateCustomDataService;
    private final BranchService branchService;
    private final WhiteListService whiteListService;

    /**
     * 详情
     */
    @GetMapping("/detail.do")
    @ApiOperation(value = "详情", notes = "传入id")
    public R<TemplateUpdate> detail(@ApiParam(value = "主键ID", required = true) @RequestParam String id) {
        TemplateUpdate detail = templateUpdateService.getById(id);
        return R.data(detail);
    }

    /**
     * 根据模板ID获取修改记录列表
     */
    @GetMapping("/listByTemplateId.do")
    @ApiOperation(value = "根据模板ID获取列表", notes = "传入templateId")
    public R<List<TemplateUpdate>> listByTemplateId(@ApiParam(value = "模板ID", required = true) @RequestParam String templateId) {
        List<TemplateUpdate> list = templateUpdateService.getByTemplateId(templateId);
        return R.data(list);
    }

    /**
     * 根据流水线ID和分支ID获取修改记录列表
     */
    @GetMapping("/listByStreamAndBranchAndTemplateId.do")
    @ApiOperation(value = "根据流水线ID和分支ID获取记录", notes = "传入streamId,branchId,templateId")
    public R<TemplateUpdate> listByStreamAndBranchAndTemplateId(@ApiParam(value = "流水线ID", required = true) @RequestParam String streamId,
                                                                       @ApiParam(value = "分支ID", required = true) @RequestParam String branchId,
                                                                       @ApiParam(value = "模板ID", required = true) @RequestParam String templateId) {
        TemplateUpdate templateUpdate = templateUpdateService.getByStreamAndBranchAndTemplateId(streamId, branchId, templateId);
        return R.data(templateUpdate);
    }

    /**
     * 获取指定条件下每个elementId的最新updateFieldsObj值
     */
    @GetMapping("/getLatestUpdateFields.do")
    @ApiOperation(value = "获取每个elementId的最新updateFieldsObj", notes = "传入streamId,branchId,templateId")
    public R<List<JSONObject>> getLatestUpdateFields(@ApiParam(value = "流水线ID", required = true) @RequestParam String streamId,
                                                     @ApiParam(value = "分支ID", required = true) @RequestParam String branchId,
                                                     @ApiParam(value = "模板ID", required = true) @RequestParam String templateId) {
        try {
            List<JSONObject> result = buildLatestUpdateFields(streamId, branchId, templateId);
            return R.data(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("获取最新更新字段失败：" + e.getMessage());
        }
    }

    /**
     * 构建最新的updateFieldsObj值
     * @param streamId 流水线ID
     * @param branchId 分支ID
     * @param templateId 模板ID
     * @return 最新的updateFieldsObj值
     */
    private List<JSONObject> buildLatestUpdateFields(String streamId, String branchId, String templateId) {
        // 获取所有相关的TemplateUpdate记录，按时间倒序排列
        List<TemplateUpdate> allUpdates = templateUpdateService.getAllByStreamAndBranchAndTemplateId(streamId, branchId, templateId);
        
        // 用于存储每个elementId的最新updateFieldsObj
        Map<String, JSONObject> latestUpdateFieldsMap = new LinkedHashMap<>();
        
        if (allUpdates != null && !allUpdates.isEmpty()) {
            // 遍历所有记录（从最新到最旧）
            for (TemplateUpdate templateUpdate : allUpdates) {
                if (templateUpdate.getData() != null && !templateUpdate.getData().isEmpty()) {
                    // 遍历每条记录的data数组
                    for (JSONObject dataItem : templateUpdate.getData()) {
                        String elementId = dataItem.getString("elementId");
                        
                        // 只有当elementId还没有被记录时才保存（保证是最新的值）
                        if (elementId != null && !latestUpdateFieldsMap.containsKey(elementId)) {
                            // 创建返回格式的JSON对象
                            JSONObject resultItem = new JSONObject();
                            resultItem.put("elementId", elementId);
                            
                            // 获取updateFieldsObj
                            JSONObject updateFieldsObj = dataItem.getJSONObject("updateFieldsObj");
                            if (updateFieldsObj != null) {
                                resultItem.put("updateFieldsObj", updateFieldsObj);
                            } else {
                                resultItem.put("updateFieldsObj", new JSONObject());
                            }
                            
                            latestUpdateFieldsMap.put(elementId, resultItem);
                        }
                    }
                }
            }
        }
        
        // 将Map转换为List并返回
        return new ArrayList<>(latestUpdateFieldsMap.values());
    }

//    ---------revit自定义属性回传--------

    @PostMapping("/updateFinished.do")
    @ApiOperation(value = "更新是否回传", notes = "updateFinished")
    public R<String> updateFinished(@RequestBody RevitAllUPDTO revitAllUPDTO){
        try {
            boolean b = templateCustomDataService.updateFinished(revitAllUPDTO);
            if (b){
                return R.data(revitAllUPDTO.getId());
            }else {
                return R.fail(revitAllUPDTO.getId()+" 回传失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    /**
     * 新增 模板修改表
     */
    @PostMapping("/save.do")
    @ApiOperation(value = "新增", notes = "传入templateUpdate")
    public R save(@Valid @RequestBody TemplateUpdate templateUpdate,
                  @RequestParam(required = false, defaultValue = "1") String isProcess) {
        // 判断newTemplate.getBlockId是否有值
        String templateId = templateUpdate.getTemplateId();
        if (templateId != null) {
            NewTemplate newTemplate = newTemplateService.getTemplateById(templateId);
            if (newTemplate != null && newTemplate.getBlockId() != null && !newTemplate.getBlockId().trim().isEmpty()) {
                // blockId有值，需要合并templateData的data和templateUpdate的data
                System.out.println("检测到blockId有值: " + newTemplate.getBlockId() + "，开始合并数据");
                try {
                    mergeTemplateDataAndUpdate(templateUpdate, newTemplate);
                } catch (Exception e) {
                    System.err.println("合并templateData和templateUpdate数据失败: " + e.getMessage());
                    e.printStackTrace();
                    return R.fail("合并数据失败: " + e.getMessage());
                }
            }
        }
        
        boolean result = templateUpdateService.addTemplateUpdate(templateUpdate);
        
        // 当type=4时，需要更新TemplateData
        if (result && templateUpdate.getTemplateId() != null) {
                updateTemplateDataForType4(templateUpdate);
        }
        
        // 触发模板检查并保存版本信息
        if (result && templateUpdate.getTemplateId() != null) {
            checkTemplateAndSave(templateUpdate.getTemplateId(), templateUpdate.getStreamId(), templateUpdate.getBranchId());
        }

        // 处理自定义模板属性回传（仅当isProcess为1时）
        if (result && isProcess.equals("1") &&
                templateUpdate.getTemplateId() != null && templateUpdate.getStreamId() != null
                && templateUpdate.getBranchId() != null && templateUpdate.getData() != null) {
            try {
                processCustomTemplateData(templateUpdate);
            } catch (Exception e) {
                System.err.println("处理自定义模板属性回传失败: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (result) {
            return R.data(templateUpdate.getId());
        }else {
            return R.fail("新增失败！");
        }
    }
    
    /**
     * 处理自定义模板属性回传
     * @param templateUpdate 模板更新对象
     */
    private void processCustomTemplateData(TemplateUpdate templateUpdate) {
        try {
            String templateId = templateUpdate.getTemplateId();
            String streamId = templateUpdate.getStreamId();
            String branchId = templateUpdate.getBranchId();
            List<JSONObject> dataList = templateUpdate.getData();
            
            // 获取commitId
            String commitId = branchService.getLatestCommitsId(branchId);
            if (commitId == null || commitId.isEmpty()) {
                System.err.println("无法获取commitId，branchId: " + branchId);
                return;
            }
            
            System.out.println("开始处理自定义模板属性回传，templateId: " + templateId + ", streamId: " + streamId + ", branchId: " + branchId + ", commitId: " + commitId);
            
            // 获取白名单列表
            List<WhiteList> whiteList = whiteListService.getByTemplateId(templateId,streamId,branchId);
            if (whiteList == null || whiteList.isEmpty()) {
                System.out.println("警告：未找到白名单，将不过滤属性");
            } else {
                System.out.println("获取到白名单，数量: " + whiteList.size());
            }
            
            List<TemplateCustomData> insertList = new ArrayList<>();
            List<TemplateCustomData> updateList = new ArrayList<>();
            
            // 遍历每个元素的更新数据
            for (JSONObject dataItem : dataList) {
                String elementId = dataItem.getString("elementId");
                JSONObject updateFieldsObj = dataItem.getJSONObject("updateFieldsObj");
                
                if (elementId == null || updateFieldsObj == null || updateFieldsObj.isEmpty()) {
                    continue;
                }
                
                System.out.println("处理elementId: " + elementId + ", updateFields: " + updateFieldsObj);
                
                // 根据streamId/branchId/elementId查找exp_Instances表记录
                ExpInstances expInstance = expInstancesService.getInstanceByElementIdAndStreamAndCommit(
                    elementId, streamId, commitId);
                
                if (expInstance == null) {
                    System.err.println("未找到ExpInstances记录，elementId: " + elementId + ", streamId: " + streamId + ", commitId: " + commitId);
                    continue;
                }
                
                // 构建新的detail数组（使用白名单过滤）
                List<JSONObject> newDetailList = buildDetailList(updateFieldsObj, expInstance, whiteList);
                
                // 查询是否已存在相同streamId/branchId/commitId/elementId的记录
                TemplateCustomData existingData = templateCustomDataService.getByStreamBranchCommitElementId(
                    streamId, branchId, commitId, elementId);
                
                if (existingData != null) {
                    // 已存在记录，合并detail列表（新值覆盖旧值）
                    List<JSONObject> mergedDetailList = mergeDetailList(existingData.getDetail(), newDetailList);
                    existingData.setDetail(mergedDetailList);
                    existingData.setModifyTime(new Date());
                    existingData.setModifyUser(templateUpdate.getModifyUser() != null ? templateUpdate.getModifyUser() : "system");
                    updateList.add(existingData);
                    System.out.println("合并已存在记录，elementId: " + elementId + ", 合并后detailSize: " + mergedDetailList.size());
                } else {
                    // 不存在记录，创建新的TemplateCustomData对象
                    TemplateCustomData customData = new TemplateCustomData(
                        expInstance.getObjId(),
                        templateId,
                        elementId,
                        streamId,
                        branchId,
                        commitId,
                        expInstance.getCategory(),
                        expInstance.getType(),
                        newDetailList,
                        0,
                        templateUpdate.getModifyUser() != null ? templateUpdate.getModifyUser() : "system"
                    );
                    insertList.add(customData);
                    System.out.println("新增记录，elementId: " + elementId + ", detailSize: " + newDetailList.size());
                }
            }
            
            // 批量新增
            if (!insertList.isEmpty()) {
                boolean insertResult = templateCustomDataService.batchInsert(insertList);
                if (insertResult) {
                    System.out.println("成功新增 " + insertList.size() + " 条自定义模板属性数据");
                } else {
                    System.err.println("新增自定义模板属性数据失败");
                }
            }
            
            // 逐条更新已存在的记录
            if (!updateList.isEmpty()) {
                int updateCount = 0;
                for (TemplateCustomData updateData : updateList) {
                    boolean updateResult = templateCustomDataService.updateTemplateCustomData(updateData);
                    if (updateResult) {
                        updateCount++;
                    }
                }
                System.out.println("成功更新 " + updateCount + " 条自定义模板属性数据");
            }
            
        } catch (Exception e) {
            System.err.println("处理自定义模板属性回传异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 合并detail列表（新值覆盖旧值）
     * 根据name字段匹配，相同name的属性只更新value值，不新增重复属性（需求2优化）
     * @param existingDetailList 已存在的detail列表
     * @param newDetailList 新的detail列表
     * @return 合并后的detail列表
     */
    private List<JSONObject> mergeDetailList(List<JSONObject> existingDetailList, List<JSONObject> newDetailList) {
        // 使用LinkedHashMap保持顺序，key为name字段
        Map<String, JSONObject> mergedMap = new LinkedHashMap<>();
        
        // 先添加已存在的detail
        if (existingDetailList != null) {
            for (JSONObject detail : existingDetailList) {
                String name = detail.getString("name");
                if (name != null && !name.isEmpty()) {
                    mergedMap.put(name, detail);
                }
            }
        }
        
        // 处理新的detail列表（需求2优化：如果已存在该属性，则只更新value值）
        if (newDetailList != null) {
            for (JSONObject newDetail : newDetailList) {
                String name = newDetail.getString("name");
                if (name != null && !name.isEmpty()) {
                    if (mergedMap.containsKey(name)) {
                        // 已存在该属性，只更新value字段，保留其他字段不变
                        JSONObject existingDetail = mergedMap.get(name);
                        Object newValue = newDetail.get("value");
                        existingDetail.put("value", newValue);
                        System.out.println("属性 '" + name + "' 已存在，仅更新value: " + newValue);
                    } else {
                        // 不存在该属性，新增整个detail对象
                        mergedMap.put(name, newDetail);
                        System.out.println("属性 '" + name + "' 不存在，新增属性");
                    }
                }
            }
        }
        
        return new ArrayList<>(mergedMap.values());
    }
    
    /**
     * applicationInternalName与属性名的映射关系
     * 用于判断构件属性是否需要走新增属性逻辑
     */
    private static final Map<String, String> APPLICATION_INTERNAL_NAME_MAP = new HashMap<String, String>() {{
        put("ALL_MODEL_TYPE_IMAGE", "类型图像");
        put("KEYNOTE_PARAM", "注释记号");
        put("ALL_MODEL_IMAGE", "型号");
        put("ALL_MODEL_MANUFACTURER", "制造商");
        put("ALL_MODEL_TYPE_COMMENTS", "类型注释");
        put("ALL_MODEL_URL", "URL");
        put("ALL_MODEL_DESCRIPTION", "说明");
        put("UNIFORMAT_DESCRIPTION", "部件说明");
        put("UNIFORMAT_CODE", "部件代码");
        put("WINDOW_TYPE_ID", "类型标记");
        put("DOOR_COST", "成本");
        put("IFC_EXPORT_ELEMENT_TYPE", "导出类型到 IFC");
        put("IFC_EXPORT_ELEMENT_TYPE_AS", "导出类型到 IFC 作为");
        put("IFC_EXPORT_PREDEFINEDTYPE_TYPE", "类型 IFC 预定义类型");
        put("IFC_TYPE_GUID", "类型 IfcGUID");
        put("ALL_MODEL_FAMILY_NAME", "族名称");
        put("ALL_MODEL_TYPE_NAME", "类型名称");
        put("ELEM_FAMILY_PARAM", "族");
        put("ELEM_TYPE_PARAM", "类型");
        put("ELEM_FAMILY_AND_TYPE_PARAM", "族与类型");
        put("ELEM_CATEGORY_PARAM", "类别名");
        put("ELEM_CATEGORY_PARAM_MT", "类别");
        put("SYMBOL_ID_PARAM", "类型ID");
    }};
    
    /**
     * 构建detail列表（使用白名单过滤）
     * @param updateFieldsObj 更新字段对象
     * @param expInstance ExpInstances实例
     * @param whiteList 白名单列表
     * @return detail列表
     */
    private List<JSONObject> buildDetailList(JSONObject updateFieldsObj, ExpInstances expInstance, List<WhiteList> whiteList) {
        List<JSONObject> detailList = new ArrayList<>();
        JSONObject parameters = expInstance.getParameters();
        
        // 建立白名单name集合，用于快速查找
        Set<String> whiteListNames = new HashSet<>();
        if (whiteList != null && !whiteList.isEmpty()) {
            for (WhiteList item : whiteList) {
                if (item.getName() != null && !item.getName().trim().isEmpty()) {
                    whiteListNames.add(item.getName().trim());
                }
            }
            System.out.println("白名单name集合: " + whiteListNames);
        }
        
        // 遍历updateFieldsObj中的每个key-value
        for (String key : updateFieldsObj.keySet()) {
            Object value = updateFieldsObj.get(key);
            JSONObject detailItem = new JSONObject();
            
            // 白名单过滤：如果白名单不为空，且当前key不在白名单中，则跳过
            if (!whiteListNames.isEmpty() && !whiteListNames.contains(key)) {
                System.out.println("属性 '" + key + "' 不在白名单中，跳过");
                continue;
            }
            
            // 检查是否为构件属性（在parameters中查找）
            boolean isComponentParam = false;
            // 用于存储是否需要走新增属性逻辑（applicationInternalName与name匹配）
            boolean shouldUseLibraryLogic = false;
            // 用于存储是否需要更新value（applicationInternalName与name不匹配）
            boolean shouldUpdateValue = false;
            
            if (parameters != null) {
                // 先检查参数是否在parameters中存在，并判断applicationInternalName与name的匹配关系
                ParameterCheckResult checkResult = checkParameterWithApplicationName(key, parameters);
                isComponentParam = checkResult.isFound;
                shouldUseLibraryLogic = checkResult.shouldUseLibraryLogic;
                shouldUpdateValue = checkResult.shouldUpdateValue;
                
                System.out.println("属性 '" + key + "' 检查结果: isComponentParam=" + isComponentParam
                    + ", shouldUseLibraryLogic=" + shouldUseLibraryLogic
                    + ", shouldUpdateValue=" + shouldUpdateValue);
            }
            
            // 情况1：如果是构件属性且applicationInternalName与name匹配，走新增属性逻辑（从sp_lib表查找）
            // 情况2：如果不是构件属性，也走新增属性逻辑
            if (shouldUseLibraryLogic || !isComponentParam) {
                List<Library> libraries = libraryService.getAllLibraryFilter(key, null, null);
                Library library = (libraries != null && !libraries.isEmpty()) ? libraries.get(0) : null;
                if (library != null) {
                    // 按格式B拼接 - 严格按实例格式顺序，强制写入所有字段（包括null和空串）
                    detailItem.put("id", safeGetString(library.getId()));
                    detailItem.put("name", safeGetString(library.getName()));
                    detailItem.put("units", safeGetString(library.getUnits()));
                    detailItem.put("version", safeGetString(library.getVersion()));
                    detailItem.put("category", safeGetString(library.getCategory()));
                    detailItem.put("parameter_type", safeGetString(library.getParameterType()));
                    detailItem.put("data_type", safeGetString(library.getDataType()));
                    detailItem.put("parameters", safeGetString(library.getParameters()));
                    detailItem.put("detail", safeGetString(library.getDetail()));
                    detailItem.put("modify_time", library.getModifyTime()); // Date类型
                    detailItem.put("modify_user", safeGetString(library.getModifyUser()));
                    detailItem.put("status", library.getStatus() != null ? library.getStatus() : 0); // Integer类型
                    detailItem.put("type", safeGetString(library.getType()));
                    detailItem.put("lod", safeGetString(library.getLod()));
                    detailItem.put("revit_parameters", safeGetString(library.getRevitParameters()));
                    detailItem.put("data_type_en", safeGetString(library.getDataTypeEn()));
                    detailItem.put("revit_parameters_en", safeGetString(library.getRevitParametersEn()));
                    detailItem.put("tag", library.getTag() != null ? library.getTag() : new String[]{}); // 数组类型，null时用空数组
                    // 强制写入可能为null的字段 - 确保key存在
                    detailItem.put("guid", library.getGuid());
                    detailItem.put("datacategory", library.getDatacategory());
                    detailItem.put("group", safeGetString(library.getGroup()));
                    detailItem.put("visible", library.getVisible());
                    detailItem.put("description", library.getDescription());
                    detailItem.put("usermodifiable", library.getUsermodifiable());
                    detailItem.put("hidewhennovalue", library.getHidewhennovalue());
                    detailItem.put("source", library.getSource());
                    detailItem.put("isUserParm", true);
                    detailItem.put("value", safeGetString(value));
                    System.out.println("属性 '" + key + "' 使用sp_lib表数据（新增属性逻辑）");
                } else {
                    System.out.println("警告：在sp_lib和parameters中都未找到属性: " + key);
                    continue;
                }
            }
            // 情况3：如果是构件属性且applicationInternalName与name不匹配，更新parameters中的value
            else if (isComponentParam && shouldUpdateValue) {
                // 调用更新value的方法，并填充detailItem
                boolean filled = fillDetailItemWithUpdatedValue(key, value, parameters, detailItem, expInstance.getCategory());
                if (!filled) {
                    System.out.println("警告：填充detailItem失败，属性: " + key);
                    continue;
                }
                System.out.println("属性 '" + key + "' 更新parameters中的value（修改value逻辑）");
            }
            
            // 添加到结果列表
            detailList.add(detailItem);
            System.out.println("属性 '" + key + "' 通过白名单验证，已添加到detail列表");
        }
        
        return detailList;
    }
    
    /**
     * 参数检查结果内部类
     */
    private static class ParameterCheckResult {
        boolean isFound;              // 是否在parameters中找到
        boolean shouldUseLibraryLogic; // 是否走新增属性逻辑（applicationInternalName与name匹配）
        boolean shouldUpdateValue;     // 是否需要更新value（applicationInternalName与name不匹配）
        
        ParameterCheckResult(boolean isFound, boolean shouldUseLibraryLogic, boolean shouldUpdateValue) {
            this.isFound = isFound;
            this.shouldUseLibraryLogic = shouldUseLibraryLogic;
            this.shouldUpdateValue = shouldUpdateValue;
        }
    }
    
    /**
     * 检查参数是否在parameters中存在，并判断applicationInternalName与name的匹配关系
     * @param key 参数名称（属性名）
     * @param parameters parameters JSON对象
     * @return 检查结果
     */
    private ParameterCheckResult checkParameterWithApplicationName(String key, JSONObject parameters) {
        // parameters是一个JSONObject，需要遍历其所有属性
        for (String paramKey : parameters.keySet()) {
            Object paramValue = parameters.get(paramKey);
            
            // 检查paramValue是否是JSONObject且包含name字段
            if (paramValue instanceof JSONObject) {
                JSONObject paramObj = (JSONObject) paramValue;
                String paramName = paramObj.getString("name");
                
                if (paramName != null && paramName.equals(key)) {
                    // 找到匹配的参数，获取applicationInternalName
                    String applicationInternalName = paramObj.getString("applicationInternalName");
                    
                    if (applicationInternalName != null && !applicationInternalName.isEmpty()) {
                        // 检查applicationInternalName是否在映射表中，且对应的值是否与name相同
                        String mappedName = APPLICATION_INTERNAL_NAME_MAP.get(applicationInternalName);
                        
                        if (mappedName != null && mappedName.equals(paramName)) {
                            // applicationInternalName与name匹配，走新增属性逻辑
                            System.out.println("属性 '" + key + "' 的applicationInternalName='" + applicationInternalName
                                + "' 与name='" + paramName + "' 匹配，走新增属性逻辑");
                            return new ParameterCheckResult(true, true, false);
                        } else {
                            // applicationInternalName与name不匹配，需要更新value
                            System.out.println("属性 '" + key + "' 的applicationInternalName='" + applicationInternalName
                                + "' 与name='" + paramName + "' 不匹配（映射值='" + mappedName + "'），走修改value逻辑");
                            return new ParameterCheckResult(true, false, true);
                        }
                    } else {
                        // 没有applicationInternalName字段，按原逻辑处理（更新value）
                        System.out.println("属性 '" + key + "' 没有applicationInternalName字段，走修改value逻辑");
                        return new ParameterCheckResult(true, false, true);
                    }
                }
            }
        }
        
        // 未找到匹配的参数
        return new ParameterCheckResult(false, false, false);
    }
    
    /**
     * 填充detailItem并更新parameters中的value
     * @param key 参数名称
     * @param value 新的参数值
     * @param parameters parameters JSON对象
     * @param detailItem 要填充的detail项
     * @param category 类别
     * @return 是否成功填充
     */
    private boolean fillDetailItemWithUpdatedValue(String key, Object value, JSONObject parameters,
                                                    JSONObject detailItem, String category) {
        // parameters是一个JSONObject，需要遍历其所有属性
        for (String paramKey : parameters.keySet()) {
            Object paramValue = parameters.get(paramKey);
            
            // 检查paramValue是否是JSONObject且包含name字段
            if (paramValue instanceof JSONObject) {
                JSONObject paramObj = (JSONObject) paramValue;
                String paramName = paramObj.getString("name");
                
                if (paramName != null && paramName.equals(key)) {
                    // 找到匹配的参数，更新value
                    paramObj.put("value", safeGetString(value));
                    
                    // 按格式A拼接 - 严格按实例格式
                    detailItem.put("id", safeGetString(paramObj.get("id")));
                    detailItem.put("name", safeGetString(paramName));
                    detailItem.put("units", safeGetString(paramObj.get("units")));
                    detailItem.put("category", safeGetString(category));
                    detailItem.put("pkey", safeGetString(paramKey)); // 使用参数的key作为pkey
                    detailItem.put("isUserParm", false);
                    detailItem.put("value", safeGetString(value));
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 安全获取字符串值 - 保持原值（null/空串/正常值）
     * @param obj 对象
     * @return 字符串值（null保持null，空串保持空串）
     */
    private String safeGetString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        return obj.toString();
    }
    
    /**
     * 获取自定义回传属性列表
     */
    @GetMapping("/getTemplateCustiomDataList.do")
    @ApiOperation(value = "getTemplateCustiomDataList", notes = "getTemplateCustiomDataList")
    public R getTemplateCustiomDataList(@RequestParam String streamId,@RequestParam String branchId,@RequestParam String templateId,@RequestParam Optional<String> elementId){
        try {
            List<TemplateCustomData> templateCustomData = templateCustomDataService.searchFilter(elementId.orElse(""), streamId, branchId, templateId);
            return R.data(templateCustomData);
        }catch (Exception e){
            e.getMessage();
            return R.fail(e.getMessage());
        }
    }

    /**
     * 修改 模板修改表
     */
    @PostMapping("/update.do")
    @ApiOperation(value = "修改", notes = "传入templateUpdate")
    public R update(@Valid @RequestBody TemplateUpdate templateUpdate) {
        return R.status(templateUpdateService.updateTemplateUpdate(templateUpdate));
    }

    /**
     * 新增或修改 模板修改表
     */
    @PostMapping("/submit.do")
    @ApiOperation(value = "新增或修改", notes = "传入templateUpdate")
    public R submit(@Valid @RequestBody TemplateUpdate templateUpdate) {
        return R.status(templateUpdateService.saveOrUpdate(templateUpdate));
    }

    /**
     * 删除 模板修改表
     */
    @PostMapping("/remove.do")
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键ID", required = true) @RequestParam String id) {
        return R.status(templateUpdateService.deleteTemplateUpdate(id));
    }

//    --------版本历史记录--------
    @SneakyThrows
    @GetMapping("/checkTemplate.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "检验版本历史", notes = "检验版本历史")
    public R checkTemplate(@ApiParam(value = "模板ID", required = true) @RequestParam String templateId) {
        try {
            List<TemplateVersion> list = templateVersionService.getByTemplateId(templateId);
            return R.data(list);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }
    
    /**
     * 下载模板版本数据为Excel
     */
    @GetMapping("/downloadTemplateVersionData.do")
    @ApiOperation(value = "下载模板版本数据", notes = "传入TemplateVersion的id，下载data数据为Excel")
    public void downloadTemplateVersionData(@ApiParam(value = "模板版本ID", required = true) @RequestParam String id,
                                            HttpServletResponse response) {
        try {
            // 获取TemplateVersion
            TemplateVersion templateVersion = templateVersionService.getById(id);
            
            if (templateVersion == null || templateVersion.getData() == null || templateVersion.getData().isEmpty()) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":false,\"msg\":\"未找到数据或数据为空\"}");
                return;
            }
            
            List<JSONObject> data = templateVersion.getData();
            
            // 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("模板版本数据");
            
            // 收集所有的key作为表头
            Set<String> allKeys = new LinkedHashSet<>();
            for (JSONObject item : data) {
                allKeys.addAll(item.keySet());
            }
            
            // 创建表头行
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            List<String> keyList = new ArrayList<>(allKeys);
            for (int i = 0; i < keyList.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(keyList.get(i));
                cell.setCellStyle(headerStyle);
            }
            
            // 填充数据行
            for (int i = 0; i < data.size(); i++) {
                JSONObject item = data.get(i);
                Row dataRow = sheet.createRow(i + 1);
                
                for (int j = 0; j < keyList.size(); j++) {
                    String key = keyList.get(j);
                    Object value = item.get(key);
                    Cell cell = dataRow.createCell(j);
                    
                    if (value != null) {
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                        } else if (value instanceof Boolean) {
                            cell.setCellValue((Boolean) value);
                        } else if (value instanceof Date) {
                            cell.setCellValue((Date) value);
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    } else {
                        cell.setCellValue("");
                    }
                }
            }
            
            // 自动调整列宽
            for (int i = 0; i < keyList.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 设置响应头
            String fileName = "template_version_data_" + id + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            
            // 写入输出流
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();
            outputStream.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":false,\"msg\":\"下载失败：" + e.getMessage() + "\"}");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 检查模板并保存版本信息（支持指定调用来源）
     * @param templateId 模板ID
     * @param streamId 流水线ID  
     * @param branchId 分支ID
     * @param source 调用来源：save/addElement
     */
    public void checkTemplateAndSave(String templateId, String streamId, String branchId, String source) {
        try {
            Map<String, Object> result = performTemplateCheck(templateId, streamId, branchId, source);
            
            // 保存结果到数据库
            TemplateVersion templateVersion = new TemplateVersion();
            templateVersion.setTemplateId(templateId);
            
            // 根据调用来源决定写入哪些字段，使用0而不是null
            if ("addElement".equals(source)) {
                // addElement调用时，写入insertCount和deleteCount，updateCount设为0
                templateVersion.setInsertCount(Math.max(0, (Integer) result.get("insert")));
                templateVersion.setDeleteCount(Math.max(0, (Integer) result.get("del")));
                templateVersion.setUpdateCount(0); // 设为0而不是null
            } else {
                // save调用时，写入updateCount，insertCount和deleteCount设为0
                templateVersion.setInsertCount(0); // 设为0而不是null
                templateVersion.setDeleteCount(0); // 设为0而不是null
                templateVersion.setUpdateCount(Math.max(0, (Integer) result.get("update")));
            }

            List<JSONObject> mergedData = mergeTemplateDataWithUpdate(templateId, branchId);
            templateVersion.setData(mergedData);

            NewTemplate newTemplate = newTemplateService.getTemplateById(templateId);
            newTemplate.setVersion((String) result.get("version"));
            newTemplate.setModifyTime(new Date());
            newTemplateService.updateTemplate(newTemplate);
            
            templateVersion.setCheckTime((String) result.get("time"));
            templateVersion.setVersion((String) result.get("version"));
            templateVersion.setCreateTime(new Date());
            
            templateVersionService.addTemplateVersion(templateVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkTemplateAndSave(String templateId, String streamId, String branchId) {
        checkTemplateAndSave(templateId, streamId, branchId, "save");
    }

    /**
     * 执行模板检查逻辑
     * @param templateId 模板ID
     * @param streamId 流水线ID
     * @param branchId 分支ID
     * @param source 调用来源：save/addElement
     * @return 检查结果
     */
    private Map<String, Object> performTemplateCheck(String templateId, String streamId, String branchId, String source) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if ("addElement".equals(source)) {
                // addElement调用：比较elementId变化，只记录本次的实际变化
                
                // 获取TemplatePartElement的最新两条记录
                List<TemplatePartElement> latestTwoElements = templatePartElementService.getLatestTwoByTemplateId(templateId);
                
                if (latestTwoElements == null || latestTwoElements.size() < 2) {
                    // 如果数据不足，默认为没有变化
                    result.put("insert", 0);
                    result.put("del", 0);
                    result.put("update", 0);
                } else {
                    // 解析两次element数据中level为"1"的数据
                    Map<String, JSONObject> currentLevelOneElements = extractLevelOneElements(latestTwoElements.get(0));
                    Map<String, JSONObject> previousLevelOneElements = extractLevelOneElements(latestTwoElements.get(1));

                    // 只计算本次变化，不累积历史数据
                    int currentInsertCount = 0;
                    int currentDeleteCount = 0;

                    // 统计新增的elementId
                    for (String elementId : currentLevelOneElements.keySet()) {
                        if (!previousLevelOneElements.containsKey(elementId)) {
                            currentInsertCount++;
                        }
                    }

                    // 统计删除的elementId
                    for (String elementId : previousLevelOneElements.keySet()) {
                        if (!currentLevelOneElements.containsKey(elementId)) {
                            currentDeleteCount++;
                        }
                    }

                    // 只记录本次实际变化，确保非负
                    result.put("insert", Math.max(0, currentInsertCount));
                    result.put("del", Math.max(0, currentDeleteCount));
                    result.put("update", 0); // addElement时不计算update
                }
            } else {
                // save调用：只记录本次的update数量
                
                // 获取TemplateUpdate数据，计算update次数
                TemplateUpdate updateData = templateUpdateService.getByStreamAndBranchAndTemplateId(streamId, branchId, templateId);
                int currentUpdateCount = 0;
                if (updateData != null && updateData.getData() != null) {
                    currentUpdateCount = Math.max(0, updateData.getData().size());
                }
                
                // 只记录本次实际的update数量，不累积历史数据
                result.put("insert", 0); // save时不计算insert
                result.put("del", 0); // save时不计算del
                result.put("update", currentUpdateCount);
            }

            // 生成当前时间戳
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            String currentTime = dateFormat.format(new Date());

            // 根据调用来源设置version
            String versionToSet = null;
            if ("save".equals(source)) {
                // save时：获取最新非空version并+1
                String latestVersion = templateVersionService.getLatestNonNullVersionByTemplateId(templateId);
                if (latestVersion != null && latestVersion.startsWith("V")) {
                    try {
                        int versionNumber = Integer.parseInt(latestVersion.substring(1));
                        versionToSet = "V" + (versionNumber + 1);
                    } catch (NumberFormatException e) {
                        versionToSet = "V1"; // 解析失败时默认为V1
                    }
                } else {
                    versionToSet = "V1"; // 没有历史版本时从V1开始
                }
            }
            // addElement时versionToSet保持null

            result.put("time", currentTime);
            result.put("version", versionToSet);
            
        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常时返回默认安全值
            result.put("insert", 0);
            result.put("del", 0);
            result.put("update", 0);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            result.put("time", dateFormat.format(new Date()));
            
            // 异常时的版本号逻辑
            String versionToSet = null;
            if ("save".equals(source)) {
                try {
                    String latestVersion = templateVersionService.getLatestNonNullVersionByTemplateId(templateId);
                    if (latestVersion != null && latestVersion.startsWith("V")) {
                        int versionNumber = Integer.parseInt(latestVersion.substring(1));
                        versionToSet = "V" + (versionNumber + 1);
                    } else {
                        versionToSet = "V1";
                    }
                } catch (Exception ex) {
                    versionToSet = "V1"; // 双重异常时默认为V1
                }
            }
            result.put("version", versionToSet);
        }

        return result;
    }

    /**
     * 从TemplatePartElement中提取level为"1"的元素
     * @param element TemplatePartElement对象
     * @return 以elementId为key的Map
     */
    private Map<String, JSONObject> extractLevelOneElements(TemplatePartElement element) {
        Map<String, JSONObject> levelOneElements = new HashMap<>();

        if (element == null || element.getElementArray() == null) {
            return levelOneElements;
        }

        for (JSONObject jsonObj : element.getElementArray()) {
            String level = jsonObj.getString("level");
            if ("1".equals(level)) {
                String elementId = jsonObj.getString("elementId");
                if (elementId != null && !elementId.isEmpty()) {
                    levelOneElements.put(elementId, jsonObj);
                }
            }
        }

        return levelOneElements;
    }

    /**
     * 为type=4的模板更新TemplateData
     * @param templateUpdate TemplateUpdate对象
     */
    private void updateTemplateDataForType4(TemplateUpdate templateUpdate) {
        try {
            String templateId = templateUpdate.getTemplateId();
            if (templateId == null || templateUpdate.getData() == null || templateUpdate.getData().isEmpty()) {
                System.out.println("updateTemplateDataForType4: 参数验证失败 - templateId或data为空");
                return;
            }
            
            System.out.println("开始处理type=4模板更新，templateId: " + templateId);
            System.out.println("TemplateUpdate.data内容: " + templateUpdate.getData());
            
            // 获取NewTemplate的type作为TemplateData的type
            NewTemplate newTemplate = newTemplateService.getTemplateById(templateId);
            if (newTemplate == null) {
                System.out.println("未找到NewTemplate: " + templateId);
                return;
            }
            String templateDataType = String.valueOf(newTemplate.getType());
            System.out.println("NewTemplate.type: " + templateDataType);
            
            // 获取现有的TemplateData
            String branchId = templateUpdate.getBranchId();
            TemplateData existingTemplateData = templateDataService.getByTemplateIdAndType(templateId, templateDataType, branchId);
            if (existingTemplateData == null || existingTemplateData.getData() == null || existingTemplateData.getData().isEmpty()) {
                System.out.println("未找到TemplateData或data为空，templateId: " + templateId + ", type: " + templateDataType + ", branchId: " + branchId);
                return;
            }
            
            System.out.println("找到TemplateData，数据条数: " + existingTemplateData.getData().size());
            System.out.println("TemplateData.data内容: " + existingTemplateData.getData());
            
            // 解析TemplateUpdate的data数据
            List<JSONObject> updateDataList = templateUpdate.getData();
            Map<String, JSONObject> updateFieldsMap = new HashMap<>();
            
            // 将TemplateUpdate.data中的更新数据按elementId组织
            for (JSONObject updateItem : updateDataList) {
                String elementId = updateItem.getString("elementId");
                JSONObject updateFieldsObj = updateItem.getJSONObject("updateFieldsObj");
                if (elementId != null && updateFieldsObj != null) {
                    updateFieldsMap.put(elementId, updateFieldsObj);
                    System.out.println("准备更新elementId: " + elementId + ", 更新字段: " + updateFieldsObj);
                }
            }
            
            // 更新TemplateData.data中匹配的elementId条目
            List<JSONObject> templateDataList = existingTemplateData.getData();
            boolean hasUpdates = false;
            
            for (JSONObject templateDataItem : templateDataList) {
                String elementId = templateDataItem.getString("elementId");
                System.out.println("检查TemplateData中的elementId: " + elementId);
                
                if (elementId != null && updateFieldsMap.containsKey(elementId)) {
                    JSONObject updateFields = updateFieldsMap.get(elementId);
                    System.out.println("找到匹配的elementId: " + elementId + ", 开始更新字段: " + updateFields);
                    
                    // 将updateFields中的所有字段合并到templateDataItem中，处理类型转换
                    for (String key : updateFields.keySet()) {
                        Object updateValue = updateFields.get(key);
                        Object originalValue = templateDataItem.get(key);
                        
                        System.out.println("更新字段 " + key + ": " + originalValue + " -> " + updateValue);
                        
                        // 处理数值类型转换
                        Object finalValue = convertValueType(updateValue, originalValue);
                        templateDataItem.put(key, finalValue);
                        
                        System.out.println("最终设置值: " + finalValue + " (类型: " + (finalValue != null ? finalValue.getClass().getSimpleName() : "null") + ")");
                    }
                    hasUpdates = true;
                } else {
                    System.out.println("elementId: " + elementId + " 在updateFieldsMap中未找到匹配项");
                }
            }
            
            // 输出所有可用的elementId以便调试
            System.out.println("TemplateData中所有elementId: " + 
                templateDataList.stream()
                    .map(item -> item.getString("elementId"))
                    .collect(java.util.stream.Collectors.toList()));
            System.out.println("TemplateUpdate中所有elementId: " + new ArrayList<>(updateFieldsMap.keySet()));
            
            // 如果有更新，保存TemplateData
            if (hasUpdates) {
                existingTemplateData.setData(templateDataList);
                boolean updateResult = templateDataService.updateTemplateData(existingTemplateData);
                if (updateResult) {
                    System.out.println("成功更新TemplateData，templateId: " + templateId);
                } else {
                    System.err.println("保存TemplateData失败，templateId: " + templateId);
                }
            } else {
                System.out.println("没有找到匹配的elementId，未执行更新");
            }
            
        } catch (Exception e) {
            System.err.println("更新TemplateData失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 处理值类型转换，保持原有类型一致性
     * @param newValue 新值
     * @param originalValue 原始值
     * @return 转换后的值
     */
    private Object convertValueType(Object newValue, Object originalValue) {
        if (newValue == null) {
            return null;
        }
        
        if (originalValue == null) {
            return newValue;
        }
        
        // 如果原值是数字类型，新值是字符串，尝试转换
        if (originalValue instanceof Number && newValue instanceof String) {
            String strValue = (String) newValue;
            try {
                if (originalValue instanceof Integer) {
                    return Integer.parseInt(strValue);
                } else if (originalValue instanceof Long) {
                    return Long.parseLong(strValue);
                } else if (originalValue instanceof Double) {
                    return Double.parseDouble(strValue);
                } else if (originalValue instanceof Float) {
                    return Float.parseFloat(strValue);
                }
            } catch (NumberFormatException e) {
                System.out.println("数值转换失败，保持原字符串值: " + strValue);
                return newValue;
            }
        }
        
        // 如果原值是字符串，新值是数字，转为字符串
        if (originalValue instanceof String && newValue instanceof Number) {
            return String.valueOf(newValue);
        }
        
        return newValue;
    }
    
    /**
     * 合并TemplateData的data和入参TemplateUpdate的data（用于blockId有值的情况）
     * 以templateDataList（TemplateData）的数据为主，匹配逻辑不变
     * 将templateDataList里对应的value修改为匹配后的value，将合并后的数据添加到templateUpdate的data中
     * 输出结果 = templateDataList全部条数 + TemplateUpdate中未匹配的条数
     * @param templateUpdate 模板更新对象
     * @param newTemplate 模板对象
     */
    private void mergeTemplateDataAndUpdate(TemplateUpdate templateUpdate, NewTemplate newTemplate) {
        try {
            String templateId = templateUpdate.getTemplateId();
            String branchId = templateUpdate.getBranchId();
            String templateType = String.valueOf(newTemplate.getType());
            
            // 调用templateDataService.getDataByTemplateIdAndType获取templateData的data值
            JSONObject templateDataResult = templateDataService.getDataByTemplateIdAndType(templateId, templateType, branchId);
            if (templateDataResult == null || !templateDataResult.containsKey("data")) {
                System.err.println("未获取到templateData的data数据");
                return;
            }
            
            Object dataObj = templateDataResult.get("data");
            List<JSONObject> templateDataList = null;
            
            // 处理data可能是JSONArray或List的情况
            if (dataObj instanceof JSONArray) {
                templateDataList = ((JSONArray) dataObj).toJavaList(JSONObject.class);
            } else if (dataObj instanceof List) {
                templateDataList = (List<JSONObject>) dataObj;
            } else {
                System.err.println("templateData的data格式不正确");
                return;
            }
            
            if (templateDataList == null || templateDataList.isEmpty()) {
                System.out.println("templateData的data为空，无需合并");
                return;
            }
            
            System.out.println("获取到templateData的data，条数: " + templateDataList.size());
            
            // 获取templateUpdate的data
            List<JSONObject> updateDataList = templateUpdate.getData();
            if (updateDataList == null || updateDataList.isEmpty()) {
                System.out.println("templateUpdate的data为空，无需合并");
                return;
            }
            
            System.out.println("templateUpdate的data条数: " + updateDataList.size());
            
            // 构建updateFieldsMap，key为elementId，value为整个updateItem（包含elementId和updateFieldsObj）
            Map<String, JSONObject> updateFieldsMap = new HashMap<>();
            // 记录哪些elementId已匹配
            Set<String> matchedElementIds = new HashSet<>();
            
            for (JSONObject updateItem : updateDataList) {
                String elementId = updateItem.getString("elementId");
                JSONObject updateFieldsObj = updateItem.getJSONObject("updateFieldsObj");
                if (elementId != null && updateFieldsObj != null) {
                    updateFieldsMap.put(elementId, updateFieldsObj);
                }
            }
            
            System.out.println("TemplateUpdate中的elementId集合: " + updateFieldsMap.keySet());
            
            // 用于存储最终合并后的数据
            List<JSONObject> finalMergedData = new ArrayList<>();
            
            // 第一步：遍历templateDataList，匹配并更新对应的值
            for (JSONObject templateDataItem : templateDataList) {
                String elementId = templateDataItem.getString("elementId");
                
                if (elementId != null && updateFieldsMap.containsKey(elementId)) {
                    // 匹配上了，替换updateFieldsObj的值
                    JSONObject updateFields = updateFieldsMap.get(elementId);
                    System.out.println("匹配elementId: " + elementId + " 的数据，替换updateFieldsObj");
                    
                    // 构造新的合并项
                    JSONObject mergedItem = new JSONObject();
                    mergedItem.put("elementId", elementId);
                    
                    // 创建新的updateFieldsObj，先复制templateDataItem的所有字段（除了elementId）
                    JSONObject mergedUpdateFieldsObj = new JSONObject();
                    for (String key : templateDataItem.keySet()) {
                        if (!"elementId".equals(key)) {
                            mergedUpdateFieldsObj.put(key, templateDataItem.get(key));
                        }
                    }
                    
                    // 将updateFields中的字段覆盖到mergedUpdateFieldsObj
                    for (String key : updateFields.keySet()) {
                        Object updateValue = updateFields.get(key);
                        Object originalValue = mergedUpdateFieldsObj.get(key);
                        Object finalValue = convertValueType(updateValue, originalValue);
                        mergedUpdateFieldsObj.put(key, finalValue);
                        System.out.println("  更新字段 " + key + ": " + originalValue + " -> " + finalValue);
                    }
                    
                    mergedItem.put("updateFieldsObj", mergedUpdateFieldsObj);
                    finalMergedData.add(mergedItem);
                    matchedElementIds.add(elementId);
                } else {
                    // 未匹配上，保持原样加入
                    JSONObject unmodifiedItem = new JSONObject();
                    unmodifiedItem.put("elementId", elementId);
                    
                    // 将templateDataItem的所有字段（除了elementId）作为updateFieldsObj
                    JSONObject updateFieldsObj = new JSONObject();
                    for (String key : templateDataItem.keySet()) {
                        if (!"elementId".equals(key)) {
                            updateFieldsObj.put(key, templateDataItem.get(key));
                        }
                    }
                    unmodifiedItem.put("updateFieldsObj", updateFieldsObj);
                    finalMergedData.add(unmodifiedItem);
                }
            }
            
            System.out.println("处理完templateDataList，当前合并数据条数: " + finalMergedData.size());
            System.out.println("已匹配的elementId集合: " + matchedElementIds);
            
            // 第二步：将TemplateUpdate中未匹配的数据添加到结果中
            int unmatchedCount = 0;
            for (JSONObject updateItem : updateDataList) {
                String elementId = updateItem.getString("elementId");
                if (elementId != null && !matchedElementIds.contains(elementId)) {
                    // 未匹配的数据，保持不变加入输出
                    JSONObject unmatchedItem = new JSONObject();
                    unmatchedItem.put("elementId", elementId);
                    unmatchedItem.put("updateFieldsObj", updateItem.getJSONObject("updateFieldsObj"));
                    finalMergedData.add(unmatchedItem);
                    unmatchedCount++;
                    System.out.println("添加未匹配的elementId: " + elementId);
                }
            }
            
            System.out.println("添加了 " + unmatchedCount + " 条未匹配的TemplateUpdate数据");
            
            // 更新templateUpdate的data为合并后的数据
            if (!finalMergedData.isEmpty()) {
                templateUpdate.setData(finalMergedData);
                System.out.println("合并完成，最终输出templateUpdate.data条数: " + finalMergedData.size()
                    + " (templateDataList: " + templateDataList.size()
                    + ", 匹配: " + matchedElementIds.size()
                    + ", 未匹配: " + unmatchedCount + ")");
            }
            
        } catch (Exception e) {
            System.err.println("合并TemplateData和TemplateUpdate数据失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("合并数据失败", e);
        }
    }
    
    /**
     * 合并TemplateData和TemplateUpdate数据（用于版本历史）
     * @param templateId 模板ID
     * @param branchId 分支ID
     * @return 合并后的数据列表
     */
    private List<JSONObject> mergeTemplateDataWithUpdate(String templateId, String branchId) {
        try {
            // 获取NewTemplate的type
            NewTemplate newTemplate = newTemplateService.getTemplateById(templateId);
            if (newTemplate == null) {
                System.out.println("未找到NewTemplate: " + templateId);
                return new ArrayList<>();
            }
            String templateDataType = String.valueOf(newTemplate.getType());
            
            // 获取TemplateData
            TemplateData templateData = templateDataService.getByTemplateIdAndType(templateId, templateDataType, branchId);
            if (templateData == null || templateData.getData() == null || templateData.getData().isEmpty()) {
                System.out.println("未找到TemplateData或data为空，templateId: " + templateId);
                return new ArrayList<>();
            }
            
            // 深拷贝TemplateData.data，避免修改原数据
            List<JSONObject> mergedData = new ArrayList<>();
            for (JSONObject item : templateData.getData()) {
                mergedData.add((JSONObject) item.clone());
            }
            
            // 获取TemplateUpdate数据
            TemplateUpdate templateUpdate = templateUpdateService.getByStreamAndBranchAndTemplateId(
                newTemplate.getStreamId(), branchId, templateId);
            
            if (templateUpdate != null && templateUpdate.getData() != null && !templateUpdate.getData().isEmpty()) {
                // 构建elementId到updateFieldsObj的映射
                Map<String, JSONObject> updateFieldsMap = new HashMap<>();
                for (JSONObject updateItem : templateUpdate.getData()) {
                    String elementId = updateItem.getString("elementId");
                    JSONObject updateFieldsObj = updateItem.getJSONObject("updateFieldsObj");
                    if (elementId != null && updateFieldsObj != null) {
                        updateFieldsMap.put(elementId, updateFieldsObj);
                    }
                }
                
                // 遍历mergedData，将updateFieldsObj中的值覆盖到对应的记录
                for (JSONObject dataItem : mergedData) {
                    String elementId = dataItem.getString("elementId");
                    if (elementId != null && updateFieldsMap.containsKey(elementId)) {
                        JSONObject updateFields = updateFieldsMap.get(elementId);
                        // 将updateFields中的所有字段合并到dataItem中
                        for (String key : updateFields.keySet()) {
                            Object updateValue = updateFields.get(key);
                            Object originalValue = dataItem.get(key);
                            // 处理类型转换
                            Object finalValue = convertValueType(updateValue, originalValue);
                            dataItem.put(key, finalValue);
                        }
                    }
                }
            }
            
            return mergedData;
            
        } catch (Exception e) {
            System.err.println("合并TemplateData和TemplateUpdate失败: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
}
