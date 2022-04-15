package com.silenteight.serp.governance.model.archive.amqp;

import lombok.Data;

import com.silenteight.serp.governance.common.integration.AmqpInboundProperties;
import com.silenteight.serp.governance.common.integration.AmqpOutboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "serp.governance.model.archive.integration")
public class ModelArchivingProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties receive;

  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties request;

  public String modelArchivingOutboundExchangeName() {
    return request.getExchange();
  }

  public String modelArchivingOutboundRoutingKey() {
    return request.getRoutingKey();
  }

  public String modelArchivingInboundQueueName() {
    return receive.getQueueName();
  }
}
