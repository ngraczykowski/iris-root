package com.silenteight.warehouse.common.integration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.messaging.broker")
class BrokerProperties {

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties alertBackupIndexing;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties qaBackupIndexing;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties alertProductionIndexing;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties qaIndexing;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties alertSimulationIndexing;

  String alertBackupIndexingQueueName() {
    return alertBackupIndexing.getQueueName();
  }

  String alertBackupIndexingRoutingKey() {
    return alertBackupIndexing.getRoutingKey();
  }

  String qaBackupIndexingQueueName() {
    return qaBackupIndexing.getQueueName();
  }

  String qaBackupIndexingRoutingKey() {
    return qaBackupIndexing.getRoutingKey();
  }

  String alertProductionIndexingQueueName() {
    return alertProductionIndexing.getQueueName();
  }

  String alertProductionIndexingRoutingKey() {
    return alertProductionIndexing.getRoutingKey();
  }

  String qaIndexingQueueName() {
    return qaIndexing.getQueueName();
  }

  String qaIndexingRoutingKey() {
    return qaIndexing.getRoutingKey();
  }

  String alertSimulationIndexingQueueName() {
    return alertSimulationIndexing.getQueueName();
  }

  String alertSimulationIndexingRoutingKey() {
    return alertSimulationIndexing.getRoutingKey();
  }
}
