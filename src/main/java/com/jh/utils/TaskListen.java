package com.jh.utils;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskListen implements TaskListener {

    //delegateTask是任务的代理对象
    @Override
    public void notify(DelegateTask delegateTask) {
        //通过任务的代理对象操作任务
        delegateTask.setAssignee("花无缺");
    }

}
