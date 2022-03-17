package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.callback.RecommendationClientApi;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;

import com.google.protobuf.ByteString;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ResponseProcessor {

  private final ResponseCreator responseCreator;
  private final RecommendationSender recommendationSender;
  private final RecommendationClientApi recommendationClientApi;

  @Async
  public void process(MessageBatchCompleted messageBatchCompleted) {
    var alertIds = messageBatchCompleted
        .getAlertIdsList()
        .asByteStringList()
        .stream()
        .map(ByteString::toStringUtf8)
        .collect(Collectors.toList());
    if (log.isDebugEnabled()) {
      log.debug("BatchCompleted, alertIds: {}", alertIds);
    }

    recommendationClientApi.recommendation(alertIds)
        .getRecommendations()
        .forEach(recommendationOut -> {
          var clientRequestDto = responseCreator.create(
              messageBatchCompleted,
              recommendationClientApi.recommendation(alertIds));
          recommendationSender.send(clientRequestDto);
        });
  }
}
