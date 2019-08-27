package com.jh.service.impl;

import com.jh.dao.MesProjectDetailMapper;
import com.jh.dao.MesWinModelMapper;
import com.jh.entity.MesWinModel;
import com.jh.service.MesWinModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesWinModelServiceImpl implements MesWinModelService {

    @Autowired
    private MesWinModelMapper mesWinModelMapper;


    public List<MesWinModel> findList(MesWinModel mesWinModel){
        return  mesWinModelMapper.findList(mesWinModel);
    }


    public  int del(MesWinModel mesWinModel){
        return mesWinModelMapper.del(mesWinModel);
    }
    public  int insertSelective(MesWinModel mesWinModel){
        return mesWinModelMapper.insertSelective(mesWinModel);
    }

    public   MesWinModel find(String id){
        return mesWinModelMapper.find(id);
    }
    public List<MesWinModel> list(MesWinModel mesWinModel){
        return  mesWinModelMapper.list(mesWinModel);
    }
    public List<MesWinModel> findAll(MesWinModel mesWinModel){
        return  mesWinModelMapper.findAll(mesWinModel);
    }

    @Override
    public List<MesWinModel> query(MesWinModel mesWinModel) {
        return mesWinModelMapper.query(mesWinModel);
    }

    @Override
    public MesWinModel findName(MesWinModel mesWinModel) {
        return mesWinModelMapper.findName(mesWinModel);
    }

    @Override
    public int findSum(String winId) {
        return mesWinModelMapper.findSum(winId);
    }
}
