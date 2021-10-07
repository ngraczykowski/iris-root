package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.payments.bridge.agents.model.OneLinerAgentResponse;
import com.silenteight.payments.bridge.agents.port.OneLinerUseCase;

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
class OneLinerProcessTest {

  private OneLinerProcess oneLinerProcess;
  @Mock
  private OneLinerUseCase oneLinerUseCase;

  @BeforeEach
  void setup() {
    when(oneLinerUseCase.invoke(any())).thenReturn(OneLinerAgentResponse.NO);
    oneLinerProcess = new OneLinerProcess(oneLinerUseCase);
  }

  @Test
  void testExtract() {
    int id = 1;
    oneLinerProcess.extract(createHitData(getMatchId(id)), getMatchValue(id));
    verify(oneLinerUseCase, times(1)).invoke(any());
  }
}
