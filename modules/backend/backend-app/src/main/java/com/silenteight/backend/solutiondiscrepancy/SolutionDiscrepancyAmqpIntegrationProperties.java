/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend.solutiondiscrepancy;

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
