package com.silenteight.sens.webapp.backend.changerequest.create;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.changerequest.CreateChangeRequestCommand;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static java.time.OffsetDateTime.parse;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class CreateChangeRequestMessageHandlerFixtures {

  static final CreateChangeRequestCommand CREATE_MESSAGE =
      CreateChangeRequestCommand.newBuilder()
          .setBulkChangeId(fromJavaUuid(fromString("2e9f8302-12e3-47c0-ae6c-2c9313785d1d")))
          .setCorrelationId(fromJavaUuid(fromString("05bf9714-b1ee-4778-a733-6151df70fca3")))
          .setMakerUsername("maker")
          .setMakerComment("Maker comment")
          .setCreatedAt(toTimestamp(parse("2020-05-20T10:15:30+01:00")))
          .build();
}
