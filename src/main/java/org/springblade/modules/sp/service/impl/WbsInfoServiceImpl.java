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
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.beanutils.BeanUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.modules.sp.constant.SpConstant;
import org.springblade.modules.sp.dto.ObjectDTO;
import org.springblade.modules.sp.dto.WbsInfoDTO;
import org.springblade.modules.sp.em.TypeEM;
import org.springblade.modules.sp.entity.WbsInfo;
import org.springblade.modules.sp.excel.WbsInfoExcel;
import org.springblade.modules.sp.mapper.WbsInfoMapper;
import org.springblade.modules.sp.service.IWbsInfoService;
import org.springblade.modules.sp.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Wbs任务表 服务实现类
 *
 * @author wangc
 * @since 2023-04-23
 */
@Service
@DS("postgresql")
public class WbsInfoServiceImpl extends BaseServiceImpl<WbsInfoMapper, WbsInfo> implements IWbsInfoService {


    @Override
    public IPage<WbsInfoVO> selectWbsInfoPage(IPage<WbsInfoVO> page, WbsInfoVO wbsInfo) {
        return page.setRecords(baseMapper.selectWbsInfoPage(page, wbsInfo));
    }

    @Override
    public List<WbsInfoGanttVO> addWbsObject(WbsInfoDTO wbsInfoDTO) {
        // 获取 被挂载的wbs 信息
        WbsInfo wbsParent = baseMapper.selectOne(
                new LambdaQueryWrapper<WbsInfo>().eq(WbsInfo::getWbsCode, wbsInfoDTO.getWbsCode())
        );
        List<ObjectDTO> objectDTOList = wbsInfoDTO.getObjectDTOList();
        // 返回前端 list 对象
        List<WbsInfoGanttVO> wbsInfoGanttVOS = new ArrayList<>();
        // 重新挂接构件到wbs
        objectDTOList.forEach(ObjectDTO -> {
            WbsInfo wbsInfo = Objects.requireNonNull(BeanUtil.copy(ObjectDTO, WbsInfo.class));
            wbsInfo.setInfoType(SpConstant.INFOTYPE_2); // 构件
            wbsInfo.setParentWbsCode(wbsInfoDTO.getWbsCode()); // 父级code
            wbsInfo.setWbsCode(SpConstant.MODEL + "_" + wbsInfo.getElementId()); // 重命名wbscode
            wbsInfo.setText(SpConstant.GOUJIAN+"_"+wbsInfo.getElementId()); // 构件
            // 设置构件信息 继承 wbs的参数信息
            wbsInfo.setStartDate(wbsParent.getStartDate());
            wbsInfo.setDeadline(wbsParent.getDeadline());
            wbsInfo.setPlannedStart(wbsParent.getPlannedStart());
            wbsInfo.setPlannedEnd(wbsParent.getPlannedEnd());
            wbsInfo.setEnglishText(SpConstant.MODEL + "_" + wbsInfo.getElementId()); // 重命名wbscode
            // wbs 的层级+1
            wbsInfo.setWbsLevel(
                    String.valueOf(
                    Integer.parseInt(wbsParent.getWbsLevel())+SpConstant.INT_DEFAULT_VALUE_1
                    )
            );
            this.save(wbsInfo);
            WbsInfoGanttVO wbsInfoGanttVO = copyE(wbsInfo, wbsParent);
            wbsInfoGanttVOS.add(wbsInfoGanttVO);
        });
        return wbsInfoGanttVOS;
    }

    @Override
    public void importExcel(List<WbsInfoExcel> data) {
        List<WbsInfo> list = new ArrayList<>();
        data.forEach(WbsInfoExcel -> {
            WbsInfo wbsInfo = Objects.requireNonNull(BeanUtil.copy(WbsInfoExcel, WbsInfo.class));
            wbsInfo.setInfoType(SpConstant.INFOTYPE_1); // wbs
            // 层级 01 1级 01-02 2级
            int level  =  StringUtils.countOccurrencesOf(wbsInfo.getWbsCode(), SpConstant.WBS_CODE_1) + SpConstant.INT_DEFAULT_VALUE_1;
            if (level>3){ // 构件
                wbsInfo.setInfoType(SpConstant.INFOTYPE_2);
            }
            wbsInfo.setWbsLevel(String.valueOf(level));
            // 截取父wbsCode
            if (StringUtil.isNotBlank(wbsInfo.getWbsCode())) {
                if (StringUtil.contains(wbsInfo.getWbsCode(), '-')) {
                    String parentCode = wbsInfo.getWbsCode().substring(SpConstant.INT_DEFAULT_VALUE_0, wbsInfo.getWbsCode().lastIndexOf(SpConstant.WBS_CODE_1));
                    wbsInfo.setParentWbsCode(parentCode);
                } else {
                    wbsInfo.setParentWbsCode(SpConstant.STRING_DEFAULT_VALUE_0);
                }
                list.add(wbsInfo);
            }
        });
        this.saveBatch(list);
    }

    @Override
    public List<WbsInfoTreeVO> tree(String streamsId) {
        return ForestNodeMerger.merge(baseMapper.tree(streamsId));
    }

    @Override
    public List<WbsInfoGanttVO> selectGanttList(String streamsId) {
        return baseMapper.selectGanttList(streamsId);
    }

    @Override
    public List<WbsInfoUEVO> selectUeList(String streamsId) {
        return baseMapper.uEList(streamsId);
    }

    @Override
    public WbsInfoUeDetailVO selectUeDetail(String objId) {
        return baseMapper.selectUeDetail(objId);
    }

    public WbsInfoGanttVO copyE (WbsInfo wbsInfo,WbsInfo wbsParent){
        WbsInfoGanttVO ganttVO = BeanUtil.copy(wbsInfo, WbsInfoGanttVO.class);
        ganttVO.setParent(wbsParent.getId());
        ganttVO.setOpen(SpConstant.INT_DEFAULT_VALUE_0);
        ganttVO.setProgress(SpConstant.STRING_DEFAULT_VALUE_0);
        return ganttVO;
    };

}
