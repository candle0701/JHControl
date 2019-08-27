package com.jh.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Controller
@RequestMapping("FileUpload")
public class FileUpload {


    @RequestMapping("/upload")
    @ResponseBody
    public JSONObject importFile(@RequestParam("file") MultipartFile file, HttpServletRequest request)  throws IOException {
        JSONObject res=new JSONObject();
      /*  String path = request.getServletContext().getRealPath("/uploadFile/");*/
        String path = "E:/deploy/uploadFile/";  //上传后的路径
        System.out.println("文件名称"+file.getOriginalFilename());
        String name = file.getOriginalFilename();//上传文件的真实名称

        String suffixName = name.substring(name.lastIndexOf("."));//获取后缀名
        String hash = Integer.toHexString(new Random().nextInt());//自定义随机数（字母+数字）作为文件名
        String fileName = UUID.randomUUID().toString().replace("-", "")+hash + suffixName;
        File filepath = new File(path, fileName);
        System.out.println("随机数文件名称"+filepath.getName());
        //判断路径是否存在，没有就创建一个
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        //将上传文件保存到一个目标文档中
        File tempFile = new File(path  + fileName);

        file.transferTo(tempFile);
        res.put("code", 0);
        res.put("msg", "");
        String url =  request.getScheme()+"://"+ request.getServerName()+":"+ request.getServerPort()+"/uploadFile/"+fileName;
        res.put("src",url);
        return res;
    }

}
