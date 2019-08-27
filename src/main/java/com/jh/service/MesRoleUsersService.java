package com.jh.service;

import com.jh.entity.MesRoleUsers;

import java.util.List;

public interface MesRoleUsersService {
    List<MesRoleUsers> findRoleUsersIdById(String id);
    List<MesRoleUsers> findAllRoleUsers();
    int insertSelective(MesRoleUsers record);
    int updateByPrimaryKeySelective(MesRoleUsers record);
    int updateByUserId(MesRoleUsers record);
    int deleteByUsersId(String usersId);
    MesRoleUsers findRoleUsersByUsersId(String usersId);
}
