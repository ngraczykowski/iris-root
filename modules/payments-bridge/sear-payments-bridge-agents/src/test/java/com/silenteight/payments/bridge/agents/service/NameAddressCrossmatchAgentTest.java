package com.silenteight.payments.bridge.agents.service;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse.Result;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.silenteight.payments.bridge.agents.model.AlertedPartyKey.*;
import static com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse.Result.CROSSMATCH;
import static com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse.Result.NO_CROSSMATCH;
import static com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse.Result.NO_DECISION;
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
        NameAddressCrossmatchAgentRequest
            .builder()
            .alertPartyEntities(
                Map.of(ALERTED_NAMEADDRESS_SEGMENT_KEY, "IND LLC EMIRATES NATIONAL FACTORY"))
            .watchlistName("EMIRATES NATIONAL FACTORY FOR PLASTIC INDUSTRIES LLC")
            .watchlistCountry("UAE")
            .watchlistType("COMPANY")
            .build());
    assertThat(nameAddressCrossmatchAgentResponse)
        .isEqualTo(NameAddressCrossmatchAgentResponse.of(
            NO_DECISION,
            Map.of(ALERTED_NAMEADDRESS_SEGMENT_KEY, "IND LLC EMIRATES NATIONAL FACTORY")));
  }

  @ParameterizedTest
  @MethodSource("request")
  void parametrizedTest(
      List<String> matchingTexts, Map<AlertedPartyKey, String> apEntTypeData,
      Map<AlertedPartyKey, String> matchedEntity, WlData wlData, Result output) {
    NameAddressCrossmatchAgentResponse nameAddressCrossmatchAgentResponse = agent.call(
        NameAddressCrossmatchAgentRequest
            .builder()
            .alertPartyEntities(apEntTypeData)
            .watchlistName(wlData.getName())
            .watchlistCountry(wlData.getCountry())
            .watchlistType(wlData.getType())
            .build()
    );
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

    static final Map<AlertedPartyKey, String> AP_DATA_1 = Map.of(ALERTED_NAME_KEY, "John Smith");
    static final Map<AlertedPartyKey, String> AP_DATA_2 =
        Map.of(ALERTED_ADDRESS_KEY, "2ND FLOOR ANDREWS BUILDING NO 130 NAWALA ROAD COLOMBO 5, "
            + "SRI LANKA");
    static final Map<AlertedPartyKey, String> AP_DATA_3 =
        Map.of(ALERTED_COMPANY_NAME_KEY, "BOGAWANTALAWA COFFEE PVT LTD");
    static final Map<AlertedPartyKey, String> AP_DATA_4 =
        Map.of(ALERTED_COMPANY_NAME_KEY, "BOGAWANTALAWA COFFEE PVT LTD", ALERTED_NAME_KEY,
            "BOGAWANTALAWA COFFEE PVT LTD");
    static final Map<AlertedPartyKey, String> AP_DATA_5 = Map.of(ALERTED_ADDRESS_KEY,
        "COMPANY\n4TH FLOOR SABA ISLAMIC BUILDING\nOPP NOUGAPRIX SUPERMARKET\n"
            + "REPUBLIC OF DJIBOUTI / DJIBOUTI",
        ALERTED_NAMEADDRESS_SEGMENT_KEY, "SABA INTERNATIONAL ");
    static final Map<AlertedPartyKey, String> AP_DATA_5_MATCH =
        Map.of(ALERTED_NAMEADDRESS_SEGMENT_KEY, "SABA INTERNATIONAL ");
    static final Map<AlertedPartyKey, String> AP_DATA_6 =
        Map.of(ALERTED_NAME_KEY, "BOGAWANTALAWA COFFEE "
            + "PVT LTD");
    static final Map<AlertedPartyKey, String> AP_DATA_7 =
        Map.of(ALERTED_COUNTRY_TOWN_KEY, "AM", ALERTED_NAME_KEY, "BOGAWANTALAWA COFFEE PVT LTD");
    static final Map<AlertedPartyKey, String> AP_DATA_9 =
        Map.of(ALERTED_NAME_KEY, "BOGAWANTALAWA COFFEE "
            + "PVT LTD");
    static final Map<AlertedPartyKey, String> AP_DATA_9_MATCH = Map.of(WILDCARD_SYMBOL, "");

    static final Map<AlertedPartyKey, String> AP_DATA_10 =
        Map.of(ALERTED_ADDRESS_KEY, "IFSCICIC0001874 CITY NASIK, BRANCH INDIRANAGAR INDIA",
            ALERTED_NAME_KEY, "ICIC BANK LTD");

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
