package com.jh.controller;

import com.jh.entity.MesUsers;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {
    // 目标方法执行之前
    public boolean preHandle(HttpSession session) throws Exception {

        MesUsers mesUsers = (MesUsers)session.getAttribute("mesUsers");

        // 如果获取的request的session中的loginUser参数为空（未登录），就返回登录页，否则放行访问
        if (mesUsers == null) {
            return false;
        } else {
            // 已登录，放行
            return true;
        }
    }

    public void postHandle(HttpServletRequest request) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
