package com.silenteight.universaldatasource.api.library.ispep.v2;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.ispep.v2.AlertedPartyItem;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class AlertedPartyItemOut {

  String country;

  static AlertedPartyItemOut createFrom(AlertedPartyItem input) {
    return AlertedPartyItemOut.builder()
        .country(input.getCountry())
        .build();
  }
}
