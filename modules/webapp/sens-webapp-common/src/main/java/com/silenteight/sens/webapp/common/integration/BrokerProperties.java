package com.silenteight.sens.webapp.common.integration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "sens.webapp.messaging.broker")
class BrokerProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpProperties auditLog;

  String auditLogQueueName() {
    return auditLog.getQueueName();
  }

  String auditLogRoutingKey() {
    return auditLog.getRoutingKey();
  }
}
