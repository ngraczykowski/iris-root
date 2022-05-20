package com.silenteight.adjudication.engine.analysis.analysis.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.analysis.AnalysisCancelledUseCase;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.analysis.integration.AnalysisChannels.ANALYSIS_ALERTS_CANCELLED_INBOUND_CHANNEL;

@RequiredArgsConstructor
@Component
@Slf4j
class AnalysisCancelledIntegrationFlow extends IntegrationFlowAdapter {

  private final AnalysisCancelledUseCase analysisCancelledUseCase;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(ANALYSIS_ALERTS_CANCELLED_INBOUND_CHANNEL)
        .handle(String.class, this::handleResponse);
  }

  private int handleResponse(String payload, MessageHeaders headers) {

    var analysisId = ResourceName.create(payload).getLong("analysis");

    if (log.isDebugEnabled()) {
      log.debug("Received analysis id to cancel = {}", analysisId);
    }

    analysisCancelledUseCase.cancelAnalysis(analysisId);
    return 0;
  }
}
