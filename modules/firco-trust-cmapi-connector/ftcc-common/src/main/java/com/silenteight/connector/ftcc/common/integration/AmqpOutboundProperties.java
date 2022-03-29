package com.silenteight.connector.ftcc.common.integration;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class AmqpOutboundProperties {

  @NotBlank
  String routingKey;
}
