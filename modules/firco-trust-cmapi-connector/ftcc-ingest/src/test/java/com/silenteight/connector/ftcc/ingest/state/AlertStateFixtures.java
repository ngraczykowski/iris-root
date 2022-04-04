package com.silenteight.connector.ftcc.ingest.state;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.fab.api.v1.AlertMessageStored.State;

import java.util.UUID;

import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State.SOLVED_FALSE_POSITIVE;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AlertStateFixtures {

  static final UUID BATCH_ID = fromString("558ecea2-a1d5-11eb-bcbc-0242ac130002");
  static final UUID MESSAGE_ID = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
  static final String FALSE_POSITIVE_STATUS = "FALSE_POSITIVE";
  static final State FALSE_POSITIVE_STATE = SOLVED_FALSE_POSITIVE;
}
