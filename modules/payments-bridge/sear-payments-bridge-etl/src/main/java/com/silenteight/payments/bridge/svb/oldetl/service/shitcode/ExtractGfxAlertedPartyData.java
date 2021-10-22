package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

@RequiredArgsConstructor
public class ExtractGfxAlertedPartyData {

  private static final int LINE_3 = 2;
  private static final int LINE_4 = 3;
  private static final int TO_LINE_4 = 4;
  private static final int LINE_5 = 4;
  private static final int TO_LINE_5 = 5;
  private static final int LINE_6 = 5;
  private static final int TO_LINE_6 = 6;
  private static final int LINE_7 = 6;

  private final MessageData messageData;
  private final String hitTag;

  public AlertedPartyData extract(MessageFieldStructure messageFieldStructure) {
    var lines = messageData.getLines(hitTag);

    if (lines.size() < 3 || lines.size() > 7)
      throw new IllegalArgumentException("I've no idea how to get data from " + hitTag);

    var partyDataBuilder = AlertedPartyData.builder()
        .messageFieldStructure(messageFieldStructure);

    switch (lines.size()) {
      default:
      case 3:
        return partyDataBuilder
            .name(lines.get(LINE_3))
            .nameAddress(lines.get(LINE_3))
            .build();

      case 4:
        return partyDataBuilder
            .name(lines.get(LINE_3))
            .address(lines.get(LINE_4))
            .ctryTown(lines.get(LINE_4))
            .nameAddresses(lines.subList(LINE_3, TO_LINE_4))
            .build();

      case 5:
        return partyDataBuilder
            .name(lines.get(LINE_3))
            .address(lines.get(LINE_4))
            .address(lines.get(LINE_5))
            .ctryTown(lines.get(LINE_5))
            .nameAddresses(lines.subList(LINE_3, TO_LINE_5))
            .build();

      case LINE_7: {
        var builder = partyDataBuilder
            .name(lines.get(LINE_3))
            .address(lines.get(LINE_4))
            .address(lines.get(LINE_5));

        if (lines.get(LINE_6).length() > LINE_3) {
          builder
              .address(lines.get(LINE_6))
              .ctryTown(lines.get(LINE_6))
              .nameAddresses(lines.subList(LINE_3, TO_LINE_6));
        } else {
          builder
              .ctryTown(lines.get(LINE_5))
              .nameAddresses(lines.subList(LINE_3, TO_LINE_5));
        }

        return builder.build();
      }

      case 7:
        return partyDataBuilder
            .name(lines.get(LINE_3))
            .address(lines.get(LINE_4))
            .address(lines.get(LINE_5))
            .address(lines.get(LINE_6))
            .ctryTown(lines.get(LINE_6))
            .nameAddresses(lines.subList(LINE_3, TO_LINE_6))
            .build();
    }
  }
}
