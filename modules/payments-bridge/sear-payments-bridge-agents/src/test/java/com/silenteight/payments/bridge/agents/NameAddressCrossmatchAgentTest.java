package com.silenteight.payments.bridge.agents;

import com.silenteight.payments.bridge.agents.NameAddressCrossmatchAgentResponse.Result;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.silenteight.payments.bridge.agents.NameAddressCrossmatchAgentResponse.Result.CROSSMATCH;
import static com.silenteight.payments.bridge.agents.NameAddressCrossmatchAgentResponse.Result.NO_CROSSMATCH;
import static com.silenteight.payments.bridge.agents.NameAddressCrossmatchAgentResponse.Result.NO_DECISION;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NameAddressCrossmatchAgentTest {

  NameAddressCrossmatchAgent agent;

  @BeforeEach
  void beforeEach() {
    agent = new NameAddressCrossmatchAgent();
  }

  @Test
  void assertThatCrossmatchAgentReturnNoDecisionWhenAlertedNameaddressSegmentKeyPresent() {
    NameAddressCrossmatchAgentResponse nameAddressCrossmatchAgentResponse = agent.call(
        new NameAddressCrossmatchAgentRequest(
            Map.of("NAMEADDRESS FIRST SEGMENT", "IND LLC EMIRATES NATIONAL FACTORY"),
            "EMIRATES NATIONAL FACTORY FOR PLASTIC INDUSTRIES LLC", "UAE", "COMPANY"));
    assertThat(nameAddressCrossmatchAgentResponse)
        .isEqualTo(NameAddressCrossmatchAgentResponse.of(
            NO_DECISION,
            Map.of("NAMEADDRESS FIRST SEGMENT", "IND LLC EMIRATES NATIONAL FACTORY")));
  }

  @ParameterizedTest
  @MethodSource("request")
  void parametrizedTest(
      List<String> matchingTexts, Map<String, String> apEntTypeData,
      Map<String, String> matchedEntity, WlData wlData, Result output) {
    NameAddressCrossmatchAgentResponse nameAddressCrossmatchAgentResponse = agent.call(
        new NameAddressCrossmatchAgentRequest(apEntTypeData, wlData.name, wlData.country,
            wlData.type));
    assertEquals(
        nameAddressCrossmatchAgentResponse,
        NameAddressCrossmatchAgentResponse.of(output, matchedEntity));
  }

  private static Stream<Arguments> request() {
    return Stream.of(
        Arguments.of(
            List.of("John Smith"),
            TestData.AP_DATA_1,
            TestData.AP_DATA_1,
            TestData.WL_INDIVIDUAL_JOHN_SMITH,
            NO_CROSSMATCH),
        Arguments.of(
            List.of("ANDREWS"),
            TestData.AP_DATA_2,
            TestData.AP_DATA_2,
            TestData.WL_INDIVIDUAL_JOHN_SMITH,
            CROSSMATCH),
        Arguments.of(
            List.of("ANDREWS"),
            null,
            Map.of("", ""),
            TestData.WL_INDIVIDUAL_OSAMA_BIN_LADEN,
            NO_DECISION),
        Arguments.of(
            List.of("BOG", "COFFEE"),
            TestData.AP_DATA_3,
            TestData.AP_DATA_3,
            TestData.WL_INDIVIDUAL_OSAMA_BIN_LADEN,
            NO_CROSSMATCH),
        Arguments.of(
            List.of("ANDREWS"),
            TestData.AP_DATA_4,
            TestData.AP_DATA_4,
            TestData.WL_COMPANY_OSAMA_BIN_LADEN,
            NO_CROSSMATCH),
        Arguments.of(
            List.of("SABA"),
            TestData.AP_DATA_5,
            TestData.AP_DATA_5_MATCH,
            TestData.WL_COMPANY_SABA,
            NO_DECISION),
        Arguments.of(
            List.of("ANDREWS"),
            TestData.AP_DATA_6,
            TestData.AP_DATA_6,
            TestData.WL_OTHER_WILDCARD,
            CROSSMATCH),
        Arguments.of(
            List.of("AM"),
            TestData.AP_DATA_7,
            TestData.AP_DATA_7,
            TestData.WL_OTHER_WILDCARD,
            NO_CROSSMATCH),
        Arguments.of(
            List.of("BOG", "COFFEE"),
            TestData.AP_DATA_3,
            TestData.AP_DATA_3,
            TestData.WL_OTHER_WILDCARD,
            CROSSMATCH),
        Arguments.of(
            List.of("BOGAWANTALAWA"),
            TestData.AP_DATA_9,
            TestData.AP_DATA_9_MATCH,
            TestData.WL_OTHER_OSAMA_BIN_LADEN,
            NO_DECISION),
        Arguments.of(
            List.of("ICIC"),
            TestData.AP_DATA_10,
            TestData.AP_DATA_10,
            TestData.createCompWl("ICIC", "India"),
            NO_CROSSMATCH
        )
    );
  }


  static class TestData {

    static final WlData WL_INDIVIDUAL_JOHN_SMITH = createIndWl("John Smith", "Poland");
    static final WlData WL_INDIVIDUAL_OSAMA_BIN_LADEN = createIndWl("OSAMA BIN LADEN", "Iraq");
    static final WlData WL_OTHER_WILDCARD = createOtherWl("*", "India");
    static final WlData WL_OTHER_OSAMA_BIN_LADEN = createOtherWl("OSAMA BIN LADEN", "Iraq");
    static final WlData WL_COMPANY_SABA = createCompWl("SABA PVT LTD", "Iraq");
    static final WlData WL_COMPANY_OSAMA_BIN_LADEN =
        createCompWl("OSAMA BIN LADEN PVT LTD", "Iraq");

    static final Map<String, String> AP_DATA_1 = Map.of("NAME", "John Smith");
    static final Map<String, String> AP_DATA_2 =
        Map.of("ADDRESS", "2ND FLOOR ANDREWS BUILDING NO 130 NAWALA ROAD COLOMBO 5, "
            + "SRI LANKA");
    static final Map<String, String> AP_DATA_3 =
        Map.of("COMPANY NAME", "BOGAWANTALAWA COFFEE PVT LTD");
    static final Map<String, String> AP_DATA_4 =
        Map.of("COMPANY NAME", "BOGAWANTALAWA COFFEE PVT LTD", "NAME",
            "BOGAWANTALAWA COFFEE PVT LTD");
    static final Map<String, String> AP_DATA_5 = Map.of("ADDRESS",
        "COMPANY\n4TH FLOOR SABA ISLAMIC BUILDING\nOPP NOUGAPRIX SUPERMARKET\n"
            + "REPUBLIC OF DJIBOUTI / DJIBOUTI",
        "NAMEADDRESS FIRST SEGMENT", "SABA INTERNATIONAL ");
    static final Map<String, String> AP_DATA_5_MATCH =
        Map.of("NAMEADDRESS FIRST SEGMENT", "SABA INTERNATIONAL ");
    static final Map<String, String> AP_DATA_6 = Map.of("NAME", "BOGAWANTALAWA COFFEE "
        + "PVT LTD");
    static final Map<String, String> AP_DATA_7 =
        Map.of("COUNTRY TOWN", "AM", "NAME", "BOGAWANTALAWA COFFEE PVT LTD");
    static final Map<String, String> AP_DATA_9 = Map.of("NAME", "BOGAWANTALAWA COFFEE "
        + "PVT LTD");
    static final Map<String, String> AP_DATA_9_MATCH = Map.of("*", "");

    static final Map<String, String> AP_DATA_10 =
        Map.of("ADDRESS", "IFSCICIC0001874 CITY NASIK, BRANCH INDIRANAGAR INDIA",
            "NAME", "ICIC BANK LTD");

    private static WlData createIndWl(String wlName, String wlCountry) {
      return new WlData("INDIVIDUAL", wlName, wlCountry);
    }

    private static WlData createCompWl(String wlName, String wlCountry) {
      return new WlData("COMPANY", wlName, wlCountry);
    }

    private static WlData createOtherWl(String wlName, String wlCountry) {
      return new WlData("OTHER", wlName, wlCountry);
    }
  }

  private static class WlData {

    String type;
    String name;
    String country;

    WlData(String type, String name, String country) {
      this.type = type;
      this.name = name;
      this.country = country;
    }

    String getType() {
      return type;
    }

    String getName() {
      return name;
    }

    String getCountry() {
      return country;
    }
  }
}
