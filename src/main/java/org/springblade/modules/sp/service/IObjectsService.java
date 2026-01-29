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
package org.springblade.modules.sp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.Objects;
import org.springblade.modules.sp.vo.HeaderVO;
import org.springblade.modules.sp.vo.ObjCountVO;
import org.springblade.modules.sp.vo.ObjParameterVO;
import org.springblade.modules.sp.vo.ObjectsVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 *  服务类
 *
 * @author wangc
 * @since 2023-08-14
 */
public interface IObjectsService extends BaseService<Objects> {

	R<ObjParameterVO> selectObjectParameters(String resourceId,String category) throws JsonProcessingException;

	R<List<ObjCountVO>> selectObjectParametersAllByStr(String resourceId, String str);

	R<List<String>> getParameterName(String resourceId);

	/**
	 * 比对两个构建的面片总数
	 * @param streamId 流ID
	 * @param objIdsA 构建A的对象ID数组
	 * @param objIdsB 构建B的对象ID数组
	 * @return 比对结果
	 */
	R<Map<String, Object>> compareFaceCount(String streamId, List<String> objIdsA, List<String> objIdsB);
}
