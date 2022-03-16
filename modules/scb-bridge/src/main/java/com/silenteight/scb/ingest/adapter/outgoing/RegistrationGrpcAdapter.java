package com.silenteight.scb.ingest.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.registration.api.library.v1.RegistrationLibraryException;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;
import com.silenteight.scb.ingest.domain.model.Batch;
import com.silenteight.scb.ingest.domain.model.RegistrationRequest;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.port.outgoing.RegistrationApiClient;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RegistrationGrpcAdapter implements RegistrationApiClient {

  private final RegistrationMapper registrationMapper;
  private final RegistrationServiceClient registrationServiceClient;

  @Override
  @Retryable(value = RegistrationLibraryException.class)
  public void registerBatch(Batch batch) {
    var registerBatchIn = registrationMapper.toRegisterBatchIn(batch);
    registrationServiceClient.registerBatch(registerBatchIn);
    log.info("Batch with id: {} registered in Core Bridge.", batch.id());
  }

  @Override
  @Retryable(value = RegistrationLibraryException.class)
  public RegistrationResponse registerAlertsAndMatches(RegistrationRequest request) {
    var alertWithMatchesIn = registrationMapper.toRegisterAlertsAndMatchesIn(request);
    var registerAlertsAndMatchesOut =
        registrationServiceClient.registerAlertsAndMatches(alertWithMatchesIn);
    log.info(
        "Alerts and matches registered in Core Bridge for batch with id: {}",
        request.getBatchId());
    return registrationMapper.toRegistrationResponse(registerAlertsAndMatchesOut);
  }
}
