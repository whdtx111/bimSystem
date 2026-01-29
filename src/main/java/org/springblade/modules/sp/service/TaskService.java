package org.springblade.modules.sp.service;

import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.Task;

import java.util.List;

public interface TaskService extends BaseService<Task> {


    Task getById(@Param("id") String id);

    List<Task> getAllTasks();


    List<Task> getTask(String streamId, String commitId);

    boolean addTask(Task task);

    boolean updateTask(Task task);

    boolean deleteTaskById(@Param("id") String id);
}
