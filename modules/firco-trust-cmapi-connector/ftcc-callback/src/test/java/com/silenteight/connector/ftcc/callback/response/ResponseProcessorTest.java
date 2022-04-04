package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.callback.outgoing.RecommendationsDeliveredPublisher;
import com.silenteight.connector.ftcc.common.resource.MessageResource;
import com.silenteight.connector.ftcc.request.details.MessageDetailsQuery;
import com.silenteight.connector.ftcc.request.details.dto.MessageDetailsDto;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.recommendation.api.library.v1.AlertOut;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;
import com.silenteight.recommendation.api.library.v1.RecommendationsOut;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
class ResponseProcessorTest {

  @Mock
  RecommendationSender recommendationSender;
  @Mock
  ResponseCreator responseCreator;
  @Mock
  MessageDetailsQuery messageDetailsQuery;
  @Mock
  RecommendationsDeliveredPublisher recommendationsDeliveredPublisher;

  @BeforeEach
  void setUp() {
  }

  @DisplayName("Receiving 2 solved alerts, should create 'ClientRequestDto' twice")
  @Test
  void receivingTwoSolvedAlerts() {
    var collect =
        IntStream.range(0, 2)
            .mapToObj(alertId -> RecommendationOut
                .builder()
                .recommendationComment("PTP")
                .alert(AlertOut.builder().id("messages/" + randomUUID()).build())
                .build())
            .collect(toList());
    var responseProcessor = new ResponseProcessor(
        responseCreator, recommendationSender, (analysisId) -> RecommendationsOut.builder()
        .recommendations(collect)
        .build(), messageDetailsQuery, recommendationsDeliveredPublisher);

    when(messageDetailsQuery.details(any())).thenReturn(
        collect.stream()
            .map(recommendationOut -> MessageDetailsDto.builder()
                .id(MessageResource.fromResourceName(
                    recommendationOut.getAlert().getId()))
                .build())
            .collect(toList()));
    var messageBatchCompleted =
        MessageBatchCompleted.newBuilder()
            .setBatchId(randomUUID().toString())
            .addAlertIds("messages/1")
            .addAlertIds("messages/2")
            .setAnalysisId("analysis/1")
            .build();

    assertAll(() -> responseProcessor.process(messageBatchCompleted));
    verify(responseCreator, atLeastOnce()).build(anyList());
    verify(responseCreator, atLeast(2)).buildMessageDto(any(), any());
  }
}
