package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.callback.outgoing.RecommendationsDeliveredEvent;
import com.silenteight.connector.ftcc.callback.outgoing.RecommendationsDeliveredPublisher;
import com.silenteight.connector.ftcc.callback.response.domain.MessageEntity;
import com.silenteight.connector.ftcc.callback.response.domain.MessageQuery;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto;
import com.silenteight.connector.ftcc.common.dto.output.ReceiveDecisionMessageDto;
import com.silenteight.connector.ftcc.common.resource.BatchResource;
import com.silenteight.connector.ftcc.common.resource.MessageResource;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;
import com.silenteight.recommendation.api.library.v1.RecommendationsOut;

import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.lineSeparator;

@RequiredArgsConstructor
@Component
@Slf4j
public class ResponseProcessor {

  private final ResponseCreator responseCreator;
  private final RecommendationSender recommendationSender;
  private final RecommendationClientApi recommendationClientApi;
  private final MessageQuery messageQuery;
  private final RecommendationsDeliveredPublisher recommendationsDeliveredPublisher;

  @Async
  public void process(MessageBatchCompleted messageBatchCompleted) {
    final var analysisName = messageBatchCompleted.getAnalysisId();
    final var batchName = messageBatchCompleted.getBatchId();

    log.info("Fetching Message from DB using batchName={}", batchName);
    var messageEntityMap =
        map(messageQuery.findByBatchId(BatchResource.fromResourceName(batchName)));

    log.info("Requesting Recommendation using analysisName={}", analysisName);
    var recommendations = recommendationClientApi.recommendation(analysisName);
    var responseDto = buildReuqest(analysisName, messageEntityMap, recommendations);

    logClientRequestDto(responseDto, analysisName);

    recommendationSender.send(responseDto);
    recommendationsDeliveredPublisher.publish(RecommendationsDeliveredEvent
        .builder()
        .batchName(batchName)
        .analysisName(analysisName)
        .build());
  }

  private ClientRequestDto buildReuqest(
      String analysisName, Map<UUID, MessageEntity> messageEntityMap,
      RecommendationsOut recommendations) {
    var decisionMessageDtos = recommendations
        .getRecommendations()
        .stream()
        .peek(recommendationOut -> logRecommendationOut(recommendationOut, analysisName))
        .flatMap(recommendation -> buildMessageHandleException(messageEntityMap, recommendation))
        .peek(clientRequestDto -> logReceiveDecisionMessageDto(clientRequestDto, analysisName))
        .collect(Collectors.toList());

    return responseCreator.build(decisionMessageDtos);
  }

  private Stream<ReceiveDecisionMessageDto> buildMessageHandleException(
      Map<UUID, MessageEntity> messageEntityMap, RecommendationOut recommendation) {
    try {
      return Stream.of(createMessageDto(messageEntityMap, recommendation));
    } catch (ResponseMessageBuildingExceptoin e) {
      return Stream.empty();
    }
  }

  private ReceiveDecisionMessageDto createMessageDto(
      Map<UUID, MessageEntity> messageEntityMap, RecommendationOut recommendation) {
    var messageEntity = Optional.of(messageEntityMap.get(uuidFrom(recommendation)))
        .orElseThrow();
    try {
      return responseCreator.buildMessageDto(messageEntity, recommendation);
    } catch (Exception e) {
      log.error("Error while building MessageDto!!!", e);
      throw new ResponseMessageBuildingExceptoin(e);
    }
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

  private void logRecommendationOut(RecommendationOut recommendationOut, String analysisName) {
    if (log.isDebugEnabled()) {
      log.debug(
          "RecommendationOut for analysisName={}{}{}", analysisName, lineSeparator(),
          recommendationOut);
    }
  }

  private void logReceiveDecisionMessageDto(
      ReceiveDecisionMessageDto messageDto, String analysisName) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Generated ReceiveDecisionMessageDto for analysisName={}{}{}", analysisName,
          lineSeparator(),
          messageDto);
    }
  }

  private void logClientRequestDto(ClientRequestDto clientRequestDto, String analysisName) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Generated ClientRequestDto for analysisName={}{}{}", analysisName, lineSeparator(),
          clientRequestDto);
    }
  }
}
