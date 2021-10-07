package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.payments.bridge.agents.model.TwoLinesNameAgentResponse;
import com.silenteight.payments.bridge.agents.port.TwoLinesNameUseCase;

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
class TwoLinesNameProcessTest {

  private TwoLinesNameProcess twoLinesNameProcess;
  @Mock
  private TwoLinesNameUseCase twoLinesNameUseCase;

  @BeforeEach
  void setup() {
    when(twoLinesNameUseCase.invoke(any())).thenReturn(TwoLinesNameAgentResponse.NO);
    twoLinesNameProcess = new TwoLinesNameProcess(twoLinesNameUseCase);
  }

  @Test
  void testExtract() {
    int id = 1;
    twoLinesNameProcess.extract(createHitData(getMatchId(id)), getMatchValue(id));
    verify(twoLinesNameUseCase, times(1)).invoke(any());
  }
}
