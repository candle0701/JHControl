package com.jh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.entity.*;
import com.jh.service.MesMenuButtonService;
import com.jh.service.MesProjectDetailLogService;
import com.jh.service.MesProjectDetailService;
import com.jh.service.MesProjectService;
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
import java.util.UUID;

@Controller
@RequestMapping("mesProjectDetail")
public class MesProjectDetailController {

    @Autowired
    private MesProjectDetailService mesProjectDetailService;

    @Autowired
    private MesProjectDetailLogService mesProjectDetailLogService;

    @Autowired
    private MesProjectService mesProjectService;


    @Autowired
    private MesMenuButtonService mesMenuButtonService;
    @RequestMapping("pdList")
    public String pdList( Model model,String menuId,HttpSession session){

        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "redirect:/";
        }else {
            List<MesMenuButton> menuButtonList = mesMenuButtonService.getButtonByRoleIdAndMenuId(mesUsers.getMesRoleUsers().getRoleId(), menuId);
            model.addAttribute("menuButtonList", menuButtonList);

            MesProject mesProject = new MesProject();
            List<MesProject> projectList = mesProjectService.findAllProject(mesProject);
            model.addAttribute("projectList", projectList);
            return "mes_project_detail/mes_project_detail_pdlist";
        }
    }

    @ResponseBody
    @RequestMapping("pagePdList")
    public JSONObject pagePdList( Model model,int page,int limit,MesProjectDetail mesProjectDetail){
        try {
            JSONObject jsonObject=new JSONObject();
            List<MesProjectDetail> list=mesProjectDetailService.findList(mesProjectDetail);
            List<MesProjectDetail> pageList=mesProjectDetailService.pageList(mesProjectDetail,(page-1)*limit,page*limit);
            JSONArray array= JSONArray.parseArray(JSON.toJSONString(pageList));
            jsonObject.put("msg","");
            jsonObject.put("code",0);
            jsonObject.put("count",list.size());
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

    @RequestMapping("form")
    public String form(MesProjectDetail mesProjectDetail, Model model){
        model.addAttribute("mesProjectDetail",mesProjectDetail);
        return "mes_project_detail/mes_project_detail_from";
    }

    @ResponseBody
    @RequestMapping("find")
    public DataInfo find(MesProjectDetail mesProjectDetail){
        DataInfo dataInfo=new DataInfo();
        try {
            List<MesProjectDetail> list=mesProjectDetailService.findList(mesProjectDetail);
            if(list.size()>0){
                dataInfo.setStatus(true);
                dataInfo.setMsg("窗号已存在.");
            }
        } catch (Exception e) {
            dataInfo.setStatus(true);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("getMesProjectDetail")
    public List<MesProjectDetail> getMesProjectDetail(MesProjectDetail mesProjectDetail){
        return mesProjectDetailService.find(mesProjectDetail);
    }

    @ResponseBody
    @RequestMapping("findList")
    public DataInfo findList(MesProjectDetail mesProjectDetail){
        DataInfo dataInfo= null;
        try {
            dataInfo = new DataInfo();
            mesProjectDetail.setVer("0");
            List<MesProjectDetail> list=mesProjectDetailService.findList(mesProjectDetail);
            if(list.size()>0){
                    dataInfo.setStatus(true);
                    dataInfo.setMsg("窗号已存在.");
            }
        } catch (Exception e) {
            dataInfo.setStatus(true);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }

    @RequestMapping("edit")
    public String edit(MesProjectDetail mesProjectDetail, Model model){
        List<MesProjectDetail> list=mesProjectDetailService.findList(mesProjectDetail);
        model.addAttribute("mesProjectDetail",list.get(0));
        return "mes_project_detail/mes_project_detail_edit";
    }






    @ResponseBody
    @RequestMapping("pageList")
    public DataInfo pageList(MesProjectDetail mesProjectDetail){
        DataInfo dataInfo=new DataInfo();
        try {
            mesProjectDetail.setVer("0");
            List<MesProjectDetail> list=mesProjectDetailService.findList(mesProjectDetail);
            if(list.size()>0){
                dataInfo.setStatus(true);
                dataInfo.setMsg("窗号已存在.");
            }
        } catch (Exception e) {
            dataInfo.setStatus(true);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }


    @ResponseBody
    @RequestMapping("findAll")
    public DataInfo findAll(MesProjectDetail mesProjectDetail){
        DataInfo dataInfo=new DataInfo();
        try {
            mesProjectDetail.setVer("0");
            List<MesProjectDetail> list=mesProjectDetailService.findAll(mesProjectDetail);
            if(list.size()>0){
                dataInfo.setStatus(true);
                dataInfo.setMsg("窗号已存在.");
            }
        } catch (Exception e) {
            dataInfo.setStatus(true);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("list")
    public JSONArray list(MesProjectDetail mesProjectDetail){
        mesProjectDetail.setVer("0");
        List<MesProjectDetail> list=mesProjectDetailService.findList(mesProjectDetail);
        for (MesProjectDetail projectDetail:list){
            projectDetail.setIds(projectDetail.getId());
        }
        JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));
        return array;
    }
    @ResponseBody
    @RequestMapping("del")
    public void del(MesProjectDetail mesProjectDetail){
        mesProjectDetail.setVer("0");
        List<MesProjectDetail> list=mesProjectDetailService.find(mesProjectDetail);
       for (MesProjectDetail mdp:list){
           mdp.setVer("1");
           mdp.setDel("1");
           mesProjectDetailService.update(mdp);
       }

    }


    @ResponseBody
    @RequestMapping("update")
    public boolean update(MesProjectDetail mesProjectDetail, HttpSession session){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");

        MesProjectDetail projectDetail=new MesProjectDetail();
        projectDetail.setWinNo(mesProjectDetail.getWinNo());
        List<MesProjectDetail> list=mesProjectDetailService.findList(projectDetail);
        mesProjectDetail.setDel("1");
        int count=mesProjectDetailService.update(mesProjectDetail);
        if(count>0){
            MesProjectDetailLog mesProjectDetailLog=new MesProjectDetailLog();
            mesProjectDetailLog.setId(UUID.randomUUID().toString().replace("-", ""));
            mesProjectDetailLog.setWinNo(list.get(0).getWinNo());//窗号
            mesProjectDetailLog.setWinTypeId(list.get(0).getWinTypeId()); //窗型
            mesProjectDetailLog.setNum(list.get(0).getNum());//数量
            mesProjectDetailLog.setBlueprintWidth(list.get(0).getBlueprintWidth());//图纸宽
            mesProjectDetailLog.setDrawingHeight(list.get(0).getDrawingHeight());//图纸高
            mesProjectDetailLog.setWinWidth(list.get(0).getWinWidth());//洞口宽
            mesProjectDetailLog.setWinHeight(list.get(0).getWinHeight());//洞口高
            mesProjectDetailLog.setPreTotal(list.get(0).getPreTotal());//暂估工程量，总量
            mesProjectDetailLog.setPrice(list.get(0).getPrice());//合同单价
            mesProjectDetailLog.setCreateDate(df.format(new Date()));
            mesProjectDetailLog.setStatus("2");
            mesProjectDetailLog.setProjectId(list.get(0).getProjectId());
            mesProjectDetailLog.setCreateBy(mesUsers.getUsername());
            mesProjectDetailLogService.insertSelective(mesProjectDetailLog);
            return  true;
        }else{
            return  false;
        }
    }

    @RequestMapping("history")
    public String history(MesProjectDetail mesProjectDetail, Model model){
        model.addAttribute("projectId",mesProjectDetail.getProjectId());
        return "mes_project_detail/mes_project_detail_list";
    }


    @ResponseBody
    @RequestMapping("historyList")
    public  JSONObject historyList( MesProjectDetail mesProjectDetail,int page,int limit){
        JSONObject jsonObject=new JSONObject();
        try {
            mesProjectDetail.setDel("1");
            mesProjectDetail.setVer("1");
            List<MesProjectDetail> list=mesProjectDetailService.findList(mesProjectDetail);
            List<MesProjectDetail> pageList=mesProjectDetailService.pageList(mesProjectDetail,(page-1)*limit,page*limit);
            JSONArray array= JSONArray.parseArray(JSON.toJSONString(pageList));
            jsonObject.put("msg","");
            jsonObject.put("code",0);
            jsonObject.put("count",list.size());
            jsonObject.put("data",array);
            return jsonObject;
        } catch (Exception e) {

            jsonObject.put("msg","系统错误,请联系管理员");
            jsonObject.put("code",200);
            jsonObject.put("count",0);
            jsonObject.put("data","");
            return jsonObject;
        }
    }


    @ResponseBody
    @RequestMapping("save")
    public  boolean save( MesProjectDetail mesProjectDetail, HttpSession session){
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");
            mesProjectDetail.setId(UUID.randomUUID().toString().replace("-", ""));
            mesProjectDetail.setCreateDate(df.format(new Date()));
            mesProjectDetail.setPartyWinNo(mesProjectDetail.getPartyWinNo().toUpperCase());
            mesProjectDetail.setWinNo(mesProjectDetail.getWinNo().toUpperCase());
            mesProjectDetail.setVer("0");
            mesProjectDetail.setItem("0");
            mesProjectDetail.setItem("0");
            int count =mesProjectDetailService.insertSelective(mesProjectDetail);
            if (count>0){
                MesProjectDetailLog mesProjectDetailLog=new MesProjectDetailLog();
                mesProjectDetailLog.setId(UUID.randomUUID().toString().replace("-", ""));
                mesProjectDetailLog.setWinNo(mesProjectDetail.getWinNo().toUpperCase());//窗号
                mesProjectDetailLog.setWinTypeId(mesProjectDetail.getWinTypeId()); //窗型
                mesProjectDetailLog.setNum(mesProjectDetail.getNum());//数量
                mesProjectDetailLog.setBlueprintWidth(mesProjectDetail.getBlueprintWidth());//图纸宽
                mesProjectDetailLog.setDrawingHeight(mesProjectDetail.getDrawingHeight());//图纸高
                mesProjectDetailLog.setWinWidth(mesProjectDetail.getWinWidth());//洞口宽
                mesProjectDetailLog.setWinHeight(mesProjectDetail.getWinHeight());//洞口高
                mesProjectDetailLog.setPreTotal(mesProjectDetail.getPreTotal());//暂估工程量，总量
                mesProjectDetailLog.setPrice(mesProjectDetail.getPrice());//合同单价
                mesProjectDetailLog.setCreateDate(df.format(new Date()));
                mesProjectDetailLog.setStatus("1");
                mesProjectDetailLog.setProjectId(mesProjectDetail.getProjectId());
                mesProjectDetailLog.setCreateBy(mesUsers.getUsername());
                mesProjectDetailLog.setPartyWinNo(mesProjectDetail.getPartyWinNo().toUpperCase());
                mesProjectDetailLogService.insertSelective(mesProjectDetailLog);
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            return false;
        }

    }


    @ResponseBody
    @RequestMapping("editUpdate")
    public  boolean editUpdate( MesProjectDetail mesProjectDetail, HttpSession session){
        try {
            mesProjectDetail.setPartyWinNo(mesProjectDetail.getPartyWinNo().toUpperCase());
            mesProjectDetail.setWinNo(mesProjectDetail.getWinNo().toUpperCase());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");
            int count =mesProjectDetailService.update(mesProjectDetail);
            if (count>0){
                MesProjectDetailLog mesProjectDetailLog=new MesProjectDetailLog();
                mesProjectDetailLog.setId(UUID.randomUUID().toString().replace("-", ""));
                mesProjectDetailLog.setWinNo(mesProjectDetail.getWinNo().toUpperCase());//窗号
                mesProjectDetailLog.setWinTypeId(mesProjectDetail.getWinTypeId()); //窗型
                mesProjectDetailLog.setNum(mesProjectDetail.getNum());//数量
                mesProjectDetailLog.setBlueprintWidth(mesProjectDetail.getBlueprintWidth());//图纸宽
                mesProjectDetailLog.setDrawingHeight(mesProjectDetail.getDrawingHeight());//图纸高
                mesProjectDetailLog.setWinWidth(mesProjectDetail.getWinWidth());//洞口宽
                mesProjectDetailLog.setWinHeight(mesProjectDetail.getWinHeight());//洞口高
                mesProjectDetailLog.setPreTotal(mesProjectDetail.getPreTotal());//暂估工程量，总量
                mesProjectDetailLog.setPrice(mesProjectDetail.getPrice());//合同单价
                mesProjectDetailLog.setCreateDate(df.format(new Date()));
                mesProjectDetailLog.setStatus("3");
                mesProjectDetailLog.setProjectId(mesProjectDetail.getProjectId());
                mesProjectDetailLog.setCreateBy(mesUsers.getUsername());
                mesProjectDetailLog.setPartyWinNo(mesProjectDetail.getPartyWinNo().toUpperCase());
                mesProjectDetailLogService.insertSelective(mesProjectDetailLog);
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @ResponseBody
    @RequestMapping("delete")
    public  boolean delete( MesProjectDetail mesProjectDetail, HttpSession session){
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");
            List<MesProjectDetail> list =mesProjectDetailService.findList(mesProjectDetail);
            mesProjectDetail.setVer("1");
            mesProjectDetail.setDel("1");
            int count =mesProjectDetailService.update(mesProjectDetail);
            if (count>0){
                mesProjectDetail=list.get(0);
                MesProjectDetailLog mesProjectDetailLog=new MesProjectDetailLog();
                mesProjectDetailLog.setId(UUID.randomUUID().toString().replace("-", ""));
                mesProjectDetailLog.setWinNo(mesProjectDetail.getWinNo().toUpperCase());//窗号
                mesProjectDetailLog.setWinTypeId(mesProjectDetail.getWinTypeId()); //窗型
                mesProjectDetailLog.setNum(mesProjectDetail.getNum());//数量
                mesProjectDetailLog.setBlueprintWidth(mesProjectDetail.getBlueprintWidth());//图纸宽
                mesProjectDetailLog.setDrawingHeight(mesProjectDetail.getDrawingHeight());//图纸高
                mesProjectDetailLog.setWinWidth(mesProjectDetail.getWinWidth());//洞口宽
                mesProjectDetailLog.setWinHeight(mesProjectDetail.getWinHeight());//洞口高
                mesProjectDetailLog.setPreTotal(mesProjectDetail.getPreTotal());//暂估工程量，总量
                mesProjectDetailLog.setPrice(mesProjectDetail.getPrice());//合同单价
                mesProjectDetailLog.setCreateDate(df.format(new Date()));
                mesProjectDetailLog.setStatus("2");
                mesProjectDetailLog.setProjectId(mesProjectDetail.getProjectId());
                mesProjectDetailLog.setCreateBy(mesUsers.getUsername());
                mesProjectDetailLog.setPartyWinNo(mesProjectDetail.getPartyWinNo().toUpperCase());
                mesProjectDetailLogService.insertSelective(mesProjectDetailLog);
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }



    @ResponseBody
    @RequestMapping("mesProjectDetailFind")
    public boolean mesProjectDetailFind(MesProjectDetail mesProjectDetail){
        mesProjectDetail.setDel("0");
        List<MesProjectDetail> list=mesProjectDetailService.find(mesProjectDetail);
       if(list.size()==0){
           return true;
       }
        return false;
    }
}



