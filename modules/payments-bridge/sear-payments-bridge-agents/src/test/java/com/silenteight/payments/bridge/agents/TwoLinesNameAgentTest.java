package com.silenteight.payments.bridge.agents;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TwoLinesNameAgentTest {

  private TwoLinesNameAgent twoLinesNameAgent;

  @BeforeEach
  void beforeEach() {
    twoLinesNameAgent = new TwoLinesNameAgent();
  }

  @ParameterizedTest
  @CsvSource({
      "'', NO",
      "'        ', NO",
      "' global', YES",
      "'this is global company name', YES",
      "' globalistic name', YES",
      "' any other text', NO",
      "' plc.', YES",
      "' plc,', YES",
      "' abcplcba', NO",
      "' abcplc.ba', NO",
      "' abcde l.l,c', YES",
      "'AV. FRANCISCO DE MIRANDA, CENTRO LIDO, TORRE A, PISO 10, OF. 10-02, URB. EL ROSA', NO",
      "'AV. FRANCIS CO DE MIRANDA, CENTRO LIDO, TORRE A, PISO 10, OF. 10-02, URB. EL ROSA', YES",
      "'AV. FRANCIS DE MIRANDA, CENTRO LIDO, TORRE A, PISO 10, OF. 10-02, URB. EL ROSA', NO"
  })
  void parametrizedTest(String alertedPartAddress, TwoLinesNameAgentResponse expected) {
    TwoLinesNameAgentRequest twoLinesNameAgentRequest = new TwoLinesNameAgentRequest(
        Collections.singletonList(alertedPartAddress));
    TwoLinesNameAgentResponse actual = twoLinesNameAgent.invoke(twoLinesNameAgentRequest);
    assertEquals(expected, actual);
  }
}

