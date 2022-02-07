package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;
import com.silenteight.payments.bridge.svb.oldetl.service.AlertedPartyDataFactory;
import com.silenteight.payments.bridge.svb.oldetl.service.ExtractDisposition;

import java.util.List;

import static com.silenteight.payments.bridge.common.dto.common.CommonTerms.*;
import static com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure.UNSTRUCTURED;

public class ExtractReceivbankAlertedPartyData implements AlertedPartyDataFactory {

  private static final List<String> FIRCO_FORMATS_INT_FED =
      List.of(FIRCO_FORMAT_FED, FIRCO_FORMAT_INT);
  private static final List<String> FIRCO_FORMATS_IAT_I_IAT_O =
      List.of(FIRCO_FORMAT_IAT_I, FIRCO_FORMAT_IAT_O);


  @Override
  public AlertedPartyData extract(final ExtractDisposition extractDisposition) {
    final MessageFieldStructure messageFieldStructure = UNSTRUCTURED;
    var hitTag = extractDisposition.getHitTag();
    var messageData = extractDisposition.getMessageData();
    var messageFormat = extractDisposition.getMessageFormat();

    List<String> tagValueLines = messageData.getLines(hitTag);
    var lastLine = tagValueLines.size() - 1;

    if (FIRCO_FORMATS_INT_FED.contains(messageFormat)) {
      return AlertedPartyData.builder()
          .name(tagValueLines.get(LINE_1).trim())
          .nameAddress(tagValueLines.get(LINE_1).trim())
          .messageFieldStructure(messageFieldStructure)
          .build();
    } else if (FIRCO_FORMATS_IAT_I_IAT_O.contains(messageFormat)) {
      return AlertedPartyData.builder()
          .accountNumber(tagValueLines.get(LINE_1).trim().toUpperCase())
          .name(tagValueLines.get(LINE_2).trim())
          .address(String.join(" ", tagValueLines.subList(LINE_3, lastLine)))
          .ctryTown(tagValueLines.get(LINE_5).trim())
          .nameAddress(String.join(" ", tagValueLines.subList(LINE_2, lastLine + 1)))
          .messageFieldStructure(messageFieldStructure)
          .build();
    } else {
      return AlertedPartyData.builder().messageFieldStructure(messageFieldStructure).build();
    }
  }
}
