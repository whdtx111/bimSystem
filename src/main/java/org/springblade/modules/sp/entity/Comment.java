package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.UUID;

@Data
@TableName("sp_comment")
@ApiModel(value = "Comment对象", description = "Comment对象")
public class Comment {

    @TableField("id")
    private String id;

    @TableField("url")
    private String url;

    public Comment(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public Comment() {
    }
}
