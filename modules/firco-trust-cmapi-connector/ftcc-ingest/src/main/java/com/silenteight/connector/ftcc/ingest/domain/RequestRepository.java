package com.silenteight.connector.ftcc.ingest.domain;

import org.springframework.data.repository.Repository;

interface RequestRepository extends Repository<RequestEntity, Long> {

  RequestEntity save(RequestEntity requestEntity);
}
