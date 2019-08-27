package com.jh.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.entity.*;
import com.jh.service.MesMenuService;
import com.jh.service.MesRoleService;
import com.jh.service.MesRoleUsersService;
import com.jh.service.MesUsersService;
import com.jh.utils.MD5;
import com.jh.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("mesUsers")
public class MesUsersController {

    private static Logger log = LoggerFactory.getLogger(MesUsersController.class);

    @Autowired
    private MesUsersService mesUsersService;

    @Autowired
    private MesRoleService mesRoleService;

    @Autowired
    private MesRoleUsersService mesRoleUsersService;

    @Autowired
    private MesMenuService mesMenuService;

    @RequestMapping("toMesUsersList")
    public String toMesUsersList(Model model,String nickname,
                                 @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                                         int pageNum,
                                 @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                                             int pageSize){
        //引入分页查询，使用PageHelper分页功能
        //在查询之前传入当前页，然后多少记录
        PageHelper.startPage(pageNum,pageSize);
        //startPage后紧跟的这个查询就是分页查询
        List<MesUsers> result = new ArrayList<>();
        if(nickname==null){
            result =  mesUsersService.findAllUsers(null,null);
            //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
            PageInfo<MesUsers> pageInfo = new PageInfo<>(result);
            model.addAttribute("mesUsersList", pageInfo);
            return "mes_users/mes_users_list";
        }else{
            result = mesUsersService.searchByUsername(nickname);
            //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
            PageInfo<MesUsers> pageInfo = new PageInfo<>(result);
            model.addAttribute("mesUsersList", pageInfo);
            return "mes_users/mes_users_list::table_refresh";
        }
    }

    //打开用户编辑页面
    @RequestMapping("toMesUsersEdit")
    public String toMesUsersEdit(String id,Model model){
        MesUsers mesUsers = mesUsersService.selectByPrimaryKey(id);
        model.addAttribute("mesUsers", mesUsers);
        return "mes_users/mes_users_edit";
    }

    public boolean usersIfExists(String username,String userId){

        Boolean boo = true;

        //如果已经存在相同部门名称，无法添加或修改
        List<MesUsers> list = mesUsersService.findAllUsers(userId,"addorEdit");
        if(list.size() > 0){
            for(MesUsers mes:list){
                if(username.equals(mes.getUsername())){
                    boo = true;
                    break;
                }else{
                    boo = false;
                }
            }
        }else {
            boo = false;
        }

        return boo;
    }

    @ResponseBody
    @RequestMapping("check")
    public DataInfo check(String username,String id){
        DataInfo dataInfo = new DataInfo();

        if(usersIfExists(username,id)){
            dataInfo.setStatus(false);
            dataInfo.setMsg("当前员工编号已使用");
        }else{
            dataInfo.setStatus(true);
        }
        return dataInfo;
    }

    @RequestMapping("toEditPassword")
    public String toEditPassword(String id, Model model, HttpSession session){
        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "login";
        }else{

            model.addAttribute("mesUsers",mesUsers);
            return "mes_users/edit_password";
        }
    }

    @ResponseBody
    @RequestMapping("editPassword")
    public DataInfo editPassword(String id,String newPassword){
        MesUsers mesUsers = new MesUsers();
        mesUsers.setId(id);
        try {
            mesUsers.setPassword(MD5.md5(newPassword));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i = mesUsersService.updateByPrimaryKeySelective(mesUsers);
        DataInfo dataInfo = new DataInfo();
        if(i>0){
            dataInfo.setStatus(true);
            dataInfo.setMsg("修改密码成功");
        }else{
            dataInfo.setStatus(false);
            dataInfo.setMsg("修改密码失败");
        }
        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("resertPassword")
    public DataInfo resertPassword(String id){

        MesUsers mesUsers = new MesUsers();
        mesUsers.setId(id);
        try {
            mesUsers.setPassword(MD5.md5("123456"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int i = mesUsersService.updateByPrimaryKeySelective(mesUsers);
        DataInfo dataInfo = new DataInfo();
        if(i>0){
            dataInfo.setStatus(true);
            dataInfo.setMsg("重置密码成功");
        }else{
            dataInfo.setStatus(false);
            dataInfo.setMsg("重置密码失败");
        }
        return dataInfo;
    }

    //用户编辑
    @RequestMapping("mesUsersEdit")
    @ResponseBody
    public DataInfo mesUsersEdit(String id, String username,String address,String tel,String nickname,MesUsers mesUsers){

        DataInfo dataInfo = new DataInfo();

        if(usersIfExists(username,id)){
            dataInfo.setStatus(false);
            dataInfo.setMsg("当前员工编号已使用");
        }else{

            int i = mesUsersService.updateByPrimaryKeySelective(mesUsers);

            if(i>0){
                dataInfo.setStatus(true);
                dataInfo.setMsg("编辑成功");
            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("编辑失败");
            }
        }
        return dataInfo;
    }

    //================================分配角色begin=================================================
    //打开分配角色页面
    @RequestMapping("toMesUsersRole")
    public String toMesUsersRole(String id,Model model){
        model.addAttribute("id", id);

        MesRoleUsers mru = mesRoleUsersService.findRoleUsersByUsersId(id);
        model.addAttribute("mru",mru);

        //获取所有角色
        List<MesRole> roleList = mesRoleService.findAllRole(null);
        model.addAttribute("roleList", roleList);

        return "mes_users/mes_users_role";
    }
    //=================================分配角色end=================================================
    //分配权限
    @ResponseBody
    @RequestMapping("assignmentRole")
    public DataInfo assignmentRole(String userId,String roleId){
        //查看当前用户id是否存在角色列表内，如果存在就更新数据(true)，如果不存在就增加数据(false)
        List<MesRoleUsers> roleList = mesRoleUsersService.findAllRoleUsers();
        Boolean flag = true;
        for(MesRoleUsers roleUsers:roleList){
            if(userId.equals(roleUsers.getUsersId())){
                flag = true;
                break;
            }else{
                flag = false;
            }
        }
        MesRoleUsers msu = new MesRoleUsers();
        msu.setRoleId(roleId);
        msu.setUsersId(userId);

        DataInfo dataInfo = new DataInfo();
        int i = 0 ;

        if(flag){//更新
            i = mesRoleUsersService.updateByUserId(msu);
        }else{//增加
            msu.setId(UUIDUtil.getUUID());
            i = mesRoleUsersService.insertSelective(msu);
        }
        if(i>0){
            dataInfo.setStatus(true);
            dataInfo.setMsg("分配角色成功");
        }else{
            dataInfo.setStatus(false);
            dataInfo.setMsg("分配角色失败");
        }
        return dataInfo;
    }
    //打开用户新增页面
    @RequestMapping("toMesUsersAdd")
    public String toMesUsersAdd(){
        return "mes_users/mes_users_add";
    }

    //用户新增
    @RequestMapping("mesUsersAdd")
    @ResponseBody
    public DataInfo mesUsersAdd(String id,Model model,String username,String address,String tel,String nickname,MesUsers mesUsers){

        DataInfo dataInfo = new DataInfo();
        if(usersIfExists(username,null)){
            dataInfo.setStatus(false);
            dataInfo.setMsg("当前员工编号已使用");
        }else{

            int i = 0;

            mesUsers.setId(UUIDUtil.getUUID());
            try {
                mesUsers.setPassword(MD5.md5("123456"));
                mesUsers.setValid("0");
            } catch (Exception e) {
                e.printStackTrace();
            }

            i = mesUsersService.insertSelective(mesUsers);

            if(i>0){
                dataInfo.setStatus(true);
                dataInfo.setMsg("新增成功");
            }else{
                dataInfo.setStatus(false);
                dataInfo.setMsg("新增失败");
            }
        }

        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("deleteByPrimaryKey")
    public DataInfo deleteByPrimaryKey(String id){

        DataInfo dataInfo = new DataInfo();

        MesUsers mesUsers = new MesUsers();
        mesUsers.setId(id);
        mesUsers.setValid("1");

//        int i = mesUsersService.deleteByPrimaryKey(id);
//        mesRoleUsersService.deleteByUsersId(id);
        int i = mesUsersService.updateByPrimaryKeySelective(mesUsers);

        if(i>0){
            dataInfo.setStatus(true);
            dataInfo.setMsg("删除成功");
        }else{
            dataInfo.setStatus(false);
            dataInfo.setMsg("删除失败");
        }

        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("getMenuNameWx")
    public List<MesMenu> getMenuNameWx(String roleId,String usersId){
        return mesMenuService.getMenuNameWx(roleId, usersId);
    }
}
