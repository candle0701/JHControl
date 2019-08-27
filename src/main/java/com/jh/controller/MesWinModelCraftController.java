package com.jh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hazelcast.internal.json.JsonArray;
import com.jh.entity.*;
import com.jh.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("mesWinModelCraft")
public class MesWinModelCraftController {


    @Autowired
    private MesWinModelCraftService mesWinModelCraftService;

    @Autowired
    private MesWinModelService mesWinModelService;

    @Autowired
    private MesDictionariesService mesDictionariesService;


    @Autowired
    private MesProcessQuotaservice mesProcessQuotaservice;
    @Autowired
    private MesProcessDateilsService mesProcessDateilsService;

    @Autowired
    private MesMenuButtonService mesMenuButtonService;


    @Autowired
    private MesTaskDetailService mesTaskDetailService;


    @RequestMapping("CraftList")
    public  String CraftList(Model model, MesWinModelCraft mesWinModelCraft ,String menuId,HttpSession session){
        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "redirect:/";
        }else {
            List<MesMenuButton> menuButtonList = mesMenuButtonService.getButtonByRoleIdAndMenuId(mesUsers.getMesRoleUsers().getRoleId(), menuId);
            model.addAttribute("menuButtonList", menuButtonList);
            return "mes_win_model_craft/list";
        }
    }



    @ResponseBody
    @RequestMapping("findList")
    public JSONObject findList(Model model, MesWinModelCraft mesWinModelCraft) {
        JSONObject jsonObject=new JSONObject();
        try {
            if(!StringUtils.isNotEmpty(mesWinModelCraft.getWinModelId()) ){
                jsonObject.put("code" ,0);
                jsonObject.put("msg","ok");
                jsonObject.put("count",0);
                jsonObject.put("data","");
                return jsonObject;
            }
            List<MesWinModelCraft> list = mesWinModelCraftService.list(mesWinModelCraft);
            List<ZTree> zTreeList = new ArrayList<ZTree>();
            for (int i = 0; i < list.size(); i++) {
                ZTree zTree = new ZTree();
                zTree.setD_id(list.get(i).getId());
                zTree.setD_pid(list.get(i).getParentId());
                zTree.setLever(list.get(i).getCode());
                if(! list.get(i).getParentId().equals("0")){
                    zTree.setNumber(list.get(i).getNumbers());
                    if(list.get(i).getCode().equals("4")){
                        MesProcessQuota mesProcessQuota=new MesProcessQuota();
                        mesProcessQuota.setId(list.get(i).getName());
                        mesProcessQuota=mesProcessQuotaservice.find(mesProcessQuota);

                        zTree.setName(mesProcessQuota.getName());
                    }else{
                        MesDictionaries mesDictionaries= mesDictionariesService.get(list.get(i).getName());
                        zTree.setName(mesDictionaries.getName());
                    }

                }else{
                    zTree.setNumber("1");
                    zTree.setName(list.get(i).getName());
                }

                zTreeList.add(zTree);
            }
            jsonObject.put("code" ,0);
            jsonObject.put("msg","ok");
            jsonObject.put("count",zTreeList.size());
            jsonObject.put("data",JSONArray.parseArray(JSON.toJSONString(zTreeList)));
        } catch (Exception e) {
            jsonObject.put("code" ,0);
            jsonObject.put("msg","系统错误,请联系管理员");

            jsonObject.put("data","");
        }
        return jsonObject;
    }






    @ResponseBody
    @RequestMapping("del")
    public DataInfo del(MesWinModelCraft mesWinModelCraft , Model model, HttpSession session){
        DataInfo dataInfo=new DataInfo();

        try {
            MesWinModelCraft  wmc=mesWinModelCraftService.find(mesWinModelCraft.getId());
            MesWinModel mesWinModel=mesWinModelService.find(wmc.getWinModelId());
            MesTaskDetail mesTaskDetail=new MesTaskDetail();
            mesTaskDetail.setDel("0");
            mesTaskDetail.setTaskWinType(mesWinModel.getCode());
            List<MesTaskDetail> mesTaskDetailList= mesTaskDetailService.find(mesTaskDetail);
            if(mesTaskDetailList.size()>0){
                dataInfo.setStatus(false);
                dataInfo.setMsg("删除失败,此工序已在作业单中使用.");
                return dataInfo;
            }


            MesWinModelCraft winModelCraft=new MesWinModelCraft();
            winModelCraft.setParentId(mesWinModelCraft.getId());
            List<MesWinModelCraft> list=mesWinModelCraftService.list(winModelCraft);
            if(list.size()>0){
                dataInfo.setStatus(false);
                dataInfo.setMsg("删除失败,请先删除子节点.");
            }else{
                    mesWinModelCraft.setDel("1");
                    int number=mesWinModelCraftService.update(mesWinModelCraft);
                    if(number>0){
                        dataInfo.setStatus(true);
                        dataInfo.setMsg("删除成功");
                    }else{
                        dataInfo.setStatus(false);
                        dataInfo.setMsg("删除失败");
                    }
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }



    @RequestMapping("copyFrom")
    public String copyFrom(String code , Model model){
        model.addAttribute("code", code);
        return "mes_win_model_craft/copy_from";
    }

    @ResponseBody
    @RequestMapping("copySelModel")
    public DataInfo copySelModel(String code , Model model){
        DataInfo dataInfo=new DataInfo();
        try {
            MesWinModel MesWinModel=new MesWinModel();
            MesWinModel.setCode(code);
            List<MesWinModel>  list=mesWinModelService.findAll(MesWinModel);
            if(list.size() > 0){
                dataInfo.setList(list);
                dataInfo.setStatus(true);

            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("请输入正确的窗型编号.");
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }

        return dataInfo;
    }

    /**
     *
     * @param code 填充对象的窗型ID
     * @param winCode   复制对象的窗型ID
     * @return
     */
    @ResponseBody
    @RequestMapping("copyAdd")
    public DataInfo copyAdd(String code ,String winCode, Model model,HttpSession session){
        DataInfo dataInfo=new DataInfo();
        try {
            List<MesWinModelCraft> winModelCraft= mesWinModelCraftService.sel(code);
            if(winModelCraft.size()>1){
                dataInfo.setStatus(false);
                dataInfo.setMsg("此窗型工序已存在，请先删除工序.");
                return dataInfo;
            }

            MesWinModelCraft mesWinModelCraft=new MesWinModelCraft();
            MesWinModel MesWinModel=new MesWinModel();
            MesWinModel.setCode(winCode);
            List<MesWinModel>  list=mesWinModelService.findAll(MesWinModel);
            mesWinModelCraft.setWinModelId(list.get(0).getWinId());
            //查询复制窗型所有树形菜单类型
            List<MesWinModelCraft> winModelCraftList=mesWinModelCraftService.list(mesWinModelCraft);
            //查询复制窗型菜单一级
            mesWinModelCraft.setParentId("0");
            List<MesWinModelCraft> copyList=mesWinModelCraftService.list(mesWinModelCraft);
            if(copyList.size() <= 0){
                dataInfo.setStatus(false);
                dataInfo.setMsg("源窗型不存在");
            }else{
                //填充窗型第菜单一级
                MesWinModel.setCode(code);
                List<MesWinModel>  list1=mesWinModelService.findAll(MesWinModel);
                mesWinModelCraft.setWinModelId(list1.get(0).getWinId());
                List<MesWinModelCraft> modelCraftList=mesWinModelCraftService.list(mesWinModelCraft);

                getChild(winModelCraftList, copyList.get(0), modelCraftList.get(0), session);

                dataInfo.setStatus(true);
                dataInfo.setMsg("复制生成成功！");
            }

            return dataInfo;
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }

        return dataInfo;
    }

    /**
     *
     * @param winModelCraftList  树形所有数据源
     * @param copy  查询复制窗型菜单一级
     * @param modelCraft 填充窗型第菜单一级
     */
    public void getChild(   List<MesWinModelCraft> winModelCraftList,MesWinModelCraft copy,MesWinModelCraft modelCraft,HttpSession session){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<MesWinModelCraft> copylist=new ArrayList<MesWinModelCraft>();

        List<MesWinModelCraft> list=new ArrayList<MesWinModelCraft>();
        for (int i=0;i<winModelCraftList.size();i++) {
            if (winModelCraftList.get(i).getParentId() != "0") {
                if (winModelCraftList.get(i).getParentId().equals(copy.getId())) {
                    copylist.add( winModelCraftList.get(i)) ;
                    MesWinModelCraft winModelCraft=new MesWinModelCraft();
                    winModelCraft.setId(UUID.randomUUID().toString().replace("-", ""));
                    winModelCraft.setName(winModelCraftList.get(i).getName());
                    winModelCraft.setParentId(modelCraft.getId());
                    winModelCraft.setSort(winModelCraftList.get(i).getSort());
                    winModelCraft.setCopyId(winModelCraftList.get(i).getId());
                    winModelCraft.setCreateBy(String.valueOf(session.getAttribute("username")));
                    winModelCraft.setCreateDate(simpleDateFormat.format(new Date()));
                    winModelCraft.setDel("0");
                    winModelCraft.setNumbers(winModelCraftList.get(i).getNumbers());
                    winModelCraft.setCode(winModelCraftList.get(i).getCode());
                    winModelCraft.setWinModelId(modelCraft.getWinModelId());
               /*     winModelCraft.setPercentage(winModelCraftList.get(i).getPercentage());*/
                    mesWinModelCraftService.insertSelective(winModelCraft);
                    list.add( winModelCraft) ;
                }
            }
        }
        if (copylist.size()> 0) {
            copyChild(list,copylist,winModelCraftList,session);
        }

    }

    /**
     *
     * @param wmcList   填充窗型菜单数据
     * @param wmcCopyList 复制窗型菜单数据
     * @param winModelCraftList  所有菜单数据
     */
    public boolean copyChild(   List<MesWinModelCraft> wmcList, List<MesWinModelCraft> wmcCopyList,List<MesWinModelCraft> winModelCraftList,HttpSession session ){

        if (wmcCopyList.size() == 0) {
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 复制窗型菜单数据
        List<MesWinModelCraft> copylist=new ArrayList<MesWinModelCraft>();

        //填充窗型菜单数据
        List<MesWinModelCraft> list=new ArrayList<MesWinModelCraft>();


        for (int i=0;i<winModelCraftList.size();i++){
            if (winModelCraftList.get(i).getParentId() != "0") {

                for (int j=0;j<wmcCopyList.size();j++){
                    if(winModelCraftList.get(i).getParentId().equals(wmcCopyList.get(j).getId())){

                        for (int k=0;k<wmcList.size();k++){
                               if(wmcCopyList.get(j).getId().equals( wmcList.get(k).getCopyId())){
                                   copylist.add( winModelCraftList.get(i)) ;

                                   MesWinModelCraft winModelCraft=new MesWinModelCraft();
                                   winModelCraft.setId(UUID.randomUUID().toString().replace("-", ""));
                                   winModelCraft.setName(winModelCraftList.get(i).getName());
                                   winModelCraft.setParentId(wmcList.get(k).getId());
                                   winModelCraft.setSort(winModelCraftList.get(i).getSort());
                                   winModelCraft.setCopyId(winModelCraftList.get(i).getId());
                                   winModelCraft.setCreateBy(String.valueOf(session.getAttribute("username")));
                                   winModelCraft.setCreateDate(simpleDateFormat.format(new Date()));
                                   winModelCraft.setDel("0");
                                   winModelCraft.setNumbers(winModelCraftList.get(i).getNumbers());
                                   winModelCraft.setWinModelId(wmcList.get(k).getWinModelId());
                                   winModelCraft.setCode(winModelCraftList.get(i).getCode());
                                   mesWinModelCraftService.insertSelective(winModelCraft);
                                   list.add( winModelCraft) ;

                               }
                        }

                    }
                }
            }
        }

        if (copylist.size()> 0) {
            copyChild(list,copylist,winModelCraftList,session);
        }

        return  true;

    }


    @RequestMapping("from")
    public String from(Model model,MesWinModelCraft mesWinModelCraft){

        MesWinModelCraft  winModelCraft=mesWinModelCraftService.find(mesWinModelCraft.getParentId());
        model.addAttribute("winModelCraft",winModelCraft);
        int code=0;
        String type="WIN_PROCESS";
        MesDictionaries mesDictionaries=new MesDictionaries();
        code=Integer.valueOf(winModelCraft.getCode());
        mesDictionaries.setType(type+String.valueOf(++code));
        model.addAttribute("mesWinModelCraft",mesWinModelCraft);
        model.addAttribute("code",code);
        if(code != 4){
            List<MesDictionaries> list=  mesDictionariesService.findList(mesDictionaries);
            model.addAttribute("list",list);
        }else{

            MesWinModelCraft  wmc=mesWinModelCraftService.find(winModelCraft.getParentId());
            MesProcessQuota mesProcessQuota=new MesProcessQuota();
            mesProcessQuota.setStatus(wmc.getName());
            mesProcessQuota.setTeamGroup(winModelCraft.getName());
            List<MesProcessQuota> list=mesProcessQuotaservice.findCode(mesProcessQuota);
            model.addAttribute("list",list);
        }

        return "mes_win_model_craft/mes_win_model_craft_from";
    }
    @ResponseBody
    @RequestMapping("findName")
    public  DataInfo findName(Model model,MesWinModelCraft mesWinModelCraft){
        DataInfo dataInfo=new DataInfo();
        try {
            List<MesWinModelCraft> list= mesWinModelCraftService.findAll(mesWinModelCraft);
            if(list.size()>0){

                dataInfo.setStatus(true);
                dataInfo.setMsg("请重新选择工序名称");
            }else{

                    dataInfo.setStatus(false);
                    dataInfo.setMsg("");
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }


    @ResponseBody
    @RequestMapping("list")
    public  List<MesWinModelCraft> list(Model model,MesWinModelCraft mesWinModelCraft){
        List<MesWinModelCraft> list= mesWinModelCraftService.list(mesWinModelCraft);
        for (MesWinModelCraft wmc: list){
            if(wmc.getCode().equals("4")){
                MesProcessQuota mesProcessQuota=new MesProcessQuota();
                mesProcessQuota.setId(wmc.getName());
                mesProcessQuota=mesProcessQuotaservice.find(mesProcessQuota);
                wmc.setName(mesProcessQuota.getName());
            }else{
                MesDictionaries group=  mesDictionariesService.get(wmc.getName());
                wmc.setName(group.getName());
            }
        }
        return list;
    }


    @ResponseBody
    @RequestMapping("add")
    public DataInfo add(MesWinModelCraft mesWinModelCraft, Model model, HttpSession session){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DataInfo dataInfo=new DataInfo();

        try {
            mesWinModelCraft.setId(UUID.randomUUID().toString().replace("-", ""));

            MesWinModelCraft winModelCraft=new MesWinModelCraft();
            winModelCraft.setParentId(mesWinModelCraft.getId());
            List<MesWinModelCraft> list= mesWinModelCraftService.list(winModelCraft);

            MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");
            mesWinModelCraft.setCreateBy(mesUsers.getUsername());


            MesWinModelCraft  mwmc= mesWinModelCraftService.find(mesWinModelCraft.getParentId());
            mesWinModelCraft.setWinModelId(mwmc.getWinModelId());

            mesWinModelCraft.setCreateDate(simpleDateFormat.format(new Date()));
            mesWinModelCraft.setDel("0");
            int code=Integer.valueOf(mwmc.getCode());
            code++;
            mesWinModelCraft.setCode(String.valueOf(code));
            int number=mesWinModelCraftService.insertSelective(mesWinModelCraft);
            if(number>0){

                dataInfo.setList(list);
                dataInfo.setStatus(true);
                dataInfo.setMsg("添加成功");
            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("添加失败");
            }
        } catch (NumberFormatException e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }



    @RequestMapping("edit")
    public String edit(MesWinModelCraft mesWinModelCraft , Model model){
        mesWinModelCraft=mesWinModelCraftService.find(mesWinModelCraft.getId());
        model.addAttribute("mesWinModelCraft",mesWinModelCraft);
        int code=Integer.valueOf(mesWinModelCraft.getCode());
        String type="WIN_PROCESS";
        MesDictionaries mesDictionaries=new MesDictionaries();
        model.addAttribute("code",code);
        if(code != 4){
            mesDictionaries.setType(type+String.valueOf(code));
            List<MesDictionaries> list=  mesDictionariesService.findList(mesDictionaries);
            model.addAttribute("list",list);
        }else{
            MesWinModelCraft  wmc=mesWinModelCraftService.find(mesWinModelCraft.getParentId());
            MesProcessQuota mesProcessQuota=new MesProcessQuota();
            mesProcessQuota.setStatus(mesWinModelCraftService.find(wmc.getParentId()).getName());
            mesProcessQuota.setTeamGroup(wmc.getName());
            List<MesProcessQuota> list=mesProcessQuotaservice.findCode(mesProcessQuota);
            model.addAttribute("list",list);
        }
        return "mes_win_model_craft/mes_win_model_craft_edit";
    }



    @ResponseBody
    @RequestMapping("update")
    public DataInfo update(MesWinModelCraft mesWinModelCraft , Model model, HttpSession session){
        DataInfo dataInfo=new DataInfo();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");
            mesWinModelCraft.setUpdateBy(mesUsers.getUsername());
            mesWinModelCraft.setUpdateDate(simpleDateFormat.format(new Date()));
            int number=mesWinModelCraftService.update(mesWinModelCraft);
            if(number>0){
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

    /**
     *  审核弹窗
     * @param model
     * @param winId
     * @return
     */
    @RequestMapping("audit")
    public  String audit(Model model, String winId ){
        model.addAttribute("winId",winId);
        return  "mes_win_model_craft/audit";
    }


    /**
     * 审核保存
     * @param model
     * @param winId
     * @return
     */
    @ResponseBody
    @RequestMapping("auditSave")
    public  DataInfo check(Model model, String winId,String remark,String status ,HttpSession session){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DataInfo dataInfo=new DataInfo();
        try {
            MesWinModel mesWinModel= mesWinModelService.find(winId);
            if(mesWinModel.getStatus().equals("1")){
                dataInfo.setStatus(false);
                dataInfo.setMsg("该窗型已审核通过");
                return dataInfo;
            }
      /*  int sum=mesWinModelService.findSum(mesWinModel.getWinId());
        if(sum != 100 ){
            dataInfo.setStatus(false);
            dataInfo.setMsg("面积比例有误,请核查!");
            return dataInfo;
        }*/
            mesWinModel.setRemark(remark);
            mesWinModel.setStatus(status);
            mesWinModel.setAuditDate(simpleDateFormat.format(new Date()));
            MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");
            mesWinModel.setAuditBy(mesUsers.getUsername());
            int numbers=mesWinModelService.del(mesWinModel);
            if(numbers>0){
                dataInfo.setStatus(true);
                dataInfo.setMsg("提交成功");
            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("提交失败");
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }





    @ResponseBody
    @RequestMapping("findTask")
    public DataInfo findTask(MesWinModelCraft mesWinModelCraft , Model model, HttpSession session){
        DataInfo dataInfo=new DataInfo();
        MesWinModelCraft  wmc=mesWinModelCraftService.find(mesWinModelCraft.getId());
        MesWinModel mesWinModel=mesWinModelService.find(wmc.getWinModelId());
        MesTaskDetail mesTaskDetail=new MesTaskDetail();
        mesTaskDetail.setDel("0");
        mesTaskDetail.setTaskWinType(mesWinModel.getCode());
        List<MesTaskDetail> mesTaskDetailList= mesTaskDetailService.find(mesTaskDetail);
        if(mesTaskDetailList.size()>0){
            dataInfo.setStatus(true);
            dataInfo.setMsg("此工序已在作业单中使用.暂不能操作");
            return dataInfo;
        }
        return dataInfo;
    }
}
