package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_element_library_file")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ElementLibraryFile", description = "ElementLibraryFile")
public class ElementLibraryFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String pid;
    private String name;
    private String type;
    private String system;
    private String lod;
    private String fileUrl;
    private Long fileSize;
    private String modifyUser;
    private Date modifyTime;
    private Integer status;

    public ElementLibraryFile(String id, String pid,String name, String type, String system, String lod, String fileUrl, Long fileSize, String modifyUser, Date modifyTime, Integer status) {
        this.id = UUID.randomUUID().toString();
        this.pid = pid;
        this.name = name;
        this.type = type;
        this.system = system;
        this.lod = lod;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
        this.status = status;
    }
    public ElementLibraryFile() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }
}
