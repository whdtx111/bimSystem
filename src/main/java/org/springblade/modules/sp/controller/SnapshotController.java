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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.constant.SpConstant;
import org.springblade.modules.sp.dto.SnapshotDTO;
import org.springblade.modules.sp.entity.Snapshot;
import org.springblade.modules.sp.service.ISnapshotService;
import org.springblade.modules.sp.utils.SignAuth;
import org.springblade.modules.sp.vo.SnapshotVO;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * 快照 控制器
 *
 * @author wangc
 * @since 2023-11-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sp/snapshot")
@CrossOrigin
@Api(value = "快照", tags = "快照")
public class SnapshotController extends BladeController {

	private ISnapshotService snapshotService;

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入snapshot")
	public R<Boolean> save(@RequestBody SnapshotDTO snapshotDTO) {
		boolean b = SignAuth.preHandle();
		if (!b){
			return R.fail(SpConstant.STR_MSG);
		}
		return snapshotService.addSnapshot(snapshotDTO);
	}


	/**
	 * 删除
	 */
	@GetMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "uid", required = true) @RequestParam String snapshotUid) {
		boolean b = SignAuth.preHandle();
		if (!b){
			return R.fail(SpConstant.STR_MSG);
		}
		return snapshotService.delSnapshot(snapshotUid);
	}

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value =  "详情", notes = "传入snapshot")
	public R<List<SnapshotDTO>> detail(@ApiParam(value = "commitId", required = true) @RequestParam String commitId) {
		boolean b = SignAuth.preHandle();
		if (!b){
			return R.fail(SpConstant.STR_MSG);
		}
		return snapshotService.getSnapshotVo(commitId);
	}

	//---------------------------------------------------------




	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入snapshot")
	public R<IPage<Snapshot>> list(Snapshot snapshot, Query query) {
		IPage<Snapshot> pages = snapshotService.page(Condition.getPage(query), Condition.getQueryWrapper(snapshot));
		return R.data(pages);
	}

	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入snapshot")
	public R<IPage<SnapshotVO>> page(SnapshotVO snapshot, Query query) {
		IPage<SnapshotVO> pages = snapshotService.selectSnapshotPage(Condition.getPage(query), snapshot);
		return R.data(pages);
	}


	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入snapshot")
	public R update(@Valid @RequestBody Snapshot snapshot) {
		return R.status(snapshotService.updateById(snapshot));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入snapshot")
	public R submit(@Valid @RequestBody Snapshot snapshot) {
		return R.status(snapshotService.saveOrUpdate(snapshot));
	}


	
}
