package com.jh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.entity.*;
import com.jh.service.*;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("statisticsController")
public class StatisticsController {


    @Autowired
    private MesBudgetWorkService mesBudgetWorkService;
    @Autowired
    private MesSettlemenService mesSettlemenService;
    @Autowired
    private MesUsersService mesUsersService;
    @Autowired
    private MesProjectService mesProjectService;

    @Autowired
    private MesProjectDetailService  mesProjectDetailService;
    @Autowired
    private MesWinModelCraftService mesWinModelCraftService;
    @Autowired
    private MesWinModelService mesWinModelService;
    @Autowired
    private MesTaskDetailService mesTaskDetailService;
    @Autowired
    private MesDictionariesService mesDictionariesService;
    @Autowired
    private MesTaskService mesTaskService;
    @Autowired
    private MesMenuButtonService mesMenuButtonService;
    /**
     * 用户工资统计跳转页面
     * @param model
     * @return
     */
    @RequestMapping("userSalary")
    public String userSalary(Model model,HttpSession session,String menuId){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        String  dateMonth=sdf.format(new Date());
        model.addAttribute("dateMonth",dateMonth);

        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "redirect:/";
        }else {
            List<MesMenuButton> menuButtonList = mesMenuButtonService.getButtonByRoleIdAndMenuId(mesUsers.getMesRoleUsers().getRoleId(), menuId);
            model.addAttribute("menuButtonList", menuButtonList);
            return "mes_statisics/mes_userSalary_list";
        }
    }
    /**
     * 用户统计信息
     * @param model
     * @param dateMonth
     * @return
     */
    @ResponseBody
    @RequestMapping("userSalaryList")
        public JSONObject userSalaryList(Model model, String dateMonth, int page, int limit){
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
            if(StringUtils.isEmpty(dateMonth)){
                dateMonth=sdf.format(new Date());
            }

            PageHelper.startPage(page,limit);
            MesSettlemen mesSettlemen=new MesSettlemen();
            mesSettlemen.setDatemonth(dateMonth);
            List<MesSettlemen> settlemenList=mesSettlemenService.findList(mesSettlemen);
            if(settlemenList.size()>0){
                PageInfo<MesSettlemen> pageInfo=new PageInfo<>(settlemenList);
                List<MesUsers> list=new ArrayList<MesUsers>();
                for (MesSettlemen msList:pageInfo.getList()){
                    MesUsers mesUsers=new MesUsers();
                    mesUsers.setPrice(msList.getPrice());
                    mesUsers.setUsername(msList.getUserId());
                    mesUsers.setNickname(msList.getUserName());
                    list.add(mesUsers);
                }
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("msg","");
                jsonObject.put("code",0);
                jsonObject.put("count",pageInfo.getTotal());
                JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));
                jsonObject.put("data",array);
                return jsonObject;
            }else{
                List<MesUsers> userList=mesUsersService.findUserTask(dateMonth);
                PageInfo<MesUsers> pageInfo = new PageInfo<>(userList);
                MesBudgetTaskwork mesBudgetTaskwork=new MesBudgetTaskwork();
                for (MesUsers li:pageInfo.getList()){
                    mesBudgetTaskwork.setTaskmanId(li.getUsername());
                    mesBudgetTaskwork.setReportTime(dateMonth);
                    mesBudgetTaskwork.setCheckStatus("1");
                    List<MesBudgetTaskwork> list=mesBudgetWorkService.find(mesBudgetTaskwork);
                    double count=0;
                    for (MesBudgetTaskwork btw:list){
                        count+=Double.valueOf(btw.getPrice())*Double.valueOf(btw.getDoneNum());
                    }
                    li.setPrice(   String.format("%.2f", count));
                }
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("msg","");
                jsonObject.put("code",0);
                jsonObject.put("count",pageInfo.getTotal());
                JSONArray array= JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
                jsonObject.put("data",array);
                return jsonObject;
            }
        } catch (NumberFormatException e) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","系统错误,请联系管理员");
            jsonObject.put("code",200);
            jsonObject.put("count",0);
            jsonObject.put("data","");
            return jsonObject;
        }
    }

    @ResponseBody
    @RequestMapping("userSalarySum")
    public JSONObject userSalarySum(Model model, String dateMonth){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        if(StringUtils.isEmpty(dateMonth)){
            dateMonth=sdf.format(new Date());
        }
        double sum=0.0;
        MesSettlemen mesSettlemen=new MesSettlemen();
        mesSettlemen.setDatemonth(dateMonth);
        List<MesSettlemen> settlemenList=mesSettlemenService.findList(mesSettlemen);
        if(settlemenList.size()>0){
            List<MesUsers> list=new ArrayList<MesUsers>();
            for (MesSettlemen msList:settlemenList){
                sum+=Double.valueOf(msList.getPrice());
            }
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("sum",sum);
            return jsonObject;
        }else{
            List<MesUsers> userList=mesUsersService.findUserTask(dateMonth);

            MesBudgetTaskwork mesBudgetTaskwork=new MesBudgetTaskwork();
            for (MesUsers li:userList){
                mesBudgetTaskwork.setTaskmanId(li.getUsername());
                mesBudgetTaskwork.setReportTime(dateMonth);
                List<MesBudgetTaskwork> list=mesBudgetWorkService.find(mesBudgetTaskwork);
                double count=0;
                for (MesBudgetTaskwork btw:list){
                    count+=Double.valueOf(btw.getPrice())*Double.valueOf(btw.getDoneNum());
                }
                li.setPrice(   String.format("%.2f", count));
                sum+=Double.valueOf(li.getPrice());
            }
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("sum", String.format("%.2f", sum));
            return jsonObject;
        }






    }


        /**
         *  导出
         * @param session
         * @param response
         * @return
         * @throws IOException
         */
        @ResponseBody
        @RequestMapping("exportExcel")
        public  String exportExcel(HttpSession session, HttpServletResponse response,String dateMonth) throws IOException{
            // 新建一个Excel的工作空间
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet=workbook.createSheet(dateMonth+"员工计件工资表");
            HSSFRow row=sheet.createRow(0);
            row.createCell(0).setCellValue("员工编号");
            row.createCell(1).setCellValue("员工名称");
            row.createCell(2).setCellValue("计件工资(元)");
            MesSettlemen mesSettlemen=new MesSettlemen();
            mesSettlemen.setDatemonth(dateMonth);
            List<MesSettlemen> settlemenList=mesSettlemenService.findList(mesSettlemen);
            if(settlemenList.size()>0){
                for (int i=0;i<settlemenList.size();i++){
                    HSSFRow rows=sheet.createRow(i+1);
                    rows.createCell(0).setCellValue(settlemenList.get(i).getUserId());
                    rows.createCell(1).setCellValue(settlemenList.get(i).getUserName());
                    rows.createCell(2).setCellValue(settlemenList.get(i).getPrice());
                }
            }else{
                List<MesUsers> userList=mesUsersService.findUserTask(dateMonth);
                MesBudgetTaskwork mesBudgetTaskwork=new MesBudgetTaskwork();
                for (int i=0;i<userList.size();i++){
                    mesBudgetTaskwork.setTaskmanId(userList.get(i).getUsername());
                    mesBudgetTaskwork.setReportTime(dateMonth);
                    mesBudgetTaskwork.setCheckStatus("1");
                    List<MesBudgetTaskwork> list=mesBudgetWorkService.find(mesBudgetTaskwork);
                    double count=0;
                    for (MesBudgetTaskwork btw:list){
                        count+=Double.valueOf(btw.getPrice())*Double.valueOf(btw.getDoneNum());
                    }
                    HSSFRow rows=sheet.createRow(i+1);
                    rows.createCell(0).setCellValue(userList.get(i).getUsername());
                    rows.createCell(1).setCellValue(userList.get(i).getNickname());
                    rows.createCell(2).setCellValue(String.format("%.2f", count));
                }
            }
            OutputStream output= null;
            try {
                String name=dateMonth+"员工计件工资表";
                output = response.getOutputStream();
                response.reset();
                response.setHeader("Content-disposition", "attachment;filename="+new String(name.getBytes("gbk"), "iso8859-1")+".xls");
                response.setContentType("application/msexcel");
                workbook.write(output);
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }










    /**
     * 项目进度统计跳转页面
     * @param model
     * @return
     */
    @RequestMapping("projectSchedule")
    public String projectSchedule(Model model){
        MesProject mesProject=new MesProject();
        mesProject.setState("1");
        List<MesProject> projectList= mesProjectService.findAllProject(mesProject);
        model.addAttribute("projectList",projectList);
        return "mes_statisics/mes_projectSchedule_list";
    }



    /**
     * 项目进度统计
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("projectScheduleList")
    public JSONObject projectScheduleList(Model model, String taskName, int page, int limit){
        try {
            JSONObject jsonObject=new JSONObject();
            if(StringUtils.isEmpty(taskName) ){
                jsonObject.put("msg","");
                jsonObject.put("code",0);
                jsonObject.put("count",0);
                jsonObject.put("data","");
                return jsonObject;
            }
            MesProjectDetail mesProjectDetail=new MesProjectDetail();
            mesProjectDetail.setProjectId(taskName);
            PageHelper.startPage(page,limit);
            /**
             * 查询项目的窗型
             */
            List<MesProjectDetail> mesProjectDetailList=mesProjectDetailService.all(mesProjectDetail);
            PageInfo<MesProjectDetail> pageInfo = new PageInfo<>(mesProjectDetailList);

            jsonObject.put("msg","");
            jsonObject.put("code",0);
            jsonObject.put("count",pageInfo.getTotal());
            JSONArray array= JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
            jsonObject.put("data",array);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","系统错误,请联系管理员");
            jsonObject.put("code",200);
            jsonObject.put("count",0);
            jsonObject.put("data","");
            return jsonObject;
        }
    }
    /**
     * 项目进度统计详情跳转
     * @param model
     * @return
     */
    @RequestMapping("projectScheduleDateil")
    public String projectScheduleDateil(Model model, String taskNum,String code,String num,String winNo,String  projectId){
        model.addAttribute("taskNum",taskNum);
        model.addAttribute("code",code);
        model.addAttribute("num",num);
        model.addAttribute("winNo",winNo);
        model.addAttribute("projectId",projectId);
        return "mes_statisics/mes_projectSchedule_dateil";
    }

    /**
     * 项目进度统计详情
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("projectScheduleDateilList")
    public JSONObject projectScheduleDateilList(Model model, String taskNum,String code,String num,String winNo,String  projectId){

        try {
            JSONObject jsonObject=new JSONObject();
            MesBudgetTaskwork mesBudgetWork=new MesBudgetTaskwork();
            mesBudgetWork.setWinNo(winNo);
            mesBudgetWork.setProjectId(projectId);
            mesBudgetWork.setMilestone("1");
            mesBudgetWork.setMilestoneClass("2");
            List<MesBudgetTaskwork> list=mesBudgetWorkService.selFind(mesBudgetWork);
            if(list.size() > 0){
                MesWinModel  mesWinModel=new MesWinModel();
                mesWinModel.setCode(list.get(0).getTaskWinType());
                List<MesWinModel> mesWinModelList=mesWinModelService.findAll(mesWinModel);


                MesWinModelCraft  winModelCraft=new MesWinModelCraft();
                winModelCraft.setName(list.get(0).getProcedureId());
                winModelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
                winModelCraft=mesWinModelCraftService.findMilestone(winModelCraft);

                JSONArray jsonArray=new JSONArray();
                JSONObject json=new JSONObject();
                json.put("winTypeId","套");
                json.put("numCount",num);
                int count=(int)Integer.valueOf(list.get(0).getDoneNum())/Integer.valueOf(winModelCraft.getNumbers());
                json.put("completion",count);
                json.put("unfinished",Integer.valueOf(num)-count );
                json.put("winNo",winNo);
                jsonArray.add(json);
                jsonObject.put("msg","");
                jsonObject.put("code",0);
                jsonObject.put("count",1);
                jsonObject.put("data",jsonArray);
            }else{
                /**
                 * 查询项目的窗型
                 */
                MesProjectDetail mesProjectDetail=new MesProjectDetail();
                mesProjectDetail.setCode(code);
                List<MesProjectDetail> mesProjectDetailList=mesProjectDetailService.allDateil(mesProjectDetail);
                for (MesProjectDetail projectDetail:mesProjectDetailList){
                    /**
                     * 总需求量
                     */
                    projectDetail.setNumCount(String.valueOf(Integer.valueOf(num)*Integer.valueOf(projectDetail.getNumbers())));
                    projectDetail.setWinNo(winNo);
                    MesProjectDetail MesProjectDetail=new MesProjectDetail();
                    MesProjectDetail.setDicId(projectDetail.getDicId());
                    MesProjectDetail.setWinNo(winNo);
                    MesProjectDetail.setProjectId(projectId);
                    MesProjectDetail mpdCount=mesProjectDetailService.queryCount(MesProjectDetail);
                    if(mpdCount != null ){
                        MesWinModelCraft  winModelCraft=new MesWinModelCraft();
                        winModelCraft.setName(mpdCount.getProcedureId());
                        winModelCraft.setWinModelId(projectDetail.getWinModelId());
                        winModelCraft=mesWinModelCraftService.findMilestone(winModelCraft);





                        int completion=(Integer.valueOf(mpdCount.getNum())/Integer.valueOf(winModelCraft.getNumbers()));
                        winModelCraft.setName(mpdCount.getProcessLevel());
                        winModelCraft.setWinModelId(projectDetail.getWinModelId());
                        List<MesWinModelCraft> mwmc=mesWinModelCraftService.list(winModelCraft);

                        completion= Integer.valueOf(mwmc.get(0).getNumbers())*completion;
                        projectDetail.setCompletion(String.valueOf(completion));
                        int count=Integer.valueOf(projectDetail.getNumCount());
                        projectDetail.setUnfinished(String.valueOf(count-completion));
                    }else{
                        projectDetail.setCompletion("0");
                        projectDetail.setUnfinished(num);
                    }
                }
                jsonObject.put("msg","");
                jsonObject.put("code",0);
                jsonObject.put("count",mesProjectDetailList.size());
                JSONArray array= JSONArray.parseArray(JSON.toJSONString(mesProjectDetailList));
                jsonObject.put("data",array);
            }
            return jsonObject;
        } catch (NumberFormatException e) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","系统错误,请联系管理员");
            jsonObject.put("code",200);
            jsonObject.put("count",0);
            jsonObject.put("data","");
            return jsonObject;
        }
    }


    /**
     * 月产量统计跳转页面
     * @param model
     * @return
     */
    @RequestMapping("yield")
    public String yield(Model model, String menuId, HttpSession session){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        String  dateMonth=sdf.format(new Date());
        model.addAttribute("dateMonth",dateMonth);

        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "login";
        }else {
            List<MesMenuButton> menuButtonList = mesMenuButtonService.getButtonByRoleIdAndMenuId(mesUsers.getMesRoleUsers().getRoleId(), menuId);
            model.addAttribute("menuButtonList", menuButtonList);

            return "mes_statisics/mes_yield_list";
        }
    }

    /**
     * 月产量统计
     * @param model

     * @return
     */
    @ResponseBody
    @RequestMapping("yieldList")
    public JSONObject yieldList(Model model, String dateMonth, int page, int limit){
        try {
            JSONObject jsonObject=new JSONObject();
            MesBudgetTaskwork mesBudgetTaskwork=new MesBudgetTaskwork();
            mesBudgetTaskwork.setReportTime(dateMonth);
            PageHelper.startPage(page,limit);
            List<MesBudgetTaskwork> mesBudgetTaskworkList=mesBudgetWorkService.findList(mesBudgetTaskwork);
            PageInfo<MesBudgetTaskwork> pageInfo = new PageInfo<>(mesBudgetTaskworkList);

            List<MesBudgetTaskwork> budgetTaskworkList=mesBudgetWorkService.findCountList(mesBudgetTaskwork);
            PageInfo<MesBudgetTaskwork> pageInfoList = new PageInfo<>(budgetTaskworkList);
            for (MesBudgetTaskwork    bdt:pageInfoList.getList()){


                MesWinModel  mesWinModel=new MesWinModel();
                mesWinModel.setCode(bdt.getTaskWinType());
                List<MesWinModel> mesWinModelList=mesWinModelService.findAll(mesWinModel);

                MesWinModelCraft   mwmc=new MesWinModelCraft();
                mwmc.setCode("2");
                mwmc.setWinModelId(mesWinModelList.get(0).getWinId());
                mwmc.setName(bdt.getProcessLevel());
                List<MesWinModelCraft> wmcList =mesWinModelCraftService.findAll(mwmc);



                MesWinModelCraft   modelCraft=new MesWinModelCraft();
                modelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
                modelCraft.setName(bdt.getProcedureId());
                List<MesWinModelCraft> mcList =mesWinModelCraftService.findAll(modelCraft);
                bdt.setNum(String.valueOf((Integer.valueOf(bdt.getNum())/Integer.valueOf(mcList.get(0).getNumbers()))*Integer.valueOf(wmcList.get(0).getNumbers())));
            }


            for (MesBudgetTaskwork mbtwl:pageInfo.getList()){
                int i=0;
                for (MesBudgetTaskwork btwl :pageInfoList.getList()){
                        if(mbtwl.getTaskId().equals(btwl.getTaskId()) && mbtwl.getProcessLevel().equals(btwl.getProcessLevel()) && mbtwl.getMilestoneClass().equals( btwl.getMilestoneClass())){
                                i+=Integer.valueOf(btwl.getNum());
                 }

                }
                mbtwl.setNum(String.valueOf(i));
                if(mbtwl.getMilestoneClass().equals("2")){
                    mbtwl.setProcessGroupName("套");
                }
        }
            jsonObject.put("msg","");
            jsonObject.put("code",0);
            jsonObject.put("count",pageInfo.getTotal());
            JSONArray array= JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
            jsonObject.put("data",array);
            return jsonObject;
        } catch (NumberFormatException e) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","系统错误,请联系管理员");
            jsonObject.put("code",200);
            jsonObject.put("count",0);
            jsonObject.put("data","");
            return jsonObject;
        }
    }

   /* @ResponseBody
    @RequestMapping("yieldSum")
    public JSONObject yieldSum(Model model, String dateMonth){
        JSONObject jsonObject=new JSONObject();
        MesBudgetTaskwork mesBudgetTaskwork=new MesBudgetTaskwork();
        mesBudgetTaskwork.setReportTime(dateMonth);
        int numbersSum=0;
        int numSum=0;
        List<MesBudgetTaskwork> mesBudgetTaskworkList=mesBudgetWorkService.findList(mesBudgetTaskwork);


        List<MesBudgetTaskwork> budgetTaskworkList=mesBudgetWorkService.findCountList(mesBudgetTaskwork);
        for (MesBudgetTaskwork    bdt:budgetTaskworkList){

            MesWinModel  mesWinModel=new MesWinModel();
            mesWinModel.setCode(bdt.getTaskWinType());
            List<MesWinModel> mesWinModelList=mesWinModelService.findAll(mesWinModel);

            MesWinModelCraft   modelCraft=new MesWinModelCraft();
            modelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
            modelCraft.setName(bdt.getProcedureId());
            List<MesWinModelCraft> mcList =mesWinModelCraftService.findAll(modelCraft);
            bdt.setNum(String.valueOf(Integer.valueOf(bdt.getNum())/Integer.valueOf(mcList.get(0).getNumbers())));
        }

        for (MesBudgetTaskwork mbtwl:mesBudgetTaskworkList) {
            int i = 0;
            for (MesBudgetTaskwork btwl : mesBudgetTaskworkList) {
                if (mbtwl.getTaskId().equals(btwl.getTaskId()) && mbtwl.getProcessLevel().equals(btwl.getProcessLevel()) && mbtwl.getMilestoneClass().equals(btwl.getMilestoneClass())) {
                    i+=Integer.valueOf(btwl.getNum());
                }
            }
            mbtwl.setNum(String.valueOf(i));
            numSum+=i;
        }
        jsonObject.put("numSum",numSum);

        return jsonObject;
    }
*/
    /**
     *  导出
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("yieldListExportExcel")
    public  String yieldListExportExcel(HttpSession session, HttpServletResponse response,String dateMonth) throws IOException{
        // 新建一个Excel的工作空间
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet=workbook.createSheet("月产量统计");
        HSSFRow row=sheet.createRow(0);
        int j=0;
        row.createCell(j).setCellValue("作业单");
        row.createCell(++j).setCellValue("类型");
        row.createCell(++j).setCellValue("组件");
        row.createCell(++j).setCellValue("完成樘数");
        row.createCell(++j).setCellValue("日期");


        MesBudgetTaskwork mesBudgetTaskwork=new MesBudgetTaskwork();
        mesBudgetTaskwork.setReportTime(dateMonth);
        List<MesBudgetTaskwork> mesBudgetTaskworkList=mesBudgetWorkService.findList(mesBudgetTaskwork);

        List<MesBudgetTaskwork> budgetTaskworkList=mesBudgetWorkService.findCountList(mesBudgetTaskwork);
        for (MesBudgetTaskwork    bdt:budgetTaskworkList){

            MesWinModel  mesWinModel=new MesWinModel();
            mesWinModel.setCode(bdt.getTaskWinType());
            List<MesWinModel> mesWinModelList=mesWinModelService.findAll(mesWinModel);

            MesWinModelCraft   mwmc=new MesWinModelCraft();
            mwmc.setCode("2");
            mwmc.setWinModelId(mesWinModelList.get(0).getWinId());
            mwmc.setName(bdt.getProcessLevel());
            List<MesWinModelCraft> wmcList =mesWinModelCraftService.findAll(mwmc);



            MesWinModelCraft   modelCraft=new MesWinModelCraft();
            modelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
            modelCraft.setName(bdt.getProcedureId());
            List<MesWinModelCraft> mcList =mesWinModelCraftService.findAll(modelCraft);
            bdt.setNum(String.valueOf((Integer.valueOf(bdt.getNum())/Integer.valueOf(mcList.get(0).getNumbers()))*Integer.valueOf(wmcList.get(0).getNumbers())));
        }
        int p=0;
        for (MesBudgetTaskwork mbtwl:mesBudgetTaskworkList) {
            int i = 0;
            for (MesBudgetTaskwork btwl : budgetTaskworkList) {
                if (mbtwl.getTaskId().equals(btwl.getTaskId()) && mbtwl.getProcessLevel().equals(btwl.getProcessLevel()) && mbtwl.getMilestoneClass().equals(btwl.getMilestoneClass())) {
                    System.out.println(btwl.getNum()+"==============");
                    i += Integer.valueOf(btwl.getNum());
                }
            }
            mbtwl.setNum(String.valueOf(i));
            if(mbtwl.getMilestoneClass().equals("2")){
                mbtwl.setProcessGroupName("套");
            }
            int k=0;
            HSSFRow rows=sheet.createRow(++p);
            rows.createCell(k).setCellValue(mbtwl.getTaskName());
            rows.createCell(++k).setCellValue(mbtwl.getTaskWinTypeName());
            rows.createCell(++k).setCellValue(mbtwl.getProcessGroupName());
            rows.createCell(++k).setCellValue(mbtwl.getNum());
            rows.createCell(++k).setCellValue(dateMonth);
        }


        OutputStream output= null;
        try {
            String name="月产量统计"+dateMonth;
            output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment;filename="+new String(name.getBytes("gbk"), "iso8859-1")+".xls");
            response.setContentType("application/msexcel");
            workbook.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 楼号窗号统计跳转页面
     * @param model
     * @param beginDate
     * @param endDate
     * @return
     */
    @RequestMapping("win")
    public String win(Model model, Date beginDate,Date endDate){
        MesProject mesProject=new MesProject();
        mesProject.setState("1");
        List<MesProject> projectList= mesProjectService.findAllProject(mesProject);
        model.addAttribute("projectList",projectList);
        return "mes_statisics/mes_win_list";
    }
    /**
     * 楼号窗号统计
     * @param model
     * @param beginDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping("winList")
    public JSONObject winList(Model model, String taskName,String beginDate,String endDate,int page, int limit,String buildingNo){
        try {
            JSONObject jsonObject=new JSONObject();
            if(StringUtils.isEmpty(taskName) ){
                jsonObject.put("msg","");
                jsonObject.put("code",0);
                jsonObject.put("count",0);
                jsonObject.put("data","");
                return jsonObject;
            }
            MesBudgetTaskwork mesBudgetTaskwork=new MesBudgetTaskwork();
            mesBudgetTaskwork.setProjectId(taskName);
            mesBudgetTaskwork.setBeginDate(beginDate);
            mesBudgetTaskwork.setEndDate(endDate);
            mesBudgetTaskwork.setBuildingNo(buildingNo);
            PageHelper.startPage(page,limit);
            List<MesBudgetTaskwork> mesBudgetTaskworkList=mesBudgetWorkService.selAll(mesBudgetTaskwork);
            PageInfo<MesBudgetTaskwork> pageInfo = new PageInfo<>(mesBudgetTaskworkList);
            for (MesBudgetTaskwork list:pageInfo.getList()){

             if(list.getMilestoneClass().equals("2")){
                    list.setTaskName("套件");
             }
                /**
                 * 计算里程碑完成数量
                 */
                MesWinModel  mesWinModel=new MesWinModel();
                mesWinModel.setCode(list.getTaskWinType());
                List<MesWinModel> mesWinModelList=mesWinModelService.findAll(mesWinModel);


                MesWinModelCraft  winModelCraft=new MesWinModelCraft();
                winModelCraft.setName(list.getProcedureId());
                winModelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
                winModelCraft=mesWinModelCraftService.findMilestone(winModelCraft);

                MesWinModelCraft   modelCraft=new MesWinModelCraft();
                modelCraft.setCode("2");
                modelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
                modelCraft.setName(list.getProcessLevel());
                List<MesWinModelCraft> mcList =mesWinModelCraftService.findAll(modelCraft);


            int count=(int)Integer.valueOf(list.getNum())/Integer.valueOf(winModelCraft.getNumbers());
            count=Integer.valueOf(mcList.get(0).getNumbers())*count;
            list.setNum(String.valueOf(count));

            MesTaskDetail  MesTaskDetail=new MesTaskDetail();
            MesTaskDetail.setProjectId(list.getProjectId());
            MesTaskDetail.setWinNo(list.getWinNo());
            MesTaskDetail.setBuildingNo(buildingNo);
            MesTaskDetail taskDetail=mesTaskDetailService.allCount(MesTaskDetail);

            list.setNumbers(String.valueOf(Integer.valueOf(taskDetail.getNum())*Integer.valueOf(mcList.get(0).getNumbers())));

        }
            jsonObject.put("msg","");
            jsonObject.put("code",0);
            jsonObject.put("count",pageInfo.getTotal());
            JSONArray array= JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
            jsonObject.put("data",array);
            return jsonObject;
        } catch (NumberFormatException e) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","系统错误,请联系管理员");
            jsonObject.put("code",200);
            jsonObject.put("count",0);
            jsonObject.put("data","");
            return jsonObject;
        }
    }
    @ResponseBody
    @RequestMapping("winSum")
    public JSONObject winSum(Model model,String taskName,String beginDate,String endDate,String buildingNo){
        JSONObject jsonObject=new JSONObject();
        int numbersSum=0;
        int numSum=0;
        MesBudgetTaskwork mesBudgetTaskwork=new MesBudgetTaskwork();
        mesBudgetTaskwork.setProjectId(taskName);
        mesBudgetTaskwork.setBeginDate(beginDate);
        mesBudgetTaskwork.setEndDate(endDate);
        mesBudgetTaskwork.setBuildingNo(buildingNo);
        List<MesBudgetTaskwork> mesBudgetTaskworkList=mesBudgetWorkService.selAll(mesBudgetTaskwork);
        for (MesBudgetTaskwork list:mesBudgetTaskworkList){

            MesWinModel  mesWinModel=new MesWinModel();
            mesWinModel.setCode(list.getTaskWinType());
            List<MesWinModel> mesWinModelList=mesWinModelService.findAll(mesWinModel);

            /**
             * 计算里程碑完成数量
             */
            MesWinModelCraft  winModelCraft=new MesWinModelCraft();
            winModelCraft.setName(list.getProcedureId());
            winModelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
            winModelCraft=mesWinModelCraftService.findMilestone(winModelCraft);
            int count=(int)Integer.valueOf(list.getNum())/Integer.valueOf(winModelCraft.getNumbers());


            MesWinModelCraft   modelCraft=new MesWinModelCraft();
            modelCraft.setCode("2");
            modelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
            modelCraft.setName(list.getProcessLevel());
            List<MesWinModelCraft> mcList =mesWinModelCraftService.findAll(modelCraft);

            count=Integer.valueOf(mcList.get(0).getNumbers())*count;
            numSum+=count;

            MesTaskDetail  MesTaskDetail=new MesTaskDetail();
            MesTaskDetail.setProjectId(list.getProjectId());
            MesTaskDetail.setWinNo(list.getWinNo());
            MesTaskDetail.setBuildingNo(buildingNo);
            MesTaskDetail taskDetail=mesTaskDetailService.allCount(MesTaskDetail);
            list.setNumbers(String.valueOf(Integer.valueOf(taskDetail.getNum())*Integer.valueOf(mcList.get(0).getNumbers())));
            numbersSum+=Integer.valueOf(list.getNumbers());
        }
        jsonObject.put("numbersSum",numbersSum);
        jsonObject.put("numSum",numSum);
        jsonObject.put("unfinished",numbersSum-numSum);
        return jsonObject;
    }



    /**
     *  导出
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("winListExportExcel")
    public  String winListExportExcel(HttpSession session, HttpServletResponse response,String taskName,String beginDate,String endDate,String buildingNo) throws IOException{
        // 新建一个Excel的工作空间
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet=workbook.createSheet("楼号窗号统计");
        HSSFRow row=sheet.createRow(0);
        int k=0;
        row.createCell(k).setCellValue("项目名称");
        if(StringUtils.isNotEmpty(buildingNo)){
            row.createCell(++k).setCellValue("楼号");
        }
        row.createCell(++k).setCellValue("窗号");
        row.createCell(++k).setCellValue("类别");
        row.createCell(++k).setCellValue("下单樘数");
        row.createCell(++k).setCellValue("完成樘数");
        row.createCell(++k).setCellValue("未完成樘数");
        if(StringUtils.isNotEmpty(beginDate)){
            row.createCell(++k).setCellValue("开始时间");
        }
        if(StringUtils.isNotEmpty(endDate)){
            row.createCell(++k).setCellValue("结束时间");
        }
        MesBudgetTaskwork mesBudgetTaskwork=new MesBudgetTaskwork();
        mesBudgetTaskwork.setProjectId(taskName);
        mesBudgetTaskwork.setBeginDate(beginDate);
        mesBudgetTaskwork.setEndDate(endDate);
        mesBudgetTaskwork.setBuildingNo(buildingNo);
        List<MesBudgetTaskwork> mesBudgetTaskworkList=mesBudgetWorkService.selAll(mesBudgetTaskwork);
        for (int i=0;i<mesBudgetTaskworkList.size();i++){
            /**
             * 计算里程碑完成数量
             */
            MesWinModel  mesWinModel=new MesWinModel();
            mesWinModel.setCode(mesBudgetTaskworkList.get(i).getTaskWinType());
            List<MesWinModel> mesWinModelList=mesWinModelService.findAll(mesWinModel);

            MesWinModelCraft  winModelCraft=new MesWinModelCraft();
            winModelCraft.setName(mesBudgetTaskworkList.get(i).getProcedureId());
            winModelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
            winModelCraft=mesWinModelCraftService.findMilestone(winModelCraft);
            int count=(int)Integer.valueOf(mesBudgetTaskworkList.get(i).getNum())/Integer.valueOf(winModelCraft.getNumbers());

            MesWinModelCraft   modelCraft=new MesWinModelCraft();
            modelCraft.setCode("2");
            modelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
            modelCraft.setName(mesBudgetTaskworkList.get(i).getProcessLevel());
            List<MesWinModelCraft> mcList =mesWinModelCraftService.findAll(modelCraft);
            count=Integer.valueOf(mcList.get(0).getNumbers())*count;

            MesTaskDetail  MesTaskDetail=new MesTaskDetail();
            MesTaskDetail.setProjectId(mesBudgetTaskworkList.get(i).getProjectId());
            MesTaskDetail.setWinNo(mesBudgetTaskworkList.get(i).getWinNo());
            MesTaskDetail.setBuildingNo(buildingNo);
            MesTaskDetail taskDetail=mesTaskDetailService.allCount(MesTaskDetail);

            if(mesBudgetTaskworkList.get(i).getMilestoneClass().equals("2")){
                mesBudgetTaskworkList.get(i).setTaskName("套件");
            }

            HSSFRow rows=sheet.createRow(i+1);
            int j=0;
            rows.createCell(j).setCellValue(mesBudgetTaskworkList.get(i).getProjectName());
            if(StringUtils.isNotEmpty(buildingNo)){
                rows.createCell(++j).setCellValue(buildingNo);
            }
            rows.createCell(++j).setCellValue(mesBudgetTaskworkList.get(i).getWinNo());
            rows.createCell(++j).setCellValue(mesBudgetTaskworkList.get(i).getTaskName());
            rows.createCell(++j).setCellValue(String.valueOf(Integer.valueOf(taskDetail.getNum())*Integer.valueOf(mcList.get(0).getNumbers())));
            rows.createCell(++j).setCellValue(count);
            rows.createCell(++j).setCellValue(Integer.valueOf(taskDetail.getNum())*Integer.valueOf(mcList.get(0).getNumbers())-Integer.valueOf(count));
            if(StringUtils.isNotEmpty(beginDate)){
                rows.createCell(++j).setCellValue(beginDate);
            }
            if(StringUtils.isNotEmpty(endDate)){
                rows.createCell(++j).setCellValue(endDate);
            }

        }
        OutputStream output= null;
        try {
            String name="楼号窗号统计";
            output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment;filename="+new String(name.getBytes("gbk"), "iso8859-1")+".xls");
            response.setContentType("application/msexcel");
            workbook.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 工程窗号量跳转页面
     * @param model
     * @return
     */
    @RequestMapping("projectWin")
    public String projectWin(Model model){
        MesProject mesProject=new MesProject();
        mesProject.setState("1");
        List<MesProject> projectList= mesProjectService.findAllProject(mesProject);
        model.addAttribute("projectList",projectList);
        return "mes_statisics/mes_project_win_list";
    }

    /**
     * 工程窗号量统计
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("projectWinList")
    public JSONObject projectWinList(Model model, String taskName,String beginDate,String endDate,int page, int limit){
        try {
            JSONObject jsonObject=new JSONObject();
            if(StringUtils.isEmpty(taskName) ){
                jsonObject.put("msg","");
                jsonObject.put("code",0);
                jsonObject.put("count",0);
                jsonObject.put("data","");
                return jsonObject;
            }

            MesBudgetTaskwork mesBudgetTaskwork=new MesBudgetTaskwork();
            mesBudgetTaskwork.setProjectId(taskName);
            mesBudgetTaskwork.setBeginDate(beginDate);
            mesBudgetTaskwork.setEndDate(endDate);
            PageHelper.startPage(page,limit);
            List<MesBudgetTaskwork> mesBudgetTaskworkList=mesBudgetWorkService.selAllCount(mesBudgetTaskwork);
            PageInfo<MesBudgetTaskwork> pageInfo = new PageInfo<>(mesBudgetTaskworkList);
            for (MesBudgetTaskwork  mbtw:pageInfo.getList()){
                MesTaskDetail  mesTaskDetail=new MesTaskDetail();
                mesTaskDetail.setWinNo(mbtw.getWinNo());
                mesTaskDetail.setProjectId(mbtw.getProjectId());
                mesTaskDetail= mesTaskDetailService.selcount(mesTaskDetail);
                mbtw.setNumbers(mesTaskDetail.getNum());

                MesWinModel  mesWinModel=new MesWinModel();
                mesWinModel.setCode(mbtw.getTaskWinType());
                List<MesWinModel> mesWinModelList=mesWinModelService.findAll(mesWinModel);

                MesWinModelCraft   modelCraft=new MesWinModelCraft();
                modelCraft.setCode("2");
                modelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
                modelCraft.setName(mbtw.getProcessLevel());
                List<MesWinModelCraft> mcList =mesWinModelCraftService.findAll(modelCraft);

                mbtw.setNumbers(String.valueOf((int)Integer.valueOf( mbtw.getNumbers())*Integer.valueOf(mbtw.getTotalNum())));
                mbtw.setDoneNum(String.valueOf(((int)Integer.valueOf(mbtw.getDoneNum())/ Integer.valueOf(mbtw.getLeftNum()))*Integer.valueOf(mcList.get(0).getNumbers())));
            }
            jsonObject.put("msg","");
            jsonObject.put("code",0);
            jsonObject.put("count",pageInfo.getTotal());
            JSONArray array= JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
            jsonObject.put("data",array);
            return jsonObject;
        } catch (NumberFormatException e) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","系统错误,请联系管理员");
            jsonObject.put("code",200);
            jsonObject.put("count",0);
            jsonObject.put("data","");
            return jsonObject;
        }
    }


    @ResponseBody
    @RequestMapping("projectWinSum")
    public JSONObject projectWinSum(Model model,String taskName,String beginDate,String endDate){
        JSONObject jsonObject=new JSONObject();
        int numbersSum=0;
        int numSum=0;
        MesBudgetTaskwork mesBudgetTaskwork=new MesBudgetTaskwork();
        mesBudgetTaskwork.setProjectId(taskName);
        mesBudgetTaskwork.setBeginDate(beginDate);
        mesBudgetTaskwork.setEndDate(endDate);
        List<MesBudgetTaskwork> mesBudgetTaskworkList=mesBudgetWorkService.selAllCount(mesBudgetTaskwork);
        for (MesBudgetTaskwork  mbtw:mesBudgetTaskworkList){

            MesTaskDetail  mesTaskDetail=new MesTaskDetail();
            mesTaskDetail.setWinNo(mbtw.getWinNo());
            mesTaskDetail.setProjectId(mbtw.getProjectId());
            mesTaskDetail= mesTaskDetailService.selcount(mesTaskDetail);



            MesWinModel  mesWinModel=new MesWinModel();
            mesWinModel.setCode(mbtw.getTaskWinType());
            List<MesWinModel> mesWinModelList=mesWinModelService.findAll(mesWinModel);

            MesWinModelCraft   modelCraft=new MesWinModelCraft();
            modelCraft.setCode("2");
            modelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
            modelCraft.setName(mbtw.getProcessLevel());
            List<MesWinModelCraft> mcList =mesWinModelCraftService.findAll(modelCraft);



            mbtw.setNumbers(mesTaskDetail.getNum());
            mbtw.setNumbers(String.valueOf((int)Integer.valueOf( mbtw.getNumbers())*Integer.valueOf(mbtw.getTotalNum())));
            mbtw.setDoneNum(String.valueOf(((int)Integer.valueOf(mbtw.getDoneNum())/ Integer.valueOf(mbtw.getLeftNum()))*Integer.valueOf(mcList.get(0).getNumbers())));
            numSum+=Integer.valueOf(mbtw.getDoneNum());
            numbersSum+=Integer.valueOf(mbtw.getNumbers());
        }

        jsonObject.put("numbersSum",numbersSum);
        jsonObject.put("numSum",numSum);

        return jsonObject;
    }
    /**
     *  导出
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("projectWinListExportExcel")
    public  String projectWinListExportExcel(HttpSession session, HttpServletResponse response,String taskName,String beginDate,String endDate) throws IOException{
        // 新建一个Excel的工作空间
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet=workbook.createSheet("工程窗号量统计");
        HSSFRow row=sheet.createRow(0);
        int k=0;
        row.createCell(k).setCellValue("项目名称");
        row.createCell(++k).setCellValue("窗号");
        row.createCell(++k).setCellValue("类别");
        row.createCell(++k).setCellValue("下单樘数");
        row.createCell(++k).setCellValue("完成樘数");
        if(StringUtils.isNotEmpty(beginDate)){
            row.createCell(++k).setCellValue("开始时间");
        }
        if(StringUtils.isNotEmpty(endDate)){
            row.createCell(++k).setCellValue("结束时间");
        }
        MesBudgetTaskwork mesBudgetTaskwork=new MesBudgetTaskwork();
        mesBudgetTaskwork.setProjectId(taskName);
        mesBudgetTaskwork.setBeginDate(beginDate);
        mesBudgetTaskwork.setEndDate(endDate);
        List<MesBudgetTaskwork> mesBudgetTaskworkList=mesBudgetWorkService.selAllCount(mesBudgetTaskwork);
        for (int i=0;i<mesBudgetTaskworkList.size();i++){

            MesTaskDetail  mesTaskDetail=new MesTaskDetail();
            mesTaskDetail.setWinNo(mesBudgetTaskworkList.get(0).getWinNo());
            mesTaskDetail.setProjectId(mesBudgetTaskworkList.get(0).getProjectId());
            mesTaskDetail= mesTaskDetailService.selcount(mesTaskDetail);



            MesWinModel  mesWinModel=new MesWinModel();
            mesWinModel.setCode(mesBudgetTaskworkList.get(i).getTaskWinType());
            List<MesWinModel> mesWinModelList=mesWinModelService.findAll(mesWinModel);

            MesWinModelCraft   modelCraft=new MesWinModelCraft();
            modelCraft.setCode("2");
            modelCraft.setWinModelId(mesWinModelList.get(0).getWinId());
            modelCraft.setName(mesBudgetTaskworkList.get(i).getProcessLevel());
            List<MesWinModelCraft> mcList =mesWinModelCraftService.findAll(modelCraft);


            mesBudgetTaskworkList.get(i).setNumbers(mesTaskDetail.getNum());

            mesBudgetTaskworkList.get(i).setNumbers(String.valueOf((int)Integer.valueOf(  mesBudgetTaskworkList.get(i).getNumbers())*Integer.valueOf( mesBudgetTaskworkList.get(i).getTotalNum())));
            mesBudgetTaskworkList.get(i).setDoneNum(String.valueOf(((int)Integer.valueOf( mesBudgetTaskworkList.get(i).getDoneNum())/ Integer.valueOf( mesBudgetTaskworkList.get(i).getLeftNum()))*Integer.valueOf(mcList.get(0).getNumbers())));
            int j=0;
            HSSFRow rows=sheet.createRow(i+1);
            rows.createCell(j).setCellValue(mesBudgetTaskworkList.get(i).getProjectName());
            rows.createCell(++j).setCellValue(mesBudgetTaskworkList.get(i).getWinNo());
            rows.createCell(++j).setCellValue( mesBudgetTaskworkList.get(i).getGroupName());
            rows.createCell(++j).setCellValue(mesBudgetTaskworkList.get(i).getNumbers());
            rows.createCell(++j).setCellValue(mesBudgetTaskworkList.get(i).getDoneNum());
            rows.createCell(++j).setCellValue(Integer.valueOf(mesBudgetTaskworkList.get(i).getNumbers())-Integer.valueOf(mesBudgetTaskworkList.get(i).getDoneNum()));
            rows.createCell(++j).setCellValue(beginDate);
            rows.createCell(++j).setCellValue(endDate);
        }
        OutputStream output= null;
        try {
            String name="工程窗号量统计";
            output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment;filename="+new String(name.getBytes("gbk"), "iso8859-1")+".xls");
            response.setContentType("application/msexcel");
            workbook.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 工序完成情况跳转页面
     * @param model
     * @param beginDate
     * @param endDate
     * @return
     */
    @RequestMapping("processStatus")
    public String processStatus(Model model, Date beginDate,Date endDate){
        return "mes_statisics/mes_process_status_list";
    }

    /**
     * 工序完成情况统计
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("processStatusList")
    public JSONObject processStatusList(Model model, MesBudgetTaskwork mesBudgetTaskwork,int page, int limit){
        try {
            JSONObject jsonObject=new JSONObject();
            if(StringUtils.isNotEmpty(mesBudgetTaskwork.getTaskId())){
                PageHelper.startPage(page,limit);
                List<MesBudgetTaskwork> mesBudgetTaskworkList=mesBudgetWorkService.all(mesBudgetTaskwork);
                PageInfo<MesBudgetTaskwork> pageInfo=new PageInfo<>(mesBudgetTaskworkList);
                jsonObject.put("count",pageInfo.getTotal());
                JSONArray array= JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
                jsonObject.put("data",array);
            }else{
                jsonObject.put("count",0);
                jsonObject.put("data","");
            }
            jsonObject.put("msg","");
            jsonObject.put("code",0);
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


}
