package com.silenteight.connector.ftcc.callback.response;

import lombok.NonNull;
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
import java.util.function.Function;

import static com.silenteight.connector.ftcc.common.resource.MessageResource.fromResourceName;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
@RequiredArgsConstructor
public class ClientRequestDtoBuilder {

  private static final String ERROR_GENERATING_CLIENT_REQUEST =
      "Error generating ClientRequestDto from Recommendations for batchName={}, analysisName={}";
  protected static final RecommendationOut EMPTY_RECOMMENDATION = RecommendationOut.builder()
      .build();

  @NonNull
  private final ResponseCreator responseCreator;
  @NonNull
  private final MessageDetailsService messageDetailsService;
  private final boolean loggingActive;

  ClientRequestDto build(
      String batchName, String analysisName, RecommendationsOut recommendations) {

    log.info("Fetching Message from DB using batchName={}", batchName);
    var messageDetailsMap = messageDetailsService.messages(batchName);
    var messageIdToRecommendationMap = getRecommendationMap(recommendations);

    try {
      var decisionMessageDtos = messageDetailsMap.values()
          .stream()
          .map(messageDetails -> {
            var recommendation = messageIdToRecommendationMap
                .getOrDefault(messageDetails.getId(), EMPTY_RECOMMENDATION);
            log(recommendation, batchName, analysisName);
            return createMessageDto(messageDetails, recommendation);
          })
          .peek(clientRequestDto -> log(clientRequestDto, batchName, analysisName))
          .collect(toList());
      return responseCreator.build(decisionMessageDtos);
    } catch (Exception exc) {
      log.error(ERROR_GENERATING_CLIENT_REQUEST, batchName, analysisName, exc);
      throw new NonRecoverableCallbackException(exc);
    }
  }

  private ReceiveDecisionMessageDto createMessageDto(
      MessageDetailsDto messageDetails, RecommendationOut recommendation) {
    return responseCreator.buildMessageDto(messageDetails, recommendation);
  }

  private void log(RecommendationOut recommendationOut, String batchName, String analysisName) {
    if (loggingActive) {
      log.debug(
          "RecommendationOut for batchName={}, analysisName={}{}{}", batchName, analysisName,
          lineSeparator(), recommendationOut);
    }
  }

  private void log(ReceiveDecisionMessageDto messageDto, String batchName, String analysisName) {
    if (loggingActive) {
      log.debug(
          "Generated ReceiveDecisionMessageDto for batchName={}, analysisName={}{}{}", batchName,
          analysisName, lineSeparator(), messageDto);
    }
  }

  private static Map<UUID, RecommendationOut> getRecommendationMap(
      RecommendationsOut recommendations) {
    return recommendations.getRecommendations()
        .stream()
        .collect(
            toMap(
                recommendationOut -> fromResourceName(recommendationOut.getAlert().getId()),
                Function.identity()));
  }
}
