package com.silenteight.serp.governance.solutiondiscrepancy;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.circuitbreaker.SolutionDiscrepancyDetectedEvent;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class SolutionDiscrepancyIntegrationFlowAdapter extends IntegrationFlowAdapter {

  static final String SOLUTION_DISCREPANCY_INBOUND_CHANNEL = "solutionDiscrepancyInbound";
  static final String SOLUTION_DISCREPANCY_OUTBOUND_CHANNEL = "solutionDiscrepancyOutbound";

  private final SolutionDiscrepancyHandler solutionDiscrepancyHandler;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(SOLUTION_DISCREPANCY_INBOUND_CHANNEL)
        .handle(
            SolutionDiscrepancyDetectedEvent.class,
            (p, h) -> solutionDiscrepancyHandler.disableReasoningBranches(p))
        .channel(SOLUTION_DISCREPANCY_OUTBOUND_CHANNEL);
  }
}
