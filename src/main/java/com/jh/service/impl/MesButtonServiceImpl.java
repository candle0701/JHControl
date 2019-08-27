package com.jh.service.impl;

import com.jh.dao.MesButtonMapper;
import com.jh.entity.MesButton;
import com.jh.service.MesButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesButtonServiceImpl implements MesButtonService {

    @Autowired
    private MesButtonMapper mesButtonMapper;

    @Override
    public List<MesButton> getAllButton(String menuId,String roleId) {
        return mesButtonMapper.getAllButton(menuId,roleId);
    }
}
