package com.jh.service.impl;

import com.jh.dao.MesRoleMapper;
import com.jh.entity.MesRole;
import com.jh.service.MesRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesRoleServiceImpl implements MesRoleService {

    @Autowired
    private MesRoleMapper mesRoleMapper;

    @Override
    public List<MesRole> findAllRole(String roleId) {
        return mesRoleMapper.findAllRole(roleId);
    }

    @Override
    public int updateByPrimaryKeySelective(MesRole record) {
        return mesRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public MesRole findByRoleName(String roleName) {
        return mesRoleMapper.findByRoleName(roleName);
    }

    @Override
    public MesRole selectByPrimaryKey(String id) {
        return mesRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(MesRole record) {
        return mesRoleMapper.insertSelective(record);
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return mesRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<MesRole> searchByRoleName(String roleName) {
        return mesRoleMapper.searchByRoleName(roleName);
    }
}
