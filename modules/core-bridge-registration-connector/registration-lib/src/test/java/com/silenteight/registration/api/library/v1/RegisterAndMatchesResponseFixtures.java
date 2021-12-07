package com.silenteight.registration.api.library.v1;

import com.silenteight.registration.api.library.v1.RegisterAlertsAndMatchesOut;
import com.silenteight.registration.internal.proto.v1.RegisterAlertsAndMatchesResponse;
import com.silenteight.registration.internal.proto.v1.RegisteredAlertWithMatches;
import com.silenteight.registration.internal.proto.v1.RegisteredMatch;

import java.util.List;

class RegisterAndMatchesResponseFixtures {

  static final String MATCH_ID = "match_id";
  static final String MATCH_NAME = "match_name";

  static final String ALERT_ID = "alert_id";
  static final String ALERT_NAME = "alert_name";

  static final List<RegisteredMatch> GRPC_REGISTERED_MATCHES = List.of(
      RegisteredMatch.newBuilder()
          .setMatchId(MATCH_ID)
          .setMatchName(MATCH_NAME)
          .build()
  );

  static final List<RegisteredAlertWithMatches> REGISTERED_ALERT_WITH_MATCHES = List.of(
      RegisteredAlertWithMatches.newBuilder()
          .setAlertId(ALERT_ID)
          .setAlertName(ALERT_NAME)
          .addAllRegisteredMatches(GRPC_REGISTERED_MATCHES)
          .build()
  );

  static final RegisterAlertsAndMatchesResponse GRPC_RESPONSE =
      RegisterAlertsAndMatchesResponse.newBuilder()
          .addAllRegisteredAlertsWithMatches(REGISTERED_ALERT_WITH_MATCHES)
          .build();

  static final RegisterAlertsAndMatchesOut RESPONSE =
      RegisterAlertsAndMatchesOut.createFrom(GRPC_RESPONSE);
}
