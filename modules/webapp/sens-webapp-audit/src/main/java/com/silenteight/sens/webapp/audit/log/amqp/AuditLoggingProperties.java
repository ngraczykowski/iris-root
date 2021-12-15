package com.silenteight.sens.webapp.audit.log.amqp;

import lombok.Data;

import com.silenteight.sens.webapp.common.integration.AmqpInboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "sens.webapp.audit-log")
public class AuditLoggingProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties inbound;

  public String inboundQueueName() {
    return inbound.getQueueName();
  }
}
