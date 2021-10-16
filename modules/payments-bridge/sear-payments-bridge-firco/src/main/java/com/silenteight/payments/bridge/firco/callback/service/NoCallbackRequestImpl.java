package com.silenteight.payments.bridge.firco.callback.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class NoCallbackRequestImpl implements CallbackRequest {

  @Override
  public void execute() {
    log.debug("No decision callback executed.");
  }
}
