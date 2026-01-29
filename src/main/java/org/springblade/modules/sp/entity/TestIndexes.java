package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sp_test_indexes")
@ApiModel(value = "检测指标", description = "检测指标对象")
public class TestIndexes extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 权限
     */
    private String auth;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建人（自定义，避免与BaseEntity冲突）
     */
    private String createdUser;

    /**
     * 来源
     */
    private String source;

    /**
     * 描述
     */
    private String description;

    public TestIndexes() {
        this.id = UUID.randomUUID().toString();
        setCreateTime(new Date());
    }

    public TestIndexes(String name, String auth, Integer status, String createdUser, String source, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.auth = auth;
        this.status = status;
        this.createdUser = createdUser;
        this.source = source;
        this.description = description;
        this.setCreateTime(new Date());
    }
}
