package com.wordpress.abhirockzz.kafEEne.concurrency.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedTask;
import javax.enterprise.concurrent.ManagedTaskListener;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

public class Consumer implements Runnable, ManagedTask {

    private KafkaConsumer<String, String> kc;
    private String topic = null;
    private String pollTimeout = null;

    public Consumer() {
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv().getOrDefault("KAFKA_CLUSTER", "192.168.99.100:9092"));
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "kafEEne-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        topic = System.getenv().getOrDefault("TOPIC_NAME", "test-topic");

        kc = new KafkaConsumer<>(consumerProps);
        kc.subscribe(Arrays.asList(topic));
        System.out.println("Subscribed to topic " + topic);

        pollTimeout = System.getenv().getOrDefault("KAFKA_CONSUMER_POLL_TIMEOUT", "30000"); //default is 30 seconds
    }

    @Override
    public void run() {
        ConsumerRecords<String, String> records = kc.poll(Long.valueOf(pollTimeout)); // timeout
        System.out.println("Got " + records.count() + " records");

        for (ConsumerRecord<String, String> record : records) {
            System.out.println("Consumed record " + record);
        }
    }

    private void committedOffsetsInfo() {

        System.out.println("Committed Offsets info\n");

        kc.partitionsFor(topic).stream()
                .map((PartitionInfo pi) -> new TopicPartition(pi.topic(), pi.partition()))
                .map((tp) -> {
                    System.out.println("Partition " + tp.partition());
                    return kc.committed(tp);
                })
                .forEach(om -> System.out.println("Committed offset " + om.offset()));
    }

    @Override
    public ManagedTaskListener getManagedTaskListener() {
        return new ManagedTaskListener() {
            @Override
            public void taskSubmitted(Future<?> future, ManagedExecutorService executor, Object task) {
                System.out.println("Task SUBMITTED in thread " + Thread.currentThread().getName());

            }

            @Override
            public void taskAborted(Future<?> future, ManagedExecutorService executor, Object task, Throwable exception) {
                System.out.println("Task ABORTED in thread " + Thread.currentThread().getName());
                kc.commitSync();
                System.out.println("Offset commit complete");
                committedOffsetsInfo();

            }

            @Override
            public void taskDone(Future<?> future, ManagedExecutorService executor, Object task, Throwable exception) {
                System.out.println("Task COMPLETED in thread " + Thread.currentThread().getName());
                kc.commitSync();
                System.out.println("Offset commit complete");
                committedOffsetsInfo();
            }

            @Override
            public void taskStarting(Future<?> future, ManagedExecutorService executor, Object task) {
                System.out.println("Task STARTING in thread " + Thread.currentThread().getName());
            }
        };
    }

    @Override
    public Map<String, String> getExecutionProperties() {
        return Collections.emptyMap();
    }

}
