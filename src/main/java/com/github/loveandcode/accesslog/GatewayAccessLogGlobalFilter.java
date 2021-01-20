package com.github.loveandcode.accesslog;

import java.util.Optional;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class GatewayAccessLogGlobalFilter implements GlobalFilter, Ordered {
	private final GatewayAccessLogProperties accessLogProperties;
	private final AccessUserInformationResolver accessUserInformationResolver;

	public GatewayAccessLogGlobalFilter(
		GatewayAccessLogProperties accessLogProperties,
		AccessUserInformationResolver accessUserInformationResolver
	) {
		this.accessLogProperties = accessLogProperties;
		this.accessUserInformationResolver = accessUserInformationResolver;
	}

	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		if (this.accessLogProperties.isEnabled()) {
			ServerHttpRequest serverHttpRequest = exchange.getRequest();
			ServerHttpResponse serverHttpResponse = exchange.getResponse();
			GatewayAccessLog gatewayAccessLog = new GatewayAccessLog(this.accessLogProperties.getTimeZone());

			gatewayAccessLog
				.address(this.fetchAddressFromRequest(serverHttpRequest))
				.method(serverHttpRequest.getMethod().toString())
				.uri(serverHttpRequest.getURI().toString())
				.user(accessUserInformationResolver.resolveAccessUserInformation(exchange));

			if (serverHttpRequest.getHeaders().get("user-agent") != null
				&& serverHttpRequest.getHeaders().get("user-agent").size() > 0) {
				gatewayAccessLog.userAgent(serverHttpRequest.getHeaders().get("user-agent").get(0));
			}

			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				String responseStatus = String.format(
					"%d %s", serverHttpResponse.getRawStatusCode(),
					HttpStatus.valueOf(serverHttpResponse.getRawStatusCode()).name()
				);
				gatewayAccessLog.status(responseStatus)
					.contentType((Optional.ofNullable(serverHttpResponse.getHeaders().getContentType())
						.orElse(new MediaType("-", "-"))).toString())
					.contentLength(serverHttpResponse.getHeaders().getContentLength());
				gatewayAccessLog.log();
			}));
		} else {
			return chain.filter(exchange);
		}
	}

	private String fetchAddressFromRequest(ServerHttpRequest request) {
		String clientIp = request.getHeaders().getFirst("X-Forwarded-For");
		if (clientIp != null) {
			return clientIp;
		} else {
			return request.getRemoteAddress() != null ? request.getRemoteAddress().getAddress().getHostAddress() : "-";
		}
	}

	public int getOrder() {
		return -1;
	}
}
