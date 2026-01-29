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

import org.springblade.modules.sp.vo.BimListTreeVO;

import java.util.List;

/**
 * Wbs任务表 服务类
 *
 * @author wangc
 * @since 2023-04-23
 */
public interface IBimService  {


    /***
     * 获取bim族分类树
     * @param resourceId
     * @return
     */
    List<BimListTreeVO> BimListTreeVO(String resourceId);

    List<BimListTreeVO> BimListTreeNew3(String resourceId, String streamId);

//    List<BimListTreeVO> updateBimListTree();
}
