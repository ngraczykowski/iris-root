package com.silenteight.connector.ftcc.callback.decision;

import lombok.AllArgsConstructor;
import lombok.Value;

import com.silenteight.connector.ftcc.common.dto.input.StatusInfoDto;

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
