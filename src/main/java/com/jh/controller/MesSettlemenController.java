package com.jh.controller;

import com.jh.entity.DataInfo;
import com.jh.entity.MesBudgetTaskwork;
import com.jh.entity.MesSettlemen;
import com.jh.entity.MesUsers;
import com.jh.service.MesBudgetWorkService;
import com.jh.service.MesSettlemenService;
import com.jh.service.MesUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("mesSettlemen")
public class MesSettlemenController {

    @Autowired
    private MesSettlemenService mesSettlemenService;
    @Autowired
    private MesBudgetWorkService mesBudgetWorkService;
    @Autowired
    private MesUsersService mesUsersService;

    @ResponseBody
    @RequestMapping("settlemenSava")
    public DataInfo sava(Model model,MesSettlemen mesSettlemen){
        DataInfo dataInfo=new DataInfo();
        List<MesSettlemen> list=mesSettlemenService.findList(mesSettlemen);
        if(list.size() > 0 ){
            dataInfo.setStatus(false);
            dataInfo.setMsg("请勿重复月结");
        }else{
            List<MesUsers> userList=mesUsersService.findUserTask(mesSettlemen.getDatemonth());
            MesBudgetTaskwork mesBudgetTaskwork=new MesBudgetTaskwork();
            for (MesUsers li:userList){
                mesBudgetTaskwork.setTaskmanId(li.getUsername());
                mesBudgetTaskwork.setReportTime(mesSettlemen.getDatemonth());
                mesBudgetTaskwork.setCheckStatus("1");
                List<MesBudgetTaskwork> btwList=mesBudgetWorkService.find(mesBudgetTaskwork);
                double count=0;
                for (MesBudgetTaskwork btw:btwList){
                    count+=Double.valueOf(btw.getPrice())*Double.valueOf(btw.getDoneNum());
                }
                MesSettlemen settlemen=new MesSettlemen();
                settlemen.setId(UUID.randomUUID().toString().replace("-", ""));
                settlemen.setUserId(li.getUsername());
                settlemen.setUserName(li.getNickname());
                settlemen.setPrice(String.format("%.2f", count));
                settlemen.setDatemonth(mesSettlemen.getDatemonth());
                mesSettlemenService.insertSelective(settlemen);
            }
            dataInfo.setStatus(true);
            dataInfo.setMsg("月结成功");
        }
     return dataInfo;
    }
}
