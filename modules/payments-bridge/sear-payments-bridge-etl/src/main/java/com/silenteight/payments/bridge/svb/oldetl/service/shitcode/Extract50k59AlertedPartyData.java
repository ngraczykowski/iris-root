package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

@RequiredArgsConstructor
public class Extract50k59AlertedPartyData {

  private static final int LINE_1 = 0;
  private static final int LINE_2 = 1;
  private static final int LINE_3 = 2;

  private final MessageData messageData;

  public AlertedPartyData extract(String tag, MessageFieldStructure messageFieldStructure) {
    var lines = messageData.getLines(tag);
    var lastLine = lines.size() - 1;

    if (lines.get(0).charAt(0) != '/')
      throw new IllegalArgumentException("First char is not /");

    return AlertedPartyData.builder()
        .messageFieldStructure(messageFieldStructure)
        .accountNumber(lines.get(LINE_1).substring(1))
        .name(lines.get(LINE_2))
        .addresses(lines.subList(LINE_3, lastLine))
        .nameAddresses(lines.subList(LINE_2, lastLine))
        .ctryTown(lines.get(lastLine))
        .build();
  }
}
