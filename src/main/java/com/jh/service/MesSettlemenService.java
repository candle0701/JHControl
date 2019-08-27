package com.jh.service;

import com.jh.entity.MesSettlemen;

import java.util.List;

public interface MesSettlemenService {
    List<MesSettlemen> findList(MesSettlemen mesSettlemen);

    int insertSelective(MesSettlemen mesSettlemen);
}
