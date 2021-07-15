package com.silenteight.searpayments.scb.etl;

import com.silenteight.searpayments.scb.domain.AlertService;
import lombok.NonNull;

import com.silenteight.searpayments.bridge.AlertEtl;
import com.silenteight.searpayments.bridge.dto.input.AlertMessageDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ScbAlertEtl implements AlertEtl {

  private final AlertParser alertParser;
  private final AlertService alertService;

  @Override
  public long invoke(@NonNull AlertMessageDto alertDto) {
//    alertParser.invoke()
    return 0;
  }
}
