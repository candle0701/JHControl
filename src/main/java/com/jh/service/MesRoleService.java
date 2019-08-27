package com.jh.service;

import com.jh.entity.MesRole;

import java.util.List;

public interface MesRoleService {

    List<MesRole> findAllRole(String roleId);
    int updateByPrimaryKeySelective(MesRole record);
    MesRole findByRoleName(String roleName);
    MesRole selectByPrimaryKey(String id);
    int insertSelective(MesRole record);
    int deleteByPrimaryKey(String id);
    List<MesRole> searchByRoleName(String roleName);

}
