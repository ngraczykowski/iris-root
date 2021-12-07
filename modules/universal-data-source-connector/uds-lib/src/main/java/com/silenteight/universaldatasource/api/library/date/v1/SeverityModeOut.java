package com.silenteight.universaldatasource.api.library.date.v1;

import com.silenteight.datasource.api.date.v1.DateFeatureInput.SeverityMode;

public enum SeverityModeOut {
  NORMAL, STRICT;

  static SeverityModeOut createFrom(SeverityMode entityType) {
    return SeverityModeOut.valueOf(entityType.name());
  }
}
