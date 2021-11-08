package com.silenteight.simulator.common.integration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "simulator.messaging.broker")
class BrokerProperties {

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties recommendations;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties indexResponse;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties datasetExpired;

  String recommendationsQueueName() {
    return recommendations.getQueueName();
  }

  String recommendationsRoutingKey() {
    return recommendations.getRoutingKey();
  }

  String indexResponseQueueName() {
    return indexResponse.getQueueName();
  }

  String indexResponseRoutingKey() {
    return indexResponse.getRoutingKey();
  }

  String datasetExpiredQueueName() {
    return datasetExpired.getQueueName();
  }

  String datasetExpiredRoutingKey() {
    return datasetExpired.getRoutingKey();
  }
}
