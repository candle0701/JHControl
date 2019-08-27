package com.jh.service;

import com.jh.entity.MesTask;

import java.util.List;

public interface MesTaskService {

    List<MesTask> findAllTask();

    List<MesTask> findReleaseTask();

    int updateByPrimaryKeySelective(MesTask record);

    MesTask selectByPrimaryKey(String id);

    int insertSelective(MesTask record);

    int deleteByPrimaryKey(String id);

    List<MesTask> search(String id, String taskName);

    List<MesTask> pageList(MesTask mesTask);
    List<MesTask> getTasking();
    List<MesTask> taskIdButtonDetail(String taskId);

    List<MesTask> selectDistinctAll( String id);

    MesTask taskCount(MesTask mesTask);

    List<MesTask> selProcessDate(MesTask mesTask);

    MesTask selDate(MesTask task);
    MesTask getNumsByProjectIdAndWinNo(String projectId,String winNo);
}
