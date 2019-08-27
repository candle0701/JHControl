package com.jh.service.impl;

import com.jh.dao.MesTaskDetailMapper;
import com.jh.entity.MesTaskDetail;
import com.jh.service.MesTaskDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MesTaskDetailServiceImpl implements MesTaskDetailService {

    @Autowired
    private MesTaskDetailMapper mesTaskDetailMapper;

    @Override
    public int insertSelective(MesTaskDetail record) {
        return mesTaskDetailMapper.insertSelective(record);
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return mesTaskDetailMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(MesTaskDetail record) {
        return mesTaskDetailMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByTaskId(MesTaskDetail record) {
        return mesTaskDetailMapper.updateByTaskId(record);
    }

    @Override
    public int deleteByTaskId(String taskId) {
        return mesTaskDetailMapper.deleteByTaskId(taskId);
    }

    @Override
    public List<MesTaskDetail> selectByTaskId(String taskId) {
        return mesTaskDetailMapper.selectByTaskId(taskId);
    }

    @Override
    public MesTaskDetail selectByPrimaryKey(String id) {
        return mesTaskDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<MesTaskDetail> ifUsedByTaskIdWinNoTaskWinType(String projectId, String winNo) {
        return mesTaskDetailMapper.ifUsedByTaskIdWinNoTaskWinType(projectId,winNo);
    }

    @Override
    public List<Map<String, String>> findMap(String taskName) {
        return mesTaskDetailMapper.findMap(taskName);
    }

    @Override
    public List<MesTaskDetail> list(MesTaskDetail mesTaskDetail) {
        return mesTaskDetailMapper.list(mesTaskDetail);
    }

    @Override
    public List<MesTaskDetail> find(MesTaskDetail mesTaskDetail) {
        return mesTaskDetailMapper.find(mesTaskDetail);
    }

    @Override
    public List<MesTaskDetail> selAll(String winModel) {
        return mesTaskDetailMapper.selAll(winModel);
    }

    @Override
    public MesTaskDetail selcount(MesTaskDetail mesTaskDetail) {
        return mesTaskDetailMapper.selcount(mesTaskDetail);
    }

    @Override
    public List<MesTaskDetail> sel(String id) {
        return mesTaskDetailMapper.sel(id);
    }

    @Override
    public MesTaskDetail allCount(MesTaskDetail mesTaskDetail) {
        return mesTaskDetailMapper.allCount(mesTaskDetail);
    }

    @Override
    public MesTaskDetail countDate(MesTaskDetail mesTaskDetail) {
        return mesTaskDetailMapper.countDate(mesTaskDetail);
    }
}
