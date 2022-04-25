package com.silenteight.scb.reports.infrastructure.amqp;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    ReportsOutgoingConfigurationProperties.class
})
class ReportConfiguration {
}
