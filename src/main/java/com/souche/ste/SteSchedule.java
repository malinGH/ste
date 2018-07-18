package com.souche.ste;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * STE抓取调度器，该调度器是一个单机的主程序：
 * 异步线程定时监控调度，每次调度做下面的事情：
 * 1. 读取props.conf配置文件中的信息，每一行记录的sample为props/ershouche/che168.props; interval = 14400
 * 2. 读取模板的上次调度时间，每个模板的上次调度时间记录在配置文件schedule.conf中，格式sample为props/ershouche/che168.props; lastSchedule=xxxx
 * 3. 如果上次调度时间在时间间隔内，且模板没在跑的过程中，则启动抓取，同时记录上次调度时间
 */
public class SteSchedule {
    // 定时调度线程池
    ScheduledExecutorService scheduledExecutorService;
    
    // 对crawler做一次调度
    void scheduleCrawler() {
        // TODO 抓取调度的处理策略
        // 抓取模板配置文件放在 /usr/local/ste/conf/props.conf
        // 格式 props/ershouche/che168.props; interval = 14400
        
        
        // 调度记录放在/usr/local/ste/conf/scheduled.conf 
        // 格式 props/ershouche/che168.props; lastSchedule=xxxx
        System.out.println("schedule");
    }
    
    // 启动调度
    void startSchedule() {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                scheduleCrawler();
            }
        }, 0, 60, TimeUnit.SECONDS);
    }
    
    void stopSchedule() {
        if(scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
    }
    
    public static void main(String[] args) {
        SteSchedule steSchedule = new SteSchedule();
        steSchedule.startSchedule();
    }
}
