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

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.dto.WbsInfoDTO;
import org.springblade.modules.sp.entity.WbsInfo;
import org.springblade.modules.sp.excel.WbsInfoExcel;
import org.springblade.modules.sp.vo.*;

import java.util.List;

/**
 * Wbs任务表 服务类
 *
 * @author wangc
 * @since 2023-04-23
 */
public interface IWbsInfoService extends BaseService<WbsInfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param wbsInfo
	 * @return
	 */
	IPage<WbsInfoVO> selectWbsInfoPage(IPage<WbsInfoVO> page, WbsInfoVO wbsInfo);


	/***
	 * 插入模型 构件
	 * 挂在wbs 下组成树
	 * @param wbsInfoDTO
	 */
	List<WbsInfoGanttVO> addWbsObject(WbsInfoDTO wbsInfoDTO);


	/**
	 * 导入数据
	 *
	 * @param data
	 * @return
	 */
	void importExcel(List<WbsInfoExcel> data);

	/**
	 * 树形结构
	 *
	 * @return
	 */
	List<WbsInfoTreeVO> tree(String streamsId);

	/***
	 * 获取甘特图数据
	 * @return
	 */
	List<WbsInfoGanttVO> selectGanttList(String streamsId);

	/***
	 * 获取ueList数据
	 * @return
	 */
	List<WbsInfoUEVO> selectUeList(String streamsId);

	/***
	 * 获取Ue数据详情
	 * @param objId
	 * @return
	 */
	WbsInfoUeDetailVO selectUeDetail(String objId);

}
