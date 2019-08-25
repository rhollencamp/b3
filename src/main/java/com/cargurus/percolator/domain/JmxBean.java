package com.cargurus.percolator.domain;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.management.ObjectName;

import static java.util.stream.Collectors.toMap;

public class JmxBean {

    private final String domain;
    private final String keyProperties;
    private final String description;
    private final List<JmxOperation> operations;
    private final Map<String, JmxAttribute> attributes;

    public JmxBean(ObjectName objectName) {
        this.domain = objectName.getDomain();
        this.keyProperties = objectName.getKeyPropertyListString();
        this.description = null;
        this.operations = null;
        this.attributes = null;
    }

    public JmxBean(ObjectName objectName,
                   String description,
                   List<JmxOperation> operations,
                   List<JmxAttribute> attributes) {
        this.domain = objectName.getDomain();
        this.keyProperties = objectName.getKeyPropertyListString();
        this.description = description;
        this.operations = operations;
        this.attributes = attributes.stream().collect(toMap(
            JmxAttribute::getName,
            x -> x,
            (u, v) -> { throw new RuntimeException(); },
            LinkedHashMap::new));
    }

    public String getDomain() {
        return domain;
    }

    public String getKeyProperties() {
        return keyProperties;
    }

    public String getDescription() {
        return description;
    }

    public List<JmxOperation> getOperations() {
        return operations;
    }

    public JmxOperation getOperation(String name, List<String> signature) {
        for (JmxOperation op : operations) {
            if (name.equals(op.getName())) {
                List<String> opSignature = op.getParameters().stream().
                    map(JmxParameter::getType).
                    collect(Collectors.toList());
                if (opSignature.equals(signature)) {
                    return op;
                }
            }
        }
        return null;
    }

    public Collection<JmxAttribute> getAttributes() {
        return attributes.values();
    }

    public JmxAttribute getAttribute(String name) {
        return attributes.get(name);
    }
}
