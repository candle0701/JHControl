package com.jh.controller;


import com.jh.entity.DataInfo;
import com.jh.entity.MesProcessDetails;
import com.jh.entity.MesProcessQuota;
import com.jh.service.MesProcessDateilsService;
import com.jh.service.MesProcessQuotaservice;
import com.jh.service.MesWinModelCraftService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("mesProcessDetails")
public class MesProcessDetailsController {

    @Autowired
    private MesProcessDateilsService mesProcessDateilsService;
    @Autowired
    private MesProcessQuotaservice mesProcessQuotaservice;
    @Autowired
    private MesWinModelCraftService mesWinModelCraftService;
    @ResponseBody
    @RequestMapping("findList")
      public List<MesProcessDetails> findList(MesProcessDetails mesProcessDetails){
        List<MesProcessDetails> list=mesProcessDateilsService.findList(mesProcessDetails);
        MesProcessQuota   mesProcessQuota=new   MesProcessQuota();
        for (MesProcessDetails mpd:list){
            mesProcessQuota.setId(mpd.getName());
            mesProcessQuota = mesProcessQuotaservice.find(mesProcessQuota);
            mpd.setName(mesProcessQuota.getName());
        }
        return list;
    }


    @ResponseBody
    @RequestMapping("list")
    public List<MesProcessDetails> list(MesProcessDetails mesProcessDetails){
        if(!StringUtils.isNotEmpty(mesProcessDetails.getWmcId())){
            MesProcessDetails mpd=mesProcessDateilsService.find(mesProcessDetails);
            mesProcessDetails.setWmcId(mpd.getWmcId());
        }

        List<MesProcessDetails> list=mesProcessDateilsService.findList(mesProcessDetails);
        MesProcessQuota   mesProcessQuota=new   MesProcessQuota();
        for (MesProcessDetails mpd:list){
            mesProcessQuota.setId(mpd.getName());
            mesProcessQuota = mesProcessQuotaservice.find(mesProcessQuota);
            mpd.setName(mesProcessQuota.getName());
        }
        return list;
    }

    @RequestMapping("edit")
    public String edit(MesProcessDetails mesProcessDetails, Model model){
        mesProcessDetails=mesProcessDateilsService.find(mesProcessDetails);
        model.addAttribute("mesProcessDetails",mesProcessDetails);

        List<MesProcessQuota> list=mesProcessQuotaservice.findList(new MesProcessQuota());
        model.addAttribute("list",list);
        return "mes_win_model_craft/edit";
    }


    @ResponseBody
    @RequestMapping("update")
    public DataInfo update(MesProcessDetails mesProcessDetails){
        DataInfo dataInfo=new DataInfo();
        try {
            int numbers=mesProcessDateilsService.update(mesProcessDetails);
            if(numbers>0){
                dataInfo.setStatus(false);
                dataInfo.setMsg("修改成功");
            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("修改失败");
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("del")
    public DataInfo del(MesProcessDetails mesProcessDetails){

        DataInfo dataInfo= null;
        try {
            MesProcessDetails mpd=mesProcessDateilsService.find(mesProcessDetails);
            mesProcessDetails.setDel("1");
            dataInfo = new DataInfo();
            int numbers=mesProcessDateilsService.update(mesProcessDetails);
            if(numbers>0){
                MesProcessDetails processDetails=new MesProcessDetails();
                processDetails.setWmcId(mpd.getWmcId());
                List<MesProcessDetails> list=mesProcessDateilsService.findList(processDetails);
                MesProcessQuota   mesProcessQuota=new   MesProcessQuota();
                for (MesProcessDetails pd:list){
                    mesProcessQuota.setId(mpd.getName());
                    mesProcessQuota = mesProcessQuotaservice.find(mesProcessQuota);
                    pd.setName(mesProcessQuota.getName());
                }
                dataInfo.setList(list);
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
    public String from(String wmcId , Model model){

        model.addAttribute("wmcId",wmcId);
        List<MesProcessQuota> list=mesProcessQuotaservice.findList(new MesProcessQuota());
        model.addAttribute("list",list);
        return "mes_win_model_craft/from";
    }


    @ResponseBody
    @RequestMapping("add")
    public DataInfo add(MesProcessDetails mesProcessDetails){
        DataInfo dataInfo= null;
        try {
            mesProcessDetails.setId(UUID.randomUUID().toString().replace("-", ""));
            mesProcessDetails.setDel("0");
            mesProcessDetails.setWinId(mesWinModelCraftService.find(mesProcessDetails.getWmcId()).getWinModelId());
            dataInfo = new DataInfo();
            int numbers=mesProcessDateilsService.insertSelective(mesProcessDetails);
            if(numbers>0){
                dataInfo.setStatus(false);
                dataInfo.setMsg("添加成功");
            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("添加失败");
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }


}
