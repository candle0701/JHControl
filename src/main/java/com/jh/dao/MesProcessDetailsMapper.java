package com.jh.dao;

import com.jh.entity.MesProcessDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesProcessDetailsMapper {
    int insert(MesProcessDetails record);

    int insertSelective(MesProcessDetails record);

    List<MesProcessDetails> findList(MesProcessDetails record);

    int update(MesProcessDetails mesProcessDetails);


    MesProcessDetails find(MesProcessDetails mesProcessDetails);

    List<MesProcessDetails> findAll(MesProcessDetails mesProcessDetails);
}