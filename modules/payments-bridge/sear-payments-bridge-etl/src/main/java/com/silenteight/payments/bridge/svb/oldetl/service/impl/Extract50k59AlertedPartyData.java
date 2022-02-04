package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import com.silenteight.payments.bridge.svb.oldetl.model.UnsupportedMessageException;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.service.AlertedPartyDataFactory;
import com.silenteight.payments.bridge.svb.oldetl.service.ExtractDisposition;

import static com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure.UNSTRUCTURED;
import static com.silenteight.payments.bridge.svb.oldetl.util.CommonTerms.*;

public class Extract50k59AlertedPartyData implements AlertedPartyDataFactory {


  @Override
  public AlertedPartyData extract(final ExtractDisposition extractDisposition) {
    var hitTag = extractDisposition.getHitTag();
    var messageData = extractDisposition.getMessageData();
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
        .messageFieldStructure(UNSTRUCTURED)
        .accountNumber(lines.get(LINE_1).substring(1).trim().toUpperCase())
        .name(lines.get(LINE_2).trim())
        .address(address.trim())
        .nameAddress(String.join(" ", lines.subList(LINE_2, lastLine + 1)).trim())
        .ctryTown(ctryTown.trim())
        .build();

  }
}
