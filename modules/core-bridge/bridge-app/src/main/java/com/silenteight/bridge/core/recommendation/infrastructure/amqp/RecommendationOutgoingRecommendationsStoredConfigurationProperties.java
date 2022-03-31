package com.silenteight.bridge.core.recommendation.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.recommendation.outgoing.recommendation-stored")
public record RecommendationOutgoingRecommendationsStoredConfigurationProperties(
    String exchangeName) {
}
