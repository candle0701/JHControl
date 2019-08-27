package com.jh.service;

import com.jh.entity.MesMessage;

import java.util.List;

public interface MesMessageService {
    int setRead( String id,String reciverId);

    List<MesMessage> selectAll(String isRead,String reciverId);

    int insertSelective(MesMessage record);

    MesMessage selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MesMessage record);
}
