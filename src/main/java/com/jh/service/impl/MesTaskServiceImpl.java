package com.jh.service.impl;

import com.jh.dao.MesTaskMapper;
import com.jh.entity.MesTask;
import com.jh.service.MesTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesTaskServiceImpl implements MesTaskService {

    @Autowired
    private MesTaskMapper mesTaskMapper;

    @Override
    public List<MesTask> findAllTask() {
        return mesTaskMapper.findAllTask();
    }

    @Override
    public List<MesTask> findReleaseTask() {
        return mesTaskMapper.findReleaseTask();
    }

    @Override
    public int updateByPrimaryKeySelective(MesTask record) {
        return mesTaskMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public MesTask selectByPrimaryKey(String id) {
        return mesTaskMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(MesTask record) {
        return mesTaskMapper.insert(record);
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return mesTaskMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<MesTask> pageList(MesTask mesTask) {
        return mesTaskMapper.pageList(mesTask);
    }

    @Override
    public List<MesTask> getTasking() {
        return mesTaskMapper.getTasking();
    }

    @Override
    public List<MesTask> taskIdButtonDetail(String taskId) {
        return mesTaskMapper.taskIdButtonDetail(taskId);
    }

    @Override
    public List<MesTask> selectDistinctAll(String id) {
        return mesTaskMapper.selectDistinctAll(id);
    }

    @Override
    public MesTask taskCount(MesTask mesTask) {
        return mesTaskMapper.taskCount(mesTask);
    }

    @Override
    public MesTask selDate(MesTask task) {
        return mesTaskMapper.selDate(task);
    }

    @Override
    public MesTask getNumsByProjectIdAndWinNo(String projectId, String winNo) {
        return mesTaskMapper.getNumsByProjectIdAndWinNo(projectId,winNo);
    }

    @Override
    public List<MesTask> selProcessDate(MesTask mesTask) {
        return mesTaskMapper.selProcessDate(mesTask);
    }

    @Override
    public List<MesTask> search(String id, String taskName) {
        return mesTaskMapper.search(id,taskName);
    }
}
