package com.silenteight.auditing.bs.amqp;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "audit-bs.integration")
class AuditingAmqpProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties request;

  public String outboundExchange() {
    return request.getExchange();
  }

  public String outboundRoutingKey() {
    return request.getRoutingKey();
  }
}
