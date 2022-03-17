package com.silenteight.connector.ftcc.callback.response;

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

import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
class ResponseProcessorTest {

  @Mock
  RecommendationSender recommendationSender;
  @Mock
  ResponseCreator responseCreator;

  @BeforeEach
  void setUp() {
  }

  @DisplayName("Receiving 2 solved alerts, should create 'ClientRequestDto' twice")
  @Test
  void receiving2SolvedAlerts() {

    var responseProcessor = new ResponseProcessor(
        responseCreator, recommendationSender, alertIds -> {
      var collect =
          alertIds
              .stream()
              .map(alertId -> RecommendationOut
                  .builder()
                  .recommendationComment("PTP")
                  .alert(AlertOut.builder().id(alertId).build())
                  .build())
              .collect(Collectors.toList());
      return RecommendationsOut.builder()
          .recommendations(collect)
          .build();
    });
    var uuid = UUID.randomUUID();
    var messageBatchCompleted =
        MessageBatchCompleted
            .newBuilder()
            .setBatchId(uuid.toString())
            .addAlertIds("alerts/1")
            .addAlertIds("alerts/2")
            .build();

    Assertions.assertAll(() -> responseProcessor.process(messageBatchCompleted));
    verify(responseCreator, atLeast(2)).create(any(), any());
  }
}
