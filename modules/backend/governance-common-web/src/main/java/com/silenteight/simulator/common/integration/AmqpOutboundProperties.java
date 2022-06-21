package com.silenteight.simulator.common.integration;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class AmqpOutboundProperties {

  @NotBlank
  String exchange;
  @NotBlank
  String routingKey;
}
