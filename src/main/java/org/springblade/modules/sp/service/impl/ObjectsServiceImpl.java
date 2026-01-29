/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.RedisUtil;
import org.springblade.modules.sp.constant.SpConstant;
import org.springblade.modules.sp.entity.Objects;
import org.springblade.modules.sp.utils.JsonFlattener;
import org.springblade.modules.sp.utils.JsonFormatUtils;
import org.springblade.modules.sp.vo.*;
import org.springblade.modules.sp.mapper.ObjectsMapper;
import org.springblade.modules.sp.service.IObjectsService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *  服务实现类
 *
 * @author wangc
 * @since 2023-08-14
 */
@Service
@DS("postgresql")
public class ObjectsServiceImpl extends BaseServiceImpl<ObjectsMapper, Objects> implements IObjectsService {

	@Autowired
	RedisUtil redisUtil;

	@Override
	public R<ObjParameterVO> selectObjectParameters(String resourceId,String category) throws JsonProcessingException {
		ObjParameterVO parameterVO = new ObjParameterVO();
		// 缓存处理
		if (redisUtil.hasKey(SpConstant.SP_REDIS_OBJ_KEY+resourceId+category)){
			parameterVO = (ObjParameterVO) redisUtil.get(SpConstant.SP_REDIS_OBJ_KEY+resourceId+category);
			return R.data(parameterVO);
		}
		Set<HeaderVO> header_set = new LinkedHashSet<>();
		List<Map<String,String>> mapList = new ArrayList<>();
		List<ObjectsVO> vos =  new ArrayList<>();
		// 全部的id 查询包含 elments层级
		List<ObjectsVO> objectsVOS = baseMapper.selectObjectsList(resourceId, category);
		// 查询出实例层级 inst层级
		List<ObjectsVO> referencedIdVOS = baseMapper.selectObjectsRefIdNotNull(resourceId, category);
		for (ObjectsVO referencedIdVO : referencedIdVOS) {
			getReferencedIdVO(objectsVOS,referencedIdVO,vos);
		}
		// 删除有问题的
		List<ObjectsVO> toRemove = objectsVOS.stream()
				.filter(obj -> SpConstant.REMOVESIGN.equals(obj.getSign()))
				.collect(Collectors.toList());
		objectsVOS.removeAll(toRemove);
		// 把newList的数据放到 原数据中
		objectsVOS.addAll(vos);
		for (ObjectsVO objectsVO : objectsVOS) {
			// 表头 json
			List<HeaderVO> jsonHeaderList = JsonFormatUtils.getJsonHeaderList(objectsVO.getParameters());
			Map<String, String> jsonDataMap = JsonFormatUtils.getJsonDataMap(objectsVO.getParameters());
			// 放入去重
			header_set.addAll(jsonHeaderList);
			// 放入 base字段
			getBaseDataFields(jsonDataMap,objectsVO);
			// 存入data
			mapList.add(jsonDataMap);
		}
		// 放入base表头
		getBaseHeader(header_set);
		parameterVO.setHeaderVOList(header_set.stream().collect(Collectors.toList()));
		parameterVO.setParameterData(mapList);
		// 放入缓存 120分钟
		redisUtil.set(SpConstant.SP_REDIS_OBJ_KEY+resourceId+category,parameterVO, SpConstant.REDIS_TIME_2880, TimeUnit.MINUTES);
        return R.data(parameterVO);
	}

	@Override
	public R<List<ObjCountVO>> selectObjectParametersAllByStr(String resourceId,String str) {
		List<ObjectsVO> vos =  new ArrayList<>();
		List<ObjCountVO> objData = new ArrayList<>();
		// 缓存处理
		if (redisUtil.hasKey(SpConstant.SP_REDIS_OBJ_PARAMETER_KEY+resourceId+str)){
			objData = (List<ObjCountVO>) redisUtil.get(SpConstant.SP_REDIS_OBJ_PARAMETER_KEY + resourceId + str);
			return R.data(objData);
		}
		Map<String,Integer> maps = new HashMap<>();
		List<Map<String,String>> mapList = new ArrayList<>();
		// 全部的id 查询包含 elments层级
		List<ObjectsVO> objectsVOS = baseMapper.selectObjectsList(resourceId, null);
		// 查询出实例层级 inst层级
		List<ObjectsVO> referencedIdVOS = baseMapper.selectObjectsRefIdNotNull(resourceId, null);
		for (ObjectsVO referencedIdVO : referencedIdVOS) {
			getReferencedIdVO(objectsVOS,referencedIdVO,vos);
		}
		// 删除有问题的
		List<ObjectsVO> toRemove = objectsVOS.stream()
				.filter(obj -> SpConstant.REMOVESIGN.equals(obj.getSign()))
				.collect(Collectors.toList());
		objectsVOS.removeAll(toRemove);
		// 把newList的数据放到 原数据中
		objectsVOS.addAll(vos);
		for (ObjectsVO objectsVO : objectsVOS) {
			String s = new String();
			s = JsonFormatUtils.selectJsonDataMapByStr(objectsVO.getParameters(), str);
			if (Func.isEmpty(s)){
				s = "N/A";
			}
			if (maps.containsKey(s)){
				Integer integer = maps.get(s);
				maps.put(s,integer+1);
			}else {
				maps.put(s,1);
			}
			Map<String, String> map = new HashMap<>();
			map.put("key",s);
			map.put("value",objectsVO.getId());
			mapList.add(map);
		}
		// 合并相同键的值
		Map<String, String> mergedMap = mapList.stream()
				.collect(Collectors.toMap(
						map -> map.get("key"),  // 键
						map -> map.get("value"),  // 值
						(value1, value2) -> value1 + "," + value2  // 合并相同键的值
				));
		objData = getObjData(mergedMap, maps);
		// 放入缓存 120分钟
		redisUtil.set(SpConstant.SP_REDIS_OBJ_PARAMETER_KEY+resourceId+str,objData);
		return R.data(objData);
	}


	@Override
	public R<List<String>> getParameterName(String resourceId) {
		List<ObjectsVO> vos =  new ArrayList<>();
		Set<String> names = new HashSet<>();
		List<String> newList = new ArrayList<>();
		// 缓存处理
		if (redisUtil.hasKey(SpConstant.SP_REDIS_OBJ_PARAMETER_NAME_KEY+resourceId)){
			newList = (List<String>) redisUtil.get(SpConstant.SP_REDIS_OBJ_PARAMETER_NAME_KEY + resourceId);
			return R.data(newList);
		}
		// 全部的id 查询包含 elments层级
		List<ObjectsVO> objectsVOS = baseMapper.selectObjectsList(resourceId, null);
		// 查询出实例层级 inst层级
		List<ObjectsVO> referencedIdVOS = baseMapper.selectObjectsRefIdNotNull(resourceId, null);
		for (ObjectsVO referencedIdVO : referencedIdVOS) {
			getReferencedIdVO(objectsVOS,referencedIdVO,vos);
		}
		// 删除有问题的
		List<ObjectsVO> toRemove = objectsVOS.stream()
				.filter(obj -> SpConstant.REMOVESIGN.equals(obj.getSign()))
				.collect(Collectors.toList());
		objectsVOS.removeAll(toRemove);
		// 把newList的数据放到 原数据中
		objectsVOS.addAll(vos);
		for (ObjectsVO objectsVO : objectsVOS) {
			List<String> jsonDataMapByName = JsonFormatUtils.getJsonDataMapByName(objectsVO.getParameters());
			// 去重
			names.addAll(jsonDataMapByName);
		}
		newList = new ArrayList<>(names);
		// 放入缓存 120分钟
		redisUtil.set(SpConstant.SP_REDIS_OBJ_PARAMETER_NAME_KEY+resourceId,newList, SpConstant.REDIS_TIME_2880, TimeUnit.MINUTES);
		return R.data(newList);
	}

	/**
	 * 比对两个构建的面片总数
	 * @param streamId 流ID
	 * @param objIdsA 构建A的对象ID数组
	 * @param objIdsB 构建B的对象ID数组
	 * @return 比对结果
	 */
	public R<Map<String, Object>> compareFaceCount(String streamId, List<String> objIdsA, List<String> objIdsB) {
		try {
			// 计算构建A的面片总数
			int faceCountA = calculateTotalFaceCount(streamId, objIdsA);

			// 计算构建B的面片总数
			int faceCountB = calculateTotalFaceCount(streamId, objIdsB);

			// 构建返回结果
			Map<String, Object> result = new HashMap<>();
			result.put("buildA_faceCount", faceCountA);
			result.put("buildB_faceCount", faceCountB);
			result.put("isEqual", faceCountA == faceCountB);
			result.put("difference", Math.abs(faceCountA - faceCountB));

			return R.data(result);
		} catch (Exception e) {
			return R.fail("面片比对计算失败: " + e.getMessage());
		}
	}

	/**
	 * 计算指定对象ID数组的总面片数
	 * @param streamId 流ID
	 * @param objIds 对象ID数组
	 * @return 总面片数
	 */
	private int calculateTotalFaceCount(String streamId, List<String> objIds) throws JsonProcessingException {
		int totalFaceCount = 0;
		ObjectMapper objectMapper = new ObjectMapper();

		for (String objId : objIds) {
			// 执行sqlA: 根据objId查询对象
			ObjectsVO objectA = baseMapper.selectObjectById(objId, streamId);
			if (objectA == null || Func.isEmpty(objectA.getParameters())) {
				continue;
			}

			// 解析data中的displayValue
			JsonNode dataNode = objectMapper.readTree(objectA.getParameters());
			JsonNode displayValueNode = dataNode.get("displayValue");
			if (displayValueNode == null || !displayValueNode.isArray() || displayValueNode.size() == 0) {
				continue;
			}

			JsonNode firstDisplayValue = displayValueNode.get(0);
			JsonNode referencedIdNode = firstDisplayValue.get("referencedId");
			if (referencedIdNode == null) {
				continue;
			}

			String referencedId = referencedIdNode.asText();

			// 执行sqlB: 根据referencedId查询对象
			ObjectsVO objectB = baseMapper.selectObjectById(referencedId, streamId);
			if (objectB == null || Func.isEmpty(objectB.getParameters())) {
				continue;
			}

			// 解析data中的faces
			JsonNode dataBNode = objectMapper.readTree(objectB.getParameters());
			JsonNode facesNode = dataBNode.get("faces");
			if (facesNode == null || !facesNode.isArray() || facesNode.size() == 0) {
				continue;
			}

			JsonNode firstFace = facesNode.get(0);
			JsonNode faceReferencedIdNode = firstFace.get("referencedId");
			if (faceReferencedIdNode == null) {
				continue;
			}

			String faceReferencedId = faceReferencedIdNode.asText();

			// 执行sqlC: 根据faces的referencedId查询最终对象
			ObjectsVO objectC = baseMapper.selectObjectById(faceReferencedId, streamId);
			if (objectC == null || Func.isEmpty(objectC.getParameters())) {
				continue;
			}

			// 计算data数组的个数
			JsonNode dataCNode = objectMapper.readTree(objectC.getParameters());
			if (dataCNode.has("data") && dataCNode.get("data").isArray()) {
				totalFaceCount += dataCNode.get("data").size();
			} else {
				// 如果没有data数组或data不是数组，则计算为1个面片
				totalFaceCount += 1;
			}
		}

		return totalFaceCount;
	}

	public static List<ObjCountVO> getObjData(Map<String, String> mergedMap, Map<String,Integer> maps){
		List<ObjCountVO> voList = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : maps.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
			ObjCountVO o = new ObjCountVO();
			o.setParameterName(key);
			o.setQuantity(String.valueOf(value));
			o.setId(mergedMap.get(key));
			voList.add(o);
        }
		return voList;
	}

	public static void getReferencedIdVO(List<ObjectsVO> objectsVOS ,ObjectsVO vo,List<ObjectsVO> vos) {
		for (ObjectsVO objectsVO : objectsVOS) {
			// 实例层级
			String oid = objectsVO.getId();
			// elments 层级
			String referencedId = vo.getReferencedId();
			if (
					oid.equals(referencedId)
			){
				objectsVO.setSign(SpConstant.REMOVESIGN);
				vo.setCategory(objectsVO.getCategory());
				vo.setFamily(objectsVO.getFamily());
				vo.setType(objectsVO.getType());
				// 把实例层级的 数据放入vos
				vo.setParameters(vo.getParameters());
				vos.add(vo);
			}
		}
	}



	// 添加基础字段
	public static void getBaseDataFields(Map<String,String> map , ObjectsVO vo) {
		map.put("id",vo.getId());
		map.put("streamId",vo.getStreamId());
		map.put("category",vo.getCategory());
		map.put("family",vo.getFamily());
		map.put("type",vo.getType());
		map.put("elementId",vo.getElementId());
	}



	// 添加基础表头
	public static void getBaseHeader(Set<HeaderVO> headerVOS) {
		for (String s : SpConstant.BASE_HEADER) {
			HeaderVO headerVO = new HeaderVO();
			headerVO.setName(s);
			headerVO.setValue(s);
			headerVOS.add(headerVO);
		}
		HeaderVO headerf = new HeaderVO();
		headerf.setName(SpConstant.FAMILY_NAME);
		headerf.setValue(SpConstant.FAMILY);
		HeaderVO headert = new HeaderVO();
		headert.setName(SpConstant.TYPE_NAME);
		headert.setValue(SpConstant.TYPE);
		headerVOS.add(headerf);
		headerVOS.add(headert);
	}

}
