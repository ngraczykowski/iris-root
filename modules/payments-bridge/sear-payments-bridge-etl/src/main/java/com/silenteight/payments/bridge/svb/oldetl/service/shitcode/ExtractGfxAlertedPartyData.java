package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

@RequiredArgsConstructor
public class ExtractGfxAlertedPartyData {

  private final MessageData messageData;
  private final String hitTag;

  public AlertedPartyData extract(MessageFieldStructure messageFieldStructure) {
    var hitTagLines = messageData.getLines(hitTag);

    if (hitTagLines.size() < 3 && hitTagLines.size() > 7)
      throw new IllegalArgumentException("I've no idea how to get data from " + hitTag);

    var partyDataBuilder = AlertedPartyData.builder()
        .messageFieldStructure(messageFieldStructure);

    switch (hitTagLines.size()) {
      default:
      case 3:
        return partyDataBuilder
            .name(hitTagLines.get(2))
            .nameAddress(hitTagLines.get(2))
            .build();

      case 4:
        return partyDataBuilder
            .name(hitTagLines.get(2))
            .address(hitTagLines.get(3))
            .ctryTown(hitTagLines.get(3))
            .nameAddresses(hitTagLines.subList(2, 4))
            .build();

      case 5:
        return partyDataBuilder
            .name(hitTagLines.get(2))
            .address(hitTagLines.get(3))
            .address(hitTagLines.get(4))
            .ctryTown(hitTagLines.get(4))
            .nameAddresses(hitTagLines.subList(2, 5))
            .build();

      case 6: {
        var builder = partyDataBuilder
            .name(hitTagLines.get(2))
            .address(hitTagLines.get(3))
            .address(hitTagLines.get(4));

        if (hitTagLines.get(5).length() > 2) {
          builder
              .address(hitTagLines.get(5))
              .ctryTown(hitTagLines.get(5))
              .nameAddresses(hitTagLines.subList(2, 6));
        } else {
          builder
              .ctryTown(hitTagLines.get(4))
              .nameAddresses(hitTagLines.subList(2, 5));
        }

        return builder.build();
      }

      case 7:
        return partyDataBuilder
            .name(hitTagLines.get(2))
            .address(hitTagLines.get(3))
            .address(hitTagLines.get(4))
            .address(hitTagLines.get(5))
            .ctryTown(hitTagLines.get(5))
            .nameAddresses(hitTagLines.subList(2, 6))
            .build();
    }
  }
}
