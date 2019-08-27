package com.jh.dao;

import com.jh.entity.MesTaskDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MesTaskDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(MesTaskDetail record);

    int insertSelective(MesTaskDetail record);

    MesTaskDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MesTaskDetail record);

    int updateByPrimaryKey(MesTaskDetail record);

    int updateByTaskId(MesTaskDetail record);

    int deleteByTaskId(@Param("taskId") String taskId);

    List<MesTaskDetail> selectByTaskId(@Param("taskId")String taskId);
    List<MesTaskDetail> ifUsedByTaskIdWinNoTaskWinType(@Param("projectId")String projectId,@Param("winNo")String winNo);

    MesTaskDetail getTaskNumByWinNoAndTaskId(@Param("winNo") String winNo,@Param("taskId")String taskId);

    List<Map<String, String>> findMap(@Param("taskName")String taskName);
    MesTaskDetail allCount(MesTaskDetail mesTaskDetail);
    List<MesTaskDetail> list(MesTaskDetail mesTaskDetail);

    List<MesTaskDetail> find(MesTaskDetail mesTaskDetail);

    List<MesTaskDetail> selAll(String winModel);

    MesTaskDetail selcount(MesTaskDetail mesTaskDetail);

    List<MesTaskDetail> sel(String id);

    MesTaskDetail countDate(MesTaskDetail mesTaskDetail);
}