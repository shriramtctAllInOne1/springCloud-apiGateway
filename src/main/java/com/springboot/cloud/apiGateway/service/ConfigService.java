package com.springboot.cloud.apiGateway.service;

import com.springboot.cloud.apiGateway.domain.RouteConfig;
import com.springboot.cloud.apiGateway.exception.ApiGatewayException;
import com.springboot.cloud.apiGateway.util.RestClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ConfigService {
    @Autowired
    RestClientUtil restClientUtil;

    @Value("${configService.url}")
    String configServiceUrl;

    public String getConfigProperty(String key) throws ApiGatewayException {
        log.info("fetch config property for key {}");
        try {

        } catch (Exception e) {
            throw new ApiGatewayException("Exception while fetching config property", e);
        }
        return "";
    }

    public List<RouteConfig> getRoutesConfig() throws ApiGatewayException {
        log.info("fetch all config route");
        List<RouteConfig> routeConfigList;
        try {
            ParameterizedTypeReference<List<RouteConfig>> typeRef = new ParameterizedTypeReference<List<RouteConfig>>() {};
            ResponseEntity<List<RouteConfig>> responseEntity = (ResponseEntity) restClientUtil.exchange(configServiceUrl, HttpMethod.GET, null, typeRef);
           routeConfigList=responseEntity.getBody();
        } catch (Exception e) {
            throw new ApiGatewayException("Exception while fetching route", e);
        }
        return routeConfigList;

    }
}
