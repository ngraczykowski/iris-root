package com.silenteight.serp.governance.qa.send.amqp;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties("serp.governance.qa.integration.request")
@ConstructorBinding
@Data
class AlertMessagingProperties {

  @NotBlank
  String exchange;

  @NotBlank
  String routingKey;
}
