package com.silenteight.warehouse.report.remove;

import java.time.OffsetDateTime;

public interface ReportsRemoval {

  long removeOlderThan(OffsetDateTime dayToRemoveReports);
}
