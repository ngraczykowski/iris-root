package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class RequestMissingFeatureValuesUseCase {

  private final MissingMatchFeatureReader missingMatchFeatureReader;
  private final AgentRequestHandler agentRequestHandler;
  private final AgentExchangeRequestGateway gateway;

  void requestMissingFeatureValues(String analysisName) {
    var analysisId = ResourceName.create(analysisName).getLong("analysis");

    log.info("Requesting missing feature values: analysisId={}", analysisId);

    // NOTE(ahaczewski): We send requests in batches, handling errors along the way.
    var totalCount = 0;
    var matchCount = 0;
    do {
      matchCount = missingMatchFeatureReader.read(
          analysisId, agentRequestHandler.createChunkHandler(this::sendRequests));
      totalCount += matchCount;
    } while (matchCount > 0);

    log.info("Finished requesting missing feature values: analysisId={}, matchCount={}",
        analysisId, totalCount);
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
