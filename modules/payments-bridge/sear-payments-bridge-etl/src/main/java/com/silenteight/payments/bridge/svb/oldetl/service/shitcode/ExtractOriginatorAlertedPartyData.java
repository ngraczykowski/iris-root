package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import java.util.List;

@RequiredArgsConstructor
public class ExtractOriginatorAlertedPartyData {

  private static final int LINE_1 = 0;
  private static final int LINE_2 = 1;
  private static final int LINE_3 = 2;
  private static final int LINE_4 = 3;

  private final MessageData messageData;

  public AlertedPartyData extract(
      MessageFieldStructure messageFieldStructure, String fircoFormat) {

    var lines = messageData.getLines("ORIGINATOR");
    var lastLine = getLastLineNotUS(lines);
    var firstLineLength = lines.get(0).length();

    var builder = AlertedPartyData.builder()
        .messageFieldStructure(messageFieldStructure)
        .ctryTown(lines.get(lastLine));

    if (firstLineLength == 2) {
      builder
          .accountNumber(lines.get(LINE_2))
          .name(lines.get(LINE_3))
          .addresses(lines.subList(LINE_4, lastLine))
          .nameAddresses(lines.subList(LINE_3, lastLine + 1));
    } else if (fircoFormat.equals("FED")) {
      builder
          .name(lines.get(LINE_1))
          .addresses(lines.subList(LINE_2, lastLine))
          .nameAddresses(lines.subList(LINE_1, lastLine + 1));
    } else {
      builder
          .accountNumber(lines.get(LINE_1))
          .name(lines.get(LINE_2))
          .addresses(lines.subList(LINE_3, lastLine))
          .nameAddresses(lines.subList(LINE_2, lastLine + 1));
    }

    return builder.build();
  }

  private static int getLastLineNotUS(List<String> lines) {
    var lastIndex = lines.size() - 1;
    if (lines.get(lastIndex).equals("US"))
      return lastIndex - 1;
    return lastIndex;
  }
}
