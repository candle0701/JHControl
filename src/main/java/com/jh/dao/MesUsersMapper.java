package com.jh.dao;

import com.jh.entity.MesUsers;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesUsersMapper {
    int deleteByPrimaryKey(String id);

    int insert(MesUsers record);

    int insertSelective(MesUsers record);

    MesUsers selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MesUsers record);

    int updateByPrimaryKey(MesUsers record);

    List<MesUsers> findAllUsers(@Param("userId")String userId,@Param("addorEdit")String addorEdit);
    List<MesUsers> findAllUsersDepart(@Param("userId")String userId);

    MesUsers findByUsername(String username);

    List<MesUsers> searchByUsername(@Param("nickname") String nickname);

    List<MesUsers> findUserTask(@Param("dateMonth")String dateMonth);

    MesUsers findUser(String man);
}