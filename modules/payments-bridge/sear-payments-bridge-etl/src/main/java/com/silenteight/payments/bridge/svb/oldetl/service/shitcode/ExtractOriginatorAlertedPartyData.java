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

  private static final String ORIGINATOR_TAG = "ORIGINATOR";
  private static final String FED_FIRCO_FORMAT = "FED";

  private final MessageData messageData;

  public AlertedPartyData extract(
      MessageFieldStructure messageFieldStructure, String fircoFormat) {

    var lines = messageData.getLines(ORIGINATOR_TAG);
    var lastLine = getLastLineNotUS(lines);
    var firstLineLength = lines.get(0).length();

    var alertedPartyDataBuilder = AlertedPartyData.builder()
        .messageFieldStructure(messageFieldStructure);

    if (lines.size() == 2)
      return alertedPartyDataBuilder
          .name(lines.get(LINE_1).trim())
          .nameAddress(lines.get(LINE_1).trim())
          .build();

    if (firstLineLength == 2)
      return alertedPartyDataBuilder
          .accountNumber(lines.get(LINE_2).trim())
          .name(lines.get(LINE_3).trim())
          .address(String.join(" ", lines.subList(LINE_4, lastLine)).trim())
          .nameAddress(String.join(" ", lines.subList(LINE_3, lastLine + 1)).trim())
          .ctryTown(lines.get(lastLine).trim())
          .build();

    if (FED_FIRCO_FORMAT.equals(fircoFormat))
      return alertedPartyDataBuilder
          .name(lines.get(LINE_1).trim())
          .address(String.join(" ", lines.subList(LINE_2, lastLine)).trim())
          .nameAddress(String.join(" ", lines.subList(LINE_1, lastLine + 1)).trim())
          .ctryTown(lines.get(lastLine).trim())
          .build();

    return alertedPartyDataBuilder
        .accountNumber(lines.get(LINE_1).trim())
        .name(lines.get(LINE_2).trim())
        .address(String.join(" ", lines.subList(LINE_3, lastLine)).trim())
        .nameAddress(String.join(" ", lines.subList(LINE_2, lastLine + 1)).trim())
        .ctryTown(lines.get(lastLine).trim())
        .build();
  }

  private static int getLastLineNotUS(List<String> lines) {
    var lastIndex = lines.size() - 1;
    if (lines.get(lastIndex).equals("US"))
      return lastIndex - 1;
    return lastIndex;
  }
}
