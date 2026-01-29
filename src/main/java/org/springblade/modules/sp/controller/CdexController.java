package org.springblade.modules.sp.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.CdexElement;
import org.springblade.modules.sp.excel.EasyExcelReadUtils;
import org.springblade.modules.sp.service.CdexElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/cdex")
@CrossOrigin
@Api(value = "cdex接口", tags = "cdex接口")
public class CdexController extends BladeController {

    @Autowired
    private CdexElementService cdexElementService;

    //    ---------------CDex上传Excel-----------------

    @SneakyThrows
    @PostMapping("/uploadWbsCDex.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadWbsCDex", notes = "uploadWbsCDex")
    public R<String> uploadWbsCDex(@RequestParam("file") MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            // 解析 Excel 数据为 key-value 格式
            List<JSONObject> parsedData = EasyExcelReadUtils.parseExcelToKeyValue(inputStream);

            // 将数据转换为 JSON 格式
            String jsonData = new ObjectMapper().writeValueAsString(parsedData);

            // 存储 JSON 数据到数据库
            CdexElement cdexElement = new CdexElement();
            cdexElement.setId(UUID.randomUUID().toString());
            cdexElement.setName(file.getOriginalFilename());
            cdexElement.setData(jsonData);
            cdexElement.setModifyTime(new Date());
            boolean isSaved = cdexElementService.addCdexElement(cdexElement);

            if (isSaved) {
                return R.data("上传成功");
            } else {
                return R.fail("上传失败");
            }
        } catch (Exception e) {
            return R.fail("上传失败: " + e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/uploadData.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadData", notes = "uploadData")
    public R<String> uploadData(@RequestParam("name") String name,@RequestParam("jsonData") String jsonData) throws IOException {
        try {
            CdexElement cdexElement = new CdexElement();
            cdexElement.setId(UUID.randomUUID().toString());
            cdexElement.setName(name);
            cdexElement.setData(jsonData);
            cdexElement.setModifyTime(new Date());
            boolean isSaved = cdexElementService.addCdexElement(cdexElement);
            if (isSaved) {
                return R.data("上传成功");
            } else {
                return R.fail("上传失败");
            }
        }catch (Exception e){
            return R.fail("上传失败！ "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getWbsCDexData.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getWbsCDexData", notes = "getWbsCDexData")
    public R<String> getWbsCDexData(@RequestParam("name") String name) {
        try {
            CdexElement byName = cdexElementService.getByName(name);
            return R.data(byName.getData());
        }catch (Exception e){
            return R.fail("获取失败！ "+e.getMessage());
        }
    }
}
