package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;
import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.adjudication.internal.v1.MatchesSolved;
import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse.Builder;
import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HandleSolvedMatchesUseCaseTest {

  private final List<String> availableSolutions =
      asList("MATCH", "NO_MATCH", "NO_DECISION", "NO_DATA");
  @Mock
  private AnalysisFacade analysisFacade;
  @Mock
  private GovernanceFacade governanceFacade;
  @Mock
  private GenerateMatchCommentsUseCase generateMatchCommentsUseCase;
  private HandleSolvedMatchesUseCase handleSolvedMatchesUseCase;

  @BeforeEach
  void setUp() {
    doAnswer((Answer<BatchSolveAlertsResponse>) invocation -> {
      BatchSolveAlertsRequest request = invocation.getArgument(0);
      Builder builder = BatchSolveAlertsResponse.newBuilder();
      for (int i = 0; i < request.getAlertsCount(); i++) {
        var alert = request.getAlertsList().get(i);
        builder.addSolutions(SolveAlertSolutionResponse.newBuilder()
            .setAlertName(alert.getName())
            .setAlertSolution(availableSolutions.get(i % availableSolutions.size()))
            .build());
      }
      return builder.build();
    })
        .when(governanceFacade).batchSolveAlerts(any(BatchSolveAlertsRequest.class));
    when(generateMatchCommentsUseCase.generateComments(any())).thenReturn("comment");
    when(analysisFacade.getAnalysisStrategy(1)).thenReturn("strategy");

    var dataAccess = new InMemoryMatchRecommendation();
    var createMatchRecommendation =
        new CreateMatchRecommendationUseCase(
            dataAccess, new InMemoryCommentInputDataAccess(), generateMatchCommentsUseCase,
            new ObjectMapper());

    var generateMatchRecommendationUseCase =
        new GenerateMatchRecommendationUseCase(dataAccess, analysisFacade, governanceFacade,
            createMatchRecommendation);
    handleSolvedMatchesUseCase = new HandleSolvedMatchesUseCase(generateMatchRecommendationUseCase);
  }

  @Test
  void test() {
    var matchRecommendationsGenerated = handleSolvedMatchesUseCase.handleMatchesSolved(
        MatchesSolved.newBuilder().setAnalysis("analysis/1").buildPartial());
    assertTrue(matchRecommendationsGenerated.isPresent());
    var matchRecommendation = matchRecommendationsGenerated.get();
    assertThat(matchRecommendation.getRecommendationInfosCount()).isEqualTo(3);
  }
}
