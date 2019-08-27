package com.jh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.entity.MesProjectDetailLog;
import com.jh.service.MesProjectDetailLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("mesProjectDetailLog")
public class MesProjectDetailLogController {
    @Autowired
    private MesProjectDetailLogService mesProjectDetailLogService;


    @ResponseBody
    @RequestMapping("pageList")
    public JSONObject list(Model model, MesProjectDetailLog mesProjectDetailLog, int page, int limit){
        try {
            PageHelper.startPage(page,limit);
            List<MesProjectDetailLog> list=mesProjectDetailLogService.list(mesProjectDetailLog);
            PageInfo<MesProjectDetailLog> pageInfo = new PageInfo<>(list);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("msg","");
            jsonObject.put("code",0);
            jsonObject.put("count",pageInfo.getTotal());
            JSONArray array= JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
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
}
