package com.use2go.demo.service;

import com.use2go.demo.model.DataMessage;

public interface DataProducer {
    void send(DataMessage msg);
}
