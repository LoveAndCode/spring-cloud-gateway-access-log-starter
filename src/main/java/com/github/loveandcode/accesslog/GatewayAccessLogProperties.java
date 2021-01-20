package com.github.loveandcode.accesslog;

import java.util.Arrays;
import java.util.TimeZone;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
	prefix = "gateway.accesslog"
)
public class GatewayAccessLogProperties {
	private Boolean enabled = true;
	private String timeZone = "UTC";

	public GatewayAccessLogProperties() {
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getTimeZone() {
		return this.timeZone;
	}

	public void setTimeZone(String timeZone) {
		long exist = Arrays.stream(TimeZone.getAvailableIDs()).filter((id) -> {
			return id.equals(timeZone);
		}).count();
		if (exist <= 0L) {
			throw new IllegalArgumentException("'" + timeZone + "'time zone not acceptable time zone id.");
		} else {
			this.timeZone = timeZone;
		}
	}
}
