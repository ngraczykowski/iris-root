/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.qco.domain.model.ChangeCondition;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
class QcoCounter {

  private final Map<ChangeCondition, Counter> counterHolder = new ConcurrentHashMap<>();
  private final QcoConfigurationHolder configurationHolder;

  @PostConstruct
  void init() {
    configurationHolder.getConfiguration()
        .forEach((changeCondition, qcoParams) ->
            counterHolder.put(changeCondition, new DefaultCounter(qcoParams.matchThreshold())));
  }

  /**
   * @return true if the counter is overflowed otherwise returns false.
   *     The method will return false when condition is not find in delivered QCO configuration.
   */
  boolean increaseAndCheckOverflow(ChangeCondition condition) {
    return counterHolder.getOrDefault(condition, () -> false)
        .increaseAndResetAfterOverflow();
  }

  @ToString
  @RequiredArgsConstructor
  private static class DefaultCounter implements Counter {

    private long value = 0;
    private final long threshold;

    @Override
    public boolean increaseAndResetAfterOverflow() {
      log.info("Counter of QCO module {}", this);
      if (++value < threshold) {
        return false;
      }
      value = 0;
      return true;
    }
  }

  private interface Counter {

    boolean increaseAndResetAfterOverflow();
  }
}
