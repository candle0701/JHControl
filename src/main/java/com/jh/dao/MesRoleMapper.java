package com.jh.dao;

import com.jh.entity.MesRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesRoleMapper {
    int deleteByPrimaryKey(String id);

    int insert(MesRole record);

    int insertSelective(MesRole record);

    MesRole selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MesRole record);

    int updateByPrimaryKey(MesRole record);

    List<MesRole> findAllRole(@Param("roleId")String roleId);

    MesRole findByRoleName(String roleName);

    List<MesRole> searchByRoleName(@Param("roleName") String roleName);
}