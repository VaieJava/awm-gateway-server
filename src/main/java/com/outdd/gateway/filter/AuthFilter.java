package com.outdd.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

	private static final String AUTHORIZE_TOKEN = "token";
	private static final String AUTHORIZE_UID = "uid";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("==================");
		String url = exchange.getRequest().getPath().pathWithinApplication().value();
		log.info("请求URL:"+url);
		log.info("method:"+exchange.getRequest().getMethod());
		MultiValueMap<String,String> map = exchange.getRequest().getQueryParams();
		for(String key : map.keySet()){
            log.info("parameter:key = "+key +"value = "+map.get(key));
        }

		//获取header
		String appKey = exchange.getRequest().getHeaders().getFirst("token");
		log.info("appKey："+appKey);
		//请求的body
		Flux<DataBuffer> flux = exchange.getRequest().getBody();
		//这里如何解析出请求的报文呢？

		return chain.filter(exchange.mutate().request(exchange.getRequest()).build());
	}

	@Override
	public int getOrder() {
		return 0;
	}
}