package com.silenteight.sens.webapp.backend.changerequest.domain;

import org.springframework.data.repository.Repository;

import java.util.List;

interface ChangeRequestRepository extends Repository<ChangeRequest, Long> {

  ChangeRequest getById(long id);

  ChangeRequest save(ChangeRequest changeRequest);

  List<ChangeRequest> findAllByState(ChangeRequestState state);
}
