package com.jh.service.impl;

import com.jh.dao.MesMessageMapper;
import com.jh.entity.MesMessage;
import com.jh.service.MesMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesMessageServiceImpl implements MesMessageService {

    @Autowired
    private MesMessageMapper mesMessageMapper;

    @Override
    public int setRead(String id,String reciverId) {
        return mesMessageMapper.setRead(id,reciverId);
    }

    @Override
    public List<MesMessage> selectAll(String isRead,String reciverId) {
        return mesMessageMapper.selectAll(isRead,reciverId);
    }

    @Override
    public int insertSelective(MesMessage record) {
        return mesMessageMapper.insertSelective(record);
    }

    @Override
    public MesMessage selectByPrimaryKey(String id) {
        return mesMessageMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(MesMessage record) {
        return mesMessageMapper.updateByPrimaryKeySelective(record);
    }
}
