package com.silenteight.payments.bridge.common.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AeAlert;

/**
 * Signals finishing alert registration process from CMAPI origin.
 */
@RequiredArgsConstructor
@Getter
public class SolvingAlertRegisteredEvent {
  private final AeAlert aeAlert;
}
