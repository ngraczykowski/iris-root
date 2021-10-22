package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import java.util.Map;

@RequiredArgsConstructor
public class Extract50FAlertedPartyData {

  private static final int FIRST_LINE = 0;

  private static final String FIRST_STRUCTURED_ROW = "1/";
  private static final String SECOND_STRUCTURED_ROW = "2/";
  private static final String THIRD_STRUCTURED_ROW = "3/";

  private final MessageData messageData;
  private final String hitTag;

  public AlertedPartyData extract(MessageFieldStructure messageFieldStructure) {
    var tagValueLines = messageData.getLines(hitTag);

    var structuredData =
        new java.util.HashMap<>(
            Map.of(FIRST_STRUCTURED_ROW, "", SECOND_STRUCTURED_ROW, "", THIRD_STRUCTURED_ROW, ""));

    tagValueLines.forEach(line -> {
      String newLineBeginning = line.substring(0, 2);
      if (structuredData.containsKey(newLineBeginning)) {
        structuredData.put(
            newLineBeginning, structuredData.get(newLineBeginning) + line.substring(2));
      }
    });

    return AlertedPartyData.builder()
        .accountNumber(tagValueLines.get(FIRST_LINE))
        .name(structuredData.get(FIRST_STRUCTURED_ROW))
        .address(structuredData.get(SECOND_STRUCTURED_ROW))
        .ctryTown(structuredData.get(THIRD_STRUCTURED_ROW))
        .nameAddress(String.format(
            "%s %s %s",
            structuredData.get(FIRST_STRUCTURED_ROW),
            structuredData.get(SECOND_STRUCTURED_ROW),
            structuredData.get(THIRD_STRUCTURED_ROW)))
        .messageFieldStructure(messageFieldStructure)
        .build();
  }

}
