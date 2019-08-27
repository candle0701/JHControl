package com.jh.service.impl;

import com.jh.dao.MesMenuMapper;
import com.jh.entity.MesMenu;
import com.jh.service.MesMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesMenuServiceImpl implements MesMenuService {
    @Autowired
    private MesMenuMapper mesMenuMapper;

    public List<MesMenu> findAllMenuByRoleId(String id){
        return mesMenuMapper.findAllMenuByRoleId(id);
    }

    @Override
    public List<MesMenu> findPermissionMenuByRoleId(String id) {
        return mesMenuMapper.findPermissionMenuByRoleId(id);
    }

    @Override
    public List<MesMenu> findMenuParentIdIsNotNull(String id) {

        return mesMenuMapper.findMenuParentIdIsNotNull(id);
    }

    @Override
    public List<MesMenu> getAllMenu() {
        return mesMenuMapper.getAllMenu();
    }

    @Override
    public List<MesMenu> getMenuNameWx(String roleId, String usersId) {
        return mesMenuMapper.getMenuNameWx(roleId,usersId);
    }

    @Override
    public MesMenu getParentMenu(String id) {
        return mesMenuMapper.getParentMenu(id);
    }

}
