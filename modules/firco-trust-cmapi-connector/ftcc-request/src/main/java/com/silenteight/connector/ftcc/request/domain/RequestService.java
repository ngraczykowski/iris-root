package com.silenteight.connector.ftcc.request.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RequestService {

  @NonNull
  private final RequestRepository requestRepository;

  public void create(@NonNull UUID batchId) {
    RequestEntity requestEntity = RequestEntity.builder()
        .batchId(batchId)
        .build();
    requestRepository.save(requestEntity);
  }
}
