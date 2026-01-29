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
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.tool.utils.CollectionUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.RedisUtil;
import org.springblade.modules.sp.constant.SpConstant;
import org.springblade.modules.sp.mapper.BimMapper;
import org.springblade.modules.sp.service.IBimService;
import org.springblade.modules.sp.vo.BimListTreeVO;
import org.springblade.modules.sp.vo.BimObjVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Wbs任务表 服务实现类
 *
 * @author wangc
 * @since 2023-04-23
 */
@Service
@DS("postgresql")
public class BimServiceImpl implements IBimService {

    @Autowired
    BimMapper bimMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<BimListTreeVO> BimListTreeVO(String resourceId) {
        List<BimListTreeVO> bimListTreeVOS = new ArrayList<>();
        // redis 缓存存放
        if (redisUtil.hasKey(resourceId)) {
            bimListTreeVOS = (List<BimListTreeVO>) redisUtil.get(resourceId);
            return bimListTreeVOS;
        }
        bimListTreeVOS = bimMapper.selectBimCategoryList(resourceId);
        bimListTreeVOS.stream().filter(o -> o.getLevel().equals("category"))
                .forEach(f -> f.setParentId("0"));
        // 不存在空
        if (CollectionUtil.isNotEmpty(bimListTreeVOS)) {
            // 放入缓存 120分钟
            redisUtil.set(resourceId, bimListTreeVOS, 120L, TimeUnit.MINUTES);
        }
        return bimListTreeVOS;
    }


    @Override
    public List<BimListTreeVO> BimListTreeNew3(String resourceId, String streamId) {
        List<BimListTreeVO> bimListTreeVOS = new ArrayList<>();
        // redis 缓存存放
        if (redisUtil.hasKey(SpConstant.SP_REDIS_TREE_KEY+resourceId)) {
            bimListTreeVOS = (List<BimListTreeVO>) redisUtil.get(SpConstant.SP_REDIS_TREE_KEY+resourceId);
            return bimListTreeVOS;
        }
        // 初始化 造数据 sp的数据
        List<BimObjVO> bimObjVOS = bimMapper.selectBimObjList(resourceId, streamId);
        // 获取没有 Instance 层级的数据
        List<BimObjVO> emptyReferencedIds = bimObjVOS.stream()
                .filter(obj -> StringUtils.isEmpty(obj.getReferencedId()))
                .collect(Collectors.toList());

        // 完成层级拼装 去除重复元素
        List<BimListTreeVO> dataInit = getDataInit2(emptyReferencedIds);
        Set<BimListTreeVO> set = new HashSet<>(dataInit);
        List<BimListTreeVO> completeList = new ArrayList<>(set);

        // 获取有 Instance 层级的数据 拼接第五层
        List<BimObjVO> nonEmptyReferencedIds = bimObjVOS.stream()
                .filter(obj -> StringUtils.isNotEmpty(obj.getReferencedId()))
                .collect(Collectors.toList());
        List<BimListTreeVO> newList = new ArrayList<>();
        for (BimObjVO nonEmptyReferencedId : nonEmptyReferencedIds) {
//            nonEmptyReferencedId.setDefinitionElementIdList(new String[0]);
            bimMapper.addBimObj(nonEmptyReferencedId);
            getByParent2(completeList, nonEmptyReferencedId,newList);
        }
        // 把newList的数据放到 completeList中
        completeList.addAll(newList);
        // 删除有第四级的
        List<BimListTreeVO> toRemove = completeList.stream()
                .filter(obj -> SpConstant.REMOVESIGN.equals(obj.getSpeckleType()))
                .collect(Collectors.toList());
        completeList.removeAll(toRemove);
        bimListTreeVOS = completeList;
        // 不存在空
        if (CollectionUtil.isNotEmpty(bimListTreeVOS)) {
            // 放入缓存 120分钟
            redisUtil.set(SpConstant.SP_REDIS_TREE_KEY+resourceId, bimListTreeVOS, SpConstant.REDIS_TIME_2880, TimeUnit.MINUTES);
        }
        return bimListTreeVOS;
    }

    private static BimListTreeVO getByParent2(List<BimListTreeVO> completeList, BimObjVO nonEmptyReferencedVO,List<BimListTreeVO> newList ) {
        for (BimListTreeVO complete : completeList) {
            if (
                   SpConstant.ELEMENTID.equals(complete.getLevel())
            ){
                if ( complete.getObjId().equals(nonEmptyReferencedVO.getReferencedId())){
                    // 拼接第五层
                    BimListTreeVO thisVo = new BimListTreeVO();
                    String replace = complete.getId().replace("|"+complete.getObjId(), "");
                    thisVo.setId(replace +"|"+nonEmptyReferencedVO.getElementId());
                    thisVo.setParentId(complete.getParentId());
                    thisVo.setLabel(nonEmptyReferencedVO.getElementId());
                    thisVo.setObjId(nonEmptyReferencedVO.getObjId());
                    thisVo.setLevel(SpConstant.ELEMENTID);
                    complete.setSpeckleType(SpConstant.REMOVESIGN);
                    newList.add(thisVo);
                }
            }
        }
        return null; // 如果没有找到匹配的对象，返回null
    }


    // 造数据
    private List<BimListTreeVO> getDataInit2(List<BimObjVO> bimObjVOS) {
        List<BimListTreeVO> deduplicatedList = new ArrayList<>();
        for (BimObjVO original : bimObjVOS) {
            //  'category' AS "level"
            if (Func.isNotEmpty(original.getCategory())) {
                BimListTreeVO category = new BimListTreeVO();
                 // 给不必要展示的数据打上标签 等下统一删除
                if (
                        SpConstant.REMOVE_XIANGJI.equals(original.getCategory()) ||
                        SpConstant.REMOVE_CAIZHI.equals(original.getCategory())  ||
                        SpConstant.REMOVE_SHITU.equals(original.getCategory())
                ){
                    category.setSpeckleType(SpConstant.REMOVESIGN);
                }
                category.setLabel(original.getCategory());
                category.setObjId(original.getObjId());
                if (
                        Func.isNotEmpty(original.getCategory()) &&
                                Func.isNotEmpty(original.getFamily()) &&
                                Func.isNotEmpty(original.getType()) &&
                                Func.isNotEmpty(original.getElementId())

                ) {
                    category.setObjId(null);
                }
                category.setId(original.getCategory());
                category.setParentId("0");
                category.setLevel(SpConstant.CATEGORY);
                deduplicatedList.add(category);
            }
            //  'family' AS "level"
            if (Func.isNotEmpty(original.getCategory()) && Func.isNotEmpty(original.getFamily())) {
                BimListTreeVO family = new BimListTreeVO();
                family.setLabel(original.getFamily());
                family.setId(original.getCategory() + "|" + original.getFamily());
                family.setParentId(original.getCategory());
                family.setLevel(SpConstant.FAMILY);
                deduplicatedList.add(family);
            }
            //  'type' AS "level"
            if (Func.isNotEmpty(original.getFamily()) && Func.isNotEmpty(original.getType())) {
                BimListTreeVO type = new BimListTreeVO();
                type.setLabel(original.getType());
                type.setId(original.getCategory() + "|" + original.getFamily() + "|" + original.getType());
                type.setParentId(original.getCategory() + "|" + original.getFamily());
                type.setLevel(SpConstant.TYPE);
                deduplicatedList.add(type);
            }
            //  'elementId' AS "level"
            if (Func.isNotEmpty(original.getType()) && Func.isNotEmpty(original.getElementId())) {
                BimListTreeVO elementId = new BimListTreeVO();
                elementId.setObjId(original.getObjId());
                elementId.setLabel(original.getElementId());
                elementId.setId(original.getCategory() + "|" + original.getFamily() + "|" + original.getType() + "|" + original.getObjId());
                elementId.setParentId(original.getCategory() + "|" + original.getFamily() + "|" + original.getType());
                elementId.setLevel(SpConstant.ELEMENTID);
                deduplicatedList.add(elementId);
            }
        }
        return deduplicatedList;
    }


}



//    public static void main(String[] args) {
//        List<BimObjVO> bimObjVOS = new ArrayList<>();
//        BimObjVO o1 = new BimObjVO();
//        o1.setId("11");
//        o1.setSpeckleType("Objects.Other.Revit.RevitInstance:Objects.BuiltElements.Revit.RevitMEPFamilyInstance");
//        o1.setReferencedId("2");
//        o1.setSign("yes");
//
//        BimObjVO o2 = new BimObjVO();
//        o2.setId("2");
//        o2.setCategory("efd");
//
//        BimObjVO o3 = new BimObjVO();
//        o3.setId("3");
//        o3.setCategory("abc");
//
//        BimObjVO o4 = new BimObjVO();
//        o4.setId("44");
//        o4.setSpeckleType("Objects.Other.Revit.RevitInstance:Objects.BuiltElements.Revit.RevitMEPFamilyInstance");
//        o4.setReferencedId("3");
//        o1.setSign("yes");
//
//        bimObjVOS.add(o1);
//        bimObjVOS.add(o2);
//        bimObjVOS.add(o3);
//        bimObjVOS.add(o4);
//
//        // 查找type 的对象
//        for (BimObjVO obj : bimObjVOS) {
//            if (SpConstant.SPECKLE_TYPE_OTHER.equals(obj.getSpeckleType())) {
//                // 修改属性
//                findDataEdit(bimObjVOS, obj);
//            }
//        }
//        // 把list中type的值删掉
//        bimObjVOS.removeIf(obj -> SpConstant.SPECKLE_TYPE_OTHER.equals(obj.getSpeckleType()));
//
//    }


