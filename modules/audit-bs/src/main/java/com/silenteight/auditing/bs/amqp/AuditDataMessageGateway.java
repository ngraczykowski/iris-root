package com.silenteight.auditing.bs.amqp;

import com.silenteight.audit.bs.api.v1.AuditData;

public interface AuditDataMessageGateway {

  void send(AuditData message);
}
