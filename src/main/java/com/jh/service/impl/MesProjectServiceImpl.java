package com.jh.service.impl;

import com.jh.dao.MesProjectMapper;
import com.jh.entity.MesProject;
import com.jh.service.MesProjectService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class MesProjectServiceImpl  implements MesProjectService {

    @Autowired
    private MesProjectMapper mesProjectMapper;

    @Override
    public  List<MesProject> findAllProject(MesProject mesProject){
        return     mesProjectMapper.findAllProject(mesProject);
    }

    public  List<MesProject> findList(MesProject mesProject,int page,int limit){
        return     mesProjectMapper.findList(mesProject,page,limit);
    }
    @Override
    public  MesProject selectByPrimaryKey(MesProject mesProject){
        return     mesProjectMapper.selectByPrimaryKey(mesProject);
    }

    @Override
    public  int insertSelective(MesProject mesProject){
        return     mesProjectMapper.insertSelective(mesProject);
    }

    @Override
    public  int updateByPrimaryKeySelective(MesProject mesProject){
        return     mesProjectMapper.updateByPrimaryKeySelective(mesProject);
    }

    @Override
    public  int updateByPrimaryKey(MesProject mesProject){
        return     mesProjectMapper.updateByPrimaryKey(mesProject);
    }

    public List<MesProject> find(MesProject mesProject){
        return     mesProjectMapper.find(mesProject);
    }


    public List<MesProject>  findUp(MesProject mesProject){
        return     mesProjectMapper.findUp(mesProject);
    }

    @Override
    public List<MesProject> selAll() {
        return   mesProjectMapper.selAll();
    }

    @Override
    public MesProject sel(MesProject mesProject) {
        return   mesProjectMapper.sel(mesProject);
    }

    @Override
    public int del(MesProject mesProject) {
        return   mesProjectMapper.del(mesProject);
    }


}
