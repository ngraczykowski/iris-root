package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import org.springframework.data.repository.CrudRepository;

interface CrudDataRetentionJobAlertRepository
    extends CrudRepository<DataRetentionJobAlertEntity, Long> {}
