package org.springblade.modules.sp.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class ElementLibraryWithFiles extends ElementLibrary {

    // 新增字段：关联的文件信息
    private List<ElementLibraryFile> files;

    public ElementLibraryWithFiles() {
        super(); // 调用父类构造方法
    }

    // 全参构造方法
    public ElementLibraryWithFiles(String[] fileIds, String name, String version, String auth,
                                   String[] project, String discription, String[] tags,
                                   List<JSONObject> codeNameParamsMapList, List<JSONObject> propertyMgt,
                                   String modifyUser, Date modifyTime, Date createTime, Integer status,String streamId,String branchId,String commitId,String loiNow,String lodNow,String library,
                                   List<ElementLibraryFile> files) {
        super(fileIds, name, version, auth, project, discription, tags,
                codeNameParamsMapList, propertyMgt, modifyUser, modifyTime, createTime, status,streamId,branchId,commitId,loiNow,lodNow,library);
        this.files = files;
    }

    // Getter 和 Setter 方法
    public List<ElementLibraryFile> getFiles() {
        return files;
    }

    public void setFiles(List<ElementLibraryFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "ElementLibraryWithFiles{" +
                "files=" + files +
                ", baseProperties=" + super.toString() +
                '}';
    }
}
