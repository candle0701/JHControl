package com.jh.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.entity.*;
import com.jh.service.*;
import com.jh.utils.UUIDUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("mesRole")
public class MesRoleController {

    private static Logger log = LoggerFactory.getLogger(MesRoleController.class);

    @Autowired
    private MesRoleService mesRoleService;

    @Autowired
    private MesButtonService mesButtonService;

    @Autowired
    private MesMenuService mesMenuService;

    @Autowired
    private MesMenuButtonService mesMenuButtonService;

    @Autowired
    private MesRoleMenuService mesRoleMenuService;

    @RequestMapping("toMesRoleList")
    public String toMesRoleList(Model model,String roleName,
                                 @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                                         int pageNum,
                                 @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                                             int pageSize){
        //引入分页查询，使用PageHelper分页功能
        //在查询之前传入当前页，然后多少记录
        PageHelper.startPage(pageNum,pageSize);
        //startPage后紧跟的这个查询就是分页查询
        List<MesRole> result = new ArrayList<>();
        if(roleName==null){
            result =  mesRoleService.findAllRole(null);
            //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
            PageInfo<MesRole> pageInfo = new PageInfo<>(result);
            model.addAttribute("mesRoleList", pageInfo);
            return "mes_role/mes_role_list";
        }else{
            result = mesRoleService.searchByRoleName(roleName);
            //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
            PageInfo<MesRole> pageInfo = new PageInfo<>(result);
            model.addAttribute("mesRoleList", pageInfo);
            return "mes_role/mes_role_list::table_refresh";
        }
    }

    //打开编辑页面
    @RequestMapping("toMesRoleEdit")
    public String toMesRoleEdit(String id,Model model){
        MesRole mesRole = mesRoleService.selectByPrimaryKey(id);
        model.addAttribute("mesRole", mesRole);
        return "mes_role/mes_role_edit";
    }

    public boolean RoleIfExists(String roleName,String roleId){

        Boolean boo = true;

        //如果已经存在相同名称，无法添加或修改
        List<MesRole> list = mesRoleService.findAllRole(roleId);
        if(list.size() > 0 ){
            for(MesRole mes:list){
                if(roleName.equals(mes.getRoleName())){
                    boo = true;
                    break;
                }else{
                    boo = false;
                }
            }
        }else{
            boo = false;
        }

        return boo;
    }

    //编辑
    @RequestMapping("mesRoleEdit")
    @ResponseBody
    public DataInfo mesRoleEdit(String id, String roleName,String description){

        DataInfo dataInfo = new DataInfo();

        if(RoleIfExists(roleName,id)){
            dataInfo.setStatus(false);
            dataInfo.setMsg("已存在相同角色名");
        }else{
            MesRole mesRole = new MesRole();
            mesRole.setId(id);
            mesRole.setRoleName(roleName);
            mesRole.setDescription(description);

            int i = mesRoleService.updateByPrimaryKeySelective(mesRole);

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

    //打开新增页面
    @RequestMapping("toMesRoleAdd")
    public String toMesRoleAdd(){
        return "mes_role/mes_role_add";
    }

    //新增
    @RequestMapping("mesRoleAdd")
    @ResponseBody
    public DataInfo mesRoleAdd(String id,Model model,String roleName,String description){

        DataInfo dataInfo = new DataInfo();
        if(RoleIfExists(roleName,null)){
            dataInfo.setStatus(false);
            dataInfo.setMsg("已存在相同角色名");
        }else{
            MesRole mesRole = new MesRole();

            int i = 0;

            String uuid = UUIDUtil.getUUID();
            mesRole.setId(uuid);
            mesRole.setRoleName(roleName);
            mesRole.setDescription(description);

            i = mesRoleService.insertSelective(mesRole);

            if(i>0){
                //角色创建成功后，为该角色分配所有菜单以及菜单下按钮
                List<MesMenu> mlist = mesMenuService.getAllMenu();
                for(MesMenu menu : mlist){
                    MesRoleMenu mrm = new MesRoleMenu();
                    mrm.setStatus("0");
                    mrm.setRoleId(uuid);
                    mrm.setId(UUIDUtil.getUUID());
                    mrm.setMenuId(menu.getId());
                    mesRoleMenuService.insertSelective(mrm);
                }
                List<MesMenuButton> blist = mesMenuButtonService.getAllButton();
                for(MesMenuButton button : blist){
                    MesMenuButton mmb = new MesMenuButton();
                    mmb.setStatus("0");
                    mmb.setRoleId(uuid);
                    mmb.setId(UUIDUtil.getUUID());
                    mmb.setMenuId(button.getMenuId());
                    mmb.setButtonId(button.getButtonId());
                    mesMenuButtonService.insertSelective(mmb);
                }

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

        int i = mesRoleService.deleteByPrimaryKey(id);
        //删除操作，删除MesMenuButton和MesRoleMenu对应的roleid数据
        mesMenuButtonService.deleteByRoleId(id);
        mesRoleMenuService.deleteByRoleId(id);

        if(i>0){
            dataInfo.setStatus(true);
            dataInfo.setMsg("删除成功");
        }else{
            dataInfo.setStatus(false);
            dataInfo.setMsg("删除失败");
        }

        return dataInfo;
    }


    //=================================分配权限提交begin=====================================================
    //在前台提交时，一个提交动作，分了两个方法，待优化
    //按钮权限提交
    @ResponseBody
    @RequestMapping("submitPermissionMenu")
    public DataInfo submitPermissionMenu(String id,String status,String roleId){

        //更新菜单
        MesRoleMenu mesRoleMenu = new MesRoleMenu();
        mesRoleMenu.setId(id);
        mesRoleMenu.setStatus(status);

        int i = mesRoleMenuService.updateByPrimaryKeySelective(mesRoleMenu);

        //更新所选菜单的父菜单
        if("1".equals(status)){
            MesMenu mesMenu = mesMenuService.getParentMenu(id);
            MesRoleMenu parentMenu = new MesRoleMenu();
            parentMenu.setRoleId(roleId);
            parentMenu.setMenuId(mesMenu.getExtendsMenuId());
            mesRoleMenuService.updateByroleIdAndMenuId(parentMenu);
        }

        DataInfo dataInfo = new DataInfo();
        if(i>0){
            dataInfo.setStatus(true);
            dataInfo.setMsg("菜单更新成功");
        }else{
            dataInfo.setStatus(false);
            dataInfo.setMsg("菜单更新失败");
        }

        return dataInfo;
    }

    //按钮权限提交
    @ResponseBody
    @RequestMapping("submitPermissionButton")
    public DataInfo submitPermissionButton(String id,String status){

        //按钮更新
        MesMenuButton mesMenuButton = new MesMenuButton();
        mesMenuButton.setId(id);
        mesMenuButton.setStatus(status);

        int i = mesMenuButtonService.updateByPrimaryKeySelective(mesMenuButton);

        DataInfo dataInfo = new DataInfo();
        if(i>0){
            dataInfo.setStatus(true);
            dataInfo.setMsg("按钮更新成功");
        }else{
            dataInfo.setStatus(false);
            dataInfo.setMsg("按钮更新失败");
        }

        return dataInfo;
    }
    //=================================分配权限提交end=====================================================

    //根据当前角色打开权限页面
    @RequestMapping("toMesRolePemission")
    public String toMesRolePemission(String id,Model model){
        model.addAttribute("id", id);

        //获取id所有子菜单及当前菜单页面下的按钮
        List<MesMenu> menuList = mesMenuService.findMenuParentIdIsNotNull(id);
        List<MesMenu> list = new ArrayList<>();
        for(MesMenu menu:menuList){
            //获取所有按钮
            List<MesButton> buttonList = mesButtonService.getAllButton(menu.getId(),menu.getMrmRoleId());
            menu.setButtonList(buttonList);
            list.add(menu);
        }
        model.addAttribute("menuList2", list);

        //获取所有菜单
        List<MesMenu> menuList2 = this.getMenu(id);
        model.addAttribute("menuList", menuList2);

        return "mes_role/mes_role_pemission";
    }

    public List<MesMenu> getMenu(String id) {
        // 原始的数据
        List<MesMenu> rootMesMenus = mesMenuService.findPermissionMenuByRoleId(id);

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
