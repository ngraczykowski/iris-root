package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;
import com.silenteight.sep.base.testing.spring.ApplicationEventPublisherSpy;

import org.junit.jupiter.api.Test;

import static com.silenteight.adjudication.engine.analysis.analysis.AnalysisFixtures.createAnalysisAlerts;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AddAlertToAnalysisUseCaseTest {

  private final ApplicationEventPublisherSpy eventPublisherSpy = new ApplicationEventPublisherSpy();
  private final AnalysisAlertRepository analysisAlertRepository =
      new InMemoryAnalysisAlertRepository();
  private final AddAlertToAnalysisUseCase addAlertToAnalysisUseCase =
      new AddAlertToAnalysisUseCase(analysisAlertRepository, eventPublisherSpy);

  @Test
  void shouldSaveAnalysisAlerts() {
    var response =
        addAlertToAnalysisUseCase.batchAddAlert("analysis/1", createAnalysisAlerts(10));
    assertThat(response.size()).isEqualTo(10);
    var event = eventPublisherSpy.getNext(AnalysisAlertsAdded.class);
    assertThat(event.getAnalysisAlertsCount()).isEqualTo(10);
    assertThat(eventPublisherSpy.isEmpty()).isTrue();
  }
}
