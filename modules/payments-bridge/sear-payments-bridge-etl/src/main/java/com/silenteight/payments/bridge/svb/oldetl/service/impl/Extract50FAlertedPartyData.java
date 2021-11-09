package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

@RequiredArgsConstructor
public class Extract50FAlertedPartyData {

  private static final int FIRST_LINE = 0;
  private static final int SECOND_LINE = 1;

  private final MessageData messageData;
  private final String hitTag;

  public AlertedPartyData extract(MessageFieldStructure messageFieldStructure) {
    var tagValueLines = messageData.getLines(hitTag);

    return new ExtractFormatFData(messageFieldStructure).extract(
        tagValueLines, FIRST_LINE, SECOND_LINE);
  }

}
