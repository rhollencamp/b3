package com.cargurus.percolator.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.MBeanOperationInfo;

public class JmxOperation {

    private final String name;
    private final String returnType;
    private final String description;
    private final List<JmxParameter> parameters;

    public JmxOperation(MBeanOperationInfo info) {
        this.name = info.getName();
        this.returnType = info.getReturnType();
        this.description = info.getDescription();
        this.parameters = Arrays.stream(info.getSignature()).
            map(JmxParameter::new).
            collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getDescription() {
        return description;
    }

    public List<JmxParameter> getParameters() {
        return parameters;
    }
}
