package com.jh.service;

import com.jh.entity.MesDictionaries;

import java.util.List;

public interface MesDictionariesService {

   List<MesDictionaries> findList(MesDictionaries mesDictionaries);
   List<MesDictionaries> getWinModelByCode(String code);
   MesDictionaries get(String dicId);
   List<MesDictionaries> getWinModel();
}
