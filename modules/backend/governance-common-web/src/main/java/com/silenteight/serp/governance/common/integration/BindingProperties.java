package com.silenteight.serp.governance.common.integration;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
class BindingProperties {

  @NotBlank
  String queueName;

  @NotBlank
  String exchange;

  @NotBlank
  String routingKey;
}
