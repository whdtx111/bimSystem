package org.springblade.modules.sp.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@ApiModel(value = "BimListTreeVO对象", description = "BimListTreeVO对象")
public class BimListTreeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String parentId;
    private String label;
    private String level;
    private String objId;

    private String originalOwnId;
    private String speckleType;
    private List<String> definitionObjIdList;
    private List<String> definitionElementIdList;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null || getClass() != obj.getClass()) { return false; }
        BimListTreeVO other = (BimListTreeVO) obj;
        return Objects.equals(id, other.id);
    }

}
