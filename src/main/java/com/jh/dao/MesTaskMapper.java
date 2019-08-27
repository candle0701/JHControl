package com.jh.dao;

import com.jh.entity.MesTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesTaskMapper {
    int deleteByPrimaryKey(String id);

    int insert(MesTask record);

    int insertSelective(MesTask record);

    MesTask selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MesTask record);

    int updateByPrimaryKey(MesTask record);

    List<MesTask> findAllTask();
    List<MesTask> selectDistinctAll(@Param("id") String id);

    List<MesTask> findReleaseTask();
    List<MesTask> getTasking();

    List<MesTask> search(@Param("id") String id, @Param("taskName")String taskName);

    List<MesTask> pageList(MesTask mesTask);
    List<MesTask> taskIdButtonDetail(@Param("taskId") String taskId);

    MesTask taskCount(MesTask mesTask);

    List<MesTask> selProcessDate(MesTask mesTask);

    MesTask selDate(MesTask task);
    MesTask getNumsByProjectIdAndWinNo(@Param("projectId") String projectId,@Param("winNo") String winNo);
}