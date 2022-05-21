package com.silenteight.hsbc.bridge.bulk;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
class BulkAlertMetadata {

  private String key;
  private String value;
}
