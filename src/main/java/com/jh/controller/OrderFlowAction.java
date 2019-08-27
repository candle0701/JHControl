package com.jh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orderFlow")
public class OrderFlowAction {

    //打开一个创建采购单的页面
    @RequestMapping("/addOrder")
    public String addOrder(Model model) throws Exception {
        return "order/addOrder";
    }

    //保存采购单信息
    @RequestMapping("/addOrderSubmit")
    public String addOrderSubmit() throws Exception {
        return "order/addOrder";
    }

    //采购单列表
    @RequestMapping("queryOrderList")
    public String queryOrderList(Model model) {
        return "order/queryOrderList";
    }
}
