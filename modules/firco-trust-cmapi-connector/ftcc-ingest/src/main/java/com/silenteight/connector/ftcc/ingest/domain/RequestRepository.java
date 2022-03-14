package com.silenteight.connector.ftcc.ingest.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

interface RequestRepository extends Repository<RequestEntity, Long> {

  RequestEntity save(RequestEntity requestEntity);

  Optional<RequestEntity> findByBatchId(UUID batchId);
}
