package com.silenteight.payments.bridge.ae.alertregistration.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.port.CreateAnalysisUseCase;
import com.silenteight.proto.payments.bridge.internal.v1.event.ModelUpdated;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
class ModelUpdatedIntegrationFlow extends IntegrationFlowAdapter {

  private final CreateAnalysisUseCase createAnalysisUseCase;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from("SomeChannelToBeSetLater")
        .handle(ModelUpdated.class, (payload, headers) ->
            createAnalysisUseCase.createAnalysis());
  }
}
