package com.silenteight.scb.outputrecommendation.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.output-recommendation.outgoing.recommendation-delivered")
public record OutputRecommendationDeliveredProperties(
    String exchangeName
) {
}
