package com.jh.dao;

import com.jh.entity.MesWinModelCraft;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesWinModelCraftMapper {
    int insert(MesWinModelCraft record);

    int insertSelective(MesWinModelCraft record);


    List<MesWinModelCraft> findList(MesWinModelCraft mesWinModelCraft);

    MesWinModelCraft getNums(@Param("winName")String winName,@Param("name")String name);

    int update(MesWinModelCraft mesWinModelCraft);


    List<MesWinModelCraft> list(MesWinModelCraft mesWinModelCraft);


    MesWinModelCraft find(String id);

    List<MesWinModelCraft> findAll(MesWinModelCraft mesWinModelCraft);

    List<MesWinModelCraft> all(MesWinModelCraft mesWinModelCraft);

    List<MesWinModelCraft> sel(String code);

    void del(String code);

    MesWinModelCraft findMilestone(MesWinModelCraft winModelCraft);
}