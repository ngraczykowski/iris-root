package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse;
import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;

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
class SpecificTermsProcessTest {

  private SpecificTermsProcess specificTermsProcess;
  @Mock
  private SpecificTermsUseCase specificTermsUseCase;

  @BeforeEach
  void setup() {
    when(specificTermsUseCase.invoke(any())).thenReturn(SpecificTermsAgentResponse.NO);
    specificTermsProcess = new SpecificTermsProcess(specificTermsUseCase);
  }

  @Test
  void testExtract() {
    int id = 1;
    specificTermsProcess.extract(createHitData(getMatchId(id)), getMatchValue(id));
    verify(specificTermsUseCase, times(1)).invoke(any());
  }
}
