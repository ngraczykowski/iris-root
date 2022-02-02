package com.silenteight.payments.bridge.svb.newlearning.step.store;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class StoreSkipPolicy implements SkipPolicy {

  @Override
  public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
    log.error("Skipped item: ", t);
    return true;
  }
}
