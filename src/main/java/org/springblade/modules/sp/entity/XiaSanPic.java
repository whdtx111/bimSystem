package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("xia_san_pic")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "XiaSanPic对象", description = "XiaSanPic对象")
public class XiaSanPic extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String streamId;
    private String commitId;
    private String objectId;
    private String elementId;
    private String name;
    private String url;
    private String checkStatus;
    private String uploadUser;

    private String modifyUser;
    private Date modifyTime;

    public XiaSanPic(String streamId, String commitId, String objectId,String elementId ,String name, String url,String checkStatus,String uploadUser,String modifyUser, Date modifyTime) {
        this.id = UUID.randomUUID().toString();
        this.streamId = streamId;
        this.commitId = commitId;
        this.objectId = objectId;
        this.elementId=elementId;
        this.name = name;
        this.url = url;
        this.checkStatus = checkStatus;
        this.uploadUser = uploadUser;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
    }

    public XiaSanPic() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }
}
