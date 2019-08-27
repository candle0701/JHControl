package com.jh.service;

import com.jh.entity.MesWinModel;

import java.util.List;

public interface MesWinModelService {


    List<MesWinModel> findList(MesWinModel mesWinModel);

    int del(MesWinModel mesWinModel);
    int insertSelective(MesWinModel mesWinModel);


    MesWinModel find(String id);

    List<MesWinModel> list(MesWinModel mesWinModel);
    List<MesWinModel> findAll(MesWinModel mesWinModel);

    MesWinModel findName(MesWinModel mesWinModel);

    List<MesWinModel> query(MesWinModel mesWinModel);

    int findSum(String winId);
}
