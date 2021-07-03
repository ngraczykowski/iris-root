package com.silenteight.searpaymentsmockup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class MockRecommendAlert implements RecommendAlert {

  private final long alertId;

  @Override
  public void invoke() {
    log.info("Recommending alert {}", alertId);
  }
}
