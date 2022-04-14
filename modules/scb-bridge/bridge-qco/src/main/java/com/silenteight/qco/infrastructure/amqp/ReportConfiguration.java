package com.silenteight.qco.infrastructure.amqp;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    ReportAmqpConfigurationProperties.class
})
class ReportConfiguration {
}
