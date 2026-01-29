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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.sp.constant.SpConstant;
import org.springblade.modules.sp.dto.SnapshotDTO;
import org.springblade.modules.sp.entity.Snapshot;
import org.springblade.modules.sp.entity.WbsInfo;
import org.springblade.modules.sp.vo.SnapshotVO;
import org.springblade.modules.sp.mapper.SnapshotMapper;
import org.springblade.modules.sp.service.ISnapshotService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 快照 服务实现类
 *
 * @author wangc
 * @since 2023-11-22
 */
@Service
@DS("postgresql")
public class SnapshotServiceImpl extends BaseServiceImpl<SnapshotMapper, Snapshot> implements ISnapshotService {

	@Override
	public IPage<SnapshotVO> selectSnapshotPage(IPage<SnapshotVO> page, SnapshotVO snapshot) {
		return page.setRecords(baseMapper.selectSnapshotPage(page, snapshot));
	}

	@Override
	public R<Boolean> addSnapshot(SnapshotDTO snapshotDTO) {
		String uuid = UUID.randomUUID().toString();
//		// 覆盖原来的版本id
//		if (Func.isNotEmpty(snapshotDTO.getSnapshotUid())){
//			int delete = baseMapper.delete(
//					new LambdaQueryWrapper<Snapshot>().eq(Snapshot::getSnapshotUid, snapshotDTO.getSnapshotUid())
//			);
//		}
		List<Snapshot> snapshotList = snapshotDTO.getSnapshotList();
		// 设置uuid 和版本快照名称
		List<Snapshot> updatedSnapshotList = snapshotList.stream()
				.map(snapshot -> {
					snapshot.setSnapshotUid(uuid);
					snapshot.setSnapshotName(snapshotDTO.getSnapshotName());
					snapshot.setStreamId(snapshotDTO.getStreamId());
					snapshot.setCommitId(snapshotDTO.getCommitId());
					snapshot.setUserId(snapshotDTO.getUserId());
					snapshot.setCreateTime(new Date());
					snapshot.setSnapshotType(SpConstant.STRING_DEFAULT_VALUE_1);
					return snapshot;
				})
				.collect(Collectors.toList());
		return R.status(this.saveBatch(updatedSnapshotList));
	}

	@Override
	public R<Boolean> delSnapshot(String uid) {
		if (Func.isEmpty(uid)){
			return R.fail("版本uid不能为空");
		}
		int delete = baseMapper.delete(
				new LambdaQueryWrapper<Snapshot>().eq(Snapshot::getSnapshotUid, uid)
		);
		return R.success("删除成功!");
	}

	@Override
	public R<List<SnapshotDTO>> getSnapshotVo(String commitId) {
		List<SnapshotDTO> snapshot = baseMapper.getSnapshot(commitId);
		for (SnapshotDTO snapshotDTO : snapshot) {
			// obj数量
			snapshotDTO.setObjCount(
					baseMapper.selectCount(
							new LambdaQueryWrapper<Snapshot>()
									.eq(Snapshot::getSnapshotUid, snapshotDTO.getSnapshotUid())
									.eq(Snapshot::getIsDeleted,SpConstant.INT_DEFAULT_VALUE_0)
					).toString()
			);
			// obj数据
			snapshotDTO.setSnapshotList(
					baseMapper.selectList(
							new LambdaQueryWrapper<Snapshot>()
									.eq(Snapshot::getSnapshotUid, snapshotDTO.getSnapshotUid())
									.eq(Snapshot::getIsDeleted,SpConstant.INT_DEFAULT_VALUE_0)
					)
			);
		}
		snapshot.sort(Comparator.comparing(SnapshotDTO::getCreateTime).reversed());
		return R.data(snapshot);
	}

}
