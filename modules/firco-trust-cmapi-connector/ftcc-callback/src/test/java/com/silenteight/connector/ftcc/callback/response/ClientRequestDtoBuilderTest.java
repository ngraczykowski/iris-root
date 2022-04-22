package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.callback.exception.NonRecoverableCallbackException;
import com.silenteight.recommendation.api.library.v1.RecommendationsOut;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.silenteight.connector.ftcc.callback.CallbackFixtures.createRecommendationsOut;
import static com.silenteight.connector.ftcc.common.testing.CommonFixtures.ANALYSIS_NAME;
import static com.silenteight.connector.ftcc.common.testing.CommonFixtures.BATCH_NAME;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ClientRequestDtoBuilderTest {

  @Mock
  private ResponseCreator responseCreator;

  @Mock
  private MessageDetailsService messageDetailsService;

  @InjectMocks
  private ClientRequestDtoBuilder underTest;

  @DisplayName("When CallbackRequestDto can't be build then NonRecoverableException is thrown")
  @Test
  void whenCallbackRequestDtoCantBeBuildThenNonRecoverableExceptionIsThrown() {
    when(responseCreator.build(anyList())).thenThrow(RuntimeException.class);
    RecommendationsOut recommendationsOut = createRecommendationsOut(1);

    assertThrows(
        NonRecoverableCallbackException.class,
        () -> underTest.build(BATCH_NAME, ANALYSIS_NAME, recommendationsOut)).getMessage();
  }

  @DisplayName("Every Recommendation create ReceiveDecisionMessageDto")
  @Test
  void everyRecommendationCreateReceiveDecisionMessageDto() {
    underTest.build(BATCH_NAME, ANALYSIS_NAME, createRecommendationsOut(2));

    verify(responseCreator, times(1)).build(anyList());
    verify(responseCreator, times(2)).buildMessageDto(any(), any());
  }
}
