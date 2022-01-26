package com.silenteight.serp.governance.common.integration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "serp.governance.messaging.broker")
class BrokerProperties {

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties analytics;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties ingest;

  @Valid
  @NestedConfigurationProperty
  private RoutingKeyProperties modelExport;

  @Valid
  @NestedConfigurationProperty
  private RoutingKeyProperties modelInUse;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties modelsArchived;

  @Valid
  @NestedConfigurationProperty
  private RoutingKeyProperties govEvents;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties qaRetentionPersonalInformationExpired;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties qaRetentionAlertsExpired;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties notification;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties notificationSendMail;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties solutionDiscrepancy;

  @Valid
  @NestedConfigurationProperty
  private ToRemoveProperties toRemove;

  String analyticsQueueName() {
    return analytics.getQueueName();
  }

  String analyticsRoutingKey() {
    return analytics.getRoutingKey();
  }

  String ingestQueueName() {
    return ingest.getQueueName();
  }

  String ingestRoutingKey() {
    return ingest.getRoutingKey();
  }

  String modelExportRoutingKey() {
    return modelExport.getRoutingKey();
  }

  String modelInUseRoutingKey() {
    return modelInUse.getRoutingKey();
  }

  String modelsArchivedQueueName() {
    return modelsArchived.getQueueName();
  }

  String modelsArchivedRoutingKey() {
    return modelsArchived.getRoutingKey();
  }

  String govEventsRoutingKey() {
    return govEvents.getRoutingKey();
  }

  String qaRetentionPersonalInformationExpiredQueueName() {
    return qaRetentionPersonalInformationExpired.getQueueName();
  }

  String qaRetentionPersonalInformationExpiredRoutingKey() {
    return qaRetentionPersonalInformationExpired.getRoutingKey();
  }

  String qaRetentionAlertsExpiredQueueName() {
    return qaRetentionAlertsExpired.getQueueName();
  }

  String qaRetentionAlertsExpiredRoutingKey() {
    return qaRetentionAlertsExpired.getRoutingKey();
  }

  String notificationQueueName() {
    return notification.getQueueName();
  }

  String notificationRoutingKey() {
    return notification.getRoutingKey();
  }

  String notificationSendMailQueueName() {
    return notificationSendMail.getQueueName();
  }

  String notificationSendMailRoutingKey() {
    return notificationSendMail.getRoutingKey();
  }

  String solutionDiscrepancyQueueName() {
    return solutionDiscrepancy.getQueueName();
  }

  String solutionDiscrepancyRoutingKey() {
    return solutionDiscrepancy.getRoutingKey();
  }

  List<BindingProperties> bindingsToRemove() {
    return toRemove.getBindings();
  }

  List<String> exchangesToRemove() {
    return toRemove.getExchanges();
  }
}
