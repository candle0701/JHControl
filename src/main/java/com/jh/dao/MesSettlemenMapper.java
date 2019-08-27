package com.jh.dao;

import com.jh.entity.MesSettlemen;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesSettlemenMapper {
    int insert(MesSettlemen record);

    int insertSelective(MesSettlemen record);

    List<MesSettlemen> findList(MesSettlemen mesSettlemen);
}