package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse;
import com.silenteight.payments.bridge.agents.port.SpecificTerms2UseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.getDatasourceUnstructuredModel;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecificTermsFactory2FactoryProcessTest {

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
    specificTerms2Process.createCategoryValue(getDatasourceUnstructuredModel(id));
    verify(specificTerms2UseCase, times(1)).invoke(any());
  }
}
