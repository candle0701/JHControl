package com.jh.dao;

import com.jh.entity.MesDepartItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesDepartItemMapper {
    int deleteByPrimaryKey(String id);

    int deleteByUserId(@Param("userId")String userId);

    int insert(MesDepartItem record);

    int insertSelective(MesDepartItem record);

    MesDepartItem selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MesDepartItem record);

    int updateByPrimaryKey(MesDepartItem record);

    List<MesDepartItem> selectByDepartId(@Param("departId") String departId);
}