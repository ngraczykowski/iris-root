package com.silenteight.warehouse.common.integration;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
class AmpqProperties {

  @NotBlank
  String queueName;

  @NotBlank
  String routingKey;
}
