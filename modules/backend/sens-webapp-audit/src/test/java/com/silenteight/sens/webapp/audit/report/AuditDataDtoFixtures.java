package com.silenteight.sens.webapp.audit.report;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.audit.api.dto.AuditLogDto;

import static java.time.OffsetDateTime.parse;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AuditDataDtoFixtures {

  static final AuditLogDto DECISION_TREE_ADD_AUDIT_DATA =
      AuditLogDto.builder()
          .eventId(fromString("05bf9714-b1ee-4778-a733-6151df70fca3"))
          .correlationId(fromString("2e9f8302-12e3-47c0-ae6c-2c9313785d1d"))
          .timestamp(parse("2020-04-15T12:14:32.456Z"))
          .type("add")
          .principal("jsmith")
          .entityId("1234")
          .entityClass("decisionTree")
          .entityAction("add")
          .details("Decision Tree added")
          .build();

  static final AuditLogDto REASONING_BRANCH_CHANGE_AUDIT_DATA =
      AuditLogDto.builder()
          .eventId(fromString("306659cf-569d-4138-8a71-2ec0578653b1"))
          .correlationId(fromString("eacffbea-567a-40b8-af84-a10cec371a70"))
          .timestamp(parse("2020-04-25T15:47:12.834Z"))
          .type("change")
          .principal("jdoe")
          .entityId("9876")
          .entityClass("reasoningBranch")
          .entityAction("change")
          .details("Reasoning branch changed")
          .build();
}
