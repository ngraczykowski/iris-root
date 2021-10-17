package com.silenteight.payments.bridge.svb.oldetl.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageStructureGtexTest {

  private MessageStructureGtex messageStructureGtex;

  private static final String MESSAGE =
      "{1:F01SVBKGB2LAXXX2279020053}{2:O1030930210920SCBLDEFXAXXX08611918842109200830N}"
          + "{3:{108:2109200022858-05}{119:STP}{111:001}{121:69ecb9af-0633-4cf4-982b-81"
          + "5bff8c5fa6}}{4:\n"
          + ":20:210920022858\n"
          + ":23B:CRED\n"
          + ":32A:999999EUR12345,\n"
          + ":33B:EUR12345,\n"
          + ":50K:/12345678901\n"
          + "XXXXXXXX XXXXXXXX, INC.\n"
          + "1234 BROADWAY - FLOOR 123\n"
          + "NEW YORK NY 10004\n"
          + "US/NEW YORK\n"
          + ":52A:SVBKUS6S\n"
          + ":59:/XX99SVBK99999999999999\n"
          + "XXXXXXXX XXXXXXXX XXXXXXXX\n"
          + "XXXXXX XXXXXX, ROE LANE\n"
          + "XXXXXXXXXXXXXX, DUBLIN, IRELAND\n"
          + ":71A:OUR\n"
          + "-}{5:{CHK:911D95750D93}}";

  @BeforeEach
  void setUp() {
    messageStructureGtex = new MessageStructureGtex(null, null, MESSAGE);
  }

  @Test
  void shouldExtractAccountNumber() {
    var response = messageStructureGtex.getAccountNumber(
        GetAccountNumberRequest.builder().matchingFields(
            List.of()).tag("tag").build());
    assertTrue(response.isPresent());
    assertThat(response.get()).isEqualTo("12345678901");
  }

}
