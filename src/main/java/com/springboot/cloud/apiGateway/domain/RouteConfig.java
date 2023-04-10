package com.springboot.cloud.apiGateway.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteConfig {
    private String routeName;
    private boolean serviceDiscoveryEnabled;
    private String serviceName;
    private String serviceUrl;
    private String gatewayPath;
    private String servicePath;
    private String resourceHierarchy;
    private String featureFlag;
    private String action;
    private boolean oidcEnabled;
    private String protocol;
}
