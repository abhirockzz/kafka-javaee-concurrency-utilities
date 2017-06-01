package com.wordpress.abhirockzz.kafEEne.concurrency.utils;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;

@Singleton
@Startup
public class ProducerTrigger {

    @Resource
    ManagedScheduledExecutorService mses;

    @PostConstruct
    public void trigger() {
        mses.schedule(new Producer(), 10, TimeUnit.SECONDS);
        System.out.println("Producer triggered....");

    }
}
