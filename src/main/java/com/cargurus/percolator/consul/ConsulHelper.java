package com.cargurus.percolator.consul;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpMethod.GET;

class ConsulHelper {

    private final String consulUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ParameterizedTypeReference<List<ServiceNode>> listOfNodes =
        new ParameterizedTypeReference<List<ServiceNode>>() { };
    private final ParameterizedTypeReference<List<KeyValue>> listOfKeyValue =
        new ParameterizedTypeReference<List<KeyValue>>() { };

    ConsulHelper(String consulUrl) {
        this.consulUrl = consulUrl;
    }

    List<ServiceNode> getServiceNodes(String service, String[] tags) {
        String url = consulUrl + "catalog/service/" + service;

        if (tags != null && tags.length > 0) {
            String tagsQueryParameters = Arrays.stream(tags).
                map(tag -> "tag=" + urlEncode(tag)).
                collect(Collectors.joining("&"));
            url += "?" + tagsQueryParameters;
        }

        ResponseEntity<List<ServiceNode>> response = restTemplate.exchange(
            url, GET, null, listOfNodes);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException();
        }
        return response.getBody();
    }

    List<KeyValue> getKeyValues(String path) {
        ResponseEntity<List<KeyValue>> response = restTemplate.exchange(
            consulUrl + "kv/" + path + "/?recurse=true", GET, null, listOfKeyValue);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException();
        }
        return response.getBody();
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}

