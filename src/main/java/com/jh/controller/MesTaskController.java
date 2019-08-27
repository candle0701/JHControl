package com.jh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.entity.*;
import com.jh.service.*;
import com.jh.utils.CurrentTime;
import com.jh.utils.UUIDUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.awt.Win32ColorModel24;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("mesTask")
public class MesTaskController {

    @Autowired
    private MesTaskService mesTaskService;

    @Autowired
    private MesProjectService mesProjectService;

    @Autowired
    private MesDictionariesService mesDictionariesService;

    @Autowired
    private MesTaskDetailService mesTaskDetailService;

    @Autowired
    private MesWinModelCraftService mesWinModelCraftService;

    @Autowired
    private MesProductionOrderService  mesProductionOrderService;

    @Autowired
    private MesProjectDetailService mesProjectDetailService;

    @Autowired
    private MesWinModelService mesWinModelService;
    @Autowired
    private MesProcessQuotaservice mesProcessQuotaservice;

    @Autowired
    private MesMenuButtonService mesMenuButtonService;

    @Autowired
    private MesMessageService mesMessageService;

    @Autowired
    private MesUsersService mesUsersService;

    @RequestMapping("toMesTaskList")
    public String toMesTaskList(Model model, String taskName,String id,String menuId,HttpSession session,
                                @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                                        int pageNum,
                                @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                                        int pageSize){

        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "login";
        }else{
            List<MesMenuButton> menuButtonList =  mesMenuButtonService.getButtonByRoleIdAndMenuId(mesUsers.getMesRoleUsers().getRoleId(),menuId);
            model.addAttribute("menuButtonList",menuButtonList);

            //引入分页查询，使用PageHelper分页功能
            //在查询之前传入当前页，然后多少记录
            PageHelper.startPage(pageNum,pageSize);
            //startPage后紧跟的这个查询就是分页查询
            List<MesTask> result = new ArrayList<>();
            if(id==null){
                result =  mesTaskService.findAllTask();
                //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
                PageInfo<MesTask> pageInfo = new PageInfo<>(result);
                model.addAttribute("mesTaskList", pageInfo);
                return "mes_task/mes_task_list";
            }else{
                result = mesTaskService.search(id,taskName);
                //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
                PageInfo<MesTask> pageInfo = new PageInfo<>(result);
                model.addAttribute("mesTaskList", pageInfo);
                return "mes_task/mes_task_list::table_refresh";
            }
        }

    }



    //打开编辑页面
    @RequestMapping("toMesTaskEdit")
    public String toMesTaskEdit(String id,Model model){
        MesTask MesTask = mesTaskService.search(id,null).get(0);
        List<MesTaskDetail> mesTaskDetailList = mesTaskDetailService.selectByTaskId(id);

        //获取窗型类型
        MesDictionaries mesDictionaries = new MesDictionaries();
        mesDictionaries.setType("WIN_MODEL");
        model.addAttribute("mesDictionariesList",mesDictionariesService.findList(mesDictionaries));

        model.addAttribute("mesTask", MesTask);
        model.addAttribute("mesTaskDetailList", mesTaskDetailList);
        return "mes_task/mes_task_edit";
    }

    //编辑
    @RequestMapping("mesTaskEdit")
    @ResponseBody
    public DataInfo MesTaskEdit(MesTask mesTask, String winNo,  String num, int size,HttpSession session){
        DataInfo dataInfo = new DataInfo();
        MesUsers mesUsers = (MesUsers)session.getAttribute("mesUsers");

        if(mesUsers == null){
            dataInfo.setStatus(false);
            dataInfo.setMsg("获取用户信息失效,请刷新页面重新登陆");
        }else {

            List<MesTask> msList = mesTaskService.selectDistinctAll(mesTask.getId());
            Boolean boo = true;
            for (MesTask mt : msList) {
                if (mesTask.getId().equals(mt.getId())) {
                    boo = false;
                    break;
                }
            }
            if (boo) {

                //先删再加
                MesTaskDetail mtd1 = new MesTaskDetail();
                mtd1.setTaskId(mesTask.getId());
                mtd1.setDel("1");
                int ii = mesTaskDetailService.updateByTaskId(mtd1);

                mesTaskService.updateByPrimaryKeySelective(mesTask);

                String[] winNoArr = winNo.split(",");
                String[] numArr = num.split(",");

                MesProjectDetail mpd = new MesProjectDetail();
                mpd.setProjectId(mesTask.getProjectId());
                mpd.setDel("0");
                List<MesProjectDetail> projectDetailList = mesProjectDetailService.findList(mpd);

                List<String> list1 = new ArrayList<String>();
                //校验输入的窗号是否是所选项目
                for (int i = 0; i < projectDetailList.size(); i++) {
                    list1.add(projectDetailList.get(i).getWinNo());
                }
                String[] projectDetailArr = list1.toArray(new String[projectDetailList.size()]);
                for (int i = 0; i < winNoArr.length; i++) {
                    boolean flag = Arrays.asList(projectDetailArr).contains(winNoArr[i]);
                    if (!flag) {
                        dataInfo.setStatus(false);
                        dataInfo.setMsg("请确认输入的第" + (i + 1) + "行窗号，在所选项目中必须存在");
                        return dataInfo;
                    }

                    MesTask mt = mesTaskService.getNumsByProjectIdAndWinNo(mesTask.getProjectId(),winNoArr[i]);
                    MesProjectDetail mpdetail = mesProjectDetailService.getMesProjectDetail(mesTask.getProjectId(),winNoArr[i]);

                    String mtNum = "";
                    String mpdNum = "";
                    if(mt == null){
                        mtNum = "0";
                    }else{
                        mtNum = mt.getNum()==null?"0":mt.getNum();
                    }

                    if(mpdetail == null){
                        mpdNum = "0";
                    }else{
                        mpdNum = mpdetail.getNum()==null?"0":mpdetail.getNum();
                    }

                    int leftNum = Integer.parseInt(mpdNum) - Integer.parseInt(mtNum);

                    if(leftNum < Integer.parseInt(numArr[i])){
                        dataInfo.setStatus(false);
                        dataInfo.setMsg("请确认输入的第" + (i + 1) + "行数量，不能超过项目剩余数量"+leftNum);
                        return dataInfo;
                    }
                }

                //保存作业档案详细
                for (int j = 0; j < size; j++) {
                    MesTaskDetail mesTaskDetail = new MesTaskDetail();
                    mesTaskDetail.setId(UUIDUtil.getUUID());
                    mesTaskDetail.setTaskId(mesTask.getId());
                    mesTaskDetail.setWinNo(winNoArr[j]);
                    mesTaskDetail.setNum(numArr[j]);
                    mesTaskDetail.setDel("0");

                    mesTaskDetailService.insertSelective(mesTaskDetail);
                }

                if (ii > 0) {
                    dataInfo.setStatus(true);
                    dataInfo.setMsg("编辑成功");

                    //添加审核内容到消息表
//                    MesMessage mesMessage = new MesMessage();
//                    mesMessage.setId(UUIDUtil.getUUID());
//                    mesMessage.setIsRead("0");
//                    mesMessage.setCreateDate(CurrentTime.getCurrentTimestamp());
//                    mesMessage.setSender(mesUsers.getNickname());
//                    mesMessage.setSenderId(mesUsers.getId());
//                    mesMessage.setMessageType("作业单编辑");
//                    mesMessage.setReciverId("d27d15a74a5b4ca89d9a7707e423a7f6");
//                    mesMessage.setContent(mesUsers.getNickname() + "对作业单" + mesTask.getId() + "进行了修改，请注意审核!");
//                    mesMessageService.insertSelective(mesMessage);
                } else {
                    dataInfo.setStatus(false);
                    dataInfo.setMsg("编辑失败");
                }
            } else {
                dataInfo.setStatus(false);
                dataInfo.setMsg("该作业单编号已存在");
            }

        }

        return dataInfo;
    }

    //打开编辑页面
    @RequestMapping("toMesTaskEditCheck")
    public String toMesTaskEditCheck(String id,Model model){
        MesTask MesTask = mesTaskService.search(id,null).get(0);
        List<MesTaskDetail> mesTaskDetailList = mesTaskDetailService.selectByTaskId(id);

        //获取窗型类型
        MesDictionaries mesDictionaries = new MesDictionaries();
        mesDictionaries.setType("WIN_MODEL");
        model.addAttribute("mesDictionariesList",mesDictionariesService.findList(mesDictionaries));

        model.addAttribute("mesTask", MesTask);
        model.addAttribute("mesTaskDetailList", mesTaskDetailList);
        return "mes_task/mes_task_edit_check";
    }

    //编辑
    @RequestMapping("mesTaskEditCheck")
    @ResponseBody
    public DataInfo mesTaskEditCheck(MesTask mesTask, String winNo,  String num, int size,String taskWinType,HttpSession session){
        DataInfo dataInfo = new DataInfo();
        MesUsers mesUsers = (MesUsers)session.getAttribute("mesUsers");

        if(mesUsers == null){
            dataInfo.setStatus(false);
            dataInfo.setMsg("获取用户信息失效,请刷新页面重新登陆");
        }else {

            List<MesTask> msList = mesTaskService.selectDistinctAll(mesTask.getId());
            Boolean boo = true;
            for (MesTask mt : msList) {
                if (mesTask.getId().equals(mt.getId())) {
                    boo = false;
                    break;
                }
            }
            if (boo) {
                mesTaskService.updateByPrimaryKeySelective(mesTask);

                String[] winNoArr = winNo.split(",");
                String[] numArr = num.split(",");

                MesProjectDetail mpd = new MesProjectDetail();
                mpd.setProjectId(mesTask.getProjectId());
                mpd.setDel("0");
                List<MesProjectDetail> projectDetailList = mesProjectDetailService.findList(mpd);

                List<String> list1 = new ArrayList<String>();
                //校验输入的窗号是否是所选项目
                for (int i = 0; i < projectDetailList.size(); i++) {
                    list1.add(projectDetailList.get(i).getWinNo());
                }
                String[] projectDetailArr = list1.toArray(new String[projectDetailList.size()]);
                for (int i = 0; i < winNoArr.length; i++) {
                    boolean flag = Arrays.asList(projectDetailArr).contains(winNoArr[i]);
                    if (!flag) {
                        dataInfo.setStatus(false);
                        dataInfo.setMsg("请确认输入的第" + (i + 1) + "行窗号，在所选项目中必须存在");
                        return dataInfo;
                    }
                }

                //先删再加
                MesTaskDetail mtd1 = new MesTaskDetail();
                mtd1.setTaskId(mesTask.getId());
                mtd1.setDel("1");
                int i = mesTaskDetailService.updateByTaskId(mtd1);

                //保存作业档案详细
                for (int j = 0; j < size; j++) {
                    MesTaskDetail mesTaskDetail = new MesTaskDetail();
                    mesTaskDetail.setId(UUIDUtil.getUUID());
                    mesTaskDetail.setTaskId(mesTask.getId());
                    mesTaskDetail.setWinNo(winNoArr[j]);
                    mesTaskDetail.setNum(numArr[j]);
                    mesTaskDetail.setDel("0");

                    mesTaskDetailService.insertSelective(mesTaskDetail);
                }

                if (i > 0) {
                    dataInfo.setStatus(true);
                    dataInfo.setMsg("编辑成功");

                } else {
                    dataInfo.setStatus(false);
                    dataInfo.setMsg("编辑失败");
                }
            } else {
                dataInfo.setStatus(false);
                dataInfo.setMsg("该作业单编号已存在");
            }
        }


        return dataInfo;
    }


    @ResponseBody
    @RequestMapping("getTaskNumByTaskDetailId")
    public MesTaskDetail getTaskNumByTaskDetailId(String id){
        MesTaskDetail mesTaskDetail = mesTaskDetailService.selectByPrimaryKey(id);
        return mesTaskDetail;
    }

    @RequestMapping("toMesTaskQuery")
    public String toMesTaskQuery(String id,Model model,HttpSession session){
        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "login";
        }else{
            model.addAttribute("mesUsers", mesUsers);

            MesTask MesTask = mesTaskService.search(id,null).get(0);
            List<MesTaskDetail> mesTaskDetailList = mesTaskDetailService.selectByTaskId(id);

            model.addAttribute("mesTask", MesTask);
            model.addAttribute("mesTaskDetailList", mesTaskDetailList);

            MesWinModel mwm = new MesWinModel();
            List<MesWinModel> mwmList = mesWinModelService.findAll(mwm);
            model.addAttribute("mwmList",mwmList);

            return "mes_task/mes_task_query";
        }

    }

    @RequestMapping("toMesTaskChangeStatus")
    public String toMesTaskChangeStatus(Model model,String id){

        MesTask mesTask = mesTaskService.selectByPrimaryKey(id);
        model.addAttribute("mesTask",mesTask);

        MesDictionaries mesDictionaries = new MesDictionaries();
        mesDictionaries.setType("TASK_STATUS");
        model.addAttribute("mesDictionariesList",mesDictionariesService.findList(mesDictionaries));
        model.addAttribute("id",id);
        return "mes_task/mes_task_change_status";
    }

    @ResponseBody
    @RequestMapping("mesTaskChangeStatus")
    public DataInfo mesTaskChangeStatus(MesTask mesTask){
        int i = mesTaskService.updateByPrimaryKeySelective(mesTask);
        DataInfo dataInfo=new DataInfo();
        if(i>0){
            dataInfo.setStatus(true);
            dataInfo.setMsg("修改成功");
        }else{
            dataInfo.setStatus(false);
            dataInfo.setMsg("修改失败");
        }
        return dataInfo;
    }

    //跳转到审核页面
    @RequestMapping("toMesTaskCheck")
    public String toMesTaskCheck(String id,Model model,HttpSession session){

        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "login";
        }else {
            model.addAttribute("mesUsers", mesUsers);

            MesTask MesTask = mesTaskService.search(id, null).get(0);
            List<MesTaskDetail> mesTaskDetailList = mesTaskDetailService.selectByTaskId(id);

            model.addAttribute("mesTask", MesTask);
            model.addAttribute("mesTaskDetailList", mesTaskDetailList);

            MesWinModel mwm = new MesWinModel();
            List<MesWinModel> mwmList = mesWinModelService.findAll(mwm);
            model.addAttribute("mwmList", mwmList);

            return "mes_task/mes_task_check";
        }
    }

    //审核释放
    @ResponseBody
    @RequestMapping("mesTaskCheckPass")
    public DataInfo mesTaskCheckPass(String detailId,String taskWinType,String taskWinName,int size,MesTask mesTask,HttpSession session,String reciverId,String id,String projectId,String winNo){
        DataInfo dataInfo = new DataInfo();

        MesUsers mesUsers = (MesUsers)session.getAttribute("mesUsers");
        if(mesUsers == null){
            dataInfo.setStatus(false);
            dataInfo.setMsg("获取用户信息失效,请刷新页面重新登陆");
        }else{
            mesTask.setChecker(mesUsers.getUsername());
            mesTask.setCheckTime(CurrentTime.getCurrentTimestamp());
            mesTask.setTaskStatus("TASK_RELEASE");
            mesTaskService.updateByPrimaryKeySelective(mesTask);

            String[] detailIdArr = detailId.split(",");
            String[] winNoArr = winNo.split(",");
            String[] taskWinTypeArr = taskWinType.split(",");
            String[] taskWinNameArr = taskWinName.split(",");
            for(int j = 0 ; j < size; j ++){
                List<MesTaskDetail> mtd1 = mesTaskDetailService.ifUsedByTaskIdWinNoTaskWinType(projectId,winNoArr[j]);
                String twtStr = "";
                for(MesTaskDetail mtdtwt:mtd1){
                    twtStr += mtdtwt.getTaskWinType()+",";
                }
                int twtint = twtStr.lastIndexOf(",");
                if(twtint != -1){
                    twtStr = twtStr.substring(0,twtStr.length()-1);
                }
                if(twtStr.indexOf(taskWinTypeArr[j])!=-1){

                    MesTaskDetail mesTaskDetail = new MesTaskDetail();
                    mesTaskDetail.setId(detailIdArr[j]);
                    mesTaskDetail.setTaskWinType(taskWinTypeArr[j]);
                    mesTaskDetail.setTaskWinName(taskWinNameArr[j]);

                    mesTaskDetailService.updateByPrimaryKeySelective(mesTaskDetail);

                }else{
                    if(mtd1.size() >= 1){
                        dataInfo.setStatus(false);
                        dataInfo.setMsg("第"+(j+1)+"行窗号"+winNoArr[j]+"已经在其他作业单中使用,只能选择"+twtStr);
                        return dataInfo;
                    }else{
                        MesTaskDetail mesTaskDetail = new MesTaskDetail();
                        mesTaskDetail.setId(detailIdArr[j]);
                        mesTaskDetail.setTaskWinType(taskWinTypeArr[j]);
                        mesTaskDetail.setTaskWinName(taskWinNameArr[j]);

                        mesTaskDetailService.updateByPrimaryKeySelective(mesTaskDetail);
                    }
                }


            }

            //添加审核内容到消息表
            MesMessage mesMessage = new MesMessage();
            mesMessage.setId(UUIDUtil.getUUID());
            mesMessage.setIsRead("0");
            mesMessage.setCreateDate(CurrentTime.getCurrentTimestamp());
            mesMessage.setSender(mesUsers.getNickname());
            mesMessage.setSenderId(mesUsers.getId());
            mesMessage.setMessageType("作业单审核通过");
            mesMessage.setReciverId(reciverId);
            mesMessage.setContent(mesUsers.getNickname()+",您好！作业单"+id+"已审核通过！");
            mesMessageService.insertSelective(mesMessage);

            dataInfo.setStatus(true);
        }
        return  dataInfo;
    }

    //审核驳回
    @ResponseBody
    @RequestMapping("mesTaskCheckBack")
    public DataInfo mesTaskCheckBack(MesTask mesTask,HttpSession session,String reciverId,String id){
        DataInfo dataInfo = new DataInfo();

        MesUsers mesUsers = (MesUsers)session.getAttribute("mesUsers");
        if(mesUsers == null){
            dataInfo.setStatus(false);
            dataInfo.setMsg("获取用户信息失效,请刷新页面重新登陆");
        }else {
            mesTask.setChecker(mesUsers.getUsername());
            mesTask.setCheckTime(CurrentTime.getCurrentTimestamp());
            mesTask.setTaskStatus("TASK_NOTPASS");
            mesTaskService.updateByPrimaryKeySelective(mesTask);

            //添加审核内容到消息表
            MesMessage mesMessage = new MesMessage();
            mesMessage.setId(UUIDUtil.getUUID());
            mesMessage.setIsRead("0");
            mesMessage.setCreateDate(CurrentTime.getCurrentTimestamp());
            mesMessage.setSender(mesUsers.getNickname());
            mesMessage.setSenderId(mesUsers.getId());
            mesMessage.setMessageType("作业单审核驳回");
            mesMessage.setReciverId(reciverId);
            mesMessage.setContent(mesUsers.getNickname()+",您好！作业单" + id + "被驳回,请注意检查！");
            mesMessageService.insertSelective(mesMessage);

            dataInfo.setStatus(true);
        }
        return  dataInfo;
    }

    //打开新增页面
    @RequestMapping("toMesTaskAdd")
    public String toMesTaskAdd(Model model){

        //获取单位
//        MesDictionaries mesDictionaries1 = new MesDictionaries();
//        mesDictionaries1.setType("UNIT_TYPE");
//        model.addAttribute("mesDictionariesList",mesDictionariesService.findList(mesDictionaries1));

        //获取窗型类型
        MesDictionaries mesDictionaries = new MesDictionaries();
        mesDictionaries.setType("WIN_MODEL");
        model.addAttribute("mesDictionariesList",mesDictionariesService.findList(mesDictionaries));

        //获取项目下拉
        MesProject mesProject = new MesProject();
        mesProject.setState("1");

        List<MesProject> mesProjectList=  mesProjectService.findAllProject(mesProject);
        model.addAttribute("mesProjectList",mesProjectList);

        return "mes_task/mes_task_add";
    }

    //新增
    @RequestMapping("mesTaskAdd")
    @ResponseBody
    public DataInfo MesTaskAdd(MesTask mesTask, String winNo,  String num, int size, HttpSession session){

        MesUsers mesUsers = (MesUsers)session.getAttribute("mesUsers");
        DataInfo dataInfo = new DataInfo();
        if(mesUsers == null){
            dataInfo.setStatus(false);
            dataInfo.setMsg("获取用户信息失效,请刷新页面重新登陆");
        }else {

            List<MesTask> msList = mesTaskService.selectDistinctAll(null);
            Boolean boo = true;
            for (MesTask mt : msList) {
                if (mesTask.getId().equals(mt.getId())) {
                    boo = false;
                    break;
                }
            }
            if (boo) {
                MesProjectDetail mpd = new MesProjectDetail();
                mpd.setProjectId(mesTask.getProjectId());
                mpd.setDel("0");
                List<MesProjectDetail> projectDetailList = mesProjectDetailService.findList(mpd);

                if (projectDetailList.size() == 0) {
                    dataInfo.setStatus(false);
                    dataInfo.setMsg("所选项目没有窗号，请先在项目里添加！");
                    return dataInfo;
                }

                String[] winNoArr = winNo.split(",");
                String[] numArr = num.split(",");

                List<String> list1 = new ArrayList<String>();
                //校验输入的窗号是否是所选项目
                for (int i = 0; i < projectDetailList.size(); i++) {
                    list1.add(projectDetailList.get(i).getWinNo());
                }
                String[] projectDetailArr = list1.toArray(new String[projectDetailList.size()]);
                for (int i = 0; i < winNoArr.length; i++) {
                    boolean flag = Arrays.asList(projectDetailArr).contains(winNoArr[i]);
                    if (!flag) {
                        dataInfo.setStatus(false);
                        dataInfo.setMsg("请确认输入的第" + (i + 1) + "行窗号，在所选项目中必须存在");
                        return dataInfo;
                    }

                    MesTask mt = mesTaskService.getNumsByProjectIdAndWinNo(mesTask.getProjectId(),winNoArr[i]);
                    MesProjectDetail mpdetail = mesProjectDetailService.getMesProjectDetail(mesTask.getProjectId(),winNoArr[i]);

                    String mtNum = "";
                    String mpdNum = "";
                    if(mt == null){
                        mtNum = "0";
                    }else{
                        mtNum = mt.getNum()==null?"0":mt.getNum();
                    }

                    if(mpdetail == null){
                        mpdNum = "0";
                    }else{
                        mpdNum = mpdetail.getNum()==null?"0":mpdetail.getNum();
                    }

                    int leftNum = Integer.parseInt(mpdNum) - Integer.parseInt(mtNum);

                    if(leftNum < Integer.parseInt(numArr[i])){
                        dataInfo.setStatus(false);
                        dataInfo.setMsg("请确认输入的第" + (i + 1) + "行数量，不能超过项目剩余数量"+leftNum);
                        return dataInfo;
                    }

                }


                //保存作业档案
                mesTask.setCreateTime(CurrentTime.getCurrentTime());
                mesTask.setCreater(mesUsers.getId());
                mesTask.setTaskStatus("TASK_NOTRELEASE");//新建作业单是未释放状态
                mesTask.setDel("0");

                int i = mesTaskService.insertSelective(mesTask);

                //保存作业档案详细
                for (int j = 0; j < size; j++) {

                    MesTaskDetail mesTaskDetail = new MesTaskDetail();
                    mesTaskDetail.setId(UUIDUtil.getUUID());
                    mesTaskDetail.setTaskId(mesTask.getId());
                    mesTaskDetail.setWinNo(winNoArr[j]);
                    mesTaskDetail.setNum(numArr[j]);
                    mesTaskDetail.setDel("0");

                    mesTaskDetailService.insertSelective(mesTaskDetail);
                }

                if (i > 0) {
                    dataInfo.setStatus(true);
                    dataInfo.setMsg("新增成功");

                    //添加审核内容到消息表
//                    MesMessage mesMessage = new MesMessage();
//                    mesMessage.setId(UUIDUtil.getUUID());
//                    mesMessage.setIsRead("0");
//                    mesMessage.setCreateDate(CurrentTime.getCurrentTimestamp());
//                    mesMessage.setSender(mesUsers.getNickname());
//                    mesMessage.setSenderId(mesUsers.getId());
//                    mesMessage.setMessageType("新添加作业单");
//                    mesMessage.setReciverId("d27d15a74a5b4ca89d9a7707e423a7f6");
//                    mesMessage.setContent(mesUsers.getNickname()+"新添加了作业单"+mesTask.getId()+"，请注意审核!");
//                    mesMessageService.insertSelective(mesMessage);

                } else {
                    dataInfo.setStatus(false);
                    dataInfo.setMsg("新增失败");
                }
            } else {
                dataInfo.setStatus(false);
                dataInfo.setMsg("该作业单编号已存在");
            }
        }


        return dataInfo;
    }

    @RequestMapping("taskIdButtonDetail")
    public String taskIdButtonDetail(String id,Model model){
        List<MesTask> mesTaskList = mesTaskService.taskIdButtonDetail(id);
        model.addAttribute("mesTaskList", mesTaskList);
        return "mes_task/mes_task_buttonDetail";
    }

    @ResponseBody
    @RequestMapping("deleteByPrimaryKey")
    public DataInfo deleteByPrimaryKey(MesTask mesTask){

        DataInfo dataInfo = new DataInfo();
        mesTask.setDel("1");
        int i = mesTaskService.updateByPrimaryKeySelective(mesTask);

        MesTaskDetail mtd = new MesTaskDetail();
        mtd.setTaskId(mesTask.getId());
        mtd.setDel("1");
        mesTaskDetailService.updateByTaskId(mtd);

        if(i>0){
            dataInfo.setStatus(true);
            dataInfo.setMsg("删除成功");
        }else{
            dataInfo.setStatus(false);
            dataInfo.setMsg("删除失败");
        }

        return dataInfo;
    }


    @RequestMapping("taskCountList")
    public String taskCountList(Model model,String menuId,HttpSession session){
        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "redirect:/";
        }else{
            List<MesMenuButton> menuButtonList =  mesMenuButtonService.getButtonByRoleIdAndMenuId(mesUsers.getMesRoleUsers().getRoleId(),menuId);
            model.addAttribute("menuButtonList",menuButtonList);
            return "mes_task/mes_task_count";
        }

    }

    @ResponseBody
    @RequestMapping("taskPageList")
    public JSONObject taskPageList(Model model, MesTask mesTask, int page, int limit){
        try {
            mesTask.setTaskStatus("TASK_RELEASE");

            PageHelper.startPage(page,limit);
            List<MesTask> list=mesTaskService.pageList(mesTask);
            PageInfo<MesTask> pageInfo = new PageInfo<>(list);
            MesWinModelCraft mesWinModelCraft=new MesWinModelCraft();
            MesWinModel mesWinModel=new MesWinModel();
            for (MesTask task:pageInfo.getList()){
                double countDate=0;
                List<MesTaskDetail>  mesTaskDetail=mesTaskDetailService.sel(task.getId());
                for (MesTaskDetail taskDetail:mesTaskDetail){
                    mesWinModel.setCode(taskDetail.getTaskWinType());
                    mesWinModel= mesWinModelService.findName(mesWinModel);
                    mesWinModelCraft.setWinModelId(mesWinModel.getWinId());
                    mesWinModelCraft.setCode("4");
                    List<MesWinModelCraft> mesWinModelCraftList =mesWinModelCraftService.findList(mesWinModelCraft);
                    for ( MesWinModelCraft winModelCraft :mesWinModelCraftList){
                        MesProcessQuota mesProcessQuota=new MesProcessQuota();
                        mesProcessQuota.setId(winModelCraft.getName());
                        MesProcessQuota processQuota=mesProcessQuotaservice.find(mesProcessQuota);
                        countDate += (Double.valueOf(processQuota.getRealityDate())+ Double.valueOf(processQuota.getAssistDate()))*Double.valueOf(winModelCraft.getNumbers())*Double.valueOf(taskDetail.getNum());

                    }
                }
                double a=countDate/60;
                Double countTime=Math.ceil(a);

                task.setProcessDate(String.format("%.2f",countDate/60));
                if(StringUtils.isBlank (task.getBiginDate())){
                    task.setBiginDate(task.getDownTime());
                    Double  date=Math.ceil(countTime/8);
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

                    Calendar calendar=Calendar.getInstance();
                    try {
                        calendar.setTime(sdf.parse(task.getDownTime()));
                        calendar.add(Calendar.DATE, (int)Math.ceil(date));
                        task.setEndDate(sdf.format(calendar.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                mesTaskService.updateByPrimaryKeySelective(task);

            }


            JSONObject jsonObject=new JSONObject();
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
    @RequestMapping("taskUpdate")
    public String taskUpdate(Model model, MesTask mesTask) {
        mesTask = mesTaskService.selectByPrimaryKey(mesTask.getId());
        model.addAttribute("mesTask", mesTask);
        return "mes_task/mes_task_update";
    }
    @ResponseBody
    @RequestMapping("taskStatusUpdate")
    public DataInfo taskStatusUpdate(Model model, MesTask mesTask){
        DataInfo dataInfo=new DataInfo();
        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("taskUpdateOrder")
    public DataInfo taskUpdateOrder(Model model, MesTask mesTask,HttpSession session){

        DataInfo dataInfo= null;
        try {
            MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");
            dataInfo = new DataInfo();
            if(mesUsers == null){
                dataInfo.setStatus(false);
                dataInfo.setMsg("获取用户信息失效,请刷新页面重新登陆");
            }else {
                mesTask = mesTaskService.selectByPrimaryKey(mesTask.getId());
                mesTask.setTaskStatus("TASK_ORDER");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                int count = mesTaskService.updateByPrimaryKeySelective(mesTask);
                int ListCount = mesProductionOrderService.sel().size();
                ListCount++;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                MesUsers user = mesUsersService.findUser(mesTask.getChecker());
                if (count > 0) {
                    MesProductionOrder mesProductionOrder = new MesProductionOrder();
                    mesProductionOrder.setId(UUID.randomUUID().toString().replace("-", ""));
                    mesProductionOrder.setOrderCode("DD" + sdf.format(new Date()) + ListCount);
                    mesProductionOrder.setTaskCode(mesTask.getId());
                    mesProductionOrder.setTaskName(mesTask.getTaskName());
                    mesProductionOrder.setDownTime(mesTask.getDownTime());
                    mesProductionOrder.setDeliveryTime(mesTask.getDeliveryTime());
                    mesProductionOrder.setProcessDate(mesTask.getProcessDate());
                    mesProductionOrder.setBiginDate(mesTask.getBiginDate());
                    mesProductionOrder.setEndDate(mesTask.getEndDate());
                    mesProductionOrder.setStatus("2");
                    mesProductionOrder.setCreateBy(mesUsers.getUsername());
                    mesProductionOrder.setCreateDate(sdf.format(new Date()));
                    mesProductionOrder.setUrgentStatus(mesTask.getUrgentStatus());
                    mesProductionOrderService.insertSelective(mesProductionOrder);


                    MesMessage mesMessage = new MesMessage();
                    mesMessage.setId(UUIDUtil.getUUID());
                    mesMessage.setIsRead("0");
                    mesMessage.setCreateDate(simpleDateFormat.format(new Date()));
                    mesMessage.setSender(mesUsers.getNickname());
                    mesMessage.setSenderId(user.getId());
                    mesMessage.setMessageType("作业单转生产订单成功");
                    mesMessage.setReciverId(user.getId());
                    mesMessage.setContent(mesUsers.getNickname()+",您好！作业单" + mesTask.getId() + "转生产订单成功！");
                    mesMessage.setReciver("d27d15a74a5b4ca89d9a7707e423a7f6");
                    mesMessageService.insertSelective(mesMessage);

                    dataInfo.setStatus(true);
                    dataInfo.setMsg("转生产订单成功");
                } else {
                    MesMessage mesMessage = new MesMessage();
                    mesMessage.setId(UUIDUtil.getUUID());
                    mesMessage.setIsRead("0");
                    mesMessage.setCreateDate(simpleDateFormat.format(new Date()));
                    mesMessage.setSender(mesUsers.getNickname());
                    mesMessage.setSenderId(user.getId());
                    mesMessage.setMessageType("作业单转生产订单失败");
                    mesMessage.setReciverId(user.getId());
                    mesMessage.setReciver("d27d15a74a5b4ca89d9a7707e423a7f6");
                    mesMessage.setContent(mesUsers.getNickname()+",您好！作业单" + mesTask.getId() + "转生产订单失败！");
                    mesMessageService.insertSelective(mesMessage);

                    dataInfo.setStatus(false);
                    dataInfo.setMsg("转生产订单失败");
                }
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }
    @ResponseBody
    @RequestMapping("update")
    public DataInfo update(Model model, MesTask mesTask){
        DataInfo dataInfo= null;
        try {
            int count=mesTaskService.updateByPrimaryKeySelective(mesTask);
            dataInfo = new DataInfo();
            if(count>0){
                dataInfo.setStatus(true);
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

    @RequestMapping("mesTaskDataCountDateil")
    public String mesTaskDataCountDateil(Model model, MesTask mesTask) {
        model.addAttribute("mesTask", mesTask);
        return "mes_task/mes_task_data_count_dateil";
    }

    @ResponseBody
    @RequestMapping("mesTaskDataCountList")
    public JSONObject mesTaskDataCountList(Model model, MesTask mesTask) {
        try {
            List<MesTask> list=mesTaskService.selProcessDate(mesTask);
            for (MesTask task : list) {
                MesTask mesTaskDate=mesTaskService.selDate(task);
                task.setNum(mesTaskDate.getNum());
            }
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","");
            jsonObject.put("code",0);
            jsonObject.put("count",list.size());
            JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));
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
    @RequestMapping("mesTaskDataSum")
    public String mesTaskDataSum(Model model, MesTask mesTask) {
        MesTask mesTaskDate=mesTaskService.selDate(mesTask);
        return mesTaskDate.getNum();
    }
}
