package com.springboot.cloud.apiGateway.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RestClientUtil<T, V> {

    public V exchange(String url, HttpMethod method, T reqObject, Class<V> responseObj, Map<String, ?> uriVariable) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<T> entity = getHttpEntity(reqObject);
        ResponseEntity<V> responseEntity = restTemplate.exchange(url, method, entity, responseObj, uriVariable);
        return (V) responseEntity;
    }

    public V exchange(String url, HttpMethod method, T reqObject, ParameterizedTypeReference<V> type) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<T> entity = getHttpEntity(reqObject);
        ResponseEntity<V> responseEntity = restTemplate.exchange(url, method, entity, type);
        return (V) responseEntity;
    }

    private HttpEntity<T> getHttpEntity(T payLoad) {
        if (payLoad == null || "".equals(payLoad))
            return new HttpEntity<T>(getHttpHeaders());
        return new HttpEntity<T>(payLoad, getHttpHeaders());
    }

    private HttpHeaders getHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        List<MediaType> supportedMediaType = new ArrayList<>();
        supportedMediaType.add(MediaType.APPLICATION_JSON);
        headers.setAccept(supportedMediaType);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
