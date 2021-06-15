package com.silenteight.warehouse.test.integration;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class AmqpOutboundProperties {

  @NotBlank
  String exchangeName;
  @NotBlank
  String routingKey;
}
