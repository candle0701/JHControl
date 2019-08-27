package com.jh.service;

import com.jh.entity.MesWinModelCraft;

import java.util.List;

public interface MesWinModelCraftService {

    int insertSelective(MesWinModelCraft mesWinModelCraft);

    MesWinModelCraft getNums(String winName,String name);
    //查询所有信息  关联窗型表
    List<MesWinModelCraft> findList(MesWinModelCraft mesWinModelCraft);

    List<MesWinModelCraft> list(MesWinModelCraft mesWinModelCraft);

    int update(MesWinModelCraft mesWinModelCraft);

     MesWinModelCraft find(String id);



    List<MesWinModelCraft> findAll(MesWinModelCraft mesWinModelCraft);

    List<MesWinModelCraft> all(MesWinModelCraft mesWinModelCraft);

    List<MesWinModelCraft> sel(String code);

    void del(String code);

    MesWinModelCraft findMilestone(MesWinModelCraft winModelCraft);
}
