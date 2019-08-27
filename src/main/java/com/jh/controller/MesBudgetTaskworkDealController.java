package com.jh.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.entity.*;
import com.jh.service.*;
import com.jh.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("mesBudgetTaskworkDeal")
public class MesBudgetTaskworkDealController {

    @Autowired
    private MesBudgetWorkService mesBudgetWorkService;

    @Autowired
    private MesTaskService mesTaskService;

    @Autowired
    private MesDictionariesService mesDictionariesService;

    @Autowired
    private MesTaskDetailService mesTaskDetailService;

    @Autowired
    private MesMenuButtonService mesMenuButtonService;

    @RequestMapping("toDealList")
    public String toDealList(Model model){
        List<MesTask> taskList = mesTaskService.getTasking();
        model.addAttribute("taskList", taskList);
        return "mes_budget_taskwork/mes_budget_taskwork_dealList";
    }

    @ResponseBody
    @RequestMapping("getDealList1")
    public DataInfo getDealList1(Model model, HttpSession session,String taskId,String menuId,String winNo,String name
//                             ,@RequestParam(name = "pageNum", required = false, defaultValue = "1")
//                                     int pageNum,
//                             @RequestParam(name = "pageSize", required = false, defaultValue = "1000000")
//                                         int pageSize
    ){

        MesUsers mesUsers = (MesUsers)session.getAttribute("mesUsers");
        DataInfo dataInfo = new DataInfo();

        if(mesUsers == null){
            dataInfo.setStatus(false);
            dataInfo.setMsg("获取用户信息失效,请刷新页面重新登陆");
        }else{
            List<MesBudgetTaskwork> mesBudgetTaskworkList1 = mesBudgetWorkService.selectByTaskmanId1("",taskId,winNo,name,mesUsers.getMesDepart().getProcessGroup());
            List<MesBudgetTaskwork> list = new ArrayList<>();
            for(MesBudgetTaskwork mbt :mesBudgetTaskworkList1){
                MesBudgetTaskwork mbtNumbers = mesBudgetWorkService.dealGetNumbers(mbt.getProcedureId(),mbt.getTaskWinType());
                String taskNum = String.valueOf(Integer.parseInt(mbtNumbers.getNumbers()) * Integer.parseInt(mbt.getNum()));
                mbt.setTaskNum(taskNum);
                list.add(mbt);
            }
            dataInfo.setStatus(true);
            dataInfo.setList(list);
        }

        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("getDealList2")
    public DataInfo getDealList2(Model model, HttpSession session,String taskId,String menuId,String winNo,String name
//            ,
//                                                   @RequestParam(name = "pageNum", required = false, defaultValue = "1")
//                                                           int pageNum,
//                                                   @RequestParam(name = "pageSize", required = false, defaultValue = "1000000")
//                                                           int pageSize
    ){

        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        DataInfo dataInfo = new DataInfo();

        if(mesUsers == null){
            dataInfo.setStatus(false);
            dataInfo.setMsg("获取用户信息失效,请刷新页面重新登陆");
        }else{
            List<MesMenuButton> menuButtonList =  mesMenuButtonService.getButtonByRoleIdAndMenuId(mesUsers.getMesRoleUsers().getRoleId(),menuId);
            model.addAttribute("menuButtonList",menuButtonList);

//        PageHelper.startPage(pageNum,pageSize);
            List<MesBudgetTaskwork> mesBudgetTaskworkList2 = mesBudgetWorkService.selectByTaskmanId2("",taskId,winNo,name,mesUsers.getMesDepart().getProcessGroup());
            dataInfo.setStatus(true);
            dataInfo.setList(mesBudgetTaskworkList2);
        }

//        PageInfo<MesBudgetTaskwork> pageInfo = new PageInfo<>(mesBudgetTaskworkList2);
        return dataInfo;
    }

    //输入作业单编号时获取作业单名称(唯一)
    @ResponseBody
    @RequestMapping("getTaskNameByTaskId")
    public MesTask getTaskNameByTaskId(String id){
        MesTask mesTask = mesTaskService.selectByPrimaryKey(id);
        if(mesTask != null){
            return mesTask;
        }else{
            return null;
        }
    }

    @ResponseBody
    @RequestMapping("dealSelectWinNo")
    public List<MesBudgetTaskwork> dealSelectWinNo(String taskId){
        return mesBudgetWorkService.dealSelectWinNo(taskId);
    }

    @ResponseBody
    @RequestMapping("dealSelectModelName")
    public List<MesBudgetTaskwork> dealSelectModelName(String taskId){
        return mesBudgetWorkService.dealSelectModelName(taskId);
    }

    //打开问题处理页面
    @RequestMapping("toDealProblemPage")
    public String toDealProblemPage(String id,Model model){

        MesBudgetTaskwork mesBudgetTaskwork = mesBudgetWorkService.getDealProblem(id);
        model.addAttribute("mesBudgetTaskwork", mesBudgetTaskwork);

        //从数组字典获取原因
        MesDictionaries md2 = new MesDictionaries();
        md2.setType("PROBLEM_REASON");
        List<MesDictionaries> reasonList = mesDictionariesService.findList(md2);
        model.addAttribute("reasonList", reasonList);

        return "mes_budget_taskwork/mes_budget_taskwork_dealPage";
    }

    @ResponseBody
    @RequestMapping("mesBudgetTaskworkAdd")
    public DataInfo mesBudgetTaskworkAdd(MesBudgetTaskwork mesBudgetTaskwork, HttpSession session){

        MesUsers mesUsers = (MesUsers)session.getAttribute("mesUsers");
        DataInfo dataInfo = new DataInfo();

        if(mesUsers == null){
            dataInfo.setStatus(false);
            dataInfo.setMsg("获取用户信息失效,请刷新页面重新登陆");
        }else{
            mesBudgetTaskwork.setId(UUIDUtil.getUUID());
            mesBudgetTaskwork.setChecker(mesUsers.getUsername());
            mesBudgetTaskwork.setCheckStatus("1");
            int i = mesBudgetWorkService.insertSelective(mesBudgetTaskwork);

            if(i>0){
                dataInfo.setStatus(true);
                dataInfo.setMsg("上报成功");
            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("上报失败");
            }
        }


        return dataInfo;
    }
}
