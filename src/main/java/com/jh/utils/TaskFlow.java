package com.jh.project.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

public class TaskFlow {

    //1 创建processEngineConfiguration对象
    ProcessEngineConfiguration conf = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("spring-dao.xml");
    //创建processEngine引擎
    ProcessEngine processEngine = conf.buildProcessEngine();

    //部署流程定义
    public void processDeployment() {
        //使用repositoryService
        RepositoryService reporsitoryService = processEngine.getRepositoryService();

        InputStream inputStreamBpmn = this.getClass().getClassLoader().getResourceAsStream("diagram/flow01.bpmn");
//			InputStream inputStreamPng = this.getClass().getClassLoader().getResourceAsStream("diagram/flow.png");

        Deployment deploy = reporsitoryService.createDeployment()
                .addInputStream("flow.bpmn", inputStreamBpmn)
//						.addInputStream("flow.png", inputStreamPng)
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
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

        //启动流程实例时设置流程变量
        //定义流程变量
//			Map<String,Object> variables = new HashMap<String, Object>();
//			variables.put("userId", "abc");
//			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey,variables);

        System.out.println("流程实例的执行id：" + processInstance.getId());
        System.out.println("流程定义的id：" + processInstance.getProcessInstanceId());
        System.out.println("流程当前运行结点标识：" + processInstance.getActivityId());
        //activiti和业务系统集成后，需要将业务唯一标识存入到activiti中
        //比如采购流程，需要将采购单的id存储到流程实例表中，通过此businessKey关联查询业务系统信息
        System.out.println("业务标识：" + processInstance.getBusinessKey());
        System.out.println("所属流程定义的id：" + processInstance.getProcessDefinitionId());
    }

    //查询个人任务
    public void queryPersonalTaskList() {
        // 任务负责，正式开发系统当前用户从session中获得
        String assignee = "花无缺";

        // 流程定义key
        String processDefinitionKey = "flow01";

        TaskService taskService = processEngine.getTaskService();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 创建任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();

        // 设置查询条件,必须要指定任务的负责人
        taskQuery.taskAssignee(assignee);

        // 指定流程定义的key，只查询该业务流程下该用户的任务
        taskQuery.processDefinitionKey(processDefinitionKey);

        // 根据任务创建时间排序
        taskQuery.orderByTaskCreateTime().asc();
        // 根据任务创建时间排序(降序)
        // taskQuery.orderByTaskCreateTime().desc();

        // 查询该用户当前的待办任务
        List<Task> list = taskQuery.list();

        for (Task task : list) {

            System.out.println("==============================");
            System.out.println("任务id：" + task.getId());
            System.out.println("任务名称：" + task.getName());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务标识" + task.getTaskDefinitionKey());
            System.out.println("任务所属流程实例的id：" + task.getProcessInstanceId());

            // 通过businessKey(业务标识)查询采购单号、采购单名称、采购金额
            /**
             * 从task对象中得到processInstanceId
             * 根据processInstanceId得到processInstance对象
             * 从processInstance对象中获得businessKey
             */
            // 流程实例的id
            String processInstanceId = task.getProcessInstanceId();
            // 根据流程实例的id查询流程实例对象

            ProcessInstance processInstance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();
            // 业务标识(采购流程中就是采购单id)
            String businessKey = processInstance.getBusinessKey();

            // 根据businessKey即采购单id查询业务数据中采购单信息
            // ........
            System.out.println("businessKey:" + businessKey);
        }

    }

    //个人任务办理
    public void completeTask() {
        // 指定要办理任务id
        String taskId = "45004";

        // 指定任务负责人
        String assignee = "花无缺";

        TaskService takService = processEngine.getTaskService();

        // 这里只指定任务id并没有指定任务负责人，只要调用complete，该任务就完成
        // 所以说必须完成任务前校验
        // 校验方法：
        // 根据任务id和任务负责人assignee查询当前任务，如果查到说该用户有完成该任务权限，否则 没有权限
        Task task = takService.createTaskQuery().taskId(taskId).taskAssignee(assignee).singleResult();

        if (task != null) {
            takService.complete(taskId);

            System.out.println("完成任务！！！");
        }

    }

}


















