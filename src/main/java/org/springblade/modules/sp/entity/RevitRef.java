package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.UUID;

@Data
@TableName("sp_revit_ref")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Revit字典对象", description = "Revit字典对象")
public class RevitRef extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;
    @TableField("name_en")
    private String nameEn;
    @TableField("name_ch")
    private String nameCh;
    @TableField("group")
    private String group;


    public RevitRef(String nameEn, String nameCh, String group) {
        this.id = UUID.randomUUID().toString();
        this.nameEn = nameEn;
        this.nameCh = nameCh;
        this.group = group;
    }
}
