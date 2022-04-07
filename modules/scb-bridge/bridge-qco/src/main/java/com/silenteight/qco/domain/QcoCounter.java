package com.silenteight.qco.domain;

import com.silenteight.qco.domain.model.ChangeCondition;

import org.springframework.stereotype.Component;

@Component
class QcoCounter {

  /**
   * @return true if the counter is overflowed otherwise returns false.
   */
  boolean increaseAndCheckOverflow(ChangeCondition condition) {
    //TODO: implement
    return false;
  }
}