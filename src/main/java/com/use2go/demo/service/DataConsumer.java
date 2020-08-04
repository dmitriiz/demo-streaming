package com.use2go.demo.service;

import com.use2go.demo.model.DataMessage;

import java.io.IOException;

public interface DataConsumer {
    void consume(DataMessage msg) throws IOException;
}
