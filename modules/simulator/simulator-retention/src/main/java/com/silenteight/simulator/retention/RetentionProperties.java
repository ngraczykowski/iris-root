package com.silenteight.simulator.retention;

import lombok.Data;

import com.silenteight.simulator.common.integration.AmqpInboundProperties;
import com.silenteight.simulator.common.integration.AmqpOutboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "simulator.retention")
public class RetentionProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties datasetExpiredInbound;

  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties simulationExpiredOutbound;

  public String datasetExpiredInboundQueueName() {
    return datasetExpiredInbound.getQueueName();
  }

  public String simulationExpiredOutboundExchange() {
    return simulationExpiredOutbound.getExchange();
  }

  public String simulationExpiredOutboundRoutingKey() {
    return simulationExpiredOutbound.getRoutingKey();
  }
}
