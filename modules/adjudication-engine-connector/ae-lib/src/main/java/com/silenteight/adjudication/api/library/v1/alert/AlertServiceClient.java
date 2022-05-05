package com.silenteight.adjudication.api.library.v1.alert;

public interface AlertServiceClient {

  RegisterAlertsAndMatchesOut registerAlertsAndMatches(RegisterAlertsAndMatchesIn command);
}
