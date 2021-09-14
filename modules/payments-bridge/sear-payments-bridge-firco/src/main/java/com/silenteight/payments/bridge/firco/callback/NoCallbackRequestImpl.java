package com.silenteight.payments.bridge.firco.callback;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class NoCallbackRequestImpl implements CallbackRequest {

  @Override
  public void invoke() {
    log.debug("invoke");
  }

}
