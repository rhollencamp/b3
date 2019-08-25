package com.cargurus.percolator.domain;

import javax.management.MBeanAttributeInfo;

public class JmxAttribute {

    private final String name;
    private final String type;
    private final String description;
    private final boolean readable;
    private final boolean writable;

    public JmxAttribute(MBeanAttributeInfo info) {
        this.name = info.getName();
        this.type = info.getType();
        this.description = info.getDescription();
        this.readable = info.isReadable();
        this.writable = info.isWritable();
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

    public boolean isReadable() {
        return readable;
    }

    public boolean isWritable() {
        return writable;
    }
}
