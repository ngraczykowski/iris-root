package com.silenteight.serp.governance.common.integration;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
class AmqpInboundProperties {

  @NonNull
  private String queueName;
}
