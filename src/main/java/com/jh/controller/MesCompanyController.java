package com.jh.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.entity.DataInfo;
import com.jh.entity.MesCompany;
import com.jh.service.MesCompanyService;
import com.jh.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("mesCompany")
public class MesCompanyController {

    private static Logger log = LoggerFactory.getLogger(MesCompanyController.class);

    @Autowired
    private MesCompanyService mesCompanyService;

    @RequestMapping("toMesCompanyList")
    public String toMesCompanyList(Model model,String companyName,
                                 @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                                         int pageNum,
                                 @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                                             int pageSize){
        //引入分页查询，使用PageHelper分页功能
        //在查询之前传入当前页，然后多少记录
        PageHelper.startPage(pageNum,pageSize);
        //startPage后紧跟的这个查询就是分页查询
        List<MesCompany> result = new ArrayList<>();
        if(companyName==null){
            result =  mesCompanyService.findAllCompany(null);
            //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
            PageInfo<MesCompany> pageInfo = new PageInfo<>(result);
            model.addAttribute("mesCompanyList", pageInfo);
            return "mes_company/mes_company_list";
        }else{
            result = mesCompanyService.searchByCompanyName(companyName);
            //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
            PageInfo<MesCompany> pageInfo = new PageInfo<>(result);
            model.addAttribute("mesCompanyList", pageInfo);
            return "mes_company/mes_company_list::table_refresh";
        }
    }

    //打开编辑页面
    @RequestMapping("toMesCompanyEdit")
    public String toMesCompanyEdit(String id,Model model){
        MesCompany mesCompany = mesCompanyService.selectByPrimaryKey(id);
        model.addAttribute("mesCompany", mesCompany);
        return "mes_company/mes_company_edit";
    }

    @ResponseBody
    @RequestMapping("check")
    public DataInfo check(String companyName,String id){
        DataInfo dataInfo = new DataInfo();

        if(CompanyIfExists(companyName,id)){
            dataInfo.setStatus(false);
            dataInfo.setMsg("已存在相同企业名称");
        }else{
            dataInfo.setStatus(true);
            dataInfo.setMsg("已存在相同企业名称");
        }
        return dataInfo;
    }

    public boolean CompanyIfExists(String companyName,String companyId){

        Boolean boo = false;

        //如果已经存在相同名称，无法添加或修改
        List<MesCompany> list = mesCompanyService.findAllCompany(companyId);
        for(MesCompany mes:list){
            if(companyName.equals(mes.getCompanyName())){
                boo = true;
                break;
            }else{
                boo = false;
            }
        }
        return boo;
    }

    //编辑
    @RequestMapping("mesCompanyEdit")
    @ResponseBody
    public DataInfo mesCompanyEdit(String id,MesCompany mesCompany, String companyName,String description){

        DataInfo dataInfo = new DataInfo();

        if(CompanyIfExists(companyName,id)){
            dataInfo.setStatus(false);
            dataInfo.setMsg("已存在相同企业名称");
        }else{
            mesCompany.setCompanyId(id);

            int i = mesCompanyService.updateByPrimaryKeySelective(mesCompany);

            if(i>0){
                dataInfo.setStatus(true);
                dataInfo.setMsg("编辑成功");
            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("编辑失败");
            }
        }
        return dataInfo;
    }

    //打开新增页面
    @RequestMapping("toMesCompanyAdd")
    public String toMesCompanyAdd(){
        return "mes_company/mes_company_add";
    }

    //新增
    @RequestMapping("mesCompanyAdd")
    @ResponseBody
    public DataInfo mesCompanyAdd(String companyId,Model model,MesCompany mesCompany,String companyName,String description){

        DataInfo dataInfo = new DataInfo();
        if(CompanyIfExists(companyName,null)){
            dataInfo.setStatus(false);
            dataInfo.setMsg("已存在相同企业名称");
        }else{
//
            mesCompany.setCompanyId(UUIDUtil.getUUID());

            int i = mesCompanyService.insertSelective(mesCompany);

            if(i>0){
                dataInfo.setStatus(true);
                dataInfo.setMsg("新增成功");
            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("新增失败");
            }
        }

        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("deleteByPrimaryKey")
    public DataInfo deleteByPrimaryKey(String companyId){

        DataInfo dataInfo = new DataInfo();

        int i = mesCompanyService.deleteByPrimaryKey(companyId);

        if(i>0){
            dataInfo.setStatus(true);
            dataInfo.setMsg("删除成功");
        }else{
            dataInfo.setStatus(false);
            dataInfo.setMsg("删除失败");
        }

        return dataInfo;
    }

}
