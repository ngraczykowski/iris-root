package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class RequestMissingFeatureValuesUseCase {

  private final MissingMatchFeatureReader missingMatchFeatureReader;
  private final AgentRequestHandler agentRequestHandler;
  private final AgentExchangeRequestGateway gateway;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "agentexchange" })
  void requestMissingFeatureValues(String analysisName) {
    var analysisId = ResourceName.create(analysisName).getLong("analysis");

    log.info("Requesting missing feature values: analysisId={}", analysisId);

    var matchCount = missingMatchFeatureReader.read(
        analysisId, agentRequestHandler.createChunkHandler(this::sendRequests));

    if (matchCount > 0) {
      log.info("Finished requesting missing feature values: analysisId={}, matchCount={}",
          analysisId, matchCount);
    } else {
      log.debug("No missing feature values requested: analysisId={}", analysisId);
    }
  }

  private void sendRequests(List<AgentExchangeRequestMessage> messages) {
    if (messages.isEmpty()) {
      log.debug("No agent exchange requests to send");
      return;
    }

    if (log.isDebugEnabled()) {
      log.debug("Sending agent requests: messageCount={}", messages.size());
    }

    // NOTE(ahaczewski): Sending messages outside of the transaction.
    // TODO(ahaczewski): Handle errors sending the messages. Should delete affected exchanges
    //  from the database.
    messages.forEach(gateway::send);
  }
}
