package com.cargurus.percolator.domain;

import javax.management.MBeanParameterInfo;

public class JmxParameter {

    private final String name;
    private final String type;
    private final String description;

    public JmxParameter(MBeanParameterInfo parameterInfo) {
        this.name = parameterInfo.getName();
        this.type = parameterInfo.getType();
        this.description = parameterInfo.getDescription();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
