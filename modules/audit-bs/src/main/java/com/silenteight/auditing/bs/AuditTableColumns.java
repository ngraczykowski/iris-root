package com.silenteight.auditing.bs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AuditTableColumns {

  static final String EVENT_ID = "event_id";
  static final String CORRELATION_ID = "correlation_id";
  static final String TIMESTAMP = "timestamp";
  static final String TYPE = "type";
  static final String PRINCIPAL = "principal";
  static final String ENTITY_ID = "entity_id";
  static final String ENTITY_CLASS = "entity_class";
  static final String ENTITY_ACTION = "entity_action";
  static final String DETAILS = "details";
}
