package com.springboot.cloud.apiGateway.config;

import com.springboot.cloud.apiGateway.domain.RouteConfig;
import com.springboot.cloud.apiGateway.service.ConfigService;
import com.springboot.cloud.apiGateway.service.GatewayRouteRefresher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.cloud.gateway.filter.factory.*;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Component
public class RefreshableRoutesLocator implements RouteLocator {
    private final RouteLocatorBuilder routeLocatorBuilder;
    private final GatewayRouteRefresher gatewayRouteRefresher;
    private final DiscoveryClient discoveryClient;
    private final ConfigService configService;
    private final RemoveRequestHeaderGatewayFilterFactory removeRequestHeaderGatewayFilterFactory;
    private final DedupeResponseHeaderGatewayFilterFactory dedupeResponseHeaderGatewayFilterFactory;
    private AddResponseHeaderGatewayFilterFactory addResponseHeaderGatewayFilterFactory;
    private RouteLocatorBuilder.Builder routeBuilder;
    private HttpClientProperties httpClientProperties;
    private Flux<Route> routeFlux;

    @Autowired
    public RefreshableRoutesLocator(RouteLocatorBuilder routeLocatorBuilder, GatewayRouteRefresher gatewayRouteRefresher, DiscoveryClient discoveryClient, ConfigService configService, RemoveRequestHeaderGatewayFilterFactory removeRequestHeaderGatewayFilterFactory, DedupeResponseHeaderGatewayFilterFactory dedupeResponseHeaderGatewayFilterFactory, AddResponseHeaderGatewayFilterFactory addResponseHeaderGatewayFilterFactory,HttpClientProperties httpClientProperties) {
        this.routeLocatorBuilder = routeLocatorBuilder;
        this.gatewayRouteRefresher = gatewayRouteRefresher;
        this.discoveryClient = discoveryClient;
        this.configService = configService;
        this.removeRequestHeaderGatewayFilterFactory = removeRequestHeaderGatewayFilterFactory;
        this.dedupeResponseHeaderGatewayFilterFactory = dedupeResponseHeaderGatewayFilterFactory;
        this.addResponseHeaderGatewayFilterFactory = addResponseHeaderGatewayFilterFactory;
        this.httpClientProperties=httpClientProperties;
        clearRoutes();
        routeFlux = routeBuilder.route("gateway_route", r -> r.path("/gateway/**")
                .filters(f -> f.rewritePath("/gateway/(?<segment>.*)", "/${segment}"))
                .uri("no://op")).build().getRoutes();
    }

    public void setTimeouts() {
        httpClientProperties.setConnectTimeout(30000);
        httpClientProperties.setResponseTimeout(Duration.ofMillis(10000));
    }

    public void buildRoute() {
        this.routeFlux = routeBuilder.build().getRoutes();
        gatewayRouteRefresher.refreshRoutes();
    }

    public void clearRoutes() {
        routeBuilder = routeLocatorBuilder.routes();
    }

    @Override
    public Flux<Route> getRoutes() {
        return routeFlux;
    }

    public RefreshableRoutesLocator addRoutes() {
        List<RouteConfig> routeConfigs = configService.getRoutesConfig();
        routeConfigs.stream().forEach(routeConfig -> {
            AbstractGatewayFilterFactory.NameConfig removeHeaderConfig = removeRequestHeaderGatewayFilterFactory.newConfig();
            DedupeResponseHeaderGatewayFilterFactory.Config deDupHeaderConfig = new DedupeResponseHeaderGatewayFilterFactory.Config();
            deDupHeaderConfig.setName("Access-Control-Allow-Origin");
            deDupHeaderConfig.setStrategy(DedupeResponseHeaderGatewayFilterFactory.Strategy.RETAIN_FIRST);
            AbstractNameValueGatewayFilterFactory.NameValueConfig addControlHeaderConfig = addResponseHeaderGatewayFilterFactory.newConfig();
            addControlHeaderConfig.setName("Access-Control-Expose-Headers");
            addControlHeaderConfig.setValue("Content-Disposition");
            routeBuilder.route(routeConfig.getRouteName(), r -> r.path(routeConfig.getGatewayPath().endsWith("/") ? routeConfig.getGatewayPath() + "**" : routeConfig.getGatewayPath())
                    .filters(f -> {
                        f.rewritePath(routeConfig.getGatewayPath() + "(?<segment>.*)", routeConfig.getServicePath() + "${segment}")
                                .filter(addResponseHeaderGatewayFilterFactory.apply(addControlHeaderConfig))
                                .filter(dedupeResponseHeaderGatewayFilterFactory.apply(deDupHeaderConfig))
                                .filter(removeRequestHeaderGatewayFilterFactory.apply(removeHeaderConfig));
                        return f;

                    }).uri(routeConfig.getServiceUrl())
            );

        });
        return this;
    }
}
