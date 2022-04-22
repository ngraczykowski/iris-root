package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.callback.exception.NonRecoverableCallbackException;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto;
import com.silenteight.connector.ftcc.common.dto.output.ReceiveDecisionMessageDto;
import com.silenteight.connector.ftcc.request.details.dto.MessageDetailsDto;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;
import com.silenteight.recommendation.api.library.v1.RecommendationsOut;

import java.util.Map;
import java.util.UUID;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class ClientRequestDtoBuilder {

  private static final String ERROR_GENERATING_CLIENT_REQUEST =
      "Error generating ClientRequestDto from Recommendations for batchName={}, analysisName={}";
  private final ResponseCreator responseCreator;

  private final MessageDetailsService messageDetailsService;

  ClientRequestDto build(
      String batchName, String analysisName, RecommendationsOut recommendations) {

    log.info("Fetching Message from DB using batchName={}", batchName);
    var messageDetailsMap = messageDetailsService.messages(batchName);

    try {
      var decisionMessageDtos = recommendations
          .getRecommendations()
          .stream()
          .peek(recommendationOut -> log(recommendationOut, batchName, analysisName))
          .map(recommendation -> createMessageDto(messageDetailsMap, recommendation))
          .peek(clientRequestDto -> log(clientRequestDto, batchName, analysisName))
          .collect(toList());
      return responseCreator.build(decisionMessageDtos);
    } catch (Exception exc) {
      log.error(ERROR_GENERATING_CLIENT_REQUEST, batchName, analysisName, exc);
      throw new NonRecoverableCallbackException(exc);
    }
  }

  private ReceiveDecisionMessageDto createMessageDto(
      Map<UUID, MessageDetailsDto> messageDetailsMap, RecommendationOut recommendation) {
    var messageDetails = messageDetailsService.messageFrom(
        messageDetailsMap,
        recommendation.getAlert().getId());
    return responseCreator.buildMessageDto(messageDetails, recommendation);
  }

  private static void log(
      RecommendationOut recommendationOut, String batchName, String analysisName) {
    if (log.isDebugEnabled()) {
      log.debug(
          "RecommendationOut for batchName={}, analysisName={}{}{}", batchName, analysisName,
          lineSeparator(),
          recommendationOut);
    }
  }

  private static void log(
      ReceiveDecisionMessageDto messageDto, String batchName, String analysisName) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Generated ReceiveDecisionMessageDto for batchName={}, analysisName={}{}{}", batchName,
          analysisName,
          lineSeparator(), messageDto);
    }
  }
}
