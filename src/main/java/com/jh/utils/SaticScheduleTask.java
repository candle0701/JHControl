package com.jh.utils;

import com.jh.entity.MesBudgetTaskwork;
import com.jh.entity.MesProductionOrder;
import com.jh.entity.MesTask;
import com.jh.service.MesBudgetWorkService;
import com.jh.service.MesProductionOrderService;
import com.jh.service.MesTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Component
@Configuration
@EnableScheduling
public class SaticScheduleTask {

    @Autowired
    private MesProductionOrderService mesProductionOrderService;

    @Autowired
    private MesTaskService mesTaskService;

    @Autowired
    private MesBudgetWorkService mesBudgetWorkService;

    @Scheduled(cron = "0 0 3 * * ?")
//    @Scheduled(cron = "0/10 * * * * *")
    private void configureTasks() {
        //生产订单状态  1 通过   2未审核  3执行中  4 关闭
        /*
         1. 生产订单由释放状态变为执行状态。根据生产订单中的生产加工开始时间进行判断，生产加工开始时间一到，生产订单状态就由释放状态变为执行状态，用于生产任务的分配。
         */
        String now = CurrentTime.getCurrentTimestamp();

        MesProductionOrder order1 = new MesProductionOrder();
        order1.setStatus("1");
        List<MesProductionOrder> orderList1 = mesProductionOrderService.getListByTaskCode(order1);
        if(orderList1.size()>0){
            for(MesProductionOrder mpo:orderList1){
                String beginDate = mpo.getBiginDate()+" 00:00:00";
                if("-1".equals(CurrentTime.compare_date(now,beginDate))){
                    //生产订单状态就由释放状态变为执行状态
                    MesProductionOrder orderUpdate1 = new MesProductionOrder();
                    orderUpdate1.setStatus("2");
                    orderUpdate1.setId(mpo.getId());
                    mesProductionOrderService.update(orderUpdate1);

                    //作业单状态改为执行中状态
                    MesTask mesTask = new MesTask();
                    mesTask.setId(mpo.getTaskCode());
                    mesTask.setTaskStatus("TASK_ING");
                    mesTaskService.updateByPrimaryKeySelective(mesTask);
                }
            }
        }

        //生产订单状态  1 通过   2未审核  3执行中  4 关闭
         /*
            2． 遍历所有正在执行的生产订单，当入库数量达到作业单的需求量时，生产订单要进行关闭，相应的作业单也要进行关闭。
            遍历任务分发，把100%的生产订单关闭，对应作业单也关闭
         */
        List<MesBudgetTaskwork> list = mesBudgetWorkService.getTaskSend();

        List<MesBudgetTaskwork> mdtList = new ArrayList<>();

        for(MesBudgetTaskwork mbt : list){
            MesBudgetTaskwork mesbt1 = mesBudgetWorkService.taskSendChild(mbt.getId());
            MesBudgetTaskwork mesbt2 = mesBudgetWorkService.taskSendMother(mbt.getId());
            if(mesbt1!=null){
                Double dou = (Double.parseDouble(mesbt1.getChild())/Double.parseDouble(mesbt2.getMother()))*100.00;
                String donePercent = String.valueOf(String.format("%.2f",dou)+"%");
                mbt.setDonePercent(donePercent);
            }else{
                mbt.setDonePercent("0%");
            }
            mdtList.add(mbt);
        }
        for(MesBudgetTaskwork mbt : mdtList){
            if("100.00%".equals(mbt.getDonePercent())){
                MesProductionOrder orderUpdate2 = new MesProductionOrder();
                orderUpdate2.setStatus("4");
                orderUpdate2.setTaskCode(mbt.getId());
                mesProductionOrderService.updateByTaskId(orderUpdate2);

                //关闭作业单
                MesTask mesTask = new MesTask();
                mesTask.setId(mbt.getId());
                mesTask.setTaskStatus("TASK_CLOSE");
                mesTaskService.updateByPrimaryKeySelective(mesTask);
            }
        }
//        MesProductionOrder order2 = new MesProductionOrder();
//        order2.setStatus("3");
//        List<MesProductionOrder> orderList2 = mesProductionOrderService.getListByTaskCode(order2);
//        if(orderList2.size()>0){
//            for(MesProductionOrder mpo:orderList2){
//                int leftNum = 0;
//                List<MesBudgetTaskwork> mesBudgetTaskworkList = mesBudgetWorkService.getTaskSendDetail(mpo.getTaskCode());
//                if(mesBudgetTaskworkList.size()>0){
//                    for(MesBudgetTaskwork mbt : mesBudgetTaskworkList){
//                        leftNum += Integer.parseInt(mbt.getLeftNum());
//                    }
//                }
//                if(leftNum == 0){
//                    //关闭生产订单
//                    MesProductionOrder orderUpdate2 = new MesProductionOrder();
//                    orderUpdate2.setStatus("4");
//                    orderUpdate2.setId(mpo.getId());
//                    mesProductionOrderService.update(orderUpdate2);
//
//                    //关闭作业单
//                    MesTask mesTask = new MesTask();
//                    mesTask.setId(mpo.getTaskCode());
//                    mesTask.setTaskStatus("TASK_CLOSE:");
//                    mesTaskService.updateByPrimaryKeySelective(mesTask);
//                }
//            }
//        }

    }
}