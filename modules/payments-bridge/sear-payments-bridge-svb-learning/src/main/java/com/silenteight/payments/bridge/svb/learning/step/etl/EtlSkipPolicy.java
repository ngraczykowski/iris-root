package com.silenteight.payments.bridge.svb.learning.step.etl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class EtlSkipPolicy implements SkipPolicy {

  @Override
  public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
    log.error("SkipPolicyException skipped due to item skipCount {}", skipCount);
    return true;
  }
}
