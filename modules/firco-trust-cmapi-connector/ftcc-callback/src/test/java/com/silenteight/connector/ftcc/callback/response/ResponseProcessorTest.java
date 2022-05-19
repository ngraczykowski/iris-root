package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.callback.outgoing.RecommendationsDeliveredPublisher;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.silenteight.connector.ftcc.callback.CallbackFixtures.createRecommendationsOut;
import static com.silenteight.connector.ftcc.common.testing.CommonFixtures.ANALYSIS_NAME;
import static com.silenteight.connector.ftcc.common.testing.CommonFixtures.BATCH_NAME;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ResponseProcessorTest {

  @Mock
  RecommendationSender recommendationSender;
  @Mock
  RecommendationsDeliveredPublisher recommendationsDeliveredPublisher;
  @Mock
  ClientRequestDtoBuilder clientRequestDtoBuilder;
  @Mock
  RecommendationClientApi recommendationClientApi;

  ResponseProcessor underTest;

  @BeforeEach
  void setUp() {
    underTest = new ResponseProcessor(clientRequestDtoBuilder,
        recommendationSender,
        recommendationClientApi,
        recommendationsDeliveredPublisher,
        true);
  }

  @DisplayName("When Recommendations Received, then 'ClientRequestDto' should be created")
  @Test
  void whenRecommendationReceivedThenClientRequestDtoShouldBeCreated() {
    when(recommendationClientApi.recommendation(ANALYSIS_NAME)).thenReturn(
        createRecommendationsOut(1));

    var messageBatchCompleted = MessageBatchCompleted.newBuilder()
        .setBatchId(BATCH_NAME)
        .setAnalysisName(ANALYSIS_NAME)
        .build();

    assertAll(() -> underTest.process(messageBatchCompleted));
    verify(clientRequestDtoBuilder, times(1)).build(eq(BATCH_NAME), eq(ANALYSIS_NAME), any());
  }
}
