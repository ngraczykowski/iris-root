package com.silenteight.sens.webapp.common.adapter.audit;

import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;

public interface AuditService<T> {

  CsvBuilder<T> generateAuditReport();
}
