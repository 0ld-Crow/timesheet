package com.springboot.cloud.app.timesheet.Job;

import com.springboot.cloud.app.timesheet.service.ISendMessageService;
import com.springboot.cloud.app.timesheet.service.IUnfilledService;
import com.springboot.cloud.app.timesheet.service.IWorkService;
import com.springboot.cloud.common.core.entity.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
@Component
public class UnfilledTask {


    @Autowired
    public IWorkService workService;


    @Autowired
    public ISendMessageService sendMessageService;

    @Autowired
    public IUnfilledService unfilledService;

    @Scheduled(cron="0 01 0 ? * TUE-SAT" )
    public void addUnfilled(){
        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>统计没有进行汇报的人员开始");
        unfilledService.updateTodayNoReport();
        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>统计没有进行汇报的人员结束");
    }

    @Scheduled(cron="0 05 18 ? * MON-FRI" )
    public void sendMessage(){
        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>定时发送消息开始");
        try {
            sendMessageService.sendMessage();
        } catch (Exception e) {
            log.error("{}", e);
        }
        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>定时发送消息结束");
    }

}
