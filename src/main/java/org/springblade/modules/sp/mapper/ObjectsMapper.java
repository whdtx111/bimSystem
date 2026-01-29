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
package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.Objects;
import org.springblade.modules.sp.vo.ObjectsVO;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author wangc
 * @since 2023-08-14
 */
public interface ObjectsMapper extends BaseMapper<Objects> {


	List<ObjectsVO> selectObjectsList(@Param("resourceId") String resourceId,
									  @Param("category") String category);

	List<ObjectsVO> selectObjectsRefIdNotNull(
			                         @Param("resourceId") String resourceId,
									 @Param("category") String category);

	/**
	 * 根据id和streamId查询单个对象
	 */
	ObjectsVO selectObjectById(@Param("id") String id, @Param("streamId") String streamId);
}
