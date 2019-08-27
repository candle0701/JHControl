package com.jh.project.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

public class FlowTest {

    //创建processEngine
//	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    //1 创建processEngineConfiguration对象
    ProcessEngineConfiguration conf = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("spring-dao.xml");
    //创建processEngine引擎
    ProcessEngine processEngine = conf.buildProcessEngine();

    //部署流程定义
    public void processDeployment() {
        //使用repositoryService
        RepositoryService reporsitoryService = processEngine.getRepositoryService();

        InputStream inputStreamBpmn = this.getClass().getClassLoader().getResourceAsStream("diagram/flow.bpmn");
//		InputStream inputStreamPng = this.getClass().getClassLoader().getResourceAsStream("diagram/flow.png");

        Deployment deploy = reporsitoryService.createDeployment()
                .addInputStream("flow.bpmn", inputStreamBpmn)
//					.addInputStream("flow.png", inputStreamPng)
                .deploy();

        System.out.println("部署id：" + deploy.getId());
        System.out.println("部署时间：" + deploy.getDeploymentTime());
    }

    //启动流程实例,使用runtimeService
    public void startProcessInstance() {
        //创建runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //流程定义的key
        String processDefinitionKey = "flow01";

        //根据流程定义的key启动流程，activiti找到最新版本的流程定义的bpmn文件去启动
//		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

        //启动流程实例时设置流程变量
        //定义流程变量
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("userId", "abc");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);

        System.out.println("流程实例的执行id：" + processInstance.getId());
        System.out.println("流程定义的id：" + processInstance.getProcessInstanceId());
        System.out.println("流程当前运行结点标识：" + processInstance.getActivityId());
        //activiti和业务系统集成后，需要将业务唯一标识存入到activiti中
        //比如采购流程，需要将采购单的id存储到流程实例表中，通过此businessKey关联查询业务系统信息
        System.out.println("业务标识：" + processInstance.getBusinessKey());
        System.out.println("所属流程定义的id：" + processInstance.getProcessDefinitionId());
    }

    //启动一个流程实例，指定业务标识
    public void startProcessInstanceBusinessKey() {

        //businesskey业务系统标识，和流程实例一一对应的业务系统表的主键，比如采购流程的businessKey就是采购单id
        String businessKey = "10002";

        //创建runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //流程定义的key
        String processDefinitionKey = "flowAudit";

        //根据流程定义的key启动流程，activiti找到最新版本的流程定义的bpmn文件去启动
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey);

        System.out.println("流程实例的执行id：" + processInstance.getId());
        System.out.println("流程定义的id：" + processInstance.getProcessInstanceId());
        System.out.println("流程当前运行结点标识：" + processInstance.getActivityId());
        //activiti和业务系统集成后，需要将业务唯一标识存入到activiti中
        //比如采购流程，需要将采购单的id存储到流程实例表中，通过此businessKey关联查询业务系统信息
        System.out.println("业务标识：" + processInstance.getBusinessKey());
        System.out.println("所属流程定义的id：" + processInstance.getProcessDefinitionId());
    }

    //查询当前运行的流程实例
    public void queryProcessInstance() {
        // runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //创建一个查询对象
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();

        //设置查询条件
        //可以按照流程定义的key查询，只查询出某个业务流程的流程实例，不指定这个key查询出所有业务流程的流程实例
        String processDefinitionKey = "flowAudit";
        processInstanceQuery.processDefinitionKey(processDefinitionKey);
        //取出查询记录
        List<ProcessInstance> list = processInstanceQuery.list();
        //分页
        //processInstanceQuery.listPage(firstResult, maxResults)

        for (ProcessInstance processInstance : list) {
            System.out.println("============================");
            System.out.println("流程实例的执行id：" + processInstance.getId());
            System.out.println("流程实例id：" + processInstance.getProcessInstanceId());
            System.out.println("当前流程实例的结点标识：" + processInstance.getActivityId());
            // activiti和业务系统集成后，需要将业务唯一标识存入activiti
            // 比如采购流程，就需要将采购单的id存储到流程实例表中。通过此businessKey关联查询业务系统信息
            System.out.println("业务标识：" + processInstance.getBusinessKey());
            System.out.println("所属流程定义的id："
                    + processInstance.getProcessDefinitionId());

            //通过businessKey关联查询采购单表业务数据
            //得到businesskey
            String businesskey = processInstance.getBusinessKey();
            //通过businesskey调用采购单mapper（业务系统的）mapper查询采购单信息
            //.......根据采购单id查询采购单信息
        }
    }

    //查询待办任务
    public void queryTaskList() {
        //任务负责人
        String assignee = "laoer";

        //流程定义的key
        String processDefinitionKey = "flow";

        //TaskService
        TaskService taskService = processEngine.getTaskService();
        //任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();

        //设置查询条件，指定任务负责
        taskQuery.taskAssignee(assignee);
        //指定流程定义的key
        taskQuery.processDefinitionKey(processDefinitionKey);

        //查询任务列表
        List<Task> list = taskQuery.list();
        //分页查询
//		List<Task> list = taskQuery.listPage(firstResult, maxResults);

        //遍历任务
        for (Task task : list) {
            System.out.println("任务id：" + task.getId());
            System.out.println("任务名称：" + task.getName());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("所属流程实例id：" + task.getProcessInstanceId());
            System.out.println("流程定义的id：" + task.getProcessDefinitionId());
        }
    }

    //办理任务
    public void completeTask() {
        //任务id
        String taskId = "25004";

        //TaskService
        TaskService taskService = processEngine.getTaskService();

        taskService.complete(taskId);
        System.out.println("任务完成！");
    }

    // 流程定义的查询
    public void queryProcessDefinition() {
        //流程定义的key
        String processDefinitionKey = "flow";

        //使用repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //流程定义查询对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        //设置查询条件
        processDefinitionQuery.processDefinitionKey(processDefinitionKey);

        //得出查询列表
        List<ProcessDefinition> list = processDefinitionQuery.list();

        for (ProcessDefinition processDefinition : list) {
            System.out.println("--------------------------------");
            System.out.println("流程定义的id：" + processDefinition.getId());
            System.out.println("流程定义的名称：" + processDefinition.getName());
            System.out.println("流程定义的key：" + processDefinition.getKey());
            System.out.println("流程部署id：" + processDefinition.getDeploymentId());
            System.out.println("bpmn文件名：" + processDefinition.getResourceName());
            System.out.println("png文件名：" + processDefinition.getDiagramResourceName());
        }
    }

    // 流程定义资源文件查看
    public void getProcessResources() throws IOException {

        // 使用repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 流程定义id
        String processDefinitionId = "flow:1:20004";

        // 查询一个流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();

        String bpmn_name = processDefinition.getResourceName();

        String png_name = processDefinition.getDiagramResourceName();

        // 部署id，来源于流程部署表
        String deploymentId = processDefinition.getDeploymentId();

        // 资源文件名称
        String resourceName_bpmn = bpmn_name;

        String resourceName_png = png_name;

        //bmpn的输入流
        InputStream inputStream_bpmn = repositoryService.getResourceAsStream(deploymentId, resourceName_bpmn);

        //png的输入流
        InputStream inputStream_png = repositoryService.getResourceAsStream(deploymentId, resourceName_png);

        //将输入流通过文件输出流写到磁盘
        FileOutputStream fileOutputStream_bpmn = new FileOutputStream(new File("H:/act6/flow.bpmn"));

        FileOutputStream fileOutputStream_png = new FileOutputStream(new File("H:/act6/flow.png"));

        byte[] b = new byte[1024];
        int len = -1;
        while ((len = inputStream_bpmn.read(b, 0, 1024)) != -1) {
            fileOutputStream_bpmn.write(b, 0, len);
        }
        while ((len = inputStream_png.read(b, 0, 1024)) != -1) {
            fileOutputStream_png.write(b, 0, len);
        }

        //释放资源
        inputStream_bpmn.close();
        inputStream_png.close();
        fileOutputStream_bpmn.close();
        fileOutputStream_png.close();
    }

    //流程定义删除
    public void deleteProcessDefinition() {
        //流程部署id
        String deploymentId = "17501";

        //使用repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //如果流程已经启动，无法删除
//		repositoryService.deleteDeployment(deploymentId);

        //通过级联删除将流程定义及相关所有内容全部删除，不限制流程是否启动
        repositoryService.deleteDeployment(deploymentId, true);
    }
}












