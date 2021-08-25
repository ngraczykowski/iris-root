package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.analysis.pendingrecommendation.PendingRecommendationFacade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.adjudication.engine.analysis.analysis.AnalysisFixtures.createAnalysisAlerts;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class AddAlertsToAnalysisUseCaseTest {

  private AddAlertsToAnalysisUseCase addAlertsToAnalysisUseCase;
  private AnalysisAlertRepository analysisAlertRepository;
  @Mock
  private PendingRecommendationFacade pendingRecommendationFacade;

  @BeforeEach
  void setUp() {
    analysisAlertRepository = new InMemoryAnalysisAlertRepository();
    addAlertsToAnalysisUseCase =
        new AddAlertsToAnalysisUseCase(
            analysisAlertRepository, pendingRecommendationFacade);
  }

  @Test
  void shouldSaveAnalysisAlerts() {
    var response =
        addAlertsToAnalysisUseCase.addAlerts("analysis/1", createAnalysisAlerts(10));
    assertThat(response.size()).isEqualTo(10);
  }
}
