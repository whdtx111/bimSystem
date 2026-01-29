package org.springblade.modules.sp.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

/**
 * revit文件对象
 *
 * @author dengtx
 * @since 2024-05-06
 */
@Data
@TableName("sp_revit_files")
@EqualsAndHashCode(callSuper = true)
public class RevitFiles extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String id;
    private String streamId;
    private String branchId;
    private String commitId;
    private String name;
    private String type;
    private String fileUrl;
    private String size;
    private String masterFileName;

    private String modifyUser;
    private Date modifyTime;
    private Integer status;

    public RevitFiles(String streamId, String branchId, String commitId, String name, String type, String fileUrl, String size, String masterFileName,String modifyUser, Date modifyTime, Integer status) {
        this.id = UUID.randomUUID().toString();
        this.streamId = streamId;
        this.branchId = branchId;
        this.commitId = commitId;
        this.name = name;
        this.type = type;
        this.fileUrl = fileUrl;
        this.size = size;
        this.masterFileName = masterFileName;
        this.modifyUser = modifyUser;
        this.modifyTime = modifyTime;
        this.status = status;
    }

    public RevitFiles() {
    }
}
