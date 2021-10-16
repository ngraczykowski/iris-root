package com.silenteight.payments.bridge.firco.callback.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class NoopCallbackRequest implements CallbackRequest {

  @Override
  public void execute() {
    log.info("No-op decision callback.");
  }
}
