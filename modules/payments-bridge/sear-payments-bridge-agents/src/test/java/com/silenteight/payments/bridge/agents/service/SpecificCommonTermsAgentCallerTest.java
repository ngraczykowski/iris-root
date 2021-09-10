package com.silenteight.payments.bridge.agents.service;

import com.silenteight.payments.bridge.agents.model.SpecificCommonTermsAgentResponse;
import com.silenteight.payments.bridge.agents.service.SpecificCommonTermsAgent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpecificCommonTermsAgentCallerTest {

  private SpecificCommonTermsAgent specificCommonTermsAgent;

  @BeforeEach
  void beforeEach() {
    specificCommonTermsAgent = new SpecificCommonTermsAgent();
  }

  @Test
  void throwNullPointerExceptionForNullArgument() {
    assertThrows(NullPointerException.class, () -> specificCommonTermsAgent
        .invoke(null, null));
  }

  @ParameterizedTest(name = "{index} {0}")
  @CsvSource({
      "'GERALD YAP\nCO AON INSURANCE LTD BLK 1234\nSINGAPORE',false, YES",
      "'/123456\nGERALD YAP\nCO AON INSURANCE LTD BLK 1234\nSINGAPORE', true, YES",
      "'GERALD YAP CO\nBLK 1234\nSINGAPORE',false, NO",
      "'GERALD YAP\nCO LTD BLK 1234\nSINGAPORE',false, NO",
      "'GERALD YAP\nCO L.L.C. BLK 1234\nSINGAPORE',false, NO",
      "'GERALD YAP\nCO L.L.C. BLK 1234\nSINGAPORE',false, NO",
      "'GERALD YAP CO LTD\nBLK 1234\nSINGAPORE',false, NO",
      "'/123456\nGERALD YAP CO LTD\nBLK 1234\nSINGAPORE',true, NO",
      "'GERALD YAP CO',false, NO",
      "'GERALD YAP CO AON',false, NO",
      "'GERALD YAP\nCO L.L.C. BLK 1234\nSINGAPORE\nGERALD YAP\nSINGAPORE',false, NO",
      "'/123456\nGERALD YAP\nCO AON INSURANCE LTD BLK 1234\nSINGAPORE\nnext line\nanother line', "
          + "true, YES",
      "'GERALD YAP\nC O AON INSURANCE LTD BLK 1234\nSINGAPORE',false, NO",
      "'/123456\nGERALD YAP\nC O AON INSURANCE LTD BLK 1234\nSINGAPORE', true, NO",
      "'GERALD YAP C\\O\nBLK 1234\nSINGAPORE',false, NO",
      "'GERALD YAP\nC/O LTD BLK 1234\nSINGAPORE',false, NO",
      "'GERALD YAP\nCOAON INSURANCE LTD BLK 1234\nSINGAPORE',false, NO",
      "'/123456\nGERALD YAP\nCOxAON INSURANCE LTD BLK 1234\nSINGAPORE', true, NO",
      "'GERALD YAP\nCOvLTD BLK 1234\nSINGAPORE',false, NO",
      "'GERALD YAP\nCObL.L.C. BLK 1234\nSINGAPORE',false, NO",
      "'GERALD YAP COX',false, NO",
      "'GERALD YAP COmAON',false, NO",
      "'/123456\nGERALD YAP COxLTD\nBLK 1234\nSINGAPORE',true, NO",
      "'/123456\nGERALD YAP COZAON\nBLK 1234\nSINGAPORE',true, NO",
  })
  void parametrizedTest(
      String fieldValue, boolean accountNumberFlagInMatchingField,
      SpecificCommonTermsAgentResponse expected) {
    SpecificCommonTermsAgentResponse actual =
        specificCommonTermsAgent.invoke(fieldValue, accountNumberFlagInMatchingField);
    assertEquals(expected, actual);
  }
}
