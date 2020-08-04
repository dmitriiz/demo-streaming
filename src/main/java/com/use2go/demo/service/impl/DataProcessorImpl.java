package com.use2go.demo.service.impl;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.use2go.demo.service.DataProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataProcessorImpl implements DataProcessor {

    @Override
    public void handle(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        for (EventBean eventBean : newEvents) {
            var o = eventBean.getUnderlying();
            var len = (oldEvents == null) ? 0 : oldEvents.length;
            log.info("EVENT: {}  [{}]", o, len);
        }
    }

}
