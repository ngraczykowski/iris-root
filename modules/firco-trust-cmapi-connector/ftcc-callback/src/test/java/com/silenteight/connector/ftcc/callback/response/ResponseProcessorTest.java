package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.callback.response.domain.MessageEntity;
import com.silenteight.connector.ftcc.callback.response.domain.MessageQuery;
import com.silenteight.connector.ftcc.common.resource.MessageResource;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.recommendation.api.library.v1.AlertOut;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;
import com.silenteight.recommendation.api.library.v1.RecommendationsOut;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
class ResponseProcessorTest {

  @Mock
  RecommendationSender recommendationSender;
  @Mock
  ResponseCreator responseCreator;

  @Mock
  MessageQuery messageRepository;

  @BeforeEach
  void setUp() {
  }

  @DisplayName("Receiving 2 solved alerts, should create 'ClientRequestDto' twice")
  @Test
  void receiving2SolvedAlerts() {

    var collect =
        IntStream.range(0, 2)
            .mapToObj(alertId -> RecommendationOut
                .builder()
                .recommendationComment("PTP")
                .alert(AlertOut.builder().id("messages/" + randomUUID()).build())
                .build())
            .collect(Collectors.toList());
    var responseProcessor = new ResponseProcessor(
        responseCreator, recommendationSender, (analysisId) -> RecommendationsOut.builder()
        .recommendations(collect)
        .build(), messageRepository);

    when(messageRepository.findByBatchId(any())).thenReturn(
        collect.stream()
            .map(recommendationOut -> MessageEntity.builder().id(
                MessageResource.fromResourceName(recommendationOut.getAlert().getId())).build())
            .collect(
                Collectors.toList()));
    var messageBatchCompleted =
        MessageBatchCompleted
            .newBuilder()
            .setBatchId(randomUUID().toString())
            .addAlertIds("messages/1")
            .addAlertIds("messages/2")
            .setAnalysisId("analysis/1")
            .build();

    Assertions.assertAll(() -> responseProcessor.process(messageBatchCompleted));
    verify(responseCreator, atLeast(2)).create(any(), any());
  }
}
