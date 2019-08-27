package com.jh.service;

import com.jh.entity.MesMenuButton;

import java.util.List;

public interface MesMenuButtonService {
    int updateByPrimaryKeySelective(MesMenuButton record);
    List<MesMenuButton> getAllButton();
    List<MesMenuButton> getButtonByRoleIdAndMenuId(String roleId,String menuId);
    int insertSelective(MesMenuButton record);
    int deleteByPrimaryKey(String id);
    int deleteByRoleId(String roleId);
}
