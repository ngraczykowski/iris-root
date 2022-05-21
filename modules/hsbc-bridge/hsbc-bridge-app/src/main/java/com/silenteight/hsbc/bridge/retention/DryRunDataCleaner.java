package com.silenteight.hsbc.bridge.retention;

import java.time.OffsetDateTime;
import java.util.Set;

public interface DryRunDataCleaner {

  Set<String> getAlertNamesToClean(OffsetDateTime expireDate);
}
