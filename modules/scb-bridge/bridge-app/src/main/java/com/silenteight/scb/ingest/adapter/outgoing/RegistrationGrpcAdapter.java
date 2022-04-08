package com.silenteight.scb.ingest.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.registration.api.library.v1.RegistrationLibraryException;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;
import com.silenteight.scb.ingest.domain.model.Batch;
import com.silenteight.scb.ingest.domain.model.RegistrationRequest;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.port.outgoing.RegistrationApiClient;

import org.apache.commons.lang3.time.StopWatch;
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
    StopWatch stopWatch = StopWatch.createStarted();
    log.info("Registering Batch: {} in Core Bridge", batch);
    var registerBatchIn = registrationMapper.toRegisterBatchIn(batch);
    registrationServiceClient.registerBatch(registerBatchIn);
    log.info("Batch {} has been registered in Core Bridge, executed in: {}", batch, stopWatch);
  }

  @Override
  @Retryable(value = RegistrationLibraryException.class)
  public RegistrationResponse registerAlertsAndMatches(RegistrationRequest request) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.info("Registering Alert And Matches in Core Bridge with batchId: {}", request.getBatchId());
    var alertWithMatchesIn = registrationMapper.toRegisterAlertsAndMatchesIn(request);
    var registerAlertsAndMatchesOut =
        registrationServiceClient.registerAlertsAndMatches(alertWithMatchesIn);
    log.info(
        "Alerts And Matches have been registered in Core Bridge with batchId: {}, executed in: {}",
        request.getBatchId(), stopWatch);
    return registrationMapper.toRegistrationResponse(registerAlertsAndMatchesOut);
  }
}
