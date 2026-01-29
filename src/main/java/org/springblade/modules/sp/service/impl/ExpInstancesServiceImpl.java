package org.springblade.modules.sp.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.ElementParameter;
import org.springblade.modules.sp.entity.ExpInstances;
import org.springblade.modules.sp.mapper.ExpInstancesMapper;
import org.springblade.modules.sp.service.ExpInstancesService;
import org.springblade.modules.sp.vo.ExpInstancesDetailVO;
import org.springblade.modules.sp.vo.NewBimListTreeVO;
import org.springblade.modules.sp.vo.ParameterDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * ExpInstances接口实现类
 * @author dengtx
 */
@Service
@DS("postgresql")
public class ExpInstancesServiceImpl extends BaseServiceImpl<ExpInstancesMapper, ExpInstances> implements ExpInstancesService {

    @Autowired
    private ExpInstancesMapper expInstancesMapper;


    public ExpInstances getExpInstances(String streamId, String objId, String commitId) {
        try {
            ExpInstances expInstances = expInstancesMapper.getExpInstances(streamId, objId, commitId);
            return expInstances;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ExpInstances> getExpInstancesFilter(String streamId, String commitId, String category, String family, String type) {
        try {
            List<ExpInstances> expInstances = expInstancesMapper.getExpInstancesFilter(streamId, category, family, type, commitId);
            return expInstances;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addExpInstances(ExpInstances expInstances) {
        try {
            if (ObjectUtils.isEmpty(expInstances)) {
                return false;
            }
            return expInstancesMapper.addExpInstances(expInstances);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteExpInstances(String commitId) {
        try {
            if (StringUtils.isEmpty(commitId)) {
                return false;
            }
            return expInstancesMapper.deleteExpInstances(commitId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteExpInstancesById(String Id) {
        try {
            if (StringUtils.isEmpty(Id)) {
                return false;
            }
            return expInstancesMapper.deleteExpInstancesById(Id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean updateExpInstances(ExpInstances expInstances){
//
//    }

    @Override
    public List<NewBimListTreeVO> BimListTreeNew(String streamId, String commitId) {
            //查找到当前stream下的记录
            List<NewBimListTreeVO> newBimListTreeVOS = expInstancesMapper.BimListTreeNew(streamId, commitId);
            //按层级分层
//        for (NewBimListTreeVO newBimListTreeVO : newBimListTreeVOS) {
//           if (newBimListTreeVO.getElementId().isEmpty() || "".equals(newBimListTreeVO.getElementId())){
//                newBimListTreeVO.setLv("null");
//           }
//
//        }
//        for (NewBimListTreeVO newBimListTreeVO : newBimListTreeVOS) {
//            newBimListTreeVO.setParameters(new JSONObject());
//        }
            return newBimListTreeVOS;
        }

        @Override
        public List<ExpInstances> getAllExpInstances (String streamId, String commitId){
            try {
                List<ExpInstances> allExpInstances = expInstancesMapper.getAllExpInstances(streamId, commitId);
                return allExpInstances;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public List<ExpInstances> getExpInstancesByElements (String streamId, String[]elementIds, String commitId){
            try {
                List<ExpInstances> allExpInstances = expInstancesMapper.getExpInstancesByElements(streamId, elementIds, commitId);
                return allExpInstances;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public ExpInstances searchExpInstances(String streamId, String elementId, String commitId) {
            try {
                ExpInstances exp = expInstancesMapper.searchExpInstances(streamId, elementId, commitId);
                return exp;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        public R<List<ElementParameter>> getParameterByElementId(String elementId, String branchId, String name, String category) {
            try {
                List<ElementParameter> elementParameters = new ArrayList<>();
                ExpInstances exp = expInstancesMapper.getParameterByElementId(elementId, branchId,category);
                if (exp == null) {
                    return R.data(elementParameters);
                }
                JSONObject jsonObject = exp.getParameters();
                // 获取 JSON 对象的所有键
                Set<String> keys = jsonObject.keySet();

                // 遍历所有键，检查每个键对应的值是否为 JSON 对象
                for (String key : keys) {
                    ElementParameter elementParameter = new ElementParameter();
                    Object value = jsonObject.get(key);

                    // 检查值是否是 JSON 对象
                    if (value instanceof JSONObject) {
                        JSONObject innerObject = (JSONObject) value;
                        elementParameter.setName(innerObject.getString("name"));
                        elementParameter.setUnits(innerObject.getString("units"));
                        elementParameter.setPkey(innerObject.getString("applicationInternalName"));
                        elementParameter.setCategory(exp.getCategory());
                        // 提取 "name" 和 "units" 属性
//                        String name = innerObject.getString("name");
//                        String units = innerObject.getString("units");

                        // 输出结果
//                        System.out.println("Key: " + key + ", Name: " + name + ", Units: " + (units != null ? units : "N/A"));
                    }
                    elementParameters.add(elementParameter);
                }

                // 使用 HashMap 来进行去重
                Map<String, ElementParameter> uniqueParameters = new HashMap<>();

                for (ElementParameter parameter : elementParameters) {
                    // 使用 name + category 作为唯一键
                    String key = parameter.getName() + "-" + parameter.getCategory();

                    // 如果 key 不存在或 category 为空，则进行去重
                    if (!uniqueParameters.containsKey(key)) {
                        uniqueParameters.put(key, parameter);
                    }
                }

                if (StringUtils.isEmpty(name) || "".equals(name)) {
                    return R.data(new ArrayList<>(uniqueParameters.values()));
                }else {
                    // 创建一个列表来存储筛选后的结果
                    List<ElementParameter> filteredParameters = new ArrayList<>();

                    // 根据 name 进行筛选
                    for (ElementParameter parameter : uniqueParameters.values()) {
                        if (StringUtils.isNotEmpty(parameter.getName())) {
                            if (parameter.getName().contains(name)) {
                                filteredParameters.add(parameter);
                            }
                        }
                    }
                    return R.data(filteredParameters); // 返回filteredParameters;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return R.fail(e.getMessage());
            }
        }

        @Override
        public boolean updateExpInstances(ExpInstances expInstances) {
            try {
                if (ObjectUtils.isEmpty(expInstances)) {
                    return false;
                }
                return expInstancesMapper.updateExpInstances(expInstances);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

    @Override
    public List<ExpInstancesDetailVO> getExpInstancesDetailByObjIds(String streamId,String branchId,String[] objIds) {
        try {
            if (objIds == null || objIds.length == 0) {
                return new ArrayList<>();
            }

            // 获取原始数据
            List<ExpInstances> instances = expInstancesMapper.getExpInstancesByObjIds(streamId,branchId,objIds);
            if (instances == null || instances.isEmpty()) {
                return new ArrayList<>();
            }

            // 转换为详细VO
            List<ExpInstancesDetailVO> detailVOList = new ArrayList<>();
            for (ExpInstances instance : instances) {
                ExpInstancesDetailVO detailVO = new ExpInstancesDetailVO();
                detailVO.setObjId(instance.getObjId());
                detailVO.setCategory(instance.getCategory());
                detailVO.setFamily(instance.getFamily());
                detailVO.setType(instance.getType());
                detailVO.setElementId(instance.getElementId());

                // 处理parameters参数，改为List<Map<String, String>>格式并格式化数值
                List<Map<String, String>> parameterList = new ArrayList<>();
                JSONObject parameters = instance.getParameters();
                if (parameters != null) {
                    for (String key : parameters.keySet()) {
                        Object value = parameters.get(key);
                        if (value instanceof JSONObject) {
                            JSONObject paramObj = (JSONObject) value;
                            String paramName = paramObj.getString("name");
                            String paramValue = paramObj.getString("value");
                            if (paramName != null) {
                                // 对数值进行格式化处理
                                String formattedValue = formatNumericValue(paramValue);
                                Map<String, String> paramMap = new HashMap<>();
                                paramMap.put("name", paramName);
                                paramMap.put("value", formattedValue);
                                parameterList.add(paramMap);
                            }
                        }
                    }
                }
                detailVO.setParameters(parameterList); // 修改这里，将原来的Map改为List

                detailVOList.add(detailVO);
            }

            return detailVOList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 格式化数值字符串
     * 如果是整数（包括.0结尾的）则去除小数点，如果是小数则保留两位小数
     * @param value 原始值
     * @return 格式化后的值
     */
    private String formatNumericValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return value;
        }

        try {
            // 尝试解析为数字
            double numValue = Double.parseDouble(value.trim());

            // 检查是否为整数（包括.0结尾的情况）
            if (numValue == Math.floor(numValue)) {
                // 是整数，返回整数格式
                return String.valueOf((long) numValue);
            } else {
                // 是小数，保留两位小数
                return String.format("%.2f", numValue);
            }
        } catch (NumberFormatException e) {
            // 不是数字，直接返回原值
            return value;
        }
    }

    @Override
    public ExpInstances getInstanceByElementIdAndStreamAndCommit(String elementId, String streamId, String commitId) {
        return expInstancesMapper.getInstanceByElementIdAndStreamAndCommit(elementId, streamId, commitId);
    }

    }
