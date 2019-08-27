package com.jh.service.impl;

import com.jh.dao.MesProcessDetailsMapper;
import com.jh.entity.MesProcessDetails;
import com.jh.service.MesProcessDateilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesProcessDateilsServiceImpl  implements MesProcessDateilsService {

    @Autowired
    private MesProcessDetailsMapper mesProcessDetailsMapper;

    public List<MesProcessDetails> findList(MesProcessDetails mesProcessDetails){
        return mesProcessDetailsMapper.findList(mesProcessDetails);
    }

    public  int update(MesProcessDetails mesProcessDetails){
        return mesProcessDetailsMapper.update(mesProcessDetails);
    }

    public  MesProcessDetails find(MesProcessDetails mesProcessDetails){
        return mesProcessDetailsMapper.find(mesProcessDetails);
    }

    public int insertSelective(MesProcessDetails mesProcessDetails){
        return mesProcessDetailsMapper.insertSelective(mesProcessDetails);

    }

    @Override
    public List<MesProcessDetails> findAll(MesProcessDetails mesProcessDetails) {
        return mesProcessDetailsMapper.findAll(mesProcessDetails);
    }
}
