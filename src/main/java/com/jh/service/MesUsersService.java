package com.jh.service;

import com.github.pagehelper.PageInfo;
import com.jh.entity.MesDepart;
import com.jh.entity.MesUsers;

import java.util.List;

public interface MesUsersService {

    List<MesUsers> findAllUsers(String userId,String addorEdit);
    List<MesUsers> findAllUsersDepart(String userId);
    int updateByPrimaryKeySelective(MesUsers record);
    MesUsers findByUsername(String username);
    MesUsers selectByPrimaryKey(String id);
    int insertSelective(MesUsers record);
    int deleteByPrimaryKey(String id);
    List<MesUsers> searchByUsername(String nickname);

    List<MesUsers> findUserTask(String dateMonth);

    MesUsers findUser(String man);
}
