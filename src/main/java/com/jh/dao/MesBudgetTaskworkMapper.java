package com.jh.dao;

import com.jh.entity.MesBudgetTaskwork;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesBudgetTaskworkMapper {
    int deleteByPrimaryKey(String id);

    int insert(MesBudgetTaskwork record);

    int insertSelective(MesBudgetTaskwork record);

    MesBudgetTaskwork selectByPrimaryKey(String id);

    List<MesBudgetTaskwork> selectByTaskmanId1(@Param("taskmanId")String taskmanId,@Param("taskId") String taskId,@Param("winNo")String winNo,@Param("name")String name,@Param("userProcessGroup") String userProcessGroup);
    List<MesBudgetTaskwork> selectByTaskmanId2(@Param("taskmanId")String taskmanId,@Param("taskId") String taskId,@Param("winNo")String winNo,@Param("name")String name,@Param("userProcessGroup") String userProcessGroup);
    List<MesBudgetTaskwork> taskSendAllModel(@Param("taskId") String taskId,@Param("userProcessGroup") String userProcessGroup);
    MesBudgetTaskwork taskSendDoneModel(@Param("taskId") String taskId, @Param("procedureId") String procedureId, @Param("winNo")String winNo,@Param("userProcessGroup") String userProcessGroup);
    List<MesBudgetTaskwork> dealSelectWinNo(@Param("taskId") String taskId);
    List<MesBudgetTaskwork> dealSelectModelName(@Param("taskId") String taskId);

    int updateByPrimaryKeySelective(MesBudgetTaskwork record);

    MesBudgetTaskwork getDealProblem(@Param("id")String id);
    MesBudgetTaskwork dealGetNumbers(@Param("procedureId")String procedureId,@Param("taskWinType") String winName);
    MesBudgetTaskwork getDonePercent(@Param("taskId")String taskId);

    MesBudgetTaskwork getTotalLeftNum(@Param("taskId")String taskId,@Param("procedureId")String procedureId,@Param("winNo")String winNo);

    int updateByPrimaryKey(MesBudgetTaskwork record);

    MesBudgetTaskwork taskSendChild(@Param("taskId") String taskId);
    MesBudgetTaskwork taskSendMother(@Param("taskId")String taskId);

    List<MesBudgetTaskwork> getTaskSend();
    List<MesBudgetTaskwork> getTaskSendDetail(@Param("taskId") String taskId);
    List<MesBudgetTaskwork> getSalaryList(@Param("beginTime")String beginTime,@Param("endTime")String endTime,@Param("taskmanId")String taskmanId);

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