package com.silenteight.simulator.model.archive.amqp;

import lombok.Data;

import com.silenteight.simulator.common.integration.AmqpInboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "simulator.model-archiving")
public class ModelArchivingProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties modelsArchivedInbound;

  public String modelsArchivedInboundQueueName() {
    return modelsArchivedInbound.getQueueName();
  }
}
