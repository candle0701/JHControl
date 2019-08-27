package com.jh.dao;

import com.jh.entity.MesMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesMessageMapper {


    int insertSelective(MesMessage record);

    MesMessage selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MesMessage record);

    int setRead(@Param("id") String id,@Param("reciverId") String reciverId);

    List<MesMessage> selectAll(@Param("isRead")String isRead,@Param("reciverId") String reciverId);

}