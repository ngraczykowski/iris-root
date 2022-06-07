package com.silenteight.auditing.bs.amqp;

import com.silenteight.audit.bs.api.v1.AuditData;

public interface AuditDataMessageGateway {

  String ID = "auditDataMessageGateway";

  void send(AuditData message);
}
