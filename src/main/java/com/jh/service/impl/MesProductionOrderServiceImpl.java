package com.jh.service.impl;

import com.jh.dao.MesProductionOrderMapper;
import com.jh.entity.MesProductionOrder;
import com.jh.entity.MesTaskDetail;
import com.jh.service.MesProductionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesProductionOrderServiceImpl implements MesProductionOrderService {


    @Autowired
    private MesProductionOrderMapper mesProductionOrderMapper;

    @Override
    public int insertSelective(MesProductionOrder mesProductionOrder) {
        return mesProductionOrderMapper.insertSelective(mesProductionOrder);
    }

    @Override
    public int update(MesProductionOrder mesProductionOrder) {
        return mesProductionOrderMapper.update(mesProductionOrder);
    }

    @Override
    public int updateByTaskId(MesProductionOrder mesProductionOrder) {
        return mesProductionOrderMapper.updateByTaskId(mesProductionOrder);
    }

    @Override
    public List<MesProductionOrder> findList(MesProductionOrder mesProductionOrder) {
        return mesProductionOrderMapper.findList(mesProductionOrder);
    }

    @Override
    public List<MesProductionOrder> getListByTaskCode(MesProductionOrder mesProductionOrder) {
        return mesProductionOrderMapper.getListByTaskCode(mesProductionOrder);
    }

    @Override
    public List<MesProductionOrder> getOrderDetailByTaskId(String taskId) {
        return mesProductionOrderMapper.getOrderDetailByTaskId(taskId);
    }

    @Override
    public List<MesProductionOrder> getSumOrderDetailByTaskId(String taskId) {
        return mesProductionOrderMapper.getSumOrderDetailByTaskId(taskId);
    }

    @Override
    public MesProductionOrder find(String id) {
        return mesProductionOrderMapper.find(id);
    }

    @Override
    public List<MesTaskDetail> sel() {
        return mesProductionOrderMapper.sel();
    }
}
