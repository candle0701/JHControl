package com.jh.controller;

import com.jh.entity.*;
import com.jh.service.MesDepartItemService;
import com.jh.service.MesDepartService;
import com.jh.service.MesDictionariesService;
import com.jh.service.MesUsersService;
import com.jh.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("mesDepart")
public class MesDepartController {

    @Autowired
    private MesDepartService mesDepartService;

    @Autowired
    private MesUsersService mesUsersService;

    @Autowired
    private MesDepartItemService mesDepartItemService;

    @Autowired
    private MesDictionariesService mesDictionariesService;

    //部门列表
    @RequestMapping("toMesDepartList")
    public String toMesDepartList(Model model) {
        List<MesDepart> result = null;
        try {
//            result = this.contextLoads();
            result = mesDepartService.findAllDepart(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("mesDepartList", result);
        return "mes_depart/mes_depart_list";
    }

    //===========================部门添加人员begin===========================================
    //打开部门添加人员页面
    @RequestMapping("toMesDepartUsersAdd")
    public String toMesDepartUsersAdd(String id,Model model){

        List<MesUsers> mesUsersList = mesUsersService.findAllUsersDepart(null);

        List<MesDepartItem> itemList = mesDepartItemService.selectByDepartId(id);

        for(MesUsers mu:mesUsersList){
            mu.setDepartStatus("0");
            for(MesDepartItem mdi:itemList){
                if(mu.getId().equals(mdi.getUserId())){
                    mu.setDepartStatus("1");
                    break;
                }else{
                    mu.setDepartStatus("0");
                }
            }
        }
        model.addAttribute("departId", id);
        model.addAttribute("mesUsersList", mesUsersList);

        //获取窗型类型
        MesDictionaries mesDictionaries = new MesDictionaries();
        mesDictionaries.setType("WIN_PROCESS3");
        model.addAttribute("mesDictionariesList",mesDictionariesService.findList(mesDictionaries));


        return "mes_depart/mes_depart_users_add";
    }
    //部门添加人员提交
    @ResponseBody
    @RequestMapping("mesDepartUsersAdd")
    public void mesDepartUsersAdd(String departId,String usersId,String status){
        //先删除再添加
        mesDepartItemService.deleteByUserId(usersId);

        if("1".equals(status)){
            MesDepartItem mdi = new MesDepartItem();
            mdi.setId(UUIDUtil.getUUID());
            mdi.setDepartId(departId);
            mdi.setUserId(usersId);
            mesDepartItemService.insertSelective(mdi);
        }

    }
    //===========================部门添加人员end===========================================

    //打开部门编辑页面
    @RequestMapping("toMesDepartEdit")
    public String editMesDepart(String id,Model model){

        MesDepart mesDepart = mesDepartService.selectByPrimaryKey(id);
        if(!"1".equals(mesDepart.getValid())){
            String currentName = mesDepart.getName().substring(0,mesDepart.getName().indexOf("-"));
            String name = mesDepart.getName().substring(mesDepart.getName().indexOf("-")+1,mesDepart.getName().length());

            mesDepart.setCurrentName(currentName);
            mesDepart.setName(name);
        }

        model.addAttribute("mesDepart", mesDepart);

        //获取窗型类型
        MesDictionaries mesDictionaries = new MesDictionaries();
        mesDictionaries.setType("WIN_PROCESS3");
        List<MesDictionaries> mesDictionariesList = mesDictionariesService.findList(mesDictionaries);
        model.addAttribute("mesDictionariesList",mesDictionariesList);

        return "mes_depart/mes_depart_edit";
    }

    //部门编辑
    @RequestMapping("mesDepartEdit")
    @ResponseBody
    public DataInfo departEdit(String id,String name,String processGroup){

        DataInfo dataInfo = new DataInfo();

        if(departIfExists(name,id)){
            dataInfo.setStatus(false);
            dataInfo.setMsg("部门未修改或已存在相同部门");
        }else{
            MesDepart mesDepart = new MesDepart();
            mesDepart.setId(id);
            mesDepart.setProcessGroup(processGroup);
            mesDepart.setName(name);

            int i = mesDepartService.updateByPrimaryKeySelective(mesDepart);

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

    //打开部门新增页面
    @RequestMapping("toMesDepartAdd")
    public String addMesDepart(String id,Model model,String name){

        //获取窗型类型
        MesDictionaries mesDictionaries = new MesDictionaries();
        mesDictionaries.setType("WIN_PROCESS3");
        model.addAttribute("mesDictionariesList",mesDictionariesService.findList(mesDictionaries));

        model.addAttribute("id", id);
        if(StringUtils.isNotEmpty(id)){
            MesDepart mesDepart = mesDepartService.selectByPrimaryKey(id);
            model.addAttribute("name", mesDepart.getName());
            model.addAttribute("checkorder", mesDepart.getCheckorder());
        }

        return "mes_depart/mes_depart_add";
    }

    public boolean departIfExists(String name,String id){

        Boolean boo = true;

        //如果已经存在相同部门名称，无法添加或修改
        List<MesDepart> list = mesDepartService.findAllDepart(id);
        if(list.size()>0){
            for(MesDepart mes:list){
                if(name.equals(mes.getName())){
                    boo = true;
                    break;
                }else{
                    boo = false;
                }
            }
        }else{
            boo = false;
        }

        return boo;
    }

    //部门新增
    @RequestMapping("mesDepartAdd")
    @ResponseBody
    public DataInfo mesDepartAdd(String id,Model model,String name,String checkorder,String processGroup){

        DataInfo dataInfo = new DataInfo();
        if(departIfExists(name,null)){
            dataInfo.setStatus(false);
            dataInfo.setMsg("已存在相同部门");
        }else{
            MesDepart mesDepart = new MesDepart();

            int i = 0;

            if(StringUtils.isEmpty(id)){
                //新增主部门
                mesDepart.setId(UUIDUtil.getUUID());
                mesDepart.setName(name);
                mesDepart.setValid("1");
                mesDepart.setProcessGroup(processGroup);
                mesDepart.setCheckorder(String.valueOf(mesDepartService.getMaxSort().getSortNum() + 100));
                i = mesDepartService.insertSelective(mesDepart);
            }else{
                //新增子部门
                mesDepart.setId(UUIDUtil.getUUID());
                mesDepart.setName(name);
                mesDepart.setParentid(id);
                mesDepart.setCheckorder(checkorder);
                mesDepart.setProcessGroup(processGroup);
                mesDepart.setParentName("1");
                i = mesDepartService.insertSelective(mesDepart);
            }

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
    public DataInfo deleteByPrimaryKey(String id){

        MesDepart mesDepart = mesDepartService.selectByParentId(id);

        DataInfo dataInfo = new DataInfo();

        if(mesDepart != null){//如果有子部门无法删除
            dataInfo.setStatus(false);
            dataInfo.setMsg("该部门有子部门，无法删除");
        }else{
            int i = mesDepartService.deleteByPrimaryKey(id);

            if(i>0){
                dataInfo.setStatus(true);
                dataInfo.setMsg("删除成功");
            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("删除失败");
            }
        }

        return dataInfo;
    }
}

