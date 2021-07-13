package com.silenteight.searpayments.scb.etl;

import lombok.NonNull;

import com.silenteight.searpayments.bridge.AlertEtl;
import com.silenteight.searpayments.bridge.dto.input.AlertMessageDto;

class ScbAlertEtl implements AlertEtl {

  @Override
  public long invoke(@NonNull AlertMessageDto alertDto) {
    return 0;
  }
}
