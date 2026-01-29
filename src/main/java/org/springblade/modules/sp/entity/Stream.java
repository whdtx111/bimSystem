package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

@Data
@TableName("streams")
@ApiModel(value = "streams对象", description = "streams对象")
public class Stream extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @TableField("id")
    private String id;
    @TableField("name")
    private String name;

    public Stream(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Stream() {
    }
}
