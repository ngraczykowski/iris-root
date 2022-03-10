package com.silenteight.connector.ftcc.ingest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
class RequestService {

  @NonNull
  private final RequestRepository requestRepository;

  public void create(@NonNull UUID batchId) {
    RequestEntity requestEntity = RequestEntity.builder()
        .batchId(batchId)
        .build();
    requestRepository.save(requestEntity);
  }
}
