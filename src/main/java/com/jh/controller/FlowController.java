package com.jh.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

//activiti流程管理
@Controller("/flow")
public class FlowController {

    //注入activiti的service
    @Autowired
    public RepositoryService repositoryService;

    @Value("${purchasingProcessDefinitionKey}")
    //流程定义key
    private String purchasingProcessDefinitionKey;

    //部署流程定义
    @RequestMapping("/deployProcess")
    public String deployProcess(Model model) {
        return "flow/deployProcess";
    }

    //部署流程提交
    @RequestMapping("/deploySubmit")
    public String deploySubmit(MultipartFile bpmn, MultipartFile png) throws IOException {

        // 通过activiti的repositoryService进行流程定义部署

        // bpmn文件
        InputStream inputStream_bpmn = bpmn.getInputStream();
        String bpmn_name = bpmn.getOriginalFilename();
        // png文件
        InputStream inputStream_png = png.getInputStream();
        String png_name = png.getOriginalFilename();

        Deployment deployment = repositoryService.createDeployment()
                .addInputStream(bpmn_name, inputStream_bpmn)
                .addInputStream(png_name, inputStream_png)
                .deploy();

        System.out.println("部署id：" + deployment.getId());

        System.out.println("部署时间：" + deployment.getDeploymentTime());

        //转到流程定义查询页面
        return "flow/queryProcessDefinition";
    }

    //流程定义查询页面
    @RequestMapping("/queryProcessDefinition")
    public String queryProcessDefinition(HttpServletRequest request, Model model) throws Exception {

        String path = this.getClass().getClassLoader().getResource("").getFile();
        //处理文件名中的空格，中文等
        path = URLDecoder.decode(path, "utf-8");

        //流程定义查询对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        //设置查询条件
        processDefinitionQuery.processDefinitionKey(purchasingProcessDefinitionKey);

        //得出查询列表
        List<ProcessDefinition> list = processDefinitionQuery.list();
        model.addAttribute("list", list);
//		for(ProcessDefinition processDefinition:list){
//			System.out.println("--------------------------------");
//			System.out.println("流程定义的id："+processDefinition.getId());
//			System.out.println("流程定义的名称："+processDefinition.getName());
//			System.out.println("流程定义的key："+processDefinition.getKey());
//			System.out.println("流程部署id："+processDefinition.getDeploymentId());
//			System.out.println("bpmn文件名："+processDefinition.getResourceName());
//			System.out.println("png文件名："+processDefinition.getDiagramResourceName());
//		}

        return "flow/queryProcessDefinition";
    }

    //流程定义删除
    @RequestMapping("/deleteDeployment")
    public String deleteProcessDefinition(String deploymentId) {

        //如果流程已经启动，无法删除
        //repositoryService.deleteDeployment(deploymentId);

        //通过级联删除将流程定义及相关所有内容全部删除，不限制流程是否启动
        repositoryService.deleteDeployment(deploymentId, true);

        return "flow/queryProcessDefinition";
    }

    //资源文件查看
    @RequestMapping("/queryResource")
    public void queryResource(HttpServletResponse response, String processDefinitionId, String resourceType) throws Exception {
        // 查询一个流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();

        // 部署id，来源于流程部署表
        String deploymentId = processDefinition.getDeploymentId();

        //资源名称
        String resourceName = null;

        if (resourceType.equals("bpmn")) {
            resourceName = processDefinition.getResourceName();
        } else {
            resourceName = processDefinition.getDiagramResourceName();
        }

        //资源输入流
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);

        byte[] b = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
}




















