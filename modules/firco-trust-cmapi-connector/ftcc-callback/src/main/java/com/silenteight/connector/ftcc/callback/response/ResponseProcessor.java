package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.callback.response.domain.MessageEntity;
import com.silenteight.connector.ftcc.callback.response.domain.MessageQuery;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto;
import com.silenteight.connector.ftcc.common.resource.BatchResource;
import com.silenteight.connector.ftcc.common.resource.MessageResource;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;

import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class ResponseProcessor {

  private final ResponseCreator responseCreator;
  private final RecommendationSender recommendationSender;
  private final RecommendationClientApi recommendationClientApi;
  private final MessageQuery messageQuery;

  @Async
  public void process(MessageBatchCompleted messageBatchCompleted) {
    var analysisId = messageBatchCompleted.getAnalysisId();

    var messageEntityMap = map(messageQuery.findByBatchId(
        BatchResource.fromResourceName(messageBatchCompleted.getBatchId())));
    var recommendations = recommendationClientApi.recommendation(analysisId);

    recommendations
        .getRecommendations()
        .forEach(recommendation -> recommendationSender.send(
            createRequest(messageEntityMap, recommendation)));
  }

  private ClientRequestDto createRequest(
      Map<UUID, MessageEntity> messageEntityMap, RecommendationOut recommendation) {
    var messageEntity = Optional.of(messageEntityMap.get(uuidFrom(recommendation)))
        .orElseThrow();
    return responseCreator.create(messageEntity, recommendation);
  }

  @NotNull
  private static UUID uuidFrom(RecommendationOut recommendation) {
    return MessageResource.fromResourceName(recommendation.getAlert().getId());
  }

  @NotNull
  private static Map<UUID, MessageEntity> map(List<MessageEntity> list) {
    return list.stream()
        .collect(Collectors.toMap(MessageEntity::getId, Function.identity()));
  }
}
