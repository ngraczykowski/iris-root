package com.silenteight.payments.bridge;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootConfiguration(proxyBeanMethods = false)
@EnableRetry
public class SpringBootTestConfiguration {
}
