package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.callback.response.domain.MessageQuery;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;

import com.google.protobuf.ByteString;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class ResponseProcessor {

  private final ResponseCreator responseCreator;
  private final RecommendationSender recommendationSender;
  private final RecommendationClientApi recommendationClientApi;
  private final MessageQuery messageDataAccess;

  @Async
  public void process(MessageBatchCompleted messageBatchCompleted) {
    var analysisId = messageBatchCompleted.getAnalysisId();
    // TODO: In recommendation-lib:1.9.0 alertIds will be removed !!!
    var alertIds = messageBatchCompleted
        .getAlertIdsList()
        .asByteStringList()
        .stream()
        .map(ByteString::toStringUtf8)
        .collect(Collectors.toList());
    if (log.isDebugEnabled()) {
      log.debug("BatchCompleted, alertIds: {}", alertIds);
    }
    var messageEntities = messageDataAccess.findByBatchId(messageBatchCompleted.getBatchId());
    var recommendation = recommendationClientApi.recommendation(analysisId);

    recommendation
        .getRecommendations()
        .forEach(recommendationOut -> {
          var clientRequestDto = responseCreator.create(
              messageEntities,
              recommendation);
          recommendationSender.send(clientRequestDto);
        });
  }
}
