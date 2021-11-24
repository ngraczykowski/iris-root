package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;
import static com.silenteight.payments.bridge.svb.oldetl.util.CommonTerms.*;

@RequiredArgsConstructor
public class ExtractOriginatorBeneFormatFAlertedPartyData {

  private final MessageData messageData;
  private final String hitTag;

  public AlertedPartyData extract(MessageFieldStructure messageFieldStructure) {
    var tagValueLines = messageData.getLines(hitTag);

    return new ExtractFormatFData(messageFieldStructure).extract(
        tagValueLines, LINE_2, LINE_3);
  }
}
