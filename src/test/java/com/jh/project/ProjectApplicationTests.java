package com.jh.project;

import com.jh.entity.MesMenu;
import com.jh.service.MesMenuService;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectApplicationTests {

    @Autowired
    private MesMenuService menuService;

    @Test
    public void contextLoads() {
        // 原始的数据
		List<MesMenu> rootMesMenus = menuService.findAllMenuByRoleId("1");
		// 查看结果
	  	for (MesMenu mesMenu : rootMesMenus) {
			System.out.println(mesMenu);
	 	}
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
		 Map<String,Object> jsonMap = new HashMap<>();
		 jsonMap.put("menu", mesMenuList);
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
