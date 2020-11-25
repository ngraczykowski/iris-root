package com.silenteight.serp.governance.bulkchange.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class BulkChangeCommandAmqpProperties {

  @NotBlank
  private String inboundQueueName;

  @NotBlank
  private String outboundExchangeName;

  @NotNull
  private String outboundRoutingKey;
}
