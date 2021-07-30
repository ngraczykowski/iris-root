package com.silenteight.serp.governance.common.integration;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmqpOutboundProperties {

  @NonNull
  private String exchangeName;
  @NonNull
  private String routingKey;
}
