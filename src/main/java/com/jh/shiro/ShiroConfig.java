//package com.jh.shiro;
//
//import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
//import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
//import org.apache.shiro.cache.ehcache.EhCacheManager;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
//import org.apache.shiro.web.mgt.CookieRememberMeManager;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.servlet.SimpleCookie;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Properties;
//
//@Configuration
//public class ShiroConfig {
//    @Bean
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager) {
//
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        //设置安全管理器
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//
//        //添加shiro内置过滤器
//        //shiro内置过滤器，可以实现权限相关的拦截
//        //anon:无需认证（登陆）可以访问
//        //authc:必须认证才可以访问
//        //user:如果使用rememberMe的功能可以直接访问
//        //perms：该资源必须得到资源权限才可以访问
//        //role：该资源必须得到角色权限才可以访问
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//
////        filterChainDefinitionMap.put("/logout", "logout");
////        filterChainDefinitionMap.put("/index", "user");
////        filterChainDefinitionMap.put("/", "user");
//        //放行login.html
//        filterChainDefinitionMap.put("/login", "anon");
//        //对所有访问认证
//        filterChainDefinitionMap.put("/**", "authc");
//        //授权过滤器,当前授权拦截后，shiro会自动跳转到未授权页面
////        filterChainDefinitionMap.put("/add","perms[user:add]");
//        //设置未授权页面
////        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
//        //修改拦截后跳转的页面
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        //首页
//        shiroFilterFactoryBean.setSuccessUrl("/index");
//        //错误页面，认证不通过跳转
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return shiroFilterFactoryBean;
//    }
//
//
//    //SecurityManager 是 Shiro 架构的核心，通过它来链接Realm和用户(文档中称之为Subject.)
//    @Bean(name="securityManager")
//    public DefaultWebSecurityManager securityManager(@Qualifier("myShiroRealm") MyShiroRealm myShiroRealm) {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(myShiroRealm); //将Realm注入到SecurityManager中。
//        securityManager.setCacheManager(ehCacheManager()); //注入缓存对象。
//        securityManager.setRememberMeManager(cookieRememberMeManager()); //注入rememberMeManager;
//        return securityManager;
//    }
//
//    @Bean(name="myShiroRealm")
//    public MyShiroRealm myShiroRealm() {
//        MyShiroRealm myShiroRealm = new MyShiroRealm();
//        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher()); //设置解密规则
//        return myShiroRealm;
//    }
//
//    //因为我们的密码是加过密的，所以，如果要Shiro验证用户身份的话，需要告诉它我们用的是md5加密的，并且是加密了两次。同时我们在自己的Realm中也通过SimpleAuthenticationInfo返回了加密时使用的盐。这样Shiro就能顺利的解密密码并验证用户名和密码是否正确了。
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher() {
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
//        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
//        return hashedCredentialsMatcher;
//    }
//    /**
//     *  开启shiro aop注解支持.
//     *  使用代理方式;所以需要开启代码支持;
//     * @param securityManager
//     * @return
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }
//    @Bean
//    public SimpleMappingExceptionResolver resolver() {
//        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
//        Properties properties = new Properties();
//        properties.setProperty("org.apache.shiro.authz.UnauthorizedException", "/403");
//        resolver.setExceptionMappings(properties);
//        return resolver;
//    }
//    @Bean
//    public EhCacheManager ehCacheManager() {
//        System.out.println("ShiroConfiguration.getEhCacheManager()");
//        EhCacheManager ehCacheManager = new EhCacheManager();
//        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
//        return ehCacheManager;
//    }
//    //cookie对象;
//    @Bean
//    public SimpleCookie rememberMeCookie() {
//        System.out.println("ShiroConfiguration.rememberMeCookie()");
//        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
//        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
//
//        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
//        simpleCookie.setMaxAge(259200);
//        return simpleCookie;
//    }
//
//    //cookie管理对象;
//    @Bean
//    public CookieRememberMeManager cookieRememberMeManager() {
//        System.out.println("ShiroConfiguration.rememberMeManager()");
//        CookieRememberMeManager manager = new CookieRememberMeManager();
//        manager.setCookie(rememberMeCookie());
//        return manager;
//    }
//
//    //配置ShiroDialect，用于thymeleaf和shiro标签配合使用
//    @Bean
//    public ShiroDialect getShiroDialect(){
//        return new ShiroDialect();
//    }
//}
