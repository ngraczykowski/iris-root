package com.silenteight.auditing.bs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import static java.util.UUID.randomUUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AuditDataDtoFixtures {

  static final AuditDataDto DECISION_TREE_ADD_AUDIT_DATA =
      AuditDataDto
          .builder()
          .eventId(randomUUID())
          .correlationId(randomUUID())
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
          .eventId(randomUUID())
          .correlationId(randomUUID())
          .timestamp(Timestamp.valueOf("2020-04-25 15:47:12.834"))
          .type("change")
          .principal("jdoe")
          .entityId("9876")
          .entityClass("reasoningBranch")
          .entityAction("change")
          .details("Reasoning branch changed")
          .build();
}
