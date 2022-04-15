package com.silenteight.sens.webapp.audit.log;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.audit.bs.api.v1.AuditData;
import com.silenteight.sens.webapp.audit.domain.AuditLogService;
import com.silenteight.sens.webapp.audit.log.amqp.listener.AuditDataMessageHandler;

import static com.silenteight.sep.base.common.logging.LogMarkers.INTERNAL;

@Slf4j
@RequiredArgsConstructor
class AuditLogUseCase implements AuditDataMessageHandler {

  @NonNull
  private final AuditLogService auditLogService;

  @Override
  public void handle(@NonNull AuditData message) {
    log.info(INTERNAL, "Audit message to be logged: message={}" + message);
    auditLogService.log(message);
    log.info(INTERNAL, "Audit message has been logged: message={}" + message);
  }
}
