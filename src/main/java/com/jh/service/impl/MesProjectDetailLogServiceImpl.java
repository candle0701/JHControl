package com.jh.service.impl;

import com.jh.dao.MesProjectDetailLogMapper;
import com.jh.entity.MesProjectDetailLog;
import com.jh.service.MesProjectDetailLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesProjectDetailLogServiceImpl implements MesProjectDetailLogService {

    @Autowired
    private MesProjectDetailLogMapper mesProjectDetailLogMapper;

    @Override
    public List<MesProjectDetailLog> list(MesProjectDetailLog mesProjectDetailLog) {
        return mesProjectDetailLogMapper.list(mesProjectDetailLog);
    }

    @Override
    public int insertSelective(MesProjectDetailLog mesProjectDetailLog) {
        return mesProjectDetailLogMapper.insertSelective(mesProjectDetailLog);
    }
}
