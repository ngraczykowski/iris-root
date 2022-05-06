package com.silenteight.connector.ftcc.common.integration;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;

@Value
@ConstructorBinding
@RequiredArgsConstructor
public class AmqpOutboundProperties {

  @NotBlank
  String routingKey;
}
