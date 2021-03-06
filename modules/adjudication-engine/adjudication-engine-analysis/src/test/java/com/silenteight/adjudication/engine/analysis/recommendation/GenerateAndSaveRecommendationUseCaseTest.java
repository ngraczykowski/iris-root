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
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
  private GovernanceFacade client;
  @Mock
  private AnalysisFacade analysisFacade;
  @Mock
  private GenerateCommentsUseCase generateCommentsUseCase;
  @Mock
  private CommentInputDataAccess commentInputDataAccess;
  @Mock
  private RabbitTemplate rabbitTemplate;

  @BeforeEach
  void setUp() {
    generateRecommendationsUseCase = new GenerateRecommendationsUseCase(
        client, new InMemoryRecommendationDataAccess(), analysisFacade, generateCommentsUseCase,
        commentInputDataAccess, createRecommendationsUseCase,
        new SimpleMeterRegistry());
    generateAndSaveRecommendationUseCase = new GenerateAndSaveRecommendationUseCase(
        generateRecommendationsUseCase, rabbitTemplate
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

    when(createRecommendationsUseCase.createRecommendations(any()))
        .thenReturn(List.of(
            RecommendationInfo.newBuilder().build()));

    String analysisName = "analysis/1";
    when(analysisFacade.getAnalysisAttachmentFlags(analysisName)).thenReturn(
        new AnalysisAttachmentFlags(true, true));
    when(generateCommentsUseCase.generateComments(any())).thenReturn(
        new GenerateCommentsResponse(null));
    when(commentInputDataAccess.getCommentInputByAlertId(1)).thenReturn(
        Optional.of(new HashMap<>()));

    var generated =
        generateAndSaveRecommendationUseCase.generateAndSaveRecommendations(analysisName);
    assertThat(generated)
        .isNotEmpty()
        .hasValueSatisfying(r -> assertThat(r.getRecommendationInfosCount()).isEqualTo(5));
  }
}
