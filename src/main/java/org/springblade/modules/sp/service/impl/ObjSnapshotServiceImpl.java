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
import org.springblade.modules.sp.dto.ObjSnapshotDTO;
import org.springblade.modules.sp.entity.ObjSnapshot;
import org.springblade.modules.sp.vo.ObjSnapshotVO;
import org.springblade.modules.sp.mapper.ObjSnapshotMapper;
import org.springblade.modules.sp.service.IObjSnapshotService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 模型 色值 和 剖切快照 服务实现类
 *
 * @author wangc
 * @since 2023-12-06
 */
@Service
@DS("postgresql")
public class ObjSnapshotServiceImpl extends BaseServiceImpl<ObjSnapshotMapper, ObjSnapshot> implements IObjSnapshotService {

	@Override
	public IPage<ObjSnapshotVO> selectObjSnapshotPage(IPage<ObjSnapshotVO> page, ObjSnapshotVO objSnapshot) {
		return page.setRecords(baseMapper.selectObjSnapshotPage(page, objSnapshot));
	}

	@Override
	public List<ObjSnapshotDTO> selectObjSnapshotList(String commitId) {
		return baseMapper.getObjSnapshotList(commitId);
	}

}
