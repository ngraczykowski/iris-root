package com.silenteight.serp.governance.policy.domain.dto;

import com.silenteight.auditing.bs.AuditDataDto;

import java.util.function.Consumer;

public interface AuditableRequest {

  void preAudit(Consumer<AuditDataDto> logger);

  void postAudit(Consumer<AuditDataDto> logger);
}
