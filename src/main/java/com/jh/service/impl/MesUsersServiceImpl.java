package com.jh.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.dao.MesUsersMapper;
import com.jh.entity.MesUsers;
import com.jh.service.MesUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesUsersServiceImpl implements MesUsersService {

    @Autowired
    private MesUsersMapper mesUsersMapper;

    @Override
    public List<MesUsers> findAllUsers(String userId,String addorEdit) {
        return mesUsersMapper.findAllUsers(userId,addorEdit);
    }
    public List<MesUsers> findAllUsersDepart(String userId) {
        return mesUsersMapper.findAllUsersDepart(userId);
    }

    @Override
    public int updateByPrimaryKeySelective(MesUsers record) {
        return mesUsersMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public MesUsers findByUsername(String username) {
        return mesUsersMapper.findByUsername(username);
    }

    @Override
    public MesUsers selectByPrimaryKey(String id) {
        return mesUsersMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(MesUsers record) {
        return mesUsersMapper.insertSelective(record);
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return mesUsersMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<MesUsers> searchByUsername(String nickname) {
        return mesUsersMapper.searchByUsername(nickname);
    }

    @Override
    public List<MesUsers> findUserTask(String dateMonth) {
        return mesUsersMapper.findUserTask(dateMonth);
    }

    @Override
    public MesUsers findUser(String man) {
        return mesUsersMapper.findUser(man);
    }
}
