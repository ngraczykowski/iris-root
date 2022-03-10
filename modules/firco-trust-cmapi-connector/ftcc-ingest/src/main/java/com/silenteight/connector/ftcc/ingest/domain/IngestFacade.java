package com.silenteight.connector.ftcc.ingest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.RegistrationApiClient;
import com.silenteight.connector.ftcc.ingest.dto.input.RequestDto;

import java.util.UUID;

@RequiredArgsConstructor
public class IngestFacade {

  @NonNull
  private final BatchIdGenerator batchIdGenerator;
  @NonNull
  private final RequestService requestService;
  @NonNull
  private final MessageService messageService;
  @NonNull
  private final RegistrationApiClient registrationApiClient;

  public void ingest(@NonNull RequestDto request) {
    UUID batchId = batchIdGenerator.generate();
    createRequestAndMessages(request, batchId);
    registerBatch(request, batchId);
  }

  private void createRequestAndMessages(RequestDto request, UUID batchId) {
    requestService.create(batchId);
    request.getMessages().forEach(message -> messageService.create(batchId, message));
  }

  private void registerBatch(RequestDto request, UUID batchId) {
    Batch batch = Batch.builder()
        .batchId(batchId.toString())
        .alertsCount(request.getMessagesCount())
        .build();
    registrationApiClient.registerBatch(batch);
  }
}
