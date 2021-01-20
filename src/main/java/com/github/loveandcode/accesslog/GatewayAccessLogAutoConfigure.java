package com.github.loveandcode.accesslog;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GatewayAccessLogProperties.class)
public class GatewayAccessLogAutoConfigure {

	@Bean
	public GatewayAccessLogGlobalFilter gatewayAccessLogGlobalFilter(
		GatewayAccessLogProperties gatewayAccessLogProperties,
		AccessUserInformationResolver accessUserInformationResolver
	) {
		return new GatewayAccessLogGlobalFilter(gatewayAccessLogProperties, accessUserInformationResolver);
	}

	@Bean
	@ConditionalOnMissingBean(AccessUserInformationResolver.class)
	public AccessUserInformationResolver defaultAccessUserInformationResolver() {
		return serverWebExchange -> "-";
	}
}
