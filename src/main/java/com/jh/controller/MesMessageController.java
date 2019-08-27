package com.jh.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.entity.DataInfo;
import com.jh.entity.MesMessage;
import com.jh.entity.MesUsers;
import com.jh.service.MesMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("mesMessage")
public class MesMessageController {
    @Autowired
    private MesMessageService mesMessageService;

    @RequestMapping("toList")
    public String toMesCompanyList(Model model, String name, HttpSession session,
                                   @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                                           int pageNum,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                                           int pageSize){
        //引入分页查询，使用PageHelper分页功能
        //在查询之前传入当前页，然后多少记录
        PageHelper.startPage(pageNum,pageSize);
        //startPage后紧跟的这个查询就是分页查询
        List<MesMessage> result = new ArrayList<>();

        MesUsers mesUsers = (MesUsers)session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "login";
        }else{
            if(name==null){
                result =  mesMessageService.selectAll(null,mesUsers.getId());
                //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
                PageInfo<MesMessage> pageInfo = new PageInfo<>(result);
                model.addAttribute("mesMessageList", pageInfo);
                return "mes_message/mes_message_list";
            }else{
                result = mesMessageService.selectAll(null,mesUsers.getId());
                //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
                PageInfo<MesMessage> pageInfo = new PageInfo<>(result);
                model.addAttribute("mesMessageList", pageInfo);
                return "mes_message/mes_message_list::table_refresh";
            }
        }
    }

//    @RequestMapping("getCount")
//    public String getCount(HttpSession session,Model model){
//        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
//        List<MesMessage> listCount = mesMessageService.selectAll("isRead",mesUsers.getId());
//        model.addAttribute("count", listCount.size());
//        return "index::isRead";
//    }

    @ResponseBody
    @RequestMapping("setReadAll")
    public DataInfo setRead(HttpSession session){
        MesUsers mesUsers = (MesUsers)session.getAttribute("mesUsers");

        DataInfo dataInfo = new DataInfo();

        if(mesUsers == null){
            dataInfo.setStatus(false);
            dataInfo.setMsg("获取用户信息失效,请刷新页面重新登陆");
        }else{
            int i = mesMessageService.setRead(null,mesUsers.getId());

            if(i>0){
                dataInfo.setStatus(true);
                dataInfo.setMsg("设置成功");
            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("设置失败");
            }
        }

        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("setReadSelect")
    public DataInfo setReadSelect(String id,HttpSession session){
        MesUsers mesUsers = (MesUsers)session.getAttribute("mesUsers");

        DataInfo dataInfo = new DataInfo();
        if(mesUsers == null){
            dataInfo.setStatus(false);
            dataInfo.setMsg("获取用户信息失效,请刷新页面重新登陆");
        }else {
            int i = mesMessageService.setRead(id, mesUsers.getId());

            if (i > 0) {
                dataInfo.setStatus(true);
                dataInfo.setMsg("设置成功");
            } else {
                dataInfo.setStatus(false);
                dataInfo.setMsg("设置失败");
            }
        }
        return dataInfo;
    }
}
