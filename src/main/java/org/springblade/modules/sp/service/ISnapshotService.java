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

import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.SnapshotDTO;
import org.springblade.modules.sp.entity.Snapshot;
import org.springblade.modules.sp.vo.SnapshotVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 快照 服务类
 *
 * @author wangc
 * @since 2023-11-22
 */
public interface ISnapshotService extends BaseService<Snapshot> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param snapshot
	 * @return
	 */
	IPage<SnapshotVO> selectSnapshotPage(IPage<SnapshotVO> page, SnapshotVO snapshot);

	/***
	 * 版本快照 新增
	 * @param snapshotDTO
	 * @return
	 */
	R<Boolean> addSnapshot(SnapshotDTO snapshotDTO);

	/***
	 * 版本快照 删除
	 * @param uid
	 * @return
	 */
	R delSnapshot(String uid);

	R<List<SnapshotDTO>> getSnapshotVo(String commitId);

}
