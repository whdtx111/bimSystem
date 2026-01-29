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

import org.springblade.modules.sp.dto.ObjSnapshotDTO;
import org.springblade.modules.sp.dto.SnapshotDTO;
import org.springblade.modules.sp.entity.ObjSnapshot;
import org.springblade.modules.sp.vo.ObjSnapshotVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * 模型 色值 和 剖切快照 Mapper 接口
 *
 * @author wangc
 * @since 2023-12-06
 */
public interface ObjSnapshotMapper extends BaseMapper<ObjSnapshot> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param objSnapshot
	 * @return
	 */
	List<ObjSnapshotVO> selectObjSnapshotPage(IPage page, ObjSnapshotVO objSnapshot);


	/***
	 * 获取版本快照
	 * @param commitId
	 * @return
	 */
	List<ObjSnapshotDTO> getObjSnapshotList(String commitId);
}
