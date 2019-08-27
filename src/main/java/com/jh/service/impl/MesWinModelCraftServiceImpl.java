package com.jh.service.impl;

import com.jh.dao.MesWinModelCraftMapper;
import com.jh.entity.MesWinModelCraft;
import com.jh.service.MesWinModelCraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesWinModelCraftServiceImpl implements MesWinModelCraftService {


    @Autowired
    private MesWinModelCraftMapper mesWinModelCraftMapper;


    public List<MesWinModelCraft> findList(MesWinModelCraft mesWinModelCraft){
        return mesWinModelCraftMapper.findList(mesWinModelCraft);
    }

    public int insertSelective(MesWinModelCraft mesWinModelCraft){
        return mesWinModelCraftMapper.insertSelective(mesWinModelCraft);
    }

    @Override
    public MesWinModelCraft getNums(String winName, String name) {
        return mesWinModelCraftMapper.getNums(winName,name);
    }

    public List<MesWinModelCraft> list(MesWinModelCraft mesWinModelCraft){
        return mesWinModelCraftMapper.list(mesWinModelCraft);
    }

    public int update(MesWinModelCraft mesWinModelCraft){
        return mesWinModelCraftMapper.update(mesWinModelCraft);
    }


    @Override
    public List<MesWinModelCraft> findAll(MesWinModelCraft mesWinModelCraft) {
        return mesWinModelCraftMapper.findAll(mesWinModelCraft);
    }

    @Override
    public List<MesWinModelCraft> all(MesWinModelCraft mesWinModelCraft) {
        return mesWinModelCraftMapper.all(mesWinModelCraft);
    }

    @Override
    public void del(String code) {
        mesWinModelCraftMapper.del(code);
    }

    @Override
    public MesWinModelCraft findMilestone(MesWinModelCraft winModelCraft) {
        return mesWinModelCraftMapper.findMilestone(winModelCraft);
    }

    @Override
    public List<MesWinModelCraft> sel(String code) {
        return mesWinModelCraftMapper.sel(code);
    }

    public  MesWinModelCraft find(String id){
        return mesWinModelCraftMapper.find(id);
    }
}
