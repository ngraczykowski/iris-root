package com.silenteight.universaldatasource.api.library.name.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.name.v1.AlertedPartyName;

@Value
@Builder
public class AlertedPartyNameOut {

  String name;

  static AlertedPartyNameOut createFrom(AlertedPartyName input) {
    return AlertedPartyNameOut.builder()
        .name(input.getName())
        .build();
  }
}
