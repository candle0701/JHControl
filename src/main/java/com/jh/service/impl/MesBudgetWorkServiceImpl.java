package com.jh.service.impl;

import com.jh.dao.MesBudgetTaskworkMapper;
import com.jh.entity.MesBudgetTaskwork;
import com.jh.service.MesBudgetWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesBudgetWorkServiceImpl implements MesBudgetWorkService {

    @Autowired
    private MesBudgetTaskworkMapper mesBudgetTaskworkMapper;

    @Override
    public int insertSelective(MesBudgetTaskwork record) {
        return mesBudgetTaskworkMapper.insertSelective(record);
    }

    @Override
    public List<MesBudgetTaskwork> selectByTaskmanId1(String taskmanId,String taskId,String winNo,String name,String userProcessGroup) {
        return mesBudgetTaskworkMapper.selectByTaskmanId1(taskmanId,taskId,winNo,name,userProcessGroup);
    }
    @Override
    public List<MesBudgetTaskwork> selectByTaskmanId2(String taskmanId,String taskId,String winNo,String name,String userProcessGroup) {
        return mesBudgetTaskworkMapper.selectByTaskmanId2(taskmanId,taskId,winNo,name,userProcessGroup);
    }

    @Override
    public List<MesBudgetTaskwork> getTaskSend() {
        return mesBudgetTaskworkMapper.getTaskSend();
    }

    @Override
    public MesBudgetTaskwork getDealProblem(String id) {
        return mesBudgetTaskworkMapper.getDealProblem(id);
    }

    @Override
    public MesBudgetTaskwork dealGetNumbers(String procedureId,String winName) {
        return mesBudgetTaskworkMapper.dealGetNumbers(procedureId,winName);
    }

    @Override
    public MesBudgetTaskwork getTotalLeftNum(String taskId, String procedureId,String winNo) {
        return mesBudgetTaskworkMapper.getTotalLeftNum(taskId,procedureId,winNo);
    }

    @Override
    public MesBudgetTaskwork taskSendChild(String taskId) {
        return mesBudgetTaskworkMapper.taskSendChild(taskId);
    }

    @Override
    public MesBudgetTaskwork taskSendMother(String taskId) {
        return mesBudgetTaskworkMapper.taskSendMother(taskId);
    }

    @Override
    public List<MesBudgetTaskwork> taskSendAllModel(String taskId,String userProcessGroup) {
        return mesBudgetTaskworkMapper.taskSendAllModel(taskId,userProcessGroup);
    }

    @Override
    public MesBudgetTaskwork taskSendDoneModel(String taskId,String procedureId,String winNo,String userProcessGroup) {
        return mesBudgetTaskworkMapper.taskSendDoneModel(taskId,procedureId,winNo,userProcessGroup);
    }

    @Override
    public List<MesBudgetTaskwork> getTaskSendDetail(String taskId) {
        return mesBudgetTaskworkMapper.getTaskSendDetail(taskId);
    }

    @Override
    public List<MesBudgetTaskwork> getSalaryList(String beginTime, String endTime,String taskmanId) {
        return mesBudgetTaskworkMapper.getSalaryList(beginTime,endTime,taskmanId);
    }

    @Override
    public MesBudgetTaskwork getDonePercent(String taskId) {
        return mesBudgetTaskworkMapper.getDonePercent(taskId);
    }

    @Override
    public List<MesBudgetTaskwork> dealSelectWinNo(String taskId) {
        return mesBudgetTaskworkMapper.dealSelectWinNo(taskId);
    }

    @Override
    public List<MesBudgetTaskwork> dealSelectModelName(String taskId) {
        return mesBudgetTaskworkMapper.dealSelectModelName(taskId);
    }

    @Override
    public List<MesBudgetTaskwork> find(MesBudgetTaskwork mesBudgetTaskwork) {
        return mesBudgetTaskworkMapper.find(mesBudgetTaskwork);
    }

    @Override
    public List<MesBudgetTaskwork>  query(MesBudgetTaskwork mesBudgetTaskwork) {
        return mesBudgetTaskworkMapper.query(mesBudgetTaskwork);
    }

    @Override
    public List<MesBudgetTaskwork> findAll(MesBudgetTaskwork mesBudgetTaskwork) {
        return mesBudgetTaskworkMapper.find(mesBudgetTaskwork);
    }

    @Override
    public List<MesBudgetTaskwork> all(MesBudgetTaskwork mesBudgetTaskwork) {
        return mesBudgetTaskworkMapper.all(mesBudgetTaskwork);
    }

    @Override
    public List<MesBudgetTaskwork> findList(MesBudgetTaskwork mesBudgetTaskwork) {
        return mesBudgetTaskworkMapper.findList(mesBudgetTaskwork);
    }

    @Override
    public List<MesBudgetTaskwork> selAllCount(MesBudgetTaskwork mesBudgetTaskwork) {
        return mesBudgetTaskworkMapper.selAllCount(mesBudgetTaskwork);
    }

    @Override
    public List<MesBudgetTaskwork> findCountList(MesBudgetTaskwork mesBudgetTaskwork) {
        return mesBudgetTaskworkMapper.findCountList(mesBudgetTaskwork);
    }

    @Override
    public List<MesBudgetTaskwork> selFind(MesBudgetTaskwork mesBudgetTaskwork) {
        return mesBudgetTaskworkMapper.selFind(mesBudgetTaskwork);
    }

    @Override
    public List<MesBudgetTaskwork> selAll(MesBudgetTaskwork mesBudgetTaskwork) {
        return mesBudgetTaskworkMapper.selAll(mesBudgetTaskwork);
    }

    @Override
    public List<MesBudgetTaskwork> queryList(MesBudgetTaskwork mesBudgetTaskwork) {
        return mesBudgetTaskworkMapper.queryList(mesBudgetTaskwork);
    }
}
