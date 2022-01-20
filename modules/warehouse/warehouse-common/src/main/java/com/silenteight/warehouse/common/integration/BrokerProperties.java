package com.silenteight.warehouse.common.integration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import java.util.List;
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
  private AmpqProperties alertProductionIndexing;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties qaIndexing;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties alertSimulationIndexing;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties personalInformationExpiredIndexing;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties alertsExpiredIndexing;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties analysisExpiredIndexing;

  @Valid
  @NestedConfigurationProperty
  private ToRemoveProperties toRemove;

  String alertBackupIndexingQueueName() {
    return alertBackupIndexing.getQueueName();
  }

  String alertBackupIndexingRoutingKey() {
    return alertBackupIndexing.getRoutingKey();
  }

  Integer alertBackupIndexingMaxPriority() {
    return alertBackupIndexing.getMaxPriority();
  }

  String alertProductionIndexingQueueName() {
    return alertProductionIndexing.getQueueName();
  }

  String alertProductionIndexingRoutingKey() {
    return alertProductionIndexing.getRoutingKey();
  }

  Integer alertProductionIndexingMaxPriority() {
    return alertBackupIndexing.getMaxPriority();
  }

  String qaIndexingQueueName() {
    return qaIndexing.getQueueName();
  }

  String qaIndexingRoutingKey() {
    return qaIndexing.getRoutingKey();
  }

  Integer qaIndexingMaxPriority() {
    return alertBackupIndexing.getMaxPriority();
  }

  String alertSimulationIndexingQueueName() {
    return alertSimulationIndexing.getQueueName();
  }

  String alertSimulationIndexingRoutingKey() {
    return alertSimulationIndexing.getRoutingKey();
  }

  Integer alertSimulationIndexingMaxPriority() {
    return alertBackupIndexing.getMaxPriority();
  }

  String personalInformationExpiredIndexingQueueName() {
    return personalInformationExpiredIndexing.getQueueName();
  }

  String personalInformationExpiredIndexingRoutingKey() {
    return personalInformationExpiredIndexing.getRoutingKey();
  }

  Integer personalInformationExpiredIndexingMaxPriority() {
    return personalInformationExpiredIndexing.getMaxPriority();
  }

  String analysisExpiredQueueName() {
    return analysisExpiredIndexing.getQueueName();
  }

  String analysisExpiredRoutingKey() {
    return analysisExpiredIndexing.getRoutingKey();
  }

  Integer analysisExpiredIndexingMaxPriority() {
    return analysisExpiredIndexing.getMaxPriority();
  }

  List<BindingProperties> bindingsToRemove() {
    return toRemove.getBindings();
  }

  String alertsExpiredIndexingQueueName() {
    return alertsExpiredIndexing.getQueueName();
  }

  String alertsExpiredIndexingRoutingKey() {
    return alertsExpiredIndexing.getRoutingKey();
  }

  Integer alertsExpiredIndexingMaxPriority() {
    return alertsExpiredIndexing.getMaxPriority();
  }
}
