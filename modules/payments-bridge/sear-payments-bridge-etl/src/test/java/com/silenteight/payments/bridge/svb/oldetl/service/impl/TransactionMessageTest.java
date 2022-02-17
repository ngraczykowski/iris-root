package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.payments.bridge.svb.oldetl.service.impl.TransactionMessage.getAllMatchingTexts;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionMessageTest {

  private static final String EXAMPLE_TAG = "TAG";

  @ParameterizedTest
  @MethodSource("testDataProvider")
  void getAllMatchingTextsFunctionTests(
      String tagValue, String matchingTextString, List<String> allMatchingTexts) {

    var messageData = new MessageData(List.of(new MessageTag(EXAMPLE_TAG, tagValue)));
    List<String> actual = getAllMatchingTexts(messageData, EXAMPLE_TAG, matchingTextString);
    assertEquals(allMatchingTexts, actual);
  }

  private static Stream<Arguments> testDataProvider() {
    return Stream.of(
        Arguments.of(
            "/123456789\nTOTAL OIL ASIA PACIFIC PTE LTD\n331 NORTH BRIDGE\n23-01 ODEON TOWERS\n"
                + "188720/SINGAPORE\n",
            "OIL ASIA, SINGAPORE", List.of("OIL ASIA", "SINGAPORE")),
        Arguments.of(
            "/123456789\nTOTAL OIL ASIA) PACIFIC PTE LTD\n331 NORTH BRIDGE\n23-01 ODEON TOWERS\n"
                + "188720/SINGAPORE\n",
            "OIL ASIA), SINGAPORE",
            List.of("OIL ASIA)", "SINGAPORE")),
        Arguments.of(
            "DIAMOND CEMENT LIMITED,220, \nSTRAND ROAD,CHATTOGRAM,BANGLADESH.\n"
                + "FACTORY:ICHANAGAR,BANDAR,\nCHATTOGRAM,BANGLADESH.\n",
            "DIAMOND CEMENT LIMITED, 220, \nSTRAND ROAD, BANGLADESH",
            List.of("DIAMOND CEMENT LIMITED", "220, \nSTRAND ROAD", "BANGLADESH")),
        Arguments.of(
            "SHIPMENT AND TRANSHIPMENT MUST NOT BE EFFECTED VIA ANY PORT IN\n"
                + "CRIMEA / SEVASTOPOL (PORT OF SEVASTOPOL, PORT OF YALTA, PORT OF\nYEVPATORIA\n "
                + "PORT OF KERCH, PORT OF FEODOSIA PORT OF EVPATORIA)\n",
            "SEVASTOPOL, PORT, SEVASTOPOL, CRIMEA",
            List.of("SEVASTOPOL, PORT", "SEVASTOPOL", "CRIMEA")));
  }
}
