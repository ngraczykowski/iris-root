package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertSolution;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;
import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GenerateAndSaveRecommendationUseCaseTest {

  private GenerateAndSaveRecommendationUseCase generateAndSaveRecommendationUseCase;
  private GenerateRecommendationsUseCase generateRecommendationsUseCase;
  @Mock
  private CreateRecommendationsUseCase createRecommendationsUseCase;
  @Mock
  private AlertSolvingClient client;
  @Mock
  private AnalysisFacade analysisFacade;

  @BeforeEach
  void setUp() {
    generateRecommendationsUseCase = new GenerateRecommendationsUseCase(
        client, new InMemoryRecommendationDataAccess(), analysisFacade);
    generateAndSaveRecommendationUseCase = new GenerateAndSaveRecommendationUseCase(
        generateRecommendationsUseCase,
        createRecommendationsUseCase
    );
  }

  @Test
  void shouldGenerateRecommendations() {
    when(client.batchSolveAlerts(any()))
        .thenReturn(BatchSolveAlertsResponse.newBuilder().addAllSolutions(
            List.of(SolveAlertSolutionResponse
                .newBuilder()
                .setAlertName("alerts/1")
                .setAlertSolution("solved")
                .build())).build());

    when(analysisFacade.getAnalysisStrategy(1)).thenReturn("strategies");

    when(createRecommendationsUseCase.createRecommendations(
        1L,
        List.of(
            AlertSolution.builder()
                .alertId(1)
                .recommendedAction("solved")
                .matchIds(new long[] { 11 })
                .matchContexts(new ObjectNode[] {})
                .build()))).thenReturn(List.of(
        RecommendationInfo.newBuilder().build()));

    var generated =
        generateAndSaveRecommendationUseCase.generateAndSaveRecommendations("analysis/1");
    assertThat(generated)
        .isNotEmpty()
        .hasValueSatisfying(r -> assertThat(r.getRecommendationInfosCount()).isEqualTo(5));
  }
}
