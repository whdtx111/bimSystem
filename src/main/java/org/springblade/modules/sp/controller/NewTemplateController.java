package org.springblade.modules.sp.controller;


import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.*;
import org.springblade.modules.sp.excel.EasyExcelReadUtils;
import org.springblade.modules.sp.service.*;
import org.springblade.modules.sp.service.WhiteListService;
import org.springblade.modules.sp.vo.StreamListTemplatesVO;
import org.springblade.modules.sp.vo.TemplateStreamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * 数据模板
 *
 * @author TianXiao Deng
 * @since 2022-07-12
 */

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/sp/newTemplate")
@CrossOrigin
@Api(value = "数据模板", tags = "数据模板")
public class NewTemplateController {

    @Autowired
    private NewTemplateService newTemplateService;
    @Autowired
    private IUsersService usersService;
    @Autowired
    private FileService fileService;
    @Autowired
    private BomFileService bomFileService;
    @Autowired
    private StreamListService streamListService;
    @Autowired
    private TemplatePartElementService templatePartElementService;
    @Autowired
    private TemplateVersionService templateVersionService;
    @Autowired
    private TemplateUpdateController templateUpdateController;
    @Autowired
    private TemplateDataService templateDataService;
    @Autowired
    private ExpInstancesService expInstancesService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private WhiteListService whiteListService;
    @Autowired
    private MaterialListConversionService materialListConversionService;
    @Autowired
    private ITemplateCustomDataService templateCustomDataService;
    @Autowired
    private ITemplateUpdateService templateUpdateService;
    
    /**
     * 字段映射表：Excel表头字段 -> 构件参数字段
     * key为Excel表头中的字段名，value为parameters中的实际字段名
     * 支持两种格式：
     * 1. 单一字段：如"长（mm）"
     * 2. 组合字段（合并单元格）：如"外观尺寸-长（mm）"
     */
    private static final Map<String, String> FIELD_MAPPINGS = new HashMap<>();
    static {
        // 构件型号映射
        FIELD_MAPPINGS.put("构件型号", "构件编码");
        
        // 单体体积映射（支持全角/半角括号）
        FIELD_MAPPINGS.put("单体体积（m³）", "体积");
        FIELD_MAPPINGS.put("单体体积(m³)", "体积");
        FIELD_MAPPINGS.put("单体体积", "体积");
        
        // 单体重量映射（支持全角/半角括号）
        FIELD_MAPPINGS.put("单体重量（T）", "重量");
        FIELD_MAPPINGS.put("单体重量(T)", "重量");
        FIELD_MAPPINGS.put("单体重量", "重量");
        
        // 砼用量映射（支持全角/半角括号）
        FIELD_MAPPINGS.put("混凝土-砼用量（m³）", "混凝土用量");
        FIELD_MAPPINGS.put("混凝土-砼用量(m³)", "混凝土用量");
        FIELD_MAPPINGS.put("混凝土-砼用量", "混凝土用量");
        
        // 砼标号映射
        FIELD_MAPPINGS.put("混凝土-砼标号", "混凝土强度");
        
        // 尺寸映射 - 单一字段格式（向下兼容）
        FIELD_MAPPINGS.put("长（mm）", "占位尺寸（长度）");
        FIELD_MAPPINGS.put("长(mm)", "占位尺寸（长度）");
        FIELD_MAPPINGS.put("长", "占位尺寸（长度）");
        FIELD_MAPPINGS.put("宽（mm）", "占位尺寸（宽度）");
        FIELD_MAPPINGS.put("宽(mm)", "占位尺寸（宽度）");
        FIELD_MAPPINGS.put("宽", "占位尺寸（宽度）");
        FIELD_MAPPINGS.put("厚（mm）", "占位尺寸（高度）");
        FIELD_MAPPINGS.put("厚(mm)", "占位尺寸（高度）");
        FIELD_MAPPINGS.put("厚", "占位尺寸（高度）");
        
        // 尺寸映射 - 组合字段格式（合并单元格：外观尺寸-xxx）
        FIELD_MAPPINGS.put("外观尺寸-长（mm）", "占位尺寸（长度）");
        FIELD_MAPPINGS.put("外观尺寸-长(mm)", "占位尺寸（长度）");
        FIELD_MAPPINGS.put("外观尺寸-长", "占位尺寸（长度）");
        FIELD_MAPPINGS.put("外观尺寸-宽（mm）", "占位尺寸（宽度）");
        FIELD_MAPPINGS.put("外观尺寸-宽(mm)", "占位尺寸（宽度）");
        FIELD_MAPPINGS.put("外观尺寸-宽", "占位尺寸（宽度）");
        FIELD_MAPPINGS.put("外观尺寸-厚（mm）", "占位尺寸（高度）");
        FIELD_MAPPINGS.put("外观尺寸-厚(mm)", "占位尺寸（高度）");
        FIELD_MAPPINGS.put("外观尺寸-厚", "占位尺寸（高度）");
        
        // 楼层映射
        FIELD_MAPPINGS.put("起始Floor", "所在楼层");
        FIELD_MAPPINGS.put("终止Floor", "所在楼层");
        
        // 户型映射
        FIELD_MAPPINGS.put("户型", "建筑单体名称");
    }
    
    /**
     * 获取数据模板文件树
     */
    @SneakyThrows
    @GetMapping("/getTemplateList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getTemplateList", notes = "getTemplateList")
    public R<List<NewTemplate>> getTemplateList() {
        try {
            List<NewTemplate> templateListDto = newTemplateService.getTemplateList();
            return R.data(templateListDto);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getTemplateListByStreamId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getTemplateListByStreamId", notes = "getTemplateListByStreamId")
    public R<List<NewTemplate>> getTemplateListByStreamId(@RequestParam String streamId,@RequestParam Optional<String> branchId) {
        try {
            List<NewTemplate> templateListDto = newTemplateService.getTemplateListByStreamId(streamId,branchId.orElse(""));
            return R.data(templateListDto);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getTemplateListDefault.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getTemplateListDefault", notes = "getTemplateListDefault")
    public R<List<NewTemplate>> getTemplateListDefault() {
        try {
            List<NewTemplate> templateListDto = newTemplateService.getTemplateListDefault();
            return R.data(templateListDto);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getTemplateWithStreamByStreamId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "获取所有流水线及其关联的模板列表", notes = "获取所有流水线及其关联的模板列表")
    public R<List<StreamListTemplatesVO>> getTemplateWithStreamByStreamId(){
        try {
            List<StreamListTemplatesVO> result = newTemplateService.getTemplateWithStreamByStreamId();
            return R.data(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getStreamListTemplatesById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "根据流水线ID获取流水线及其关联的模板列表", notes = "根据流水线ID获取流水线及其关联的模板列表")
    public R<List<StreamListTemplatesVO>> getStreamListTemplatesById(@RequestParam String streamListId){
        try {
            List<StreamListTemplatesVO> result = newTemplateService.getStreamListTemplatesById(streamListId);
            return R.data(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/filterTemplateList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "filterTemplateList", notes = "filterTemplateList")
    public R<List<NewTemplate>> filterTemplateList(@RequestParam Optional<String> pid, @RequestParam Optional<String> fileId,
                                                   @RequestParam Optional<String> streamId, @RequestParam Optional<String> branchId,
                                                   @RequestParam Optional<String> name, @RequestParam Optional<String> type,
                                                   @RequestParam Optional<String> source, @RequestParam Optional<String> version,
                                                   @RequestParam Optional<String> detail, @RequestParam Optional<String> modifyUser,
                                                   @RequestParam Optional<String> auth) {
        try {
            List<NewTemplate> templateLists = newTemplateService.filterTemplateList(pid.orElse(null), fileId.orElse(null), streamId.orElse(null), 
                    branchId.orElse(null), name.orElse(null), type.orElse(null), source.orElse(null), version.orElse(null), 
                    detail.orElse(null), modifyUser.orElse(null), auth.orElse(null));
            return R.data(templateLists);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getTemplateById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getTemplateById", notes = "getTemplateById")
    public R<NewTemplate> getTemplateById(@RequestParam String id) {
        try {
            NewTemplate templateListDto = newTemplateService.getTemplateById(id);
            return R.data(templateListDto);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping(value = "/addTemplate.do", consumes = "application/json")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addTemplate", notes = "addTemplate")
    public R addTemplate(@RequestBody Map<String, Object> requestMap) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String detailJson = "";

            // 提取detail字段并转为JSON字符串
            if (requestMap.containsKey("detail")) {
                Object detailObj = requestMap.get("detail");
                if (detailObj != null) {
                    if (detailObj instanceof String) {
                        // 如果已经是字符串，直接使用
                        detailJson = (String) detailObj;
                    } else {
                        // 如果是对象，转换为JSON字符串
                        detailJson = objectMapper.writeValueAsString(detailObj);
                    }
                }
                // 从map中移除detail，避免后续转换冲突
                requestMap.remove("detail");
            }
            
            // 将剩余字段转换为NewTemplate对象
            NewTemplate newTemplate = objectMapper.convertValue(requestMap, NewTemplate.class);

            // 设置detail字段
            newTemplate.setDetail(detailJson);
            newTemplate.setStatus(0);
            newTemplate.setModifyTime(new Date());
            if (StringUtils.isEmpty(newTemplate.getSource())) {
                newTemplate.setSource("back");
            }
            if (newTemplate.getType().equals("0")){
                newTemplate.setPid("0");
            }
            if (StringUtils.isEmpty(newTemplate.getVersion())) {
                newTemplate.setVersion("V1");
            }
            if (StringUtils.isEmpty(newTemplate.getTemplateId())) {
                List<BomFile> latestTemplateFile = bomFileService.getLatestTemplateFile();
                for (BomFile file : latestTemplateFile) {
                    if (file.getType().equals(newTemplate.getType())) {
                        newTemplate.setTemplateId(file.getId());
                    }
                }
            }
            boolean saved = newTemplateService.saveTemplate(newTemplate);
            if (saved) {
                // 新增逻辑：如果type=4的自定义模板，解析表头并保存到白名单表
                if ("4".equals(newTemplate.getType())) {
                    try {
                        log.info("检测到type=4的自定义模板，开始解析表头并保存到白名单表");
                        parseAndSaveWhiteList(newTemplate);
                        log.info("表头解析并保存到白名单表成功");
                    } catch (Exception e) {
                        log.error("解析表头并保存到白名单表失败: {}", e.getMessage(), e);
                        // 不影响主流程，继续执行
                    }
                }
                
                // 新增逻辑：如果streamId为空且streamListId不为空，则同步到该StreamList的所有streamIds
                boolean shouldSync = (newTemplate.getStreamId() == null || 
                                    newTemplate.getStreamId().trim().isEmpty()) && 
                                    (newTemplate.getStreamListId() != null && 
                                    !newTemplate.getStreamListId().trim().isEmpty());
                
                log.info("模板保存成功，检查是否需要同步: streamId=[{}], streamListId=[{}], shouldSync=[{}]", 
                    newTemplate.getStreamId(), newTemplate.getStreamListId(), shouldSync);
                
                if (shouldSync) {
                    try {
                        log.info("开始同步模板到所有stream...");
                        syncTemplateToAllStreams(newTemplate);
                        log.info("模板同步完成");
                    } catch (Exception e) {
                        log.error("同步模板到所有stream失败: {}", e.getMessage(), e);
                        return R.fail("模板保存成功但同步失败: " + e.getMessage());
                    }
                }
                return R.data(newTemplate.getId());
            } else {
                return R.fail("保存模板失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("保存模板失败: " + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateTemplate.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateTemplate", notes = "updateTemplate")
    public R updateTemplate(@RequestBody NewTemplate newTemplate) {
        try {
            newTemplate.setStatus(0);
            newTemplateService.updateTemplate(newTemplate);
            return R.data(newTemplate.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteTemplate.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteTemplate", notes = "deleteTemplate")
    public R deleteTemplate(@RequestParam String id) {
        try {
            log.info("开始删除模板及其关联数据，templateId: {}", id);
            
            // 1. 删除关联的TemplateCustomData记录
            try {
                templateCustomDataService.deleteByTemplateId(id);
                log.info("成功删除TemplateCustomData关联数据，templateId: {}", id);
            } catch (Exception e) {
                log.error("删除TemplateCustomData关联数据失败，templateId: {}, error: {}", id, e.getMessage());
            }
            
            // 2. 删除关联的TemplateUpdate记录
            try {
                templateUpdateService.deleteByTemplateId(id);
                log.info("成功删除TemplateUpdate关联数据，templateId: {}", id);
            } catch (Exception e) {
                log.error("删除TemplateUpdate关联数据失败，templateId: {}, error: {}", id, e.getMessage());
            }
            
            // 3. 删除关联的TemplateData记录
            try {
                templateDataService.deleteByTemplateId(id);
                log.info("成功删除TemplateData关联数据，templateId: {}", id);
            } catch (Exception e) {
                log.error("删除TemplateData关联数据失败，templateId: {}, error: {}", id, e.getMessage());
            }
            
            // 4. 删除关联的TemplateVersion记录
            try {
                templateVersionService.deleteByTemplateId(id);
                log.info("成功删除TemplateVersion关联数据，templateId: {}", id);
            } catch (Exception e) {
                log.error("删除TemplateVersion关联数据失败，templateId: {}, error: {}", id, e.getMessage());
            }
            
            // 5. 删除关联的TemplatePartElement记录
            try {
                templatePartElementService.deleteTemplatePartElement(id);
                log.info("成功删除TemplatePartElement关联数据，templateId: {}", id);
            } catch (Exception e) {
                log.error("删除TemplatePartElement关联数据失败，templateId: {}, error: {}", id, e.getMessage());
            }
            
            // 6. 最后删除模板本身
            newTemplateService.deleteTemplate(id);
            log.info("成功删除模板，templateId: {}", id);
            
            return R.data(id);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    ----------外层指定模板管理------------
    @SneakyThrows
    @GetMapping("/getStreamList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getStreamList", notes = "getStreamList")
    public R<List<StreamList>> getStreamList() {
        try {
            return R.data(streamListService.getStreamList());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getStreamById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getStreamById", notes = "getStreamById")
    public R<StreamList> getStreamById(@RequestParam String id) {
        try {
            return R.data(streamListService.getStreamListById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addStream.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addStream", notes = "addStream")
    public R addStream(@RequestBody StreamList streamList) {
        try {
            streamListService.addStreamList(streamList);
            return R.data(streamList.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateStream.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateStream", notes = "updateStream")
    public R updateStream(@RequestBody StreamList streamList) {
        try {
            // 获取修改前的streamIds
            StreamList originalStreamList = streamListService.getStreamListById(streamList.getId());
            if (originalStreamList == null) {
                return R.fail("流水线列表不存在");
            }
            
            String[] originalStreamIds = originalStreamList.getStreamIds();
            String[] newStreamIds = streamList.getStreamIds();
            
            // 转换为Set便于比较
            Set<String> originalSet = originalStreamIds != null ? new HashSet<>(Arrays.asList(originalStreamIds)) : new HashSet<>();
            Set<String> newSet = newStreamIds != null ? new HashSet<>(Arrays.asList(newStreamIds)) : new HashSet<>();
            
            // 找出新增的streamId
            Set<String> addedStreamIds = new HashSet<>(newSet);
            addedStreamIds.removeAll(originalSet);
            
            // 找出删除的streamId
            Set<String> removedStreamIds = new HashSet<>(originalSet);
            removedStreamIds.removeAll(newSet);
            
            log.info("StreamList ID: {}, 新增的streamIds: {}, 删除的streamIds: {}", streamList.getId(), addedStreamIds, removedStreamIds);
            
            // 处理新增的streamId
            if (!addedStreamIds.isEmpty()) {
                processAddedStreamIds(addedStreamIds, streamList.getId());
            }
            
            // 处理删除的streamId
            if (!removedStreamIds.isEmpty()) {
                processRemovedStreamIds(removedStreamIds);
            }
            
            // 更新StreamList
            streamListService.updateStreamList(streamList);
            
            return R.data(streamList.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    /**
     * 处理新增的streamId - 复制模板
     */
    private void processAddedStreamIds(Set<String> addedStreamIds, String streamListId) {
        try {
            // 获取该streamListId下streamId为null的所有模板
            List<NewTemplate> templatesWithNullStreamId = newTemplateService.getTemplatesByStreamListIdWithNullStreamId(streamListId);
            
            if (templatesWithNullStreamId.isEmpty()) {
                log.info("没有找到streamId为null的模板，无需复制");
                return;
            }
            
            log.info("找到 {} 个streamId为null的模板需要复制", templatesWithNullStreamId.size());
            
            // 为每个新增的streamId复制模板
            for (String newStreamId : addedStreamIds) {
                log.info("为streamId {} 复制模板", newStreamId);
                boolean success = newTemplateService.batchCopyTemplatesWithRelations(templatesWithNullStreamId, newStreamId, streamListId);
                if (!success) {
                    throw new RuntimeException("复制模板失败，streamId: " + newStreamId);
                }
            }
            
            log.info("新增streamId的模板复制完成");
        } catch (Exception e) {
            log.error("处理新增streamId失败: {}", e.getMessage(), e);
            throw new RuntimeException("处理新增streamId失败: " + e.getMessage());
        }
    }

    /**
     * 处理删除的streamId - 删除模板（如果没有数据）
     * 新逻辑：如果某个模板有数据，保留该模板，并删除同一streamId下所有没有数据的模板
     */
    private void processRemovedStreamIds(Set<String> removedStreamIds) {
        try {
            for (String removedStreamId : removedStreamIds) {
                log.info("处理删除的streamId: {}", removedStreamId);
                
                // 获取该streamId对应的所有模板
                List<NewTemplate> templatesForStreamId = newTemplateService.getTemplatesByStreamId(removedStreamId);
                
                if (templatesForStreamId.isEmpty()) {
                    log.info("streamId {} 没有对应的模板", removedStreamId);
                    continue;
                }
                
                log.info("streamId {} 有 {} 个模板需要检查", removedStreamId, templatesForStreamId.size());
                
                // Step 1: 先检查所有模板的数据情况，分为有数据和无数据两组
                List<NewTemplate> templatesWithData = new ArrayList<>();
                List<NewTemplate> templatesWithoutData = new ArrayList<>();
                
                for (NewTemplate template : templatesForStreamId) {
                    boolean hasData = templateDataService.hasDataByTemplateId(template.getId());
                    
                    if (hasData) {
                        templatesWithData.add(template);
                        log.info("模板 {} (name: {}) 有数据", template.getId(), template.getName());
                    } else {
                        templatesWithoutData.add(template);
                        log.info("模板 {} (name: {}) 无数据", template.getId(), template.getName());
                    }
                }
                
                log.info("streamId {} 统计: 有数据的模板 {} 个, 无数据的模板 {} 个", 
                    removedStreamId, templatesWithData.size(), templatesWithoutData.size());
                
                // Step 2: 如果有任何一个模板有数据，则删除该streamId下所有无数据的模板
                if (!templatesWithData.isEmpty()) {
                    log.info("streamId {} 中有模板存在数据，开始删除无数据的模板", removedStreamId);
                    
                    for (NewTemplate template : templatesWithoutData) {
                        log.info("准备删除无数据的模板: {} (name: {})", template.getId(), template.getName());
                        boolean deleteSuccess = newTemplateService.deleteTemplate(template.getId());
                        if (!deleteSuccess) {
                            log.error("删除模板失败: {} (name: {})", template.getId(), template.getName());
                        } else {
                            log.info("成功删除无数据的模板: {} (name: {})", template.getId(), template.getName());
                        }
                    }
                } else {
                    // Step 3: 如果所有模板都没有数据，则全部删除
                    log.info("streamId {} 中所有模板都无数据，全部删除", removedStreamId);
                    
                    for (NewTemplate template : templatesWithoutData) {
                        log.info("准备删除模板: {} (name: {})", template.getId(), template.getName());
                        boolean deleteSuccess = newTemplateService.deleteTemplate(template.getId());
                        if (!deleteSuccess) {
                            log.error("删除模板失败: {} (name: {})", template.getId(), template.getName());
                        } else {
                            log.info("成功删除模板: {} (name: {})", template.getId(), template.getName());
                        }
                    }
                }
            }
            
            log.info("删除streamId的模板处理完成");
        } catch (Exception e) {
            log.error("处理删除streamId失败: {}", e.getMessage(), e);
            throw new RuntimeException("处理删除streamId失败: " + e.getMessage());
        }
    }

    /**
     * 将新增的模板同步到StreamList中的所有streamIds
     * @param newTemplate 新增的模板（streamId为空，streamListId不为空）
     */
    private void syncTemplateToAllStreams(NewTemplate newTemplate) {
        try {
            log.info("=== 开始同步新增模板到所有streamIds ===");
            log.info("新增模板: ID={}, Name={}, PID={}", newTemplate.getId(), newTemplate.getName(), newTemplate.getPid());
            
            // 获取StreamList信息
            StreamList streamList = streamListService.getStreamListById(newTemplate.getStreamListId());
            if (streamList == null) {
                log.warn("StreamList不存在: {}", newTemplate.getStreamListId());
                return;
            }
            
            String[] streamIds = streamList.getStreamIds();
            if (streamIds == null || streamIds.length == 0) {
                log.info("StreamList {} 没有配置streamIds，无需同步", newTemplate.getStreamListId());
                return;
            }
            
            log.info("需要同步到的streamIds: {}", Arrays.toString(streamIds));
            
            // 为每个streamId创建新模板的副本
            for (String streamId : streamIds) {
                log.info("开始为streamId {} 创建模板副本", streamId);
                
                // Step 1: 创建新模板副本
                NewTemplate copyTemplate = new NewTemplate();
                copyTemplate.setId(UUID.randomUUID().toString());
                copyTemplate.setFileId(newTemplate.getFileId());
                copyTemplate.setTemplateId(newTemplate.getTemplateId());
                copyTemplate.setStreamListId(newTemplate.getStreamListId());
                copyTemplate.setStreamId(streamId); // 设置为目标streamId
                copyTemplate.setBranchId(newTemplate.getBranchId());
                copyTemplate.setBlockId(newTemplate.getBlockId());
                copyTemplate.setName(newTemplate.getName());
                copyTemplate.setType(newTemplate.getType());
                copyTemplate.setDescription(newTemplate.getDescription());
                copyTemplate.setAuth(newTemplate.getAuth());
                copyTemplate.setSource(newTemplate.getSource());
                copyTemplate.setVersion(newTemplate.getVersion());
                copyTemplate.setDetail(newTemplate.getDetail());
                copyTemplate.setModifyUser(newTemplate.getModifyUser());
                copyTemplate.setModifyTime(new Date());
                copyTemplate.setStatus(newTemplate.getStatus());
                
                // Step 2: 处理父子关系
                if (newTemplate.getPid() != null && !newTemplate.getPid().equals("0")) {
                    // 根据父模板name找到对应streamId的父模板
                    String parentPid = findParentTemplateIdByName(newTemplate.getPid(), streamId, newTemplate.getStreamListId());
                    copyTemplate.setPid(parentPid != null ? parentPid : "0");
                    log.info("设置父子关系: {} -> PID={}", newTemplate.getName(), copyTemplate.getPid());
                } else {
                    copyTemplate.setPid("0");
                    log.info("设置为根节点: {} -> PID=0", newTemplate.getName());
                }
                
                // Step 3: 保存新模板
                boolean saved = newTemplateService.saveTemplate(copyTemplate);
                if (saved) {
                    log.info("成功为streamId {} 创建模板副本: {}", streamId, copyTemplate.getId());
                } else {
                    throw new RuntimeException("为streamId " + streamId + " 创建模板副本失败");
                }
            }
            
            log.info("=== 模板同步完成 ===");
            
        } catch (Exception e) {
            log.error("同步模板失败: {}", e.getMessage(), e);
            throw new RuntimeException("同步模板失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据父模板ID找到对应名称，然后在指定streamId中查找同名父模板的ID
     */
    private String findParentTemplateIdByName(String originalParentId, String targetStreamId, String streamListId) {
        try {
            // Step 1: 根据原父模板ID获取父模板信息
            NewTemplate originalParent = newTemplateService.getTemplateById(originalParentId);
            if (originalParent == null) {
                log.warn("找不到原父模板: {}", originalParentId);
                return null;
            }
            
            // Step 2: 在目标streamId中查找同名的模板
            List<NewTemplate> targetStreamTemplates = newTemplateService.getTemplatesByStreamId(targetStreamId);
            for (NewTemplate template : targetStreamTemplates) {
                if (template.getName().equals(originalParent.getName()) && 
                    template.getType().equals(originalParent.getType())) {
                    log.info("找到父模板: {} -> {}", originalParent.getName(), template.getId());
                    return template.getId();
                }
            }
            
            log.warn("在streamId {} 中找不到名为 {} 的父模板", targetStreamId, originalParent.getName());
            return null;
            
        } catch (Exception e) {
            log.error("查找父模板失败: {}", e.getMessage(), e);
            return null;
        }
    }


    @SneakyThrows
    @PostMapping("/deleteStream.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteStream", notes = "deleteStream")
    public R deleteStream(@RequestParam String id) {
        try {
            newTemplateService.getNewTemplateByStreamListId(id).forEach(newTemplate -> {
                newTemplateService.deleteTemplate(newTemplate.getId());
            });
            streamListService.deleteStreamList(id);
            return R.data(id);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    ---------构件模板关联------------
    @SneakyThrows
    @GetMapping("/getElementByTemplateId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getElementByTemplateId", notes = "getElementByTemplateId")
    public R<TemplatePartElement> getElementByTemplateId(@RequestParam String templateId) {
        try {
            return R.data(templatePartElementService.getByTemplateId(templateId));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addElement.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addElement", notes = "addElement")
    public R addElement(@RequestBody Map<String, Object> params) {
        try {

            
            String templateId = (String) params.get("templateId");
            String streamId = (String) params.get("streamId");
            String branchId = (String) params.get("branchId");
            if (StringUtils.isEmpty(templateId)) {
                return R.fail("模板ID不能为空");
            }
            
            String type = (String) params.get("type");
            
            // 从请求中获取element数组并转换为JSON字符串
            Object elementObj = params.get("element");
            String elementJson = JSON.toJSONString(elementObj);
            
            // 创建TemplatePartElement对象
            TemplatePartElement templatePartElement = new TemplatePartElement();
            templatePartElement.setId(UUID.randomUUID().toString());
            templatePartElement.setTemplateId(templateId);
            templatePartElement.setElement(elementJson);
            templatePartElement.setCreateTime(new Date());


            
            boolean result;
                result = templatePartElementService.addTemplatePartElement(templatePartElement);
            
            if (result) {
                // 新增：处理TemplateData更新逻辑
                try {
                    updateTemplateDataFromExpInstances(streamId,branchId,templateId, type, elementObj);
                } catch (Exception e) {
                    log.error("更新TemplateData失败: {}", e.getMessage(), e);
                    // 不影响主流程
                }
                
                // 触发模板检查 - 由于没有streamId和branchId参数，传递null
                try {
                    templateUpdateController.checkTemplateAndSave(templateId, streamId,branchId, "addElement");
                } catch (Exception e) {
                    // 记录错误但不影响主要功能
                    log.error("模板检查触发失败: {}", e.getMessage());
                }
                
                return R.data(templatePartElement.getId());
            } else {
                return R.fail("操作失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateElement.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateElement", notes = "updateElement")
    public R updateElement(@RequestBody TemplatePartElement templatePartElement) {
        try {
            templatePartElementService.updateById(templatePartElement);
            return R.data(templatePartElement.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteElement.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteElement", notes = "deleteElement")
    public R deleteElement(@RequestParam String id) {
        try {
            templatePartElementService.removeById(id);
            return R.data(id);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    模板blockId关联

    @SneakyThrows
    @PostMapping("/addBlockId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addBlockId", notes = "addBlockId")
    public R addBlockId( @RequestParam String templateId,
                         @RequestParam String type,
                         @RequestParam(required = false) String branchId,
                         @RequestParam String blockName) {
        try {
            NewTemplate templateById = newTemplateService.getTemplateById(templateId);
            templateById.setBlockId(blockName);
            boolean b = newTemplateService.updateTemplate(templateById);
            if (!b){
                return R.fail("绑定地块号失败！");
            }
            boolean c = templateDataService.saveMergedData(templateId, type, branchId, blockName);
            if (!c){
                return R.fail("初始化数据失败！");
            }
            return R.data(templateId);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getBlockId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getBlockId", notes = "getBlockId")
    public R getBlockId(@RequestParam String templateId) {
        try {
            NewTemplate templateById = newTemplateService.getTemplateById(templateId);
            return R.data(templateById.getBlockId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    ---------模板文件上传------------
    @SneakyThrows
    @PostMapping("/uploadTemplateFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadTemplateFile", notes = "uploadTemplateFile")
    public R uploadTemplateFile(@RequestHeader("Authorization") String token,@RequestParam("file") MultipartFile file,@RequestParam String type) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }

            Map<String,String> map = new HashMap<>();
            map.put("type",type);
            map.put("modifyUser",user.getName());
            BomFile bomFile = fileService.uploadBomFile(file, map);
            if (bomFile != null) {
                return R.data(bomFile);
            }else {
                return R.fail("文件上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    根据type获取最新已上传模板文件
    @SneakyThrows
    @GetMapping("/getLatestTemplateFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getLatestTemplateFile", notes = "getLatestTemplateFile")
    public R<List<BomFile>> getLatestTemplateFile() {
        try {
            List<BomFile> bomFile = bomFileService.getLatestTemplateFile();
            return R.data(bomFile);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    获取文件路径
    @SneakyThrows
    @GetMapping("/getFilePath.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getFilePath", notes = "getFilePath")
    public R<String> getFilePath(@RequestParam String id){
        try {
            BomFile file = bomFileService.getById(id);
            return R.data(file.getUrl());
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }

    // 解析文件
//    解析PCMES2.0标准格式BOM表头文件
    @SneakyThrows
    @GetMapping("/parseTemplateFileToJson.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "解析模板文件为JSON", notes = "解析模板文件为JSON")
    public R parseTemplateFileToJson(@RequestParam String id,@RequestParam String type) {
        InputStream inputStream = null;
        try {
            BomFile bomFile = new BomFile();
            // 1. 通过模板id获取文件信息
            NewTemplate templateById = newTemplateService.getTemplateById(id);
            if (templateById == null) {
                return R.fail("模板不存在");
            }

            //解析自定义模板 (type=4 支持多子表和复杂表头)
            if (type.equals("4")) {
              bomFile = bomFileService.getById(templateById.getFileId());
                if (bomFile == null) {
                    return R.fail("文件不存在");
                }
            }else {
                bomFile = bomFileService.getByType(type);
            if (bomFile == null) {
                return R.fail("文件不存在");
            }
            }
                // 2. 获取文件URL并下载
                String fileUrl = bomFile.getUrl();
                try {
                    URL url = new URL(fileUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    inputStream = conn.getInputStream();

                    // 3. 解析Excel文件
                    String fileName = bomFile.getName();
                    if (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls")) {
                        return R.fail("不支持的文件类型，仅支持Excel文件");
                    }

                    // 使用Apache POI处理Excel文件
                    Workbook workbook = WorkbookFactory.create(inputStream);

                    // 构建最终的结果对象
                    Map<String, Object> result = new HashMap<>();
                    result.put("name", fileName.substring(0, fileName.lastIndexOf('.')));

                    List<Map<String, Object>> sheetsList = new ArrayList<>();

                    log.info("=== 开始解析模板，共{}个工作表 ===", workbook.getNumberOfSheets());

                    // 遍历所有工作表
                    for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                        Sheet sheet = workbook.getSheetAt(sheetIndex);
                        if (sheet == null) {
                            continue;
                        }

                        String sheetName = sheet.getSheetName();
                        log.info("开始解析工作表: {}", sheetName);

                        Map<String, Object> sheetInfo = new HashMap<>();
                        sheetInfo.put("name", sheetName);

                        // 使用多级表头解析方法解析当前工作表
                        List<Map<String, Object>> headerData = parseMultiLevelHeaders(sheet);

                        log.info("工作表 {} 解析完成，共解析出{}个表头字段", sheetName, headerData.size());

                        sheetInfo.put("data", headerData);
                        sheetsList.add(sheetInfo);
                    }

                    result.put("sheets", sheetsList);

                    workbook.close();
                    log.info("===模板解析完成 ===");
                    return R.data(result);
                } finally {
                    // 4. 确保资源释放
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            // 忽略关闭异常
                        }
                    }
                }
//            }
            // type不为4时，处理多级合并单元格
//            BomFile bomFile = bomFileService.getByType(type);
//            if (bomFile == null) {
//                return R.fail("文件不存在");
//            }
//
//            // 2. 获取文件URL并下载
//            String fileUrl = bomFile.getUrl();
//            try {
//                URL url = new URL(fileUrl);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                inputStream = conn.getInputStream();
//
//                // 3. 解析Excel文件
//                String fileName = bomFile.getName();
//                if (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls")) {
//                    return R.fail("不支持的文件类型，仅支持Excel文件");
//                }
//
//                // 使用Apache POI处理Excel文件
//                Workbook workbook = WorkbookFactory.create(inputStream);
//
//                // 使用第一个工作表
//                Sheet sheet = workbook.getSheetAt(0);
//                if (sheet == null) {
//                    return R.fail("无法读取Excel工作表");
//                }
//
//                // 解析多级表头结构
//                List<Map<String, Object>> result = parseMultiLevelHeaders(sheet);
//
//                workbook.close();
//                return R.data(result);
//            } finally {
//                // 4. 确保资源释放
//                if (inputStream != null) {
//                    try {
//                        inputStream.close();
//                    } catch (IOException e) {
//                        // 忽略关闭异常
//                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("解析文件失败: " + e.getMessage());
        }
    }

    /**
     * 检查行是否包含非空单元格
     */
    private boolean hasNonEmptyCell(Row row) {
        if (row == null) {
            return false;
        }

        for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
            Cell cell = row.getCell(colIndex);
            if (cell != null) {
                String cellValue = getCellValueAsString(cell);
                if (cellValue != null && !cellValue.trim().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取单元格的值（字符串形式）
     * @param cell 单元格
     * @return 单元格的值
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                    } else {
                        // 避免科学计数法
                        double doubleValue = cell.getNumericCellValue();
                        if (doubleValue == Math.floor(doubleValue)) {
                            return String.valueOf((long) doubleValue);
                        } else {
                            return String.valueOf(doubleValue);
                        }
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    try {
                        return cell.getStringCellValue();
                    } catch (Exception e) {
                        try {
                            return String.valueOf(cell.getNumericCellValue());
                        } catch (Exception e2) {
                            return cell.getCellFormula();
                        }
                    }
                default:
                    return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 解析多级表头结构
     * 支持任意级别的合并单元格嵌套
     * @param sheet Excel工作表
     * @return 解析后的表头结构
     */
    private List<Map<String, Object>> parseMultiLevelHeaders(Sheet sheet) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 先找到实际的表头区域（前15行内查找）
        int maxHeaderRows = Math.min(15, sheet.getLastRowNum() + 1);
        
        // 查找包含表头数据的行范围
        int firstHeaderRow = -1;
        int lastHeaderRow = -1;
        
        // 调试：打印前15行的内容
        System.out.println("=== Excel文件前15行内容分析 ===");
        for (int rowIndex = 0; rowIndex < maxHeaderRows; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                StringBuilder rowContent = new StringBuilder();
                boolean hasContent = false;
                for (int colIndex = 0; colIndex < Math.min(10, row.getLastCellNum()); colIndex++) {
                    Cell cell = row.getCell(colIndex);
                    String cellValue = getCellValueAsString(cell);
                    if (cellValue != null && !cellValue.trim().isEmpty()) {
                        rowContent.append("[").append(cellValue.trim()).append("] ");
                        hasContent = true;
                    }
                }
                if (hasContent) {
                    boolean isMeaningful = hasMeaningfulContent(row);
                    System.out.println("第" + (rowIndex + 1) + "行 (有意义:" + isMeaningful + "): " + rowContent.toString());
                }
            }
        }
        
        // 改进的表头识别逻辑
        for (int rowIndex = 0; rowIndex < maxHeaderRows; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null && isActualHeaderRow(row, rowIndex)) {
                if (firstHeaderRow == -1) {
                    firstHeaderRow = rowIndex;
                }
                lastHeaderRow = rowIndex;
            }
        }
        
        System.out.println("识别的表头行范围: " + firstHeaderRow + " 到 " + lastHeaderRow);
        
        if (firstHeaderRow == -1) {
            return result;
        }
        
        // 如果只有一行表头，简单处理
        if (firstHeaderRow == lastHeaderRow) {
            result = parseSingleRowHeaders(sheet, firstHeaderRow);
        } else {
            // 多行表头，使用合并单元格逻辑
            result = parseMultiRowHeaders(sheet, firstHeaderRow, lastHeaderRow);
        }
        
        // 对叶节点进行路径拼接处理
        result = processLeafNodePaths(result);
        
        System.out.println("最终解析结果数量: " + result.size());
        return result;
    }

    private String extractFileNameWithoutExtension(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return fileName;
        }
        int lastSlash = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        String namePart = lastSlash >= 0 ? fileName.substring(lastSlash + 1) : fileName;
        int lastDot = namePart.lastIndexOf('.');
        return lastDot > 0 ? namePart.substring(0, lastDot) : namePart;
    }
    
    /**
     * 处理叶节点路径拼接
     * 对于没有children的最末端节点，将其field和title拼接上完整的父级路径
     * @param headerList 表头列表
     * @return 处理后的表头列表
     */
    private List<Map<String, Object>> processLeafNodePaths(List<Map<String, Object>> headerList) {
        if (headerList == null || headerList.isEmpty()) {
            return headerList;
        }
        
        // 递归处理每个顶级节点
        for (Map<String, Object> header : headerList) {
            processNodePaths(header, new ArrayList<>());
        }
        
        return headerList;
    }
    
    /**
     * 递归处理节点路径
     * @param node 当前节点
     * @param parentPath 父级路径列表
     */
    private void processNodePaths(Map<String, Object> node, List<String> parentPath) {
        if (node == null) return;
        
        String currentField = (String) node.get("field");
        if (currentField == null) return;
        
        // 构建当前路径（包含当前节点）
        List<String> currentPath = new ArrayList<>(parentPath);
        currentPath.add(currentField);
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> children = (List<Map<String, Object>>) node.get("children");
        
        if (children == null || children.isEmpty()) {
            // 这是叶节点，需要拼接完整路径
            if (currentPath.size() > 1) { // 只有当有父级路径时才拼接
                String fullPath = String.join("-", currentPath);
                node.put("field", fullPath);
                node.put("title", fullPath);
                System.out.println("叶节点路径拼接: " + currentField + " -> " + fullPath);
            }
        } else {
            // 这是中间节点，递归处理子节点
            for (Map<String, Object> child : children) {
                processNodePaths(child, currentPath);
            }
        }
    }
    
    /**
     * 检查行是否包含有意义的内容（非空且不是纯数字行）
     */
    private boolean hasMeaningfulContent(Row row) {
        if (row == null) return false;
        
        int cellCount = 0;
        int textCellCount = 0;
        
        for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
            Cell cell = row.getCell(colIndex);
            if (cell != null) {
                String value = getCellValueAsString(cell);
                if (value != null && !value.trim().isEmpty()) {
                    cellCount++;
                    // 包含中文、英文字母或常见表头词汇的认为是文本
                    if (value.matches(".*[\\u4e00-\\u9fa5a-zA-Z].*") || 
                        value.contains("编码") || value.contains("类型") || 
                        value.contains("名称") || value.contains("规格") ||
                        value.contains("单位") || value.contains("数量")) {
                        textCellCount++;
                    }
                }
            }
        }
        
        // 至少有3个非空单元格，且80%以上是文本内容
        return cellCount >= 3 && textCellCount >= Math.max(1, cellCount * 0.8);
    }
    
    /**
     * 解析单行表头
     */
    private List<Map<String, Object>> parseSingleRowHeaders(Sheet sheet, int headerRow) {
        List<Map<String, Object>> headers = new ArrayList<>();
        Row row = sheet.getRow(headerRow);
        
        if (row == null) return headers;
        
        System.out.println("=== 解析第" + (headerRow + 1) + "行的单行表头 ===");
        
        for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
            Cell cell = row.getCell(colIndex);
            if (cell != null) {
                String cellValue = getCellValueAsString(cell);
                if (cellValue != null && !cellValue.trim().isEmpty()) {
                    Map<String, Object> header = new HashMap<>();
                    header.put("field", cellValue.trim());
                    header.put("title", cellValue.trim());
                    headers.add(header);
                    System.out.println("添加表头字段: " + cellValue.trim());
                }
            }
        }
        
        System.out.println("单行表头解析完成，共" + headers.size() + "个字段");
        return headers;
    }
    
    /**
     * 解析多行表头（支持合并单元格）
     */
    private List<Map<String, Object>> parseMultiRowHeaders(Sheet sheet, int startRow, int endRow) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        System.out.println("=== 解析多行表头，从第" + (startRow + 1) + "行到第" + (endRow + 1) + "行 ===");
        
        // 获取合并单元格信息
        Map<String, CellRangeAddress> mergedCellMap = new HashMap<>();
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.getFirstRow() >= startRow && region.getLastRow() <= endRow) {
                System.out.println("发现合并单元格: 行" + (region.getFirstRow() + 1) + "-" + (region.getLastRow() + 1) + 
                    ", 列" + (region.getFirstColumn() + 1) + "-" + (region.getLastColumn() + 1));
                for (int row = region.getFirstRow(); row <= region.getLastRow(); row++) {
                    for (int col = region.getFirstColumn(); col <= region.getLastColumn(); col++) {
                        mergedCellMap.put(row + "_" + col, region);
                    }
                }
            }
        }
        
        // 找到最大列数
        int maxCol = 0;
        for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null && row.getLastCellNum() > maxCol) {
                maxCol = row.getLastCellNum();
            }
        }
        
        System.out.println("最大列数: " + maxCol + ", 合并单元格数量: " + mergedCellMap.size() / 4); // 除以4是因为每个合并区域会产生多个键值对
        
        // 从第一行开始，构建表头结构
        result = buildHeaderStructure(sheet, startRow, endRow, 0, maxCol - 1, mergedCellMap, new HashSet<>());
        
        System.out.println("多行表头解析完成，共" + result.size() + "个顶级字段");
        return result;
    }
    
    /**
     * 递归构建表头结构
     */
    private List<Map<String, Object>> buildHeaderStructure(Sheet sheet, int currentRow, int endRow, 
                                                          int startCol, int endCol, 
                                                          Map<String, CellRangeAddress> mergedCellMap,
                                                          Set<String> processedCells) {
        List<Map<String, Object>> headers = new ArrayList<>();
        
        if (currentRow > endRow || startCol > endCol) {
            return headers;
        }
        
        for (int colIndex = startCol; colIndex <= endCol; colIndex++) {
            String cellKey = currentRow + "_" + colIndex;
            
            // 跳过已处理的单元格
            if (processedCells.contains(cellKey)) {
                continue;
            }
            
            Row row = sheet.getRow(currentRow);
            if (row == null) continue;
            
            Cell cell = row.getCell(colIndex);
            String cellValue = getCellValueAsString(cell);
            
            if (cellValue == null || cellValue.trim().isEmpty()) {
                continue;
            }
            
            Map<String, Object> header = new HashMap<>();
            header.put("field", cellValue.trim());
            header.put("title", cellValue.trim());
            
            // 检查是否是合并单元格
            CellRangeAddress mergedRegion = mergedCellMap.get(cellKey);
            if (mergedRegion != null) {
                // 标记合并区域内的所有单元格为已处理
                for (int r = mergedRegion.getFirstRow(); r <= mergedRegion.getLastRow(); r++) {
                    for (int c = mergedRegion.getFirstColumn(); c <= mergedRegion.getLastColumn(); c++) {
                        processedCells.add(r + "_" + c);
                    }
                }
                
                // 如果合并单元格只跨列不跨行，检查下一行是否有子表头
                if (mergedRegion.getFirstRow() == mergedRegion.getLastRow() && currentRow < endRow) {
                    List<Map<String, Object>> children = buildHeaderStructure(sheet, currentRow + 1, endRow,
                        mergedRegion.getFirstColumn(), mergedRegion.getLastColumn(), 
                        mergedCellMap, processedCells);
                    
                    if (!children.isEmpty()) {
                        header.put("children", children);
                    }
                }
                
                // 跳过合并区域的其他列
                colIndex = mergedRegion.getLastColumn();
            } else {
                // 单个单元格，标记为已处理
                processedCells.add(cellKey);
                
                // 检查下一行是否有对应的子表头
                if (currentRow < endRow) {
                    List<Map<String, Object>> children = buildHeaderStructure(sheet, currentRow + 1, endRow,
                        colIndex, colIndex, mergedCellMap, processedCells);
                    
                    if (!children.isEmpty()) {
                        header.put("children", children);
                    }
                }
            }
            
            headers.add(header);
        }
        
        return headers;
    }
    
//    删除文件

    @SneakyThrows
    @PostMapping("/deleteBomFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteBomFile", notes = "deleteBomFile")
    public R deleteBomFile(@RequestParam String id) {
        try {
            BomFile file = bomFileService.getById(id);
            String fileurl = fileService.extractPath(file.getUrl());
            //删服务器
            boolean b = fileService.removeFile(fileurl,"bom-files");
            //删数据库
            boolean c = bomFileService.deleteBomFile(id);
            if (b&&c) {
                return R.data(id);
            }else {
                return R.fail("删除文件失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/downloadTemplate.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "下载智链自定义数据模板", notes = "下载智链自定义数据模板文件")
    public void downloadTemplate(@RequestParam(value = "fileName", defaultValue = "智链自定义数据模板.xlsx") String fileName,
                                 HttpServletResponse response) {
        try {
            fileService.downloadTemplate(fileName, response);
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

//    ----------revit模型回传----------
//    @SneakyThrows
//    @PostMapping("/uploadRevitModel.do")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "uploadRevitModel", notes = "uploadRevitModel")
//    public R uploadRevitModel(@RequestParam String templateId){
//        try {
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return R.fail(e.getMessage());
//        }
//    }

    @SneakyThrows
    @GetMapping("/getCheckTemplateList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "获取模板版本检查历史列表", notes = "根据templateId获取版本检查历史")
    public R<List<TemplateVersion>> getCheckTemplateList(@RequestParam String templateId) {
        try {
            List<TemplateVersion> versionList = templateVersionService.getByTemplateId(templateId);
            return R.data(versionList);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    /**
     * 解析Excel数据文件为JSON格式
     * 
     * @param file 上传的Excel文件
     * @param id 模板ID
     * @param type 模板类型
     * @return 解析后的JSON数据
     */
    @SneakyThrows
    @PostMapping("/parseExcelDataToJson.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "解析Excel数据文件为JSON", notes = "根据模板表头解析Excel数据文件")
    public R parseExcelDataToJson(@RequestParam("file") MultipartFile file, 
                                  @RequestParam String id, 
                                  @RequestParam String type) {
        InputStream inputStream = null;
        try {
            // 1. 校验上传的Excel文件
            if (file == null || file.isEmpty()) {
                return R.fail("请上传文件");
            }
            String branchId = newTemplateService.getTemplateById(id).getBranchId();

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls"))) {
                return R.fail("不支持的文件类型，仅支持Excel文件");
            }

            // 2. 先调用parseTemplateFileToJson获取模板表头信息
            R templateHeaderResult = parseTemplateFileToJson(id, type);
            if (!templateHeaderResult.isSuccess()) {
                return R.fail("获取模板表头信息失败: " + templateHeaderResult.getMsg());
            }
            
            // 从templateHeaderResult获取表头信息
            Map<String, Object> templateData = (Map<String, Object>) templateHeaderResult.getData();
            List<Map<String, Object>> sheets = (List<Map<String, Object>>) templateData.get("sheets");
            
            // 构建表头字段映射表（工作表名 -> 字段列表）
            Map<String, List<String>> sheetHeadersMap = new HashMap<>();
            if (sheets != null) {
                for (Map<String, Object> sheet : sheets) {
                    String sheetName = (String) sheet.get("name");
                    List<Map<String, Object>> headerData = (List<Map<String, Object>>) sheet.get("data");
                    List<String> headerFields = new ArrayList<>();
                    
                    if (headerData != null) {
                        for (Map<String, Object> header : headerData) {
                            String field = (String) header.get("field");
                            if (field != null && !field.trim().isEmpty()) {
                                headerFields.add(field.trim());
                            }
                        }
                    }
                    sheetHeadersMap.put(sheetName, headerFields);
                }
            }

            // 3. 解析上传的Excel文件数据
            inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);

            List<Map<String, Object>> resultData = new ArrayList<>();

            // 遍历所有工作表
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                if (sheet == null) {
                    continue;
                }

                String currentSheetName = sheet.getSheetName();
                List<String> headerFields = sheetHeadersMap.get(currentSheetName);
                
                // 如果模板中没有对应的工作表，跳过
                if (headerFields == null || headerFields.isEmpty()) {
                    continue;
                }

                // 查找表头行
                Row headerRow = null;
                int headerRowIndex = -1;
                for (int rowIndex = 0; rowIndex <= 10; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null && hasNonEmptyCell(row)) {
                        headerRow = row;
                        headerRowIndex = rowIndex;
                        break;
                    }
                }

                if (headerRow == null) {
                    continue;
                }

                // 建立列索引与字段名的映射
                Map<Integer, String> columnToFieldMap = new HashMap<>();
                for (int colIndex = 0; colIndex < headerRow.getLastCellNum(); colIndex++) {
                    Cell cell = headerRow.getCell(colIndex);
                    if (cell != null) {
                        String cellValue = getCellValueAsString(cell);
                        if (cellValue != null && !cellValue.trim().isEmpty()) {
                            String trimmedValue = cellValue.trim();
                            // 检查是否在模板表头中存在
                            if (headerFields.contains(trimmedValue)) {
                                columnToFieldMap.put(colIndex, trimmedValue);
                            }
                        }
                    }
                }

                // 解析数据行（从表头行的下一行开始）
                for (int rowIndex = headerRowIndex + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row dataRow = sheet.getRow(rowIndex);
                    if (dataRow == null || !hasNonEmptyCell(dataRow)) {
                        continue;
                    }

                    Map<String, Object> rowData = new LinkedHashMap<>();
                    
                    // 添加基础字段
                    String elementId = UUID.randomUUID().toString();
                    rowData.put("elementId", elementId);
                    
                    String objectId = generateObjectId(currentSheetName, rowIndex);
                    rowData.put("objectId", objectId);
                    
                    String partId = generatePartId(currentSheetName, rowIndex);
                    rowData.put("partId", partId);
                    
                    rowData.put("sheetName", currentSheetName);
                    rowData.put("rowIndex", rowIndex);

                    // 添加动态字段（根据表头映射）
                    for (Map.Entry<Integer, String> entry : columnToFieldMap.entrySet()) {
                        int colIndex = entry.getKey();
                        String fieldName = entry.getValue();
                        
                        Cell cell = dataRow.getCell(colIndex);
                        Object cellValue = null;
                        
                        if (cell != null) {
                            // 根据单元格类型获取值
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        cellValue = cell.getDateCellValue();
                                    } else {
                                        double numValue = cell.getNumericCellValue();
                                        // 如果是整数，返回整数；否则返回小数
                                        if (numValue == Math.floor(numValue)) {
                                            cellValue = (long) numValue;
                                        } else {
                                            cellValue = numValue;
                                        }
                                    }
                                    break;
                                case STRING:
                                    cellValue = cell.getStringCellValue().trim();
                                    break;
                                case BOOLEAN:
                                    cellValue = cell.getBooleanCellValue();
                                    break;
                                case FORMULA:
                                    try {
                                        cellValue = cell.getNumericCellValue();
                                        if (cellValue.equals(Math.floor((Double) cellValue))) {
                                            cellValue = ((Double) cellValue).longValue();
                                        }
                                    } catch (Exception e) {
                                        cellValue = cell.getStringCellValue().trim();
                                    }
                                    break;
                                default:
                                    cellValue = "";
                                    break;
                            }
                        } else {
                            cellValue = "";
                        }
                        
                        rowData.put(fieldName, cellValue);
                    }

                    resultData.add(rowData);
                }
            }

            workbook.close();
            
            // 构建最终返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("data", resultData);
            
            // 将解析的JSON数据保存到sp_template_data表中
            try {
                // 先删除已存在的相同templateId和type的记录
                templateDataService.deleteByTemplateIdAndType(id, type, null);
                
                // 创建新的TemplateData记录
                TemplateData templateDatam = new TemplateData();
                templateDatam.setTemplateId(id);
                templateDatam.setType(type);
                templateDatam.setBranchId(branchId);
                
                // 将resultData转换为List<JSONObject>
                List<JSONObject> dataList = new ArrayList<>();
                for (Map<String, Object> rowData : resultData) {
                    JSONObject jsonObject = new JSONObject(rowData);
                    dataList.add(jsonObject);
                }
                templateDatam.setData(dataList);
                
                // 保存到数据库
                boolean saveResult = templateDataService.addTemplateData(templateDatam);
                if (saveResult) {
                    log.info("Excel数据已成功保存到数据库，templateId: {}, type: {}, 记录数: {}", 
                             id, type, dataList.size());
                } else {
                    log.warn("Excel数据保存到数据库失败，templateId: {}, type: {}", id, type);
                }
            } catch (Exception e) {
                log.error("保存Excel数据到数据库时发生异常，templateId: {}, type: {}", id, type, e);
                // 数据库保存失败不影响接口返回，仅记录日志
            }
            
            return R.data(result);

        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("解析Excel文件失败: " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // 忽略关闭异常
                }
            }
        }
    }


    /**
     * 生成objectId
     */
    private String generateObjectId(String sheetName, int rowIndex) {
        String input = sheetName + "_" + rowIndex + "_" + System.currentTimeMillis();
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

    /**
     * 生成partId
     */
    private String generatePartId(String sheetName, int rowIndex) {
        return "V1-" + sheetName.substring(0, Math.min(sheetName.length(), 3)).toUpperCase() + 
               String.format("%03d", rowIndex);
    }
    
    /**
     * 判断是否是实际的表头行（更精确的判断）
     */
    private boolean isActualHeaderRow(Row row, int rowIndex) {
        if (row == null) return false;
        
        int cellCount = 0;
        int headerKeywordCount = 0;
        List<String> cellValues = new ArrayList<>();
        
        for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
            Cell cell = row.getCell(colIndex);
            if (cell != null) {
                String value = getCellValueAsString(cell);
                if (value != null && !value.trim().isEmpty()) {
                    cellCount++;
                    cellValues.add(value.trim());
                    
                    // 检查是否包含典型的表头关键词
                    String lowerValue = value.toLowerCase();
                    if (lowerValue.contains("编码") || lowerValue.contains("类型") || 
                        lowerValue.contains("名称") || lowerValue.contains("规格") ||
                        lowerValue.contains("单位") || lowerValue.contains("数量") ||
                        lowerValue.contains("编号") || lowerValue.contains("信息") ||
                        lowerValue.contains("尺寸") || lowerValue.contains("定位") ||
                        lowerValue.contains("material") || lowerValue.contains("code") ||
                        lowerValue.contains("type") || lowerValue.contains("name") ||
                        lowerValue.contains("spec") || lowerValue.contains("unit") ||
                        lowerValue.contains("构件") || lowerValue.contains("部位") ||
                        lowerValue.contains("钢筋") || lowerValue.contains("混凝土") ||
                        lowerValue.contains("楼层") || lowerValue.contains("高度") ||
                        lowerValue.contains("宽度") || lowerValue.contains("厚度") ||
                        value.matches(".*[A-Z][0-9]+.*") || // 如A1, B2等列标识
                        value.length() <= 15) { // 短文本更可能是表头
                        headerKeywordCount++;
                    }
                }
            }
        }
        
        // 表头行的特征：至少有2个非空单元格，且大部分内容符合表头特征
        // 降低要求：从4个单元格降低到2个，以支持字段较少的表头
        boolean isHeader = cellCount >= 2 && headerKeywordCount >= Math.max(1, cellCount * 0.5);
        
        if (isHeader) {
            System.out.println("识别为表头行第" + (rowIndex + 1) + "行: " + String.join(", ", cellValues));
        }
        
        return isHeader;
    }
    
    /**
     * 从ExpInstances获取parameters数据并更新TemplateData
     * @param templateId 模板ID
     * @param type 类型
     * @param elementObj element数组对象
     */
    private void updateTemplateDataFromExpInstances(String streamId,String branchId,String templateId, String type, Object elementObj) {
        try {
            // 1. 解析element数组
            List<Map<String, Object>> elementList = new ArrayList<>();
            if (elementObj instanceof List) {
                elementList = (List<Map<String, Object>>) elementObj;
            } else if (elementObj instanceof String) {
                elementList = JSON.parseObject((String) elementObj, List.class);
            } else {
                elementList = JSON.parseObject(JSON.toJSONString(elementObj), List.class);
            }
            
            if (elementList == null) {
                elementList = new ArrayList<>();
            }
            
            // 特殊处理：element为空数组的情况（全部解绑）
            boolean isFullUnbind = elementList.isEmpty();
            if (isFullUnbind) {
                log.info("检测到全部解绑操作（element为空数组），将删除所有非UUID格式的elementId数据");
            }
            
            // 1.1 提取新的elementId列表（只处理level="1"的元素）
            Set<String> newElementIds = new HashSet<>();
            for (Map<String, Object> element : elementList) {
                String level = (String) element.get("level");
                if ("1".equals(level)) {
                    String inputElementId = (String) element.get("elementId");
                    if (!StringUtils.isEmpty(inputElementId)) {
                        newElementIds.add(inputElementId);
                    }
                }
            }
            log.info("新elementId列表: {}", newElementIds);
            
            // 2. 获取现有的TemplateData，如果不存在则创建新的
            TemplateData templateData = templateDataService.getByTemplateIdAndType(templateId, type, branchId);
            boolean isNewTemplateData = false;
            
            if (templateData == null) {
                // 如果是全部解绑且TemplateData不存在，直接返回
                if (isFullUnbind) {
                    log.info("全部解绑操作但TemplateData不存在，无需处理");
                    return;
                }
                
                // 创建新的TemplateData记录
                templateData = new TemplateData();
                templateData.setId(UUID.randomUUID().toString());
                templateData.setTemplateId(templateId);
                templateData.setType(type);
                templateData.setBranchId(branchId);
                templateData.setData(new ArrayList<>());
                templateData.setCreateTime(new Date());
                isNewTemplateData = true;
                log.info("创建新的TemplateData记录，templateId: {}, type: {}, branchId: {}", templateId, type, branchId);
            }
            
            List<JSONObject> dataList = templateData.getData();
            if (dataList == null) {
                dataList = new ArrayList<>();
                templateData.setData(dataList);
            }
            
            // 3. 如果是全部解绑，直接删除所有非UUID数据
            if (isFullUnbind) {
                List<JSONObject> itemsToRemove = new ArrayList<>();
                for (JSONObject dataItem : dataList) {
                    String existingElementId = dataItem.getString("elementId");
                    if (!StringUtils.isEmpty(existingElementId) && !isUUIDFormat(existingElementId)) {
                        itemsToRemove.add(dataItem);
                        log.info("全部解绑 - 标记删除非UUID元素: elementId={}, sheetName={}", 
                                existingElementId, dataItem.getString("sheetName"));
                    }
                }
                
                // 执行删除操作
                if (!itemsToRemove.isEmpty()) {
                    dataList.removeAll(itemsToRemove);
                    templateData.setData(dataList);
                    
                    // 保存更新后的TemplateData
                    boolean saveResult = templateDataService.updateTemplateData(templateData);
                    if (saveResult) {
                        log.info("全部解绑成功 - 删除了 {} 个非UUID构件数据，剩余 {} 个UUID数据", 
                                 itemsToRemove.size(), dataList.size());
                    } else {
                        log.error("全部解绑保存失败");
                    }
                } else {
                    log.info("全部解绑 - 没有找到需要删除的非UUID数据");
                }
                return; // 全部解绑处理完成，直接返回
            }
            
            // 4. 正常的新增/更新逻辑（element不为空时）
            // 调用parseTemplateFileToJson获取表头信息
            R templateHeaderResult = parseTemplateFileToJson(templateId, type);
            if (templateHeaderResult == null || templateHeaderResult.getCode() != 200) {
                log.error("获取模板表头信息失败");
                return;
            }
            
            Map<String, Object> headerData = (Map<String, Object>) templateHeaderResult.getData();
            if (headerData == null) {
                log.error("表头数据为空");
                return;
            }
            
            // 5. 创建elementId+sheetName到dataItem的映射，便于查找（支持多sheet）
            Map<String, JSONObject> existingDataMap = new HashMap<>();
            for (JSONObject dataItem : dataList) {
                String existingElementId = dataItem.getString("elementId");
                String existingSheetName = dataItem.getString("sheetName");
                if (!StringUtils.isEmpty(existingElementId)) {
                    // 使用elementId+sheetName作为复合key，确保每个sheet的数据独立
                    String compositeKey = existingElementId + "|" + (existingSheetName != null ? existingSheetName : "");
                    existingDataMap.put(compositeKey, dataItem);
                }
            }
            
            // 6. 根据type类型处理表头和element数据
            boolean hasUpdate = false;
            
//            if (type.equals("4")) {
                // type=4时，需要处理所有sheets，每个sheet单独处理
                List<Map<String, Object>> sheets = (List<Map<String, Object>>) headerData.get("sheets");
                if (sheets != null && !sheets.isEmpty()) {
                    
                    // 遍历每个sheet，分别处理
                    for (Map<String, Object> sheet : sheets) {
                        String sheetName = (String) sheet.get("name");
                        if (StringUtils.isEmpty(sheetName)) {
                            sheetName = "Template";
                        }
                        
                        // 获取当前sheet的字段名（递归提取，包括children中的二级表头）
                        Set<String> currentSheetFieldNames = new HashSet<>();
                        List<Map<String, Object>> sheetData = (List<Map<String, Object>>) sheet.get("data");
                        if (sheetData != null) {
                            // 使用递归方法提取所有字段名，包括children中的二级表头
                            extractFieldNames(sheetData, currentSheetFieldNames);
                            log.debug("Sheet {} 提取到字段: {}", sheetName, currentSheetFieldNames);
                        }
                        
                        if (currentSheetFieldNames.isEmpty()) {
                            log.debug("Sheet {} 没有字段，跳过处理", sheetName);
                            continue;
                        }
                        
                        // 处理当前sheet的所有element
                        boolean sheetHasUpdate = processElementsForSheet(
                            elementList, streamId, branchId, 
                            currentSheetFieldNames, sheetName, 
                            dataList, existingDataMap
                        );
                        
                        if (sheetHasUpdate) {
                            hasUpdate = true;
                        }
                    }
                }
//            } else {
//                // 其他type，处理data结构 - 使用原有逻辑
//                Set<String> fieldNames = new HashSet<>();
//                List<Map<String, Object>> fields = (List<Map<String, Object>>) headerData.get("data");
//                if (fields != null) {
//                    extractFieldNames(fields, fieldNames);
//                }
//
//                if (fieldNames.isEmpty()) {
//                    log.warn("未找到任何字段名");
//                    return;
//                }
//
//                // 处理所有element（原有逻辑）
//                boolean elementsHasUpdate = processElementsForSheet(
//                    elementList, streamId, branchId,
//                    fieldNames, "Template",
//                    dataList, existingDataMap
//                );
//
//                if (elementsHasUpdate) {
//                    hasUpdate = true;
//                }
//            }
            
            // 7. 删除减少的构件数据
            // 删除那些不在新elementId列表中但不是UUID格式的数据项
            List<JSONObject> itemsToRemove = new ArrayList<>();
            for (JSONObject dataItem : dataList) {
                String existingElementId = dataItem.getString("elementId");
                if (!StringUtils.isEmpty(existingElementId)) {
                    // 检查是否不在新elementId列表中且不是UUID格式
                    if (!newElementIds.contains(existingElementId) && !isUUIDFormat(existingElementId)) {
                        itemsToRemove.add(dataItem);
                        log.info("标记删除元素: elementId={}, sheetName={}", 
                                existingElementId, dataItem.getString("sheetName"));
                    }
                }
            }
            
            // 执行删除操作
            if (!itemsToRemove.isEmpty()) {
                dataList.removeAll(itemsToRemove);
                hasUpdate = true;
                log.info("删除了 {} 个减少的构件数据", itemsToRemove.size());
                
                // 重新构建existingDataMap（因为删除了一些项）
                existingDataMap.clear();
                for (JSONObject dataItem : dataList) {
                    String existingElementId = dataItem.getString("elementId");
                    String existingSheetName = dataItem.getString("sheetName");
                    if (!StringUtils.isEmpty(existingElementId)) {
                        String compositeKey = existingElementId + "|" + (existingSheetName != null ? existingSheetName : "");
                        existingDataMap.put(compositeKey, dataItem);
                    }
                }
            }
            
            // 8. 保存更新后的TemplateData
            if (hasUpdate) {
                // 确保templateData对象包含最新的dataList
                templateData.setData(dataList);
                
                // 添加保存前的详细调试日志
                log.info("准备保存TemplateData - templateId: {}, type: {}, dataList大小: {}", 
                         templateId, type, dataList.size());
                
                // 检查JSON序列化大小
                try {
                    String jsonString = com.alibaba.fastjson.JSON.toJSONString(dataList);
                    log.info("JSON序列化成功 - 字符长度: {}, 前100字符: {}", 
                             jsonString.length(), 
                             jsonString.length() > 100 ? jsonString.substring(0, 100) + "..." : jsonString);
                    
                    // 检查每个dataItem的完整性
                    for (int i = 0; i < dataList.size() && i < 5; i++) {
                        JSONObject item = dataList.get(i);
                        log.info("数据项[{}]: elementId={}, sheetName={}, 字段数={}", 
                                 i, item.getString("elementId"), item.getString("sheetName"), item.size());
                    }
                } catch (Exception e) {
                    log.error("JSON序列化检查失败", e);
                }
                
                // 保存到数据库
                boolean saveResult = false;
                try {
                    if (isNewTemplateData) {
                        log.info("执行新增操作 - 调用addTemplateData");
                        saveResult = templateDataService.addTemplateData(templateData);
                        log.info("addTemplateData执行结果: {}", saveResult);
                    } else {
                        log.info("执行更新操作 - 调用updateTemplateData");
                        saveResult = templateDataService.updateTemplateData(templateData);
                        log.info("updateTemplateData执行结果: {}", saveResult);
                    }
                } catch (Exception e) {
                    log.error("保存TemplateData时发生异常: templateId={}, type={}", templateId, type, e);
                    throw e; // 重新抛出异常，让外层捕获
                }
                
                if (saveResult) {
                    log.info("成功保存TemplateData更新到数据库，templateId: {}, type: {}, 总记录数: {}", 
                             templateId, type, dataList.size());
                    
                    // 保存后验证数据完整性
                    try {
                        TemplateData savedData = templateDataService.getByTemplateIdAndType(templateId, type, branchId);
                        if (savedData != null && savedData.getData() != null) {
                            log.info("保存后验证 - 实际保存记录数: {}, 预期记录数: {}", 
                                     savedData.getData().size(), dataList.size());
                            if (savedData.getData().size() != dataList.size()) {
                                log.error("数据截断检测到！预期: {}, 实际: {}", dataList.size(), savedData.getData().size());
                            } else {
                                log.info("数据保存验证通过，记录数一致");
                            }
                        } else {
                            log.error("保存后查询数据为空，可能保存失败！templateId: {}, type: {}, branchId: {}", 
                                     templateId, type, branchId);
                        }
                    } catch (Exception e) {
                        log.error("保存后验证数据完整性失败", e);
                    }
                } else {
                    log.error("保存TemplateData到数据库失败！返回false - templateId: {}, type: {}, isNew: {}", 
                             templateId, type, isNewTemplateData);
                }
            } else {
                log.info("没有需要更新的数据");
            }
            
            log.info("=== updateTemplateDataFromExpInstances 执行完成 ===");
        } catch (Exception e) {
            log.error("更新TemplateData失败: templateId={}, type={}, 异常信息: {}", templateId, type, e.getMessage(), e);
            log.info("=== updateTemplateDataFromExpInstances 执行失败 ===");
        }
    }
    
    /**
     * 递归提取字段名（处理嵌套的children结构）
     */
    private void extractFieldNames(List<Map<String, Object>> fields, Set<String> fieldNames) {
        if (fields == null) {
            return;
        }
        
        for (Map<String, Object> field : fields) {
            String fieldName = (String) field.get("field");
            if (fieldName != null) {
                fieldNames.add(fieldName);
            }
            
            // 处理children
            List<Map<String, Object>> children = (List<Map<String, Object>>) field.get("children");
            if (children != null) {
                extractFieldNames(children, fieldNames);
            }
        }
    }
    
    /**
     * 处理elementList中的元素，并更新TemplateData
     * @param elementList 元素列表
     * @param streamId 流水线ID
     * @param branchId 分支ID
     * @param fieldNames 字段名集合
     * @param sheetName 工作表名
     * @param dataList TemplateData中的数据列表
     * @param existingDataMap elementId到dataItem的映射
     * @return 是否有更新
     */
    private boolean processElementsForSheet(List<Map<String, Object>> elementList, String streamId, String branchId, 
                                            Set<String> fieldNames, String sheetName, 
                                            List<JSONObject> dataList, Map<String, JSONObject> existingDataMap) {
        boolean hasUpdate = false;
        
        for (Map<String, Object> element : elementList) {
            String level = (String) element.get("level");
            // 只处理level="1"的元素
            if (!"1".equals(level)) {
                continue;
            }
            
            String inputElementId = (String) element.get("elementId");
            if (StringUtils.isEmpty(inputElementId)) {
                log.debug("输入elementId为空，跳过");
                continue;
            }
            
            // 7. 根据elementId、streamId、branchId查询ExpInstances
            String commitId = null;
            if (!StringUtils.isEmpty(branchId)) {
                commitId = branchService.getLatestCommitsId(branchId);
                if (StringUtils.isEmpty(commitId)) {
                    log.warn("获取commitId失败: branchId={}", branchId);
                } else {
                    log.debug("获取commitId成功: branchId={}, commitId={}", branchId, commitId);
                }
            } else {
                log.warn("branchId为空，将使用null作为commitId查询");
            }
            
            log.debug("查询ExpInstances: streamId={}, elementId={}, commitId={}", streamId, inputElementId, commitId);
            ExpInstances expInstance = expInstancesService.searchExpInstances(streamId, inputElementId, commitId);
            if (expInstance == null) {
                log.warn("未找到ExpInstances记录: streamId={}, elementId={}, commitId={}", streamId, inputElementId, commitId);
                continue;
            } else {
                log.debug("找到ExpInstances记录: elementId={}, objId={}", inputElementId, expInstance.getObjId());
            }
            
            JSONObject parameters = expInstance.getParameters();
            if (parameters == null || parameters.isEmpty()) {
                log.debug("parameters为空: elementId={}", inputElementId);
                continue;
            }
            
            String expInstanceElementId = expInstance.getElementId();
            if (StringUtils.isEmpty(expInstanceElementId)) {
                log.debug("ExpInstances中elementId为空，跳过");
                continue;
            }
            
            // 8. 匹配表头字段与parameters，收集更新数据
            // 优化：即使未匹配到值，也应将表头字段添加到fieldUpdates中，值为空字符串
            Map<String, Object> fieldUpdates = new HashMap<>();
            for (String fieldName : fieldNames) {
                Object value = findFirstValueInParameters(parameters, fieldName);
                if (value != null && !value.toString().trim().isEmpty()) {
                    fieldUpdates.put(fieldName, value);
                } else {
                    // 如果没有匹配到值，使用空字符串（需求1）
                    fieldUpdates.put(fieldName, "");
                }
            }
            
            // 9. 判断是新增还是覆盖
            JSONObject targetDataItem = existingDataMap.get(expInstanceElementId + "|" + sheetName);
            
            if (targetDataItem != null) {
                // 覆盖现有记录
                for (Map.Entry<String, Object> entry : fieldUpdates.entrySet()) {
                    targetDataItem.put(entry.getKey(), entry.getValue());
                }
                log.info("覆盖现有记录: elementId={}, 更新字段数={}", expInstanceElementId, fieldUpdates.size());
                hasUpdate = true;
                
            } else {
                // 新增记录 - 包含elementId、匹配的字段数据和基础字段
                JSONObject newDataItem = new JSONObject();
                
                // 添加elementId
                newDataItem.put("elementId", expInstanceElementId);
                
                // 添加所有表头字段数据（包括空值字段）
                for (Map.Entry<String, Object> entry : fieldUpdates.entrySet()) {
                    newDataItem.put(entry.getKey(), entry.getValue());
                }
                
                // 计算新记录的索引
                int newRowIndex = dataList.size() + 1;
                
                // 添加基础字段
                newDataItem.put("sheetName", sheetName);
                newDataItem.put("objectId", expInstance.getObjId());
                newDataItem.put("partId", generatePartId(sheetName, newRowIndex));
                newDataItem.put("rowIndex", newRowIndex);
                
                // 添加到dataList和映射中
                dataList.add(newDataItem);
                existingDataMap.put(expInstanceElementId + "|" + sheetName, newDataItem);
                
                log.info("新增记录: elementId={}, 字段数={}, rowIndex={}", expInstanceElementId, fieldUpdates.size(), newRowIndex);
                hasUpdate = true;
            }
        }
        
        return hasUpdate;
    }
    
    /**
     * 在parameters中查找匹配fieldName的第一个value值
     * 支持字段映射：如果fieldName在映射表中，会使用映射后的字段名进行查找
     * 如果有多个相同name的参数，只返回第一个找到的value
     */
    private Object findFirstValueInParameters(JSONObject parameters, String fieldName) {
        try {
            // 1. 检查是否需要字段映射
            String mappedFieldName = FIELD_MAPPINGS.get(fieldName);
            
            // 2. 优先使用映射后的字段名查找
            if (mappedFieldName != null && !mappedFieldName.isEmpty()) {
                log.debug("字段映射: {} -> {}", fieldName, mappedFieldName);
                Object result = findValueByName(parameters, mappedFieldName);
                if (result != null) {
                    log.debug("使用映射字段找到匹配参数: 原字段={}, 映射字段={}, value={}", 
                             fieldName, mappedFieldName, result);
                    return result;
                }
            }
            
            // 3. 如果映射字段未找到，使用原始字段名查找（向下兼容）
            Object result = findValueByName(parameters, fieldName);
            if (result != null) {
                log.debug("使用原始字段找到匹配参数: fieldName={}, value={}", fieldName, result);
            }
            return result;
            
        } catch (Exception e) {
            log.error("查找参数值异常: fieldName={}, error={}", fieldName, e.getMessage());
        }
        return null;
    }
    
    /**
     * 在parameters中根据name查找第一个匹配的value
     */
    private Object findValueByName(JSONObject parameters, String targetName) {
        if (parameters == null || targetName == null) {
            return null;
        }
        
        try {
            for (String key : parameters.keySet()) {
                Object paramObj = parameters.get(key);
                
                if (paramObj instanceof JSONObject) {
                    JSONObject param = (JSONObject) paramObj;
                    String name = param.getString("name");
                    
                    // 匹配name与targetName，找到第一个就返回
                    if (targetName.equals(name)) {
                        Object value = param.get("value");
                        if (value != null) {
                            return value;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("根据name查找参数值异常: targetName={}, error={}", targetName, e.getMessage());
        }
        return null;
    }
    
    /**
     * 解析type=4自定义模板的表头并保存到白名单表
     * 
     * @param newTemplate 新增的模板对象
     */
    private void parseAndSaveWhiteList(NewTemplate newTemplate) throws Exception {
        log.info("开始解析type=4模板的表头, templateId={}, fileId={}", newTemplate.getId(), newTemplate.getFileId());
        
        // 0. 先删除该模板的旧白名单记录（防止重复）
        String templateId = newTemplate.getId();
        String branchId = newTemplate.getBranchId();
        if (templateId != null && !templateId.trim().isEmpty()) {
            try {
                List<WhiteList> existingList = whiteListService.getByTemplateId(templateId,newTemplate.getStreamId(),newTemplate.getBranchId());
                if (existingList != null && !existingList.isEmpty()) {
                    log.info("发现该模板已存在{}条白名单记录，先删除旧记录", existingList.size());
                    whiteListService.deleteByTemplateId(templateId);
                }
            } catch (Exception e) {
                log.warn("删除旧白名单记录失败，继续执行: {}", e.getMessage());
            }
        }
        
        // 1. 获取fileId
        String fileId = newTemplate.getFileId();
        if (fileId == null || fileId.trim().isEmpty()) {
            log.warn("模板的fileId为空，无法解析表头");
            return;
        }
        
        // 2. 根据fileId查找bomFile记录
        BomFile bomFile = bomFileService.getById(fileId);
        if (bomFile == null) {
            log.warn("未找到对应的BomFile记录, fileId={}", fileId);
            return;
        }
        
        log.info("找到BomFile记录: name={}, url={}", bomFile.getName(), bomFile.getUrl());
        
        // 3. 解析Excel文件表头（参考parseTemplateFileToJson方法）
        String fileUrl = bomFile.getUrl();
        InputStream inputStream = null;
        
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            inputStream = conn.getInputStream();
            
            // 使用Apache POI处理Excel文件
            Workbook workbook = WorkbookFactory.create(inputStream);
            
            // 准备保存的白名单列表
            List<WhiteList> whiteListList = new ArrayList<>();
            // 使用Set去重，避免重复的表头字段
            Set<String> headerNamesSet = new HashSet<>();
            
            // 遍历所有工作表
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                if (sheet == null) {
                    continue;
                }
                
                String sheetName = sheet.getSheetName();
                log.info("解析工作表: {}", sheetName);
                
                // 查找第一个非空行作为表头行
                Row headerRow = null;
                for (int rowIndex = 0; rowIndex <= 10; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null && hasNonEmptyCell(row)) {
                        headerRow = row;
                        break;
                    }
                }
                
                if (headerRow != null) {
                    // 遍历表头行的所有单元格
                    for (int colIndex = 0; colIndex < headerRow.getLastCellNum(); colIndex++) {
                        Cell cell = headerRow.getCell(colIndex);
                        if (cell != null) {
                            String cellValue = getCellValueAsString(cell);
                            if (cellValue != null && !cellValue.trim().isEmpty()) {
                                // 使用Set去重，如果多个sheet有相同的表头字段，只保存一次
                                String headerName = cellValue.trim();
                                if (!headerNamesSet.contains(headerName)) {
                                    headerNamesSet.add(headerName);
                                    // 创建白名单记录
                                    WhiteList whiteList = new WhiteList(
                                        headerName,
                                        newTemplate.getId(),
                                        fileId,
                                        newTemplate.getStreamId(),
                                            branchId  // branchId在此处为null，因为addTemplate时没有branchId
                                    );
                                    whiteListList.add(whiteList);
                                    log.debug("添加白名单记录: name={}", headerName);
                                }
                            }
                        }
                    }
                }
            }
            
            workbook.close();
            
            // 4. 批量保存到白名单表
            if (!whiteListList.isEmpty()) {
                log.info("准备批量保存白名单记录，共{}条", whiteListList.size());
                boolean success = whiteListService.batchAddWhiteList(whiteListList);
                if (success) {
                    log.info("批量保存白名单记录成功，共{}条", whiteListList.size());
                } else {
                    log.error("批量保存白名单记录失败");
                }
            } else {
                log.warn("未解析到任何表头数据");
            }
            
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // 忽略关闭异常
                }
            }
        }
    }
    
    private boolean isUUIDFormat(String elementId) {
        try {
            UUID.fromString(elementId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // ============= Excel Template Data 相关接口 =============
    
    /**
     * 解析Excel并保存数据
     * 根据templateId、streamId、branchId从material_list_conversions获取Excel文件并解析
     */
    @SneakyThrows
    @PostMapping("/batchParseAndSaveExcel.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "解析Excel并保存", notes = "解析Excel并保存")
    public R batchParseAndSaveExcel(@RequestParam String templateId, 
                                     @RequestParam String streamId, 
                                     @RequestParam String branchId,@RequestParam String type) {
        try {
            log.info("开始解析Excel: templateId={}, streamId={}, branchId={}", templateId, streamId, branchId);
            
            if (templateId == null || templateId.trim().isEmpty() ||
                streamId == null || streamId.trim().isEmpty() ||
                branchId == null || branchId.trim().isEmpty()) {
                return R.fail("参数不能为空");
            }
            
            // 用于收集所有文件的解析数据
            List<JSONObject> allExcelData = new ArrayList<>();
            List<String> errorMessages = new ArrayList<>();
            List<String> successFileNames = new ArrayList<>();
            List<Integer> successConversionIds = new ArrayList<>();
            
            // 1. 查询material_list_conversions表获取minio_url
            List<MaterialListConversion> conversions = materialListConversionService
                    .getByTemplateStreamBranch(templateId, streamId, branchId);
            
            if (conversions == null || conversions.isEmpty()) {
                log.warn("未找到对应的Excel文件: templateId={}, streamId={}, branchId={}", templateId, streamId, branchId);
                return R.fail("未找到对应的Excel文件记录");
            }
            
            log.info("共找到 {} 个Excel文件需要解析", conversions.size());
            
            // 2. 循环处理每个Excel文件，收集所有解析数据
            for (MaterialListConversion conversion : conversions) {
                String minioUrl = conversion.getMinioUrl();
                if (minioUrl == null || minioUrl.trim().isEmpty()) {
                    errorMessages.add("文件 " + conversion.getFileName() + ": MinIO URL为空");
                    continue;
                }
                
                // 获取文件名
                String fileName = conversion.getFileName();
                if (fileName == null || fileName.trim().isEmpty()) {
                    fileName = "未命名文件_" + conversion.getId();
                }
                        
                log.info("开始下载并解析Excel [{}]: {}", fileName, minioUrl);
                
                // 下载Excel文件
                InputStream inputStream = null;
                try {
                    URL url = new URL(minioUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(30000);
                    inputStream = conn.getInputStream();
                    
                    // 解析Excel为JSON（解析所有工作表，自动识别表头类型）
                    List<JSONObject> excelData = parseExcelAllSheetsWithFileName(inputStream, fileName);
                    
                    if (excelData == null || excelData.isEmpty()) {
                        errorMessages.add("文件 " + fileName + ": 解析结果为空");
                        log.warn("Excel解析结果为空: {}", fileName);
                        continue;
                    }
                    
                    log.info("Excel解析成功 [{}]: 共 {} 条数据", fileName, excelData.size());
                    
                    // 将解析数据添加到总列表
                    allExcelData.addAll(excelData);
                    successFileNames.add(fileName);
                    
                    // 记录成功的conversion ID，用于后续更新状态
                    if (conversion.getId() != null) {
                        successConversionIds.add(conversion.getId());
                    }
                    
                } catch (Exception e) {
                    log.error("处理Excel文件失败 [{}]: {}", fileName, e.getMessage(), e);
                    errorMessages.add("文件 " + fileName + ": " + e.getMessage());
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            log.error("关闭输入流失败", e);
                        }
                    }
                }
            }
            
            // 3. 如果没有任何成功的数据，返回失败
            if (allExcelData.isEmpty()) {
                log.error("所有Excel文件解析都失败了");
                Map<String, Object> failResult = new HashMap<>();
                failResult.put("successCount", 0);
                failResult.put("failCount", conversions.size());
                failResult.put("errors", errorMessages);
                return R.fail("所有Excel文件解析都失败: " + String.join("; ", errorMessages));
            }
            
            log.info("所有Excel文件解析完成，总共 {} 条数据，来自 {} 个文件", allExcelData.size(), successFileNames.size());
            
            // 4. 保存或更新到TemplateData（type=4）
            log.info("准备保存到TemplateData: templateId={}, type=4, branchId={}, data.size={}", 
                    templateId, branchId, allExcelData != null ? allExcelData.size() : 0);
            
            TemplateData existingData = templateDataService.getByTemplateIdAndType(templateId, "4", branchId);
            
            boolean saveSuccess = false;
            if (existingData != null) {
                // 更新
                log.info("找到已存在的记录，准备更新: id={}", existingData.getId());
                existingData.setData(allExcelData);
                existingData.setCreatedTime(new Date());
                
                log.info("更新前数据检查: id={}, templateId={}, type={}, branchId={}, data.size={}", 
                        existingData.getId(), existingData.getTemplateId(), existingData.getType(), 
                        existingData.getBranchId(), existingData.getData() != null ? existingData.getData().size() : 0);
                
                saveSuccess = templateDataService.updateTemplateData(existingData);
                if (saveSuccess) {
                    log.info("更新TemplateData成功: id={}, type=4, 总数据条数={}", existingData.getId(), allExcelData.size());
                } else {
                    log.error("更新TemplateData失败");
                }
            } else {
                // 插入
                log.info("未找到已存在记录，准备插入新数据");
                TemplateData newData = new TemplateData();
                newData.setId(UUID.randomUUID().toString());
                newData.setTemplateId(templateId);
                newData.setType(type);
                newData.setBranchId(branchId);
                newData.setData(allExcelData);
                newData.setCreatedTime(new Date());
                
                log.info("插入前数据检查: id={}, templateId={}, type={}, branchId={}, data.size={}", 
                        newData.getId(), newData.getTemplateId(), newData.getType(), 
                        newData.getBranchId(), newData.getData() != null ? newData.getData().size() : 0);
                
                saveSuccess = templateDataService.addTemplateData(newData);
                if (saveSuccess) {
                    log.info("保存TemplateData成功: id={}, type=4, 总数据条数={}", newData.getId(), allExcelData.size());
                } else {
                    log.error("保存TemplateData失败");
                }
            }
            
            // 6. 更新所有成功文件的状态为"read"
            if (saveSuccess) {
                for (Integer conversionId : successConversionIds) {
                    try {
                        boolean statusUpdated = materialListConversionService.updateStatus(conversionId, "read");
                        if (statusUpdated) {
                            log.info("成功更新文件状态为read: conversionId={}", conversionId);
                        } else {
                            log.warn("更新文件状态失败: conversionId={}", conversionId);
                        }
                    } catch (Exception e) {
                        log.error("更新文件状态异常: conversionId={}, error={}", conversionId, e.getMessage());
                    }
                }
            }
            
            // 7. 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successFileNames.size());
            result.put("failCount", errorMessages.size());
            result.put("totalDataCount", allExcelData.size());
            result.put("successFiles", successFileNames);
            
            if (!errorMessages.isEmpty()) {
                result.put("errors", errorMessages);
            }
            
            if (saveSuccess) {
                result.put("status", "success");
                result.put("message", "解析成功");
            } else {
                result.put("status", "partial_success");
                result.put("message", "文件解析成功但保存失败");
            }
            
            log.info("Excel批量解析完成: 成功{}个文件(共{}条数据), 失败{}个文件", 
                    successFileNames.size(), allExcelData.size(), errorMessages.size());
            return R.data(result);
            
        } catch (Exception e) {
            log.error("解析Excel失败", e);
            return R.fail("解析Excel失败: " + e.getMessage());
        }
    }
    
    /**
     * 解析Excel所有工作表为JSON格式，自动识别表头类型
     * 支持两种表头格式：
     * 1. 构件基础信息表：第1和第2行为组合表头（部分单元格已合并）
     * 2. 构件物料信息表：第1行为单行表头
     * 
     * @param inputStream Excel文件输入流
     * @param fileName Excel文件名
     * @return 所有工作表的解析数据
     */
    private List<JSONObject> parseExcelAllSheetsWithFileName(InputStream inputStream, String fileName) throws Exception {
        List<JSONObject> result = new ArrayList<>();
        
        byte[] excelData = EasyExcelReadUtils.readInputStreamToByteArray(inputStream);
        
        try (InputStream poiInputStream = new ByteArrayInputStream(excelData)) {
            Workbook workbook = WorkbookFactory.create(poiInputStream);
            
            log.info("Excel文件共有 {} 张工作表", workbook.getNumberOfSheets());

            String displaySheetName = extractFileNameWithoutExtension(fileName);
            if (StringUtils.isEmpty(displaySheetName)) {
                displaySheetName = fileName;
            }

            // 遍历所有工作表
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                if (sheet == null) {
                    continue;
                }
                
                String sheetName = sheet.getSheetName();
                log.info("开始解析工作表: {}", sheetName);
                
                // 判断表头类型
                boolean isMergedHeader = isMergedHeaderSheet(sheet);
                
                List<JSONObject> sheetData;
                if (isMergedHeader) {
                    // 组合表头（第1和第2行）- 构件基础信息表
                    log.info("工作表 {} 检测为组合表头格式（第1和第2行）- 构件基础信息表", sheetName);
                    sheetData = parseSheetWithMergedHeader(sheet, displaySheetName);
                } else {
                    // 第1行表头 - 构件物料信息表
                    log.info("工作表 {} 检测为单行表头格式（第1行）- 构件物料信息表", sheetName);
                    sheetData = parseSheetWithSingleHeader(sheet, displaySheetName);
                }
                
                if (sheetData != null && !sheetData.isEmpty()) {
                    log.info("工作表 {} 解析成功，共 {} 条数据", sheetName, sheetData.size());
                    result.addAll(sheetData);
                } else {
                    log.warn("工作表 {} 解析结果为空", sheetName);
                }
            }
            
            workbook.close();
        }
        
        return result;
    }
    
    /**
     * 判断工作表是否使用组合表头（第1和第2行）
     * 判断依据：
     * - 构件基础信息表：第1行和第2行都有数据，第1行有合并单元格（如"混凝土"、"外观尺寸"）
     * - 构件物料信息表：第1行为表头，第2行为数据或为空
     */
    private boolean isMergedHeaderSheet(Sheet sheet) {
        Row row0 = sheet.getRow(0);
        Row row1 = sheet.getRow(1);
        
        // 如果第1行为空，不是有效的表头
        if (row0 == null || !hasNonEmptyCell(row0)) {
            log.debug("第1行为空或无数据，判断为非多级表头");
            return false;
        }
        
        // 如果第2行为空或数据很少，判断为单行表头（构件物料信息表）
        if (row1 == null || !hasNonEmptyCell(row1)) {
            log.debug("第2行为空或无数据，判断为非多级表头");
            return false;
        }
        
        // 检查第1行是否包含典型的物料信息表关键字
        // 如果包含"Type"、"Name"、"物料编码"等，判断为单行表头
        for (int colIndex = 0; colIndex < Math.min(10, row0.getLastCellNum()); colIndex++) {
            Cell cell = row0.getCell(colIndex);
            String value = getCellValueAsString(cell);
            if (value != null && (value.equals("Type") || value.equals("Name") || 
                value.equals("物料编码") || value.equals("Material") ||
                value.equals("长度") || value.equals("数量") || value.equals("units"))) {
                log.debug("第1行包含物料信息关键字[{}]，判断为非多级表头", value);
                return false;  // 第1行是物料信息表头，非组合表头
            }
        }
        
        // 检查第1行是否有合并单元格（多级表头的典型特征）
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        boolean hasRow0MergedCell = false;
        for (CellRangeAddress region : mergedRegions) {
            // 如果合并区域包含第1行（索引0），说明第1行有合并单元格
            if (region.getFirstRow() == 0 && region.getLastRow() == 0) {
                hasRow0MergedCell = true;
                log.debug("第1行检测到合并单元格: 列{}到列{}", region.getFirstColumn(), region.getLastColumn());
                break;
            }
        }
        
        if (hasRow0MergedCell) {
            log.info("检测到第1行有合并单元格且第2行有数据，判断为多级表头格式");
            return true;
        }
        
        // 如果没有合并单元格，但第1行和第2行都有数据，也可能是多级表头
        // 进一步检查：统计第1行和第2行的非空单元格数量
        int row0CellCount = 0;
        int row1CellCount = 0;
        for (int colIndex = 0; colIndex < Math.max(row0.getLastCellNum(), row1.getLastCellNum()); colIndex++) {
            Cell cell0 = row0.getCell(colIndex);
            Cell cell1 = row1.getCell(colIndex);
            if (getCellValueAsString(cell0) != null && !getCellValueAsString(cell0).trim().isEmpty()) {
                row0CellCount++;
            }
            if (getCellValueAsString(cell1) != null && !getCellValueAsString(cell1).trim().isEmpty()) {
                row1CellCount++;
            }
        }
        
        // 如果第1行和第2行的单元格数量都不少于5个，判断为多级表头
        if (row0CellCount >= 5 && row1CellCount >= 5) {
            log.info("第1行有{}个非空单元格，第2行有{}个非空单元格，判断为多级表头格式", row0CellCount, row1CellCount);
            return true;
        }
        
        log.debug("未满足多级表头条件，判断为单行表头");
        return false;
    }
    
    /**
     * 解析使用单行表头的工作表（第1行为表头）- 构件物料信息表
     */
    private List<JSONObject> parseSheetWithSingleHeader(Sheet sheet, String sheetName) throws Exception {
        List<JSONObject> result = new ArrayList<>();
        
        // 第1行（索引0）是表头
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            log.warn("第1行表头为空");
            return result;
        }
        
        // 读取表头，确保读取所有列
        List<String> headers = new ArrayList<>();
        int lastCellNum = headerRow.getLastCellNum();
        if (lastCellNum <= 0) {
            log.warn("表头行没有任何列数据");
            return result;
        }
        
        for (int colIndex = 0; colIndex < lastCellNum; colIndex++) {
            Cell cell = headerRow.getCell(colIndex);
            String header = getCellValueAsString(cell);
            // 保存表头，即使为空也保存（保持列索引对应关系）
            headers.add(header != null ? header.trim() : "");
        }
        
        log.info("工作表 [{}] 解析到 {} 个表头列: {}", sheetName, headers.size(), headers);
        
        // 读取数据行（从第2行开始，索引1）
        int dataRowCount = 0;
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            
            // 检查是否为空行
            boolean isEmptyRow = true;
            for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
                Cell cell = row.getCell(colIndex);
                String cellValue = getCellValueAsString(cell);
                if (cellValue != null && !cellValue.trim().isEmpty()) {
                    isEmptyRow = false;
                    break;
                }
            }
            
            if (isEmptyRow) {
                continue;
            }
            
            // 构建JSON对象
            JSONObject jsonRow = new JSONObject();
            
            // 添加基础字段
            // 1. partId - 格式：V1-sheetName前3个字符+rowIndex
            String partId = generatePartId(sheetName, rowIndex);
            jsonRow.put("partId", partId);
            
            // 2. objectId - 使用MD5生成唯一ID
            String objectId = generateObjectId(sheetName, rowIndex);
            jsonRow.put("objectId", objectId);
            
            // 3. rowIndex - Excel行号
            jsonRow.put("rowIndex", rowIndex);
            
            // 4. sheetName - 工作表名称
            jsonRow.put("sheetName", sheetName);
            
            // 检查表头中是否包含elementIdV2列
            boolean hasElementIdV2Column = headers.contains("elementIdV2");
            
            // 添加表头-单元格映射（解析所有列的数据）
            String elementIdFromExcel = null; // 用于存储从Excel读取的elementId值
            for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
                String header = headers.get(colIndex);
                
                // 跳过空表头的列
                if (header == null || header.trim().isEmpty()) {
                    continue;
                }
                
                Cell cell = row.getCell(colIndex);
                String cellValue = getCellValueAsString(cell);
                
                // 如果是elementId列，先保存值，后面根据是否有elementIdV2列决定如何处理
                if ("elementId".equals(header)) {
                    elementIdFromExcel = cellValue;
                    continue; // 先不放入jsonRow，后面统一处理
                }
                
                // 保存所有表头字段的值（包括elementIdV2）
                jsonRow.put(header, cellValue != null ? cellValue : "");
            }
            
            // 5. elementId处理逻辑：
            // - 有elementIdV2列：elementId使用UUID自动生成
            // - 无elementIdV2列：elementId使用Excel中的原单元格值
            if (hasElementIdV2Column) {
                // 有elementIdV2列，elementId使用UUID
                jsonRow.put("elementId", UUID.randomUUID().toString());
            } else {
                // 无elementIdV2列，elementId使用Excel中的原值
                jsonRow.put("elementId", elementIdFromExcel != null ? elementIdFromExcel : UUID.randomUUID().toString());
            }
            
            result.add(jsonRow);
            dataRowCount++;
        }
        
        log.info("工作表 [{}] 解析完成，共 {} 条数据记录", sheetName, dataRowCount);
        
        return result;
    }
    
    /**
     * 解析使用组合表头的工作表（第1和第2行为组合表头）- 构件基础信息表
     */
    private List<JSONObject> parseSheetWithMergedHeader(Sheet sheet, String sheetName) throws Exception {
        List<JSONObject> result = new ArrayList<>();
        
        Row row0 = sheet.getRow(0);
        Row row1 = sheet.getRow(1);
        
        if (row0 == null || row1 == null) {
            log.warn("组合表头的行为空");
            return result;
        }
        
        // 读取组合表头
        List<String> headers = new ArrayList<>();
        int maxCol = Math.max(row0.getLastCellNum(), row1.getLastCellNum());
        
        // 获取所有合并单元格区域
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        
        for (int colIndex = 0; colIndex < maxCol; colIndex++) {
            String header = buildMergedHeader(sheet, row0, row1, colIndex, mergedRegions);
            headers.add(header);
        }
        
        log.info("工作表 [{}] 解析到 {} 个组合表头列: {}", sheetName, headers.size(), headers);
        
        // 读取数据行（从第3行开始，索引2）
        int dataRowCount = 0;
        for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            
            // 检查是否为空行
            boolean isEmptyRow = true;
            for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
                Cell cell = row.getCell(colIndex);
                String cellValue = getCellValueAsString(cell);
                if (cellValue != null && !cellValue.trim().isEmpty()) {
                    isEmptyRow = false;
                    break;
                }
            }
            
            if (isEmptyRow) {
                continue;
            }
            
            // 构建JSON对象
            JSONObject jsonRow = new JSONObject();
            
            // 添加基础字段
            // 1. partId - 格式：V1-sheetName前3个字符+rowIndex
            String partId = generatePartId(sheetName, rowIndex);
            jsonRow.put("partId", partId);
            
            // 2. objectId - 使用MD5生成唯一ID
            String objectId = generateObjectId(sheetName, rowIndex);
            jsonRow.put("objectId", objectId);
            
            // 3. rowIndex - Excel行号
            jsonRow.put("rowIndex", rowIndex);
            
            // 4. sheetName - 工作表名称
            jsonRow.put("sheetName", sheetName);
            
            // 检查表头中是否包含elementIdV2列
            boolean hasElementIdV2ColumnMerged = headers.contains("elementIdV2");
            
            // 添加表头-单元格映射（解析所有列的数据）
            String elementIdFromExcelMerged = null; // 用于存储从Excel读取的elementId值
            for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
                String header = headers.get(colIndex);
                
                // 跳过空表头的列
                if (header == null || header.trim().isEmpty()) {
                    continue;
                }
                
                Cell cell = row.getCell(colIndex);
                String cellValue = getCellValueAsString(cell);
                
                // 如果是elementId列，先保存值，后面根据是否有elementIdV2列决定如何处理
                if ("elementId".equals(header)) {
                    elementIdFromExcelMerged = cellValue;
                    continue; // 先不放入jsonRow，后面统一处理
                }
                
                // 保存所有表头字段的值（包括elementIdV2）
                jsonRow.put(header, cellValue != null ? cellValue : "");
            }
            
            // 5. elementId处理逻辑：
            // - 有elementIdV2列：elementId使用UUID自动生成
            // - 无elementIdV2列：elementId使用Excel中的原单元格值
            if (hasElementIdV2ColumnMerged) {
                // 有elementIdV2列，elementId使用UUID
                jsonRow.put("elementId", UUID.randomUUID().toString());
            } else {
                // 无elementIdV2列，elementId使用Excel中的原值
                jsonRow.put("elementId", elementIdFromExcelMerged != null ? elementIdFromExcelMerged : UUID.randomUUID().toString());
            }
            
            result.add(jsonRow);
            dataRowCount++;
        }
        
        log.info("工作表 [{}] 解析完成，共 {} 条数据记录", sheetName, dataRowCount);
        
        return result;
    }
    
    /**
     * 构建组合表头的表头名称
     * 处理Excel多级表头，生成"一级表头-二级表头"格式的key值
     * 例如："外观尺寸-长（mm）"、"混凝土-砀用量（m³）"
     */
    private String buildMergedHeader(Sheet sheet, Row row0, Row row1, int colIndex, 
                                     List<CellRangeAddress> mergedRegions) {
        // 获取第1行（索引0）的值 - 一级表头
        String value0 = getCellValueForMergedCell(sheet, row0, colIndex, mergedRegions);
        // 获取第2行（索引1）的值 - 二级表头
        String value1 = getCellValueAsString(row1.getCell(colIndex));
        
        value0 = value0 != null ? value0.trim() : "";
        value1 = value1 != null ? value1.trim() : "";
        
        // 调试日志
        if (colIndex < 15) { // 只打印前15列的调试信息
            log.debug("列 {} - 第1行: [{}], 第2行: [{}]", colIndex, value0, value1);
        }
        
        if (!value0.isEmpty() && !value1.isEmpty()) {
            // 两行都有值
            if (value0.equals(value1)) {
                // 如果两行的值相同，只返回一个值（如“构件型号”）
                return value0;
            } else {
                // 如果两行的值不同，合并为“第1行值-第2行值”
                // 例如：“外观尺寸-长（mm）”、“混凝土-砀用量（m³）”
                return value0 + "-" + value1;
            }
        } else if (!value0.isEmpty()) {
            // 只有第1行有值，第2行为空
            return value0;
        } else if (!value1.isEmpty()) {
            // 只有第2行有值，第1行为空
            return value1;
        } else {
            // 都为空
            return "";
        }
    }
    
    /**
     * 获取单元格的值，考虑合并单元格的情况
     * 对于合并单元格，返回合并区域左上角单元格的值
     */
    private String getCellValueForMergedCell(Sheet sheet, Row row, int colIndex, 
                                             List<CellRangeAddress> mergedRegions) {
        if (row == null) {
            return "";
        }
        
        int rowNum = row.getRowNum();
        
        // 先检查是否在合并单元格区域中
        for (CellRangeAddress region : mergedRegions) {
            // 检查当前单元格是否在这个合并区域内
            if (region.isInRange(rowNum, colIndex)) {
                // 获取合并区域左上角单元格的值
                int firstRow = region.getFirstRow();
                int firstCol = region.getFirstColumn();
                
                Row mergedRow = sheet.getRow(firstRow);
                if (mergedRow != null) {
                    Cell mergedCell = mergedRow.getCell(firstCol);
                    String mergedValue = getCellValueAsString(mergedCell);
                    if (mergedValue != null && !mergedValue.trim().isEmpty()) {
                        return mergedValue.trim();
                    }
                }
            }
        }
        
        // 如果不是合并单元格，直接获取当前单元格的值
        Cell cell = row.getCell(colIndex);
        String directValue = getCellValueAsString(cell);
        return directValue != null ? directValue.trim() : "";
    }
    

    
    /**
     * 根据templateId、streamId和branchId获取对应的Excel文件名列表和表头信息
     * 返回格式: { "data": { "sheets": [{"name": "file1", "data": [{"field": "col1", "title": "col1"}]}] } }
     */
    @SneakyThrows
    @GetMapping("/getExcelFileNames.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "获取Excel文件名列表", notes = "根据templateId、streamId和branchId获取Excel文件名列表")
    public R getExcelFileNames(@RequestParam String templateId, 
                               @RequestParam String streamId, 
                               @RequestParam String branchId) {
        try {
            log.info("获取Excel文件名列表: templateId={}, streamId={}, branchId={}", templateId, streamId, branchId);
            
            if (templateId == null || templateId.trim().isEmpty() ||
                streamId == null || streamId.trim().isEmpty() ||
                branchId == null || branchId.trim().isEmpty()) {
                return R.fail("参数不能为空");
            }
            
            // 1. 调用parseTemplateFileToJson获取表头信息
            NewTemplate template = newTemplateService.getTemplateById(templateId);
            if (template == null) {
                log.warn("未找到模板: templateId={}", templateId);
                Map<String, Object> emptyResult = new HashMap<>();
                emptyResult.put("sheets", new ArrayList<>());
                return R.data(emptyResult);
            }
            
            String type = template.getType();
            R templateHeaderResult = parseTemplateFileToJson(templateId, type);
            
            if (!templateHeaderResult.isSuccess()) {
                log.error("获取模板表头信息失败: {}", templateHeaderResult.getMsg());
                return R.fail("获取模板表头信息失败: " + templateHeaderResult.getMsg());
            }
            
            // 解析表头数据
            // templateHeaderResult.getData() 返回的结构是: { "sheets": [{ "data": [...] }] }
            List<Map<String, String>> headerData = new ArrayList<>();
            Object templateData = templateHeaderResult.getData();
            if (templateData != null) {
                try {
                    if (templateData instanceof Map) {
                        Map<?, ?> dataMap = (Map<?, ?>) templateData;
                        // 获取 sheets 数组
                        Object sheetsObj = dataMap.get("sheets");
                        if (sheetsObj instanceof List) {
                            List<?> sheetsList = (List<?>) sheetsObj;
                            if (!sheetsList.isEmpty()) {
                                // 获取第一个 sheet
                                Object firstSheet = sheetsList.get(0);
                                if (firstSheet instanceof Map) {
                                    Map<?, ?> sheetMap = (Map<?, ?>) firstSheet;
                                    // 获取 data 数组
                                    Object dataObj = sheetMap.get("data");
                                    if (dataObj instanceof List) {
                                        List<?> dataList = (List<?>) dataObj;
                                        // 遍历表头数据
                                        for (Object item : dataList) {
                                            if (item instanceof Map) {
                                                Map<?, ?> itemMap = (Map<?, ?>) item;
                                                if (itemMap.containsKey("field")) {
                                                    Map<String, String> header = new HashMap<>();
                                                    String fieldValue = String.valueOf(itemMap.get("field"));
                                                    String titleValue = String.valueOf(itemMap.get("title"));
                                                    header.put("field", fieldValue);
                                                    header.put("title", titleValue);
                                                    headerData.add(header);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("解析表头数据失败: {}", e.getMessage(), e);
                }
            }
            
            log.info("获取到表头信息: {} 个字段", headerData.size());
            
            // 2. 直接从MaterialListConversion表获取文件名列表
            List<MaterialListConversion> conversions = materialListConversionService
                    .getByTemplateStreamBranch(templateId, streamId, branchId);
            
            Set<String> fileNameSet = new LinkedHashSet<>(); // 使用Set去重，用LinkedHashSet保持顺序
            if (conversions != null && !conversions.isEmpty()) {
                for (MaterialListConversion conversion : conversions) {
                    String fileName = conversion.getFileName();
                    if (fileName != null && !fileName.trim().isEmpty() && !"null".equals(fileName)) {
                        fileNameSet.add(fileName);
                    } else {
                        // 如果文件名为空，使用默认名称
                        String defaultName = "未命名文件_" + conversion.getId();
                        fileNameSet.add(defaultName);
                        log.warn("MaterialListConversion id={} 的fileName为空，使用默认名称: {}", 
                                conversion.getId(), defaultName);
                    }
                }
            } else {
                log.warn("未找到对应的MaterialListConversion记录: templateId={}, streamId={}, branchId={}", 
                        templateId, streamId, branchId);
            }
            
            log.info("从MaterialListConversion获取到 {} 个文件名", fileNameSet.size());
            
            // 3. 构建sheets数组
            List<Map<String, Object>> sheets = new ArrayList<>();
            for (String fileName : fileNameSet) {
                Map<String, Object> sheet = new HashMap<>();
                sheet.put("name", fileName);
                sheet.put("data", new ArrayList<>(headerData)); // 每个文件使用相同的表头
                sheets.add(sheet);
            }
            
            // 4. 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("sheets", sheets);
            
            log.info("获取Excel文件名列表成功: templateId={}, streamId={}, branchId={}, 文件数量={}", 
                    templateId, streamId, branchId, sheets.size());
            return R.data(result);
            
        } catch (Exception e) {
            log.error("获取Excel文件名列表失败: templateId={}, streamId={}, branchId={}", templateId, streamId, branchId, e);
            return R.fail("获取失败: " + e.getMessage());
        }
    }
    
    /**
     * 清理文件名中的非法字符
     */
    private String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "未命名";
        }
        // 移除可能已经存在的.xlsx或.xls后缀
        String cleaned = fileName.trim();
        if (cleaned.toLowerCase().endsWith(".xlsx")) {
            cleaned = cleaned.substring(0, cleaned.length() - 5);
        } else if (cleaned.toLowerCase().endsWith(".xls")) {
            cleaned = cleaned.substring(0, cleaned.length() - 4);
        }
        // 移除Windows文件名中的非法字符
        return cleaned.replaceAll("[\\\\/:*?\"<>|]", "_");
    }
    
    /**
     * 清理工作表名称中的非法字符
     */
    private String sanitizeSheetName(String sheetName) {
        if (sheetName == null || sheetName.trim().isEmpty()) {
            return "Sheet1";
        }
        // Excel工作表名称不能包含: \ / ? * [ ]
        String sanitized = sheetName.replaceAll("[\\\\/:*?\\[\\]]", "_");
        // Excel工作表名称最大长度为31个字符
        if (sanitized.length() > 31) {
            sanitized = sanitized.substring(0, 31);
        }
        return sanitized;
    }
    
    /**
     * 下载Excel文件 - 从templateData的data导出
     * @param templateId 模板ID
     * @param type 类型
     * @param branchId 分支ID（可选）
     * @param response HTTP响应
     */
    @SneakyThrows
    @GetMapping("/downloadTemplateDataExcel.do")
    @ApiOperationSupport(order = 100)
    @ApiOperation(value = "下载模板数据Excel", notes = "从templateData的data导出为Excel文件")
    public void downloadTemplateDataExcel(@RequestParam String templateId, 
                                          @RequestParam String type,
                                          @RequestParam(required = false) String branchId,
                                          HttpServletResponse response) {
        try {
            log.info("开始导出Excel: templateId={}, type={}, branchId={}", templateId, type, branchId);
            
            // 0. 获取模板名称用于文件名
            String templateName = "template_data";
            NewTemplate template = newTemplateService.getTemplateById(templateId);
            if (template != null && template.getName() != null && !template.getName().trim().isEmpty()) {
                templateName = template.getName().trim();
            }
            
            // 1. 获取数据
            JSONObject dataResult = templateDataService.getDataByTemplateIdAndType(templateId, type, branchId);
            if (dataResult == null || !dataResult.containsKey("data")) {
                log.warn("未找到数据: templateId={}, type={}, branchId={}", templateId, type, branchId);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("未找到数据");
                return;
            }
            
            List<JSONObject> dataList = (List<JSONObject>) dataResult.get("data");
            if (dataList == null || dataList.isEmpty()) {
                log.warn("数据为空: templateId={}, type={}, branchId={}", templateId, type, branchId);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().write("数据为空");
                return;
            }
            
            log.info("获取到数据记录数: {}", dataList.size());
            
            // 2. 按sheetName分组数据
            Map<String, List<JSONObject>> sheetDataMap = new LinkedHashMap<>();
            for (JSONObject item : dataList) {
                String sheetName = item.getString("sheetName");
                if (StringUtils.isEmpty(sheetName)) {
                    sheetName = "Sheet1"; // 默认sheet名称
                }
                
                sheetDataMap.computeIfAbsent(sheetName, k -> new ArrayList<>()).add(item);
            }
            
            log.info("数据分组完成，共{}个sheet", sheetDataMap.size());
            
            // 3. 创建Excel工作簿
            Workbook workbook = WorkbookFactory.create(true); // 创建.xlsx格式
            
            // 4. 为每个sheetName创建一个工作表
            for (Map.Entry<String, List<JSONObject>> entry : sheetDataMap.entrySet()) {
                String sheetName = sanitizeSheetName(entry.getKey());
                List<JSONObject> sheetData = entry.getValue();
                
                log.info("创建工作表: {}, 记录数: {}", sheetName, sheetData.size());
                
                // 创建工作表
                Sheet sheet = workbook.createSheet(sheetName);
                
                // 5. 提取所有字段名（忽略elementId和objectId）
                Set<String> allFields = new LinkedHashSet<>();
                for (JSONObject item : sheetData) {
                    for (String key : item.keySet()) {
                        // 忽略elementId和objectId
                        if (!"elementId".equals(key) && !"objectId".equals(key)) {
                            allFields.add(key);
                        }
                    }
                }
                
                List<String> fieldList = new ArrayList<>(allFields);
                log.info("工作表 {} 的字段数: {}", sheetName, fieldList.size());
                
                // 6. 创建表头行
                Row headerRow = sheet.createRow(0);
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);
                
                for (int i = 0; i < fieldList.size(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(fieldList.get(i));
                    cell.setCellStyle(headerStyle);
                }
                
                // 7. 创建数据行
                CellStyle dataStyle = workbook.createCellStyle();
                dataStyle.setBorderBottom(BorderStyle.THIN);
                dataStyle.setBorderTop(BorderStyle.THIN);
                dataStyle.setBorderLeft(BorderStyle.THIN);
                dataStyle.setBorderRight(BorderStyle.THIN);
                
                int rowIndex = 1;
                for (JSONObject item : sheetData) {
                    Row dataRow = sheet.createRow(rowIndex++);
                    
                    for (int i = 0; i < fieldList.size(); i++) {
                        String fieldName = fieldList.get(i);
                        Object value = item.get(fieldName);
                        
                        Cell cell = dataRow.createCell(i);
                        cell.setCellStyle(dataStyle);
                        
                        if (value != null) {
                            // 根据值类型设置单元格值
                            if (value instanceof Number) {
                                cell.setCellValue(((Number) value).doubleValue());
                            } else if (value instanceof Boolean) {
                                cell.setCellValue((Boolean) value);
                            } else {
                                cell.setCellValue(value.toString());
                            }
                        }
                    }
                }
                
                // 8. 自动调整列宽
                for (int i = 0; i < fieldList.size(); i++) {
                    try {
                        sheet.autoSizeColumn(i);
                        // 设置最大宽度限制
                        int currentWidth = sheet.getColumnWidth(i);
                        if (currentWidth > 15000) {
                            sheet.setColumnWidth(i, 15000);
                        }
                    } catch (Exception e) {
                        log.warn("自动调整列宽失败: column={}", i);
                    }
                }
                
                log.info("工作表 {} 创建完成", sheetName);
            }
            
            // 9. 设置响应头 - 文件名格式：模板名称_日期（如：物料清单表_11.29）
            java.time.LocalDate today = java.time.LocalDate.now();
            String dateStr = String.format("%d.%d", today.getMonthValue(), today.getDayOfMonth());
            String fileName = templateName + "_" + dateStr + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            
            // 10. 写入响应流
            try (OutputStream outputStream = response.getOutputStream()) {
                workbook.write(outputStream);
                outputStream.flush();
            }
            
            workbook.close();
            log.info("Excel文件导出成功: {}", fileName);
            
        } catch (Exception e) {
            log.error("导出Excel失败: templateId={}, type={}, branchId={}", templateId, type, branchId, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("导出失败: " + e.getMessage());
        }
    }
}
