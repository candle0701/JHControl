package com.jh.service.impl;

import com.jh.dao.MesMenuButtonMapper;
import com.jh.entity.MesMenuButton;
import com.jh.service.MesMenuButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesMenuButtonServiceImpl implements MesMenuButtonService {

    @Autowired
    private MesMenuButtonMapper mesMenuButtonMapper;

    @Override
    public int updateByPrimaryKeySelective(MesMenuButton record) {
        return mesMenuButtonMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<MesMenuButton> getAllButton() {
        return mesMenuButtonMapper.getAllButton();
    }

    @Override
    public List<MesMenuButton> getButtonByRoleIdAndMenuId(String roleId, String menuId) {
        return mesMenuButtonMapper.getButtonByRoleIdAndMenuId(roleId,menuId);
    }

    @Override
    public int insertSelective(MesMenuButton record) {
        return mesMenuButtonMapper.insertSelective(record);
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return mesMenuButtonMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByRoleId(String roleId) {
        return mesMenuButtonMapper.deleteByRoleId(roleId);
    }
}
