package com.jh.dao;

import com.jh.entity.MesRoleUsers;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesRoleUsersMapper {
    int deleteByPrimaryKey(String id);

    int deleteByUsersId(@Param("usersId") String usersId);

    int insert(MesRoleUsers record);

    int insertSelective(MesRoleUsers record);

    MesRoleUsers selectByPrimaryKey(String id);
    MesRoleUsers findRoleUsersByUsersId(@Param("usersId")String usersId);

    int updateByPrimaryKeySelective(MesRoleUsers record);

    int updateByUserId(MesRoleUsers record);

    int updateByPrimaryKey(MesRoleUsers record);

    List<MesRoleUsers> findRoleUsersIdById(String id);

    List<MesRoleUsers> findAllRoleUsers();
}