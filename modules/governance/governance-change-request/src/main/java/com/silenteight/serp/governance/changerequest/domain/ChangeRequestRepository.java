package com.silenteight.serp.governance.changerequest.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.*;

interface ChangeRequestRepository extends Repository<ChangeRequest, Long> {

  ChangeRequest save(ChangeRequest changeRequest);

  Collection<ChangeRequest> findAllByState(ChangeRequestState state);

  List<ChangeRequest> findAllByStateIn(Set<ChangeRequestState> states, Pageable pageable);

  Optional<ChangeRequest> findByChangeRequestId(UUID changeRequestId);
}
