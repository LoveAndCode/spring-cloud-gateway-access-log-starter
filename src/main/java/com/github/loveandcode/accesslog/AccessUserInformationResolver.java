package com.github.loveandcode.accesslog;

import org.springframework.web.server.ServerWebExchange;

public interface AccessUserInformationResolver {
	String resolveAccessUserInformation(ServerWebExchange serverWebExchange);
}
