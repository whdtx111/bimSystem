package org.springblade.modules.sp.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.ExpInstances;
import org.springblade.modules.sp.entity.RevitLOD;
import org.springblade.modules.sp.mapper.RevitLODMapper;
import org.springblade.modules.sp.service.ExpInstancesService;
import org.springblade.modules.sp.service.RevitLODService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springblade.modules.sp.service.NewTemplateService;
import org.springblade.modules.sp.service.TemplateDataService;
import org.springblade.modules.sp.service.BranchService;
import org.springblade.modules.sp.entity.NewTemplate;
import org.springblade.modules.sp.entity.TemplateData;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@DS("postgresql")
public class RevitLODServiceImpl extends BaseServiceImpl<RevitLODMapper, RevitLOD> implements RevitLODService {

    @Autowired
    private RevitLODMapper revitLODMapper;

    @Autowired
    private ExpInstancesService expInstancesService;
    
    @Autowired
    private NewTemplateService newTemplateService;
    
    @Autowired
    private TemplateDataService templateDataService;
    
    @Autowired
    private BranchService branchService;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public RevitLOD getById(String id) {
        try {
            return revitLODMapper.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RevitLOD getByCheckId(String checkId) {
        try {
            return revitLODMapper.getByCheckId(checkId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RevitLOD getByStreamBranchCommit(String streamId, String branchId, String commitId) {
        try {
            return revitLODMapper.getByStreamBranchCommit(streamId, branchId, commitId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RevitLOD getByStreamBranchCommitAndCheckId(String streamId, String branchId, String commitId, String checkId) {
        try {
            return revitLODMapper.getByStreamBranchCommitAndCheckId(streamId, branchId, commitId, checkId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RevitLOD> getListByStreamBranchCommit(String streamId, String branchId, String commitId) {
        try {
            return revitLODMapper.getListByStreamBranchCommit(streamId, branchId, commitId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RevitLOD> getLatestListByStreamBranch(String streamId, String branchId) {
        try {
            return revitLODMapper.getLatestListByStreamBranch(streamId, branchId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RevitLOD> getByStreamId(String streamId) {
        try {
            return revitLODMapper.getByStreamId(streamId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addRevitLOD(RevitLOD revitLOD) {
        try {
            int result = revitLODMapper.addRevitLOD(revitLOD);
            if (result > 0) {
                // 清理相关缓存
                clearRelatedCache(revitLOD.getStreamId(), revitLOD.getBranchId(), revitLOD.getCommitId());
            }
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteById(String id) {
        try {
            // 先获取要删除的记录信息，用于清理缓存
            RevitLOD revitLOD = getById(id);
            int result = revitLODMapper.deleteById(id);
            if (result > 0 && revitLOD != null) {
                // 清理相关缓存
                clearRelatedCache(revitLOD.getStreamId(), revitLOD.getBranchId(), revitLOD.getCommitId());
            }
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteByCheckId(String checkId) {
        try {
            // 先获取要删除的记录信息，用于清理缓存
            RevitLOD revitLOD = getByCheckId(checkId);
            int result = revitLODMapper.deleteByCheckId(checkId);
            if (result > 0 && revitLOD != null) {
                // 清理相关缓存
                clearRelatedCache(revitLOD.getStreamId(), revitLOD.getBranchId(), revitLOD.getCommitId());
            }
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateRevitLOD(RevitLOD revitLOD) {
        try {
            int result = revitLODMapper.updateRevitLOD(revitLOD);
            if (result > 0) {
                // 清理相关缓存
                clearRelatedCache(revitLOD.getStreamId(), revitLOD.getBranchId(), revitLOD.getCommitId());
            }
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<RevitLOD> getAllRevitLOD() {
        try {
            return revitLODMapper.getAllRevitLOD();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RevitLOD> batchSaveRevitLOD(List<Map<String, Object>> jsonDataList) {
        List<RevitLOD> savedList = new ArrayList<>();
        
        try {
            for (Map<String, Object> jsonData : jsonDataList) {
                RevitLOD revitLOD = convertJsonToRevitLOD(jsonData);
                if (revitLOD != null) {
                    boolean success = false;
                    
                    // 修改：检查是否存在相同streamId、branchId、commitId、checkId的记录
                    RevitLOD existingRecord = getByStreamBranchCommitAndCheckId(
                        revitLOD.getStreamId(), 
                        revitLOD.getBranchId(), 
                        revitLOD.getCommitId(),
                        revitLOD.getCheckId()
                    );
                    
                    if (existingRecord != null) {
                        // 存在相同记录，执行覆盖更新
                        revitLOD.setId(existingRecord.getId()); // 保持原有ID
                        revitLOD.setCreatedTime(existingRecord.getCreatedTime()); // 保持原有创建时间
                        // 对于批量操作，直接调用mapper，避免重复清理缓存
                        int updateResult = revitLODMapper.updateRevitLOD(revitLOD);
                        success = updateResult > 0;
                        if (success) {
                            savedList.add(revitLOD);
                            System.out.println("成功覆盖更新RevitLOD数据，ID: " + revitLOD.getId() + 
                                ", CheckID: " + revitLOD.getCheckId() + 
                                ", StreamId: " + revitLOD.getStreamId() + 
                                ", BranchId: " + revitLOD.getBranchId() + 
                                ", CommitId: " + revitLOD.getCommitId());
                        } else {
                            System.err.println("覆盖更新RevitLOD数据失败，CheckID: " + revitLOD.getCheckId());
                        }
                    } else {
                        // 不存在相同记录，执行新增
                        // 对于批量操作，直接调用mapper，避免重复清理缓存
                        int insertResult = revitLODMapper.addRevitLOD(revitLOD);
                        success = insertResult > 0;
                        if (success) {
                            savedList.add(revitLOD);
                            System.out.println("成功新增RevitLOD数据，ID: " + revitLOD.getId() + 
                                ", CheckID: " + revitLOD.getCheckId() + 
                                ", StreamId: " + revitLOD.getStreamId() + 
                                ", BranchId: " + revitLOD.getBranchId() + 
                                ", CommitId: " + revitLOD.getCommitId());
                        } else {
                            System.err.println("新增RevitLOD数据失败，CheckID: " + revitLOD.getCheckId());
                        }
                    }
                }
            }
            // 批量操作完成后，统一清理缓存
            if (!savedList.isEmpty()) {
                // 获取所有涉及的streamId、branchId、commitId组合
                savedList.stream()
                    .collect(Collectors.groupingBy(r -> r.getStreamId() + ":" + r.getBranchId() + ":" + r.getCommitId()))
                    .keySet()
                    .forEach(key -> {
                        String[] parts = key.split(":");
                        if (parts.length == 3) {
                            clearRelatedCache(parts[0], parts[1], parts[2]);
                        }
                    });
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("批量保存RevitLOD数据时发生异常: " + e.getMessage());
        }
        
        return savedList;
    }

    @Override
    public List<Map<String, Object>> getFormattedOutputById(String id) {
        try {
            RevitLOD revitLOD = getById(id);
            if (revitLOD == null) {
                System.err.println("未找到ID为 " + id + " 的RevitLOD数据");
                return new ArrayList<>();
            }

            return formatRevitLODOutput(revitLOD);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> getFormattedOutput(String streamId, String branchId) {
        try {
            // 生成缓存键（不再包含commitId）
            String cacheKey = "revit_lod_formatted_output:" + streamId + ":" + branchId;
            
            // 尝试从Redis缓存获取
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cachedResult = (List<Map<String, Object>>) redisTemplate.opsForValue().get(cacheKey);
            if (cachedResult != null) {
                System.out.println("从Redis缓存获取RevitLOD格式化输出数据，缓存键: " + cacheKey);
                return cachedResult;
            }
            
            // 缓存未命中，执行数据库查询（获取最新记录，不需要commitId）
            List<RevitLOD> revitLODList = getLatestListByStreamBranch(streamId, branchId);
            if (revitLODList == null || revitLODList.isEmpty()) {
                System.err.println("未找到数据！streamId: " + streamId + ", branchId: " + branchId);
                return new ArrayList<>();
            }

            // 按checkId从小到大排序
            revitLODList.sort((r1, r2) -> {
                String checkId1 = r1.getCheckId();
                String checkId2 = r2.getCheckId();
                if (checkId1 == null && checkId2 == null) {
                    return 0;
                }
                if (checkId1 == null) {
                    return 1;
                }
                if (checkId2 == null) {
                    return -1;
                }
                // 尝试按数字排序，如果不是数字则按字符串排序
                try {
                    Integer num1 = Integer.valueOf(checkId1);
                    Integer num2 = Integer.valueOf(checkId2);
                    return num1.compareTo(num2);
                } catch (NumberFormatException e) {
                    return checkId1.compareTo(checkId2);
                }
            });
            
            // 批量获取objId映射，减少数据库查询次数
            // 使用每条记录自身的commitId进行查询
            Map<String, String> elementIdToObjIdMap = buildElementIdToObjIdMapForLatest(revitLODList, streamId, branchId);
            
            // 为每个checkId的记录生成格式化输出
            List<Map<String, Object>> result = new ArrayList<>();
            for (RevitLOD revitLOD : revitLODList) {
                List<Map<String, Object>> formattedOutput = formatRevitLODOutputWithCache(revitLOD, elementIdToObjIdMap);
                result.addAll(formattedOutput);
            }
            
            // 将结果缓存到Redis，过期时间5分钟
            redisTemplate.opsForValue().set(cacheKey, result, 5, TimeUnit.MINUTES);
            System.out.println("RevitLOD格式化输出数据已缓存到Redis，缓存键: " + cacheKey);
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> getFormattedOutputWithCommitId(String streamId, String branchId, String commitId) {
        try {
            // 生成缓存键（包含commitId）
            String cacheKey = "revit_lod_formatted_output:" + streamId + ":" + branchId + ":" + commitId;
            
            // 尝试从Redis缓存获取
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cachedResult = (List<Map<String, Object>>) redisTemplate.opsForValue().get(cacheKey);
            if (cachedResult != null) {
                System.out.println("从Redis缓存获取RevitLOD格式化输出数据，缓存键: " + cacheKey);
                return cachedResult;
            }
            
            // 缓存未命中，执行数据库查询（使用commitId）
            List<RevitLOD> revitLODList = getListByStreamBranchCommit(streamId, branchId, commitId);
            if (revitLODList == null || revitLODList.isEmpty()) {
                System.out.println("使用commitId未找到数据！streamId: " + streamId + ", branchId: " + branchId + ", commitId: " + commitId);
                return new ArrayList<>();
            }

            // 按checkId从小到大排序
            revitLODList.sort((r1, r2) -> {
                String checkId1 = r1.getCheckId();
                String checkId2 = r2.getCheckId();
                if (checkId1 == null && checkId2 == null) {
                    return 0;
                }
                if (checkId1 == null) {
                    return 1;
                }
                if (checkId2 == null) {
                    return -1;
                }
                // 尝试按数字排序，如果不是数字则按字符串排序
                try {
                    Integer num1 = Integer.valueOf(checkId1);
                    Integer num2 = Integer.valueOf(checkId2);
                    return num1.compareTo(num2);
                } catch (NumberFormatException e) {
                    return checkId1.compareTo(checkId2);
                }
            });
            
            // 批量获取objId映射，减少数据库查询次数
            Map<String, String> elementIdToObjIdMap = buildElementIdToObjIdMap(revitLODList, streamId, branchId, commitId);
            
            // 为每个checkId的记录生成格式化输出
            List<Map<String, Object>> result = new ArrayList<>();
            for (RevitLOD revitLOD : revitLODList) {
                List<Map<String, Object>> formattedOutput = formatRevitLODOutputWithCache(revitLOD, elementIdToObjIdMap);
                result.addAll(formattedOutput);
            }
            
            // 将结果缓存到Redis，过期时间5分钟
            redisTemplate.opsForValue().set(cacheKey, result, 5, TimeUnit.MINUTES);
            System.out.println("RevitLOD格式化输出数据已缓存到Redis，缓存键: " + cacheKey);
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 将JSON数据转换为RevitLOD实体
     */
    private RevitLOD convertJsonToRevitLOD(Map<String, Object> jsonData) {
        try {
            RevitLOD revitLOD = new RevitLOD();
            
            // 基础字段映射
            revitLOD.setCheckId(getString(jsonData, "id"));
            revitLOD.setStreamId(getString(jsonData, "streamId"));
            revitLOD.setBranchId(getString(jsonData, "branchId"));
            revitLOD.setCommitId(getString(jsonData, "commitId"));
            revitLOD.setName(getString(jsonData, "name"));
            revitLOD.setValuePass(getInteger(jsonData, "valuePass"));
            revitLOD.setValueNotPass(getInteger(jsonData, "valueNotPass"));
            revitLOD.setCheckStatus(getString(jsonData, "status"));
            revitLOD.setIsPass(getString(jsonData, "isPass"));

            // 构建data字段
            List<JSONObject> dataList = new ArrayList<>();
            
            // 处理passAssemblyPC数据
            List<Map<String, Object>> passAssemblyPC = (List<Map<String, Object>>) jsonData.get("passAssemblyPC");
            if (passAssemblyPC != null) {
                for (Map<String, Object> assembly : passAssemblyPC) {
                    processAssemblyData(assembly, dataList, "passAssemblyPC");
                }
            }

            // 处理notPassAssemblyPC数据
            List<Map<String, Object>> notPassAssemblyPC = (List<Map<String, Object>>) jsonData.get("notPassAssemblyPC");
            if (notPassAssemblyPC != null) {
                for (Map<String, Object> assembly : notPassAssemblyPC) {
                    processAssemblyData(assembly, dataList, "notPassAssemblyPC");
                }
            }

            // 处理lodElement数据
            List<Map<String, Object>> lodElement = (List<Map<String, Object>>) jsonData.get("lodElement");
            if (lodElement != null) {
                for (Map<String, Object> element : lodElement) {
                    processLodElementData(element, dataList);
                }
            }

            revitLOD.setData(dataList);
            return revitLOD;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("转换JSON数据到RevitLOD实体时发生异常: " + e.getMessage());
            return null;
        }
    }

    /**
     * 处理Assembly数据（passAssemblyPC和notPassAssemblyPC）
     * 修复：为每个assembly创建一个主dataItem，而不是为每个part创建dataItem
     */
    private void processAssemblyData(Map<String, Object> assembly, List<JSONObject> dataList, String type) {
        try {
            String assemblyName = getString(assembly, "name");
            String assemblyElementId = getString(assembly, "elementId");
            String assemblyIsPass = getString(assembly, "isPass");

            // 为整个assembly创建一个dataItem
            JSONObject dataItem = new JSONObject();
            dataItem.put("type", type);
            dataItem.put("assemblyName", assemblyName);
            dataItem.put("assemblyElementId", assemblyElementId);
            dataItem.put("assemblyIsPass", assemblyIsPass);
            // 使用assembly的elementId作为主要的elementId
            dataItem.put("elementId", assemblyElementId);
            dataItem.put("isPass", assemblyIsPass);
            
            // 收集所有parts信息
            List<Map<String, Object>> allParts = new ArrayList<>(); 
            List<Map<String, Object>> partsOne = (List<Map<String, Object>>) assembly.get("partsOne");
            if (partsOne != null) {
                for (Map<String, Object> part : partsOne) {
                    Map<String, Object> partInfo = new HashMap<>();
                    partInfo.put("elementId", getString(part, "elementId"));
                    partInfo.put("isPass", getString(part, "isPass"));
                    partInfo.put("interElementIds", part.get("interElementIds"));
                    partInfo.put("interNotElementIds", part.get("interNotElementIds"));
                    partInfo.put("partType", "partsOne");
                    allParts.add(partInfo);
                }
            }

            List<Map<String, Object>> partsElse = (List<Map<String, Object>>) assembly.get("partsElse");
            if (partsElse != null) {
                for (Map<String, Object> part : partsElse) {
                    Map<String, Object> partInfo = new HashMap<>();
                    partInfo.put("elementId", getString(part, "elementId"));
                    partInfo.put("isPass", getString(part, "isPass"));
                    partInfo.put("interElementIds", part.get("interElementIds"));
                    partInfo.put("interNotElementIds", part.get("interNotElementIds"));
                    partInfo.put("partType", "partsElse");
                    allParts.add(partInfo);
                }
            }
            
            // 将parts信息作为整体存储
            dataItem.put("parts", allParts);
            dataList.add(dataItem);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("处理Assembly数据时发生异常: " + e.getMessage());
        }
    }

    /**
     * 处理lodElement数据
     */
    private void processLodElementData(Map<String, Object> element, List<JSONObject> dataList) {
        try {
            JSONObject dataItem = new JSONObject();
            dataItem.put("type", "lodElement");
            dataItem.put("elementId", getString(element, "elementId"));
            dataItem.put("filename", getString(element, "filename"));
            dataItem.put("uniqueId", getString(element, "uniqueId"));
            dataItem.put("isPass", getString(element, "isPass"));
            dataItem.put("sucessNotElementIds", element.get("sucessNotElementIds"));
            dataList.add(dataItem);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("处理lodElement数据时发生异常: " + e.getMessage());
        }
    }

    /**
     * 格式化RevitLOD输出数据（保留原方法以兼容其他调用）
     */
    private List<Map<String, Object>> formatRevitLODOutput(RevitLOD revitLOD) {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            
            // 创建主要检测项
            Map<String, Object> mainItem = new HashMap<>();
            mainItem.put("id", revitLOD.getId());
            mainItem.put("checkId", revitLOD.getCheckId()); // 新增checkId字段
            mainItem.put("commitId", revitLOD.getCommitId()); // 新增commitId字段
            mainItem.put("label", revitLOD.getName());
            mainItem.put("valuePass", String.valueOf(revitLOD.getValuePass()));
            mainItem.put("valueNotPass", String.valueOf(revitLOD.getValueNotPass()));
            
            List<Map<String, Object>> details = new ArrayList<>();
            
            if (revitLOD.getData() != null) {
                for (JSONObject dataItem : revitLOD.getData()) {
                    Map<String, Object> detail = formatDetailItem(revitLOD, dataItem);
                    if (detail != null) {
                        details.add(detail);
                    }
                }
            }
            
            // 对details进行排序：不通过的(result=0)放在前面，通过的(result=1)放在后面
            if (!details.isEmpty()) {
                List<Map<String, Object>> sortedDetails = new ArrayList<>();
                
                // 先添加不通过的记录(result=0)
                for (Map<String, Object> detail : details) {
                    if (detail.containsKey("result") && Integer.valueOf(0).equals(detail.get("result"))) {
                        sortedDetails.add(detail);
                    }
                }
                
                // 再添加通过的记录(result=1)
                for (Map<String, Object> detail : details) {
                    if (detail.containsKey("result") && Integer.valueOf(1).equals(detail.get("result"))) {
                        sortedDetails.add(detail);
                    }
                }
                
                // 更新details
                details = sortedDetails;
            }
            
            mainItem.put("details", details);
            result.add(mainItem);
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("格式化RevitLOD输出数据时发生异常: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 格式化详细项数据
     * 修复：正确处理objId映射和type字段
     */
    private Map<String, Object> formatDetailItem(RevitLOD revitLOD, JSONObject dataItem) {
        try {
            Map<String, Object> detail = new HashMap<>();
            
            String isPass = dataItem.getString("isPass");
            int result = isPassValue(isPass) ? 1 : 0;
            detail.put("result", result);
            
            // 设置type字段 - 优先使用assemblyName
            String type = "";
            if (dataItem.containsKey("assemblyName") && !StringUtils.isEmpty(dataItem.getString("assemblyName"))) {
                type = dataItem.getString("assemblyName");
            } else if (dataItem.containsKey("elementId") && !StringUtils.isEmpty(dataItem.getString("elementId"))) {
                type = dataItem.getString("elementId");
            }
            detail.put("type", type);
            
            // 设置description字段
            String description = result == 1 ? "通过" : revitLOD.getName() + "不通过";
            detail.put("description", description);
            
            // 查询objId - 使用正确的elementId
            String elementId = null;
            // 对于assembly数据，使用assemblyElementId或elementId
            if (dataItem.containsKey("assemblyElementId") && !StringUtils.isEmpty(dataItem.getString("assemblyElementId"))) {
                elementId = dataItem.getString("assemblyElementId");
            } else if (dataItem.containsKey("elementId") && !StringUtils.isEmpty(dataItem.getString("elementId"))) {
                elementId = dataItem.getString("elementId");
            }
            
            String objId = "";
            if (!StringUtils.isEmpty(elementId)) {
                objId = getObjIdByElementId(elementId, revitLOD.getStreamId(), revitLOD.getBranchId(), revitLOD.getCommitId());
                if (objId == null) {
                    objId = "";
                    System.out.println("警告：未找到elementId=" + elementId + ", streamId=" + revitLOD.getStreamId() + ", commitId=" + revitLOD.getCommitId() + " 对应的objId");
                }
            }
            detail.put("objId", objId);
            
            return detail;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("格式化详细项数据时发生异常: " + e.getMessage());
            return null;
        }
    }

    /**
     * 根据elementId查询objId（带缓存优化）
     */
    private String getObjIdByElementId(String elementId, String streamId, String branchId, String commitId) {
        try {
            if (StringUtils.isEmpty(elementId) || StringUtils.isEmpty(streamId) || StringUtils.isEmpty(commitId)) {
                return null;
            }

            // 生成缓存键
            String cacheKey = "objId:" + streamId + ":" + elementId + ":" + commitId;
            
            // 尝试从Redis缓存获取
            String cachedObjId = (String) redisTemplate.opsForValue().get(cacheKey);
            if (cachedObjId != null) {
                if ("NOT_FOUND".equals(cachedObjId)) {
                    return null; // 缓存了"未找到"的结果
                }
                return cachedObjId;
            }

            ExpInstances expInstance = expInstancesService.searchExpInstances(streamId, elementId, commitId);
            if (expInstance != null) {
                String objId = expInstance.getObjId();
                // 缓存找到的结果，过期时间10分钟
                redisTemplate.opsForValue().set(cacheKey, objId, 10, TimeUnit.MINUTES);
                return objId;
            } else {
                // 缓存"未找到"的结果，过期时间2分钟
                redisTemplate.opsForValue().set(cacheKey, "NOT_FOUND", 2, TimeUnit.MINUTES);
                System.out.println("未找到elementId为 " + elementId + " 的ExpInstances记录");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("查询objId时发生异常: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 批量构建elementId到objId的映射，减少数据库查询次数
     */
    private Map<String, String> buildElementIdToObjIdMap(List<RevitLOD> revitLODList, String streamId, String branchId, String commitId) {
        Map<String, String> elementIdToObjIdMap = new HashMap<>();
        
        try {
            // 收集所有需要查询的elementId
            List<String> allElementIds = new ArrayList<>();
            for (RevitLOD revitLOD : revitLODList) {
                if (revitLOD.getData() != null) {
                    for (JSONObject dataItem : revitLOD.getData()) {
                        // 收集assemblyElementId
                        String assemblyElementId = dataItem.getString("assemblyElementId");
                        if (!StringUtils.isEmpty(assemblyElementId) && !allElementIds.contains(assemblyElementId)) {
                            allElementIds.add(assemblyElementId);
                        }
                        
                        // 收集elementId
                        String elementId = dataItem.getString("elementId");
                        if (!StringUtils.isEmpty(elementId) && !allElementIds.contains(elementId)) {
                            allElementIds.add(elementId);
                        }
                    }
                }
            }
            
            // 批量查询objId
            for (String elementId : allElementIds) {
                String objId = getObjIdByElementId(elementId, streamId, branchId, commitId);
                if (objId != null) {
                    elementIdToObjIdMap.put(elementId, objId);
                }
            }
            
            System.out.println("批量构建elementId到objId映射完成，映射数量: " + elementIdToObjIdMap.size());
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("批量构建elementId到objId映射时发生异常: " + e.getMessage());
        }
        
        return elementIdToObjIdMap;
    }
    
    /**
     * 批量构建elementId到objId的映射（用于最新记录查询，每条记录使用自身的commitId）
     */
    private Map<String, String> buildElementIdToObjIdMapForLatest(List<RevitLOD> revitLODList, String streamId, String branchId) {
        Map<String, String> elementIdToObjIdMap = new HashMap<>();
        
        try {
            // 收集所有需要查询的elementId及其对应的commitId
            Map<String, String> elementIdToCommitIdMap = new HashMap<>();
            for (RevitLOD revitLOD : revitLODList) {
                String commitId = revitLOD.getCommitId();
                if (revitLOD.getData() != null) {
                    for (JSONObject dataItem : revitLOD.getData()) {
                        // 收集assemblyElementId
                        String assemblyElementId = dataItem.getString("assemblyElementId");
                        if (!StringUtils.isEmpty(assemblyElementId) && !elementIdToCommitIdMap.containsKey(assemblyElementId)) {
                            elementIdToCommitIdMap.put(assemblyElementId, commitId);
                        }
                        
                        // 收集elementId
                        String elementId = dataItem.getString("elementId");
                        if (!StringUtils.isEmpty(elementId) && !elementIdToCommitIdMap.containsKey(elementId)) {
                            elementIdToCommitIdMap.put(elementId, commitId);
                        }
                    }
                }
            }
            
            // 批量查询objId，使用各自的commitId
            for (Map.Entry<String, String> entry : elementIdToCommitIdMap.entrySet()) {
                String elementId = entry.getKey();
                String commitId = entry.getValue();
                String objId = getObjIdByElementId(elementId, streamId, branchId, commitId);
                if (objId != null) {
                    elementIdToObjIdMap.put(elementId, objId);
                }
            }
            
            System.out.println("批量构建elementId到objId映射完成（最新记录模式），映射数量: " + elementIdToObjIdMap.size());
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("批量构建elementId到objId映射时发生异常: " + e.getMessage());
        }
        
        return elementIdToObjIdMap;
    }
    
    /**
     * 格式化RevitLOD输出数据（使用objId映射缓存）
     */
    private List<Map<String, Object>> formatRevitLODOutputWithCache(RevitLOD revitLOD, Map<String, String> elementIdToObjIdMap) {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            
            // 创建主要检测项
            Map<String, Object> mainItem = new HashMap<>();
            mainItem.put("id", revitLOD.getId());
            mainItem.put("checkId", revitLOD.getCheckId()); 
            mainItem.put("commitId", revitLOD.getCommitId()); // 新增commitId字段
            mainItem.put("label", revitLOD.getName());
            mainItem.put("valuePass", String.valueOf(revitLOD.getValuePass()));
            mainItem.put("valueNotPass", String.valueOf(revitLOD.getValueNotPass()));
            
            List<Map<String, Object>> details = new ArrayList<>();
            
            if (revitLOD.getData() != null) {
                for (JSONObject dataItem : revitLOD.getData()) {
                    Map<String, Object> detail = formatDetailItemWithCache(revitLOD, dataItem, elementIdToObjIdMap);
                    if (detail != null) {
                        details.add(detail);
                    }
                }
            }
            
            // 对details进行排序：不通过的(result=0)放在前面，通过的(result=1)放在后面
            if (!details.isEmpty()) {
                List<Map<String, Object>> sortedDetails = new ArrayList<>();
                
                // 先添加不通过的记录(result=0)
                for (Map<String, Object> detail : details) {
                    if (detail.containsKey("result") && Integer.valueOf(0).equals(detail.get("result"))) {
                        sortedDetails.add(detail);
                    }
                }
                
                // 再添加通过的记录(result=1)
                for (Map<String, Object> detail : details) {
                    if (detail.containsKey("result") && Integer.valueOf(1).equals(detail.get("result"))) {
                        sortedDetails.add(detail);
                    }
                }
                
                // 更新details
                details = sortedDetails;
            }
            
            mainItem.put("details", details);
            result.add(mainItem);
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("格式化RevitLOD输出数据时发生异常: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * 格式化详细项数据（使用objId映射缓存）
     */
    private Map<String, Object> formatDetailItemWithCache(RevitLOD revitLOD, JSONObject dataItem, Map<String, String> elementIdToObjIdMap) {
        try {
            Map<String, Object> detail = new HashMap<>();
            
            String isPass = dataItem.getString("isPass");
            int result = isPassValue(isPass) ? 1 : 0;
            detail.put("result", result);
            
            // 设置type字段 - 优先使用assemblyName
            String type = "";
            if (dataItem.containsKey("assemblyName") && !StringUtils.isEmpty(dataItem.getString("assemblyName"))) {
                type = dataItem.getString("assemblyName");
            } else if (dataItem.containsKey("elementId") && !StringUtils.isEmpty(dataItem.getString("elementId"))) {
                type = dataItem.getString("elementId");
            }
            detail.put("type", type);
            
            // 设置description字段
            String description = result == 1 ? "通过" : revitLOD.getName() + "不通过";
            detail.put("description", description);
            
            // 使用缓存的objId映射
            String elementId = null;
            if (dataItem.containsKey("assemblyElementId") && !StringUtils.isEmpty(dataItem.getString("assemblyElementId"))) {
                elementId = dataItem.getString("assemblyElementId");
            } else if (dataItem.containsKey("elementId") && !StringUtils.isEmpty(dataItem.getString("elementId"))) {
                elementId = dataItem.getString("elementId");
            }
            
            String objId = "";
            if (!StringUtils.isEmpty(elementId) && elementIdToObjIdMap.containsKey(elementId)) {
                objId = elementIdToObjIdMap.get(elementId);
                if (objId == null) {
                    objId = "";
                }
            }
            detail.put("objId", objId);
            
            return detail;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("格式化详细项数据时发生异常: " + e.getMessage());
            return null;
        }
    }

    // 辅助方法
    private String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    private Integer getInteger(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value != null) {
            if (value instanceof Integer) {
                return (Integer) value;
            } else {
                try {
                    return Integer.valueOf(value.toString());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }

    private Boolean getBoolean(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value != null) {
            if (value instanceof Boolean) {
                return (Boolean) value;
            } else {
                return Boolean.valueOf(value.toString());
            }
        }
        return null;
    }
    
    /**
     * 判断String类型的isPass值是否表示通过
     * 支持多种格式："true", "1", "pass", "yes", "通过" 等
     */
    private boolean isPassValue(String isPass) {
        if (isPass == null) {
            return false;
        }
        String value = isPass.toLowerCase().trim();
        return "true".equals(value) || "1".equals(value) || "pass".equals(value) || 
               "yes".equals(value) || "通过".equals(isPass.trim());
    }

    @Override
    public List<NewTemplate> getTemplateIdsByElementId(String elementId, String streamId, String branchId) {
        List<String> resultTemplateIds = new ArrayList<>();
        List<NewTemplate> result = new ArrayList<>();

        try {
            if (StringUtils.isEmpty(elementId) || StringUtils.isEmpty(streamId) || StringUtils.isEmpty(branchId)) {
                return null;
            }

            List<NewTemplate> newTemplates = newTemplateService.getTemplateListByStreamId(streamId,branchId);
            if (newTemplates == null || newTemplates.isEmpty()) {
                System.out.println("未找到streamId为 " + streamId + " 的模板数据");
                return null;
            }
            
            // 遍历每个NewTemplate，检查其TemplateData中是否包含目标elementId
            for (NewTemplate newTemplate : newTemplates) {
                String templateId = newTemplate.getId();
                if (StringUtils.isEmpty(templateId)) {
                    continue;
                }
                
                // 获取该templateId的所有TemplateData
                List<TemplateData> templateDataList = templateDataService.getByTemplateId(templateId);
                if (templateDataList == null || templateDataList.isEmpty()) {
                    continue;
                }
                
                // 判断是否为物料清单表模板（type=2），决定使用哪个字段进行校验
                boolean isMaterialListTemplate = "2".equals(newTemplate.getType());
                String searchFieldName = isMaterialListTemplate ? "elementIdV2" : "elementId";
                
                // 检查TemplateData中的data是否包含目标elementId或elementIdV2
                boolean containsElementId = false;
                for (TemplateData templateData : templateDataList) {
                    if (templateData.getData() != null) {
                        for (JSONObject dataItem : templateData.getData()) {
                            if (dataItem != null) {
                                // 对于物料清单表模板（type=2），检查elementIdV2字段；其他模板检查elementId字段
                                if (isMaterialListTemplate) {
                                    // 检查elementIdV2字段是否匹配
                                    if (checkElementIdV2Match(dataItem, elementId)) {
                                        containsElementId = true;
                                        break;
                                    }
                                } else {
                                    // 原有逻辑：检查JSON对象中是否包含目标elementId
                                    if (containsElementIdInJson(dataItem, elementId)) {
                                        containsElementId = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (containsElementId) {
                        break;
                    }
                }
                
                // 如果找到包含该elementId的templateData，则添加templateId到结果中
                if (containsElementId && !resultTemplateIds.contains(templateId)) {
                    resultTemplateIds.add(templateId);
                    NewTemplate templateById = newTemplateService.getTemplateById(templateId);
                    if (templateById != null) {
                        result.add(templateById);
                    }
                }
            }

//            List<NewTemplate> bomTemplate = newTemplateService.getBomTemplate(streamId,branchId);
//            for (NewTemplate newTemplate : bomTemplate) {
//                result.add(newTemplate);
//            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("查询包含elementId的templateId时发生异常: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 检查物料清单表模板（type=2）中的elementIdV2字段是否匹配
     * @param jsonObj JSON对象
     * @param targetElementId 目标elementId
     * @return 如果elementIdV2字段匹配则返回true，否则返回false
     */
    private boolean checkElementIdV2Match(JSONObject jsonObj, String targetElementId) {
        if (jsonObj == null || StringUtils.isEmpty(targetElementId)) {
            return false;
        }
        
        // 检查第一层是否有elementIdV2字段
        if (jsonObj.containsKey("elementIdV2")) {
            Object elementIdV2Value = jsonObj.get("elementIdV2");
            if (elementIdV2Value != null) {
                String elementIdV2Str = elementIdV2Value.toString();
                if (targetElementId.equals(elementIdV2Str)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 递归检查JSON对象中是否包含指定的elementId
     */
    private boolean containsElementIdInJson(JSONObject jsonObj, String targetElementId) {
        if (jsonObj == null || StringUtils.isEmpty(targetElementId)) {
            return false;
        }
        
        for (String key : jsonObj.keySet()) {
            Object value = jsonObj.get(key);
            
            // 如果值是字符串，直接比较
            if (value instanceof String) {
                if (targetElementId.equals(value.toString())) {
                    return true;
                }
            }
            // 如果值是JSONObject，递归检查
            else if (value instanceof JSONObject) {
                if (containsElementIdInJson((JSONObject) value, targetElementId)) {
                    return true;
                }
            }
            // 如果值是JSONArray，检查数组中的每个元素
            else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (Object item : array) {
                    if (item instanceof String && targetElementId.equals(item.toString())) {
                        return true;
                    } else if (item instanceof JSONObject) {
                        if (containsElementIdInJson((JSONObject) item, targetElementId)) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * 清理相关缓存
     */
    private void clearRelatedCache(String streamId, String branchId, String commitId) {
        try {
            if (StringUtils.isEmpty(streamId) || StringUtils.isEmpty(branchId) || StringUtils.isEmpty(commitId)) {
                return;
            }
            
            // 清理格式化输出缓存
            String formattedOutputCacheKey = "revit_lod_formatted_output:" + streamId + ":" + branchId + ":" + commitId;
            redisTemplate.delete(formattedOutputCacheKey);
            
            // 清理objId相关缓存（使用模糊匹配删除）
            String objIdPattern = "objId:" + streamId + ":*:" + commitId;
            clearCacheByPattern(objIdPattern);
            
            System.out.println("已清理相关缓存，streamId: " + streamId + ", branchId: " + branchId + ", commitId: " + commitId);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("清理缓存时发生异常: " + e.getMessage());
        }
    }
    
    /**
     * 根据模式清理缓存
     */
    private void clearCacheByPattern(String pattern) {
        try {
            // 使用scan命令查找匹配的键，避免使用keys命令影响性能
            java.util.Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                System.out.println("根据模式清理缓存: " + pattern + ", 清理数量: " + keys.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("根据模式清理缓存时发生异常: " + e.getMessage());
        }
    }
}
