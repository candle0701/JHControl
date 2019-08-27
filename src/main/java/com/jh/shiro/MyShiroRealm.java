//package com.jh.shiro;
//
//import com.jh.entity.MesMenu;
//import com.jh.entity.MesRoleUsers;
//import com.jh.entity.MesUsers;
//import com.jh.service.MesMenuService;
//import com.jh.service.MesRoleUsersService;
//import com.jh.service.MesUsersService;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.SimpleAuthenticationInfo;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.apache.shiro.util.ByteSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//public class MyShiroRealm extends AuthorizingRealm {
//    @Autowired
//    private MesUsersService usersService;
//    @Autowired
//    private MesRoleUsersService roleUsersService;
//    @Autowired
//    private MesMenuService menuService;
//
//    //授权
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
//        System.out.println("=====执行授权逻辑=====");
//        //给资源授权
//        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        MesUsers users  = (MesUsers)principal.getPrimaryPrincipal();
//        //实际：需要根据当前用户的角色和功能权限来构建一个授权信息对象，交给安全管理器
//
//        if (users.getUsername().equals("admin")) {
//            //如果是超级管理员
//            //查询出所有的角色，给认证信息对象
//            List<MesRoleUsers> mesRoleUsersList = roleUsersService.findRoleUsersIdById(users.getId());
//            for (MesRoleUsers mesRoleUsers : mesRoleUsersList) {
//                authorizationInfo.addRole(mesRoleUsers.getRoleId().toString());
//            }
//            //查询出所有的功能权限，给认证对象
//            List<MesMenu> mesMenuList = menuService.findAllMenuByRoleId(users.getId());
//            for (MesMenu mesMenu : mesMenuList) {
//                authorizationInfo.addStringPermission(mesMenu.getPermission());
//            }
//        } else {
//            //如果是普通用户
//            List<MesRoleUsers> mesRoleUsersList = roleUsersService.findRoleUsersIdById(users.getId());
//            for (MesRoleUsers mesRoleUsers : mesRoleUsersList) {
//                authorizationInfo.addRole(mesRoleUsers.getRoleId().toString());
//                //导航查询,获取某角色的拥有的功能权限
//                List<MesMenu> mesMenuList = menuService.findAllMenuByRoleId(mesRoleUsers.getRoleId());
//                for (MesMenu mesMenu : mesMenuList) {
//                    authorizationInfo.addStringPermission(mesMenu.getPermission());
//                }
//            }
//        }
//
//        return authorizationInfo;
//    }
//
//    //认证
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
//        System.out.println("======执行认证逻辑======");
//        //获取用户的输入的账号.
//        String username = (String)token.getPrincipal();
//        //通过username从数据库中查找 User对象，如果找到，没找到.
//        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
//        MesUsers users = usersService.findByUsername(username);
//        if(users == null){
//            ////没有返回登录用户名对应的SimpleAuthenticationInfo对象时,就会在LoginController中抛出UnknownAccountException异常
//            return null;
//        }
//        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
//                users, //用户名
//                users.getPassword(), //密码
//                ByteSource.Util.bytes(users.getCredentialsSalt()),//salt=username+salt
//                getName()  //realm name
//        );
//        return authenticationInfo;
//    }
//}
