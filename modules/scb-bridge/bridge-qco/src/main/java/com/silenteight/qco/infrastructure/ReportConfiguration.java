package com.silenteight.qco.infrastructure;

import com.silenteight.qco.infrastructure.amqp.ReportAmqpConfigurationProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    CommentsPrefixProperties.class,
    ReportAmqpConfigurationProperties.class
})
class ReportConfiguration {
}
