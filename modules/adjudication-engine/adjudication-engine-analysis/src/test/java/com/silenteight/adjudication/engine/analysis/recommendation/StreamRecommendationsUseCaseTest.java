package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v1.StreamRecommendationsRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.GenerateCommentsResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StreamRecommendationsUseCaseTest {

  private StreamRecommendationsUseCase streamRecommendationsUseCase;
  @Mock
  private GenerateCommentsUseCase generateCommentsUseCase;
  private List<Recommendation> recommendations;
  private Consumer<Recommendation> consume;

  @BeforeEach
  void setUp() {
    recommendations = new ArrayList<>();
    consume = r -> recommendations.add(r);
    streamRecommendationsUseCase = new StreamRecommendationsUseCase(
        generateCommentsUseCase,
        new InMemoryRecommendationDataAccess());
  }

  @Test
  void shouldStreamRecommendationsFromAnalysis() {
    var request = StreamRecommendationsRequest.newBuilder().setAnalysis("analysis/1").build();

    when(generateCommentsUseCase.generateComments(any()))
        .thenReturn(new GenerateCommentsResponse("comment"));

    streamRecommendationsUseCase.streamRecommendations(request, consume);
    assertThat(recommendations.size()).isEqualTo(2);
  }

  @Test
  void shouldStreamRecommendationsFromDataset() {
    var request = StreamRecommendationsRequest
        .newBuilder()
        .setAnalysis("analysis/1")
        .setDataset("analysis/1/datasets/1")
        .build();

    when(generateCommentsUseCase.generateComments(any()))
        .thenReturn(new GenerateCommentsResponse("comment"));

    streamRecommendationsUseCase.streamRecommendations(request, consume);
    assertThat(recommendations.size()).isEqualTo(1);
  }
}
