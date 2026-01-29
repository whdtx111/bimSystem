package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.StreamFiles;
import org.springblade.modules.sp.entity.XiaSanPic;
import org.springblade.modules.sp.entity.XiaSanShot;
import org.springblade.modules.sp.service.FileService;
import org.springblade.modules.sp.service.XiaSanPicService;
import org.springblade.modules.sp.service.XiaSanShotService;
import org.springblade.modules.sp.vo.XiasanshotCountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 夏三项目相关控制器
 * @author dengtx
 * @since 2024年9月20日14:04:59
 */
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/xiasan")
@Api(value = "夏三项目相关控制器", tags = "夏三项目相关控制器")
public class XiaSanController extends BladeController {

    @Autowired
    private FileService fileService;
    @Autowired
    private XiaSanPicService xiaSanPicService;
    @Autowired
    private XiaSanShotService xiaSanShotService;

    @SneakyThrows
    @PostMapping("/uploadFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadFile", notes = "uploadFile")
    public R<String> uploadFile(@RequestParam MultipartFile file,@RequestParam String uploadUser,@RequestParam String checkStatus,@RequestParam String streamId,@RequestParam String commitId,@RequestParam String objectId,@RequestParam String elementId, @RequestParam String modifyUser){
        try {
            Map<String,String> map = new HashMap<>();
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            map.put("streamId",streamId);
            map.put("commitId",commitId);
            map.put("objectId",objectId);
            map.put("elementId",elementId);
            map.put("checkStatus",checkStatus);
            map.put("uploadUser",uploadUser);
            map.put("modifyUser",modifyUser);
            map.put("size",file.getSize()+"");
            XiaSanPic savedFile = fileService.saveXiaSanPicFile(fileName,file,map);
            return R.success("File uploaded successfully: " + savedFile.getName());
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("File uploaded fail"+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/uploadFiles.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadFiles", notes = "uploadFiles")
    public R<String> uploadFiles(@RequestParam List<MultipartFile> files,@RequestParam String uploadUser,@RequestParam String checkStatus,@RequestParam String streamId,@RequestParam String commitId,@RequestParam String objectId,@RequestParam String elementId, @RequestParam String modifyUser){
        try {
            String fileNames = "";
            for (MultipartFile file : files) {
                Map<String,String> map = new HashMap<>();
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                map.put("streamId",streamId);
                map.put("commitId",commitId);
                map.put("objectId",objectId);
                map.put("elementId",elementId);
                map.put("checkStatus",checkStatus);
                map.put("uploadUser",uploadUser);
                map.put("modifyUser",modifyUser);
                map.put("size",file.getSize()+"");
                XiaSanPic savedFile = fileService.saveXiaSanPicFile(fileName,file,map);
                fileNames = fileNames+","+savedFile.getName();
            }
            return R.success("File uploaded successfully: " +fileNames);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("File uploaded fail"+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getXiaSanPicList.do")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "getXiaSanPicList", notes = "getXiaSanPicList")
    public R<List<XiaSanPic>> getXiaSanPicList(@RequestParam Optional<String> streamId,@RequestParam Optional<String> commitId,@RequestParam Optional<String> objectId,@RequestParam Optional<String> elementId,@RequestParam Optional<String> checkStatus){
        try {
            List<XiaSanPic> xiaSanPicList = new ArrayList<>();
            if (StringUtils.isEmpty(checkStatus)){
               return R.data(xiaSanPicList);
            }else {
                return R.data(xiaSanPicService.searchFilter(streamId.orElse(null),commitId.orElse(null),objectId.orElse(null),elementId.orElse(null),checkStatus.orElse(null)));
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("File uploaded fail"+e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteFile", notes = "deleteFile")
    public R<String> deleteFile(@RequestParam String id){
        try {
            XiaSanPic xiaSanPic = xiaSanPicService.getById(id);
            String fileurl = fileService.xiasanExtractPath(xiaSanPic.getUrl());
            //删服务器
            boolean a = fileService.removeFile(fileurl,"xiasan-files");
            //删数据库
            boolean b = xiaSanPicService.deleteXiaSanPicById(id);
            if (a&&b){
                return R.success("删除成功");
            }else {
                return R.fail("删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("File uploaded fail"+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getXiaSanShotById.do")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "getXiaSanShotById", notes = "getXiaSanShotById")
    public R<XiaSanShot> getXiaSanShotById(@RequestParam String id){
        try {
            return R.data(xiaSanShotService.getById(id));
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("File uploaded fail"+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/xiaSanShotSearchFilter.do")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "XiaSanShotSearchFilter", notes = "XiaSanShotSearchFilter")
    public R<List<XiaSanShot>> XiaSanShotSearchFilter(@RequestParam Optional<String> checkStatus,@RequestParam Optional<String> streamId, @RequestParam Optional<String> commitId,@RequestParam Optional<String> elementId,@RequestParam Optional<String> objectId){
        try {
            return R.data(xiaSanShotService.searchFilter(checkStatus.orElse(null),streamId.orElse(null),commitId.orElse(null),elementId.orElse(null),objectId.orElse(null)));
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("File uploaded fail"+e.getMessage());
        }
    }


    @SneakyThrows
    @PostMapping("/addXiaSanShot.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addXiaSanShot", notes = "addXiaSanShot")
    public R<String> addXiaSanShot(@RequestBody XiaSanShot xiaSanShot){
        try {
            String streamId = xiaSanShot.getStreamId();
            String elementId = xiaSanShot.getElementId();
            XiaSanShot byElementId = xiaSanShotService.getByElementId(streamId, elementId);
            if (byElementId != null){
                xiaSanShot.setId(byElementId.getId());
                xiaSanShot.setModifyTime(new Date());
                boolean b = xiaSanShotService.updateXiaSanShot(xiaSanShot);
                if (b){
                    return R.success("更新问题成功，问题ID: " + xiaSanShot.getId());
                }else {
                    return R.fail("更新问题失败");
                }
            }else {
                xiaSanShot.setId(UUID.randomUUID().toString());
                xiaSanShot.setModifyTime(new Date());
                boolean b = xiaSanShotService.addXiaSanShot(xiaSanShot);
                if (b){
                    return R.success("新增问题成功，问题ID: " + xiaSanShot.getId());
                }else {
                    return R.fail("新增问题失败");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("File uploaded fail"+e.getMessage());
        }
    }

//    @SneakyThrows
//    @PostMapping("/addXiaSanShot.do")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "addXiaSanShot", notes = "addXiaSanShot")
//    public R<String> addXiaSanShot(@RequestBody XiaSanShot xiaSanShot){
//        try {
//            xiaSanShot.setId(UUID.randomUUID().toString());
//            xiaSanShot.setModifyTime(new Date());
//            boolean b = xiaSanShotService.addXiaSanShot(xiaSanShot);
//            if (b){
//                return R.success("新增问题成功，问题ID: " + xiaSanShot.getId());
//            }else {
//                return R.fail("新增问题失败");
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            return R.fail("File uploaded fail"+e.getMessage());
//        }
//    }

    @SneakyThrows
    @PostMapping("/updateXiaSanShot.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateXiaSanShot", notes = "updateXiaSanShot")
    public R<String> updateXiaSanShot(@RequestBody XiaSanShot xiaSanShot){
        try {
            xiaSanShot.setModifyTime(new Date());
            boolean b = xiaSanShotService.updateXiaSanShot(xiaSanShot);
            if (b){
                return R.success("更新问题成功，问题ID: " + xiaSanShot.getId());
            }else {
                return R.fail("更新问题失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("File uploaded fail"+e.getMessage());
        }
    }


    @SneakyThrows
    @PostMapping("/deleteXiaSanShotById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteXiaSanShotById", notes = "deleteXiaSanShotById")
    public R<String> deleteXiaSanShotById(@RequestParam String id){
        try {
            boolean b = xiaSanShotService.deleteXiaSanShotById(id);
            if (b){
                return R.success("删除问题成功，问题ID: " + id);
            }else {
                return R.fail("删除问题失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("File uploaded fail"+e.getMessage());
        }
    }

//    -----------获取不同状态的焊缝总数----------------

    @SneakyThrows
    @PostMapping("/getXiaSanShotCount.do")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "getXiaSanShotCount", notes = "getXiaSanShotCount")
    public R<List<XiasanshotCountVO>> getXiaSanShotCount(@RequestParam String streamId,@RequestParam String commitId){
        try {
            List<XiasanshotCountVO> xiasanshotCountVOS = new ArrayList<>();
            List<String> checkStatusList = xiaSanShotService.getcheckStatus(streamId,commitId);
            for (String checkStatus : checkStatusList) {
                XiasanshotCountVO xiasanshotCountVO = new XiasanshotCountVO();
                int count = xiaSanShotService.countByCommitId(commitId,streamId,checkStatus);
                xiasanshotCountVO.setCheckStatus(checkStatus);
                xiasanshotCountVO.setCount(count+"");
                xiasanshotCountVOS.add(xiasanshotCountVO);
            }
            return R.data(xiasanshotCountVOS);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("File uploaded fail"+e.getMessage());
        }
    }
}


