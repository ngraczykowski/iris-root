package com.silenteight.serp.governance.model.accept.amqp;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties("serp.governance.model.export.integration.request")
@ConstructorBinding
@Data
class PolicyPromotedMessagingProperties {

  @NotBlank
  String exchange;

  @NotBlank
  String routingKey;
}
