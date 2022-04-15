package com.silenteight.sens.webapp.audit.log;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.audit.bs.api.v1.AuditData;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static java.time.Instant.parse;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AuditLogFixtures {

  static final AuditData AUDIT_DATA = AuditData.newBuilder()
      .setEventId(fromJavaUuid(fromString("a9b45451-6fde-4832-8dc0-d17b4708d8ca")))
      .setCorrelationId(fromJavaUuid(fromString("b4708d8c-4832-6fde-8dc0-d17b4708d8ca")))
      .setTimestamp(toTimestamp(parse("2020-09-20T13:15:48Z")))
      .setType("create")
      .setPrincipal("jsmith")
      .setEntityId("1234")
      .setEntityClass("object")
      .setEntityAction("CREATE")
      .setDetails("Object created")
      .build();
}
