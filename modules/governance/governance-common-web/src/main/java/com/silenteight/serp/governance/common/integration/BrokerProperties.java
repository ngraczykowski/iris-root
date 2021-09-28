package com.silenteight.serp.governance.common.integration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

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
  private AmpqProperties qaRetention;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties notification;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties notificationSendMail;

  @Valid
  @NestedConfigurationProperty
  private AmpqProperties solutionDiscrepancy;

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

  String qaRetentionQueueName() {
    return qaRetention.getQueueName();
  }

  String qaRetentionRoutingKey() {
    return qaRetention.getRoutingKey();
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
}
