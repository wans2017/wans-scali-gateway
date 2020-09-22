package com.wans.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by admin on 2020/9/18.
 */
/*
GlobalFilter全局过滤器接口，实现filter方法进行校验
Ordered优先级顺序，值越大则优先级越低
 */
@Component
public class TokenGlobalFilter implements GlobalFilter, Ordered {
    @Value("${server.port}")
    private String serverPort;
    /*
    ServerWebExchange服务网络交换器，有点像Context上下文
    GatewayFilterChain网关过滤链表
     */
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
        // 获取token参数
        String token = serverWebExchange.getRequest().getQueryParams().getFirst("token");
        if (StringUtils.isEmpty(token)) {   // token不能为空
            ServerHttpResponse response = serverWebExchange.getResponse();  // 获取返回
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);       // 设置状态码
            String msg = "token not is null";
            DataBuffer buffer = response.bufferFactory().wrap(msg.getBytes()); // 数据缓冲器写入信息
            return response.writeWith(Mono.just(buffer));
        }
        // 在请求头中存放网关端口号serverPort
        ServerHttpRequest request = serverWebExchange.getRequest().mutate().header("serverPort", serverPort).build();
        return gatewayFilterChain.filter(serverWebExchange.mutate().request(request).build());
        // return gatewayFilterChain.filter(serverWebExchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
