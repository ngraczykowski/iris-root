package com.silenteight.warehouse.common.integration;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AmqpInboundProperties {

  @NonNull
  private String queueName;
}
