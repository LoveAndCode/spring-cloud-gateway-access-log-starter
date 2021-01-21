# spring-cloud-gateway-access-log-starter
Simple Spring Cloud Gateway Access Log starter


### Build
```shell
./gradlew clean build -x test
```

### Property
```yaml
gateway:
  accesslog:
    enabled: true # enable or disable logging, default value is true.
    timeZone: Asia/Seoul # log datetime zone setting, default is UTC
```


### Custom Access User Information Resolver

```java
public interface AccessUserInformationResolver {
	String resolveAccessUserInformation(ServerWebExchange serverWebExchange);
}
```

AccessUserInformationResolver is a class that helps you leave user or client information in the access log.
For example, if you want to leave session information for a user or client in all request logs, you can leave it through the AccessUserInformationResolver class.

### Example
- Implement the AccessUserInformationResolver interface and register it as a Spring Bean.
```java
package com.tistory.johnmark.springgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.loveandcode.accesslog.AccessUserInformationResolver;

@SpringBootApplication
public class SpringGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringGatewayApplication.class, args);
	}

	@Bean
	public AccessUserInformationResolver accessUserInformationResolver() {
		return serverWebExchange -> "ANONYMOUS";
	}
}


```

- Example Log Image
![gatewayAccessLogExample](./image.png)


### Future Plan
- [ ] Maven repository Support
- [ ] Request Response Log Filter
- [ ] Sensitive data masking processing option