package com.jh.dao;

import com.jh.entity.MesWinModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesWinModelMapper {
    int insert(MesWinModel record);

    int insertSelective(MesWinModel record);


    //查询所有数据  关联字典表
    List<MesWinModel> findList(MesWinModel mesWinModel);

    int del(MesWinModel mesWinModel);
    MesWinModel find(String id);

    List<MesWinModel> list(MesWinModel mesWinModel);
    List<MesWinModel> findAll(MesWinModel mesWinModel);
    MesWinModel findName(MesWinModel mesWinModel);
    List<MesWinModel> query(MesWinModel mesWinModel);
    int findSum(String winId);
}