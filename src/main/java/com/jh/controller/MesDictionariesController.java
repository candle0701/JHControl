package com.jh.controller;

import com.jh.entity.MesDictionaries;
import com.jh.service.MesDictionariesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("mesProjectDetailLog")
public class MesDictionariesController {
    @Autowired
    private MesDictionariesService  mesDictionariesService;



}
