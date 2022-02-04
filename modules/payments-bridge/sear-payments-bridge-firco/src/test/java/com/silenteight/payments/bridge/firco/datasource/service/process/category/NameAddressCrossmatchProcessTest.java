package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse;
import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.getCategoryValueExtractModel;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        NameAddressCrossmatchAgentResponse.NO_CROSSMATCH);
    when(createAlertedPartyEntitiesUseCase.create(any())).thenReturn(new HashMap<>());
    nameAddressCrossmatchProcess = new NameAddressCrossmatchProcess(
        nameAddressCrossmatchUseCase,
        createAlertedPartyEntitiesUseCase);
  }

  @Test
  void testExtract() {
    int id = 1;
    var categoryValueExtractModel = getCategoryValueExtractModel(id);
    nameAddressCrossmatchProcess.createCategoryValue(categoryValueExtractModel);
    verify(nameAddressCrossmatchUseCase, times(1)).call(any());
  }
}
