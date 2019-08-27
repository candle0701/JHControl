package com.jh.service.impl;

import com.jh.dao.MesDepartItemMapper;
import com.jh.entity.MesDepartItem;
import com.jh.service.MesDepartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesDepartItemServiceImpl implements MesDepartItemService {

    @Autowired
    private MesDepartItemMapper mesDepartItemMapper;

    @Override
    public List<MesDepartItem> selectByDepartId(String departId) {
        return mesDepartItemMapper.selectByDepartId(departId);
    }

    @Override
    public int deleteByUserId(String userId) {
        return mesDepartItemMapper.deleteByUserId(userId);
    }

    @Override
    public int insertSelective(MesDepartItem record) {
        return mesDepartItemMapper.insertSelective(record);
    }
}
