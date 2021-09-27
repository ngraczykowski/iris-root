package com.silenteight.payments.bridge.ae.recommendation.service;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.payments.bridge.ae.recommendation.port.AlertRecommendationValueReceivedGateway;
import com.silenteight.payments.bridge.ae.recommendation.port.RecommendationClientPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.payments.bridge.ae.recommendation.service.RecommendationFixture.createRecommendation;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiveGeneratedGeneratedRecommendationServiceTest {

  private ReceiveGeneratedGeneratedRecommendationService service;
  @Mock
  private RecommendationClientPort recommendationClientPort;
  @Mock
  private AlertRecommendationValueReceivedGateway gateway;

  @BeforeEach
  void setUp() {
    service = new ReceiveGeneratedGeneratedRecommendationService(recommendationClientPort, gateway);
  }

  @Test
  void shouldSendMessage() {
    when(recommendationClientPort.receiveRecommendation(any())).thenReturn(createRecommendation());

    service.handleGeneratedRecommendationMessage(
        RecommendationsGenerated.newBuilder().addRecommendationInfos(
            RecommendationInfo
                .newBuilder()
                .setAlert("alerts/1")
                .setRecommendation("recommendation")
                .build()).build());

    verify(gateway).send(createRecommendation());
  }
}
