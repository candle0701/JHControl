package com.jh.service;

import com.jh.entity.MesDepartItem;

import java.util.List;

public interface MesDepartItemService {
    List<MesDepartItem> selectByDepartId(String departId);
    int deleteByUserId(String userId);
    int insertSelective(MesDepartItem record);
}
