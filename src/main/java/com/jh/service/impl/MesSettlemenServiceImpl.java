package com.jh.service.impl;

import com.jh.dao.MesSettlemenMapper;
import com.jh.entity.MesSettlemen;
import com.jh.service.MesSettlemenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesSettlemenServiceImpl implements MesSettlemenService {
    @Autowired
    private MesSettlemenMapper mesSettlemenMapper;

    @Override
    public List<MesSettlemen> findList(MesSettlemen mesSettlemen) {
        return mesSettlemenMapper.findList(mesSettlemen);
    }

    @Override
    public int insertSelective(MesSettlemen mesSettlemen) {
        return mesSettlemenMapper.insertSelective(mesSettlemen);
    }
}
