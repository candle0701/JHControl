package com.jh.service;

import com.jh.entity.MesMenu;

import java.util.List;

public interface MesMenuService {
    List<MesMenu> findAllMenuByRoleId(String id);
    List<MesMenu> findPermissionMenuByRoleId(String id);
    List<MesMenu> findMenuParentIdIsNotNull(String id);
    List<MesMenu> getAllMenu();
    List<MesMenu> getMenuNameWx(String roleId,String usersId);
    MesMenu getParentMenu(String id);
}
