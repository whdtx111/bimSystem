package org.springblade.modules.sp.entity;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.gson.JsonObject;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.tool.utils.DateTimeUtil;
import org.springblade.core.tool.utils.DateUtil;

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
public class DwgFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String projectId;
    private String[] objectId;
    private String name;
    private String mapId;
    private String url;
    private JSONObject detail;

    private String modifyUser;
    private Date modifyTime;
    private Integer status;

//    public DwgFile(String projectId,String[] objectId,String name, String mapId, JSONObject detail, String modifyUser) {
//        this.id = UUID.randomUUID().toString();
//        this.projectId = projectId;
//        this.objectId = objectId;
//        this.name = name;
//        this.mapId = mapId;
//        this.detail = detail;
//        this.modifyUser = modifyUser;
//        this.modifyTime = new Date();
//        this.status = 0;
//    }
}
