package com.jh.service;

import com.jh.entity.MesBudgetTaskwork;

import java.util.List;

public interface MesBudgetWorkService {
    int insertSelective(MesBudgetTaskwork record);

    List<MesBudgetTaskwork> selectByTaskmanId1(String taskmanId,String taskId,String winNo,String name,String userProcessGroup);
    List<MesBudgetTaskwork> selectByTaskmanId2(String taskmanId,String taskId,String winNo,String name,String userProcessGroup);

    List<MesBudgetTaskwork> getTaskSend();

    MesBudgetTaskwork getDealProblem(String id);
    MesBudgetTaskwork dealGetNumbers(String procedureId,String winName);
    MesBudgetTaskwork getTotalLeftNum(String taskId,String procedureId,String winNo);
    MesBudgetTaskwork taskSendChild(String taskId);
    MesBudgetTaskwork taskSendMother(String taskId);
    List<MesBudgetTaskwork> taskSendAllModel(String taskId,String userProcessGroup);
    MesBudgetTaskwork taskSendDoneModel(String taskId,String procedureId,String winNo,String userProcessGroup);
    List<MesBudgetTaskwork> getTaskSendDetail(String taskId);
    List<MesBudgetTaskwork> getSalaryList(String beginTime,String endTime,String taskmanId);
    MesBudgetTaskwork getDonePercent(String taskId);
    List<MesBudgetTaskwork> dealSelectWinNo(String taskId);
    List<MesBudgetTaskwork> dealSelectModelName(String taskId);
    List<MesBudgetTaskwork> find(MesBudgetTaskwork mesBudgetTaskwork);

    List<MesBudgetTaskwork> findAll(MesBudgetTaskwork mesBudgetTaskwork);

    List<MesBudgetTaskwork>  query(MesBudgetTaskwork mesBudgetTaskwork);

    List<MesBudgetTaskwork> queryList(MesBudgetTaskwork mesBudgetTaskwork);

    List<MesBudgetTaskwork> all(MesBudgetTaskwork mesBudgetTaskwork);

    List<MesBudgetTaskwork> selAll(MesBudgetTaskwork mesBudgetTaskwork);

    List<MesBudgetTaskwork> findList(MesBudgetTaskwork mesBudgetTaskwork);

    List<MesBudgetTaskwork> selFind(MesBudgetTaskwork mesBudgetTaskwork);

    List<MesBudgetTaskwork> findCountList(MesBudgetTaskwork mesBudgetTaskwork);

    List<MesBudgetTaskwork> selAllCount(MesBudgetTaskwork mesBudgetTaskwork);
}
