package com.silenteight.sens.webapp.common.adapter.audit;

import com.silenteight.sens.webapp.common.support.csv.LinesSupplier;

public interface AuditService {

  LinesSupplier generateAuditReport();
}
