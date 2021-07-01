package com.example.provider.kafka;

import com.example.common.vo.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * kafka生产者
 */
@Component
public class KafkaSender {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Gson gson = new GsonBuilder().create();

    //发送消息方法
    public void send() {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());
        logger.info("+++++++++++++++++++++  message = {}", gson.toJson(message));
        kafkaTemplate.send("hhh", gson.toJson(message));
    }
}