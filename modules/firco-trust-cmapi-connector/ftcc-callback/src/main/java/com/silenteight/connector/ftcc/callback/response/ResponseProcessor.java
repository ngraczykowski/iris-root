package com.silenteight.connector.ftcc.callback.response;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.callback.outgoing.RecommendationsDeliveredEvent;
import com.silenteight.connector.ftcc.callback.outgoing.RecommendationsDeliveredPublisher;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;

import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;

import static com.silenteight.connector.ftcc.common.MdcParams.BATCH_NAME;
import static java.lang.System.lineSeparator;

@RequiredArgsConstructor
@Slf4j
public class ResponseProcessor {

  @NonNull
  private final ClientRequestDtoBuilder clientRequestDtoBuilder;
  @NonNull
  private final RecommendationSender recommendationSender;
  @NonNull
  private final RecommendationClientApi recommendationClientApi;
  @NonNull
  private final RecommendationsDeliveredPublisher recommendationsDeliveredPublisher;
  private final boolean loggingActive;

  @Async
  public void process(MessageBatchCompleted messageBatchCompleted) {
    final var analysisName = messageBatchCompleted.getAnalysisName();
    final var batchName = messageBatchCompleted.getBatchId();
    MDC.put(BATCH_NAME, batchName);
    try {
      log.info("Requesting Recommendation using analysisName={}", analysisName);
      var recommendations = recommendationClientApi.recommendation(analysisName);
      var responseDto =
          clientRequestDtoBuilder.build(batchName, analysisName, recommendations);

      if (loggingActive)
        logClientRequestDto(responseDto, analysisName);

      recommendationSender.send(batchName, responseDto);
      recommendationsDeliveredPublisher.publish(RecommendationsDeliveredEvent
          .builder()
          .batchName(batchName)
          .analysisName(analysisName)
          .build());
    } finally {
      MDC.remove(BATCH_NAME);
    }
  }

  private static void logClientRequestDto(ClientRequestDto clientRequestDto, String analysisName) {
    log.debug(
        "Generated ClientRequestDto for analysisName={}{}{}", analysisName, lineSeparator(),
        clientRequestDto);
  }
}
