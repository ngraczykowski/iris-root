package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentResponse;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.getCategoryValueExtractModel;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricalRiskAssessmentFactoryProcessTest {

  private HistoricalRiskAssessmentProcess historicalRiskAssessmentProcess;

  @Mock
  private HistoricalRiskAssessmentUseCase historicalRiskAssessmentUseCase;

  @BeforeEach
  void setup() {
    when(historicalRiskAssessmentUseCase.invoke(any())).thenReturn(
        HistoricalRiskAssessmentAgentResponse.NO);
    historicalRiskAssessmentProcess =
        new HistoricalRiskAssessmentProcess(historicalRiskAssessmentUseCase);
  }

  @Test
  void testExtract() {
    int id = 1;
    var categoryValueExtractModel = getCategoryValueExtractModel(id);
    historicalRiskAssessmentProcess.createCategoryValue(categoryValueExtractModel);
    verify(historicalRiskAssessmentUseCase, times(1)).invoke(any());
  }
}
