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

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.gson.JsonArray;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.AesUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.sp.dto.ObjectDTO;
import org.springblade.modules.sp.dto.WbsInfoDTO;
import org.springblade.modules.sp.entity.WbsInfo;
import org.springblade.modules.sp.excel.WbsImportListener;
import org.springblade.modules.sp.excel.WbsInfoExcel;
import org.springblade.modules.sp.service.IObjectsService;
import org.springblade.modules.sp.service.IWbsInfoService;
import org.springblade.modules.sp.vo.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
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
@RequestMapping("/sp/wbs-object")
@CrossOrigin
@Api(value = "Wbs任务表", tags = "Wbs任务表接口")
public class WbsInfoController extends BladeController {

	private IWbsInfoService wbsInfoService;


	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入wbsInfo")
	public R<WbsInfo> detail(WbsInfo wbsInfo) {
		WbsInfo detail = wbsInfoService.getOne(Condition.getQueryWrapper(wbsInfo));
		return R.data(detail);
	}

	/**
	 * 分页 Wbs任务表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入wbsInfo")
	public R<IPage<WbsInfo>> list(WbsInfo wbsInfo, Query query) {
		IPage<WbsInfo> pages = wbsInfoService.page(Condition.getPage(query), Condition.getQueryWrapper(wbsInfo));
		return R.data(pages);
	}

	/**
	 * 自定义分页 Wbs任务表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入wbsInfo")
	public R<IPage<WbsInfoVO>> page(WbsInfoVO wbsInfo, Query query) {
		IPage<WbsInfoVO> pages = wbsInfoService.selectWbsInfoPage(Condition.getPage(query), wbsInfo);
		return R.data(pages);
	}

	/**
	 * 新增 Wbs任务表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入wbsInfo")
	public R save(@Valid @RequestBody WbsInfo wbsInfo) {
		return R.status(wbsInfoService.save(wbsInfo));
	}

	/**
	 * 修改 Wbs任务表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入wbsInfo")
	public R update(@Valid @RequestBody WbsInfo wbsInfo) {
		return R.status(wbsInfoService.updateById(wbsInfo));
	}

	/**
	 * 新增或修改 Wbs任务表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入wbsInfo")
	public R submit(@Valid @RequestBody WbsInfo wbsInfo) {
		return R.status(wbsInfoService.saveOrUpdate(wbsInfo));
	}


	/**
	 * 删除 Wbs任务表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(wbsInfoService.deleteLogic(Func.toLongList(ids)));
	}


	/**
	 * 导入数据
	 */
	@PostMapping("/import-excel")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "导入", notes = "传入excel")
	public R importUser(MultipartFile file) {
		String filename = file.getOriginalFilename();
		if (StringUtils.isEmpty(filename)) {
			throw new RuntimeException("请上传文件!");
		}
		if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
			throw new RuntimeException("请上传正确的excel文件!");
		}
		InputStream inputStream;
		try {
			WbsImportListener importListener = new WbsImportListener(wbsInfoService);
			inputStream = new BufferedInputStream(file.getInputStream());
			ExcelReaderBuilder builder = EasyExcel.read(inputStream, WbsInfoExcel.class, importListener);
			builder.doReadAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return R.success("操作成功");
	}


	/**
	 * 插入 模型-构件
	 * wangc 2023年05月10日10:17:18
	 */
	@PostMapping("/addWbsObject")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入wbsInfoDTOs")
	public R<List<WbsInfoGanttVO>> addWbsObject(@RequestBody WbsInfoDTO wbsInfoDTO) {
		List<WbsInfoGanttVO> wbsInfoGanttVOS = wbsInfoService.addWbsObject(wbsInfoDTO);
		return R.data(wbsInfoGanttVOS);
	}


	/**
	 * 删除 构件
	 * wangc 2023年05月19日09:59:57
	 */
	@GetMapping("/delWbsObject")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "删除", notes = "删除构件")
	public R delWbsObject(String id) {
		return R.status(wbsInfoService.removeById(Long.parseLong(id)));
	}




	/**
	 * 获取树形结构
	 * wangc 2023年05月05日11:11:12
	 * @return
	 */
	@GetMapping("/tree")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<WbsInfoTreeVO>> tree(String streamsId) {
		List<WbsInfoTreeVO> tree = wbsInfoService.tree(streamsId);
		return R.data(tree);
	}

	/**
	 * 获取甘特图结构
	 * wangc 2023年05月05日11:11:08
	 * @return
	 */
	@GetMapping("/ganttList")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "甘特图结构", notes = "甘特图结构")
	public R<List<WbsInfoGanttVO>> ganttList (String streamsId) {
		List<WbsInfoGanttVO> list = wbsInfoService.selectGanttList(streamsId);
		return R.data(list);
	}

	/**
	 * 获取Ue数据结构
	 * wangc 2023年06月09日14:19:06
	 * @return
	 */
	@GetMapping("/ueList")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "Ue数据结构", notes = "Ue数据结构")
	public R<List<WbsInfoUEVO>> ueList (String streamsId) {
		List<WbsInfoUEVO> list = wbsInfoService.selectUeList(streamsId);
		return R.data(list);
	}


	/**
	 * Ue详情数据模拟结构
	 * wangc 2023年06月09日14:31:57
	 * @return
	 */
	@GetMapping("/ueDetailTest")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "Ue详情数据结构", notes = "Ue详情数据结构")
	public R<WbsInfoUeDetailVO> ueDetailTest (String objId) {
		//WbsInfoUeDetailVO detailVO = wbsInfoService.selectUeDetail(objId);
		WbsInfoUeDetailVO vo = new WbsInfoUeDetailVO();
		vo.setObjStreamId("53dfae35c5");
		vo.setObjType("ZJGQA01-NJX-JC-LXQ-SW1001-SL001");
		vo.setObjUnits("mm");
		vo.setObjFamily("基本墙");
		vo.setObjCategory("墙");
		vo.setObjElementId("257383");
		vo.setObjId("04ef35fcb23db172139d5d47043c89af");
		return R.data(vo);
	}

	/**
	 * Ue详情数据结构
	 * wangc 2023年06月09日14:31:57
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


