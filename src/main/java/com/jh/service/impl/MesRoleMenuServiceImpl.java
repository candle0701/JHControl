package com.jh.service.impl;

import com.jh.dao.MesRoleMenuMapper;
import com.jh.entity.MesRoleMenu;
import com.jh.service.MesRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MesRoleMenuServiceImpl implements MesRoleMenuService {

    @Autowired
    private MesRoleMenuMapper mesRoleMenuMapper;

    @Override
    public int updateByPrimaryKeySelective(MesRoleMenu record) {
        return mesRoleMenuMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByroleIdAndMenuId(MesRoleMenu record) {
        return mesRoleMenuMapper.updateByroleIdAndMenuId(record);
    }

    @Override
    public int insertSelective(MesRoleMenu record) {
        return mesRoleMenuMapper.insertSelective(record);
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return mesRoleMenuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByRoleId(String roleId) {
        return mesRoleMenuMapper.deleteByRoleId(roleId);
    }
}
