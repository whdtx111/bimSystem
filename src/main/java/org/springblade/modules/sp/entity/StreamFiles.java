package org.springblade.modules.sp.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_dwg_file")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "dwg文件对象", description = "dwg文件对象")
/**
 * dwg实体类
 *
 * @author dengtx
 * @since 2024-05-06
 */
public class StreamFiles extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String[] objectId;
    private String streamId;
    private String name;
    private String type;
    private String fileUrl;
    private String size;

    private String modifyUser;
    private Date modifyTime;
    private Integer status;

//    public StreamFiles(String id,String objectID, String streamId, String name, String type, String fileUrl, String modifyUser, Date modifyTime, Integer status) {
//        this.id = UUID.randomUUID().toString();
//        this.objectID = objectID;
//        this.streamId = streamId;
//        this.name = name;
//        this.type = type;
//        this.fileUrl = fileUrl;
//        this.modifyUser = modifyUser;
//        this.modifyTime = modifyTime;
//        this.status = status;
//    }
}
