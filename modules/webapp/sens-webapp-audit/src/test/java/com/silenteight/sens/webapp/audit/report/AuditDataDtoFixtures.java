package com.silenteight.sens.webapp.audit.report;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.auditing.bs.AuditDataDto;

import java.sql.Timestamp;

import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AuditDataDtoFixtures {

  static final AuditDataDto DECISION_TREE_ADD_AUDIT_DATA =
      AuditDataDto
          .builder()
          .eventId(fromString("05bf9714-b1ee-4778-a733-6151df70fca3"))
          .correlationId(fromString("2e9f8302-12e3-47c0-ae6c-2c9313785d1d"))
          .timestamp(Timestamp.valueOf("2020-04-15 12:14:32.456"))
          .type("add")
          .principal("jsmith")
          .entityId("1234")
          .entityClass("decisionTree")
          .entityAction("add")
          .details("Decision Tree added")
          .build();

  static final AuditDataDto REASONING_BRANCH_CHANGE_AUDIT_DATA =
      AuditDataDto
          .builder()
          .eventId(fromString("306659cf-569d-4138-8a71-2ec0578653b1"))
          .correlationId(fromString("eacffbea-567a-40b8-af84-a10cec371a70"))
          .timestamp(Timestamp.valueOf("2020-04-25 15:47:12.834"))
          .type("change")
          .principal("jdoe")
          .entityId("9876")
          .entityClass("reasoningBranch")
          .entityAction("change")
          .details("Reasoning branch changed")
          .build();
}
