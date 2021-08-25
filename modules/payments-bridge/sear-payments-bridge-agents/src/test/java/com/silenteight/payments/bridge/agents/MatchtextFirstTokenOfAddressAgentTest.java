package com.silenteight.payments.bridge.agents;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.payments.bridge.agents.MatchtextFirstTokenOfAddressAgentResponse.NO;
import static com.silenteight.payments.bridge.agents.MatchtextFirstTokenOfAddressAgentResponse.YES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatchtextFirstTokenOfAddressAgentTest {

  private MatchtextFirstTokenOfAddressAgent matchtextFirstTokenOfAddressAgent;

  @BeforeEach
  void beforeEach() {
    matchtextFirstTokenOfAddressAgent = new MatchtextFirstTokenOfAddressAgent();
  }

  @Test
  void throwNullPointerExceptionForNullArguments() {
    assertThrows(NullPointerException.class, () -> matchtextFirstTokenOfAddressAgent.invoke(null));
  }

  @ParameterizedTest
  @MethodSource("generateAgentInputData")
  void parametrizedTest(
      List<String> matchingTexts, List<String> addresses,
      MatchtextFirstTokenOfAddressAgentResponse expected) {
    MatchtextFirstTokenOfAddressAgentRequest matchtextFirstTokenOfAddressAgentRequest =
        MatchtextFirstTokenOfAddressAgentRequest.builder()
            .matchingTexts(matchingTexts)
            .addresses(addresses)
            .build();
    MatchtextFirstTokenOfAddressAgentResponse matchtextFirstTokenOfAddressAgentResponse =
        matchtextFirstTokenOfAddressAgent.invoke(matchtextFirstTokenOfAddressAgentRequest);
    assertEquals(expected, matchtextFirstTokenOfAddressAgentResponse);
  }

  static Stream<Arguments> generateAgentInputData() {
    return Stream.of(
        Arguments.of(
            List.of("SABA"), List.of(
                "SABA INTERNATIONAL COMPANY 4TH FLOOR SABA ISLAMIC BUILDING OPP NOUGAPRIX "
                    + "SUPERMARKET REPUBLIC OF DJIBOUTI / DJIBOUTI"),
            YES),
        Arguments.of(
            List.of("ICIC BANK"),
            List.of("ICIC BANK LTD IFSCICIC0001874 CITY NASIK, BRANCH INDIRANAGAR INDIA"), YES),
        Arguments.of(
            Collections.emptyList(), List.of(
                "HO RIYADH KINGDOM OF SAUDI ARABIA P.O. BOX 56006 RIYADH",
                "KINGDOM OF SAUDI ARABIA P.O. BOX RIYADH"), NO),
        Arguments.of(
            List.of("ICIC BANK"),
            List.of("     HO RIYADH KINGDOM OF SAUDI ARABIA P.O. BOX 56006 RIYADH     "), NO),
        Arguments.of(
            List.of("ICIC BANK"),
            List.of("     ICIC BANK LTD IFSCICIC0001874 CITY NASIK, BRANCH INDIRANAGAR INDIA"),
            YES),
        Arguments.of(List.of(""), List.of(""), YES),
        Arguments.of(
            List.of("Icic BANK"),
            List.of("ICIC bank LTD IFSCICIC0001874 CITY NASIK, BRANCH INDIRANAGAR INDIA"), YES),
        Arguments.of(
            List.of("           Icic BANK         "),
            List.of("  ICIC bank LTD IFSCICIC0001874 CITY NASIK, BRANCH INDIRANAGAR INDIA   "), YES)
    );
  }
}
