package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse.Result;
import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.categories.port.outgoing.CreateCategoryValuesClient;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.createAeAlert;
import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.createAlertEtlResponse;
import static java.util.List.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryEtlProcessTest {

  private CategoryEtlProcess categoryEtlProcess;
  @Mock
  private CreateCategoryValuesClient createCategoryValuesClient;
  @Mock
  private NameAddressCrossmatchUseCase nameAddressCrossmatchUseCase;
  @Mock
  private CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase;

  @BeforeEach
  void setup() {
    when(nameAddressCrossmatchUseCase.call(any())).thenReturn(
        NameAddressCrossmatchAgentResponse.of(Result.NO_CROSSMATCH, new HashMap<>()));
    when(createAlertedPartyEntitiesUseCase.create(any())).thenReturn(new HashMap<>());
    categoryEtlProcess = new CategoryEtlProcess(
        of(new NameAddressCrossmatchProcess(
            nameAddressCrossmatchUseCase, createAlertedPartyEntitiesUseCase)),
        createCategoryValuesClient);
  }

  @Test
  void testExtractAndLoad() {
    int numberOfMatches = 5;
    categoryEtlProcess.extractAndLoad(
        createAeAlert(numberOfMatches), createAlertEtlResponse(numberOfMatches));
    verify(createCategoryValuesClient, times(numberOfMatches)).createCategoriesValues(any());
  }
}
