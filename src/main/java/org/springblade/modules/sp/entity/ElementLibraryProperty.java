package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.UUID;
@Data
@TableName("element_library_property")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ElementLibraryProperty对象", description = "ElementLibraryProperty实体表")
public class ElementLibraryProperty extends BaseEntity {

    private String id;
    private String pid;
    private String name;
    private String type;

    public ElementLibraryProperty(String id, String pid, String name, String type) {
        this.id = UUID.randomUUID().toString();
        this.pid = pid;
        this.name = name;
        this.type = type;
    }

    public ElementLibraryProperty() {
        this.id = UUID.randomUUID().toString();
    }
}
