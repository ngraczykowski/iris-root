package com.silenteight.connector.ftcc.callback.outgoing;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@ConstructorBinding
@ConfigurationProperties(prefix = "ftcc.core-bridge.outbound.recommendations-delivered")
public class RecommendationsDeliveredAmqpProperties {

  String exchange;
}
