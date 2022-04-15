package com.silenteight.sens.webapp.audit.log.amqp.listener;

import lombok.NonNull;

import com.silenteight.audit.bs.api.v1.AuditData;

public interface AuditDataMessageHandler {

  void handle(@NonNull AuditData message);
}
