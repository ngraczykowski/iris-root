package com.silenteight.warehouse.common.environment;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EnvironmentProperties.class)
class EnvironmentConfiguration {
}
