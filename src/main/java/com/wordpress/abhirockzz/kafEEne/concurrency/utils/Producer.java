package com.wordpress.abhirockzz.kafEEne.concurrency.utils;

import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class Producer implements Runnable {

    private KafkaProducer<String, String> producer;
    private String topic = null;
    private String producerPause = null;

    public Producer() {
        Properties consumerProps = new Properties();
        consumerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv().getOrDefault("KAFKA_CLUSTER", "192.168.99.100:9092"));
        consumerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        consumerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(consumerProps);
        topic = System.getenv().getOrDefault("TOPIC_NAME", "test-topic");

        producerPause = System.getenv().getOrDefault("KAFKA_PRODUCER_WAIT", "3000"); //defaults to 3 seconds
    }

    static Random rnd = new Random();

    @Override
    public void run() {
        System.out.println("Producing to topic " + topic);
        String numPartitions  = System.getenv().getOrDefault("NUM_PARTITIONS", "1");
        System.out.println("Total Partitions " + numPartitions);
        
        while (true) {
            try {
                producer.send(new ProducerRecord<>(topic, "key-" + rnd.nextInt(10), "val-" + rnd.nextInt(10)),
                new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata record, Exception excptn) {
                        System.out.println("Sent data to Offset " + record.offset()
                                + " in Partition " + record.partition());
                    }
                });

                Thread.sleep(Long.valueOf(producerPause));
            } catch (Exception ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
