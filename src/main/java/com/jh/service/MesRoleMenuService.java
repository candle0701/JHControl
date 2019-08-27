package com.jh.service;

import com.jh.entity.MesRoleMenu;

public interface MesRoleMenuService {
    int updateByPrimaryKeySelective(MesRoleMenu record);
    int updateByroleIdAndMenuId(MesRoleMenu record);
    int insertSelective(MesRoleMenu record);
    int deleteByPrimaryKey(String id);
    int deleteByRoleId(String roleId);
}
