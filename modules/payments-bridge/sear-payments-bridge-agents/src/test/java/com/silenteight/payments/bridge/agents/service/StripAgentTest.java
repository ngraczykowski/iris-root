package com.silenteight.payments.bridge.agents.service;

import com.silenteight.payments.bridge.agents.model.StripAgentResponse;
import com.silenteight.payments.bridge.agents.service.StripAgent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StripAgentTest {

  private StripAgent stripAgent;

  @BeforeEach
  void beforeEach() {
    stripAgent = new StripAgent();
  }

  @Test
  void throwNullPointerExceptionForNullRequest() {
    assertThrows(
        NullPointerException.class,
        () -> stripAgent.invoke(null));
  }

  @ParameterizedTest
  @MethodSource("request")
  void testComplexRequest(String hittedEntityId, StripAgentResponse expected) {
    var actual = stripAgent.invoke(hittedEntityId);
    assertEquals(expected, actual);
  }

  private static Stream<Arguments> request() {
    return Stream.of(
        Arguments.of(
            "STRIP123",
            StripAgentResponse.STRIPPED
        ), Arguments.of(
            " STRIP123",
            StripAgentResponse.STRIPPED
        ), Arguments.of(
            " STRIP123 ",
            StripAgentResponse.STRIPPED
        ), Arguments.of(
            "321STRIP",
            StripAgentResponse.STRIPPED
        ), Arguments.of(
            "SSTRIP",
            StripAgentResponse.STRIPPED
        ), Arguments.of(
            "HIUSTRIPbofeb",
            StripAgentResponse.STRIPPED
        ), Arguments.of(
            "STRI",
            StripAgentResponse.NOT_STRIPPED
        ), Arguments.of(
            "STR IP",
            StripAgentResponse.NOT_STRIPPED
        ), Arguments.of(
            "PIRTS",
            StripAgentResponse.NOT_STRIPPED
        ), Arguments.of(
            "",
            StripAgentResponse.NOT_STRIPPED
        ), Arguments.of(
            " ",
            StripAgentResponse.NOT_STRIPPED
        ), Arguments.of(
            "S1T2R3I4P",
            StripAgentResponse.NOT_STRIPPED
        ), Arguments.of(
            "STRRIP",
            StripAgentResponse.NOT_STRIPPED
        ), Arguments.of(
            "#F%$2G13^&^*&FR*",
            StripAgentResponse.NOT_STRIPPED
        )
    );
  }
}
