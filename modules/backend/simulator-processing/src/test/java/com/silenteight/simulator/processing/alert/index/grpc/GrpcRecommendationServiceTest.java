package com.silenteight.simulator.processing.alert.index.grpc;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceBlockingStub;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.simulator.processing.alert.index.fixtures.RecommendationFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrpcRecommendationServiceTest {

  @InjectMocks
  private GrpcRecommendationService underTest;

  @Mock
  private RecommendationServiceBlockingStub recommendationStub;

  @Test
  void shouldGetRecommendationWithMetadata() {
    // given
    when(recommendationStub.getRecommendationWithMetadata(
        makeGetRecommendationRequest(RECOMMENDATION_NAME)))
        .thenReturn(RECOMMENDATION_WITH_METADATA);

    // when
    RecommendationWithMetadata recommendationWithMetadata =
        underTest.getRecommendationWithMetadata(RECOMMENDATION_NAME);

    // then
    Recommendation recommendation = recommendationWithMetadata.getRecommendation();
    assertThat(recommendation.getName()).isEqualTo(RECOMMENDATION_NAME);
    assertThat(recommendation.getAlert()).isEqualTo(ALERT_NAME);
    assertThat(recommendation.getCreateTime()).isEqualTo(RECOMMENDATION_CREATE_TIME);
    assertThat(recommendation.getRecommendedAction()).isEqualTo(RECOMMENDED_ACTION);
    assertThat(recommendation.getRecommendationComment()).isEqualTo(RECOMMENDATION_COMMENT);
    RecommendationMetadata metadata = recommendationWithMetadata.getMetadata();
    assertThat(metadata.getName()).isEqualTo(RECOMMENDATION_METADATA_NAME);
    assertThat(metadata.getAlert()).isEqualTo(ALERT_NAME);
  }

  private static GetRecommendationRequest makeGetRecommendationRequest(String recommendation) {
    return GetRecommendationRequest.newBuilder()
        .setRecommendation(recommendation)
        .build();
  }
}
