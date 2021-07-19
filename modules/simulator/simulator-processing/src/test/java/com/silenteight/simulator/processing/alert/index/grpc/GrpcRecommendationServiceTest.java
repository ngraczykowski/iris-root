package com.silenteight.simulator.processing.alert.index.grpc;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceBlockingStub;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.simulator.processing.alert.index.grpc.RecommendationFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrpcRecommendationServiceTest {

  @InjectMocks
  private GrpcRecommendationService underTest;

  @Mock
  private RecommendationServiceBlockingStub recommendationStub;

  @Test
  void shouldGetRecommendation() {
    // given
    when(recommendationStub.getRecommendation(makeGetRecommendationRequest(NAME)))
        .thenReturn(RECOMMENDATION);

    // when
    Recommendation recommendation = underTest.getRecommendation(NAME);

    // then
    assertThat(recommendation.getName()).isEqualTo(NAME);
    assertThat(recommendation.getAlert()).isEqualTo(ALERT);
    assertThat(recommendation.getCreateTime()).isEqualTo(CREATE_TIME);
    assertThat(recommendation.getRecommendedAction()).isEqualTo(RECOMMENDED_ACTION);
    assertThat(recommendation.getRecommendationComment()).isEqualTo(COMMENT);
  }

  private static GetRecommendationRequest makeGetRecommendationRequest(String recommendation) {
    return GetRecommendationRequest.newBuilder()
        .setRecommendation(recommendation)
        .build();
  }
}
