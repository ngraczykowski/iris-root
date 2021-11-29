package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse.Result;
import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessHelper.createHitData;
import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessHelper.getMatchId;
import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessHelper.getMatchValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NameAddressCrossmatchProcessTest {

  private NameAddressCrossmatchProcess nameAddressCrossmatchProcess;
  @Mock
  private NameAddressCrossmatchUseCase nameAddressCrossmatchUseCase;
  @Mock
  private CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase;

  @BeforeEach
  void setup() {
    when(nameAddressCrossmatchUseCase.call(any())).thenReturn(
        NameAddressCrossmatchAgentResponse.of(Result.NO_CROSSMATCH, new HashMap<>()));
    when(createAlertedPartyEntitiesUseCase.create(any())).thenReturn(new HashMap<>());
    nameAddressCrossmatchProcess = new NameAddressCrossmatchProcess(
        nameAddressCrossmatchUseCase,
        createAlertedPartyEntitiesUseCase);
  }

  @Test
  void testExtract() {
    int id = 1;
    nameAddressCrossmatchProcess.extract(
        createHitData(getMatchId(id)), getMatchValue(id));
    verify(nameAddressCrossmatchUseCase, times(1)).call(any());
  }
}
