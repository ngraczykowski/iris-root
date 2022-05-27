package com.silenteight.bridge.core.reports.infrastructure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    ReportsProperties.class
})
class ReportsConfiguration {
}
