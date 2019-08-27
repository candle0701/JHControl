package com.jh.service;

import com.jh.entity.MesCompany;

import java.util.List;

public interface MesCompanyService {

    List<MesCompany> findAllCompany(String companyId);
    int updateByPrimaryKeySelective(MesCompany record);
    MesCompany findByCompanyName(String companyName);
    MesCompany selectByPrimaryKey(String companyId);
    int insertSelective(MesCompany record);
    int deleteByPrimaryKey(String companyId);
    List<MesCompany> searchByCompanyName(String companyName);
}
