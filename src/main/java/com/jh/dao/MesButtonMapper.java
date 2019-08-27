package com.jh.dao;

import com.jh.entity.MesButton;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesButtonMapper {
    int deleteByPrimaryKey(String id);

    int insert(MesButton record);

    int insertSelective(MesButton record);

    MesButton selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MesButton record);

    int updateByPrimaryKey(MesButton record);

    List<MesButton> getAllButton(@Param("menuId")String menuId,@Param("roleId")String roleId);
}