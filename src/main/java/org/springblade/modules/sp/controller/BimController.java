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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.oss.MinioTemplate;
import org.springblade.core.oss.model.BladeFile;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.sp.constant.SpConstant;
import org.springblade.modules.sp.entity.FileCommon;
import org.springblade.modules.sp.service.ExpInstancesService;
import org.springblade.modules.sp.service.IBimService;
import org.springblade.modules.sp.service.IFileCommonService;
import org.springblade.modules.sp.service.IObjectsService;
import org.springblade.modules.sp.utils.SignAuth;
import org.springblade.modules.sp.vo.BimListTreeVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Wbs任务表 控制器
 *
 * @author wangc
 * @since 2023-04-23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sp/bim")
@CrossOrigin
@Api(value = "bim接口", tags = "bim接口")
public class BimController extends BladeController {

	private IBimService bimService;
	private MinioTemplate minioTemplate;
	private IFileCommonService fileCommonService;
	private IObjectsService objectsService;
	private ExpInstancesService expInstancesService;

	/**
	 * bim结构树
	 * wangc 2023年10月25日10:00:35 2024-03-25 14:42:11
	 * @return
	 */
	@GetMapping("/bimCategoryTreeList-new3")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "bim结构树", notes = "bim结构树")
	public R<List<BimListTreeVO>> bimCategoryTreeListNew3 (String resourceId, String streamId) {
//		boolean b = SignAuth.preHandle();
//		if (!b){
//			return R.fail(SpConstant.STR_MSG);
//		}
		List<BimListTreeVO> bimListTreeVOS = bimService.BimListTreeNew3(resourceId, streamId);
		return R.data(bimListTreeVOS);
	}

	/**
	 * bim结构树（分表expinstances版）
	 * @author dengtx
	 * @time 2024年6月12日16:15:25
	 * @param resourceId
	 * @param streamId
	 * @return
	 */
	@GetMapping("/bimCategoryTreeList-new000")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "bim结构树", notes = "bim结构树")
	public R<List<BimListTreeVO>> bimCategoryTreeListNew000 (String resourceId, String streamId) {
//		boolean b = SignAuth.preHandle();
//		if (!b){
//			return R.fail(SpConstant.STR_MSG);
//		}

		List<BimListTreeVO> bimListTreeVOS = bimService.BimListTreeNew3(resourceId, streamId);
		return R.data(bimListTreeVOS);
	}

	/**
	 * 上传文件 objects
	 * wangc 2023年07月11日13:41:58
	 * @param file 文件
	 * @return ObjectStat
	 */
	@SneakyThrows
	@PostMapping("/put-file/objects")
	public R<List<FileCommon>> putFileObjects(
			@RequestParam (value = "file") MultipartFile file,
			@RequestParam (value = "streamsId") String streamsId, // 流id
			@RequestParam (value = "commitsId") String commitsId, // 节点id
			@RequestParam (value = "objectsId") String objectsId, // 构件id
			@RequestParam (value = "elementId",required = false) String elementId, // 非必填
			@RequestParam (value = "userId",required = false) String userId, // 用户id
			@RequestParam (value = "fileCoord",required = false) String fileCoord, // 坐标
			@RequestParam (value = "remark",required = false) String remark // 备注
			) {
		BladeFile bladeFile = minioTemplate.putFile(file.getOriginalFilename(), file.getInputStream());
		List<FileCommon> fileCommons = new ArrayList<>();
		String[] strings = Func.toStrArray(objectsId);
		if (strings.length>0){
			for (String s : strings) {
				FileCommon fileCommon = new FileCommon();
				fileCommon.setStreamsId(streamsId);
				fileCommon.setCommitsId(commitsId);
				fileCommon.setObjectsId(s);
				fileCommon.setElementId(elementId);
				fileCommon.setResourceType(SpConstant.OBJTYPE);
				fileCommon.setFileSize(file.getSize());
				fileCommon.setFileType(file.getContentType());
				fileCommon.setFileLink(bladeFile.getLink());
				fileCommon.setFileDomain(bladeFile.getDomain());
				fileCommon.setFileName(bladeFile.getName());
				fileCommon.setFileOriginalName(bladeFile.getOriginalName());
				fileCommon.setRemark(remark);
				fileCommon.setUserId(userId); // 用户id
				fileCommon.setFileCoord(fileCoord); // 文件坐标
				fileCommons.add(fileCommon);
			}
		}
		fileCommonService.saveBatch(fileCommons);
		return R.data(fileCommons);
	}

	/**
	 * 获取文件外链
	 * wangc 2023年07月11日13:48:15
	 * @param objectsId objid
	 * @return FileCommon
	 */
	@SneakyThrows
	@GetMapping("/file-link/objects")
	public R<List<FileCommon>> fileLinkObjects(@RequestParam String objectsId) {
		List<FileCommon> list = fileCommonService.list(
				new LambdaQueryWrapper<FileCommon>().eq(
						FileCommon::getObjectsId, objectsId
				)
		);
		return R.data(list);
	}

	/**
	 * 根据节点信息获取文件
	 * wangc 2023年07月11日13:48:15
	 * @param commitsId
	 * @return FileCommon
	 */
	@SneakyThrows
	@GetMapping("/file-link/objectsByCommitsId")
	public R<List<FileCommon>> fileLinkObjectsByCommitsId(@RequestParam String commitsId) {
		List<FileCommon> list = fileCommonService.list(
				new LambdaQueryWrapper<FileCommon>().eq(
						FileCommon::getCommitsId, commitsId
				)
		);
		return R.data(list);
	}


	/**
	 * 上传文件 streams
	 * wangc 2023年07月11日13:42:20
	 * @param file 文件
	 * @return ObjectStat
	 */
	@SneakyThrows
	@PostMapping("/put-file/streams")
	public R putFileStreams(
			@RequestParam MultipartFile file,
			@RequestParam String commitsId,
			@RequestParam String streamsId,
			@RequestParam String userId,
			@RequestParam String fileCoord
	) {
		BladeFile bladeFile = minioTemplate.putFile(file.getOriginalFilename(), file.getInputStream());
		FileCommon fileCommon = new FileCommon();
		fileCommon.setStreamsId(streamsId);
		fileCommon.setCommitsId(commitsId);
		fileCommon.setResourceType(SpConstant.STREAMSTYPE);
		fileCommon.setFileSize(file.getSize());
		fileCommon.setFileType(file.getContentType());
		fileCommon.setFileLink(bladeFile.getLink());
		fileCommon.setFileDomain(bladeFile.getDomain());
		fileCommon.setFileName(bladeFile.getName());
		fileCommon.setFileOriginalName(bladeFile.getOriginalName());
		fileCommon.setUserId(userId); // 用户id
		fileCommon.setFileCoord(fileCoord); // 文件坐标
		fileCommonService.save(fileCommon);
		return R.success("上传成功!");
	}

}


