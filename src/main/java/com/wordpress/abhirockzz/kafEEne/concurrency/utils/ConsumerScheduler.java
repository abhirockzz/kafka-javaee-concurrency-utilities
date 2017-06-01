package com.wordpress.abhirockzz.kafEEne.concurrency.utils;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;

@Singleton
@Startup
public class ConsumerScheduler {

    @Resource
    ManagedScheduledExecutorService mses;

    private ScheduledFuture<?> consumerJob;
    
    @PostConstruct
    public void regular() {
        consumerJob = mses.scheduleWithFixedDelay(new Consumer(), 15, 1, TimeUnit.SECONDS);
        System.out.println("Consumer triggered by thread " + Thread.currentThread().getName());

    }
    
    @PreDestroy
    public void stop(){
        System.out.println("Cancelling consumer job.....");
        consumerJob.cancel(true);
    }

}
