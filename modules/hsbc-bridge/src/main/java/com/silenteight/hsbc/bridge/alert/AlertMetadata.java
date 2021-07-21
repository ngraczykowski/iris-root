package com.silenteight.hsbc.bridge.alert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
class AlertMetadata {

  private String key;
  private String value;

  AlertMetadata(MetadataKey key, String value) {
    this.key = key.getName();
    this.value = value;
  }

  @AllArgsConstructor
  @Getter
  enum MetadataKey {

    DISCRIMINATOR("discriminator"),
    EXTENDED_ATTRIBUTE_5("extendedAttribute5"),
    S8_LOB_COUNTRY_CODE("s8_lobCountryCode"),
    TRACKING_ID("trackingId");

    private String name;
  }
}
