package com.silenteight.serp.governance.common.integration;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
class AmqpOutboundProperties {

  @NonNull
  private String exchangeName;
  @NonNull
  private String routingKey;
}
