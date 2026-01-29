package org.springblade.modules.sp.service;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springblade.core.oss.MinioTemplate;
import org.springblade.core.oss.model.BladeFile;
import org.springblade.modules.sp.config.MinioProperties;
import org.springblade.modules.sp.entity.*;
import org.springblade.modules.sp.mapper.StreamFilesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@DS("postgresql")
@Slf4j
public class FileService {

//    private MinioTemplate minioTemplate;
    @Autowired
    private StreamFilesMapper streamFilesMapper;

//    @Value("${streamFiles.url}")
//    private String fileurl;

    @Autowired
    private MinioTemplate minioTemplate;

    @Autowired
    private MinioService minioService;

    @Autowired
    private RevitFilesService revitFilesService;

    @Autowired
    private XiaSanPicService xiaSanPicService;

    @Autowired
    private IObjSnapshotService objSnapshotService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ElementLibraryFileService elementLibraryFileService;
    @Autowired
    private DwgService dwgService;
    @Autowired
    private BomFileService bomFileService;

    @Resource
    private MinioProperties minioProperties;
    /**
     * 文件上传
     * @param file
     * @return
     */

//    public StreamFiles saveFile1(String fileName, MultipartFile multipartFile,Map<String,String> map) throws IOException {
//        // 确定存储路径
//        Path uploadPath = Paths.get(fileurl);
//        // 如果目录不存在则创建目录
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//        try {
//            // 保存文件
//            System.out.println("#########fileUrl: "+fileurl);
//            Path filePath = uploadPath.resolve(fileName);
//            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//            // 创建 StreamFiles 对象并保存到数据库
//            StreamFiles fileRecord = new StreamFiles();
//            fileRecord.setId(UUID.randomUUID().toString());
//            fileRecord.setObjectId("null"); // 设置其他需要的信息
//            fileRecord.setStreamId(map.get("streamsId"));
//            fileRecord.setName(fileName);
//            fileRecord.setType(multipartFile.getContentType());
//            fileRecord.setFileUrl(fileurl+fileName);
//            fileRecord.setModifyUser(map.get("modifyUser")); // Username needs to come from the logged-in user context or input
//            fileRecord.setModifyTime(new Date()); // Setting the current date as the modification time
//            fileRecord.setStatus(0); // Assuming 1 means active status
//            streamFilesMapper.addStreamFiles(fileRecord);
//            return fileRecord;
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Fail to store file", e);
//        }
//    }

//    public StreamFiles saveFile(String fileName, MultipartFile multipartFile,Map<String,String> map) throws IOException {
//        try {
////            MinioClient minioClient = minioService.getClient();
////            System.out.println(minioClient);
//            //检查桶是否存在
//            boolean bucketisExist = false;
//            bucketisExist = minioService.checkBucketExist(minioProperties.getBucketName());
//        if (!bucketisExist) {
//            // 创建桶
//           minioService.createBucket(minioProperties.getBucketName());
//        }
//        boolean folderisExist = minioService.checkBucketFolderExist(minioProperties.getBucketName(), minioProperties.getFolderName(), false);
//        if (!folderisExist){
////            创建文件夹
//            minioService.createBucketFolder(minioProperties.getBucketName(),minioProperties.getFolderName());
//        }
//        log.info("Fileupload starter, 开始文件上传！");
//        Map<String, String> resMap = minioService.uploadFile(multipartFile, minioProperties.getBucketName());
//        if ("0".equals(resMap.get("resCode"))){
//            log.info("文件"+multipartFile.getOriginalFilename()+"上传成功！");
//        }else {
//            log.info("文件"+multipartFile.getOriginalFilename()+"上传失败！");
//        }
//        String minioFilename = resMap.get("minioFilename");
//        String fileDownloadUrl = minioService.getFileDownloadUrl(minioProperties.getBucketName(), minioFilename, 999999999);
////             创建 StreamFiles 对象并保存到数据库
//            StreamFiles fileRecord = new StreamFiles();
//            fileRecord.setId(UUID.randomUUID().toString());
//            fileRecord.setObjectId("null"); // 设置其他需要的信息
//            fileRecord.setStreamId(map.get("streamsId"));
//            fileRecord.setName(fileName);
//            fileRecord.setType(multipartFile.getContentType());
//            fileRecord.setFileUrl(fileDownloadUrl);
//            fileRecord.setModifyUser(map.get("modifyUser")); // Username needs to come from the logged-in user context or input
//            fileRecord.setModifyTime(new Date()); // Setting the current date as the modification time
//            fileRecord.setStatus(0); // Assuming 1 means active status
//            streamFilesMapper.addStreamFiles(fileRecord);
//            return fileRecord;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Fail to store file", e);
//        }
//    }

    /**
     * 文件上传
     * @param fileName
     * @param multipartFile
     * @param map
     * @return
     * @throws IOException
     */
    public StreamFiles saveFile(String fileName, MultipartFile multipartFile,Map<String,String> map) throws IOException {
        try {
            log.info("开始上传文件: {}, MinIO配置: {}:{}", fileName, minioProperties.getIp(), minioProperties.getPort());
            //检查桶是否存在
            boolean bucketisExist = minioTemplate.bucketExists(minioProperties.getBucketName());
            log.info("桶是否存在: {}", bucketisExist);

            if (!bucketisExist) {
                minioTemplate.makeBucket(minioProperties.getBucketName());
                log.info("创建桶: {}", minioProperties.getBucketName());
            }
            //校验stream下是否有同名文件
            String streamId = map.get("streamId");
            StreamFiles remark = streamFilesMapper.getFile(streamId, fileName);
            if (ObjectUtils.isNotEmpty(remark)){
                log.error("Fileupload fail, 单工程下文件名重复，文件上传失败！");
                return null;
            }else {
                log.info("Fileupload starter, 开始文件上传！");
                BladeFile bladeFile = minioTemplate.putFile(minioProperties.getBucketName(), minioProperties.getDownloadDir()+multipartFile.getOriginalFilename(), multipartFile);
                //保存文件类型(模型: model、图纸: drawing、文件：file)
                String type = "";
                String fileType = extractFileExtension(bladeFile.getOriginalName());
                if ("dwg".equals(fileType) || "pdf".equals(fileType)){
                    type = "drawing";
                }
                if ("doc".equals(fileType)||"docx".equals(fileType)||"xls".equals(fileType)||"xlsx".equals(fileType)||"ppt".equals(fileType)||"pptx".equals(fileType)){
                    type = "file";
                }
                if ("rvt".equals(fileType)||"ifc".equals(fileType)||"obj".equals(fileType)||"stl".equals(fileType)){
                    type = "model";
                }
//             创建 StreamFiles 对象并保存到数据库
                StreamFiles fileRecord = new StreamFiles();
                fileRecord.setId(UUID.randomUUID().toString());
                fileRecord.setObjectId(new String[]{""}); // 设置其他需要的信息
                fileRecord.setStreamId(streamId);
                fileRecord.setName(bladeFile.getOriginalName());
                fileRecord.setType(type);
                fileRecord.setSize(map.get("size"));
                fileRecord.setFileUrl(bladeFile.getLink());
                fileRecord.setModifyUser(map.get("modifyUser")); // Username needs to come from the logged-in user context or input
                fileRecord.setModifyTime(new DateTime()); // Setting the current date as the modification time
                fileRecord.setStatus(0); // Assuming 1 means active status
                streamFilesMapper.addStreamFiles(fileRecord);
                return fileRecord;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fail to store file", e);
        }
    }

    /**
     * 夏三图片上传
     * @param fileName
     * @param multipartFile
     * @param map
     * @return
     * @throws IOException
     */
    public XiaSanPic saveXiaSanPicFile(String fileName, MultipartFile multipartFile, Map<String,String> map) throws IOException {
        try {
            //检查桶是否存在
            boolean bucketisExist = false;
            String bucketName = "xiasan-files";
            String downloadDir = "/uploads/xiaSanPic/";
            bucketisExist = minioTemplate.bucketExists(bucketName);
            if (!bucketisExist) {
                // 创建桶
                minioTemplate.makeBucket(bucketName);
            }
            //校验相同commit下是否有同名文件
            String streamId = map.get("streamId");
            String objectId = map.get("objectId");
            String commitId = map.get("commitId");
            String elementId = map.get("elementId");
            String name = new DateTime()+"-"+fileName;
            String checkStatus = map.get("checkStatus");
            String uploadUser = map.get("uploadUser");
            XiaSanPic remark = xiaSanPicService.getByName(name,streamId,elementId);
            if (ObjectUtils.isNotEmpty(remark)){
                log.error("Fileupload fail, 文件已存在，文件上传失败！");
                return null;
            }else {
                log.info("Fileupload starter, 开始文件上传！");
                BladeFile bladeFile = minioTemplate.putFile(bucketName, downloadDir+multipartFile.getOriginalFilename(), multipartFile);
                //创建实体保存数据库
                XiaSanPic fileRecord = new XiaSanPic();
                fileRecord.setId(UUID.randomUUID().toString());
                fileRecord.setObjectId(objectId);
                fileRecord.setStreamId(streamId);
                fileRecord.setCommitId(commitId);
                fileRecord.setElementId(elementId);
                fileRecord.setName(name);
                fileRecord.setUrl(bladeFile.getLink());
                fileRecord.setModifyUser(map.get("modifyUser")); // Username needs to come from the logged-in user context or input
                fileRecord.setModifyTime(new Date());
                fileRecord.setCheckStatus(checkStatus);
                fileRecord.setUploadUser(uploadUser);
                // Setting the current date as the modification time
                xiaSanPicService.addXiaSanPic(fileRecord);
                return fileRecord;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fail to store file", e);
        }
    }

    public ElementLibraryFile saveElementLibraryFile(String fileName, MultipartFile multipartFile,String pid) throws IOException {
        try {
            //检查桶是否存在
            boolean bucketisExist = false;
            String bucketName = "element-library-files";
            String downloadDir = "/uploads/elementLibraryPic/";
            bucketisExist = minioTemplate.bucketExists(bucketName);
            if (!bucketisExist) {
                // 创建桶
                minioTemplate.makeBucket(bucketName);
            }
            log.info("Fileupload starter, 开始文件上传！");
            BladeFile bladeFile = minioTemplate.putFile(bucketName, downloadDir + multipartFile.getOriginalFilename(), multipartFile);
            String type = extractFileExtension(bladeFile.getOriginalName());
            String fileUrl = bladeFile.getLink();
            String fileSize = (multipartFile.getSize()/1024)+"";
            ElementLibraryFile fileRecord = new ElementLibraryFile();
            fileRecord.setId(UUID.randomUUID().toString());
            fileRecord.setPid(pid);
            fileRecord.setName(fileName);
            fileRecord.setSystem("");
            fileRecord.setLod("");
            fileRecord.setType(type);
            fileRecord.setFileUrl(fileUrl);
            fileRecord.setFileSize(Long.valueOf(fileSize));
            fileRecord.setModifyUser("admin");
            fileRecord.setModifyTime(new Date());
            fileRecord.setStatus(0);
            elementLibraryFileService.addElementLibraryFile(fileRecord);
            return fileRecord;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean reupdateElementLibraryFile(String fileName, MultipartFile multipartFile,String pid,String fileId,String lod) throws IOException {
        try {
            //检查桶是否存在
            boolean bucketisExist = false;
            String bucketName = "element-library-files";
            String downloadDir = "/uploads/elementLibraryPic/";
            bucketisExist = minioTemplate.bucketExists(bucketName);
            if (!bucketisExist) {
                // 创建桶
                minioTemplate.makeBucket(bucketName);
            }
            log.info("Fileupload starter, 开始文件上传！");
            BladeFile bladeFile = minioTemplate.putFile(bucketName, downloadDir + multipartFile.getOriginalFilename(), multipartFile);
            String type = extractFileExtension(bladeFile.getOriginalName());
            String fileUrl = bladeFile.getLink();
            String fileSize = (multipartFile.getSize()/1024)+"";
            ElementLibraryFile fileRecord = elementLibraryFileService.getById(fileId);
            fileRecord.setType(type);
            fileRecord.setName(fileName);
            fileRecord.setFileUrl(fileUrl);
            fileRecord.setFileSize(Long.valueOf(fileSize));
            fileRecord.setModifyUser("revit");
            fileRecord.setModifyTime(new Date());
            fileRecord.setLod(lod);
            boolean b = elementLibraryFileService.updateElementLibraryFile(fileRecord);
            return b;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 碰撞检测文件上传
     * @param multipartFile 上传的文件
     * @param streamId 流ID
     * @param branchId 分支ID
     * @param commitId 提交ID
     * @return 返回MinIO文件URL
     * @throws IOException
     */
    public String saveCrashDetectionFile(MultipartFile multipartFile, String streamId, String branchId, String commitId) throws IOException {
        try {
            log.info("开始上传碰撞检测文件: {}, streamId: {}, branchId: {}, commitId: {}",
                    multipartFile.getOriginalFilename(), streamId, branchId, commitId);

            // 检查桶是否存在
            String bucketName = "crash-detection-files";
            String downloadDir = "/uploads/crashDetection/";

            boolean bucketExists = minioTemplate.bucketExists(bucketName);
            log.info("桶是否存在: {}", bucketExists);

            if (!bucketExists) {
                minioTemplate.makeBucket(bucketName);
                log.info("创建桶: {}", bucketName);
            }

            // 生成文件名：streamId_branchId_commitId_timestamp_原文件名
            String originalFilename = multipartFile.getOriginalFilename();


            log.info("开始上传文件到MinIO: {}", originalFilename);

            // 上传文件到MinIO
            BladeFile bladeFile = minioTemplate.putFile(bucketName, downloadDir + originalFilename, multipartFile);

            if (bladeFile != null && bladeFile.getLink() != null) {
                log.info("碰撞检测文件上传成功: {}", bladeFile.getLink());
                return bladeFile.getLink();
            } else {
                log.error("碰撞检测文件上传失败，返回的BladeFile为空");
                throw new RuntimeException("文件上传失败");
            }
        } catch (Exception e) {
            log.error("碰撞检测文件上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("碰撞检测文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 模型快照图片上传
     * @param fileName
     * @param multipartFile
     * @param map
     * @return
     * @throws IOException
     */
    public ObjSnapshot saveObjSnapshotFile(String fileName, MultipartFile multipartFile, Map<String,String> map) throws IOException {
        try {
            //检查桶是否存在
            boolean bucketisExist = false;
            String bucketName = "obj-snapshot-files";
            String downloadDir = "/uploads/objSnapshotPic/";
            bucketisExist = minioTemplate.bucketExists(bucketName);
            if (!bucketisExist) {
                // 创建桶
                minioTemplate.makeBucket(bucketName);
            }
            String name = new DateTime() + "-" + fileName;
            String streamId = map.get("streamId");
            String snapshotName = map.get("snapshotName");
            String modelData = map.get("modelData");
            String colorData = map.get("colorData");
            String cameraData = map.get("cameraData");
            String remark = map.get("remark");
            String commitId = map.get("commitId");
            String userId = map.get("userId");

            log.info("Fileupload starter, 开始文件上传！");
            BladeFile bladeFile = minioTemplate.putFile(bucketName, downloadDir+multipartFile.getOriginalFilename(), multipartFile);

            ObjSnapshot snapshot = new ObjSnapshot();
            snapshot.setStreamId(streamId);
            snapshot.setSnapshotName(snapshotName);
            snapshot.setModelData(modelData);
            snapshot.setColorData(colorData);
            snapshot.setCameraData(cameraData);
            snapshot.setRemark(remark);
            snapshot.setCommitId(commitId);
            snapshot.setUserId(userId);
            snapshot.setUrl(bladeFile.getLink());
            snapshot.setCreateTime(new Date());
            snapshot.setUpdateTime(new Date());
            objSnapshotService.save(snapshot);
            return snapshot;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Fail to store file", e);
        }
    }

    public Task saveTaskFile(String fileName, MultipartFile multipartFile, Map<String,String> map) throws IOException {
        try {
            //检查桶是否存在
            boolean bucketisExist = false;
            String bucketName = "task-files";
            String downloadDir = "/uploads/taskFiles/";
            bucketisExist = minioTemplate.bucketExists(bucketName);
            if (!bucketisExist) {
                // 创建桶
                minioTemplate.makeBucket(bucketName);
            }
            log.info("Fileupload starter, 开始文件上传！");
            BladeFile bladeFile = minioTemplate.putFile(bucketName, downloadDir+multipartFile.getOriginalFilename(), multipartFile);
            Task task = new Task();
            task.setId(UUID.randomUUID().toString());
            task.setStreamId(map.get("streamId"));
            task.setCommitId(map.get("commitId"));
            task.setType(map.get("type"));
            task.setTaskStatus(map.get("taskStatus"));
            task.setDetail(map.get("detail"));
            task.setModifyUser(map.get("modifyUser"));
            task.setDeadLine(map.get("deadLine"));
            task.setTargetId(map.get("targetId"));
            task.setCollisionId(map.get("collisionId"));
            task.setUrl(bladeFile.getLink());
            task.setOthers(map.get("others"));
            taskService.addTask(task);
            return task;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Fail to store file", e);
        }
    }

    public RevitFiles saveRevitFile(String fileName, MultipartFile multipartFile,Map<String,String> map) throws IOException {
        try {
            //检查桶是否存在
            boolean bucketisExist = false;
            String bucketName = "revit-files";
            String downloadDir = "/uploads/revitFiles/";
            bucketisExist = minioTemplate.bucketExists(bucketName);
            if (!bucketisExist) {
                // 创建桶
                minioTemplate.makeBucket(bucketName);
            }
            //校验相同commit下是否有同名文件
            String streamId = map.get("streamId");
            String branchId = map.get("branchId");
            String commitId = map.get("commitId");
            String type = map.get("type");
            String masterFileName = map.get("masterFileName");
            RevitFiles remark = revitFilesService.getByName(streamId, branchId, commitId,fileName);
            if (ObjectUtils.isNotEmpty(remark)){
                log.error("Fileupload fail, 文件已存在，文件上传失败！");
                return null;
            }else {
                log.info("Fileupload starter, 开始文件上传！");
                BladeFile bladeFile = minioTemplate.putFile(bucketName, downloadDir+multipartFile.getOriginalFilename(), multipartFile);
                //创建实体保存数据库
                RevitFiles fileRecord = new RevitFiles();
                fileRecord.setId(UUID.randomUUID().toString());
                fileRecord.setStreamId(streamId);
                fileRecord.setBranchId(branchId);
                fileRecord.setCommitId(commitId);
                fileRecord.setName(bladeFile.getOriginalName());
                fileRecord.setType(type);
                fileRecord.setMasterFileName(masterFileName);
                fileRecord.setFileUrl(bladeFile.getLink());
                fileRecord.setModifyUser(map.get("modifyUser")); // Username needs to come from the logged-in user context or input
                fileRecord.setModifyTime(new DateTime()); // Setting the current date as the modification time
                fileRecord.setStatus(0); // Assuming 1 means active status
                revitFilesService.addRevitFile(fileRecord);
                return fileRecord;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Fail to store file", e);
        }
    }

    public Comment saveCommentFile(String fileName, MultipartFile multipartFile,Map<String,String> map) throws IOException {
        try {
            //检查桶是否存在
            boolean bucketisExist = false;
            String bucketName = "comment-files";
            String downloadDir = "/uploads/commentFiles/";
            bucketisExist = minioTemplate.bucketExists(bucketName);
            if (!bucketisExist) {
                // 创建桶
                minioTemplate.makeBucket(bucketName);
            }
            log.info("Fileupload starter, 开始文件上传！");
            BladeFile bladeFile = minioTemplate.putFile(bucketName, downloadDir + multipartFile.getOriginalFilename(), multipartFile);

            //创建实体保存数据库
            Comment fileRecord = new Comment();
            fileRecord.setId(map.get("id"));
            fileRecord.setUrl(bladeFile.getLink());
            commentService.insertComment(fileRecord);
            return fileRecord;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Fail to store file", e);
        }
    }

    public DwgFile saveDwgFile(MultipartFile multipartFile,Map<String,String> map,JSONObject detail,String[] objectId) throws IOException {
        try {
            //检查桶是否存在
            boolean bucketisExist = false;
            String bucketName = "dwg-files";
            String downloadDir = "/uploads/dwgFiles/";
            bucketisExist = minioTemplate.bucketExists(bucketName);
            if (!bucketisExist) {
                // 创建桶
                minioTemplate.makeBucket(bucketName);
            }
            log.info("Fileupload starter, 开始文件上传");
            BladeFile bladeFile = minioTemplate.putFile(bucketName, downloadDir + multipartFile.getOriginalFilename(), multipartFile);
            //创建实体保存数据库
            DwgFile file = new DwgFile();
            file.setId(UUID.randomUUID().toString());
            file.setProjectId(map.get("projectId"));
            file.setName(map.get("name"));
            file.setMapId(map.get("mapId"));
            file.setUrl(bladeFile.getLink());
            file.setDetail(detail);
            file.setObjectId(objectId);
            file.setModifyUser(map.get("modifyUser")); // Username needs to come from the logged-in user context or input
            file.setModifyTime(new DateTime()); // Setting the current date as the modification time
            file.setStatus(0); // Assuming 1 means active status
            dwgService.addDwgFile(file);
            return file;
    }catch (Exception e){
        e.printStackTrace();
        throw new RuntimeException("Fail to store file", e);
    }
    }

    /**
     * LOI模板文件上传到MinIO
     * @param multipartFile 上传的文件
     * @return 文件访问URL
     * @throws IOException
     */
    public String uploadLoiTemplate(MultipartFile multipartFile) throws IOException {
        try {
            // 检查桶是否存在
            String bucketName = "loi-template-files";
            String downloadDir = "/uploads/loiTemplate/";
            boolean bucketExists = minioTemplate.bucketExists(bucketName);
            if (!bucketExists) {
                minioTemplate.makeBucket(bucketName);
                log.info("创建桶: {}", bucketName);
            }

            log.info("开始上传LOI模板文件: {}", multipartFile.getOriginalFilename());
            BladeFile bladeFile = minioTemplate.putFile(bucketName, downloadDir + multipartFile.getOriginalFilename(), multipartFile);
            log.info("LOI模板文件上传成功: {}", bladeFile.getLink());

            return bladeFile.getLink();
        } catch (Exception e) {
            log.error("LOI模板文件上传失败", e);
            throw new RuntimeException("LOI模板文件上传失败: " + e.getMessage(), e);
        }
    }

    public void downloadLoiTemplate(String fileName, HttpServletResponse response) throws Exception {
        try {
            log.info("开始下载LOI模板文件: {}", fileName);

            // 从classpath资源目录读取模板文件
            String templatePath = "templates/" + fileName;
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(templatePath);

            if (inputStream == null) {
                throw new Exception("模板文件不存在: " + fileName);
            }

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

            // 将文件内容写入响应
            try (OutputStream outputStream = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            } finally {
                inputStream.close();
            }

            log.info("LOI模板文件下载成功: {}", fileName);
        } catch (Exception e) {
            log.error("LOI模板文件下载失败: {}", fileName, e);
            throw new Exception("LOI模板文件下载失败: " + e.getMessage(), e);
        }
    }

    /**
     * 下载智链自定义数据模板文件
     * @param fileName 文件名
     * @param response HTTP响应对象
     * @throws Exception 如果下载过程中出现错误
     */
    public void downloadTemplate(String fileName, HttpServletResponse response) throws Exception {
        try {
            log.info("开始下载智链自定义数据模板文件: {}", fileName);

                // 如果数据库中没有记录，则从资源目录读取模板文件
                String templatePath = "templates/" + fileName;
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(templatePath);
                
                if (inputStream == null) {
                    throw new Exception("模板文件不存在: " + fileName);
                }
                
                // 设置响应头
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setCharacterEncoding("UTF-8");
                String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
                
                // 将文件内容写入响应
                try (OutputStream outputStream = response.getOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                } finally {
                    inputStream.close();
                }
                log.info("从资源目录下载智链自定义数据模板文件成功: {}", fileName);
        } catch (Exception e) {
            log.error("智链自定义数据模板文件下载失败: {}", fileName, e);
            throw new Exception("智链自定义数据模板文件下载失败: " + e.getMessage(), e);
        }
    }

    /**
     * Bom表格文件上传
     * @param multipartFile 上传的文件
     * @return 文件访问URL
     * @throws IOException
     */
    public BomFile uploadBomFile(MultipartFile multipartFile,Map<String,String> map) throws IOException {
        try {
            // 检查桶是否存在
            String bucketName = "bom-files";
            String downloadDir = "/uploads/bomFile/";
            boolean bucketExists = minioTemplate.bucketExists(bucketName);
            if (!bucketExists) {
                minioTemplate.makeBucket(bucketName);
                log.info("创建桶: {}", bucketName);
            }

            log.info("开始上传Bom文件: {}", multipartFile.getOriginalFilename());
            BladeFile bladeFile = minioTemplate.putFile(bucketName, downloadDir + multipartFile.getOriginalFilename(), multipartFile);
            log.info("Bom文件上传成功: {}", bladeFile.getLink());

            String fileUrl = bladeFile.getLink();
            String fileSize = (multipartFile.getSize()/1024)+"";
            //创建实体保存数据库
            BomFile fileRecord = new BomFile();
            fileRecord.setName(multipartFile.getOriginalFilename());
            fileRecord.setUrl(fileUrl);
            fileRecord.setType(map.get("type"));
            fileRecord.setModifyUser(map.get("modifyUser"));
            fileRecord.setModifyTime(new DateTime());
            fileRecord.setSize(fileSize);
            boolean b = bomFileService.addBomFile(fileRecord);
            if (!b){
                return null;
            }
            return fileRecord;

        } catch (Exception e) {
            log.error("Bom文件上传失败", e);
            throw new RuntimeException("Bom文件上传失败: " + e.getMessage(), e);
        }
    }



    //获取服务器文件相对路径
//    public String getUrl(String fileName){
//        String path = minioTemplate.filePath(minioProperties.getBucketName(), fileName);
//        return path;
//    }


    public boolean removeFile(String fileurl,String bucketName){
        boolean isRemove = false;
        try {
            minioTemplate.removeFile(bucketName,fileurl);
            isRemove = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return isRemove;
    }


    /**
     * 文件扩展名分割
     * @param filename
     * @return
     */
    public static String extractFileExtension(String filename) {
        int lastIndexOfDot = filename.lastIndexOf('.');
        if (lastIndexOfDot > 0) {
            return filename.substring(lastIndexOfDot + 1);
        }
        return "";
    }

    /**
     * 文件Url切割，根据bucketName动态分割
     * @param url 文件URL
     * @return 文件相对路径
     */
    public String extractPath(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }

        // 解析URL，提取bucketName和文件路径
        try {
            // 移除协议部分（http://或https://）
            String urlWithoutProtocol = url;
            if (url.contains("://")) {
                urlWithoutProtocol = url.split("://")[1];
            }

            // 移除域名和端口部分
            String path = "";
            if (urlWithoutProtocol.contains("/")) {
                path = urlWithoutProtocol.substring(urlWithoutProtocol.indexOf("/"));
            }

            // 第一个部分是bucketName
            String[] pathParts = path.split("/");
            if (pathParts.length > 1) {
                // 构建分割字符串，格式为：/bucketName/
                String splitPattern = "/" + pathParts[1] + "/";

                // 使用构建的分割字符串进行分割
                String[] parts = url.split(splitPattern, 2);
                if (parts.length > 1) {
                    return "/" + parts[1]; // 添加前导斜杠，保留upload目录
                }
            }

            // 如果无法解析，回退到原来的方法
            log.warn("无法解析URL: " + url + "，使用默认分割方法");
            String[] parts = url.split("/stream-files-uploads/");
            return parts.length > 1 ? parts[1] : "";
        } catch (Exception e) {
            log.error("解析URL时出错: " + url, e);
            // 出错时回退到原来的方法
            String[] parts = url.split("/stream-files-uploads/");
            return parts.length > 1 ? parts[1] : "";
        }
    }

    /**
     * 文件Url切割
     * @param
     * @return
     */
    public String xiasanExtractPath(String url) {
        // 可以通过 "/stream-files-uploads/" 分割来提取所需的部分
        String[] parts = url.split("/xiasan-files/upload/");
        // 返回切割后的字符串的第二部分（如果存在的话）
        return parts.length > 1 ? parts[1] : "";
    }

    public List<RevitParameter> parseFile(MultipartFile file) {
        List<RevitParameter> parameters = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            // 使用 OpenCSV 解析器构建 CSVReader
            CSVReader reader = new CSVReaderBuilder(br)
                    .withCSVParser(new CSVParserBuilder().withSeparator('\t').build()) // 使用制表符作为分隔符
                    .build();

            String[] line;
            boolean headerFound = false;
            int lineNumber = 0;

            // 读取每一行
            while ((line = reader.readNext()) != null) {
                lineNumber++;

                // Debug: 打印每行内容
                System.out.println("Line " + lineNumber + ": " + String.join("|", line));

                // 处理行前导和尾随空白及不可见字符
                for (int i = 0; i < line.length; i++) {
                    line[i] = line[i].trim().replaceAll("[^\\x20-\\x7E]", "");  // 去除不可见字符
                }

                // Debug: 打印每行内容的清理后情况
                System.out.println("Processed Line " + lineNumber + ": " + String.join("|", line));

                // 确保行长度正确，并且符合我们期望的格式
                if (line.length > 0 && line[0].replace("*", "").trim().equalsIgnoreCase("PARAM")) {
                    // 如果没有头部信息，找到后就跳过
                    if (!headerFound) {
                        headerFound = true;
                        continue;
                    }

                    // Debug: 打印符合条件的行
                    System.out.println("Matched Line " + lineNumber + ": " + String.join("|", line));

                    // 解析参数信息
                    RevitParameter parameter = new RevitParameter(
                            line[1], // GUID
                            line[2], // NAME
                            line[3], // DATATYPE
                            line.length > 4 ? line[4] : "", // DATACATEGORY, 可能缺失
                            line.length > 5 ? line[5] : "", // GROUP
                            line.length > 6 ? line[6] : "", // VISIBLE
                            line.length > 7 ? line[7] : "", // DESCRIPTION
                            line.length > 8 ? line[8] : "", // USERMODIFIABLE
                            line.length > 9 ? line[9] : ""  // HIDEWHENNOVALUE
                    );

                    parameters.add(parameter);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parameters;
    }


    public List<RevitParameter> parseFile1(MultipartFile file) {
        List<RevitParameter> parameters = new ArrayList<>();
        Map<String, String> groupMap = new HashMap<>();  // 存储Group ID和Name映射关系

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_16LE))) {
            String line;
            boolean isGroupSection = false;
            boolean isParamSection = false;

            // 逐行读取文件
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;  // 跳过空行
                }

                if (line.startsWith("*GROUP")) {
                    isGroupSection = true;
                    isParamSection = false;
                    continue;
                }

                if (line.startsWith("*PARAM")) {
                    isParamSection = true;
                    isGroupSection = false;
                    continue;
                }

                // 处理Group部分
                if (isGroupSection && line.startsWith("GROUP")) {
                    String[] groupData = line.split("\\t");
                    if (groupData.length >= 3) {
                        String groupId = groupData[1].trim();
                        String groupName = groupData[2].trim();
                        groupMap.put(groupId, groupName);  // 存储Group ID和Name的映射关系
                    }
                }

                // 处理Param部分
                if (isParamSection && line.startsWith("PARAM")) {
                    String[] paramData = line.split("\\t");
                    if (paramData.length >= 6) {
                        String paramGuid = paramData[1].trim();
                        String paramName = paramData[2].trim();
                        String paramDataType = paramData[3].trim();
                        String paramGroup = paramData[5].trim();

                        // 根据Group ID获取对应的Name
                        String groupName = groupMap.get(paramGroup);

                        RevitParameter parameter = new RevitParameter(
                                paramGuid,  // GUID
                                paramName,  // NAME
                                paramDataType,  // DATATYPE
                                "",  // DATACATEGORY (可能缺失)
                                groupName != null ? groupName : paramGroup,  // 匹配Group Name
                                paramData[6].trim(),  // VISIBLE
                                paramData.length > 7 ? paramData[7].trim() : "",  // DESCRIPTION
                                paramData.length > 8 ? paramData[8].trim() : "",  // USERMODIFIABLE
                                paramData.length > 9 ? paramData[9].trim() : ""   // HIDEWHENNOVALUE
                        );

                        parameters.add(parameter);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parameters;
    }





    // 获取当前层级的上一级层级名称
    public String getWbsParentLevel(String currentLevel) {
        switch (currentLevel) {
            case "分部工程":
                return "单位工程";
            case "子分部工程":
                return "分部工程";
            case "分项工程":
                return "子分部工程";
            case "位置拆解":
                return "分项工程";
            default:
                return null;
        }
    }

    // 获取当前层级的上一级层级名称
    public String getEncodingParentLevel(String currentLevel) {
        switch (currentLevel) {
            case "2":
                return "1";
            case "3":
                return "2";
            case "4":
                return "3";
            case "5":
                return "4";
            default:
                return null;
        }
    }

    // 获取当前层级的上一级层级名称
    public String getEbsParentLevel(String currentLevel) {
        switch (currentLevel) {
            case "1":
                return "0";
            case "2":
                return "1";
            case "3":
                return "2";
            case "4":
                return "3";
            default:
                return "root";
        }
    }


//    private static Boolean parseBoolean(String value) {
//        return "1".equals(value);
//    }
}
