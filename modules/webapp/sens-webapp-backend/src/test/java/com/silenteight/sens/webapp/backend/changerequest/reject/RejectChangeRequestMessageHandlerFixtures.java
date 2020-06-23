package com.silenteight.sens.webapp.backend.changerequest.reject;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.changerequest.RejectChangeRequestCommand;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static java.time.OffsetDateTime.parse;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class RejectChangeRequestMessageHandlerFixtures {

  static final RejectChangeRequestCommand REJECT_MESSAGE =
      RejectChangeRequestCommand.newBuilder()
          .setChangeRequestId(5L)
          .setCorrelationId(fromJavaUuid(fromString("05bf9714-b1ee-4778-a733-6151df70fca3")))
          .setRejectorUsername("rejector")
          .setRejectorComment("rejector comment ABC")
          .setRejectedAt(toTimestamp(parse("2020-05-20T10:15:30+01:00")))
          .build();
}
