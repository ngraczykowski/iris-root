package com.silenteight.payments.bridge.firco.decision;

import lombok.AllArgsConstructor;
import lombok.Value;

import com.silenteight.payments.bridge.firco.dto.input.StatusInfoDto;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class DestinationStatus {

  StatusInfoDto status;

  boolean valid;

  static DestinationStatus createValid(StatusInfoDto status) {
    return new DestinationStatus(status, true);
  }

  static DestinationStatus createInvalid(StatusInfoDto status) {
    return new DestinationStatus(status, false);
  }
}
