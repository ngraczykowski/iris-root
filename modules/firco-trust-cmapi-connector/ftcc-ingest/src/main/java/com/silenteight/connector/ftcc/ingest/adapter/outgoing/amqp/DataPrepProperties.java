package com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp;

import lombok.Data;

import com.silenteight.connector.ftcc.common.integration.AmqpOutboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "ftcc.data-prep")
class DataPrepProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties outbound;

  String outboundExchange() {
    return outbound.getExchange();
  }

  String outboundRoutingKey() {
    return outbound.getRoutingKey();
  }
}