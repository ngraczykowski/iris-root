package com.silenteight.scb.ingest.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.registration.api.library.v1.RegisterBatchIn;
import com.silenteight.registration.api.library.v1.RegistrationLibraryException;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;
import com.silenteight.scb.ingest.domain.model.Batch;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RegistrationGrpcAdapter implements RegistrationApiClient {

  private final RegistrationServiceClient registrationServiceClient;

  @Override
  @Retryable(value = RegistrationLibraryException.class)
  public void registerBatch(Batch batch) {
    //TODO: create mapper
    var registerBatchIn = RegisterBatchIn.builder().build();
    registrationServiceClient.registerBatch(registerBatchIn);
    log.info("Batch with id: {} registered in Core Bridge.", batch.id());
  }

}
