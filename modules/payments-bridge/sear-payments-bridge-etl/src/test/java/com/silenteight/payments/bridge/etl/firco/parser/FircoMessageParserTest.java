package com.silenteight.payments.bridge.etl.firco.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FircoMessageParserTest {

  private static final String MESSAGE =
      "[FIRCOSOFT     X] SVB BE MUX FILTER\n"
          + "[APPLI         X] GFX\n"
          + "[UNIT          X] US\n"
          + "[REFERENCE     X] 20212251314100\n"
          + "[TYPE          X] FED 1000 O\n"
          + "[VALUEDATE      ] 2021/08/13\n"
          + "[AMOUNT         ] USD 12345.00\n"
          + "[SENDER         ] 999999999\n"
          + "[ORIGINATOR     ] AC\n"
          + "1234567890\n"
          + "XXXXXXXXXXXXXX XXX\n"
          + "123456 XXXXXXXXXXXX\n"
          + "JERSEY CITY, NJ 07302\n"
          + "US\n"
          + "[DBTACCOFF      ] 001\n"
          + "[DBTACCTYP      ] DDA\n"
          + "[DBTACCNUM      ] 9999999999\n"
          + "[RECEIVBANK     ] XXXXXXXXX XXXX\n"
          + "[RECEIVER       ] 999999999\n"
          + "[BENE           ] AC\n"
          + "9999999999\n"
          + "XXXXXX XXXXXXXX\n"
          + "12345 PLEASANT LAKE BLVD\n"
          + "CLARKSBURG ,\n"
          + "US\n"
          + "[ORGBENEINF     ] /INV/9XXX9X9X99999XXX/ XXXXXXX XXXX\n"
          + "9XX9X999\n"
          + "[FXRATE        X] 1.000000\n"
          + "[TRANCD         ] DOMPC\n"
          + "[CHARGESCD      ] SHA";

  private final FircoMessageParser parser = new FircoMessageParser(MESSAGE);

  @Test
  void shouldExtractKeyValues() {
    var messageData = parser.parse();
    assertThat(messageData.get("APPLI")).isEqualTo("GFX");
    assertThat(messageData.get("ORIGINATOR")).isEqualTo("AC\n"
        + "1234567890\n"
        + "XXXXXXXXXXXXXX XXX\n"
        + "123456 XXXXXXXXXXXX\n"
        + "JERSEY CITY, NJ 07302\n"
        + "US");
    assertThat(messageData.get("CHARGESCD")).isEqualTo("SHA");
    assertThat(messageData.get("FXRATE")).isEqualTo("1.000000");
  }
}
