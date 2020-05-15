package com.silenteight.sens.webapp.backend.changerequest.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

interface ChangeRequestRepository extends Repository<ChangeRequest, Long> {

  ChangeRequest save(ChangeRequest changeRequest);

  Optional<ChangeRequest> findByBulkChangeId(UUID bulkChangeId);
}
