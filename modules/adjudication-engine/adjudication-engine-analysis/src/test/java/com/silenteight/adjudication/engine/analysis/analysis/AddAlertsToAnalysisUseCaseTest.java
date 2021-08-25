package com.silenteight.adjudication.engine.analysis.analysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static com.silenteight.adjudication.engine.analysis.analysis.AnalysisFixtures.createAnalysisAlerts;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class AddAlertsToAnalysisUseCaseTest {

  private AddAlertsToAnalysisUseCase addAlertsToAnalysisUseCase;
  private AnalysisAlertRepository analysisAlertRepository;
  @Mock
  private ApplicationEventPublisher applicationEventPublisher;

  @BeforeEach
  void setUp() {
    analysisAlertRepository = new InMemoryAnalysisAlertRepository();
    addAlertsToAnalysisUseCase =
        new AddAlertsToAnalysisUseCase(
            analysisAlertRepository,
            new PublishAnalysisAlertUseCase(applicationEventPublisher));
  }

  @Test
  void shouldSaveAnalysisAlerts() {
    var response =
        addAlertsToAnalysisUseCase.addAlerts("analysis/1", createAnalysisAlerts(10));
    assertThat(response.size()).isEqualTo(10);
  }
}
