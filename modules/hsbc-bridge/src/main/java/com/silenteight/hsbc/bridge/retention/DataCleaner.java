package com.silenteight.hsbc.bridge.retention;

import java.time.OffsetDateTime;

public interface DataCleaner {

  void clean(OffsetDateTime expireDate);
}
