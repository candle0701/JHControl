package com.jh.dao;

import com.jh.entity.MesMenuButton;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesMenuButtonMapper {
    int deleteByPrimaryKey(String id);

    int insert(MesMenuButton record);

    int insertSelective(MesMenuButton record);

    MesMenuButton selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MesMenuButton record);

    int updateByPrimaryKey(MesMenuButton record);

    List<MesMenuButton> getAllButton();

    List<MesMenuButton> getButtonByRoleIdAndMenuId(@Param("roleId")String roleId,@Param("menuId")String menuId);

    int deleteByRoleId(@Param("roleId")String roleId);
}