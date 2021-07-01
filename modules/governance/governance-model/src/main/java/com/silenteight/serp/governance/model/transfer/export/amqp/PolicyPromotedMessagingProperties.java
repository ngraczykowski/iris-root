package com.silenteight.serp.governance.model.transfer.export.amqp;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties("serp.governance.model.export.integration.request")
@ConstructorBinding
@Value
class PolicyPromotedMessagingProperties {

  @NotBlank
  String exchange;

  @NotBlank
  String routingKey;
}
