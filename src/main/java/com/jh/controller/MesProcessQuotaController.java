package com.jh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.entity.*;
import com.jh.service.MesDictionariesService;
import com.jh.service.MesMenuButtonService;
import com.jh.service.MesProcessQuotaservice;
import com.jh.service.MesWinModelCraftService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("mesProcessQuota")
public class MesProcessQuotaController {


    @Autowired
    private MesProcessQuotaservice mesProcessQuotaservice;
    @Autowired
    private MesDictionariesService mesDictionariesService;
    @Autowired
    private MesMenuButtonService mesMenuButtonService;

    @Autowired
    private MesWinModelCraftService     mesWinModelCraftService;

    @RequestMapping("mesProcessQuotaList")
    public  String mesProcessQuotaList(Model model, String menuId, HttpSession session){

        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "redirect:/";
        }else{
            List<MesMenuButton> menuButtonList = mesMenuButtonService.getButtonByRoleIdAndMenuId(mesUsers.getMesRoleUsers().getRoleId(), menuId);
            model.addAttribute("menuButtonList", menuButtonList);
        }
        return "mes_process_quota/mes_process_quota_list";
    }

    @ResponseBody
    @RequestMapping("pageList")
    public JSONObject pageList(MesProcessQuota mesProcessQuota,int page, int limit){
        try {
            PageHelper.startPage(page,limit);
            List<MesProcessQuota> list=mesProcessQuotaservice.findList(mesProcessQuota);
            PageInfo<MesProcessQuota> pageInfo = new PageInfo<>(list);
            for (MesProcessQuota li:pageInfo.getList()){
                MesDictionaries mesDictionaries=mesDictionariesService.get(li.getStatus());
                li.setStatus(mesDictionaries.getName());
                MesDictionaries md=mesDictionariesService.get(li.getTeamGroup());
                li.setTeamGroup(md.getName());
            }
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","");
            jsonObject.put("code",0);
            jsonObject.put("count",pageInfo.getTotal());
            JSONArray array= JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
            jsonObject.put("data",array);
            return jsonObject;
        } catch (Exception e) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","系统错误,请联系管理员");
            jsonObject.put("code",200);
            jsonObject.put("count",0);
            jsonObject.put("data","");
            return jsonObject;
        }
    }

    @ResponseBody
    @RequestMapping("del")
    public DataInfo del(MesProcessQuota mesProcessQuota){
        DataInfo dataInfo=new DataInfo();
        try {
            MesWinModelCraft mesWinModelCraft=new MesWinModelCraft();
            mesWinModelCraft.setName(mesProcessQuota.getId());
            mesWinModelCraft.setCode("4");
            List<MesWinModelCraft> list=mesWinModelCraftService.list(mesWinModelCraft);
            if(list.size()>0) {
                dataInfo.setStatus(false);
                dataInfo.setMsg("删除失败,此数据正在使用");
                return dataInfo;
            }
            mesProcessQuota.setDel("1");
            int number=mesProcessQuotaservice.update(mesProcessQuota);
            if(number>0){
                dataInfo.setStatus(true);
                dataInfo.setMsg("删除成功");
            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("删除失败");
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }


    @RequestMapping("from")
    public  String from(Model model,MesProcessQuota mesProcessQuota){
        MesDictionaries mesDictionaries=new MesDictionaries();
        mesDictionaries.setType("WIN_PROCESS3");
        List<MesDictionaries> processGroup=mesDictionariesService.findList(mesDictionaries);
        model.addAttribute("processGroup",processGroup);


        mesDictionaries.setType("WIN_PROCESS2");
        List<MesDictionaries> processCategory=mesDictionariesService.findList(mesDictionaries);
        model.addAttribute("processCategory",processCategory);

        if(StringUtils.isNotEmpty(mesProcessQuota.getId())){
           mesProcessQuota=mesProcessQuotaservice.find(mesProcessQuota);
           model.addAttribute("mesProcessQuota",mesProcessQuota);
         /*   return "/mes_process_quota/mes_process_quota_edit";*/
        }
        return "mes_process_quota/mes_process_quota_add";
    }
    @ResponseBody
    @RequestMapping("verifyCode")
    public  DataInfo verifyCode(Model model,MesProcessQuota mesProcessQuota){
        DataInfo dataInfo=new DataInfo();
        List<MesProcessQuota> list=mesProcessQuotaservice.findCode(mesProcessQuota);
        if(list.size() == 0){
            dataInfo.setStatus(true);
        }else{
            dataInfo.setStatus(false);
        }
        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("save")
    public  DataInfo save(Model model,MesProcessQuota  mesProcessQuota ){
        DataInfo dataInfo= null;
        try {
            dataInfo = new DataInfo();
            int count=0;
            if(StringUtils.isNotEmpty(mesProcessQuota.getId())){
                count=mesProcessQuotaservice.update(mesProcessQuota);
            }else{
                mesProcessQuota.setId(mesProcessQuota.getCode());
                count=mesProcessQuotaservice.insertSelective(mesProcessQuota);
            }
            if(count>0){
                dataInfo.setStatus(true);
                dataInfo.setMsg("保存成功");
            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("保存失败");
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }



}
