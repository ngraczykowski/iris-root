package com.silenteight.serp.governance.qa.manage.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.qa.manage.domain.dto.EraseAlertRequest;

import java.util.Optional;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Slf4j
public class AlertService {

  @NonNull
  private final AlertRepository alertRepository;
  @NonNull
  private final AuditingLogger auditingLogger;

  @Transactional
  public void eraseAlert(EraseAlertRequest request) {
    Optional<Alert> alert = getAlert(request.getAlertId());
    if (alert.isEmpty())
      return;

    request.preAudit(auditingLogger::log);
    alertRepository.delete(alert.get());
    request.postAudit(auditingLogger::log);
  }

  private Optional<Alert> getAlert(long alertId) {
    return alertRepository.findById(alertId);
  }
}
