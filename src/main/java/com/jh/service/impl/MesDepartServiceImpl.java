package com.jh.service.impl;

import com.jh.dao.MesDepartMapper;
import com.jh.entity.MesDepart;
import com.jh.service.MesDepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesDepartServiceImpl implements MesDepartService {

    @Autowired
    private MesDepartMapper mesDepartMapper;

    @Override
    public List<MesDepart> findAllDepart(String id) {
        return mesDepartMapper.findAllDepart(id);
    }

    @Override
    public MesDepart selectByPrimaryKey(String id) {
        return mesDepartMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(MesDepart record) {
        return mesDepartMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int insert(MesDepart record) {
        return mesDepartMapper.insert(record);
    }

    @Override
    public int insertSelective(MesDepart record) {
        return mesDepartMapper.insertSelective(record);
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return mesDepartMapper.deleteByPrimaryKey(id);
    }

    @Override
    public MesDepart selectByParentId(String parentid) {
        return mesDepartMapper.selectByParentId(parentid);
    }

    @Override
    public MesDepart getMaxSort() {
        return mesDepartMapper.getMaxSort();
    }
}
