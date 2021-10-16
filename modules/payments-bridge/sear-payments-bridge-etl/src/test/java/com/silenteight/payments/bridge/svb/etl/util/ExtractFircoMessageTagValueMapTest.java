package com.silenteight.payments.bridge.svb.etl.util;

import org.junit.jupiter.api.Test;

import static com.silenteight.payments.bridge.svb.etl.util.ExtractFircoMessageTagValueMap.extract;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ExtractFircoMessageTagValueMapTest {

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

  @Test
  void shouldExtractKeyValues() {
    var tagValues = extract(MESSAGE);
    assertThat(tagValues.get("APPLI")).isEqualTo("GFX\n");
    assertThat(tagValues.get("ORIGINATOR")).isEqualTo("AC\n"
        + "1234567890\n"
        + "XXXXXXXXXXXXXX XXX\n"
        + "123456 XXXXXXXXXXXX\n"
        + "JERSEY CITY, NJ 07302\n"
        + "US\n");
  }
}
