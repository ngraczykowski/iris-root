package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.api.v1.Recommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class StreamRecommendationsUseCaseTest {

  private StreamRecommendationsUseCase streamRecommendationsUseCase;
  private List<Recommendation> recommendations;
  private Consumer<Recommendation> consume;

  @BeforeEach
  void setUp() {
    recommendations = new ArrayList<>();
    consume = r -> recommendations.add(r);
    streamRecommendationsUseCase = new StreamRecommendationsUseCase(
        new InMemoryRecommendationDataAccess());
  }

  @Test
  void shouldStreamRecommendationsFromAnalysis() {
    streamRecommendationsUseCase.streamRecommendations("analysis/1", consume);
    assertThat(recommendations.size()).isEqualTo(2);
  }

  @Test
  void shouldStreamRecommendationsFromDataset() {
    streamRecommendationsUseCase.streamRecommendations("analysis/1/datasets/1", consume);
    assertThat(recommendations.size()).isEqualTo(1);
  }
}
