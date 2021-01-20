package com.github.loveandcode.accesslog;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import reactor.util.Logger;
import reactor.util.Loggers;

final class GatewayAccessLog {
	static final Logger LOGGER = Loggers.getLogger("com.tistory.johnmark.accesslog.GatewayAccessLog");
	static final DateTimeFormatter DATE_TIME_FORMATTER =
		DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
	static final String COMMON_LOG_FORMAT =
		"[{}] [{}] [{}] \"{} {} [{}] [{}]\" [{}] [{} ms] [{}]";
	static final String MISSING = "-";
	final String zonedDateTime;
	String address = MISSING;
	CharSequence method;
	CharSequence uri;
	String protocol = MISSING;
	String user = MISSING;
	CharSequence status;
	String contentType = MISSING;
	String userAgent = MISSING;
	long contentLength;
	long startTime = System.currentTimeMillis();

	GatewayAccessLog(String timeZone) {
		this.zonedDateTime = ZonedDateTime.now(ZoneId.of(timeZone)).format(DATE_TIME_FORMATTER);
	}

	GatewayAccessLog address(String address) {
		this.address = Objects.requireNonNull(address, "address");
		return this;
	}

	GatewayAccessLog method(CharSequence method) {
		this.method = Objects.requireNonNull(method, "method");
		return this;
	}

	GatewayAccessLog uri(CharSequence uri) {
		this.uri = Objects.requireNonNull(uri, "uri");
		return this;
	}

	GatewayAccessLog protocol(String protocol) {
		this.protocol = Objects.requireNonNull(protocol, "protocol");
		return this;
	}

	GatewayAccessLog status(CharSequence status) {
		this.status = Objects.requireNonNull(status, "status");
		return this;
	}

	GatewayAccessLog contentLength(long contentLength) {
		this.contentLength = contentLength;
		return this;
	}

	GatewayAccessLog user(String user) {
		this.user = user;
		return this;
	}

	GatewayAccessLog contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	GatewayAccessLog userAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}

	long duration() {
		return System.currentTimeMillis() - startTime;
	}

	void log() {
		LOGGER.info(COMMON_LOG_FORMAT, zonedDateTime, user, address, method, uri, contentType, status,
			(contentLength > -1 ? contentLength : MISSING), duration(), userAgent
		);
	}
}