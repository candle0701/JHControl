package com.jh.controller;

import com.jh.entity.DataInfo;
import com.jh.entity.MesMenu;
import com.jh.entity.MesMessage;
import com.jh.entity.MesUsers;
import com.jh.service.MesMenuService;
import com.jh.service.MesMessageService;
import com.jh.service.MesUsersService;
import com.jh.utils.MD5;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private MesMenuService menuService;

    @Autowired
    private MesUsersService mesUsersService;

    @Autowired
    private MesMessageService mesMessageService;

    @RequestMapping("/index")
    public String index(Model model,HttpSession session){
        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");

        if(mesUsers == null){
            return "login";
        }else{

            List<MesMessage> listCount = mesMessageService.selectAll("isRead",mesUsers.getId());
            model.addAttribute("count", listCount.size());

            List<MesMenu> result = this.contextLoads(mesUsers);
            model.addAttribute("menuList", result);
            model.addAttribute("mesUsers", mesUsers);
            return "index";
        }

    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("mesUsers");
        return "login";
    }

    @RequestMapping("/")
    public String toLogin() {
        return "login";
    }

    @ResponseBody
    @RequestMapping("/loginAccount_wx")
    public DataInfo loginAccount_wx(String username, String password, Model model, HttpSession session)throws Exception{
        MesUsers mesUsers = mesUsersService.findByUsername(username);

        DataInfo dataInfo = new DataInfo();
        String msg = "";
        if(mesUsers == null){
            dataInfo.setStatus(false);
            dataInfo.setMsg("用户名不存在");
            return dataInfo;
        }else{
            if(mesUsers.getPassword().equals(password)){
                dataInfo.setStatus(true);
                dataInfo.setMsg("验证成功");
                dataInfo.setObj(mesUsers);
                //登陆成功
                return dataInfo;
            }else {
                dataInfo.setStatus(false);
                dataInfo.setMsg("密码错误");
                return dataInfo;
            }
        }
    }

    // 这里如果不写method参数的话，默认支持所有请求，如果想缩小请求范围，还是要添加method来支持get, post等等某个请求。
    @RequestMapping("/loginAccount")
    public String login(String username, String password, Model model, HttpSession session) throws Exception {
//        //1 获取subject
//        Subject subject = SecurityUtils.getSubject();
//        //2 封装用户数据
//        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
//            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//            //3 执行登陆方法
//            try {
//                //执行subject.login后进入MyShiroRealm认证逻辑
//                subject.login(token);
//
//                MesUsers mesUsers = mesUsersService.findByUsername(username);
//                session.setAttribute("mesUsers",mesUsers);
//                //登陆成功
//                return "redirect:index";
//            } catch (UnknownAccountException e) {
//                //登陆失败:用户名不存在
//                model.addAttribute("msg", "用户名不存在");
//                return "login";
//            } catch (IncorrectCredentialsException e) {
//                //登陆失败：用户名错误
//                model.addAttribute("msg","密码错误");
//                return "login";
//            }
//        }else{
//            return "login";
//        }
        MesUsers mesUsers = mesUsersService.findByUsername(username);
        if(mesUsers == null){
            model.addAttribute("msg", "用户名不存在");
            return "login";
        }else{
            if(mesUsers.getPassword().equals(MD5.md5(password))){
                session.setAttribute("mesUsers",mesUsers);
                //登陆成功
                return "redirect:index";
            }else {
                model.addAttribute("msg","密码错误");
                return "login";
            }
        }

    }

    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    @RequestMapping("/403")
    public String unauthorizedRole(){
        System.out.println("------没有权限-------");
        return "403";
    }

    public List<MesMenu> contextLoads(MesUsers mesusers) {
        // 原始的数据
        List<MesMenu> rootMesMenus = menuService.findAllMenuByRoleId(mesusers.getMesRoleUsers().getRoleId());
        // 查看结果
//        for (MesMenu mesMenu : rootMesMenus) {
//            System.out.println(mesMenu);
//        }
        // 最后的结果
        List<MesMenu> mesMenuList = new ArrayList<MesMenu>();
        // 先找到所有的一级菜单
        for (int i = 0; i < rootMesMenus.size(); i++) {
            // 一级菜单没有parentId
            if (StringUtils.isBlank(rootMesMenus.get(i).getExtendsMenuId())) {
                mesMenuList.add(rootMesMenus.get(i));
            }
        }
        // 为一级菜单设置子菜单，getChild是递归调用的
        for (MesMenu mesMenu : mesMenuList) {
            mesMenu.setChildMesMenus(getChild(mesMenu.getId().toString(), rootMesMenus));
        }
        return mesMenuList;
    }

    private List<MesMenu> getChild(String id, List<MesMenu> rootMesMenus) {
        // 子菜单
        List<MesMenu> childList = new ArrayList<>();
        for (MesMenu mesMenu : rootMesMenus) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (StringUtils.isNotBlank(mesMenu.getExtendsMenuId())) {
                if (mesMenu.getExtendsMenuId().equals(id)) {
                    childList.add(mesMenu);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (MesMenu mesMenu : childList) {// 没有url子菜单还有子菜单
            if (StringUtils.isBlank(mesMenu.getUrl())) {
                // 递归
                mesMenu.setChildMesMenus(getChild(mesMenu.getId().toString(), rootMesMenus));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }
}