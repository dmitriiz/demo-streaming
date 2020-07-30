package com.use2go.demo.service.impl;

import com.use2go.demo.model.AppConstants;
import com.use2go.demo.model.DataMessage;
import com.use2go.demo.service.DataConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;

@Slf4j
public class DataConsumerImpl implements DataConsumer {

    @KafkaListener(topics = AppConstants.TOPIC_OUT)
    public void consume(DataMessage msg) throws IOException {
        log.info("Message received: {}", msg);
    }

}
