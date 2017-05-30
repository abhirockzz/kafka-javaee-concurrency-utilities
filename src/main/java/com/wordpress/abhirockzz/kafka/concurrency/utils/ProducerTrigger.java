package com.wordpress.abhirockzz.kafka.concurrency.utils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedExecutorService;

@Singleton
@Startup
public class ProducerTrigger {

    @Resource
    ManagedExecutorService mes;

    @PostConstruct
    public void trigger() {
        mes.execute(new Producer());
        System.out.println("Producer triggered....");

    }
}
