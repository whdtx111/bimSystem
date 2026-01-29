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

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import lombok.SneakyThrows;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.sp.constant.SpConstant;
import org.springblade.modules.sp.entity.CrashDetection;
import org.springblade.modules.sp.entity.Selector;
import org.springblade.modules.sp.entity.Task;
import org.springblade.modules.sp.service.CrashDetectionService;
import org.springblade.modules.sp.service.FileService;
import org.springblade.modules.sp.service.SelectorService;
import org.springblade.modules.sp.service.TaskService;
import org.springblade.modules.sp.utils.SignAuth;
import org.springblade.modules.sp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.sp.entity.Objects;
import org.springblade.modules.sp.service.IObjectsService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 *  控制器
 *
 * @author wangc
 * @since 2023-08-14
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sp/objects")
@CrossOrigin
@Api(value = "", tags = "接口")
public class ObjectsController extends BladeController {

	private IObjectsService objectsService;

	@Autowired
	private TaskService taskService;
	@Autowired
	private SelectorService selectorService;
    @Autowired
    private FileService fileService;
	@Autowired
	private CrashDetectionService crashDetectionService;

	/***
	 * 获取 objparameterList
	 * @param resourceId
	 * @param category
	 * @return 20231205092932
	 * @throws JsonProcessingException
	 */
	@GetMapping("/objParameterList")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "parameterList", notes = "parameterList")
	public R<ObjParameterVO>  objParameterList(
			@RequestParam("resourceId") String resourceId,
			@RequestParam("category") String category) throws JsonProcessingException {
//		boolean b = SignAuth.preHandle();
//		if (!b){
//			return R.fail(SpConstant.STR_MSG);
//		}
		return objectsService.selectObjectParameters(resourceId,category);
	}


	@GetMapping("/selectObjectParametersAllByStr")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "selectObjectParametersAllByStr", notes = "selectObjectParametersAllByStr")
	public R<List<ObjCountVO>>  selectObjectParametersAllByStr(
			@RequestParam("resourceId") String resourceId,
			@RequestParam("str") String str
																		) throws JsonProcessingException {
//		boolean b = SignAuth.preHandle();
//		if (!b){
//			return R.fail(SpConstant.STR_MSG);
//		}
		return objectsService.selectObjectParametersAllByStr(resourceId,str);
	}

	@GetMapping("/getObjectParametersName")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "getObjectParametersName", notes = "getObjectParametersName")
	public R<List<String>>  getObjectParametersName(@RequestParam("resourceId") String resourceId) throws JsonProcessingException {
//		boolean b = SignAuth.preHandle();
//		if (!b){
//			return R.fail(SpConstant.STR_MSG);
//		}
		return objectsService.getParameterName(resourceId);
	}

	@SneakyThrows
	@PostMapping("/compare-face-count.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "compareFaceCount", notes = "compareFaceCount")
	public R<Map<String, Object>> compareFaceCount(@RequestBody ObjFacesVO objFacesVO) {
		try {
			return objectsService.compareFaceCount(objFacesVO.getStreamId(), objFacesVO.getObjIdsA(), objFacesVO.getObjIdsB());
		}catch (Exception e){
			e.printStackTrace();
			return R.fail(e.getMessage());
		}
	}

//	----------------Task(碰撞检测)--------------------

	@SneakyThrows
	@GetMapping("/task/getById.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "/task/getById", notes = "/task/getById")
	public R<Task> getById(@RequestParam String id){
		try {
			Task res = taskService.getById(id);
			return R.data(res);
		}catch (Exception e){
			return null;
		}
	}

	@SneakyThrows
	@GetMapping("/task/getAllTasks.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "/task/getAllTasks", notes = "/task/getAllTasks")
	public R<List<Task>> getAllTasks(){
		try {
			List<Task> res = taskService.getAllTasks();
			return R.data(res);
		}catch (Exception e){
			e.printStackTrace();
			return R.fail(e.getMessage());
		}
	}

	@SneakyThrows
	@GetMapping("/task/getTask.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "/task/getTask", notes = "/task/getTask")
	public R<List<Task>> getTask(@RequestParam String streamId, @RequestParam String commitId){
		try {
			List<Task> res = taskService.getTask(streamId, commitId);
			return R.data(res);
		}catch (Exception e){
		   e.printStackTrace();
		   return R.fail(e.getMessage());
		}
	}

	@SneakyThrows
	@PostMapping("/task/addTask.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "/task/addTask", notes = "/task/addTask")
	public R<String> addTask(@RequestParam MultipartFile file, @RequestParam String streamId, @RequestParam String commitId, @RequestParam String type,
							  @RequestParam String taskStatus,@RequestParam String detail,@RequestParam String modifyUser,@RequestParam String deadLine,@RequestParam String targetId,@RequestParam String collisionId,@RequestParam String others) {
		try {
			Map<String, String> map = new HashMap<>();
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			map.put("fileName", fileName);
			map.put("taskStatus", taskStatus);
			map.put("detail", detail);
			map.put("modifyUser", modifyUser);
			map.put("deadLine", deadLine);
			map.put("targetId", targetId);
			map.put("streamId", streamId);
			map.put("commitId", commitId);
			map.put("type", type);
			map.put("collisionId", collisionId);
			map.put("others", others);
			Task task = fileService.saveTaskFile(fileName, file, map);
			return R.data(task.getUrl());
		}catch (Exception e){
			e.printStackTrace();
			return R.fail(e.getMessage());
		}
	}

	@SneakyThrows
	@PostMapping("/task/updateTask.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "/task/updateTask", notes = "/task/updateTask")
	public R<Boolean> updateTask(@RequestBody Task task){
		try {
			boolean b = taskService.updateTask(task);
			return R.data(b);
		}catch (Exception e){
			e.printStackTrace();
			return R.fail(e.getMessage());
		}
	}

	@SneakyThrows
	@PostMapping("/task/deleteTaskById.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "/task/deleteTaskById", notes = "/task/deleteTaskById")
	public R<Boolean> deleteTaskById(@RequestParam String id){
		try {
			boolean b = taskService.deleteTaskById(id);
			return R.data(b);
		}catch (Exception e){
			e.printStackTrace();
			return R.fail(e.getMessage());
		}
	}

//	------------------------模型选择集-----------------------

	@SneakyThrows
	@GetMapping("/getSelectorById.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "getSelectorById", notes = "getSelectorById")
	public R<Selector> getSelectorById(@RequestParam String id){
		try {
			Selector res = selectorService.getById(id);
			return R.data(res);
		}catch (Exception e){
			e.printStackTrace();
			return R.fail(e.getMessage());
		}
	}

	@SneakyThrows
	@PostMapping("/searchSelectorFilter.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "searchSelectorFilter", notes = "searchSelectorFilter")
	public R<List<Selector>> searchSelectorFilter(@RequestBody SelectorVO selectorVO) {
		try {
			List<Selector> res = selectorService.searchFilter(selectorVO.getCommitIds(), selectorVO.getStreamId());
			return R.data(res);
		}catch (Exception e){
			e.printStackTrace();
			return R.fail(e.getMessage());
		}
	}

	@SneakyThrows
	@PostMapping("/addSelector.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "addSelector", notes = "addSelector")
	public R<String> addSelector(@RequestBody Selector selector) {
		try {
			selector.setName("选择集");
			selector.setColor("#3B82F6");
			boolean b = selectorService.addSelector(selector);
			if (b) {
				return R.data("新增选择集成功，ID：" + selector.getId());
			}else {
				return R.fail("新增选择集失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.fail(e.getMessage());
		}
	}

	@SneakyThrows
	@PostMapping("/updateSelector.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "updateSelector", notes = "updateSelector")
	public R<String> updateSelector(@RequestBody Selector selector) {
		try {
			selector.setModifyTime(new Date());
			selector.setStatus(0);
			boolean b = selectorService.updateSelector(selector);
			if (b) {
				return R.data("修改选择集成功，ID：" + selector.getId());
			}else {
				return R.fail("修改选择集失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.fail(e.getMessage());
		}
	}

	@SneakyThrows
	@PostMapping("/updateName.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "updateName", notes = "updateName")
	public R<Boolean> updateName(@RequestParam String id, @RequestParam String name) {
		try {
			boolean b = selectorService.updateName(id, name);
			return R.data(b);
		} catch (Exception e) {
			e.printStackTrace();
			return R.fail(e.getMessage());
		}
	}

	@SneakyThrows
	@PostMapping("/deleteSelectorById.do")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "deleteSelectorById", notes = "deleteSelectorById")
	public R<Boolean> deleteSelectorById(@RequestParam String id) {
		try {
			boolean b = selectorService.deleteSelectorById(id);
			return R.data(b);
		} catch (Exception e) {
			e.printStackTrace();
			return R.fail(e.getMessage());
		}
	}

	/**
	 * 碰撞检测文件上传接口
	 * @param file 上传的文件
	 * @param streamId 流ID
	 * @param branchId 分支ID
	 * @param commitId 提交ID
	 * @param modifyUser 修改用户
	 * @return
	 */
	@SneakyThrows
	@PostMapping("/uploadCrashDetectionFile.do")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "上传碰撞检测文件", notes = "上传碰撞检测文件到MinIO并保存记录")
	public R<Map<String, Object>> uploadCrashDetectionFile(
			@RequestParam("file") MultipartFile file,
			@RequestParam("streamId") String streamId,
			@RequestParam("branchId") String branchId,
			@RequestParam("commitId") String commitId,
			@RequestParam("threshold") String threshold,
			@RequestParam(value = "modifyUser", required = false) String modifyUser) {
		try {
			// 验证文件
			if (file == null || file.isEmpty()) {
				return R.fail("上传文件不能为空");
			}

			// 验证参数
			if (!StringUtils.hasLength(streamId) || !StringUtils.hasLength(branchId) || !StringUtils.hasLength(commitId)) {
				return R.fail("streamId、branchId、commitId不能为空");
			}

			// 检查是否存在相同的记录
			CrashDetection existingRecord = crashDetectionService.getByStreamBranchCommit(streamId, branchId, commitId);
			
			// 使用FileService上传文件到MinIO（会覆盖同名文件）
			String minIoUrl = fileService.saveCrashDetectionFile(file, streamId, branchId, commitId);
			
			if (minIoUrl == null || minIoUrl.isEmpty()) {
				return R.fail("文件上传失败");
			}
			
			CrashDetection crashDetection;
			boolean isUpdate = false;
			
			if (existingRecord != null) {
				// 更新现有记录
				crashDetection = existingRecord;
				crashDetection.setMinIoUrl(minIoUrl);
				crashDetection.setStatus(1); // 重新设置为待处理
				crashDetection.setThreshold(threshold);
				crashDetection.setData(null); // 清空旧数据
				crashDetection.setModifyUser(StringUtils.hasLength(modifyUser) ? modifyUser : "system");
				crashDetection.setModifyTime(new Date());
				
				boolean updated = crashDetectionService.updateCrashDetection(crashDetection);
				if (!updated) {
					return R.fail("更新记录失败");
				}
				isUpdate = true;
			} else {
				// 创建新记录
				crashDetection = new CrashDetection();
				crashDetection.setStreamId(streamId);
				crashDetection.setBranchId(branchId);
				crashDetection.setCommitId(commitId);
				crashDetection.setMinIoUrl(minIoUrl);
				crashDetection.setStatus(1); // 待处理
				crashDetection.setData(null);
				crashDetection.setThreshold(threshold);
				crashDetection.setModifyUser(StringUtils.hasLength(modifyUser) ? modifyUser : "system");

				boolean saved = crashDetectionService.addCrashDetection(crashDetection);
				if (!saved) {
					return R.fail("保存记录失败");
				}
			}

			// 返回结果
			Map<String, Object> result = new HashMap<>();
			result.put("id", crashDetection.getId());
			result.put("minIoUrl", minIoUrl);
			result.put("status", crashDetection.getStatus());
			result.put("message", isUpdate ? "文件上传成功，已覆盖原记录" : "文件上传成功，等待处理");
			result.put("isUpdate", isUpdate);

			return R.data(result);
		} catch (Exception e) {
			e.printStackTrace();
			return R.fail("上传失败: " + e.getMessage());
		}
	}

	/**
	 * 查询碰撞检测结果
	 * @param streamId 流ID
	 * @param branchId 分支ID
	 * @param commitId 提交ID
	 * @return
	 */
	@SneakyThrows
	@GetMapping("/getCrashDetectionResult.do")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "查询碰撞检测结果", notes = "根据streamId/branchId/commitId查询碰撞检测结果")
	public R<Map<String, Object>> getCrashDetectionResult(
			@RequestParam("streamId") String streamId,
			@RequestParam("branchId") String branchId,
			@RequestParam("commitId") String commitId) {
		try {
			// 查询碰撞检测记录
			CrashDetection crashDetection = crashDetectionService.getByStreamBranchCommit(streamId, branchId, commitId);

			Map<String, Object> result = new HashMap<>();

			if (crashDetection == null) {
				// 未查询到记录
				result.put("status", -1);
				result.put("message", "数据不存在");
				return R.data(result);
			}

			Integer status = crashDetection.getStatus();
			
			if (status == 1) {
				// 待处理
				result.put("status", status);
				result.put("message", "数据待处理");
				result.put("id", crashDetection.getId());
				result.put("minIoUrl", crashDetection.getMinIoUrl());
				result.put("threshold", crashDetection.getThreshold());
				result.put("modifyTime", crashDetection.getModifyTime());
				return R.data(result);
			} else if (status == 2){
				// 其他状态
				result.put("status", status);
				result.put("message", "数据处理中，请耐心等待!");
				result.put("id", crashDetection.getId());
				result.put("minIoUrl", crashDetection.getMinIoUrl());
				result.put("threshold", crashDetection.getThreshold());
				result.put("modifyTime", crashDetection.getModifyTime());
				return R.data(result);
			}else if (status == 3) {
				// 成功处理，返回data数据
				result.put("status", status);
				result.put("message", "处理成功");
				result.put("data", crashDetection.getData());
				result.put("id", crashDetection.getId());
				result.put("minIoUrl", crashDetection.getMinIoUrl());
				result.put("threshold", crashDetection.getThreshold());
				result.put("modifyTime", crashDetection.getModifyTime());
				return R.data(result);
			}else {
				result.put("status", status);
				result.put("message", "处理成功");
				result.put("data", crashDetection.getData());
				result.put("id", crashDetection.getId());
				result.put("minIoUrl", crashDetection.getMinIoUrl());
				result.put("threshold", crashDetection.getThreshold());
				result.put("modifyTime", crashDetection.getModifyTime());
				return R.data(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.fail("查询失败: " + e.getMessage());
		}
	}

}
