package com.jh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.entity.*;
import com.jh.service.*;
import com.jh.utils.UUIDUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("mesProductionOrder")
public class MesProductionOrderController {
    @Autowired
    private MesProductionOrderService  mesProductionOrderService;
    @Autowired
    private MesTaskService mesTaskService;

    @Autowired
    private MesTaskDetailService  mesTaskDetailService;

    @Autowired
    private MesWinModelCraftService mesWinModelCraftService;

    @Autowired
    private MesWinModelService  mesWinModelService;

    @Autowired
    private MesMenuButtonService mesMenuButtonService;

    @Autowired
    private MesMessageService mesMessageService;

    @Autowired
    private MesUsersService mesUsersService;

    @RequestMapping("productionOrderList")
    public  String mesProductionOrderList(Model model, String menuId, HttpSession session){
        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "redirect:/";
        }else {
            List<MesMenuButton> menuButtonList = mesMenuButtonService.getButtonByRoleIdAndMenuId(mesUsers.getMesRoleUsers().getRoleId(), menuId);
            model.addAttribute("menuButtonList", menuButtonList);
        }
        return "mes_production_order/mes_production_order_list";

    }

    @ResponseBody
    @RequestMapping("orderPageList")
    public JSONObject mesProductionOrderList(Model model, MesProductionOrder mesProductionOrder,int page, int limit) {
        try {
            PageHelper.startPage(page,limit);
            List<MesProductionOrder> list=mesProductionOrderService.findList(mesProductionOrder);
            for(MesProductionOrder mpo : list){
                System.out.println(mpo.getTaskCode());
                List<MesProductionOrder> mpoList = mesProductionOrderService.getSumOrderDetailByTaskId(mpo.getTaskCode());
                if(mpoList.get(0) != null){
                    mpo.setProcessDate(mpoList.get(0).getTotalNumbers());
                }else {
                    mpo.setProcessDate("0.00");
                }

            }
            PageInfo<MesProductionOrder> pageInfo = new PageInfo<>(list);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","");
            jsonObject.put("code",0);
            jsonObject.put("count",pageInfo.getTotal());
            JSONArray array= JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
            jsonObject.put("data",array);
            return  jsonObject;
        } catch (Exception e) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","系统错误,请联系管理员");
            jsonObject.put("code",200);
            jsonObject.put("count",0);
            jsonObject.put("data","");
            return jsonObject;
        }
    }


    @RequestMapping("getOrderDetailByTaskId")
    public String getOrderDetailByTaskId(Model model, String taskId) {

        List<MesProductionOrder> list = mesProductionOrderService.getOrderDetailByTaskId(taskId);
        model.addAttribute("list", list);
        return "mes_production_order/mes_order_buttonDetail";
    }

    @ResponseBody
    @RequestMapping("taskDelOrder")
    public DataInfo taskDelOrder(Model model, MesProductionOrder mesProductionOrder) {
        DataInfo dataInfo=new DataInfo();
        try {
            mesProductionOrder= mesProductionOrderService.find(mesProductionOrder.getId());
            if(!"1".equals(mesProductionOrder.getStatus() ) ){
                dataInfo.setMsg("删除失败,订单已开始生产执行.");
                dataInfo.setStatus(false);
                return  dataInfo;
            }

            mesProductionOrder.setDel("1");
            int count=mesProductionOrderService.update(mesProductionOrder);

            if(count>0){
                MesTask  mesTask=new MesTask();
                mesTask.setTaskStatus("TASK_RELEASE");
                mesTask.setId(mesProductionOrder.getTaskCode());
                mesTaskService.updateByPrimaryKeySelective(mesTask);
                dataInfo.setMsg("删除成功");
                dataInfo.setStatus(true);
            }else {
                dataInfo.setMsg("删除失败");
                dataInfo.setStatus(false);
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return  dataInfo;
    }


    @ResponseBody
    @RequestMapping("taskAuditOrder")
    public DataInfo taskAuditOrder(Model model, MesProductionOrder mesProductionOrder,HttpSession session) {
        DataInfo dataInfo = new DataInfo();
        try {
            MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");

            mesProductionOrder.setStatus("1");
            int count = mesProductionOrderService.update(mesProductionOrder);
            mesProductionOrder = mesProductionOrderService.find(mesProductionOrder.getId());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            MesUsers user = mesUsersService.findUser(mesProductionOrder.getCreateBy());
            if (count > 0) {


                MesMessage mesMessage = new MesMessage();
                mesMessage.setId(UUIDUtil.getUUID());
                mesMessage.setIsRead("0");
                mesMessage.setCreateDate(simpleDateFormat.format(new Date()));
                mesMessage.setSender(mesUsers.getNickname());
                mesMessage.setSenderId(user.getId());
                mesMessage.setMessageType("您的生产订单审核成功");
                mesMessage.setReciverId(user.getId());
                mesMessage.setContent("您的生产订单" + mesProductionOrder.getOrderCode() + "审核成功！");
                mesMessageService.insertSelective(mesMessage);

                dataInfo.setMsg("审核成功");
                dataInfo.setStatus(true);
            } else {

                MesMessage mesMessage = new MesMessage();
                mesMessage.setId(UUIDUtil.getUUID());
                mesMessage.setIsRead("0");
                mesMessage.setCreateDate(simpleDateFormat.format(new Date()));
                mesMessage.setSender(mesUsers.getNickname());
                mesMessage.setSenderId(user.getId());
                mesMessage.setMessageType("您的生产订单审核失败");
                mesMessage.setReciverId(user.getId());
                mesMessage.setContent("您的生产订单" + mesProductionOrder.getOrderCode() + "审核失败！");
                mesMessageService.insertSelective(mesMessage);
                dataInfo.setMsg("审核失败");
                dataInfo.setStatus(false);
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }

        return  dataInfo;
    }





    @RequestMapping("taskUpdatetOrder")
    public String taskUpdatetOrder(Model model, MesProductionOrder mesProductionOrder,String processDate) {
        DataInfo dataInfo=new DataInfo();
        mesProductionOrder=mesProductionOrderService.find(mesProductionOrder.getId());
        model.addAttribute("mesProductionOrder",mesProductionOrder);
        model.addAttribute("processDate",processDate);
        return  "mes_production_order/mes_production_order_update";
    }


    @ResponseBody
    @RequestMapping("update")
    public DataInfo update(Model model, MesProductionOrder mesProductionOrder) {
        DataInfo dataInfo= null;
        try {
            dataInfo = new DataInfo();
            int count=mesProductionOrderService.update(mesProductionOrder);
            mesProductionOrder=mesProductionOrderService.find(mesProductionOrder.getId());
            if(count>0){
                MesTask  mesTask=new MesTask();
                mesTask.setId(mesProductionOrder.getTaskCode());
                mesTask.setBiginDate(mesProductionOrder.getBiginDate());
                mesTask.setEndDate(mesProductionOrder.getEndDate());
                mesTaskService.updateByPrimaryKeySelective(mesTask);
                dataInfo.setMsg("保存成功");
                dataInfo.setStatus(true);
            }else {
                dataInfo.setMsg("保存失败");
                dataInfo.setStatus(false);
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return  dataInfo;
    }


    @RequestMapping("taskStatusOrder")
    public String taskStatusOrder(Model model, MesProductionOrder mesProductionOrder,String processDate) {
        DataInfo dataInfo=new DataInfo();
        mesProductionOrder=mesProductionOrderService.find(mesProductionOrder.getId());
        model.addAttribute("mesProductionOrder",mesProductionOrder);
        model.addAttribute("processDate",processDate);
        return  "mes_production_order/mes_production_order_status";
    }


    @ResponseBody
    @RequestMapping("updateStatus")
    public DataInfo updateStatus(Model model, MesProductionOrder mesProductionOrder) {
        DataInfo dataInfo=new DataInfo();
        try {
            int count=mesProductionOrderService.update(mesProductionOrder);
            if(count>0){
                dataInfo.setMsg("保存成功");
                dataInfo.setStatus(true);
            }else {
                dataInfo.setMsg("保存失败");
                dataInfo.setStatus(false);
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return  dataInfo;
    }


    @RequestMapping("orderList")
    public String orderList(Model model, MesProductionOrder mesProductionOrder) {
        mesProductionOrder.setDel("0");
        List<MesProductionOrder> list= mesProductionOrderService.findList(mesProductionOrder);
        model.addAttribute("list",list);
        return  "mes_production_order/mes_production_order_sel";
    }


    @ResponseBody
    @RequestMapping("list")
    public JSONObject list(Model model, String  taskName,int page, int limit) {
        if(taskName != null ){
            PageHelper.startPage(page,limit);
            List<Map<String,String>> list=mesTaskDetailService.findMap(taskName.trim());
            PageInfo<Map<String,String>> pageInfo = new PageInfo<>(list);
            for (Map<String,String> map:pageInfo.getList()){
                String procedureId=map.get("procedureId");
                String taskWinType=map.get("taskWinType");

                String dicId=map.get("dicId");
                String winCode=map.get("winCode");
                String taskId=map.get("taskId");
                MesTaskDetail mesTaskDetail=new  MesTaskDetail();
                mesTaskDetail.setWinNo(winCode);
                mesTaskDetail.setDicId(dicId);
                mesTaskDetail.setTaskId(taskId);
                mesTaskDetail=mesTaskDetailService.countDate(mesTaskDetail);
                map.put("countDate", mesTaskDetail.getCountDate() );
                if(StringUtils.isNotEmpty(procedureId)){
                    MesWinModel  mesWinModel=new MesWinModel();
                    mesWinModel.setCode(taskWinType);
                    List<MesWinModel> mesWinModelList=mesWinModelService.findAll(mesWinModel);
                    MesWinModelCraft  winModelCraft=new MesWinModelCraft();
                    winModelCraft.setName(procedureId);
                    winModelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
                    winModelCraft=mesWinModelCraftService.findMilestone(winModelCraft);
                    map.put("amount", winModelCraft.getNumbers() );
                }else{
                    map.put("amount", "0" );
                }
            }
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","");
            jsonObject.put("code",0);
            jsonObject.put("count",pageInfo.getTotal());
            JSONArray array= JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
            jsonObject.put("data",array);
            return  jsonObject;
        }else{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","");
            jsonObject.put("code",0);
            jsonObject.put("count",0);
            jsonObject.put("data","");
            return  jsonObject;
        }

    }

}
