package com.jh.service.impl;

import com.jh.dao.MesCompanyMapper;
import com.jh.entity.MesCompany;
import com.jh.service.MesCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesCompanyServiceImpl implements MesCompanyService {

    @Autowired
    private MesCompanyMapper mesCompanyMapper;

    @Override
    public List<MesCompany> findAllCompany(String companyId) {
        return mesCompanyMapper.findAllCompany(companyId);
    }

    @Override
    public int updateByPrimaryKeySelective(MesCompany record) {
        return mesCompanyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public MesCompany findByCompanyName(String companyName) {
        return mesCompanyMapper.findByCompanyName(companyName);
    }

    @Override
    public MesCompany selectByPrimaryKey(String companyId) {
        return mesCompanyMapper.selectByPrimaryKey(companyId);
    }

    @Override
    public int insertSelective(MesCompany record) {
        return mesCompanyMapper.insertSelective(record);
    }

    @Override
    public int deleteByPrimaryKey(String companyId) {
        return mesCompanyMapper.deleteByPrimaryKey(companyId);
    }

    @Override
    public List<MesCompany> searchByCompanyName(String companyName) {
        return mesCompanyMapper.searchByCompanyName(companyName);
    }
}
