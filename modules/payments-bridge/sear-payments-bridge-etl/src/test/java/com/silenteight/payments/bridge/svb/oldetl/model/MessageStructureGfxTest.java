package com.silenteight.payments.bridge.svb.oldetl.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageStructureGfxTest {

  private MessageStructureGfx messageStructureGfx;

  private static final String MESSAGE = "[FIRCOSOFT     X] SVB BE MUX FILTER\n"
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

  @BeforeEach
  void setUp() {
    messageStructureGfx = new MessageStructureGfx(null, null, MESSAGE);
  }

  @Test
  void shouldExtractAccountNumber() {
    var response = messageStructureGfx.getAccountNumber(
        GetAccountNumberRequest.builder().matchingFields(
            List.of()).tag("tag").build());
    assertTrue(response.isPresent());
    assertThat(response.get()).isEqualTo("1234567890");
  }
}
