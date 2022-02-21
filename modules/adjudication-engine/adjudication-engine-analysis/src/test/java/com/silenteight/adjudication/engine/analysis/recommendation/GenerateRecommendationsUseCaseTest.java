package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.NotificationFlags;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;
import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputDataAccess;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.GenerateCommentsResponse;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.SaveRecommendationRequest;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;
import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

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
  private AlertSolvingClient client;
  @Mock
  private AnalysisFacade analysisFacade;
  @Mock
  private GenerateCommentsUseCase generateCommentsUseCase;
  @Mock
  private CommentInputDataAccess commentInputDataAccess;

  private int handledRecommendation;

  @BeforeEach
  void setUp() {
    handledRecommendation = 0;
    generateRecommendationsUseCase =
        new GenerateRecommendationsUseCase(
            client, new InMemoryRecommendationDataAccess(), analysisFacade, generateCommentsUseCase,
            commentInputDataAccess);
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
    when(analysisFacade.getAnalysis(analysisName)).thenReturn(Analysis.newBuilder()
        .setNotificationFlags(NotificationFlags.newBuilder()
            .setAttachRecommendation(true).setAttachMetadata(true).build()).build());

    var solution = generateRecommendationsUseCase
        .generateRecommendations(analysisName, this::handleRecommendation)
        .get(0);
    assertThat(solution.getAlert()).isEqualTo("alerts/1");
    assertThat(solution.getRecommendation()).isEqualTo("solved");
    assertThat(handledRecommendation).isEqualTo(5);
  }

  @Test
  void shouldReturnEmptyList() {
    var solution = generateRecommendationsUseCase.generateRecommendations(
        "analysis/5",
        this::handleRecommendation);
    assertThat(solution.size()).isEqualTo(0);
    assertThat(handledRecommendation).isEqualTo(0);
  }

  private List<RecommendationInfo> handleRecommendation(SaveRecommendationRequest request) {
    handledRecommendation++;
    return List.of(RecommendationInfo
        .newBuilder()
        .setRecommendation(request.getAlertSolutions().get(0).getRecommendedAction())
        .setAlert("alerts/" + request.getAlertSolutions().get(0).getAlertId())
        .build());
  }
}
