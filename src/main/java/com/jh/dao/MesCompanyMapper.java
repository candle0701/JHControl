package com.jh.dao;

import com.jh.entity.MesCompany;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesCompanyMapper {
    int deleteByPrimaryKey(String companyId);

    int insert(MesCompany record);

    int insertSelective(MesCompany record);

    MesCompany selectByPrimaryKey(String companyId);

    int updateByPrimaryKeySelective(MesCompany record);

    int updateByPrimaryKey(MesCompany record);

    List<MesCompany> findAllCompany(@Param("companyId")String companyId);

    MesCompany findByCompanyName(String companyName);

    List<MesCompany> searchByCompanyName(@Param("companyName") String companyName);
}