package com.silenteight.searpaymentsmockup;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class MockAlertEtl implements AlertEtl {

  @Override
  public long invoke(@NonNull AlertDto alertDto) {
    log.info("Processed alert: {}. Returning its ID", alertDto);
    return 1L;
  }
}
