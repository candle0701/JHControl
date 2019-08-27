package com.jh.service.impl;

import com.jh.dao.MesRoleUsersMapper;
import com.jh.entity.MesRoleUsers;
import com.jh.service.MesRoleUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesRoleUsersServiceImpl implements MesRoleUsersService {

    @Autowired
    private MesRoleUsersMapper mesRoleUsersMapper;

    @Override
    public List<MesRoleUsers> findRoleUsersIdById(String id) {
        return mesRoleUsersMapper.findRoleUsersIdById(id);
    }

    @Override
    public List<MesRoleUsers> findAllRoleUsers() {
        return mesRoleUsersMapper.findAllRoleUsers();
    }

    @Override
    public int insertSelective(MesRoleUsers record) {
        return mesRoleUsersMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(MesRoleUsers record) {
        return mesRoleUsersMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByUserId(MesRoleUsers record) {
        return mesRoleUsersMapper.updateByUserId(record);
    }

    @Override
    public int deleteByUsersId(String usersId) {
        return mesRoleUsersMapper.deleteByUsersId(usersId);
    }

    @Override
    public MesRoleUsers findRoleUsersByUsersId(String usersId) {
        return mesRoleUsersMapper.findRoleUsersByUsersId(usersId);
    }
}
