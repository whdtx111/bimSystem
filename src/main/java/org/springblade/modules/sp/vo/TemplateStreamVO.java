package org.springblade.modules.sp.vo;

import lombok.Data;

import java.util.Date;

@Data
public class TemplateStreamVO {
    /* --- NewTemplate 字段 --- */
    private String id;          // 模板 id
    private String pid;
    private String fileId;
    private String templateId;
    private String streamId;
    private String name;
    private String type;
    private String description;
    private String auth;
    private String source;
    private String version;
    private String detail;
    private String modifyUser;
    private Date modifyTime;
    private Integer status;

    /* --- StreamList 字段 --- */
    private String   streamListId;        // sl_id
    private String[] streamIds;           // stream_ids
    private Date     streamListModifyTime;// sl_modify_time
}
