package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;
import com.silenteight.adjudication.engine.analysis.analysis.domain.AnalysisAttachmentFlags;
import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputDataAccess;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.GenerateCommentsResponse;
import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;
import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateRecommendationsUseCaseTest {

  private GenerateRecommendationsUseCase generateRecommendationsUseCase;
  @Mock
  private GovernanceFacade client;
  @Mock
  private AnalysisFacade analysisFacade;
  @Mock
  private GenerateCommentsUseCase generateCommentsUseCase;
  @Mock
  private CommentInputDataAccess commentInputDataAccess;
  @Mock
  CreateRecommendationsUseCase createRecommendationsUseCase;

  private int handledRecommendation;

  @BeforeEach
  void setUp() {
    handledRecommendation = 0;
    generateRecommendationsUseCase =
        new GenerateRecommendationsUseCase(
            client, new InMemoryRecommendationDataAccess(), analysisFacade, generateCommentsUseCase,
            commentInputDataAccess, createRecommendationsUseCase,
            new SimpleMeterRegistry());
  }

  @Test
  void shouldReturnAlertSolutions() {
    when(client.batchSolveAlerts(any()))
        .thenReturn(BatchSolveAlertsResponse.newBuilder().addAllSolutions(
            List.of(SolveAlertSolutionResponse
                .newBuilder()
                .setAlertName("alerts/1")
                .setAlertSolution("solved")
                .build())).build());

    when(analysisFacade.getAnalysisStrategy(1)).thenReturn("strategies");
    when(generateCommentsUseCase.generateComments(any())).thenReturn(
        new GenerateCommentsResponse(null));
    when(commentInputDataAccess.getCommentInputByAlertId(1)).thenReturn(
        Optional.of(new HashMap<String, Object>()));
    final String analysisName = "analysis/1";
    when(analysisFacade.getAnalysisAttachmentFlags(analysisName)).thenReturn(
        new AnalysisAttachmentFlags(true, true));

    when(createRecommendationsUseCase.createRecommendations(any())).thenReturn(
        List.of(RecommendationInfo
            .newBuilder()
            .setRecommendation("solved")
            .setAlert("alerts/1")
            .build()));

    var solution = generateRecommendationsUseCase
        .generateRecommendations(analysisName)
        .get(0);
    assertThat(solution.getAlert()).isEqualTo("alerts/1");
    assertThat(solution.getRecommendation()).isEqualTo("solved");

    verify(createRecommendationsUseCase, times(5)).createRecommendations(any());
  }

  @Test
  void shouldReturnEmptyList() {
    var solution = generateRecommendationsUseCase.generateRecommendations(
        "analysis/5");
    assertThat(solution.size()).isEqualTo(0);

    verify(createRecommendationsUseCase, times(0)).createRecommendations(any());
  }
}
