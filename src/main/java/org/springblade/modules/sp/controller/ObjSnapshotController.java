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
import lombok.SneakyThrows;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.sp.constant.SpConstant;
import org.springblade.modules.sp.dto.ObjSnapshotDTO;
import org.springblade.modules.sp.entity.Comment;
import org.springblade.modules.sp.entity.ObjSnapshot;
import org.springblade.modules.sp.entity.StreamFiles;
import org.springblade.modules.sp.service.CommentService;
import org.springblade.modules.sp.service.FileService;
import org.springblade.modules.sp.service.IObjSnapshotService;
import org.springblade.modules.sp.utils.SignAuth;
import org.springblade.modules.sp.vo.ObjSnapshotVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模型 色值 和 剖切快照 控制器
 *
 * @author wangc
 * @since 2023-12-06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sp/objsnapshot")
@CrossOrigin
@Api(value = "模型 色值 和 剖切快照", tags = "模型 色值 和 剖切快照接口")
public class ObjSnapshotController extends BladeController {

	@Autowired
	private IObjSnapshotService objSnapshotService;
	@Autowired
	private FileService fileService;
	@Autowired
	private CommentService 	commentService;

	/**
	 * 列表数据
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "列表数据", notes = "传入objSnapshot")
	public R<List<ObjSnapshotDTO>> detail(@RequestParam("commitId") String commitId) {
		boolean b = SignAuth.preHandle();
		if (!b) {
			return R.fail(SpConstant.STR_MSG);
		}
		List<ObjSnapshotDTO> objSnapshotDTOS = objSnapshotService.selectObjSnapshotList(commitId);
		return R.data(objSnapshotDTOS);
	}

	/**
	 * 删除 模型 色值 和 剖切快照
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		boolean b = SignAuth.preHandle();
		if (!b) {
			return R.fail(SpConstant.STR_MSG);
		}
		return R.status(objSnapshotService.deleteLogic(Func.toLongList(ids)));
	}


//	/**
//	 * 新增 模型 色值 和 剖切快照
//	 */
//	@PostMapping("/save")
//	@ApiOperationSupport(order = 4)
//	@ApiOperation(value = "新增", notes = "传入objSnapshot")
//	public R save(@RequestBody ObjSnapshot objSnapshot) {
//		boolean b = SignAuth.preHandle();
//		if (!b){
//			return R.fail(SpConstant.STR_MSG);
//		}
//		objSnapshot.setCreateTime(new Date());
//		return R.status(objSnapshotService.save(objSnapshot));
//	}

	/**
	 * 新增 模型 色值 和 剖切快照
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入objSnapshot")
	public R save(@RequestParam MultipartFile file, @RequestParam String snapshotName, @RequestParam String modelData, @RequestParam String colorData,@RequestParam String cameraData,@RequestParam String remark, @RequestParam String streamId, @RequestParam String commitId, @RequestParam String userId) {
		try {
			Map<String, String> map = new HashMap<>();
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			map.put("snapshotName", snapshotName);
			map.put("modelData", modelData);
			map.put("colorData", colorData);
			map.put("cameraData", cameraData);
			map.put("remark", remark);
			map.put("commitId", commitId);
			map.put("streamId", streamId);
			map.put("userId", userId);
			map.put("size", file.getSize() + "");
			ObjSnapshot savedFile = fileService.saveObjSnapshotFile(fileName, file, map);
			return R.success("File uploaded successfully: " + savedFile.getSnapshotName());
		} catch (Exception e) {
			e.printStackTrace();
			return R.fail("File uploaded fail" + e.getMessage());
		}
	}

// -------------------------

	/**
	 * 分页 模型 色值 和 剖切快照
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入objSnapshot")
	public R<IPage<ObjSnapshot>> list(ObjSnapshot objSnapshot, Query query) {
		IPage<ObjSnapshot> pages = objSnapshotService.page(Condition.getPage(query), Condition.getQueryWrapper(objSnapshot));
		return R.data(pages);
	}

	/**
	 * 自定义分页 模型 色值 和 剖切快照
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入objSnapshot")
	public R<IPage<ObjSnapshotVO>> page(ObjSnapshotVO objSnapshot, Query query) {
		IPage<ObjSnapshotVO> pages = objSnapshotService.selectObjSnapshotPage(Condition.getPage(query), objSnapshot);
		return R.data(pages);
	}

	/**
	 * 修改 模型 色值 和 剖切快照
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入objSnapshot")
	public R update(@Valid @RequestBody ObjSnapshot objSnapshot) {
		return R.status(objSnapshotService.updateById(objSnapshot));
	}

	/**
	 * 新增或修改 模型 色值 和 剖切快照
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入objSnapshot")
	public R submit(@Valid @RequestBody ObjSnapshot objSnapshot) {
		return R.status(objSnapshotService.saveOrUpdate(objSnapshot));
	}


//	---------------评论上传接口-------------------

	@SneakyThrows
	@PostMapping("/uploadComment.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "uploadComment", notes = "uploadComment")
	public R<Comment> uploadComment(@RequestParam MultipartFile file,@RequestParam String id) {
		try {
			Map<String, String> map = new HashMap<>();
			map.put("id", id);
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			Comment comment = fileService.saveCommentFile(fileName, file,map);
			return R.data(comment);
		} catch (Exception e) {
			e.printStackTrace();
			return R.fail("File uploaded fail" + e.getMessage());
		}
	}

	@SneakyThrows
	@GetMapping("/getComment.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "getComment", notes = "getComment")
	public R<List<Comment>> getComment(@RequestParam String[] id) {
		try {
			return R.data(commentService.getCommentsByIds(id));
		} catch (Exception e) {
			e.printStackTrace();
			return R.fail("File uploaded fail" + e.getMessage());
		}
	}

}
