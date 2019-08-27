package com.jh.service;

import com.jh.entity.MesProductionOrder;
import com.jh.entity.MesTaskDetail;

import java.util.Collection;
import java.util.List;

public interface MesProductionOrderService {

    int insertSelective(MesProductionOrder mesProductionOrder);

    int update(MesProductionOrder mesProductionOrder);
    int updateByTaskId(MesProductionOrder mesProductionOrder);

    List<MesProductionOrder> findList(MesProductionOrder mesProductionOrder);

    List<MesProductionOrder> getListByTaskCode(MesProductionOrder mesProductionOrder);

    List<MesProductionOrder> getOrderDetailByTaskId(String taskId);
    List<MesProductionOrder> getSumOrderDetailByTaskId(String taskId);

    MesProductionOrder find(String  id);

    List<MesTaskDetail> sel();
}
