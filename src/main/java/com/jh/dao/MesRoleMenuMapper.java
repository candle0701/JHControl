package com.jh.dao;

import com.jh.entity.MesRoleMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MesRoleMenuMapper {
    int deleteByPrimaryKey(String id);

    int insert(MesRoleMenu record);

    int insertSelective(MesRoleMenu record);

    MesRoleMenu selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MesRoleMenu record);
    int updateByroleIdAndMenuId(MesRoleMenu record);

    int updateByPrimaryKey(MesRoleMenu record);

    int deleteByRoleId(@Param("roleId")String roleId);
}