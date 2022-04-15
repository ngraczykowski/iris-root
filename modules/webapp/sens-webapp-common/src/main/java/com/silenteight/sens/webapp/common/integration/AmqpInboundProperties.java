package com.silenteight.sens.webapp.common.integration;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class AmqpInboundProperties {

  @NotBlank
  String queueName;
}
