package com.silenteight.payments.bridge.svb.oldetl.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageStructurePepTest {

  private MessageStructurePep messageStructurePep;

  private static final String MESSAGE = "[FIRCOSOFT     X] SVB BE MUX FILTER\n"
      + "[APPLI         X] PEP\n"
      + "[UNIT          X] US\n"
      + "[REFERENCE     X] 1234567\n"
      + "[TYPE          X] IAT-I\n"
      + "[VALUEDATE     X] 2021/09/23\n"
      + "[PURPOSE        ] IAT PAYPAL\n"
      + "[AMOUNT        X] USD 400\n"
      + "[INSPMTAMT     X] USD 400\n"
      + "[SENDER         ] 123456789\n"
      + "[SENDERBANK     ] 123456789\n"
      + "WELLS FARGO BANK\n"
      + "01\n"
      + "23456789\n"
      + "US\n"
      + "[ORIGINATOR     ] 0123456789\n"
      + "XXX XXXX AI\n"
      + "SHEN YANG XXX XX XXXX XX XXX XXX XX\n"
      + "SHENYANG\n"
      + "CN\n"
      + "[CRDACCNUM      ] 123456789\n"
      + "[RECEIVBANK     ] 123456789\n"
      + "SILICON VALLEY BANK\n"
      + "01\n"
      + "123456789\n"
      + "US\n"
      + "[RECEIVER       ] 121140399\n"
      + "[BENE           ] 1015865726194\n"
      + "XXXXXX LLC\n"
      + "6064 CLOVERDALE DRIVE\n"
      + "FORT MILL\n"
      + "US\n"
      + "[TRANCD        X] 27\n"
      + "[DESTCNTRY      ] US\n"
      + "[FXMATH        X] FF\n"
      + "[FXREFNB       X]\n"
      + "[ADDENDACNT    X] 0007\n"
      + "[ORIGSTSCD     X] 1";

  @BeforeEach
  void setUp() {
    messageStructurePep = new MessageStructurePep(null, null, MESSAGE);
  }

  @Test
  void shouldExtractAccountNumber() {
    var response = messageStructurePep.getAccountNumber(
        GetAccountNumberRequest.builder().matchingFields(
            List.of()).tag("tag").build());
    assertTrue(response.isPresent());
    assertThat(response.get()).isEqualTo("0123456789");
  }
}
