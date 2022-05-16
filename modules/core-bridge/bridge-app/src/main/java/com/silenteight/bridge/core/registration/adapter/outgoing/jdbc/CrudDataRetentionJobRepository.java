package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import org.springframework.data.repository.CrudRepository;

interface CrudDataRetentionJobRepository extends CrudRepository<DataRetentionJobEntity, Long> {}
