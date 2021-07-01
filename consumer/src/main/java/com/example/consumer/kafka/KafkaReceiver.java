package com.example.consumer.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * kafka消费者
 */
@Component
public class KafkaReceiver {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = {"hhh"},groupId = "xiaofeng1")
    public void listen(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            logger.info("-xiaofeng1-- test1：" + record+"，record:" + record+",message:" + message+",---");
        }

    }
}