package org.springblade.modules.sp.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springblade.core.tool.node.INode;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "WbsInfoUeDetailsVO对象", description = "WbsInfoUeDetailsVO对象")
public class BimTreeVO implements INode {
    private static final long serialVersionUID = 1L;

    private String code;
    private String pCode;
    private String level;

    /**
     * 主键ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String label;

    /**
     * 父节点ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    /**
     * 子孙节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<INode> children;

    @Override
    public List<INode> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        return this.children;
    }
}
