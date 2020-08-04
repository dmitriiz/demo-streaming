package com.use2go.demo.service.impl;

import com.use2go.demo.model.AppConstants;
import com.use2go.demo.model.DataMessage;
import com.use2go.demo.service.DataProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class DataProducerImpl implements DataProducer {

    private final KafkaTemplate<String, DataMessage> kafkaTemplate;

    public DataProducerImpl(KafkaTemplate<String, DataMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(DataMessage msg) {
        kafkaTemplate.send(AppConstants.TOPIC_IN, msg.getSource(), msg).addCallback(
                result -> {
                    log.info("Message published: {}", msg);
                },
                error -> {
                    log.error("Cannot send message", error);
                }
        );
    }

    @Scheduled(fixedRate = 1000)
    public void sendRandomMessage() {
        var msg = new DataMessage();
        msg.setSource(AppConstants.randomSource());
        msg.setValue(AppConstants.randomValue());
        send(msg);
    }

}
