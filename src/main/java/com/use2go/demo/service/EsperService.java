package com.use2go.demo.service;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.UpdateListener;
import org.springframework.core.io.Resource;

public interface EsperService {
    EPRuntime createRuntime(Resource src, Class eventClasses, UpdateListener updateListener);
    void processMessage(EPRuntime runtime, Object event);
}
