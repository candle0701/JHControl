package com.jh.service;

import com.jh.entity.MesDepart;

import java.util.List;

public interface MesDepartService {
    List<MesDepart> findAllDepart(String id);
    MesDepart selectByPrimaryKey(String id);
    int updateByPrimaryKeySelective(MesDepart record);
    int insert(MesDepart record);
    int insertSelective(MesDepart record);
    int deleteByPrimaryKey(String id);
    MesDepart selectByParentId(String parentid);
    MesDepart getMaxSort();
}
