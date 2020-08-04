package com.use2go.demo.service.impl;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.*;
import com.use2go.demo.service.EsperService;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class EsperServiceImpl implements EsperService {

    @Override
    public EPRuntime createRuntime(Resource src, Class eventClass, UpdateListener updateListener) {
        String epl;
        try (Reader reader = new InputStreamReader(src.getInputStream(), StandardCharsets.UTF_8)) {
            epl = FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        var configuration = new Configuration();
        configuration.getCommon().addEventType(eventClass.getSimpleName(), eventClass);
        var args = new CompilerArguments(configuration);
        var compiler = EPCompilerProvider.getCompiler();
        EPCompiled compiled;
        try {
            compiled = compiler.compile(epl, args);
        } catch (EPCompileException e) {
            throw new RuntimeException(e);
        }

        var runtime = EPRuntimeProvider.getDefaultRuntime(configuration);
        EPDeployment deployment;
        try {
            deployment = runtime.getDeploymentService().deploy(compiled);
        }
        catch (EPDeployException ex) {
            throw new RuntimeException(ex);
        }

        var statement = runtime.getDeploymentService().getStatement(deployment.getDeploymentId(), "demo");
        statement.addListener(updateListener);

        return runtime;
    }

    @Override
    public void processMessage(EPRuntime runtime, Object event) {
        runtime.getEventService().sendEventBean(event, event.getClass().getSimpleName());
    }

}
