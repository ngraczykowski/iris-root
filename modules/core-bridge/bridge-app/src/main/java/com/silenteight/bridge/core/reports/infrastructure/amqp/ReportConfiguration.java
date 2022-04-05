package com.silenteight.bridge.core.reports.infrastructure.amqp;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    AmqpReportsProperties.class,
    ReportsOutgoingConfigurationProperties.class,
    ReportsIncomingBatchDeliveredProperties.class
})
class ReportConfiguration {
}
