package com.use2go.demo.service;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;

public interface DataProcessor {
    void handle(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime);
}
