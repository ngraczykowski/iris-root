package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse;
import com.silenteight.payments.bridge.agents.port.SpecificTerms2UseCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.getCategoryValueExtractModel;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecificTerms2ProcessTest {

  private SpecificTerms2Process specificTerms2Process;
  @Mock
  private SpecificTerms2UseCase specificTerms2UseCase;

  @BeforeEach
  void setup() {
    when(specificTerms2UseCase.invoke(any())).thenReturn(new SpecificTermsAgentResponse("NO"));
    specificTerms2Process = new SpecificTerms2Process(specificTerms2UseCase);
  }

  @Test
  void testExtract() {
    int id = 1;
    var categoryValueExtractModel = getCategoryValueExtractModel(id);
    specificTerms2Process.createCategoryValue(
        categoryValueExtractModel.getAlertName(), categoryValueExtractModel.getMatchName(),
        categoryValueExtractModel.getHitData()
            .getHitAndWlPartyData());
    verify(specificTerms2UseCase, times(1)).invoke(any());
  }
}
