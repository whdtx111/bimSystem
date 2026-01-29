package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_stream_list")
public class StreamList extends BaseEntity {

    private String id;
    private String[] streamIds;
    private Date modifyTime;

    public StreamList() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }

    public StreamList(String[] streamIds) {
        this.id = UUID.randomUUID().toString();
        this.streamIds = streamIds;
        this.modifyTime = new Date();
    }

}
