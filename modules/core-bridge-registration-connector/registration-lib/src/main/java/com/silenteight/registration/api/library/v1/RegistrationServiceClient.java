package com.silenteight.registration.api.library.v1;

public interface RegistrationServiceClient {

  EmptyOut registerBatch(RegisterBatchIn request);

  EmptyOut notifyBatchError(NotifyBatchErrorIn request);

  RegisterAlertsAndMatchesOut registerAlertsAndMatches(RegisterAlertsAndMatchesIn request);
}
