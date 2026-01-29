package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.Task;
import org.springblade.modules.sp.mapper.TaskMapper;
import org.springblade.modules.sp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class TaskServiceImpl extends BaseServiceImpl<TaskMapper, Task> implements TaskService {

    @Autowired
    private TaskMapper taskMapper;


    @Override
    public Task getById(String id) {
        try {
            return taskMapper.getById(id);
        } catch (Exception e) {
           e.printStackTrace();
           return null;
        }
    }

    @Override
    public List<Task> getAllTasks() {
        try {
          return taskMapper.getAllTasks();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Task> getTask(String streamId, String commitId) {
        try {
            return taskMapper.getTask(streamId, commitId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addTask(Task task) {
        try {
            if (ObjectUtils.isEmpty(task)) {
                return false;
            }
           return taskMapper.addTask(task);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTask(Task task) {
        try {
            if (ObjectUtils.isEmpty(task)) {
                return false;
            }
           return taskMapper.updateTask(task);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteTaskById(String id) {
        try {
           return taskMapper.deleteTaskById(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
