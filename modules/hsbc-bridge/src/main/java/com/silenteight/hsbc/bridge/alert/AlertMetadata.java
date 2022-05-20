package com.silenteight.hsbc.bridge.alert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.hsbc.bridge.alert.dto.MetadataKey;

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
}
