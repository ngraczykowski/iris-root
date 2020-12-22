package com.silenteight.serp.governance.notifier;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "serp.notifier.integration")
@ConstructorBinding
@Value
class NotifierIntegrationProperties {

  @NotBlank
  String inboundQueueName;

  @NotNull
  String outboundExchangeName;

  @NotNull
  String outboundRoutingKey;
}
