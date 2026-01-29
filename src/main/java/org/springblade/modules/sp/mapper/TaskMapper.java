package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.LibParameters;
import org.springblade.modules.sp.entity.Task;

import java.util.List;
@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    Task getById(@Param("id") String id);

    List<Task> getAllTasks();

    List<Task> getTask(String streamId, String commitId);

    boolean addTask(Task task);

    boolean updateTask(Task task);

    boolean deleteTaskById(@Param("id") String id);

}
