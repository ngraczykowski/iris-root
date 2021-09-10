package com.silenteight.payments.bridge.agents.service;

import com.silenteight.payments.bridge.agents.model.SanctionedNationalityAgentResponse;
import com.silenteight.payments.bridge.agents.service.SanctionedNationalityAgent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SanctionedNationalityAgentTest {

  private SanctionedNationalityAgent agent;

  @BeforeEach
  void beforeEach() {
    agent = new SanctionedNationalityAgent();
  }

  @Test
  void throwNullPointerExceptionWhenMessageDataIsNull() {
    assertThrows(NullPointerException.class, () -> agent.invoke(null, asList("IRAN","CUBA")));
  }

  @ParameterizedTest
  @CsvSource({
      "'',NO",
      "'something IRANsomething',YES",
      "'hahaha CUBAhaha',YES",
      "'hahaha CUBAhaha IRAN a aaaa ',YES",
      "'asdf askj l k',NO",
  })
  void parameterizedTest(String messageData, SanctionedNationalityAgentResponse expected) {
    var actual = agent.invoke(messageData, asList("IRAN","CUBA"));
    assertEquals(expected, actual);
  }

}
