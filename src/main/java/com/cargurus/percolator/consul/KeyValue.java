package com.cargurus.percolator.consul;

import com.fasterxml.jackson.annotation.JsonProperty;

class KeyValue {

    @JsonProperty("Key")   private String key;
    @JsonProperty("Value") private String value;

    String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

    String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }
}
