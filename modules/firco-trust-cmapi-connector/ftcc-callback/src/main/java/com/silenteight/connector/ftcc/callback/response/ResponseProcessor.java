package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.callback.outgoing.RecommendationsDeliveredEvent;
import com.silenteight.connector.ftcc.callback.outgoing.RecommendationsDeliveredPublisher;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto;
import com.silenteight.connector.ftcc.common.dto.output.ReceiveDecisionMessageDto;
import com.silenteight.connector.ftcc.common.resource.BatchResource;
import com.silenteight.connector.ftcc.common.resource.MessageResource;
import com.silenteight.connector.ftcc.request.details.MessageDetailsQuery;
import com.silenteight.connector.ftcc.request.details.dto.MessageDetailsDto;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;
import com.silenteight.recommendation.api.library.v1.RecommendationsOut;

import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.lineSeparator;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Component
@Slf4j
public class ResponseProcessor {

  private final ResponseCreator responseCreator;
  private final RecommendationSender recommendationSender;
  private final RecommendationClientApi recommendationClientApi;
  private final MessageDetailsQuery messageDetailsQuery;
  private final RecommendationsDeliveredPublisher recommendationsDeliveredPublisher;

  @Async
  public void process(MessageBatchCompleted messageBatchCompleted) {
    final var analysisName = messageBatchCompleted.getAnalysisId();
    final var batchName = messageBatchCompleted.getBatchId();

    log.info("Fetching Message from DB using batchName={}", batchName);
    var messageEntityMap =
        map(messageDetailsQuery.details(BatchResource.fromResourceName(batchName)));

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
      String analysisName,
      Map<UUID, MessageDetailsDto> messageDetailsMap,
      RecommendationsOut recommendations) {

    var decisionMessageDtos = recommendations
        .getRecommendations()
        .stream()
        .peek(recommendationOut -> logRecommendationOut(recommendationOut, analysisName))
        .flatMap(recommendation -> buildMessageHandleException(messageDetailsMap, recommendation))
        .peek(clientRequestDto -> logReceiveDecisionMessageDto(clientRequestDto, analysisName))
        .collect(Collectors.toList());

    return responseCreator.build(decisionMessageDtos);
  }

  private Stream<ReceiveDecisionMessageDto> buildMessageHandleException(
      Map<UUID, MessageDetailsDto> messageDetailsMap, RecommendationOut recommendation) {
    try {
      return Stream.of(createMessageDto(messageDetailsMap, recommendation));
    } catch (ResponseMessageBuildingExceptoin e) {
      return Stream.empty();
    }
  }

  private ReceiveDecisionMessageDto createMessageDto(
      Map<UUID, MessageDetailsDto> messageDetailsMap, RecommendationOut recommendation) {

    var messageDetails = of(messageDetailsMap.get(uuidFrom(recommendation))).orElseThrow();
    return responseCreator.buildMessageDto(messageDetails, recommendation);
  }

  @NotNull
  private static UUID uuidFrom(RecommendationOut recommendation) {
    return MessageResource.fromResourceName(recommendation.getAlert().getId());
  }

  @NotNull
  private static Map<UUID, MessageDetailsDto> map(List<MessageDetailsDto> list) {
    return list
        .stream()
        .collect(toMap(MessageDetailsDto::getId, Function.identity()));
  }

  private static void logRecommendationOut(
      RecommendationOut recommendationOut, String analysisName) {

    if (log.isDebugEnabled()) {
      log.debug(
          "RecommendationOut for analysisName={}{}{}", analysisName, lineSeparator(),
          recommendationOut);
    }
  }

  private static void logReceiveDecisionMessageDto(
      ReceiveDecisionMessageDto messageDto, String analysisName) {

    if (log.isDebugEnabled()) {
      log.debug(
          "Generated ReceiveDecisionMessageDto for analysisName={}{}{}", analysisName,
          lineSeparator(),
          messageDto);
    }
  }

  private static void logClientRequestDto(ClientRequestDto clientRequestDto, String analysisName) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Generated ClientRequestDto for analysisName={}{}{}", analysisName, lineSeparator(),
          clientRequestDto);
    }
  }
}
