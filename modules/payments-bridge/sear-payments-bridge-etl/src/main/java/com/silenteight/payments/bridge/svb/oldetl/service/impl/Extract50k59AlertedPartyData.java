package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.model.UnsupportedMessageException;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

@RequiredArgsConstructor
public class Extract50k59AlertedPartyData {

  private static final int LINE_1 = 0;
  private static final int LINE_2 = 1;
  private static final int LINE_3 = 2;

  private final MessageData messageData;

  public AlertedPartyData extract(String hitTag, MessageFieldStructure messageFieldStructure) {
    var lines = messageData.getLines(hitTag);
    var lastLine = lines.size() - 1;

    if (lines.get(0).charAt(0) != '/')
      throw new UnsupportedMessageException("First char is not / when hitTag= " + hitTag);

    String address = lines.size() > 2 ?
                     String.join(" ", lines.subList(LINE_3, lastLine)) :
                     "";
    String ctryTown = lines.size() > 2 ?
                      lines.get(lastLine) :
                      "";

    return AlertedPartyData.builder()
        .messageFieldStructure(messageFieldStructure)
        .accountNumber(lines.get(LINE_1).substring(1).trim().toUpperCase())
        .name(lines.get(LINE_2).trim())
        .address(address.trim())
        .nameAddress(String.join(" ", lines.subList(LINE_2, lastLine + 1)).trim())
        .ctryTown(ctryTown.trim())
        .build();
  }
}
