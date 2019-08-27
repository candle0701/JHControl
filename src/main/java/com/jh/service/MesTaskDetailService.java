package com.jh.service;

import com.jh.entity.MesTaskDetail;

import java.util.List;
import java.util.Map;

public interface MesTaskDetailService {
    int insertSelective(MesTaskDetail record);
    int deleteByPrimaryKey(String id);
    int updateByPrimaryKeySelective(MesTaskDetail record);
    int updateByTaskId(MesTaskDetail record);
    int deleteByTaskId(String taskId);
    List<MesTaskDetail> selectByTaskId(String taskId);
    MesTaskDetail selectByPrimaryKey(String id);
    List<MesTaskDetail> ifUsedByTaskIdWinNoTaskWinType(String projectId,String winNo);
    List<Map<String, String>> findMap(String taskName);

    MesTaskDetail allCount(MesTaskDetail mesTaskDetail);

    List<MesTaskDetail> list(MesTaskDetail mesTaskDetail);

    List<MesTaskDetail> find(MesTaskDetail mesTaskDetail);

    List<MesTaskDetail> selAll(String winModel);

    MesTaskDetail selcount(MesTaskDetail mesTaskDetail);

    List<MesTaskDetail> sel(String id);

    MesTaskDetail countDate(MesTaskDetail mesTaskDetail);
}
