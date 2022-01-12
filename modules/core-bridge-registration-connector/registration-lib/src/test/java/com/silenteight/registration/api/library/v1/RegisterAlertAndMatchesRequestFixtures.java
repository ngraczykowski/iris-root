package com.silenteight.registration.api.library.v1;

import java.util.List;

class RegisterAlertAndMatchesRequestFixtures {

  static final String MATCH_ID = "match_id";
  static final String ALERT_ID = "alert_id";
  static final String BATCH_ID = "batch_id";

  static final List<MatchIn> MATCH_INS = List.of(MatchIn.builder()
      .matchId(MATCH_ID)
      .build());

  static final List<AlertWithMatchesIn> ALERT_WITH_MATCHES_IN = List.of(
      AlertWithMatchesIn.builder()
          .alertId(ALERT_ID)
          .status(AlertStatusIn.SUCCESS)
          .errorDescription("failure cause")
          .metadata("metadata")
          .matches(MATCH_INS)
          .build()
  );

  static final RegisterAlertsAndMatchesIn REGISTER_ALERTS_AND_MATCHES_IN =
      RegisterAlertsAndMatchesIn
          .builder()
          .batchId(BATCH_ID)
          .alertsWithMatches(ALERT_WITH_MATCHES_IN)
          .build();
}
