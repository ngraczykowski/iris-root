package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import java.util.List;

@RequiredArgsConstructor
public class ExtractReceivbankAlertedPartyData {

  private static final List<String> MESSAGE_FORMATS_INT_FED = List.of("FED", "INT");
  private static final List<String> MESSAGE_FORMATS_IAT_I_IAT_O = List.of("IAT-I", "IAT-O");

  private static final int FIRST_LINE = 0;
  private static final int SECOND_LINE = 1;
  private static final int FIFTH_LINE = 4;

  private final MessageData messageData;
  private final String hitTag;
  private final String messageFormat;

  public AlertedPartyData extract(MessageFieldStructure messageFieldStructure) {

    List<String> tagValueLines = messageData.getLines(hitTag);
    var lastLine = tagValueLines.size() - 1;

    if (MESSAGE_FORMATS_INT_FED.contains(messageFormat)) {
      return AlertedPartyData.builder()
          .name(tagValueLines.get(FIRST_LINE).trim())
          .messageFieldStructure(messageFieldStructure)
          .build();
    } else if (MESSAGE_FORMATS_IAT_I_IAT_O.contains(messageFormat)) {
      return AlertedPartyData.builder()
          .accountNumber(tagValueLines.get(FIRST_LINE).trim())
          .name(tagValueLines.get(SECOND_LINE).trim())
          .addresses(tagValueLines.subList(SECOND_LINE, lastLine))
          .ctryTown(tagValueLines.get(FIFTH_LINE).trim())
          .nameAddresses(tagValueLines.subList(SECOND_LINE, lastLine + 1))
          .messageFieldStructure(messageFieldStructure)
          .build();
    } else {
      return AlertedPartyData.builder().messageFieldStructure(messageFieldStructure).build();
    }
  }
}
