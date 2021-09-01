package com.silenteight.hsbc.bridge.alert.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MetadataKey {

  DISCRIMINATOR("discriminator"),
  EXTENDED_ATTRIBUTE_5("extendedAttribute5"),
  S8_LOB_COUNTRY_CODE("s8_lobCountryCode"),
  TRACKING_ID("trackingId");

  private String name;
}
