package com.jh.dao;

import com.jh.entity.MesDepart;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesDepartMapper {
    int deleteByPrimaryKey(String id);

    int insert(MesDepart record);

    int insertSelective(MesDepart record);

    MesDepart selectByPrimaryKey(String id);
    MesDepart getMaxSort();

    MesDepart selectByParentId(String parentid);

    int updateByPrimaryKeySelective(MesDepart record);

    int updateByPrimaryKey(MesDepart record);

    List<MesDepart> findAllDepart(@Param("id")String id);


}