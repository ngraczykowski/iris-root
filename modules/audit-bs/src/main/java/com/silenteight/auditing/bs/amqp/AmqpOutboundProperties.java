package com.silenteight.auditing.bs.amqp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
class AmqpOutboundProperties {

  @NonNull
  private String exchange;
  @NonNull
  private String routingKey;
}
