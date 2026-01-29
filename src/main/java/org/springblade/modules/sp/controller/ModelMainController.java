package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.oss.MinioTemplate;
import org.springblade.core.oss.model.BladeFile;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.modules.sp.config.MinioProperties;
import org.springblade.modules.sp.entity.*;
import org.springblade.modules.sp.mapper.StreamFilesMapper;
import org.springblade.modules.sp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * 模型主数据控制器
 * @author dengtx
 * @since 2024年5月14日16:53:53
 */
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/modelMain")
@Api(value = "模型主数据控制器", tags = "模型主数据控制器")
@Slf4j
public class ModelMainController extends BladeController {

    @Autowired
    private FileService fileService;

    @Autowired
    private StreamFilesService streamFilesService;

    @Autowired
    private StreamService streamService;
    @Autowired
    private IUsersService usersService;
    @Autowired
    private SubscriptionOrderService subscriptionOrderService;
    @Autowired
    private SubscriptionConfigService subscriptionConfigService;
    @Autowired
    private SubscriptionOrderStreamsService subscriptionOrderStreamsService;

    @SneakyThrows
    @PostMapping("/uploadFile.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "uploadFile", notes = "uploadFile")
    public R uploadFile(@RequestParam MultipartFile file,@RequestParam String streamId,@RequestHeader("Authorization") String token){
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }

            // 获取用户订阅信息
            SubscriptionOrder order = subscriptionOrderService.getByUserId(user.getId());
            if (order == null) {
                return R.fail("用户未订阅");
            }

            // 获取套餐配置
            SubscriptionConfig config = subscriptionConfigService.getById(order.getConfigId());
            Integer totalStorage = config.getStorage();
            String unit = config.getStorageUnit();

            // 计算总容量（转换为KB）
            long totalStorageKB;
            switch (unit.toUpperCase()) {
                case "G":
                    totalStorageKB = totalStorage * 1024 * 1024L;
                    break;
                case "M":
                    totalStorageKB = totalStorage * 1024L;
                    break;
                case "T":
                    totalStorageKB = totalStorage *1024 * 1024 * 1024L;
                    break;
                default:
                    return R.fail("存储单位配置错误");
            }

            // 计算可用容量（KB）
            long usedStorageKB = order.getUsedStorage() != null ? order.getUsedStorage() : 0;
            long availableStorageKB = totalStorageKB - usedStorageKB;

            // 获取文件大小（KB）
            long fileSize = file.getSize() / 1024; // 转换为KB

            // 校验存储空间
            if (fileSize > availableStorageKB) {
                return R.fail("存储空间不足，当前可用：" + availableStorageKB + "KB，文件大小：" + fileSize + "KB");
            }
//            保存文件
            Map<String,String> map = new HashMap<>();
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            map.put("streamId",streamId);
            map.put("modifyUser",user.getName());
            map.put("size",file.getSize()+"");
            StreamFiles savedFile = fileService.saveFile(fileName,file,map);
            if (savedFile != null) {
                // 更新已使用存储空间
                order.setUsedStorage(usedStorageKB + fileSize);
                subscriptionOrderService.updateSubscriptionOrder(order);
//                更新对应项目的容量信息
                SubscriptionOrderStreams orderStreams = subscriptionOrderStreamsService.getByStreamIdAndOrderId(streamId, order.getId());
                if (orderStreams != null) {
                    orderStreams.setUsedStorage(orderStreams.getUsedStorage() + fileSize);
                    subscriptionOrderStreamsService.updateSubscriptionOrderStreams(orderStreams);
                }
            }
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
    public R uploadFiles(@RequestParam List<MultipartFile> files,@RequestParam String streamId,@RequestHeader("Authorization") String token){
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }

            // 获取用户订阅信息
            SubscriptionOrder order = subscriptionOrderService.getByUserId(user.getId());
            if (order == null) {
                return R.fail("用户未订阅");
            }

            // 获取套餐配置
            SubscriptionConfig config = subscriptionConfigService.getById(order.getConfigId());
            Integer totalStorage = config.getStorage();
            String unit = config.getStorageUnit();

            // 计算总容量（转换为KB）
            long totalStorageKB;
            switch (unit.toUpperCase()) {
                case "G":
                    totalStorageKB = totalStorage * 1024 * 1024L;
                    break;
                case "M":
                    totalStorageKB = totalStorage * 1024L;
                    break;
                case "T":
                    totalStorageKB = totalStorage *1024 * 1024 * 1024L;
                    break;
                default:
                    return R.fail("存储单位配置错误");
            }

            // 计算可用容量（KB）
            long usedStorageKB = order.getUsedStorage() != null ? order.getUsedStorage() : 0;
            long availableStorageKB = totalStorageKB - usedStorageKB;

            // 获取文件大小（KB）
            long fileSize = 0;
            for (MultipartFile file : files) {
                fileSize += file.getSize() / 1024; // 转换为KB
            }
            // 校验存储空间
            if (fileSize > availableStorageKB) {
                return R.fail("存储空间不足，当前可用：" + availableStorageKB + "KB，文件大小：" + fileSize + "KB");
            }

            List<String> fileUrls = new ArrayList<>();
            for (MultipartFile file : files) {
                //            保存文件
                Map<String,String> map = new HashMap<>();
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                map.put("streamId",streamId);
                map.put("modifyUser",user.getName());
                map.put("size",file.getSize()+"");
                StreamFiles savedFile = fileService.saveFile(fileName,file,map);
                if (savedFile == null) {
                    return R.fail("File uploaded fail");
                }
                fileUrls.add(savedFile.getFileUrl());
            }
            // 更新已使用存储空间
            order.setUsedStorage(usedStorageKB + fileSize);
            subscriptionOrderService.updateSubscriptionOrder(order);
//                更新对应项目的容量信息
            SubscriptionOrderStreams orderStreams = subscriptionOrderStreamsService.getByStreamIdAndOrderId(streamId, order.getId());
            if (orderStreams != null) {
                orderStreams.setUsedStorage(orderStreams.getUsedStorage() + fileSize);
                subscriptionOrderStreamsService.updateSubscriptionOrderStreams(orderStreams);
            }
            return R.data(fileUrls);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("File uploaded fail"+e.getMessage());
        }
    }

    /**
     * 服务器文件拿取
     */

    @SneakyThrows
    @GetMapping("/getStreamFileByStreamId.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getStreamFileByStreamId", notes = "getStreamFileByStreamId")
    public R<StreamFiles> getStreamFileByStreamId(@RequestParam String streamId,@RequestParam String name){
        try {
            StreamFiles file = streamFilesService.getStreamFileByStreamId(streamId, name);
            return R.data(file);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("获取文件失败:"+e.getMessage());
        }
    }




    /**
     * 服务器文件删除
     * @return
     */
    @SneakyThrows
    @PostMapping("/deleteStreamFiles.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteStreamFiles", notes = "deleteStreamFiles")
    public R<Boolean> deleteStreamFiles(@RequestParam String id){
        StreamFiles file = streamFilesService.getById(id);
        String fileurl = fileService.extractPath(file.getFileUrl());
        //删服务器
        boolean status = fileService.removeFile(fileurl,"stream-files-uploads");
        //删数据库
        streamFilesService.deleteStreamFiles(id);
        return R.status(status);
    }

//    @SneakyThrows
//    @GetMapping("/getFilePath.do")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "getFilePath", notes = "getFilePath")
//    public String getFilePath(@RequestParam String fileName){
//        return fileService.getUrl(fileName);
//    }

    @SneakyThrows
    @GetMapping("/getFilePath.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getFilePath", notes = "getFilePath")
    public R<String> getFilePath(@RequestParam String id){
        try {
            StreamFiles file = streamFilesService.getById(id);
            return R.data(file.getFileUrl());
        }catch (Exception e){
           return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getFileById.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getFileById", notes = "getFileById")
    public R<StreamFiles> getFileById(@RequestParam String id){
        try {
            StreamFiles file = streamFilesService.getById(id);
            return R.data(file);
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }


    @SneakyThrows
    @GetMapping("/getAllStreamFiles.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllStreamFiles", notes = "getAllStreamFiles")
    public R<List<StreamFiles>> getAllStreamFiles(@RequestParam String streamId){
            List<StreamFiles> allStreamFiles = streamFilesService.getStreamFiles(streamId);
            return R.data(allStreamFiles);
    }

    @SneakyThrows
    @PostMapping("/rename.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "rename", notes = "rename")
    public R<Boolean> rename(@RequestParam String id,@RequestParam String name){
        boolean res = streamFilesService.rename(id, name);
        return R.status(res);
    }


    @SneakyThrows
    @GetMapping("/getStreamFiles.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getStreamFiles", notes = "getStreamFiles")
    public R<List<StreamFiles>> getStreamFiles(@RequestParam String streamId,@RequestParam String type){
        List<StreamFiles> allStreamFiles = streamFilesService.getStreamFiles(streamId,type);
        return R.data(allStreamFiles);
    }

    @SneakyThrows
    @PostMapping("/linkObjects.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "linkObjects", notes = "linkObjects")
    public R linkObjects(@RequestParam String id,@RequestParam String[] objectId){
        try {
            String msg = "开始文件绑定模型任务！";
            StreamFiles file = streamFilesService.getById(id);
            if (ObjectUtil.isEmpty(file)){
                return R.fail("文件不存在！");
            }
            boolean res = streamFilesService.linkObjects(id, objectId);
            if (res){
                msg = "文件："+file.getName()+"绑定模型成功！！模型ID："+objectId;
                return R.success(msg);
            }else {
                msg = "文件："+file.getName()+"绑定模型失败！！模型ID："+objectId;
                return R.fail(msg);
            }
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }

    /**
     * Stream工作流删除
     * @param streamId
     * @param name
     * @return
     */
    @SneakyThrows
    @PostMapping("/deleteStream.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteStream", notes = "deleteStream")
    public R<Boolean> deleteStream(@RequestParam String streamId){
        try {
            //找到当前工作流下的全部文件
            List<StreamFiles> files = streamFilesService.getStreamFiles(streamId);
            boolean status = false;
            for (StreamFiles file : files) {
                String fileurl = fileService.extractPath(file.getFileUrl());
                //删服务器
                status = fileService.removeFile(fileurl,"stream-files-uploads");
                //删数据库
                streamFilesService.deleteStreamFiles(file.getId());
            }
            return R.status(status);
        }catch (Exception e){
            return R.fail(streamId+"工作流绑定文件删除失败!! "+e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getAllStreams.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getAllStreams", notes = "getAllStreams")
    public R<List<Stream>> getAllStreams(){
        try {
            List<Stream> allStreams = streamService.getAllStreams();
            return R.data(allStreams);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    @SneakyThrows
//    @PostMapping("/deleteStreamFiles.do")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "deleteStreamFiles", notes = "deleteStreamFiles")
//    public R<Boolean> deleteStreamFiles(@RequestParam String id){
//        boolean status = streamFilesService.deleteStreamFiles(id);
//        return R.status(status);
//    }


//    @SneakyThrows
//    @PostMapping("/uploadFiles.do")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "uploadStreamFile", notes = "uploadStreamFile")
//    public R uploadStreamFile(@RequestParam MultipartFile file,@RequestParam String streamsId,@RequestParam String modifyUser){
//        try {
//            Map<String,String> map = new HashMap<>();
//            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//            map.put("streamsId",streamsId);
//            map.put("modifyUser",modifyUser);
//            //检查桶是否存在
//            boolean bucketisExist = false;
//            bucketisExist = minioTemplate.bucketExists(minioProperties.getBucketName());
//            if (!bucketisExist) {
//                // 创建桶
//                minioTemplate.makeBucket(minioProperties.getBucketName());
//            }
//            log.info("Fileupload starter, 开始文件上传！");
//            BladeFile bladeFile = minioTemplate.putFile(minioProperties.getBucketName(), file.getOriginalFilename(), file);
////             创建 StreamFiles 对象并保存到数据库
//            StreamFiles fileRecord = new StreamFiles();
//            fileRecord.setId(UUID.randomUUID().toString());
//            fileRecord.setObjectId("null"); // 设置其他需要的信息
//            fileRecord.setStreamId(map.get("streamsId"));
//            fileRecord.setName(bladeFile.getOriginalName());
//            fileRecord.setType(file.getContentType());
//            fileRecord.setFileUrl(bladeFile.getLink());
//            fileRecord.setModifyUser(map.get("modifyUser")); // Username needs to come from the logged-in user context or input
//            fileRecord.setModifyTime(new Date()); // Setting the current date as the modification time
//            fileRecord.setStatus(0); // Assuming 1 means active status
//            streamFilesMapper.addStreamFiles(fileRecord);
//            return R.success("File uploaded successfully: " + fileRecord.getName());
//        }catch (Exception e){
//            e.printStackTrace();
//            return R.fail("File uploaded fail"+e.getMessage());
//        }
//    }

}
