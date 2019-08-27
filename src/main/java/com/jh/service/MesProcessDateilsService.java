package com.jh.service;

import com.jh.entity.MesProcessDetails;

import java.util.List;

public interface MesProcessDateilsService {

    List<MesProcessDetails> findList(MesProcessDetails mesProcessDetails);

    int update(MesProcessDetails mesProcessDetails);

    MesProcessDetails find(MesProcessDetails mesProcessDetails);

    int insertSelective(MesProcessDetails mesProcessDetails);

    List<MesProcessDetails> findAll(MesProcessDetails mesProcessDetails);
}
