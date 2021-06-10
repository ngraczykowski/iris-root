package com.silenteight.sens.webapp.user.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RolesProperties.class)
class RolesConfiguration {
}
