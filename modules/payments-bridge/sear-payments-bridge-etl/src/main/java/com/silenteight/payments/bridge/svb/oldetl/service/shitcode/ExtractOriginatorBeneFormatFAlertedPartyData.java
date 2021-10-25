package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

@RequiredArgsConstructor
public class ExtractOriginatorBeneFormatFAlertedPartyData {

  private static final int SECOND_LINE = 1;
  private static final int THIRD_LINE = 2;

  private final MessageData messageData;
  private final String hitTag;

  public AlertedPartyData extract(MessageFieldStructure messageFieldStructure) {
    var tagValueLines = messageData.getLines(hitTag);

    return new ExtractFormatFData(messageFieldStructure).extract(
        tagValueLines, SECOND_LINE, THIRD_LINE);
  }
}
