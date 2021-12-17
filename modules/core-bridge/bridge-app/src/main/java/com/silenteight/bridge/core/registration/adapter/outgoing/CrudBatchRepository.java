package com.silenteight.bridge.core.registration.adapter.outgoing;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface CrudBatchRepository extends CrudRepository<BatchEntity, Long> {

  Optional<BatchEntity> findByExternalId(String externalId);

}
