package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class RequestMissingFeatureValuesUseCase {

  private final MissingMatchFeatureReader missingMatchFeatureReader;
  private final AgentRequestHandler agentRequestHandler;
  private final AgentExchangeRequestGateway gateway;

  void requestMissingFeatureValues(PendingRecommendations pendingRecommendations) {
    pendingRecommendations.getAnalysisList().forEach(this::doRequestMissingFeatureValues);
  }

  private void doRequestMissingFeatureValues(String analysisName) {
    var analysisId = ResourceName.create(analysisName).getLong("analysis");

    if (log.isDebugEnabled()) {
      log.debug("Requesting missing feature values: analysisId={}", analysisId);
    }

    // NOTE(ahaczewski): We send requests in batches, handling errors along the way.
    var matchCount = 0;
    do {
      matchCount = missingMatchFeatureReader.read(
          analysisId, agentRequestHandler.createChunkHandler(this::sendRequests));
    } while (matchCount > 0);
  }

  private void sendRequests(List<AgentExchangeRequestMessage> messages) {
    if (log.isDebugEnabled()) {
      log.debug("Sending agent requests: messageCount={}", messages.size());
    }

    // NOTE(ahaczewski): Sending messages outside of the transaction.
    // TODO(ahaczewski): Handle errors sending the messages. Should delete affected exchanges
    //  from the database.
    messages.forEach(gateway::send);
  }
}
