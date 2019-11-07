package com.silenteight.sens.webapp.common.adapter.audit;

import java.util.List;

public interface AuditRepository<T> {

  default int getAuditedMaxResults() {
    return 10000;
  }

  List<T> findAudited();
}

