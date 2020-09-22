package com.wans.service;

import com.wans.entity.GateWay;
import com.wans.mapper.GateWayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路由网关服务
 * Created by admin on 2020/9/22.
 */
@Service
public class GateWayService implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;
    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter; // 操作gateway路由Api
    @Autowired
    private GateWayMapper gateWayMapper;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
    // 初始化路由配置
    public String initAllRoute() {
        // 从数据库查询配置的网关配置
        List<GateWay> gateWays = gateWayMapper.gateWayAll();
        for (GateWay gw : gateWays) {
            loadRoute(gw);
        }
        return "success";
    }

    public String loadRoute(GateWay gateWay) {
        RouteDefinition definition = new RouteDefinition();
        Map<String, String> predicateParams = new HashMap<>(8);
        PredicateDefinition predicate = new PredicateDefinition();
        FilterDefinition filterDefinition = new FilterDefinition();
        Map<String, String> filterParams = new HashMap<>(8);
        // 如果配置路由type为0的话 则从注册中心获取路由信息;另外是从http协议地址
        URI uri ;
        if ("0".equals(gateWay.getRouteType())) {
            uri = UriComponentsBuilder.fromUriString(gateWay.getRouteUrl()).build().toUri();
        } else {
            uri = UriComponentsBuilder.fromHttpUrl(gateWay.getRouteUrl()).build().toUri();
        }
        // 定义的路由唯一的id
        definition.setId(gateWay.getRouteId());
        predicate.setName("Path");
        //路由转发地址
        predicateParams.put("pattern", gateWay.getRoutePattern());
        predicate.setArgs(predicateParams);

        // 名称是固定的, 路径去前缀
        filterDefinition.setName("StripPrefix");
        filterParams.put("_genkey_0", "1");
        filterDefinition.setArgs(filterParams);
        definition.setPredicates(Arrays.asList(predicate));
        definition.setFilters(Arrays.asList(filterDefinition));
        definition.setUri(uri);
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }

}
