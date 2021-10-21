package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import com.google.common.base.Splitter;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ExtractPepAlertedPartyData {

  /* CASE 1
  [ORIGINATOR     ] 99999999999
  NAME NAME NAME
  ADDRESS STREET ADDRESS
  CITY CITY
  US
  */

  /* CASE 2
  [ORGBENEINF     ] NAME NAME NAME
  ADDRESS STREET ADDRESS
  [ORGBENEINF     ] CITY CITY
  *US*123456
  US
  123456
  */

  /* CASE 3
  [ORGBENEINF     ] NAME NAME NAME
  ADDRESS STREET ADDRESS
  [ORGBENEINF     ] New York
  NY
  US
  12345
  */

  private static final int LINE_1 = 0;
  private static final int LINE_2 = 1;
  private static final int LINE_3 = 2;
  private static final int LINE_4 = 3;
  private static final int TO_LINE_3 = 2;
  private static final int TO_LINE_4 = 3;
  protected static final Splitter LINE_SPLITTER = Splitter.on("\n");

  private final MessageData messageData;
  private final String hitTag;

  public AlertedPartyData extract(MessageFieldStructure messageFieldStructure) {
    var values = messageData.findAllValues(hitTag).collect(Collectors.joining("\n"));

    var lines = LINE_SPLITTER.splitToList(values);

    if (lines.size() < 5 || lines.size() > 6)
      throw new IllegalArgumentException("I've no idea how to get data from PEP: " + hitTag);

    switch (lines.size()) {
      default:
      case 5:
        return AlertedPartyData.builder()
            .messageFieldStructure(messageFieldStructure)
            .name(lines.get(LINE_2))
            .address(lines.get(LINE_3))
            .ctryTown(lines.get(LINE_4))
            .nameAddresses(lines.subList(LINE_2, TO_LINE_4))
            .build();
      case 6:
        return AlertedPartyData.builder()
            .messageFieldStructure(messageFieldStructure)
            .name(lines.get(LINE_1))
            .address(lines.get(LINE_2))
            .ctryTown(lines.get(LINE_3))
            .nameAddresses(lines.subList(LINE_1, TO_LINE_3))
            .build();
    }
  }
}
