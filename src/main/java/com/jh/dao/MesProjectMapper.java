package com.jh.dao;

import com.jh.entity.MesProject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MesProjectMapper {
    int deleteByPrimaryKey(String projectId);

    int insert(MesProject record);

    int insertSelective(MesProject record);

    MesProject selectByPrimaryKey(MesProject mesProject);

    int updateByPrimaryKeySelective(MesProject record);

    int updateByPrimaryKey(MesProject record);

    List<MesProject> findAllProject( MesProject mesProject);
    List<MesProject> findList(@Param("mesProject") MesProject mesProject, @Param("page")int page,@Param("limit") int limit);
    List<MesProject> find(MesProject mesProject);
    List<MesProject>  findUp(MesProject mesProject);
    List<MesProject> selAll();

    MesProject sel(MesProject mesProject);

    int del(MesProject mesProject);
}