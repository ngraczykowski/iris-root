package com.silenteight.payments.bridge.agents.service;

import com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpecificTerms2AgentCallerTest {

  private SpecificTerms2Agent specificTerms2Agent;

  @BeforeEach
  void beforeEach() {
    specificTerms2Agent =
        new SpecificTerms2Configuration(
            new SpecificTerms2AwsFileProvider(),
            new SpecificTerms2Properties()).specificTerms2DefaultAgent();
  }

  @Test
  void throwNullPointerExceptionForNullArgument() {
    assertThrows(NullPointerException.class, () -> specificTerms2Agent.invoke(null));
  }

  @ParameterizedTest
  @CsvFileSource(
      resources = "/SpecificTerms2AgentCallerTestCases.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void parametrizedTest(String fieldValue, SpecificTermsAgentResponse expected) {
    var actual = specificTerms2Agent.invoke(
        SpecificTermsRequest
            .builder()
            .allMatchFieldsValue(fieldValue.replace("\\n", "\n"))
            .build());
    assertEquals(expected, actual);
  }
}
