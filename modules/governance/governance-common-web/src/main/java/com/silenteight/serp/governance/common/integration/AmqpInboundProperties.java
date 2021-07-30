package com.silenteight.serp.governance.common.integration;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmqpInboundProperties {

  @NonNull
  private String queueName;
}
