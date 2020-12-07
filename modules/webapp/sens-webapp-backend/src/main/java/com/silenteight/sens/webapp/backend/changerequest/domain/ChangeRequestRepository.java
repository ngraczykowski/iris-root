package com.silenteight.sens.webapp.backend.changerequest.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface ChangeRequestRepository extends Repository<ChangeRequest, Long> {

  Optional<ChangeRequest> findById(long id);

  ChangeRequest save(ChangeRequest changeRequest);

  List<ChangeRequest> findAllByState(ChangeRequestState state);

  List<ChangeRequest> findAllByStateIn(Set<ChangeRequestState> states, Pageable pageable);
}
