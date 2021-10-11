package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentResponse;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessHelper.createHitData;
import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessHelper.getMatchId;
import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessHelper.getMatchValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalRiskAssasmentProcessTest {

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
    HitData hitData = createHitData(getMatchId(id));
    historicalRiskAssessmentProcess.extract(hitData, getMatchValue(id));
    verify(historicalRiskAssessmentUseCase, times(1)).invoke(any());
  }
}
