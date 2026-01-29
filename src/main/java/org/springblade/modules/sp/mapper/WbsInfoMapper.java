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

import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.WbsInfo;
import org.springblade.modules.sp.vo.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.system.vo.DeptVO;

import java.util.List;

/**
 * Wbs任务表 Mapper 接口
 *
 * @author wangc
 * @since 2023-04-23
 */
public interface WbsInfoMapper extends BaseMapper<WbsInfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param wbsInfo
	 * @return
	 */
	List<WbsInfoVO> selectWbsInfoPage(IPage page, WbsInfoVO wbsInfo);

	/**
	 * 获取树形节点
	 *
	 * @return
	 */
	List<WbsInfoTreeVO> tree(String streamsId);

	/**
	 * list 树
	 *
	 * @return
	 */
	List<WbsInfoUEVO> uEList(String streamsId);

	/***
	 * 获取甘特图数据
	 * @return
	 */
	List<WbsInfoGanttVO> selectGanttList(String streamsId);

	/***
	 * 获取ue详情
	 * @param objId
	 * @return
	 */
	WbsInfoUeDetailVO selectUeDetail(String objId);


	List<BimListTreeVO> selectBimCategoryList(String streamsId);

}
