package com.jh.dao;

import com.jh.entity.MesProductionOrder;
import com.jh.entity.MesTaskDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesProductionOrderMapper {
    int insert(MesProductionOrder record);

    int insertSelective(MesProductionOrder record);

    MesProductionOrder find(String id);

    List<MesProductionOrder> findList(MesProductionOrder mesProductionOrder);

    List<MesProductionOrder> getListByTaskCode(MesProductionOrder mesProductionOrder);
    List<MesProductionOrder> getOrderDetailByTaskId(@Param("taskId")String taskId);
    List<MesProductionOrder> getSumOrderDetailByTaskId(@Param("taskId")String taskId);

    int update(MesProductionOrder mesProductionOrder);
    int updateByTaskId(MesProductionOrder mesProductionOrder);
    List<MesTaskDetail> sel();
}