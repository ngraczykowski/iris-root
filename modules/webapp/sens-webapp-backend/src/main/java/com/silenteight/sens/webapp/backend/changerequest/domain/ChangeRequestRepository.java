package com.silenteight.sens.webapp.backend.changerequest.domain;

import org.springframework.data.repository.Repository;

interface ChangeRequestRepository extends Repository<ChangeRequest, Long> {

  ChangeRequest save(ChangeRequest changeRequest);
}
