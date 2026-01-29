/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.service.IBimService;
import org.springblade.modules.sp.service.IWbsInfoService;
import org.springblade.modules.sp.vo.BimListTreeVO;
import org.springblade.modules.sp.vo.WbsInfoUeDetailVO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ue端接口 控制器
 *
 * @author wangc
 * @since 2023年12月05日09:26:33
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sp/ue")
@CrossOrigin
@Api(value = "ue端接口接口", tags = "ue端接口接口")
public class UeController extends BladeController {

	private IBimService bimService;
	private IWbsInfoService wbsInfoService;
	/**
	 * bim结构树
	 * wangc 2023年12月05日09:26:54
	 * @return
	 */
	@GetMapping("/bimCategoryTreeList-new3")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "bim结构树", notes = "bim结构树")
	public R<List<BimListTreeVO>> bimCategoryTreeListNew3 (String resourceId, String streamId) {
		List<BimListTreeVO> bimListTreeVOS = bimService.BimListTreeNew3(resourceId, streamId);
		return R.data(bimListTreeVOS);
	}

	/**
	 * Ue详情数据结构
	 * wangc 2023年12月05日09:28:09
	 * @return
	 */
	@GetMapping("/ueDetail")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "Ue详情数据结构", notes = "Ue详情数据结构")
	public R<WbsInfoUeDetailVO> ueDetail (String objId) {
		WbsInfoUeDetailVO detailVO = wbsInfoService.selectUeDetail(objId);
		return R.data(detailVO);
	}

}


