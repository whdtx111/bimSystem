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
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.RulesLibrary;
import org.springblade.modules.sp.entity.Wbs;
import org.springblade.modules.sp.vo.RulesDetailVO;
import org.springblade.modules.sp.vo.RulesLibraryDetailVO;
import org.springblade.modules.sp.vo.RulesLibraryVO;

import java.util.List;

/**
 * 4a_用户 Mapper 接口
 *
 * @author wangc
 * @since 2023-09-13
 */
@Mapper
public interface RulesLibraryMapper extends BaseMapper<RulesLibrary> {

    RulesLibrary getRulesLibraryById(String id);

    List<RulesLibrary> getAll();

    Page<RulesLibrary> filterRulesLibrary(String name, String auth, String source, Integer status);

    boolean addRulesLibrary(RulesLibrary rulesLibrary);

    boolean insertRulesLibraryBatch(List<RulesLibrary> list);

    boolean updateRulesLibrary(RulesLibrary rulesLibrary);

    boolean deleteRulesLibraryById(String id);

    RulesLibraryDetailVO getRulesLibraryWithDetails(String id);

    List<RulesLibraryDetailVO> getAllRulesLibraryWithDetails(String streamId);

}
