package com.silenteight.serp.governance.solutiondiscrepancy;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@ConfigurationProperties(prefix = "serp.governance.integration.solution-discrepancy")
@ConstructorBinding
class SolutionDiscrepancyAmqpIntegrationProperties {

  @NotBlank
  String inboundQueueName;

  @NotNull
  String outboundExchangeName;

  @NotNull
  String reasoningBranchesDisabledRoutingKey;
}
