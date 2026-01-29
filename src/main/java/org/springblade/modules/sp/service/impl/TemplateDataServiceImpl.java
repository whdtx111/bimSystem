package org.springblade.modules.sp.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.TemplateData;
import org.springblade.modules.sp.mapper.TemplateDataMapper;
import org.springblade.modules.sp.service.TemplateDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@DS("postgresql")
@Slf4j
public class TemplateDataServiceImpl extends BaseServiceImpl<TemplateDataMapper, TemplateData> implements TemplateDataService {

    @Autowired
    private TemplateDataMapper templateDataMapper;

    @Value("${external.api.bff.base-url:http://10.5.57.107:8100}")
    private String bffBaseUrl;

    @Override
    public TemplateData getById(String id) {
        try {
            return templateDataMapper.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject getDataByTemplateIdAndType(String templateId, String type, String branchId) {
        try {
            TemplateData templateData = templateDataMapper.getByTemplateIdAndType(templateId, type, branchId);
            if (templateData != null && templateData.getData() != null) {
                JSONObject result = new JSONObject();
                result.put("data", templateData.getData());
                return result;
            }
            return new JSONObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    @Override
    public TemplateData getByTemplateIdAndType(String templateId, String type, String branchId) {
        try {
            return templateDataMapper.getByTemplateIdAndType(templateId, type, branchId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<TemplateData> getByTemplateId(String templateId) {
        try {
            return templateDataMapper.getByTemplateId(templateId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<TemplateData> getByType(String type) {
        try {
            return templateDataMapper.getByType(type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addTemplateData(TemplateData templateData) {
        try {
            return templateDataMapper.addTemplateData(templateData);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteById(String id) {
        try {
            return templateDataMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteByTemplateIdAndType(String templateId, String type, String branchId) {
        try {
            return templateDataMapper.deleteByTemplateIdAndType(templateId, type, branchId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTemplateData(TemplateData templateData) {
        try {
            return templateDataMapper.updateTemplateData(templateData);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<TemplateData> getAllTemplateData() {
        try {
            return templateDataMapper.getAllTemplateData();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean hasDataByTemplateId(String templateId) {
        try {
            int count = templateDataMapper.countByTemplateId(templateId);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveMergedData(String templateId, String type, String branchId, String blockName) {
        try {
            // 1. 查询对应的templateData记录
            TemplateData templateData = templateDataMapper.getByTemplateIdAndType(templateId, type, branchId);
            
            if (templateData == null || templateData.getData() == null) {
                log.error("未找到templateData记录: templateId={}, type={}, branchId={}", templateId, type, branchId);
                throw new RuntimeException("未找到对应的模板数据记录");
            }
            
            List<JSONObject> templateDataList = templateData.getData();
            
            // 2. 调用外部by-block接口
            String byBlockUrl = bffBaseUrl + "/bff/pcmes/spu-flow/by-block?blockName=" + blockName;
            JSONObject byBlockData = callByBlockApi(byBlockUrl);
            
            if (byBlockData == null || byBlockData.getJSONArray("list") == null) {
                log.warn("by-block接口未返回数据，跳过合并: blockName={}", blockName);
                return false;
            }
            
            JSONArray byBlockList = byBlockData.getJSONArray("list");
            
            // 3. 遍历templateData的data数组，根据"构件编码"匹配by-block中的"构型"
            for (int i = 0; i < templateDataList.size(); i++) {
                JSONObject templateItem = templateDataList.get(i);
                String componentCode = templateItem.getString("构件编码");
                
                if (componentCode == null || componentCode.trim().isEmpty()) {
                    log.debug("templateData中第{}条记录的构件编码为空，跳过", i);
                    continue;
                }
                
                // 处理构件编码：加上-C后缀
                String processedComponentCode = componentCode + "-C";
                
                // 在by-block数据中查找匹配的"构型"
                JSONObject matchedByBlockItem = null;
                for (int j = 0; j < byBlockList.size(); j++) {
                    JSONObject byBlockItem = byBlockList.getJSONObject(j);
                    String modelType = byBlockItem.getString("构型");
                    
                    if (processedComponentCode.equals(modelType)) {
                        matchedByBlockItem = byBlockItem;
                        break;
                    }
                }
                
                // 如果找到匹配的数据，合并到templateData中
                if (matchedByBlockItem != null) {
                    log.debug("找到匹配: 构件编码={}, 构型={}", componentCode, componentCode);
                    
                    // 将by-block中的所有key-value合并到templateItem中
                    // 如果key相同则覆盖，如果key不存在则新增
                    for (Map.Entry<String, Object> entry : matchedByBlockItem.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        
                        // 跳过"构型"字段，因为它对应的是templateData中的"构件编码"
                        if ("构型".equals(key)) {
                            continue;
                        }
                        
                        // 覆盖或新增字段
                        templateItem.put(key, value);
                    }
                } else {
                    log.debug("未找到匹配的构型: 构件编码={}", componentCode);
                }
            }
            
            // 4. 更新templateData到数据库
            templateData.setData(templateDataList);
            boolean updateResult = templateDataMapper.updateTemplateData(templateData);
            
            if (updateResult) {
                log.info("成功保存合并后的数据: templateId={}, type={}, branchId={}", templateId, type, branchId);
            } else {
                log.error("保存合并后的数据失败: templateId={}, type={}, branchId={}", templateId, type, branchId);
            }
            
            return updateResult;
        } catch (Exception e) {
            log.error("保存合并数据失败", e);
            e.printStackTrace();
            throw new RuntimeException("保存合并数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 调用外部by-block接口
     */
    private JSONObject callByBlockApi(String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JSONObject responseJson = JSONObject.parseObject(response.getBody());
                // 假设返回格式为 {"code": 200, "data": {...}}
                if (responseJson.containsKey("data")) {
                    return responseJson.getJSONObject("data");
                }
                return responseJson;
            }
            return null;
        } catch (Exception e) {
            log.error("调用by-block接口失败: " + url, e);
            return null;
        }
    }

    @Override
    public boolean deleteByTemplateId(String templateId) {
        try {
            return templateDataMapper.deleteByTemplateId(templateId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
