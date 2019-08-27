package com.jh.service;


import com.jh.entity.MesProject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MesProjectService {

     int insertSelective(MesProject mesProject) ;

    List<MesProject> findAllProject(MesProject mesProject);

    List<MesProject> findList(MesProject mesProject,int page,int limit);

    MesProject selectByPrimaryKey(MesProject mesProject);

    int updateByPrimaryKey(MesProject project);

    int updateByPrimaryKeySelective(MesProject project);
    List<MesProject> find(MesProject mesProject);


    List<MesProject>  findUp(MesProject mesProject);


    List<MesProject> selAll();

    MesProject sel(MesProject mesProject);

    int del(MesProject mesProject);
}
