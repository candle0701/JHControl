package com.jh.service.impl;


import com.jh.dao.MesDictionariesMapper;
import com.jh.entity.MesDictionaries;
import com.jh.service.MesDictionariesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesDictionariesServiceImpl implements MesDictionariesService {

    @Autowired
    private MesDictionariesMapper mesDictionariesMapper;

      public  List<MesDictionaries> findList(MesDictionaries mesDictionaries){
          return  mesDictionariesMapper.findList(mesDictionaries );
      }

    @Override
    public List<MesDictionaries> getWinModelByCode(String code) {
        return mesDictionariesMapper.getWinModelByCode(code);
    }


    public  MesDictionaries get(String dicId){
        return  mesDictionariesMapper.get(dicId );
    }

    @Override
    public List<MesDictionaries> getWinModel() {
        return mesDictionariesMapper.getWinModel();
    }


}
