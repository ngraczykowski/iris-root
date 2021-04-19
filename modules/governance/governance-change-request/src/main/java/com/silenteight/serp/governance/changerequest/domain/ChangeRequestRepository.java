package com.silenteight.serp.governance.changerequest.domain;

import org.springframework.data.repository.Repository;

import java.util.Collection;

interface ChangeRequestRepository extends Repository<ChangeRequest, Long> {

  ChangeRequest save(ChangeRequest changeRequest);

  Collection<ChangeRequest> findAllByState(ChangeRequestState state);
}
