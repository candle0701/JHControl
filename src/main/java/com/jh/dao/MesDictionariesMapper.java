package com.jh.dao;

import com.jh.entity.MesDictionaries;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MesDictionariesMapper {
    int insert(MesDictionaries record);

    int insertSelective(MesDictionaries record);

    MesDictionaries get(String dicId);

    List<MesDictionaries> findList(MesDictionaries mesDictionaries);

    List<MesDictionaries> getWinModelByCode(@Param("code")String code);
    List<MesDictionaries> getWinModel();
}