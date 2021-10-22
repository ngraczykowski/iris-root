package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import java.util.List;

@RequiredArgsConstructor
public class ExtractBeneOrgbankInsbankAlertedPartyData {

  private static final List<String> MESSAGE_FORMATS_FED_IAT_O_INT = List.of("FED", "IAT-O", "INT");
  private static final List<String> MESSAGE_FORMATS_IAT_I = List.of("IAT-I");

  private static final int FIRST_LINE = 0;
  private static final int SECOND_LINE = 1;
  private static final int THIRD_LINE = 2;
  private static final int FOURTH_LINE = 3;
  private static final int FIFTH_LINE = 4;
  private static final int SIXTH_LINE = 5;

  private final MessageData messageData;
  private final String hitTag;
  private final String messageFormat;

  public AlertedPartyData extract(MessageFieldStructure messageFieldStructure) {
    List<String> tagValueLines = messageData.getLines(hitTag);
    if (tagValueLines.get(0).length() == 2) {
      return AlertedPartyData.builder()
          .accountNumber(tagValueLines.get(SECOND_LINE))
          .name(tagValueLines.get(THIRD_LINE))
          .address(tagValueLines.get(FOURTH_LINE))
          .address(tagValueLines.get(FIFTH_LINE))
          .ctryTown(tagValueLines.get(SIXTH_LINE))
          .nameAddress(String.format(
              "%s %s %s",
              tagValueLines.get(THIRD_LINE),
              tagValueLines.get(FOURTH_LINE),
              tagValueLines.get(FIFTH_LINE)))
          .messageFieldStructure(messageFieldStructure)
          .build();
    } else if (tagValueLines.get(0).length() > 2) {
      if (MESSAGE_FORMATS_FED_IAT_O_INT.contains(messageFormat)) {
        return AlertedPartyData.builder()
            .name(tagValueLines.get(FIRST_LINE))
            .address(tagValueLines.get(SECOND_LINE))
            .address(tagValueLines.get(THIRD_LINE))
            .ctryTown(tagValueLines.get(FOURTH_LINE))
            .nameAddress(String.format(
                "%s %s %s",
                tagValueLines.get(FIRST_LINE),
                tagValueLines.get(SECOND_LINE),
                tagValueLines.get(THIRD_LINE)))
            .messageFieldStructure(messageFieldStructure)
            .build();
      } else if (MESSAGE_FORMATS_IAT_I.contains(messageFormat)) {
        return AlertedPartyData.builder()
            .accountNumber(tagValueLines.get(FIRST_LINE))
            .name(tagValueLines.get(SECOND_LINE))
            .address(tagValueLines.get(THIRD_LINE))
            .address(tagValueLines.get(FOURTH_LINE))
            .ctryTown(tagValueLines.get(FIFTH_LINE))
            .nameAddress(String.format(
                "%s %s %s",
                tagValueLines.get(SECOND_LINE),
                tagValueLines.get(THIRD_LINE),
                tagValueLines.get(FOURTH_LINE)))
            .messageFieldStructure(messageFieldStructure)
            .build();
      }
    }
    return AlertedPartyData.builder().messageFieldStructure(messageFieldStructure).build();
  }
}
