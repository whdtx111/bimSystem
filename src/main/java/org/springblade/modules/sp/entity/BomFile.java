package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;
@Data
@TableName("sp_bom_file")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Bom文件实体", description = "Bom文件实体")
public class BomFile extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String url;
    private String type;
    private String size;
    private String modifyUser;
    private Date modifyTime;

    public BomFile() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }

    private BomFile(String id, String name, String url, String type, String size, String modifyUser) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.url = url;
        this.type = type;
        this.size = size;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
    }
}
