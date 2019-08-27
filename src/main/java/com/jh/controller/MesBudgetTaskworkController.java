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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("mesBudgetTaskwork")
public class MesBudgetTaskworkController {

    @Autowired
    private MesTaskService mesTaskService;

    @Autowired
    private MesTaskDetailService mesTaskDetailService;

    @Autowired
    private MesDictionariesService mesDictionariesService;

    @Autowired
    private MesBudgetWorkService mesBudgetWorkService;

    @Autowired
    private MesProcessQuotaservice mesProcessQuotaservice;

    @Autowired
    private MesWinModelCraftService mesWinModelCraftService;

    @ResponseBody
    @RequestMapping("getNums")
    public MesWinModelCraft getNums(String taskWinType,String procedureId,Model model,String taskId,String winNo){

        MesBudgetTaskwork mbtNumbers = mesBudgetWorkService.dealGetNumbers(procedureId,taskWinType);

        MesBudgetTaskwork mbt = mesBudgetWorkService.getTotalLeftNum(taskId,procedureId,winNo);
        String totalNum = "0";
        if(mbt != null){
            totalNum = "".equals(mbt.getDoneNum())?"0":mbt.getDoneNum();
        }
        MesWinModelCraft mwmc = mesWinModelCraftService.getNums(taskWinType, procedureId);
        mwmc.setLeftTotalNum(totalNum);
        if(mbtNumbers == null){
            mwmc.setMbtNumbers("0");
        }else{
            mwmc.setMbtNumbers(mbtNumbers.getNumbers());
        }

        return mwmc;
    }

    @ResponseBody
    @RequestMapping("getProcessList")
    public List<MesProcessQuota> getProcessList(Model model,String teamGroup,String taskWinType){
        MesProcessQuota mqp = new MesProcessQuota();
        mqp.setDel("0");
        mqp.setTeamGroup(teamGroup);
        mqp.setCodeNo(taskWinType);
        return mesProcessQuotaservice.list(mqp);
    }


    @RequestMapping("toAddPage")
    public String toAddPage(Model model){
        //获取作业单
        List<MesTask> mesTaskList = mesTaskService.findReleaseTask();
        model.addAttribute("mesTaskList", mesTaskList);

        //从数据字典获取工序组
        MesDictionaries md = new MesDictionaries();
        md.setType("WIN_PROCESS3");
        List<MesDictionaries> mesDictionariesList = mesDictionariesService.findList(md);
        model.addAttribute("mesDictionariesList", mesDictionariesList);

        return "mes_budget_taskwork/mes_budget_taskwork_add";
    }

    @ResponseBody
    @RequestMapping("getWinNoListByTaskId")
    public List<MesTaskDetail> getWinNoListByTaskId(String taskId){

        List<MesTaskDetail> mesTaskDetailList = mesTaskDetailService.selectByTaskId(taskId);

        return mesTaskDetailList;
    }

    @ResponseBody
    @RequestMapping("getWinModelByCode")
    public List<MesDictionaries> getWinModelByCode(String code){
//        return mesDictionariesService.getWinModelByCode(code);
        return mesDictionariesService.getWinModel();
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
            mesBudgetTaskwork.setTaskmanId(mesUsers.getUsername());
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

    @ResponseBody
    @RequestMapping("getProjectIdByTaskId")
    public String getProjectIdByTaskId(String taskId){
        MesTask mesTask = mesTaskService.selectByPrimaryKey(taskId);
        return mesTask.getProjectId();
    }

    //任务分发
    @RequestMapping("toTaskSendPage")
    public String toTaskSendPage(Model model){
        List<MesBudgetTaskwork> list = mesBudgetWorkService.getTaskSend();

        List<MesBudgetTaskwork> mdtList = new ArrayList<>();

        for(MesBudgetTaskwork mbt : list){
            MesBudgetTaskwork mesbt1 = mesBudgetWorkService.taskSendChild(mbt.getId());
            MesBudgetTaskwork mesbt2 = mesBudgetWorkService.taskSendMother(mbt.getId());
            if(mesbt1!=null){
                Double dou = (Double.parseDouble(mesbt1.getChild())/Double.parseDouble(mesbt2.getMother()))*100.00;
                String donePercent = String.valueOf(String.format("%.2f",dou)+"%");
                mbt.setDonePercent(donePercent);
            }else{
                mbt.setDonePercent("0%");
            }
            mdtList.add(mbt);
        }
        Collections.sort(mdtList, new Comparator<MesBudgetTaskwork>(){
            public int compare(MesBudgetTaskwork o1, MesBudgetTaskwork o2) {
                // 这里返回的值，1升序 -1降序
                return Double.parseDouble(o1.getDonePercent().substring(0,o1.getDonePercent().length()-1))>Double.parseDouble(o2.getDonePercent().substring(0,o2.getDonePercent().length()-1)) ? 1:-1;
                // 升序
//                return o1.getDonePercent().compareTo(o2.getDonePercent());
                // 降序
//                return o2.getDonePercent().compareTo(o1.getDonePercent());
            }});
        model.addAttribute("mdtList",mdtList);

        return "mes_budget_taskwork/mes_budget_taskwork_taskSend";
    }

    @ResponseBody
    @RequestMapping("getTaskSendDetail")
    public DataInfo getTaskSendDetail(String taskId,HttpSession session){

        MesUsers mesUsers = (MesUsers)session.getAttribute("mesUsers");
        DataInfo dataInfo = new DataInfo();

        if(mesUsers == null){
            dataInfo.setStatus(false);
            dataInfo.setMsg("获取用户信息失效,请刷新页面重新登陆");
        }else{
            List<MesBudgetTaskwork> allList = mesBudgetWorkService.taskSendAllModel(taskId,mesUsers.getMesDepart().getProcessGroup());

            for(MesBudgetTaskwork all:allList){
                MesBudgetTaskwork doneList = mesBudgetWorkService.taskSendDoneModel(taskId,all.getProcedureId(),all.getWinNo(),mesUsers.getMesDepart().getProcessGroup());
                if(doneList != null){
                    if(all.getMpqName().equals(doneList.getMpqName())){
                        all.setChild(doneList.getChild());
                    }
                }else{
                    all.setChild("0");
                }

            }
            dataInfo.setStatus(true);
            dataInfo.setList(allList);
        }


        return dataInfo;
    }

    @RequestMapping("toSalaryPage")
    public String toSalaryPage(Model model,String beginTime,String endTime,String type,HttpSession session,
                               @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                                       int pageNum,
                               @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                                           int pageSize){
        MesUsers mesUsers = (MesUsers)session.getAttribute("mesUsers");

        if(mesUsers == null){
            return "login";
        }else{
            List<MesBudgetTaskwork> result = mesBudgetWorkService.getSalaryList(beginTime,endTime,mesUsers.getUsername());

            double total = 0.0;
            for(MesBudgetTaskwork mbt : result){
                total += Double.parseDouble(mbt.getDoneNum())*Double.parseDouble(mbt.getPrice());
            }
            String totalSalary =String.format("%.2f", total);
            model.addAttribute("totalSalary", totalSalary);

            PageHelper.startPage(pageNum,pageSize);
            List<MesBudgetTaskwork> salaryList = mesBudgetWorkService.getSalaryList(beginTime,endTime,mesUsers.getUsername());
            PageInfo<MesBudgetTaskwork> pageInfo = new PageInfo<>(salaryList);
            model.addAttribute("salaryList", pageInfo);


            if(type == null){
                return "mes_budget_taskwork/mes_budget_taskwork_salary";
            }else{
                return "mes_budget_taskwork/mes_budget_taskwork_salary::table_refresh";
            }
        }


    }
}
