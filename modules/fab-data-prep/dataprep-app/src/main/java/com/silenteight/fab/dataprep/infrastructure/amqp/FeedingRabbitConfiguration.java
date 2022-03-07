package com.silenteight.fab.dataprep.infrastructure.amqp;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    AmqpFeedingOutgoingMatchFeatureInputSetFedProperties.class
})
class FeedingRabbitConfiguration {
}
